package com.miui.gallery.util.uil;

import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.Adler32;

/* loaded from: classes2.dex */
public class BlobCache implements Closeable {
    public int mActiveBytes;
    public RandomAccessFile mActiveDataFile;
    public int mActiveEntries;
    public int mActiveHashStart;
    public int mActiveRegion;
    public RandomAccessFile mDataFile0;
    public RandomAccessFile mDataFile1;
    public int mFileOffset;
    public RandomAccessFile mInactiveDataFile;
    public int mInactiveHashStart;
    public MappedByteBuffer mIndexBuffer;
    public FileChannel mIndexChannel;
    public RandomAccessFile mIndexFile;
    public int mMaxBytes;
    public int mMaxEntries;
    public int mSlotOffset;
    public int mVersion;
    public byte[] mIndexHeader = new byte[32];
    public byte[] mBlobHeader = new byte[20];
    public Adler32 mAdler32 = new Adler32();
    public LookupRequest mLookupRequest = new LookupRequest();

    /* loaded from: classes2.dex */
    public static class LookupRequest {
        public byte[] buffer;
        public long key;
        public int length;
    }

    public BlobCache(String str, int i, int i2, boolean z, int i3) throws IOException {
        this.mIndexFile = new RandomAccessFile(str + ".idx", "rw");
        this.mDataFile0 = new RandomAccessFile(str + ".0", "rw");
        this.mDataFile1 = new RandomAccessFile(str + ".1", "rw");
        this.mVersion = i3;
        if (z || !loadIndex()) {
            resetCache(i, i2);
            if (loadIndex()) {
                return;
            }
            closeAll();
            throw new IOException("unable to load index");
        }
    }

    public static void deleteFiles(String str) {
        deleteFileSilently(str + ".idx");
        deleteFileSilently(str + ".0");
        deleteFileSilently(str + ".1");
    }

    public static void deleteFileSilently(String str) {
        try {
            new File(str).delete();
        } catch (Throwable unused) {
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        syncAll();
        closeAll();
    }

    public final void closeAll() {
        closeSilently(this.mIndexChannel);
        closeSilently(this.mIndexFile);
        closeSilently(this.mDataFile0);
        closeSilently(this.mDataFile1);
    }

    public final boolean loadIndex() {
        try {
            this.mIndexFile.seek(0L);
            this.mDataFile0.seek(0L);
            this.mDataFile1.seek(0L);
            byte[] bArr = this.mIndexHeader;
            if (this.mIndexFile.read(bArr) != 32) {
                Log.w("BlobCache", "cannot read header");
                return false;
            } else if (readInt(bArr, 0) != -1289277392) {
                Log.w("BlobCache", "cannot read header magic");
                return false;
            } else if (readInt(bArr, 24) != this.mVersion) {
                Log.w("BlobCache", "version mismatch");
                return false;
            } else {
                this.mMaxEntries = readInt(bArr, 4);
                this.mMaxBytes = readInt(bArr, 8);
                this.mActiveRegion = readInt(bArr, 12);
                this.mActiveEntries = readInt(bArr, 16);
                this.mActiveBytes = readInt(bArr, 20);
                if (checkSum(bArr, 0, 28) != readInt(bArr, 28)) {
                    Log.w("BlobCache", "header checksum does not match");
                    return false;
                }
                int i = this.mMaxEntries;
                if (i <= 0) {
                    Log.w("BlobCache", "invalid max entries");
                    return false;
                }
                int i2 = this.mMaxBytes;
                if (i2 <= 0) {
                    Log.w("BlobCache", "invalid max bytes");
                    return false;
                }
                int i3 = this.mActiveRegion;
                if (i3 != 0 && i3 != 1) {
                    Log.w("BlobCache", "invalid active region");
                    return false;
                }
                int i4 = this.mActiveEntries;
                if (i4 >= 0 && i4 <= i) {
                    int i5 = this.mActiveBytes;
                    if (i5 >= 4 && i5 <= i2) {
                        if (this.mIndexFile.length() != (this.mMaxEntries * 12 * 2) + 32) {
                            Log.w("BlobCache", "invalid index file length");
                            return false;
                        }
                        byte[] bArr2 = new byte[4];
                        if (this.mDataFile0.read(bArr2) != 4) {
                            Log.w("BlobCache", "cannot read data file magic");
                            return false;
                        } else if (readInt(bArr2, 0) != -1121680112) {
                            Log.w("BlobCache", "invalid data file magic");
                            return false;
                        } else if (this.mDataFile1.read(bArr2) != 4) {
                            Log.w("BlobCache", "cannot read data file magic");
                            return false;
                        } else if (readInt(bArr2, 0) != -1121680112) {
                            Log.w("BlobCache", "invalid data file magic");
                            return false;
                        } else {
                            FileChannel channel = this.mIndexFile.getChannel();
                            this.mIndexChannel = channel;
                            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0L, this.mIndexFile.length());
                            this.mIndexBuffer = map;
                            map.order(ByteOrder.LITTLE_ENDIAN);
                            setActiveVariables();
                            return true;
                        }
                    }
                    Log.w("BlobCache", "invalid active bytes");
                    return false;
                }
                Log.w("BlobCache", "invalid active entries");
                return false;
            }
        } catch (IOException e) {
            Log.e("BlobCache", "loadIndex failed.", e);
            return false;
        }
    }

