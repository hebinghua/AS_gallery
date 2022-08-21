package com.miui.gallery.provider.updater;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater106 extends GalleryDBUpdater {
    public static final long[] ATTRIBUTES_BITS = {16, 32, 64, 128, 2048, 4096};

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        long j = 0;
        for (long j2 : ATTRIBUTES_BITS) {
            j |= j2;
        }
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "UPDATE %s SET %s = (%s & ~%d) WHERE %s COLLATE NOCASE like '%s'", "album", "attributes", "attributes", Long.valueOf(j), "localPath", MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH));
        return UpdateResult.defaultResult();
    }
}
