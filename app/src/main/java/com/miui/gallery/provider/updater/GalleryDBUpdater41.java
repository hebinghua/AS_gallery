package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.deprecated.Storage;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater41 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        String format = String.format(Locale.US, " %s = %d AND ( %s != %d OR %s != %d OR %s != %d )", "serverType", 2, "localFlag", 7, "localFlag", 4, "localFlag", 8);
        ArrayList<String> videosExceptCreated = getVideosExceptCreated(supportSQLiteDatabase, "cloud", format);
        ArrayList<String> videosExceptCreated2 = getVideosExceptCreated(supportSQLiteDatabase, "shareImage", format);
        if ((videosExceptCreated == null || videosExceptCreated.isEmpty()) && (videosExceptCreated2 == null || videosExceptCreated2.isEmpty())) {
            DefaultLogger.d("GalleryDBUpdater41", "none video in database, skip delete and resync.");
            return UpdateResult.defaultResult();
        }
        String str = "DELETE FROM %s WHERE " + format;
        if (videosExceptCreated != null && !videosExceptCreated.isEmpty()) {
            supportSQLiteDatabase.execSQL(String.format(str, "cloud"));
            deleteVideoThumbnailFile(videosExceptCreated);
        }
        if (videosExceptCreated2 != null && !videosExceptCreated2.isEmpty()) {
            supportSQLiteDatabase.execSQL(String.format(str, "shareImage"));
            deleteVideoThumbnailFile(videosExceptCreated2);
        }
        Preference.sSetLastSlowScanTime(0L);
        setAllSyncTagAsDefault(GalleryApp.sGetAndroidContext(), supportSQLiteDatabase);
        return UpdateResult.defaultResult();
    }

    public final ArrayList<String> getVideosExceptCreated(SupportSQLiteDatabase supportSQLiteDatabase, String str, String str2) {
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder(str).columns(new String[]{"sha1"}).selection(str2, null).create());
        if (query != null) {
            try {
                ArrayList<String> arrayList = new ArrayList<>();
                while (query.moveToNext()) {
                    arrayList.add(query.getString(0));
                }
                return arrayList;
            } finally {
                query.close();
            }
        }
        return null;
    }

    public final void deleteVideoThumbnailFile(ArrayList<String> arrayList) {
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            StringBuilder sb = new StringBuilder();
            sb.append("/Android/data/com.miui.gallery/cache/microthumbnailFile");
            String str = File.separator;
            sb.append(str);
            sb.append(CloudUtils.getThumbnailNameBySha1(next));
            String pathInPriorStorage = StorageUtils.getPathInPriorStorage(sb.toString());
            if (pathInPriorStorage == null) {
                return;
            }
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("GalleryDBUpdater41", "deleteVideoThumbnailFile");
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
            DocumentFile documentFile = storageStrategyManager.getDocumentFile(pathInPriorStorage, permission, appendInvokerTag);
            if (documentFile != null) {
                documentFile.delete();
            }
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(Storage.getMainSDCardCloudThumbnailFilePath() + str + CloudUtils.getThumbnailNameBySha1(next), permission, appendInvokerTag);
            if (documentFile2 != null) {
                documentFile2.delete();
            }
        }
    }

    public static void setAllSyncTagAsDefault(Context context, SupportSQLiteDatabase supportSQLiteDatabase) {
        DefaultLogger.d("GalleryDBUpdater41ConvertOldData", "setAllSyncTagAsDefault, url != 2.1");
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