    public final void setActiveVariables() throws IOException {
        int i = this.mActiveRegion;
        RandomAccessFile randomAccessFile = i == 0 ? this.mDataFile0 : this.mDataFile1;
        this.mActiveDataFile = randomAccessFile;
        this.mInactiveDataFile = i == 1 ? this.mDataFile0 : this.mDataFile1;
        randomAccessFile.setLength(this.mActiveBytes);
        this.mActiveDataFile.seek(this.mActiveBytes);
        this.mActiveHashStart = 32;
        this.mInactiveHashStart = 32;
        if (this.mActiveRegion == 0) {
            this.mInactiveHashStart = 32 + (this.mMaxEntries * 12);
        } else {
            this.mActiveHashStart = 32 + (this.mMaxEntries * 12);
        }
    }

    public final void resetCache(int i, int i2) throws IOException {
        this.mIndexFile.setLength(0L);
        this.mIndexFile.setLength((i * 12 * 2) + 32);
        this.mIndexFile.seek(0L);
        byte[] bArr = this.mIndexHeader;
        writeInt(bArr, 0, -1289277392);
        writeInt(bArr, 4, i);
        writeInt(bArr, 8, i2);
        writeInt(bArr, 12, 0);
        writeInt(bArr, 16, 0);
        writeInt(bArr, 20, 4);
        writeInt(bArr, 24, this.mVersion);
        writeInt(bArr, 28, checkSum(bArr, 0, 28));
        this.mIndexFile.write(bArr);
        this.mDataFile0.setLength(0L);
        this.mDataFile1.setLength(0L);
        this.mDataFile0.seek(0L);
        this.mDataFile1.seek(0L);
        writeInt(bArr, 0, -1121680112);
        this.mDataFile0.write(bArr, 0, 4);
        this.mDataFile1.write(bArr, 0, 4);
    }

    public final void flipRegion() throws IOException {
        int i = 1 - this.mActiveRegion;
        this.mActiveRegion = i;
        this.mActiveEntries = 0;
        this.mActiveBytes = 4;
        writeInt(this.mIndexHeader, 12, i);
        writeInt(this.mIndexHeader, 16, this.mActiveEntries);
        writeInt(this.mIndexHeader, 20, this.mActiveBytes);
        updateIndexHeader();
        setActiveVariables();
        clearHash(this.mActiveHashStart);
        syncIndex();
    }

    public final void updateIndexHeader() {
        byte[] bArr = this.mIndexHeader;
        writeInt(bArr, 28, checkSum(bArr, 0, 28));
        this.mIndexBuffer.position(0);
        this.mIndexBuffer.put(this.mIndexHeader);
    }

