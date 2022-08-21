package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.data.PackageGenerator;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater104 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("source_pkg").setType("TEXT").build());
            PackageGenerator.getInstance().generate();
        }
        return UpdateResult.defaultResult();
    }
}
