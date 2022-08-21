package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater49 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(60));
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", GalleryDBHelper.getInstance().getShareImageColumns().get(54));
        }
        return UpdateResult.defaultResult();
    }
}
