package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class MoveFromCloudAlbum extends BaseMove {
    public final boolean isToNotSyncedAlbum;
    public final Context mContextImpl;

    public MoveFromCloudAlbum(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, j, j2, cursor);
        this.mContextImpl = context;
        this.isToNotSyncedAlbum = !AlbumCacheManager.getInstance().isAutoUpload(Long.valueOf(j2));
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move.BaseMove
    public void buildValues() {
        this.mValues.put("localFlag", Integer.valueOf(this.isToNotSyncedAlbum ? 18 : 5));
        this.mValues.putAll(Util.copyOf(Contracts.PUBLIC_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.putAll(Util.copyOf(Contracts.PRIVATE_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.put("fromLocalGroupId", Long.valueOf(this.mFromAlbum));
        this.mValues.put("localGroupId", Long.valueOf(this.mAlbumId));
        if (!this.isToNotSyncedAlbum) {
            this.mValues.put("localImageId", Long.valueOf(this.mMediaId));
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        supportSQLiteDatabase.beginTransactionNonExclusive();
        try {
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (cloud album) => inserts : %s", Util.desensitization(this.mValues));
            long insert = supportSQLiteDatabase.insert("cloud", 0, this.mValues);
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (cloud album) => inserts finish id [%d]", Long.valueOf(insert));
            if (insert > 0) {
                mediaManager.insert(insert, this.mValues);
                Cursor queryFavoritesTableById = queryFavoritesTableById(supportSQLiteDatabase, new String[]{j.c}, this.mMediaId);
                int i = 2;
                if (queryFavoritesTableById != null && queryFavoritesTableById.getCount() > 0) {
                    CloudUtils.replaceFavoritesById(this.mContextImpl, this.mMediaId, insert);
                }
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
                if (!this.isToNotSyncedAlbum) {
                    i = 11;
                }
                contentValues.put("localFlag", Integer.valueOf(i));
                DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (cloud album) => update : %s", Util.desensitization(contentValues));
                int update = supportSQLiteDatabase.update("cloud", 0, contentValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
                DefaultLogger.d("galleryAction_Method_AddToAlbum", "MOVE (cloud album) => update finish count [%d] id [%d]", Integer.valueOf(update), Long.valueOf(this.mMediaId));
                if (update > 0) {
                    mediaManager.delete("_id=?", new String[]{String.valueOf(this.mMediaId)});
                    markAsDirty(this.mMediaId);
                    RemarkManager.remarkMediaId(RemarkInfoFactory.createMoveRemarkInfo(this.mMediaId, getFromFilePath(), getToFilePath()));
                }
            } else {
                insert = -101;
            }
            supportSQLiteDatabase.setTransactionSuccessful();
            return insert;
        } finally {
            supportSQLiteDatabase.endTransaction();
        }
    }

    public final Cursor queryFavoritesTableById(SupportSQLiteDatabase supportSQLiteDatabase, String[] strArr, long j) {
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("favorites").columns(strArr).selection("cloud_id = ?", new String[]{String.valueOf(j)}).create());
    }
}
