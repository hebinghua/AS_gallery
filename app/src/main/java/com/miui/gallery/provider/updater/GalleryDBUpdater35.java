package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater35 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_serverId", "cloud", "serverId", true);
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_albumId", "shareAlbum", "albumId", true);
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_shareId", "shareImage", "shareId", true);
        return UpdateResult.defaultResult();
    }
}
