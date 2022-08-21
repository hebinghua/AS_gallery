package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.InternalContract$Album;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater89 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        return i >= 73;
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            supportSQLiteDatabase.execSQL(String.format(Locale.US, "UPDATE %s SET attributes=(attributes | (((attributes & 2) | (NOT (attributes & 1))) << 6))  WHERE serverType=0 AND " + InternalContract$Album.ALIAS_NON_SYSTEM_ALBUM, "cloud"));
        }
        return UpdateResult.defaultResult();
    }
}
