package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater36 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(53));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(54));
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", GalleryDBHelper.getInstance().getShareImageColumns().get(47));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", GalleryDBHelper.getInstance().getShareImageColumns().get(48));
        }
        return UpdateResult.defaultResult();
    }
}
