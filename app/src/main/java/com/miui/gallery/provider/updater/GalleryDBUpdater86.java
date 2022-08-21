package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater86 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("extraGPS").setType("TEXT").build());
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("address").setType("TEXT").build());
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", new TableColumn.Builder().setName("extraGPS").setType("TEXT").build());
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", new TableColumn.Builder().setName("address").setType("TEXT").build());
        }
        return UpdateResult.defaultResult();
    }
}
