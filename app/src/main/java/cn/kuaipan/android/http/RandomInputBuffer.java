package cn.kuaipan.android.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/* loaded from: classes.dex */
public class RandomInputBuffer {
    public byte[] buf;
    public File bufFile;
    public final NetCacheManager cache;
    public boolean eof;
    public RandomAccessFile file;
    public boolean fileAssigned;
    public InputStream in;
    public int pos;

    public RandomInputBuffer(InputStream inputStream, NetCacheManager netCacheManager) {
        if (netCacheManager == null || inputStream == null) {
            throw new IllegalArgumentException("InputStream & NetCacheManager can not be null.");
        }
        this.in = inputStream;
        this.cache = netCacheManager;
        this.buf = new byte[8192];
        this.pos = 0;
        this.eof = false;
    }

    public synchronized int available(int i) throws IOException {
        int i2;
        InputStream inputStream = this.in;
        if (inputStream == null) {
            throw new IOException("RandomInputBuffer has been closed.");
        }
        if (!this.eof) {
            i2 = inputStream.available() + this.pos;
        } else {
            i2 = this.pos;
        }
        return i2 - i;
    }

    public synchronized void close() throws IOException {
        InputStream inputStream = this.in;
        if (inputStream != null) {
            inputStream.close();
            this.in = null;
        }
        RandomAccessFile randomAccessFile = this.file;
        if (randomAccessFile != null) {
            randomAccessFile.close();
            this.file = null;
        }
        File file = this.bufFile;
        if (file != null) {
            this.cache.releaseCache(file);
            this.bufFile = null;
        }
        this.buf = null;
        this.pos = 0;
        this.eof = false;
    }

    public int read(int i) throws IOException {
        byte[] bArr = new byte[1];
        if (read(i, bArr) > 0) {
            return bArr[0];
        }
        return -1;
    }

    public int read(int i, byte[] bArr) throws IOException {
        return read(i, bArr, 0, bArr.length);
    }

    public synchronized int read(int i, byte[] bArr, int i2, int i3) throws IOException {
        if (this.in != null) {
            if (bArr == null) {
                throw new NullPointerException("buffer == null");
            }
            if ((i2 | i3) < 0 || i2 > bArr.length - i3) {
                throw new IndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return 0;
            }
            int i4 = i + i3;
            if (i4 > this.pos) {
                fillBuf(i4);
            }
            return readFromBuf(i, bArr, i2, i3);
        }
        throw new IOException("RandomInputBuffer has been closed.");
    }

    public final int readFromBuf(int i, byte[] bArr, int i2, int i3) throws IOException {
        if (i3 == 0) {
            return 0;
        }
        if (this.eof && i >= this.pos) {
            return -1;
        }
        int i4 = this.pos;
        if (i > i4) {
            throw new IndexOutOfBoundsException("from > pos");
        }
        if (i == i4) {
            return 0;
        }
        int min = Math.min(i4 - i, i3);
        RandomAccessFile randomAccessFile = this.file;
        if (randomAccessFile == null) {
            System.arraycopy(this.buf, i, bArr, i2, min);
            return min;
        }
        randomAccessFile.seek(i);
        return this.file.read(bArr, i2, min);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0083  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void fillBuf(int r7) throws java.io.IOException {
        /*
            r6 = this;
            boolean r0 = r6.eof
            if (r0 != 0) goto Lac
            int r0 = r6.pos
            if (r7 > r0) goto La
            goto Lac
        La:
            int r0 = r7 % 8192
            if (r0 != 0) goto Lf
            goto L15
        Lf:
            int r7 = r7 / 8192
            int r7 = r7 * 8192
            int r7 = r7 + 8192
        L15:
            boolean r0 = r6.fileAssigned
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L4e
            byte[] r0 = r6.buf
            int r0 = r0.length
            if (r7 <= r0) goto L4e
            r6.fileAssigned = r1
            cn.kuaipan.android.http.NetCacheManager r0 = r6.cache     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            java.io.File r0 = r0.assignCache()     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            r6.bufFile = r0     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            if (r0 == 0) goto L4e
            java.io.RandomAccessFile r0 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            java.io.File r3 = r6.bufFile     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            java.lang.String r4 = "rw"
            r0.<init>(r3, r4)     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            r6.file = r0     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            byte[] r3 = r6.buf     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            int r4 = r6.pos     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            r0.write(r3, r2, r4)     // Catch: java.lang.Exception -> L3f java.io.InterruptedIOException -> L4c
            goto L4e
        L3f:
            java.io.RandomAccessFile r0 = r6.file     // Catch: java.lang.Exception -> L46 java.io.InterruptedIOException -> L4a
            if (r0 == 0) goto L46
            r0.close()     // Catch: java.lang.Exception -> L46 java.io.InterruptedIOException -> L4a
        L46:
            r0 = 0
            r6.file = r0
            goto L4e
        L4a:
            r7 = move-exception
            throw r7
        L4c:
            r7 = move-exception
            throw r7
        L4e:
            java.io.RandomAccessFile r0 = r6.file
            if (r0 != 0) goto L83
            byte[] r0 = r6.buf
            int r3 = r0.length
            if (r7 <= r3) goto L67
            int r0 = r0.length
        L58:
            int r0 = r0 * 2
            if (r0 >= r7) goto L5d
            goto L58
        L5d:
            byte[] r0 = new byte[r0]
            byte[] r3 = r6.buf
            int r4 = r3.length
            java.lang.System.arraycopy(r3, r2, r0, r2, r4)
            r6.buf = r0
        L67:
            java.io.InputStream r0 = r6.in
            byte[] r2 = r6.buf
            int r3 = r6.pos
            int r4 = r2.length
            int r4 = r4 - r3
            int r7 = r7 - r3
            int r7 = java.lang.Math.min(r4, r7)
            int r7 = r0.read(r2, r3, r7)
            if (r7 < 0) goto L80
            int r0 = r6.pos
            int r0 = r0 + r7
            r6.pos = r0
            goto Lac
        L80:
            r6.eof = r1
            goto Lac
        L83:
            java.io.InputStream r0 = r6.in
            byte[] r3 = r6.buf
            int r4 = r3.length
            int r5 = r6.pos
            int r7 = r7 - r5
            int r7 = java.lang.Math.min(r4, r7)
            int r7 = r0.read(r3, r2, r7)
            if (r7 < 0) goto Laa
            java.io.RandomAccessFile r0 = r6.file
            int r1 = r6.pos
            long r3 = (long) r1
            r0.seek(r3)
            java.io.RandomAccessFile r0 = r6.file
            byte[] r1 = r6.buf
            r0.write(r1, r2, r7)
            int r0 = r6.pos
            int r0 = r0 + r7
            r6.pos = r0
            goto Lac
        Laa:
            r6.eof = r1
        Lac:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuaipan.android.http.RandomInputBuffer.fillBuf(int):void");
    }
}
