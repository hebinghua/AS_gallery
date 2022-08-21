package androidx.documentfile.provider;

import android.net.Uri;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class DocumentFileWrapper extends DocumentFile {
    public final DocumentFile mWrapped;

    @Override // androidx.documentfile.provider.DocumentFile
    public DocumentFile[] listFiles() {
        return new DocumentFile[0];
    }

    public DocumentFileWrapper(DocumentFile documentFile) {
        super(documentFile.getParentFile());
        this.mWrapped = documentFile;
    }

    public DocumentFile get() {
        return this.mWrapped;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public DocumentFile createFile(String str, String str2) {
        return this.mWrapped.createFile(str, str2);
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public DocumentFile createDirectory(String str) {
        return this.mWrapped.createDirectory(str);
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public Uri getUri() {
        return this.mWrapped.getUri();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public String getName() {
        return this.mWrapped.getName();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean isDirectory() {
        return this.mWrapped.isDirectory();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean isFile() {
        return this.mWrapped.isFile();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public long lastModified() {
        return this.mWrapped.lastModified();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public long length() {
        return this.mWrapped.length();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean canRead() {
        return this.mWrapped.canRead();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean delete() {
        try {
            boolean delete = this.mWrapped.delete();
            DefaultLogger.w("DocumentFile", "try delete [%s]. is success: %s", getUri(), Boolean.valueOf(delete));
            DefaultLogger.debugPrintStackMsg("DocumentFile", true);
            return delete;
        } catch (Throwable th) {
            DefaultLogger.w("DocumentFile", "try delete [%s]. is success: %s", getUri(), Boolean.FALSE);
            DefaultLogger.debugPrintStackMsg("DocumentFile", true);
            throw th;
        }
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean exists() {
        return this.mWrapped.exists();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public DocumentFile findFile(String str) {
        DocumentFile[] listFiles;
        for (DocumentFile documentFile : this.mWrapped.listFiles()) {
            if (str.equals(documentFile.getName())) {
                return documentFile;
            }
        }
        return null;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean renameTo(String str) {
        return this.mWrapped.renameTo(str);
    }
}
