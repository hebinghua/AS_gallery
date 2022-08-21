package androidx.documentfile.provider;

import android.net.Uri;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import kotlin.io.FilesKt__UtilsKt;

/* loaded from: classes.dex */
public class GalleryRawDocumentFile extends RawDocumentFile {
    public File mFile;

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ boolean canRead() {
        return super.canRead();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ DocumentFile createDirectory(String str) {
        return super.createDirectory(str);
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ DocumentFile createFile(String str, String str2) {
        return super.createFile(str, str2);
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ boolean exists() {
        return super.exists();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ String getName() {
        return super.getName();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ Uri getUri() {
        return super.getUri();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ boolean isDirectory() {
        return super.isDirectory();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ boolean isFile() {
        return super.isFile();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ long lastModified() {
        return super.lastModified();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ long length() {
        return super.length();
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public /* bridge */ /* synthetic */ DocumentFile[] listFiles() {
        return super.listFiles();
    }

    public GalleryRawDocumentFile(DocumentFile documentFile, File file) {
        super(documentFile, file);
        this.mFile = file;
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public boolean delete() {
        boolean z;
        boolean z2 = false;
        try {
            if (this.mFile.exists()) {
                if (!this.mFile.isDirectory()) {
                    boolean delete = this.mFile.delete();
                    if (!delete) {
                        try {
                            delete = this.mFile.delete();
                        } catch (Exception e) {
                            z = delete;
                            e = e;
                            try {
                                DefaultLogger.w("GalleryRawDocumentFile", "delete error : %s", e);
                                DefaultLogger.w("GalleryRawDocumentFile", "try delete [%s]. is success: %s", getUri(), Boolean.valueOf(z));
                                DefaultLogger.debugPrintStackMsg("GalleryRawDocumentFile", true);
                                return false;
                            } catch (Throwable th) {
                                th = th;
                                z2 = z;
                                DefaultLogger.w("GalleryRawDocumentFile", "try delete [%s]. is success: %s", getUri(), Boolean.valueOf(z2));
                                DefaultLogger.debugPrintStackMsg("GalleryRawDocumentFile", true);
                                throw th;
                            }
                        } catch (Throwable th2) {
                            boolean z3 = delete;
                            th = th2;
                            z2 = z3;
                            DefaultLogger.w("GalleryRawDocumentFile", "try delete [%s]. is success: %s", getUri(), Boolean.valueOf(z2));
                            DefaultLogger.debugPrintStackMsg("GalleryRawDocumentFile", true);
                            throw th;
                        }
                    }
                    StorageSolutionProvider.get().apply(this);
                    DefaultLogger.w("GalleryRawDocumentFile", "try delete [%s]. is success: %s", getUri(), Boolean.valueOf(delete));
                    DefaultLogger.debugPrintStackMsg("GalleryRawDocumentFile", true);
                    return delete;
                }
                DefaultLogger.w("GalleryRawDocumentFile", "avoid to delete media set by deleting whole directory");
                z2 = FilesKt__UtilsKt.deleteRecursively(this.mFile);
            }
            DefaultLogger.w("GalleryRawDocumentFile", "try delete [%s]. is success: %s", getUri(), Boolean.FALSE);
            DefaultLogger.debugPrintStackMsg("GalleryRawDocumentFile", true);
            return z2;
        } catch (Exception e2) {
            e = e2;
            z = false;
        } catch (Throwable th3) {
            th = th3;
        }
    }

    @Override // androidx.documentfile.provider.RawDocumentFile, androidx.documentfile.provider.DocumentFile
    public boolean renameTo(String str) {
        boolean z;
        StorageStrategyManager storageStrategyManager;
        File file;
        String absolutePath = this.mFile.getAbsolutePath();
        try {
            File file2 = new File(this.mFile.getParentFile(), str);
            if (super.renameTo(str)) {
                this.mFile = file2;
                z = true;
                storageStrategyManager = StorageSolutionProvider.get();
                file = new File(absolutePath);
            } else {
                z = false;
                storageStrategyManager = StorageSolutionProvider.get();
                file = new File(absolutePath);
            }
            storageStrategyManager.apply(DocumentFile.fromFile(file));
            StorageSolutionProvider.get().apply(this);
            return z;
        } catch (Throwable th) {
            StorageSolutionProvider.get().apply(DocumentFile.fromFile(new File(absolutePath)));
            StorageSolutionProvider.get().apply(this);
            throw th;
        }
    }
}
