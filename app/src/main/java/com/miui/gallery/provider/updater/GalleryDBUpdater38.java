package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.content.Context;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class GalleryDBUpdater38 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloudSettings()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.getInstance().getCloudSettingColumns().get(11));
        }
        setAllSyncTagAsDefault(GalleryApp.sGetAndroidContext(), supportSQLiteDatabase);
        return UpdateResult.defaultResult();
    }

    public static void setAllSyncTagAsDefault(Context context, SupportSQLiteDatabase supportSQLiteDatabase) {
        DefaultLogger.d("GalleryDBUpdater38ConvertOldData", "setAllSyncTagAsDefault, url != 2.1");
        ContentValues contentValues = new ContentValues();
        contentValues.put("syncTag", (Integer) 0);
        contentValues.put("shareSyncTagAlbumList", (Integer) 0);
        contentValues.put("shareSyncTagAlbumInfo", (Integer) 0);
        contentValues.put("shareSyncTagImageList", (Integer) 0);
        supportSQLiteDatabase.update("cloudSetting", 0, contentValues, null, null);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("albumImageTag", (Integer) 0);
        supportSQLiteDatabase.update("shareAlbum", 0, contentValues2, null, null);
        SyncUtil.requestSync(context, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(Long.MAX_VALUE).build());
    }
}
