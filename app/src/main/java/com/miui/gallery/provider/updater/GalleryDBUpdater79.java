package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.BackgroundJobService;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryDBUpdater79 extends GalleryDBUpdater {
    public int oldVersion;

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public boolean handle(int i) {
        this.oldVersion = i;
        return super.handle(i);
    }

    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("localGroupId", (Long) (-1000L));
            contentValues.putNull("thumbnailFile");
            SafeDBUtil.safeUpdate(supportSQLiteDatabase, "cloud", contentValues, "groupId=?", new String[]{String.valueOf(1000L)});
        }
        if (this.oldVersion > 72) {
            DefaultLogger.i("GalleryDBUpdater79", "delete secret thumbnail, because it has no sha1");
            BackgroundJobService.startJobDeleteSecretThumbnail(GalleryApp.sGetAndroidContext());
        }
        return UpdateResult.defaultResult();
    }
}
