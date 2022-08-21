package cn.kuaipan.android.kss.download;

import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class KssDownloadFile {
    public final long currentSize;
    public final File innerCacheFile;

    /* renamed from: cn.kuaipan.android.kss.download.KssDownloadFile$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends KssDownloadFile {
    }

    public abstract boolean exists();

    public abstract KssAccessor getKssAccessor() throws IOException;

    public abstract void resetFileIfNeeded(boolean z, long j);

    public abstract void setLastModifyTime(long j);

    public /* synthetic */ KssDownloadFile(File file, long j, AnonymousClass1 anonymousClass1) {
        this(file, j);
    }

    public KssDownloadFile(File file, long j) {
        this.innerCacheFile = file;
        this.currentSize = j;
    }

    public static KssDownloadFile createByFileDescriptor(final DownloadDescriptorFile downloadDescriptorFile) {
        return new KssDownloadFile(downloadDescriptorFile.innerCacheFile, downloadDescriptorFile.currentSize) { // from class: cn.kuaipan.android.kss.download.KssDownloadFile.2
            @Override // cn.kuaipan.android.kss.download.KssDownloadFile
            public void setLastModifyTime(long j) {
            }

            @Override // cn.kuaipan.android.kss.download.KssDownloadFile
            public KssAccessor getKssAccessor() throws IOException {
                return new KssFileDescriptorAccessor(downloadDescriptorFile);
            }

            @Override // cn.kuaipan.android.kss.download.KssDownloadFile
            public void resetFileIfNeeded(boolean z, long j) {
                if (!z || downloadDescriptorFile.currentSize > j) {
                    try {
                        downloadDescriptorFile.reset();
                    } catch (IOException unused) {
                        throw new SecurityException("Failed delete target file. Can't download to dest path: " + downloadDescriptorFile.fileName);
                    }
                }
            }

            @Override // cn.kuaipan.android.kss.download.KssDownloadFile
            public boolean exists() {
                return downloadDescriptorFile.currentSize > 0;
            }
        };
    }
}
