package androidx.documentfile.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import java.io.File;

/* loaded from: classes.dex */
public abstract class DocumentFile {
    public final DocumentFile mParent;

    public abstract boolean canRead();

    public abstract DocumentFile createDirectory(String str);

    public abstract DocumentFile createFile(String str, String str2);

    public abstract boolean delete();

    public abstract boolean exists();

    public abstract String getName();

    public abstract Uri getUri();

    public abstract boolean isDirectory();

    public abstract boolean isFile();

    public abstract long lastModified();

    public abstract long length();

    public abstract DocumentFile[] listFiles();

    public abstract boolean renameTo(String str);

    public DocumentFile(DocumentFile documentFile) {
        this.mParent = documentFile;
    }

    public static DocumentFile fromFile(File file) {
        return new RawDocumentFile(null, file);
    }

    public static DocumentFile fromTreeUri(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= 21) {
            String treeDocumentId = DocumentsContract.getTreeDocumentId(uri);
            if (DocumentsContract.isDocumentUri(context, uri)) {
                treeDocumentId = DocumentsContract.getDocumentId(uri);
            }
            return new TreeDocumentFile(null, context, DocumentsContract.buildDocumentUriUsingTree(uri, treeDocumentId));
        }
        return null;
    }

    public DocumentFile getParentFile() {
        return this.mParent;
    }

    public DocumentFile findFile(String str) {
        DocumentFile[] listFiles;
        for (DocumentFile documentFile : listFiles()) {
            if (str.equals(documentFile.getName())) {
                return documentFile;
            }
        }
        return null;
    }
}
