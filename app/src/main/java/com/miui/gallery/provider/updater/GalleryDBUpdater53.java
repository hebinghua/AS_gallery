package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater53 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_cloud_size", "cloud", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.createIndex(supportSQLiteDatabase, "index_shareimage_size", "shareImage", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        }
        return UpdateResult.defaultResult();
    }
}
