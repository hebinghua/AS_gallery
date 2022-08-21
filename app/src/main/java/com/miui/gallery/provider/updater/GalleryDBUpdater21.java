package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.updater.UpdateResult;

/* loaded from: classes2.dex */
public class GalleryDBUpdater21 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(44));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(45));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(46));
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_localFlag", "cloud", "localFlag");
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_fileName_NOCASE", "cloud", "fileName COLLATE NOCASE");
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_cloud_albumId", "cloud", "albumId");
        }
        if (!updateResult.isRecreateTableCloudSettings()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(6));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(7));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(8));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(9));
        }
        GalleryDBHelper.createTable(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns());
        GalleryDBHelper.createTable(supportSQLiteDatabase, "shareImage", GalleryDBHelper.getInstance().getShareImageColumns());
        GalleryDBHelper.createTable(supportSQLiteDatabase, "shareUser", GalleryDBHelper.getInstance().getShareUserColumns());
        return new UpdateResult.Builder().recreateTableShareUser().recreateTableShareAlbum().recreateTableShareImage().build();
    }
}
