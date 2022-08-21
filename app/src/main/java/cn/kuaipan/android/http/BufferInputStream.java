package cn.kuaipan.android.http;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class BufferInputStream extends InputStream {
    public static final byte[] tempBuf = new byte[8192];
    public RandomInputBuffer buf;
    public int markpos = -1;
    public int pos;

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    public BufferInputStream(RandomInputBuffer randomInputBuffer) {
        this.buf = randomInputBuffer;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        RandomInputBuffer randomInputBuffer = this.buf;
        if (randomInputBuffer == null) {
            throw streamClosed();
        }
        return randomInputBuffer.available(this.pos);
    }

    public final IOException streamClosed() throws IOException {
        throw new IOException("BufferedInputStream is closed");
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.buf = null;
    }

    @Override // java.io.InputStream
    public synchronized void mark(int i) {
        this.markpos = this.pos;
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        int read;
        RandomInputBuffer randomInputBuffer = this.buf;
        if (randomInputBuffer == null) {
            throw streamClosed();
        }
        read = randomInputBuffer.read(this.pos);
        if (read >= 0) {
            this.pos++;
        }
        return read;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
        int read;
        RandomInputBuffer randomInputBuffer = this.buf;
        if (randomInputBuffer == null) {
            throw streamClosed();
        }
        read = randomInputBuffer.read(this.pos, bArr, i, i2);
        if (read > 0) {
            this.pos += read;
        }
        return read;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        if (this.buf == null) {
            throw new IOException("Stream is closed");
        }
        int i = this.markpos;
        if (-1 == i) {
            throw new IOException("Mark has been invalidated.");
        }
        this.pos = i;
    }

    @Override // java.io.InputStream
    public synchronized long skip(long j) throws IOException {
        byte[] bArr;
        if (j < 1) {
            return 0L;
        }
        RandomInputBuffer randomInputBuffer = this.buf;
        if (randomInputBuffer == null) {
            throw streamClosed();
        }
        long j2 = this.pos + j;
        int i = 0;
        while (true) {
            int i2 = this.pos;
            if (j2 <= i2) {
                break;
            }
            int read = randomInputBuffer.read(i2, tempBuf, 0, (int) Math.min(bArr.length, j2 - i2));
            if (read < 0) {
                break;
            }
            this.pos += read;
            i += read;
        }
        return i;
    }
}