    public final void clearHash(int i) {
        byte[] bArr = new byte[1024];
        this.mIndexBuffer.position(i);
        int i2 = this.mMaxEntries * 12;
        while (i2 > 0) {
            int min = Math.min(i2, 1024);
            this.mIndexBuffer.put(bArr, 0, min);
            i2 -= min;
        }
    }

    public void insert(long j, byte[] bArr) throws IOException {
        int length = bArr.length + 24;
        int i = this.mMaxBytes;
        if (length > i) {
            throw new RuntimeException("blob is too large!");
        }
        if (this.mActiveBytes + 20 + bArr.length > i || this.mActiveEntries * 2 >= this.mMaxEntries) {
            flipRegion();
        }
        if (!lookupInternal(j, this.mActiveHashStart)) {
            int i2 = this.mActiveEntries + 1;
            this.mActiveEntries = i2;
            writeInt(this.mIndexHeader, 16, i2);
        }
        insertInternal(j, bArr, bArr.length);
        updateIndexHeader();
    }

    public final void insertInternal(long j, byte[] bArr, int i) throws IOException {
        byte[] bArr2 = this.mBlobHeader;
        int checkSum = checkSum(bArr);
        writeLong(bArr2, 0, j);
        writeInt(bArr2, 8, checkSum);
        writeInt(bArr2, 12, this.mActiveBytes);
        writeInt(bArr2, 16, i);
        this.mActiveDataFile.write(bArr2);
        this.mActiveDataFile.write(bArr, 0, i);
        this.mIndexBuffer.putLong(this.mSlotOffset, j);
        this.mIndexBuffer.putInt(this.mSlotOffset + 8, this.mActiveBytes);
        int i2 = this.mActiveBytes + i + 20;
        this.mActiveBytes = i2;
        writeInt(this.mIndexHeader, 20, i2);
    }

    public byte[] lookup(long j) throws IOException {
        try {
            LookupRequest lookupRequest = this.mLookupRequest;
            lookupRequest.key = j;
            if (!lookup(lookupRequest)) {
                return null;
            }
            LookupRequest lookupRequest2 = this.mLookupRequest;
            byte[] bArr = lookupRequest2.buffer;
            lookupRequest2.buffer = null;
            return bArr;
        } finally {
            this.mLookupRequest.buffer = null;
        }
    }

    public boolean lookup(LookupRequest lookupRequest) throws IOException {
        if (!lookupInternal(lookupRequest.key, this.mActiveHashStart) || !getBlob(this.mActiveDataFile, this.mFileOffset, lookupRequest)) {
            int i = this.mSlotOffset;
            if (!lookupInternal(lookupRequest.key, this.mInactiveHashStart) || !getBlob(this.mInactiveDataFile, this.mFileOffset, lookupRequest)) {
                return false;
            }
            int i2 = lookupRequest.length;
            if (this.mActiveBytes + 20 + i2 <= this.mMaxBytes && this.mActiveEntries * 2 < this.mMaxEntries) {
                this.mSlotOffset = i;
                try {
                    insertInternal(lookupRequest.key, lookupRequest.buffer, i2);
                    int i3 = this.mActiveEntries + 1;
                    this.mActiveEntries = i3;
                    writeInt(this.mIndexHeader, 16, i3);
                    updateIndexHeader();
                } catch (Throwable unused) {
                    Log.e("BlobCache", "cannot copy over");
                }
            }
            return true;
        }
        return true;
    }

