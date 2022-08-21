package cn.kuaipan.android.kss.download;

import cn.kuaipan.android.utils.Encode;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public class KssFileDescriptorAccessor implements KssAccessor {
    public FileInputStream mFileInputStream;
    public FileLock mFileLocker;
    public FileOutputStream mFileOutputStream;
    public boolean mClosed = false;
    public final ReentrantLock mLocker = new ReentrantLock();

    @Override // cn.kuaipan.android.kss.download.KssAccessor
    public void inflate(long j) throws IOException {
    }

    public KssFileDescriptorAccessor(DownloadDescriptorFile downloadDescriptorFile) throws IOException {
        this.mFileInputStream = downloadDescriptorFile.openFileInputStream();
        FileOutputStream openFileOutputStream = downloadDescriptorFile.openFileOutputStream();
        this.mFileOutputStream = openFileOutputStream;
        this.mFileLocker = openFileOutputStream.getChannel().tryLock();
    }

    @Override // cn.kuaipan.android.kss.download.KssAccessor
    public void lock() {
        this.mLocker.lock();
    }

    @Override // cn.kuaipan.android.kss.download.KssAccessor
    public void unlock() {
        this.mLocker.unlock();
    }

    @Override // cn.kuaipan.android.kss.download.KssAccessor
    public String sha1(long j, long j2) throws IOException {
        if (this.mClosed) {
            throw new IOException();
        }
        lock();
        try {
            return Encode.SHA1Encode(this.mFileInputStream.getChannel(), j, j2);
        } finally {
            unlock();
        }
    }

    @Override // cn.kuaipan.android.kss.download.KssAccessor
    public int write(byte[] bArr, int i, int i2, LoadRecorder loadRecorder) throws IOException {
        if (this.mClosed) {
            throw new IOException();
        }
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedIOException();
        }
        lock();
        long j = 0;
        if (loadRecorder != null) {
            try {
                j = loadRecorder.getStart();
                i2 = (int) Math.min(i2, loadRecorder.size());
            } finally {
                unlock();
            }
        }
        FileChannel channel = this.mFileOutputStream.getChannel();
        channel.position(j);
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        channel.write(ByteBuffer.wrap(bArr2));
        if (loadRecorder != null) {
            loadRecorder.add(i2);
        }
        return i2;
    }

    @Override // cn.kuaipan.android.kss.download.KssAccessor
    public void close() throws IOException {
        if (this.mClosed) {
            return;
        }
        this.mClosed = true;
        try {
            FileLock fileLock = this.mFileLocker;
            if (fileLock != null) {
                fileLock.release();
                this.mFileLocker = null;
            }
        } finally {
            try {
                FileOutputStream fileOutputStream = this.mFileOutputStream;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                    this.mFileOutputStream = null;
                }
            } catch (IOException unused) {
            }
        }
    }

    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }
}
