package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.os.Device;

/* loaded from: classes2.dex */
public class GalleryDBUpdater37 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        replaceHongMiFilePath(supportSQLiteDatabase);
        return UpdateResult.defaultResult();
    }

    public final void replaceHongMiFilePath(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (Device.IS_HONGMI) {
            String[] strArr = {"localFile", "thumbnailFile", "microthumbfile"};
            for (int i = 0; i < 3; i++) {
                String str = strArr[i];
                supportSQLiteDatabase.execSQL(String.format("update %s set %s = substr(%s, 0, 16) || (substr(%s, 10, 7) = 'sdcard0') ||  substr(%s, 17) where substr(%s, 10, 7) = 'sdcard1' OR substr(%s, 10, 7) = 'sdcard0' ", "cloud", str, str, str, str, str, str));
                supportSQLiteDatabase.execSQL(String.format("update %s set %s = substr(%s, 0, 16) || (substr(%s, 10, 7) = 'sdcard0') ||  substr(%s, 17) where substr(%s, 10, 7) = 'sdcard1' OR substr(%s, 10, 7) = 'sdcard0' ", "shareImage", str, str, str, str, str, str));
            }
        }
    }
}
