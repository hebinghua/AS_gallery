package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater99 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (updateResult.isRecreateTableCloud()) {
            return UpdateResult.defaultResult();
        }
        GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("realSize").setType("INTEGER").build());
        GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("realDateModified").setType("INTEGER").build());
        supportSQLiteDatabase.execSQL("UPDATE cloud SET (realSize, realDateModified)=(size, dateModified)");
        return UpdateResult.defaultResult();
    }
}