    public final boolean getBlob(RandomAccessFile randomAccessFile, int i, LookupRequest lookupRequest) throws IOException {
        byte[] bArr = this.mBlobHeader;
        long filePointer = randomAccessFile.getFilePointer();
        try {
            randomAccessFile.seek(i);
            if (randomAccessFile.read(bArr) != 20) {
                Log.w("BlobCache", "cannot read blob header");
                return false;
            }
            long readLong = readLong(bArr, 0);
            if (readLong == 0) {
                return false;
            }
            if (readLong != lookupRequest.key) {
                Log.w("BlobCache", "blob key does not match: " + readLong);
                return false;
            }
            int readInt = readInt(bArr, 8);
            int readInt2 = readInt(bArr, 12);
            if (readInt2 != i) {
                Log.w("BlobCache", "blob offset does not match: " + readInt2);
                return false;
            }
            int readInt3 = readInt(bArr, 16);
            if (readInt3 >= 0 && readInt3 <= (this.mMaxBytes - i) - 20) {
                byte[] bArr2 = lookupRequest.buffer;
                if (bArr2 == null || bArr2.length < readInt3) {
                    lookupRequest.buffer = new byte[readInt3];
                }
                byte[] bArr3 = lookupRequest.buffer;
                lookupRequest.length = readInt3;
                if (randomAccessFile.read(bArr3, 0, readInt3) != readInt3) {
                    Log.w("BlobCache", "cannot read blob data");
                    return false;
                } else if (checkSum(bArr3, 0, readInt3) == readInt) {
                    return true;
                } else {
                    Log.w("BlobCache", "blob checksum does not match: " + readInt);
                    return false;
                }
            }
            Log.w("BlobCache", "invalid blob length: " + readInt3);
            return false;
        } catch (Throwable th) {
            try {
                Log.e("BlobCache", "getBlob failed.", th);
                return false;
            } finally {
                randomAccessFile.seek(filePointer);
            }
        }
    }

    public final boolean lookupInternal(long j, int i) {
        int i2 = this.mMaxEntries;
        int i3 = (int) (j % i2);
        if (i3 < 0) {
            i3 += i2;
        }
        int i4 = i3;
        while (true) {
            int i5 = (i4 * 12) + i;
            long j2 = this.mIndexBuffer.getLong(i5);
            int i6 = this.mIndexBuffer.getInt(i5 + 8);
            if (i6 == 0) {
                this.mSlotOffset = i5;
                return false;
            } else if (j2 == j) {
                this.mSlotOffset = i5;
                this.mFileOffset = i6;
                return true;
            } else {
                i4++;
                if (i4 >= this.mMaxEntries) {
                    i4 = 0;
                }
                if (i4 == i3) {
                    Log.w("BlobCache", "corrupted index: clear the slot.");
                    this.mIndexBuffer.putInt((i4 * 12) + i + 8, 0);
                }
            }
        }
    }

    public void syncIndex() {
        try {
            this.mIndexBuffer.force();
        } catch (Throwable th) {
            Log.w("BlobCache", "sync index failed", th);
        }
    }

    public void syncAll() {
        syncIndex();
        try {
            this.mDataFile0.getFD().sync();
        } catch (Throwable th) {
            Log.w("BlobCache", "sync data file 0 failed", th);
        }
        try {
            this.mDataFile1.getFD().sync();
        } catch (Throwable th2) {
            Log.w("BlobCache", "sync data file 1 failed", th2);
        }
    }

    public int checkSum(byte[] bArr) {
        this.mAdler32.reset();
        this.mAdler32.update(bArr);
        return (int) this.mAdler32.getValue();
    }

    public int checkSum(byte[] bArr, int i, int i2) {
        this.mAdler32.reset();
        this.mAdler32.update(bArr, i, i2);
        return (int) this.mAdler32.getValue();
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Throwable unused) {
        }
    }

    public static int readInt(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16);
    }

    public static long readLong(byte[] bArr, int i) {
        long j = bArr[i + 7] & 255;
        for (int i2 = 6; i2 >= 0; i2--) {
            j = (j << 8) | (bArr[i + i2] & 255);
        }
        return j;
    }

    public static void writeInt(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < 4; i3++) {
            bArr[i + i3] = (byte) (i2 & 255);
            i2 >>= 8;
        }
    }

    public static void writeLong(byte[] bArr, int i, long j) {
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i + i2] = (byte) (255 & j);
            j >>= 8;
        }
    }
}
