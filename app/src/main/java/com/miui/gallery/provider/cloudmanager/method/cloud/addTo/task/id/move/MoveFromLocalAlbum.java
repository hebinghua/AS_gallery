package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MoveFromLocalAlbum extends BaseMove {
    public MoveFromLocalAlbum(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, j, j2, cursor);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move.BaseMove
    public void buildValues() {
        this.mValues.put("localGroupId", Long.valueOf(this.mAlbumId));
        this.mValues.put("localFlag", (Integer) 17);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        supportSQLiteDatabase.beginTransactionNonExclusive();
        try {
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (local album) => update : %s", Util.desensitization(this.mValues));
            int update = supportSQLiteDatabase.update("cloud", 0, this.mValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (local album) => update finish count [%d] id [%d]", Integer.valueOf(update), Long.valueOf(this.mMediaId));
            if (update > 0) {
                markAsDirty(this.mMediaId);
                RemarkManager.remarkMediaId(RemarkInfoFactory.createMoveRemarkInfo(this.mMediaId, getFromFilePath(), getToFilePath()));
                this.mValues.put("fromLocalGroupId", Long.valueOf(this.mFromAlbum));
                mediaManager.update("_id=?", new String[]{String.valueOf(this.mMediaId)}, this.mValues);
            }
            supportSQLiteDatabase.setTransactionSuccessful();
            return this.mMediaId;
        } finally {
            supportSQLiteDatabase.endTransaction();
        }
    }
}
