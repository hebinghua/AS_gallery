package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater52 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(61));
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", GalleryDBHelper.getInstance().getShareImageColumns().get(55));
        }
        if (!updateResult.isRecreateTableOwnerSubUbi()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "ownerSubUbifocus", GalleryDBHelper.getInstance().getOwnerSubUbiFocusColumns().get(51));
        }
        if (!updateResult.isRecreateTableSharerSubUbi()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareSubUbifocus", GalleryDBHelper.getInstance().getShareSubUbiFocusColumns().get(53));
        }
        return UpdateResult.defaultResult();
    }
}
