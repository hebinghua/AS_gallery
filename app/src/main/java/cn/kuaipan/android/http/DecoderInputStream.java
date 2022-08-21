package cn.kuaipan.android.http;

import cn.kuaipan.android.utils.RandomInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Objects;
import java.util.zip.DataFormatException;

/* loaded from: classes.dex */
public class DecoderInputStream extends FilterInputStream {
    public final byte[] buf;
    public boolean closed;
    public final IKscDecoder decoder;
    public boolean eof;
    public long markPos;
    public final long startPos;

    public DecoderInputStream(InputStream inputStream, IKscDecoder iKscDecoder) {
        this(inputStream, iKscDecoder, 512);
    }

    public DecoderInputStream(InputStream inputStream, IKscDecoder iKscDecoder, int i) {
        super(inputStream);
        this.markPos = -1L;
        if (inputStream == null || iKscDecoder == null) {
            throw null;
        }
        if (i <= 0) {
            throw new IllegalArgumentException();
        }
        iKscDecoder.init();
        this.decoder = iKscDecoder;
        this.buf = new byte[i];
        if (((FilterInputStream) this).in instanceof RandomInputStream) {
            this.startPos = ((RandomInputStream) ((FilterInputStream) this).in).getCurrentPos();
        } else {
            this.startPos = 0L;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        byte[] bArr = new byte[1];
        if (read(bArr, 0, 1) == -1) {
            return -1;
        }
        return bArr[0] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        Objects.requireNonNull(bArr);
        if (i < 0 || i2 < 0 || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 == 0) {
            return 0;
        }
        if (i > bArr.length || i2 < 0 || i < 0 || bArr.length - i < i2) {
            throw new ArrayIndexOutOfBoundsException();
        }
        try {
            fill();
            int readDecodeData = this.decoder.readDecodeData(bArr, i, i2);
            if (readDecodeData > 0) {
                return readDecodeData;
            }
            if (this.eof && this.decoder.canEnd()) {
                return -1;
            }
            if (this.eof) {
                throw new EOFException();
            }
            throw ((IOException) new IOException().initCause(new DataFormatException("Failed read data from decoder.")));
        } catch (DataFormatException e) {
            if (this.eof) {
                throw new EOFException();
            }
            throw ((IOException) new IOException().initCause(e));
        }
    }

    public final void fill() throws IOException, DataFormatException {
        int needFill;
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        int i = Integer.MAX_VALUE;
        while (!this.eof && (needFill = this.decoder.needFill()) > 0) {
            if (needFill > i) {
                throw new DataFormatException("Needed data is increased");
            }
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }
            int read = ((FilterInputStream) this).in.read(this.buf, 0, Math.min(needFill, this.buf.length));
            if (read >= 0) {
                this.decoder.fillData(this.buf, 0, read);
            } else {
                this.eof = true;
            }
            i = needFill;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j) throws IOException {
        long j2 = 0;
        if (j >= 0) {
            while (j2 < j) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedIOException();
                }
                byte[] bArr = this.buf;
                long j3 = j - j2;
                int read = read(bArr, 0, j3 > ((long) bArr.length) ? bArr.length : (int) j3);
                if (read == -1) {
                    return j2;
                }
                j2 += read;
            }
            return j2;
        }
        throw new IllegalArgumentException();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (!this.closed) {
            return this.eof ? 0 : 1;
        }
        throw new IOException("Stream is closed");
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            try {
                this.decoder.end();
            } finally {
                this.closed = true;
                this.eof = true;
                super.close();
            }
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i) {
        if (((FilterInputStream) this).in instanceof RandomInputStream) {
            this.markPos = ((RandomInputStream) ((FilterInputStream) this).in).getCurrentPos();
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        if (!markSupported()) {
            throw new IOException();
        }
        if (this.markPos < 0) {
            return;
        }
        if (((RandomInputStream) ((FilterInputStream) this).in).getCurrentPos() > this.markPos) {
            this.eof = false;
        }
        ((RandomInputStream) ((FilterInputStream) this).in).moveToPos(this.markPos);
        this.decoder.init();
        this.decoder.skip(this.markPos - this.startPos);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return (((FilterInputStream) this).in instanceof RandomInputStream) && this.decoder.supportRepeat();
    }
}
