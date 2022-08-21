package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MoveFromSystemAlbum extends BaseMove {
    public final boolean mIsToAutoUploadedAlbum;

    public MoveFromSystemAlbum(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, j, j2, cursor);
        this.mIsToAutoUploadedAlbum = AlbumCacheManager.getInstance().isAutoUpload(Long.valueOf(j2));
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move.BaseMove
    public void buildValues() {
        if (this.mIsToAutoUploadedAlbum) {
            this.mValues.put("localFlag", (Integer) 5);
            this.mValues.put("fromLocalGroupId", Long.valueOf(this.mFromAlbum));
            this.mValues.put("localGroupId", Long.valueOf(this.mAlbumId));
            this.mValues.putNull("groupId");
            this.mValues.putNull("localImageId");
            return;
        }
        this.mValues.put("localFlag", (Integer) 18);
        this.mValues.putAll(Util.copyOf(Contracts.PUBLIC_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.putAll(Util.copyOf(Contracts.PRIVATE_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.put("fromLocalGroupId", Long.valueOf(this.mFromAlbum));
        this.mValues.put("localGroupId", Long.valueOf(this.mAlbumId));
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        supportSQLiteDatabase.beginTransactionNonExclusive();
        try {
            if (this.mIsToAutoUploadedAlbum) {
                moveToAutoUploadedAlbum(supportSQLiteDatabase, mediaManager);
            } else {
                moveToNotAutoUploadedAlbum(supportSQLiteDatabase, mediaManager);
            }
            supportSQLiteDatabase.setTransactionSuccessful();
            return this.mMediaId;
        } finally {
            supportSQLiteDatabase.endTransaction();
        }
    }

    public final void moveToAutoUploadedAlbum(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (system album) => update : %s", Util.desensitization(this.mValues));
        int update = supportSQLiteDatabase.update("cloud", 0, this.mValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
        DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (system album) => update finish count [%d] id [%d]", Integer.valueOf(update), Long.valueOf(this.mMediaId));
        if (update > 0) {
            mediaManager.update("_id=?", new String[]{String.valueOf(this.mMediaId)}, this.mValues);
            markAsDirty(this.mMediaId);
            RemarkManager.remarkMediaId(RemarkInfoFactory.createMoveRemarkInfo(this.mMediaId, getFromFilePath(), getToFilePath()));
        }
    }

    public final void moveToNotAutoUploadedAlbum(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (system album) => inserts : %s", Util.desensitization(this.mValues));
        long insert = supportSQLiteDatabase.insert("cloud", 0, this.mValues);
        DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (system album) => inserts finish id [%d]", Long.valueOf(insert));
        if (insert > 0) {
            mediaManager.insert(insert, this.mValues);
            markAsDirty(insert);
            RemarkManager.remarkMediaId(RemarkInfoFactory.createMoveRemarkInfo(insert, getFromFilePath(), getToFilePath()));
            String asString = this.mValues.getAsString("localFile");
            if (TextUtils.isEmpty(asString)) {
                asString = this.mValues.getAsString("thumbnailFile");
            }
            if (!TextUtils.isEmpty(asString)) {
                RemarkManager.remarkMediaId(RemarkInfoFactory.createMoveCloudMediaRemarkInfo(insert, asString, null));
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("localFlag", (Integer) 2);
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (system album) => update : %s", Util.desensitization(contentValues));
            int update = supportSQLiteDatabase.update("cloud", 0, contentValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (system album) => update finish count [%d] id [%d]", Integer.valueOf(update), Long.valueOf(this.mMediaId));
            if (update <= 0) {
                return;
            }
            mediaManager.delete("_id=?", new String[]{String.valueOf(this.mMediaId)});
            markAsDirty(this.mMediaId);
            RemarkManager.remarkMediaId(RemarkInfoFactory.createMoveRemarkInfo(this.mMediaId, getFromFilePath(), getToFilePath()));
        }
    }
}
