package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater101 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (updateResult.isRecreateTableCloud()) {
            return UpdateResult.defaultResult();
        }
        GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_local_group_id", "cloud", "localGroupId");
        return UpdateResult.defaultResult();
    }
}
