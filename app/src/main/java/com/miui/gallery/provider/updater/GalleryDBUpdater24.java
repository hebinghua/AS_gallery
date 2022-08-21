package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater24 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableShareAlbum()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(20));
        }
        return UpdateResult.defaultResult();
    }
}
