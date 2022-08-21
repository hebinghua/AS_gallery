package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.util.SafeDBUtil;

/* loaded from: classes2.dex */
public class GalleryDBUpdater82 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (updateResult.isRecreateTableCloud()) {
            return UpdateResult.defaultResult();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("localGroupId", (Long) (-1000L));
        SafeDBUtil.safeUpdate(supportSQLiteDatabase, "cloud", contentValues, "groupId=?", new String[]{String.valueOf(1000L)});
        return UpdateResult.defaultResult();
    }
}
