package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;

/* loaded from: classes2.dex */
public class GalleryDBUpdater42 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        return i < 29 || i > 37;
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.cleanCloudData(supportSQLiteDatabase);
        Preference.setDBUpgradeTo42();
        SyncUtil.requestSync(GalleryApp.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(Long.MAX_VALUE).build());
        return UpdateResult.defaultResult();
    }
}
