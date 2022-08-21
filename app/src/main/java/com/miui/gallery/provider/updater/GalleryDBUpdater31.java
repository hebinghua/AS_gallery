package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater31 extends GalleryDBUpdater {
    public int oldVersion;

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        this.oldVersion = i;
        return super.handle(i);
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(51));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(52));
        }
        if (!updateResult.isRecreateTableShareAlbum()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(22));
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(23));
        }
        if (this.oldVersion >= 21) {
            GalleryDBHelper.cleanCloudData(supportSQLiteDatabase);
        }
        return UpdateResult.defaultResult();
    }
}
