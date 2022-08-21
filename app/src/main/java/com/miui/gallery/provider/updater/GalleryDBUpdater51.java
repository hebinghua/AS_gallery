package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater51 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, "drop index if exists index_mixed_datetime");
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_cloud_mixed_exif_datetime", "cloud", "mixedDateTime, exifDateTime");
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, "drop index if exists index_mixed_datetime");
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_shareimage_mixed_exif_datetime", "shareImage", "mixedDateTime, exifDateTime");
        }
        return UpdateResult.defaultResult();
    }
}
