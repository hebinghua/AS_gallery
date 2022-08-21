package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater70 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_microthumbnail", "cloud", "groupId DESC, dateModified DESC");
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_shareimage_microthumbnail", "shareImage", "groupId DESC, dateModified DESC");
        }
        return UpdateResult.defaultResult();
    }
}
