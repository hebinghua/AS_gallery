package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.provider.GalleryDBHelper;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater78 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        return i >= 74;
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (updateResult.isRecreateTableCloud()) {
            return UpdateResult.defaultResult();
        }
        Locale locale = Locale.US;
        String format = String.format(locale, "%s=%s AND (%s = %d OR %s=%d OR %s=%d OR (%s=%d AND %s='%s')) AND %s NOT NULL AND %s & %d = 0", "serverType", 0, "localFlag", 7, "localFlag", 8, "localFlag", 10, "localFlag", 0, "serverStatus", "custom", "babyInfoJson", "attributes", 1L);
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(6);
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(locale, "update %s set %s=(%s | %d), %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s", "cloud", "attributes", "attributes", 1L, "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, format));
        return UpdateResult.defaultResult();
    }
}
