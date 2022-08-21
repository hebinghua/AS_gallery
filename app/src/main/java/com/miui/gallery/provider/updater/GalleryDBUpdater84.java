package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater84 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            supportSQLiteDatabase.execSQL(String.format(Locale.US, "update %s set attributes=( ((attributes & 1) * 1) | ((attributes & 2) * 2) | ((attributes & 4) * 4) ) where attributes > 0", "cloud"));
        }
        if (!updateResult.isRecreateTableShareAlbum()) {
            supportSQLiteDatabase.execSQL(String.format(Locale.US, "update %s set attributes=( ((attributes & 1) * 1) | ((attributes & 2) * 2) | ((attributes & 4) * 4) ) where attributes > 0", "shareAlbum"));
        }
        return UpdateResult.defaultResult();
    }
}
