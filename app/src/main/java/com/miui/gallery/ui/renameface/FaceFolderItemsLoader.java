package com.miui.gallery.ui.renameface;

import android.app.Activity;
import com.miui.gallery.ui.renameface.FolderItemsLoader;

/* compiled from: FaceAlbumHandlerBase.java */
/* loaded from: classes2.dex */
public class FaceFolderItemsLoader extends FolderItemsLoader {
    public FaceFolderItemsLoader(Activity activity, String str, FolderItemsLoader.LoaderUpdatedItems loaderUpdatedItems, long[] jArr) {
        super(activity, str, loaderUpdatedItems, jArr, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0050, code lost:
        if (r2 != null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005a, code lost:
        if (r2 != null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005c, code lost:
        r2.close();
     */
    @Override // com.miui.gallery.ui.renameface.FolderItemsLoader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.ArrayList<com.miui.gallery.model.DisplayFolderItem> refreshCloudFolderItems() {
        /*
            r9 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.lang.ref.SoftReference<android.app.Activity> r1 = r9.mActivityRef
            java.lang.Object r1 = r1.get()
            android.app.Activity r1 = (android.app.Activity) r1
            if (r1 == 0) goto L5f
            r2 = 0
            android.content.ContentResolver r3 = r1.getContentResolver()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            android.net.Uri r4 = com.miui.gallery.provider.GalleryContract.PeopleFace.PERSONS_URI     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            java.lang.String[] r5 = com.miui.gallery.util.face.PeopleCursorHelper.PROJECTION     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r2 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
        L1f:
            if (r2 == 0) goto L50
            boolean r1 = r2.moveToNext()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            if (r1 == 0) goto L50
            java.lang.String r1 = com.miui.gallery.util.face.PeopleCursorHelper.getThumbnailPath(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            java.lang.String r3 = com.miui.gallery.util.face.PeopleCursorHelper.getPeopleName(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            if (r4 != 0) goto L1f
            java.lang.String r4 = com.miui.gallery.util.face.PeopleCursorHelper.getPeopleLocalId(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            boolean r4 = r9.isMediaSetCandidate(r4)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            if (r4 == 0) goto L1f
            com.miui.gallery.ui.renameface.FaceDisplayFolderItem r4 = new com.miui.gallery.ui.renameface.FaceDisplayFolderItem     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            java.lang.String r5 = com.miui.gallery.util.face.PeopleCursorHelper.getPeopleLocalId(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            com.miui.gallery.util.face.FaceRegionRectF r6 = com.miui.gallery.util.face.PeopleCursorHelper.getFaceRegionRectF(r2)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            r4.<init>(r3, r1, r5, r6)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            r0.add(r4)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L5a
            goto L1f
        L50:
            if (r2 == 0) goto L5f
            goto L5c
        L53:
            r0 = move-exception
            if (r2 == 0) goto L59
            r2.close()
        L59:
            throw r0
        L5a:
            if (r2 == 0) goto L5f
        L5c:
            r2.close()
        L5f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.renameface.FaceFolderItemsLoader.refreshCloudFolderItems():java.util.ArrayList");
    }
}
