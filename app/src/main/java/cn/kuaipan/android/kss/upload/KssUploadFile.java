package cn.kuaipan.android.kss.upload;

import cn.kuaipan.android.utils.RandomFileDescriptorInputStream;
import cn.kuaipan.android.utils.RandomInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public abstract class KssUploadFile {
    public final String fileAbsPath;
    public final String filePath;
    public final long fileSize;

    /* renamed from: cn.kuaipan.android.kss.upload.KssUploadFile$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends KssUploadFile {
    }

    public abstract InputStream getInputStream() throws IOException;

    public abstract RandomInputStream getRandomFileInputStream() throws IOException;

    public abstract boolean isFileInvalid();

    public /* synthetic */ KssUploadFile(String str, String str2, long j, AnonymousClass1 anonymousClass1) {
        this(str, str2, j);
    }

    public KssUploadFile(String str, String str2, long j) {
        this.fileAbsPath = str;
        this.filePath = str2;
        this.fileSize = j;
    }

    public static KssUploadFile createByFileDescriptor(final UploadDescriptorFile uploadDescriptorFile) {
        return new KssUploadFile(uploadDescriptorFile.filePath, uploadDescriptorFile.fileName, uploadDescriptorFile.fileSize) { // from class: cn.kuaipan.android.kss.upload.KssUploadFile.2
            @Override // cn.kuaipan.android.kss.upload.KssUploadFile
            public boolean isFileInvalid() {
                return uploadDescriptorFile.fileSize <= 0;
            }

            @Override // cn.kuaipan.android.kss.upload.KssUploadFile
            public InputStream getInputStream() throws IOException {
                return uploadDescriptorFile.openFileInputStream();
            }

            @Override // cn.kuaipan.android.kss.upload.KssUploadFile
            public RandomInputStream getRandomFileInputStream() throws IOException {
                return new RandomFileDescriptorInputStream(uploadDescriptorFile);
            }
        };
    }
}
