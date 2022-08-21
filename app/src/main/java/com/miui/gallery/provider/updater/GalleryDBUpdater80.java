package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater80 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        return i >= 74;
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "update %s set %s=(%s & ~%d)  where %s=%s and (%s is null or %s!='%s')", "cloud", "attributes", "attributes", 4L, "serverType", 0, "serverId", "serverId", 1L));
        return UpdateResult.defaultResult();
    }
}
