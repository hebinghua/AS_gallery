package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.SQLiteView;

/* loaded from: classes2.dex */
public class GalleryDBUpdater91 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("specialTypeFlags").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", new TableColumn.Builder().setName("specialTypeFlags").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
        }
        SQLiteView.of("extended_cloud").createByVersion(supportSQLiteDatabase, 2);
        return UpdateResult.defaultResult();
    }
}
