package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater88 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTablePeopleFace()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "peopleFace", new TableColumn.Builder().setName("relationText").setType("TEXT").build());
        }
        return UpdateResult.defaultResult();
    }
}
