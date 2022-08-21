package com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id;

import android.content.Context;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class LogicBranch2 extends LogicBranch {
    public LogicBranch2(Context context, ArrayList<Long> arrayList, long j, long j2, long j3, IDataProvider iDataProvider) {
        super(context, arrayList, j, j2, j3, iDataProvider);
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        this.mContentValues.put("localGroupId", Long.valueOf(this.mAlbumId));
        this.mContentValues.putNull("groupId");
        if (TextUtils.isEmpty(this.mServerId) && this.mLocalFlag == 7) {
            this.mContentValues.put("localFlag", (Integer) 8);
        }
        addFilePath(this.mContentValues, this.mMediaConflictValidation == -105);
        this.mContentValues.putNull("secretKey");
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        super.execute(supportSQLiteDatabase, mediaManager);
        int update = supportSQLiteDatabase.update("cloud", 0, this.mContentValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
        DefaultLogger.d("galleryAction_removeSecret2", "executeDB update => %s ; count = [%d]", Util.desensitization(this.mContentValues), Integer.valueOf(update));
        if (update > 0) {
            if (mediaManager != null) {
                mediaManager.update("_id=?", new String[]{String.valueOf(this.mMediaId)}, this.mContentValues);
            }
            long j = this.mMediaId;
            markAsDirty(j);
            return j;
        }
        return -101L;
    }
}
