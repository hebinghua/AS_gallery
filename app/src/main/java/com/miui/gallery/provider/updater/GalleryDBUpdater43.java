package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes2.dex */
public class GalleryDBUpdater43 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        String format = String.format(Locale.US, "CASE WHEN %1$s IS NULL THEN %2$s ELSE strftime('%%s000', substr(%1$s, 0, 5)||'-'||substr(%1$s, 6, 2)||'-'||substr(%1$s,9,2)||' '||substr(%1$s, 11, 9)||'.000')%3$+d END", "exifDateTime", "dateModified", Integer.valueOf(TimeZone.getDefault().getRawOffset()));
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", GalleryDBHelper.getInstance().getCloudColumns().get(55));
            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("UPDATE %s SET %s = %s", "cloud", "mixedDateTime", format));
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", GalleryDBHelper.getInstance().getShareImageColumns().get(49));
            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("UPDATE %s SET %s = %s", "shareImage", "mixedDateTime", format));
        }
        return UpdateResult.defaultResult();
    }
}
