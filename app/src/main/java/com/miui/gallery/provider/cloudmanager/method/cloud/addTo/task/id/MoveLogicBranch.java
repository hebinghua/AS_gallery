package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MoveLogicBranch extends LogicBranch {
    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public boolean checkValidation(long j) {
        return j == -1 || j == -3;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public /* bridge */ /* synthetic */ void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        super.doPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public /* bridge */ /* synthetic */ long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        return super.execute(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public /* bridge */ /* synthetic */ void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        super.postPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.LogicBranch
    public /* bridge */ /* synthetic */ void setFinalFileName(String str) {
        super.setFinalFileName(str);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.LogicBranch
    public /* bridge */ /* synthetic */ void setFromFilePath(String str) {
        super.setFromFilePath(str);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.LogicBranch
    public /* bridge */ /* synthetic */ void setToFilePath(String str) {
        super.setToFilePath(str);
    }

    public MoveLogicBranch(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, long j2, Cursor cursor) {
        super(context, arrayList, iDataProvider, j, j2, cursor);
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x008a  */
    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long verify(androidx.sqlite.db.SupportSQLiteDatabase r8, com.miui.gallery.provider.cache.MediaManager r9) throws com.miui.gallery.storage.exceptions.StoragePermissionMissingException {
        /*
            r7 = this;
            long r0 = r7.mServerId
            r2 = 0
            int r9 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r9 != 0) goto L1d
            com.miui.gallery.provider.cloudmanager.CheckPostProcessing r9 = new com.miui.gallery.provider.cloudmanager.CheckPostProcessing
            android.content.Context r0 = r7.mContext
            java.lang.String r1 = r7.mLocalFile
            r9.<init>(r0, r1)
            r0 = 0
            long r0 = r9.run(r8, r0)
            r2 = -111(0xffffffffffffff91, double:NaN)
            int r9 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r9 != 0) goto L1d
            return r0
        L1d:
            java.lang.String r9 = r7.mServerStatus
            java.lang.String r0 = "recovery"
            boolean r9 = r0.equalsIgnoreCase(r9)
            if (r9 == 0) goto L2a
            r8 = -115(0xffffffffffffff8d, double:NaN)
            return r8
        L2a:
            java.lang.String r0 = r7.mFileName
            long r1 = r7.mAlbumId
            long r3 = r7.mMediaId
            java.lang.String r5 = r7.mSha1
            r6 = r8
            long r8 = com.miui.gallery.provider.cloudmanager.MediaConflict.verify(r0, r1, r3, r5, r6)
            r0 = -102(0xffffffffffffff9a, double:NaN)
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 == 0) goto Lb6
            r0 = -117(0xffffffffffffff8b, double:NaN)
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 != 0) goto L45
            goto Lb6
        L45:
            long r0 = r7.mMediaId
            boolean r0 = com.miui.gallery.provider.cache.ShareMediaManager.isOtherShareMediaId(r0)
            java.lang.String r1 = "galleryAction_Method_AddToAlbum"
            if (r0 == 0) goto L57
            java.lang.String r8 = "Illegal operate: move share media"
            com.miui.gallery.util.logger.DefaultLogger.w(r1, r8)
            r8 = -114(0xffffffffffffff8e, double:NaN)
            return r8
        L57:
            com.miui.gallery.provider.cache.AlbumCacheManager r0 = com.miui.gallery.provider.cache.AlbumCacheManager.getInstance()
            long r2 = r7.mAlbumId
            java.lang.Long r2 = java.lang.Long.valueOf(r2)
            boolean r0 = r0.isAutoUpload(r2)
            r2 = 1
            r3 = 0
            if (r0 != 0) goto L92
            android.database.Cursor r0 = r7.mCursor
            if (r0 == 0) goto L87
            boolean r0 = r0.moveToFirst()
            if (r0 == 0) goto L87
            android.database.Cursor r0 = r7.mCursor
            java.lang.String r4 = "localFile"
            int r4 = r0.getColumnIndex(r4)
            java.lang.String r0 = r0.getString(r4)
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L87
            r0 = r2
            goto L88
        L87:
            r0 = r3
        L88:
            if (r0 != 0) goto L92
            java.lang.String r8 = "Illegal operate: move to not uploaded album for no local file"
            com.miui.gallery.util.logger.DefaultLogger.w(r1, r8)
            r8 = -122(0xffffffffffffff86, double:NaN)
            return r8
        L92:
            r4 = -118(0xffffffffffffff8a, double:NaN)
            int r0 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r0 != 0) goto Lad
            java.lang.String r8 = "Target dir has this image，delete src image!"
            com.miui.gallery.util.logger.DefaultLogger.w(r1, r8)
            android.content.Context r8 = r7.mContext
            r9 = 42
            long[] r0 = new long[r2]
            long r1 = r7.mMediaId
            r0[r3] = r1
            com.miui.gallery.provider.CloudUtils.deleteById(r8, r9, r0)
            long r8 = r7.mMediaId
            return r8
        Lad:
            r0 = -116(0xffffffffffffff8c, double:NaN)
            int r0 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r0 != 0) goto Lb5
            r8 = -3
        Lb5:
            return r8
        Lb6:
            r8 = -1
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.MoveLogicBranch.verify(androidx.sqlite.db.SupportSQLiteDatabase, com.miui.gallery.provider.cache.MediaManager):long");
    }
}
