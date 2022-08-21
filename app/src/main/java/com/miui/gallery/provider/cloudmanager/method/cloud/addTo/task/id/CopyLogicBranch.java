package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CheckPostProcessing;
import com.miui.gallery.provider.cloudmanager.MediaConflict;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CopyLogicBranch extends LogicBranch {
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

    public CopyLogicBranch(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, long j2, Cursor cursor) {
        super(context, arrayList, iDataProvider, j, j2, cursor);
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        if (this.mServerId == 0) {
            long run = new CheckPostProcessing(this.mContext, this.mLocalFile).run(supportSQLiteDatabase, null);
            if (run == -111) {
                return run;
            }
        }
        if ("recovery".equalsIgnoreCase(this.mServerStatus)) {
            return -115L;
        }
        long verify = MediaConflict.verify(this.mFileName, this.mAlbumId, this.mMediaId, this.mSha1, supportSQLiteDatabase);
        if (verify == -102 || verify == -117) {
            return -1L;
        }
        if (verify != -116) {
            return verify;
        }
        DefaultLogger.d("galleryAction_Method_AddToAlbum", "copy => fileName has exist, append ending");
        return -3L;
    }
}
