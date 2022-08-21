package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater72 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTablePeopleFace()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "peopleFace", GalleryDBHelper.getInstance().getPeopleFaceColumns().get(18));
        }
        return UpdateResult.defaultResult();
    }
}
