package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryDBUpdater97 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) (-1));
        int safeUpdate = SafeDBUtil.safeUpdate(supportSQLiteDatabase, "cloud", contentValues, "_id IN (SELECT _id FROM ((SELECT _id FROM cloud WHERE serverType=0 AND serverId IS NULL) AS Album LEFT JOIN (SELECT localGroupId, count(_id) AS media_count FROM cloud WHERE serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) GROUP BY localGroupId) AS MediaCount ON Album._id=MediaCount.localGroupId) WHERE media_count IS NULL OR media_count=0)", (String[]) null);
        if (safeUpdate >= 0) {
            DefaultLogger.d("GalleryDBUpdater97", "delete unsynced empty albums count:" + safeUpdate);
        } else {
            DefaultLogger.d("GalleryDBUpdater97", "delete unsynced empty albums failed.");
        }
        DefaultLogger.d("GalleryDBUpdater97", "delete unsynced empty albums.");
        return UpdateResult.defaultResult();
    }
}
