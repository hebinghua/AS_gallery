package com.android.volley.toolbox;

import android.os.SystemClock;
import android.text.TextUtils;
import com.android.volley.Cache;
import com.android.volley.Header;
import com.android.volley.VolleyLog;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class DiskBasedCache implements Cache {
    public final int mMaxCacheSizeInBytes;
    public final File mRootDirectory;
    public final Map<String, CacheHeader> mEntries = new LinkedHashMap(16, 0.75f, true);
    public long mTotalSize = 0;

    public DiskBasedCache(File file, int i) {
        this.mRootDirectory = file;
        this.mMaxCacheSizeInBytes = i;
    }

    @Override // com.android.volley.Cache
    public synchronized Cache.Entry get(String str) {
        CacheHeader cacheHeader = this.mEntries.get(str);
        if (cacheHeader == null) {
            return null;
        }
        File fileForKey = getFileForKey(str);
        try {
            CountingInputStream countingInputStream = new CountingInputStream(new BufferedInputStream(createInputStream(fileForKey)), fileForKey.length());
            try {
                CacheHeader readHeader = CacheHeader.readHeader(countingInputStream);
                if (!TextUtils.equals(str, readHeader.key)) {
                    VolleyLog.d("%s: key=%s, found=%s", fileForKey.getAbsolutePath(), str, readHeader.key);
                    removeEntry(str);
                    return null;
                }
                return cacheHeader.toCacheEntry(streamToBytes(countingInputStream, countingInputStream.bytesRemaining()));
            } finally {
                countingInputStream.close();
            }
        } catch (IOException e) {
            VolleyLog.d("%s: %s", fileForKey.getAbsolutePath(), e.toString());
            remove(str);
            return null;
        }
    }

    @Override // com.android.volley.Cache
    public synchronized void initialize() {
        long length;
        CountingInputStream countingInputStream;
        if (!this.mRootDirectory.exists()) {
            if (!this.mRootDirectory.mkdirs()) {
                VolleyLog.e("Unable to create cache dir %s", this.mRootDirectory.getAbsolutePath());
            }
            return;
        }
        File[] listFiles = this.mRootDirectory.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File file : listFiles) {
            try {
                length = file.length();
                countingInputStream = new CountingInputStream(new BufferedInputStream(createInputStream(file)), length);
            } catch (IOException unused) {
                file.delete();
            }
            try {
                CacheHeader readHeader = CacheHeader.readHeader(countingInputStream);
                readHeader.size = length;
                putEntry(readHeader.key, readHeader);
                countingInputStream.close();
            } catch (Throwable th) {
                countingInputStream.close();
                throw th;
                break;
            }
        }
    }

    @Override // com.android.volley.Cache
    public synchronized void put(String str, Cache.Entry entry) {
        pruneIfNeeded(entry.data.length);
        File fileForKey = getFileForKey(str);
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(createOutputStream(fileForKey));
            CacheHeader cacheHeader = new CacheHeader(str, entry);
            if (!cacheHeader.writeHeader(bufferedOutputStream)) {
                bufferedOutputStream.close();
                VolleyLog.d("Failed to write header for %s", fileForKey.getAbsolutePath());
                throw new IOException();
            }
            bufferedOutputStream.write(entry.data);
            bufferedOutputStream.close();
            putEntry(str, cacheHeader);
        } catch (IOException unused) {
            if (fileForKey.delete()) {
                return;
            }
            VolleyLog.d("Could not clean up file %s", fileForKey.getAbsolutePath());
        }
    }

    public synchronized void remove(String str) {
        boolean delete = getFileForKey(str).delete();
        removeEntry(str);
        if (!delete) {
            VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
        }
    }

    public final String getFilenameForKey(String str) {
        int length = str.length() / 2;
        String valueOf = String.valueOf(str.substring(0, length).hashCode());
        return valueOf + String.valueOf(str.substring(length).hashCode());
    }

    public File getFileForKey(String str) {
        return new File(this.mRootDirectory, getFilenameForKey(str));
    }

    public final void pruneIfNeeded(int i) {
        long j;
        long j2 = i;
        if (this.mTotalSize + j2 < this.mMaxCacheSizeInBytes) {
            return;
        }
        if (VolleyLog.DEBUG) {
            VolleyLog.v("Pruning old cache entries.", new Object[0]);
        }
        long j3 = this.mTotalSize;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<Map.Entry<String, CacheHeader>> it = this.mEntries.entrySet().iterator();
        int i2 = 0;
        while (it.hasNext()) {
            CacheHeader value = it.next().getValue();
            if (getFileForKey(value.key).delete()) {
                j = j2;
                this.mTotalSize -= value.size;
            } else {
                j = j2;
                String str = value.key;
                VolleyLog.d("Could not delete cache entry for key=%s, filename=%s", str, getFilenameForKey(str));
            }
            it.remove();
            i2++;
            if (((float) (this.mTotalSize + j)) < this.mMaxCacheSizeInBytes * 0.9f) {
                break;
            }
            j2 = j;
        }
        if (!VolleyLog.DEBUG) {
            return;
        }
        VolleyLog.v("pruned %d files, %d bytes, %d ms", Integer.valueOf(i2), Long.valueOf(this.mTotalSize - j3), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
    }

    public final void putEntry(String str, CacheHeader cacheHeader) {
        if (!this.mEntries.containsKey(str)) {
            this.mTotalSize += cacheHeader.size;
        } else {
            this.mTotalSize += cacheHeader.size - this.mEntries.get(str).size;
        }
        this.mEntries.put(str, cacheHeader);
    }

    public final void removeEntry(String str) {
        CacheHeader remove = this.mEntries.remove(str);
        if (remove != null) {
            this.mTotalSize -= remove.size;
        }
    }

    public static byte[] streamToBytes(CountingInputStream countingInputStream, long j) throws IOException {
        long bytesRemaining = countingInputStream.bytesRemaining();
        if (j >= 0 && j <= bytesRemaining) {
            int i = (int) j;
            if (i == j) {
                byte[] bArr = new byte[i];
                new DataInputStream(countingInputStream).readFully(bArr);
                return bArr;
            }
        }
        throw new IOException("streamToBytes length=" + j + ", maxLength=" + bytesRemaining);
    }

    public InputStream createInputStream(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public OutputStream createOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    /* loaded from: classes.dex */
    public static class CacheHeader {
        public final List<Header> allResponseHeaders;
        public final String etag;
        public final String key;
        public final long lastModified;
        public final long serverDate;
        public long size;
        public final long softTtl;
        public final long ttl;

        public CacheHeader(String str, String str2, long j, long j2, long j3, long j4, List<Header> list) {
            this.key = str;
            this.etag = "".equals(str2) ? null : str2;
            this.serverDate = j;
            this.lastModified = j2;
            this.ttl = j3;
            this.softTtl = j4;
            this.allResponseHeaders = list;
        }

        public CacheHeader(String str, Cache.Entry entry) {
            this(str, entry.etag, entry.serverDate, entry.lastModified, entry.ttl, entry.softTtl, getAllResponseHeaders(entry));
            this.size = entry.data.length;
        }

        public static List<Header> getAllResponseHeaders(Cache.Entry entry) {
            List<Header> list = entry.allResponseHeaders;
            return list != null ? list : HttpHeaderParser.toAllHeaderList(entry.responseHeaders);
        }

        public static CacheHeader readHeader(CountingInputStream countingInputStream) throws IOException {
            if (DiskBasedCache.readInt(countingInputStream) != 538247942) {
                throw new IOException();
            }
            return new CacheHeader(DiskBasedCache.readString(countingInputStream), DiskBasedCache.readString(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readLong(countingInputStream), DiskBasedCache.readHeaderList(countingInputStream));
        }

        public Cache.Entry toCacheEntry(byte[] bArr) {
            Cache.Entry entry = new Cache.Entry();
            entry.data = bArr;
            entry.etag = this.etag;
            entry.serverDate = this.serverDate;
            entry.lastModified = this.lastModified;
            entry.ttl = this.ttl;
            entry.softTtl = this.softTtl;
            entry.responseHeaders = HttpHeaderParser.toHeaderMap(this.allResponseHeaders);
            entry.allResponseHeaders = Collections.unmodifiableList(this.allResponseHeaders);
            return entry;
        }

        public boolean writeHeader(OutputStream outputStream) {
            try {
                DiskBasedCache.writeInt(outputStream, 538247942);
                DiskBasedCache.writeString(outputStream, this.key);
                String str = this.etag;
                if (str == null) {
                    str = "";
                }
                DiskBasedCache.writeString(outputStream, str);
                DiskBasedCache.writeLong(outputStream, this.serverDate);
                DiskBasedCache.writeLong(outputStream, this.lastModified);
                DiskBasedCache.writeLong(outputStream, this.ttl);
                DiskBasedCache.writeLong(outputStream, this.softTtl);
                DiskBasedCache.writeHeaderList(this.allResponseHeaders, outputStream);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                VolleyLog.d("%s", e.toString());
                return false;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class CountingInputStream extends FilterInputStream {
        public long bytesRead;
        public final long length;

        public CountingInputStream(InputStream inputStream, long j) {
            super(inputStream);
            this.length = j;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int read = super.read();
            if (read != -1) {
                this.bytesRead++;
            }
            return read;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i, int i2) throws IOException {
            int read = super.read(bArr, i, i2);
            if (read != -1) {
                this.bytesRead += read;
            }
            return read;
        }

        public long bytesRemaining() {
            return this.length - this.bytesRead;
        }
    }

    public static int read(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    public static void writeInt(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 24) & 255);
    }

    public static int readInt(InputStream inputStream) throws IOException {
        return (read(inputStream) << 24) | (read(inputStream) << 0) | 0 | (read(inputStream) << 8) | (read(inputStream) << 16);
    }

    public static void writeLong(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) (j >>> 0));
        outputStream.write((byte) (j >>> 8));
        outputStream.write((byte) (j >>> 16));
        outputStream.write((byte) (j >>> 24));
        outputStream.write((byte) (j >>> 32));
        outputStream.write((byte) (j >>> 40));
        outputStream.write((byte) (j >>> 48));
        outputStream.write((byte) (j >>> 56));
    }

    public static long readLong(InputStream inputStream) throws IOException {
        return ((read(inputStream) & 255) << 0) | 0 | ((read(inputStream) & 255) << 8) | ((read(inputStream) & 255) << 16) | ((read(inputStream) & 255) << 24) | ((read(inputStream) & 255) << 32) | ((read(inputStream) & 255) << 40) | ((read(inputStream) & 255) << 48) | ((255 & read(inputStream)) << 56);
    }

    public static void writeString(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes(Keyczar.DEFAULT_ENCODING);
        writeLong(outputStream, bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    public static String readString(CountingInputStream countingInputStream) throws IOException {
        return new String(streamToBytes(countingInputStream, readLong(countingInputStream)), Keyczar.DEFAULT_ENCODING);
    }

    public static void writeHeaderList(List<Header> list, OutputStream outputStream) throws IOException {
        if (list != null) {
            writeInt(outputStream, list.size());
            for (Header header : list) {
                writeString(outputStream, header.getName());
                writeString(outputStream, header.getValue());
            }
            return;
        }
        writeInt(outputStream, 0);
    }

    public static List<Header> readHeaderList(CountingInputStream countingInputStream) throws IOException {
        int readInt = readInt(countingInputStream);
        if (readInt < 0) {
            throw new IOException("readHeaderList size=" + readInt);
        }
        List<Header> emptyList = readInt == 0 ? Collections.emptyList() : new ArrayList<>();
        for (int i = 0; i < readInt; i++) {
            emptyList.add(new Header(readString(countingInputStream).intern(), readString(countingInputStream).intern()));
        }
        return emptyList;
    }
}
