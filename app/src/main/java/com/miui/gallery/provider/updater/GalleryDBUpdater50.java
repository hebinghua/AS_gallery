package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater50 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloudSettings()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(13));
        }
        return UpdateResult.defaultResult();
    }
}
