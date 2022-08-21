package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.updater.UpdateResult;

/* loaded from: classes2.dex */
public class GalleryDBUpdater14 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        UpdateResult.Builder builder = new UpdateResult.Builder();
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.dropTable(supportSQLiteDatabase, "cloud");
            GalleryDBHelper.createTable(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns());
            builder.recreateTableCloud();
        }
        if (!updateResult.isRecreateTableCloudSettings()) {
            GalleryDBHelper.dropTable(supportSQLiteDatabase, "cloudSetting");
            GalleryDBHelper.createTable(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns());
            builder.recreateTableCloudSettings();
        }
        return builder.build();
    }
}
