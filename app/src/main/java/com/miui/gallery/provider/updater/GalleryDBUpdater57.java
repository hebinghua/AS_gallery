package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater57 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_serverId_peopleFace", "peopleFace", "serverId");
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_groupId_peopleFace", "peopleFace", "groupId");
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_faceId_faceToImages", "faceToImages", "faceId");
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_imageServerId_faceToImages", "faceToImages", "imageServerId");
        return UpdateResult.defaultResult();
    }
}
