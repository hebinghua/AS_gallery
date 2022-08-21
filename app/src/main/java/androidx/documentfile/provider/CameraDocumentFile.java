package androidx.documentfile.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Objects;

/* loaded from: classes.dex */
public class CameraDocumentFile extends DocumentFile {
    public final Context mApplicationContext;
    public final Uri mCameraProcessingUrl;
    public final Uri mMediaStoreUrl;

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean canRead() {
        return true;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public DocumentFile createDirectory(String str) {
        return null;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public DocumentFile createFile(String str, String str2) {
        return null;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean delete() {
        return false;
    }

    public CameraDocumentFile(Context context, Uri uri, Uri uri2) {
        super(null);
        Objects.requireNonNull(context);
        Objects.requireNonNull(uri2);
        this.mApplicationContext = context;
        this.mMediaStoreUrl = uri;
        this.mCameraProcessingUrl = uri2;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public Uri getUri() {
        return this.mCameraProcessingUrl;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public String getName() {
        return DocumentsContractApi19.getName(this.mApplicationContext, this.mCameraProcessingUrl);
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean isDirectory() {
        return DocumentsContractApi19.getType(this.mApplicationContext, this.mCameraProcessingUrl) == null;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean isFile() {
        String type = DocumentsContractApi19.getType(this.mApplicationContext, this.mCameraProcessingUrl);
        return BaseFileMimeUtil.isImageFromMimeType(type) || BaseFileMimeUtil.isVideoFromMimeType(type);
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0038, code lost:
        r2.close();
     */
    @Override // androidx.documentfile.provider.DocumentFile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long lastModified() {
        /*
            r9 = this;
            r0 = 0
            android.content.Context r2 = r9.mApplicationContext     // Catch: java.lang.Exception -> L3c
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch: java.lang.Exception -> L3c
            android.net.Uri r4 = r9.mCameraProcessingUrl     // Catch: java.lang.Exception -> L3c
            java.lang.String r2 = "date_modified"
            java.lang.String[] r5 = new java.lang.String[]{r2}     // Catch: java.lang.Exception -> L3c
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r2 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Exception -> L3c
            if (r2 == 0) goto L36
            boolean r3 = r2.moveToFirst()     // Catch: java.lang.Throwable -> L2c
            if (r3 != 0) goto L20
            goto L36
        L20:
            r3 = 0
            long r3 = r2.getLong(r3)     // Catch: java.lang.Throwable -> L2c
            r5 = 1000(0x3e8, double:4.94E-321)
            long r3 = r3 * r5
            r2.close()     // Catch: java.lang.Exception -> L3c
            return r3
        L2c:
            r3 = move-exception
            r2.close()     // Catch: java.lang.Throwable -> L31
            goto L35
        L31:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch: java.lang.Exception -> L3c
        L35:
            throw r3     // Catch: java.lang.Exception -> L3c
        L36:
            if (r2 == 0) goto L3b
            r2.close()     // Catch: java.lang.Exception -> L3c
        L3b:
            return r0
        L3c:
            r2 = move-exception
            java.lang.String r3 = "DocumentFile"
            com.miui.gallery.util.logger.DefaultLogger.w(r3, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.documentfile.provider.CameraDocumentFile.lastModified():long");
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0035, code lost:
        r2.close();
     */
    @Override // androidx.documentfile.provider.DocumentFile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long length() {
        /*
            r9 = this;
            r0 = 0
            android.content.Context r2 = r9.mApplicationContext     // Catch: java.lang.Exception -> L39
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch: java.lang.Exception -> L39
            android.net.Uri r4 = r9.mCameraProcessingUrl     // Catch: java.lang.Exception -> L39
            java.lang.String r2 = "_size"
            java.lang.String[] r5 = new java.lang.String[]{r2}     // Catch: java.lang.Exception -> L39
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r2 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Exception -> L39
            if (r2 == 0) goto L33
            boolean r3 = r2.moveToFirst()     // Catch: java.lang.Throwable -> L29
            if (r3 != 0) goto L20
            goto L33
        L20:
            r3 = 0
            long r3 = r2.getLong(r3)     // Catch: java.lang.Throwable -> L29
            r2.close()     // Catch: java.lang.Exception -> L39
            return r3
        L29:
            r3 = move-exception
            r2.close()     // Catch: java.lang.Throwable -> L2e
            goto L32
        L2e:
            r2 = move-exception
            r3.addSuppressed(r2)     // Catch: java.lang.Exception -> L39
        L32:
            throw r3     // Catch: java.lang.Exception -> L39
        L33:
            if (r2 == 0) goto L38
            r2.close()     // Catch: java.lang.Exception -> L39
        L38:
            return r0
        L39:
            r2 = move-exception
            java.lang.String r3 = "DocumentFile"
            com.miui.gallery.util.logger.DefaultLogger.w(r3, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.documentfile.provider.CameraDocumentFile.length():long");
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean exists() {
        Cursor query;
        boolean z = true;
        try {
            query = this.mApplicationContext.getContentResolver().query(this.mCameraProcessingUrl, null, null, null, null);
        } catch (Exception e) {
            DefaultLogger.e("DocumentFile", e);
        }
        if (query != null && query.moveToFirst()) {
            query.close();
            return true;
        }
        if (query != null) {
            query.close();
        }
        if (this.mMediaStoreUrl != null) {
            try {
                Cursor query2 = this.mApplicationContext.getContentResolver().query(this.mMediaStoreUrl, null, null, null, null);
                if (query2 == null || !query2.moveToFirst()) {
                    z = false;
                }
                if (query2 != null) {
                    query2.close();
                }
                return z;
            } catch (Exception e2) {
                DefaultLogger.e("DocumentFile", e2);
            }
        }
        return false;
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public DocumentFile[] listFiles() {
        throw new UnsupportedOperationException();
    }

    @Override // androidx.documentfile.provider.DocumentFile
    public boolean renameTo(String str) {
        throw new UnsupportedOperationException();
    }
}
