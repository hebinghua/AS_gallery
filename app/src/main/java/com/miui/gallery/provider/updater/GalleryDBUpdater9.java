package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.updater.UpdateResult;

/* loaded from: classes2.dex */
public class GalleryDBUpdater9 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "cloud");
        GalleryDBHelper.createTable(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns());
        GalleryDBHelper.createTable(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns());
        return new UpdateResult.Builder().recreateTableCloud().recreateTableCloudSettings().build();
    }
}
