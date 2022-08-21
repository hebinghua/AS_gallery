package cn.kuaipan.android.kss.download;

import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/* loaded from: classes.dex */
public class DownloadDescriptorFile {
    public final long currentSize;
    public final String fileName;
    public final File innerCacheFile;
    public final ParcelFileDescriptor pFd;

    public DownloadDescriptorFile(ParcelFileDescriptor parcelFileDescriptor, String str, long j, File file) {
        this.pFd = parcelFileDescriptor;
        this.fileName = str;
        this.currentSize = j;
        this.innerCacheFile = file;
    }

    public FileInputStream openFileInputStream() throws IOException {
        ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(this.pFd.dup());
        autoCloseInputStream.getChannel().position(0L);
        return autoCloseInputStream;
    }

    public FileOutputStream openFileOutputStream() throws IOException {
        ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(this.pFd.dup());
        autoCloseOutputStream.getChannel().position(0L);
        return autoCloseOutputStream;
    }

    public void reset() throws IOException {
        ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = null;
        try {
            ByteBuffer allocate = ByteBuffer.allocate(1);
            ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream2 = new ParcelFileDescriptor.AutoCloseOutputStream(this.pFd.dup());
            try {
                FileChannel channel = autoCloseOutputStream2.getChannel();
                channel.position(0L);
                allocate.put((byte) 0);
                channel.write(allocate);
                try {
                    autoCloseOutputStream2.close();
                } catch (IOException unused) {
                }
            } catch (Throwable th) {
                th = th;
                autoCloseOutputStream = autoCloseOutputStream2;
                if (autoCloseOutputStream != null) {
                    try {
                        autoCloseOutputStream.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
