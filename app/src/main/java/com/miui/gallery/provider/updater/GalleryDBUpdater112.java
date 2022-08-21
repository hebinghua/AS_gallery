package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater112 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableAlbum()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "album", new TableColumn.Builder().setName("scan_public_media_count").setType("INTEGER").setDefaultValue(String.valueOf(0)).build());
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "album", new TableColumn.Builder().setName("scan_public_media_generation_modified").setType("INTEGER").setDefaultValue(String.valueOf(0)).build());
        }
        return UpdateResult.defaultResult();
    }
}
