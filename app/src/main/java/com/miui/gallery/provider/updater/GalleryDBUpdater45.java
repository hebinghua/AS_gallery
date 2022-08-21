package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.util.deprecated.Time;

/* loaded from: classes2.dex */
public class GalleryDBUpdater45 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, Time.getUpgradeMixedDateTimeSqlString("cloud"));
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, Time.getUpgradeMixedDateTimeSqlString("shareImage"));
        return UpdateResult.defaultResult();
    }
}
