package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater19 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_fileName", "cloud", "fileName");
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_sort", "cloud", "dateModified DESC, _id DESC");
        }
        return UpdateResult.defaultResult();
    }
}
