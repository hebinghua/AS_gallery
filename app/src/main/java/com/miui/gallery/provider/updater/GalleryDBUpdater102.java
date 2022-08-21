package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.SQLiteView;

/* loaded from: classes2.dex */
public class GalleryDBUpdater102 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        SQLiteView.of("extended_cloud").createByVersion(supportSQLiteDatabase, 5);
        SQLiteView.of("extended_faceImage").createByVersion(supportSQLiteDatabase, 5);
        return UpdateResult.defaultResult();
    }
}
