package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.LocationGenerator;
import com.miui.gallery.util.SafeDBUtil;

/* loaded from: classes2.dex */
public class GalleryDBUpdater83 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        return i >= 74;
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("location");
        SafeDBUtil.safeUpdate(supportSQLiteDatabase, "cloud", contentValues, (String) null, (String[]) null);
        LocationGenerator.getInstance().generate(GalleryApp.sGetAndroidContext());
        return UpdateResult.defaultResult();
    }
}
