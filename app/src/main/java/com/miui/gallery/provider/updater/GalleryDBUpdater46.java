package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.util.deprecated.Preference;

/* loaded from: classes2.dex */
public class GalleryDBUpdater46 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloudSettings()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(12));
        }
        if (!updateResult.isRecreateTableShareAlbum()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.getInstance().getShareAlbumColumns().get(24));
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("syncInfo", "");
        supportSQLiteDatabase.update("cloudSetting", 0, contentValues, null, null);
        Preference.setSyncFetchSyncExtraInfoFromV2ToV3(true);
        return UpdateResult.defaultResult();
    }
}
