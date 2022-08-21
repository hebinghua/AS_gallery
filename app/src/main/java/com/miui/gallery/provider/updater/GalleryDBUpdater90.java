package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.SQLiteView;
import com.miui.gallery.provider.updater.UpdateResult;
import com.miui.gallery.util.SafeDBUtil;
import com.xiaomi.stat.a.j;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater90 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.createTable(supportSQLiteDatabase, "favorites", GalleryDBHelper.getInstance().getFavoritesColumns());
        if (!updateResult.isRecreateTableCloud()) {
            updateCameraAlbumValues(supportSQLiteDatabase);
            refillIsFavorite(supportSQLiteDatabase);
        }
        SQLiteView.of("extended_cloud").createByVersion(supportSQLiteDatabase, 1);
        return new UpdateResult.Builder().recreateTableFavorite().build();
    }

    public final void updateCameraAlbumValues(SupportSQLiteDatabase supportSQLiteDatabase) {
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "UPDATE %s SET %s=%d, %s=%d, %s=(CASE WHEN %s=null THEN null WHEN %s>%d THEN %d ELSE %s END) WHERE %s=%d", "cloud", "dateTaken", 999L, "mixedDateTime", 999L, "sortBy", "sortBy", "sortBy", 1L, 999L, "sortBy", "serverId", 1L));
    }

    public final void refillIsFavorite(final SupportSQLiteDatabase supportSQLiteDatabase) {
        SafeDBUtil.safeQuery(supportSQLiteDatabase, "cloud", new String[]{"description", j.c}, "serverType IN (1,2) AND description NOT NULL AND description != '' AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater90.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Void mo1808handle(Cursor cursor) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String string = cursor.getString(0);
                        Long valueOf = Long.valueOf(cursor.getLong(1));
                        Boolean isFavoriteFromDescription = CloudUtils.getIsFavoriteFromDescription(string);
                        if (isFavoriteFromDescription != null && isFavoriteFromDescription.booleanValue()) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("isFavorite", (Integer) 1);
                            contentValues.put("cloud_id", valueOf);
                            contentValues.put("dateFavorite", Long.valueOf(System.currentTimeMillis()));
                            contentValues.put("source", (Integer) 1);
                            SafeDBUtil.safeInsert(supportSQLiteDatabase, "favorites", contentValues);
                        }
                    }
                    return null;
                }
                return null;
            }
        });
    }
}
