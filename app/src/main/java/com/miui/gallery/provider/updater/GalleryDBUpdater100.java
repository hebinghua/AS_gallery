package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater100 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "album");
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "newFlagCache");
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "whiteListLastModify");
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "photoGpsCache");
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "localUbifocus");
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "event");
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "eventPhoto");
        GalleryDBHelper.dropTable(supportSQLiteDatabase, "albumCoverKey");
        return UpdateResult.defaultResult();
    }
}
