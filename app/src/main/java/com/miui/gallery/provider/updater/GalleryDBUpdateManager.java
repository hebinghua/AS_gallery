package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryDBUpdateManager {
    public static UpdateResult update(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        UpdateResult defaultResult = UpdateResult.defaultResult();
        int i3 = i;
        while (true) {
            i3++;
            if (i3 <= i2) {
                StrategyRegistry strategyRegistry = StrategyRegistry.getInstance();
                GalleryDBUpdater galleryDBUpdater = (GalleryDBUpdater) strategyRegistry.get("GalleryDBUpdater" + i3);
                if (galleryDBUpdater != null && galleryDBUpdater.handle(i)) {
                    DefaultLogger.d("GalleryDBUpdateManager", "upgrade to [%d].", Integer.valueOf(i3));
                    defaultResult.merge(galleryDBUpdater.update(supportSQLiteDatabase, defaultResult));
                }
            } else {
                return defaultResult;
            }
        }
    }
}
