package cn.kuaipan.android.utils;

import cn.kuaipan.android.kss.upload.UploadDescriptorFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class RandomFileDescriptorInputStream extends RandomInputStream {
    public FileInputStream mFileInputStream;
    public final UploadDescriptorFile uploadFile;
    public long mPos = 0;
    public long mMarkedPos = 0;
    public boolean mClosed = false;

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    public RandomFileDescriptorInputStream(UploadDescriptorFile uploadDescriptorFile) throws IOException {
        this.uploadFile = uploadDescriptorFile;
        this.mFileInputStream = uploadDescriptorFile.openFileInputStream();
    }

    @Override // java.io.InputStream
    public synchronized int read() throws IOException {
        int read;
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
        }
        read = this.mFileInputStream.getChannel().read(ByteBuffer.allocate(1));
        if (read >= 0) {
            this.mPos++;
        }
        return read;
    }

    @Override // java.io.InputStream
    public synchronized int available() throws IOException {
        return (int) (this.uploadFile.fileSize - this.mPos);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        this.mFileInputStream.close();
        this.mClosed = true;
    }

    @Override // cn.kuaipan.android.utils.RandomInputStream
    public synchronized void moveToPos(long j) throws IOException {
        if (j < this.uploadFile.fileSize) {
            this.mFileInputStream.getChannel().position(j);
            this.mPos = j;
        } else {
            throw new IOException("Seek position is not available");
        }
    }

    @Override // cn.kuaipan.android.utils.RandomInputStream
    public synchronized long getCurrentPos() {
        return this.mPos;
    }

    @Override // java.io.InputStream
    public void mark(int i) {
        if (!this.mClosed) {
            this.mMarkedPos = this.mPos;
        }
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr) throws IOException {
        int read;
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
        }
        read = this.mFileInputStream.getChannel().read(ByteBuffer.wrap(bArr));
        if (read > 0) {
            this.mPos += read;
        }
        return read;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
        int read;
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
        }
        byte[] bArr2 = new byte[i2];
        read = this.mFileInputStream.getChannel().read(ByteBuffer.wrap(bArr2));
        System.arraycopy(bArr2, 0, bArr, i, i2);
        if (read > 0) {
            this.mPos += read;
        }
        return read;
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        if (this.mClosed) {
            this.mClosed = false;
            this.mFileInputStream = this.uploadFile.openFileInputStream();
        }
        this.mFileInputStream.getChannel().position(this.mMarkedPos);
        this.mPos = this.mMarkedPos;
    }

    @Override // java.io.InputStream
    public long skip(long j) throws IOException {
        int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
        if (i == 0) {
            return 0L;
        }
        if (i < 0) {
            throw new IOException("Number of bytes to skip cannot be negative");
        }
        long j2 = this.uploadFile.fileSize;
        long j3 = this.mPos;
        if (j2 - j3 < j) {
            this.mPos = j2;
            this.mFileInputStream.getChannel().position(this.mPos);
            return this.uploadFile.fileSize - this.mPos;
        }
        this.mPos = j3 + j;
        this.mFileInputStream.getChannel().position(this.mPos);
        return j;
    }
}
