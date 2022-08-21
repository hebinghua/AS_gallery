package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater23 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableShareAlbum()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(16));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(17));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(18));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(19));
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", GalleryDBHelper.getInstance().getShareImageColumns().get(46));
        }
        return UpdateResult.defaultResult();
    }
}
