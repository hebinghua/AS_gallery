package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater85 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.createTable(supportSQLiteDatabase, "discoveryMessage", GalleryDBHelper.getInstance().getDiscoveryMessageColumns());
        GalleryDBHelper.createTable(supportSQLiteDatabase, "recentDiscoveredMedia", GalleryDBHelper.getInstance().getRecentDiscoveredMediaColumns());
        return UpdateResult.defaultResult();
    }
}
