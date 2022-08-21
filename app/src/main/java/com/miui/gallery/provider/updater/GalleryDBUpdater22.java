package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.updater.UpdateResult;

/* loaded from: classes2.dex */
public class GalleryDBUpdater22 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableShareUser()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareUser", GalleryDBHelper.getInstance().getShareUserColumns().get(12));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareUser", GalleryDBHelper.getInstance().getShareUserColumns().get(11));
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_shareUser_localAlbumId", "shareUser", "localAlbumId");
        }
        if (!updateResult.isRecreateTableCloudSettings()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(10));
        }
        GalleryDBHelper.createTable(supportSQLiteDatabase, "cloudUser", GalleryDBHelper.getInstance().getCloudUserColumns());
        return new UpdateResult.Builder().recreateTableCloudUser().build();
    }
}
