package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.updater.UpdateResult;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class GalleryDBUpdater {
    public abstract UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult);

    public boolean handle(int i) {
        return true;
    }

    public final UpdateResult update(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        try {
            return doUpdate(supportSQLiteDatabase, updateResult);
        } catch (Throwable th) {
            DefaultLogger.e("GalleryDBUpdater", "[%s] failed since %s.", getClass().getSimpleName(), th.getMessage());
            return new UpdateResult.Builder().success(false).build();
        }
    }
}
