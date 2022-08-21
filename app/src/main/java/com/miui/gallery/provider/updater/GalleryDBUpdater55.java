package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.updater.UpdateResult;

/* loaded from: classes2.dex */
public class GalleryDBUpdater55 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.createTable(supportSQLiteDatabase, "peopleFace", GalleryDBHelper.getInstance().getPeopleFaceColumns());
        GalleryDBHelper.createTable(supportSQLiteDatabase, "faceToImages", GalleryDBHelper.getInstance().getFace2ImagesColumns());
        return new UpdateResult.Builder().recreateTablePeopleFace().build();
    }
}
