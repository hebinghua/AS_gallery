package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater64 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloudUser()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudUser", GalleryDBHelper.getInstance().getCloudUserColumns().get(13));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudUser", GalleryDBHelper.getInstance().getCloudUserColumns().get(14));
        }
        if (!updateResult.isRecreateTableShareUser()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareUser", GalleryDBHelper.getInstance().getShareUserColumns().get(13));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareUser", GalleryDBHelper.getInstance().getShareUserColumns().get(14));
        }
        return UpdateResult.defaultResult();
    }
}
