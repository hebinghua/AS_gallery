package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater68 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloudUser()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudUser", GalleryDBHelper.getInstance().getCloudUserColumns().get(15));
        }
        return UpdateResult.defaultResult();
    }
}
