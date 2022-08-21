package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.SQLiteView;

/* loaded from: classes2.dex */
public class GalleryDBUpdater95 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (updateResult.isRecreateTablePeopleFace()) {
            return UpdateResult.defaultResult();
        }
        GalleryDBHelper.addColumn(supportSQLiteDatabase, "peopleFace", new TableColumn.Builder().setName("selectCoverId").setType("TEXT").build());
        SQLiteView.of("extended_faceImage").createByVersion(supportSQLiteDatabase, 3);
        return UpdateResult.defaultResult();
    }
}
