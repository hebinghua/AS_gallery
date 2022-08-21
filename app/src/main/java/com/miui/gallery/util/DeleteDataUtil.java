package com.miui.gallery.util;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Process;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.cloud.CloudTableUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.datalayer.utils.AlbumFileCache;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.ThumbnailPreference;
import com.miui.gallery.preference.ThumbnailWritePreference;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.SearchHistoryManager;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.peoplecover.LocalPeopleCoverManager;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android30.PermissionUtils;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/* loaded from: classes2.dex */
public class DeleteDataUtil {
    public static /* synthetic */ Object $r8$lambda$BArBsz_mBmSH6PXu76yruTok5PE(ArrayList arrayList, Cursor cursor) {
        return lambda$deleteFileByUri$1(arrayList, cursor);
    }

    public static boolean delete(Context context, int i) {
        if (context == null) {
            return false;
        }
        SyncUtil.stopSync(context);
        ImageDownloader.getInstance().cancelAll();
        boolean deleteDB = deleteDB(context, i) & deleteFile(context, i) & true & deletePreferences();
        ScannerEngine.getInstance().triggerScan();
        return deleteDB;
    }

    public static boolean deleteFile(Context context, int i) {
        if (i == 0 || i == 1) {
            return deleteHiddenFiles(context);
        }
        if (i == 2) {
            return deleteSyncedFile(context);
        }
        throw new IllegalArgumentException("unsupported policy: " + i);
    }

    public static boolean deleteDB(Context context, int i) {
        Preference.setSyncShouldClearDataBase(true);
        boolean resetCloudDataBase = resetCloudDataBase(context);
        if (resetCloudDataBase) {
            SyncLogger.d("DeleteDataUtil", "delete cloud success");
            Preference.setSyncShouldClearDataBase(false);
        } else {
            SyncLogger.e("DeleteDataUtil", "delete cloud failed");
        }
        boolean resetMediaRemarkInfo = resetCloudDataBase & resetMediaRemarkInfo();
        if (resetMediaRemarkInfo) {
            SyncLogger.d("DeleteDataUtil", "delete MediaRemarkInfo end.");
        } else {
            SyncLogger.e("DeleteDataUtil", "delete MediaRemarkInfo DB failed!");
        }
        boolean resetCloudControlData = resetMediaRemarkInfo & resetCloudControlData();
        if (resetCloudControlData) {
            SyncLogger.d("DeleteDataUtil", "delete CloudControl DB end.");
        } else {
            SyncLogger.e("DeleteDataUtil", "delete CloudControl DB failed!");
        }
        boolean resetFavoritesData = resetCloudControlData & resetFavoritesData();
        if (resetFavoritesData) {
            SyncLogger.d("DeleteDataUtil", "delete Favorites DB end.");
        } else {
            SyncLogger.e("DeleteDataUtil", "delete Favorites DB failed!");
        }
        CardManager.getInstance().onAccountDelete();
        PersistentResponseHelper.clearData();
        LocalPeopleCoverManager.getInstance().onAccountDelete();
        SearchHistoryManager.getInstance().deleteAll();
        TrashManager.getInstance().onAccountDelete(i);
        return resetFavoritesData;
    }

    public static boolean deletePreferences() {
        IntentUtil.removeAllShortCutForBabyAlbums(GalleryApp.sGetAndroidContext());
        Preference.sRemoveCloudSettings();
        GalleryPreferences.sRemoveCloudSettings();
        ThumbnailPreference.clear();
        ThumbnailWritePreference.clear();
        AlbumFileCache.getInstance().clear();
        GalleryPreferences.MiCloud.setCloudSpaceAlmostFullTipCounts(0);
        GalleryPreferences.MiCloud.setCloudSpaceFullTipLastCheckTime(0L);
        SyncLogger.d("DeleteDataUtil", "removed cloud settings.");
        return true;
    }

    public static boolean resetCloudControlData() {
        CloudControlManager.getInstance().clearCloudCache();
        for (String str : GalleryDBHelper.getCloudControlTables()) {
            Uri build = GalleryCloudUtils.BASE_URI.buildUpon().appendPath(str).build();
            int safeDelete = GalleryUtils.safeDelete(build, null, null);
            SyncLogger.d("DeleteDataUtil", String.format("clean %s finished, deleted rows=%d", build, Integer.valueOf(safeDelete)));
            if (safeDelete == -1) {
                return false;
            }
        }
        return true;
    }

    public static boolean resetFavoritesData() {
        int safeDelete = GalleryUtils.safeDelete(GalleryContract.Favorites.URI, null, null);
        SyncLogger.d("DeleteDataUtil", String.format(Locale.US, "clean favorites finished, deleted rows=%d", Integer.valueOf(safeDelete)));
        return safeDelete != -1;
    }

    public static boolean resetMediaRemarkInfo() {
        return RemarkManager.onAccountDelete();
    }

    public static boolean resetCloudDataBase(Context context) {
        String[] cloudTables;
        resetTableCloud(context);
        deleteEmptyAlbum(context);
        boolean z = true;
        for (String str : GalleryDBHelper.getCloudTables()) {
            if (!TextUtils.equals(str, "cloud") && !TextUtils.equals(str, "album")) {
                Uri build = GalleryCloudUtils.BASE_URI.buildUpon().appendPath(str).build();
                int safeDelete = GalleryUtils.safeDelete(build, null, null);
                SyncLogger.d("DeleteDataUtil", String.format(Locale.US, "clean %s finished, deleted rows=%d", build, Integer.valueOf(safeDelete)));
                if (safeDelete == -1) {
                    z = false;
                }
            }
        }
        return z;
    }

    public static void resetTableCloud(Context context) {
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("serverTag");
        contentValues.put("serverStatus", "custom");
        contentValues.put("localFlag", (Integer) 0);
        Uri uri = GalleryContract.Album.URI;
        arrayList.add(ContentProviderOperation.newUpdate(uri).withValues(contentValues).withSelection(String.format("%s IN (%s)", "serverId", TextUtils.join(",", CloudTableUtils.getKeepServerIdAlbums())), null).build());
        SyncLogger.d("DeleteDataUtil", "update system album in cloud table");
        ContentValues contentValues2 = new ContentValues();
        contentValues2.putNull("serverId");
        contentValues2.putNull("serverTag");
        contentValues2.putNull("serverStatus");
        contentValues2.put("localFlag", (Integer) 7);
        Locale locale = Locale.US;
        arrayList.add(ContentProviderOperation.newUpdate(uri).withValues(contentValues2).withSelection(String.format(locale, "%s IS NOT NULL AND %s NOT IN (%s)", "serverId", "serverId", TextUtils.join(",", CloudTableUtils.getKeepServerIdAlbums())), null).build());
        SyncLogger.d("DeleteDataUtil", "update other albums");
        String str = CloudUtils.itemIsNotGroup;
        String format = String.format(locale, " %s AND (%s OR %s)", str, CloudUtils.SELECTION_ITEM_WITHOUT_VALID_FILE, CloudUtils.SELECTION_SERVER_STATUS_CLEAR);
        Uri uri2 = GalleryContract.Cloud.CLOUD_URI;
        arrayList.add(ContentProviderOperation.newDelete(uri2).withSelection(format, null).build());
        SyncLogger.d("DeleteDataUtil", "delete rows without valid file");
        ContentValues contentValues3 = new ContentValues();
        contentValues3.putNull("duration");
        contentValues3.put("serverType", (Integer) 1);
        contentValues3.put("mimeType", "image/jpeg");
        arrayList.add(ContentProviderOperation.newUpdate(uri2).withValues(contentValues3).withSelection(String.format(locale, " %s AND (%s != %d) AND (%s IS NULL OR %s = '')", CloudUtils.itemIsVideo, "localFlag", 2, "localFile", "localFile"), null).build());
        SyncLogger.d("DeleteDataUtil", "update videos ");
        ContentValues contentValues4 = new ContentValues();
        CloudUtils.putServerColumnsNull(contentValues4);
        contentValues4.putNull("microthumbfile");
        contentValues4.putNull("editedColumns");
        contentValues4.putNull("description");
        contentValues4.put("localFlag", (Integer) 7);
        arrayList.add(ContentProviderOperation.newUpdate(uri2).withValues(contentValues4).withSelection(String.format(locale, "%s AND (%s != %d) AND %s IS NOT NULL", str, "localFlag", 2, "serverId"), null).build());
        try {
            context.getContentResolver().applyBatch("com.miui.gallery.cloud.provider", arrayList);
            List list = (List) SafeDBUtil.safeQuery(context, uri2, new String[]{j.c}, String.format(locale, "%s AND  (%s != %d) AND  (%s IS NULL OR %s = '')", str, "localFlag", 2, "localFile", "localFile"), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.util.DeleteDataUtil.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public List<Long> mo1808handle(Cursor cursor) {
                    ArrayList arrayList2 = new ArrayList();
                    if (cursor == null || !cursor.moveToFirst()) {
                        return arrayList2;
                    }
                    do {
                        arrayList2.add(Long.valueOf(cursor.getLong(0)));
                    } while (cursor.moveToNext());
                    return arrayList2;
                }
            });
            if (BaseMiscUtil.isValid(list)) {
                String format2 = String.format("%s IN (%s)", j.c, TextUtils.join(",", list));
                GalleryUtils.safeExec(String.format(locale, "UPDATE %s SET %s = %s WHERE %s", "cloud", "localFile", "thumbnailFile", format2));
                MediaManager.getInstance().delete(format2, null);
                MediaManager.getInstance().insert(GalleryDBHelper.getInstance(context).getWritableDatabase(), format2, null);
                context.getContentResolver().notifyChange(uri2, null);
                context.getContentResolver().notifyChange(uri, null);
                context.getContentResolver().notifyChange(GalleryContract.Media.URI, null);
            }
            SyncLogger.d("DeleteDataUtil", "update media");
        } catch (Exception e) {
            SyncLogger.d("DeleteDataUtil", "fail do reset cloud table", e);
        }
    }

    public static void deleteEmptyAlbum(final Context context) {
        SafeDBUtil.safeQuery(context, GalleryContract.Album.URI, new String[]{j.c}, "_id IN (SELECT _id FROM ((" + InternalContract$Album.SQL_QUERY_NON_SYSTEM_ALBUMS + ") AS Album LEFT JOIN (SELECT localGroupId, count(_id) AS media_count FROM cloud WHERE serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) GROUP BY localGroupId) AS MediaCount ON Album." + j.c + "=MediaCount.localGroupId) WHERE media_count IS NULL OR media_count=0)", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Cursor>() { // from class: com.miui.gallery.util.DeleteDataUtil.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Cursor mo1808handle(Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {
                    LinkedList linkedList = new LinkedList();
                    do {
                        linkedList.add(Long.valueOf(cursor.getLong(0)));
                    } while (cursor.moveToNext());
                    int safeDelete = SafeDBUtil.safeDelete(context, GalleryContract.Album.URI, "_id IN (" + TextUtils.join(",", linkedList) + ")", null);
                    if (safeDelete >= 0) {
                        SyncLogger.d("DeleteDataUtil", String.format(Locale.US, "clean empty album finished, deleted rows=%d", Integer.valueOf(safeDelete)));
                    } else {
                        SyncLogger.e("DeleteDataUtil", "clean empty album failed");
                    }
                }
                return cursor;
            }
        });
    }

    public static boolean deleteHiddenFiles(Context context) {
        SyncLogger.d("DeleteDataUtil", "delete hidden files");
        boolean z = true;
        try {
            File[] listFiles = new File(StaticContext.sGetAndroidContext().getExternalCacheDir().getAbsolutePath()).listFiles();
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("DeleteDataUtil", "deleteHiddenFiles");
            if (listFiles != null) {
                boolean z2 = true;
                for (File file : listFiles) {
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    boolean z3 = documentFile != null && documentFile.delete();
                    z2 &= z3;
                    SyncLogger.d("DeleteDataUtil", "delete %s result %s", file.getName(), Boolean.valueOf(z3));
                }
                z = z2;
            }
            deleteSyncedSecretFiles();
            deleteSyncedItemsWithoutLocalFile();
            return z;
        } catch (NullPointerException e) {
            SyncLogger.d("DeleteDataUtil", TextUtils.join("\n", e.getStackTrace()));
            return true;
        }
    }

    public static void deleteSyncedSecretFiles() {
        GalleryUtils.safeQuery(GalleryCloudUtils.CLOUD_URI, new String[]{"localFlag", "thumbnailFile", "localFile", j.c}, "localGroupId = -1000 AND (localFlag != ?) AND (localFlag != ?)", new String[]{String.valueOf(7), String.valueOf(8)}, (String) null, DeleteDataUtil$$ExternalSyntheticLambda1.INSTANCE);
    }

    public static /* synthetic */ Object lambda$deleteSyncedSecretFiles$0(Cursor cursor) {
        if (cursor != null) {
            ArrayList arrayList = new ArrayList(cursor.getCount());
            while (cursor.moveToNext()) {
                String string = cursor.getString(1);
                String string2 = cursor.getString(2);
                if (!TextUtils.isEmpty(string2)) {
                    deleteFileUnderSecretAlbum(string2);
                }
                if (!TextUtils.isEmpty(string)) {
                    deleteFileUnderSecretAlbum(string);
                }
                arrayList.add(Long.valueOf(cursor.getLong(3)));
            }
            deleteFromDBByIds(GalleryCloudUtils.CLOUD_URI, arrayList);
            return null;
        }
        return null;
    }

    public static void deleteSyncedItemsWithoutLocalFile() {
        deleteFileByUri(GalleryCloudUtils.CLOUD_URI, "serverStatus = 'custom' AND (serverId != '' AND serverId IS NOT NULL) AND (localFile = '' OR localFile IS NULL) AND serverType != 0");
    }

    public static void deleteFileUnderSecretAlbum(String str) {
        if (!ExtraTextUtils.endsWithIgnoreCase(BaseFileUtils.getParentFolderPath(str), "MIUI/Gallery/cloud/secretAlbum")) {
            return;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("DeleteDataUtil", "deleteFileUnderSecretAlbum"));
        if (documentFile != null) {
            documentFile.delete();
        }
        SyncLogger.d("DeleteDataUtil", "Delete secret album file [%s]", str);
    }

    public static boolean deleteSyncedFile(Context context) {
        SyncLogger.d("DeleteDataUtil", "delete sync files");
        Uri[] uriArr = {GalleryCloudUtils.CLOUD_URI, GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI, GalleryCloudUtils.SHARE_IMAGE_URI, GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI};
        for (int i = 0; i < 4; i++) {
            Uri uri = uriArr[i];
            deleteFileByUri(uri, "serverStatus = 'custom' AND (" + CloudTableUtils.sItemIsNotGroup + ")");
        }
        return true;
    }

    public static void deleteFileByUri(Uri uri, String str) {
        final ArrayList arrayList = new ArrayList(100);
        GalleryUtils.safeQuery(uri, new String[]{"microthumbfile", "thumbnailFile", "localFile", j.c}, str, (String[]) null, (String) null, new GalleryUtils.QueryHandler() { // from class: com.miui.gallery.util.DeleteDataUtil$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle */
            public final Object mo1712handle(Cursor cursor) {
                return DeleteDataUtil.$r8$lambda$BArBsz_mBmSH6PXu76yruTok5PE(arrayList, cursor);
            }
        });
    }

    public static /* synthetic */ Object lambda$deleteFileByUri$1(ArrayList arrayList, Cursor cursor) {
        if (cursor != null) {
            ArrayList arrayList2 = new ArrayList(cursor.getCount());
            long currentTimeMillis = System.currentTimeMillis();
            boolean z = false;
            int i = 0;
            int i2 = 0;
            while (cursor.moveToNext()) {
                String string = cursor.getString(0);
                String string2 = cursor.getString(1);
                String string3 = cursor.getString(2);
                if (!TextUtils.isEmpty(string)) {
                    z = true;
                }
                if (!TextUtils.isEmpty(string2)) {
                    i++;
                    arrayList.add(string2);
                }
                if (!TextUtils.isEmpty(string3)) {
                    i2++;
                    arrayList.add(string3);
                }
                if (arrayList.size() + 3 > 100 || (cursor.isLast() && !arrayList.isEmpty())) {
                    MediaStoreUtils.makeInvalid(GalleryApp.sGetAndroidContext(), arrayList);
                    arrayList.clear();
                }
                arrayList2.add(Long.valueOf(cursor.getLong(3)));
            }
            if (z) {
                GalleryPreferences.Sync.setNeedCleanMicroThumb(true);
            }
            DefaultLogger.d("DeleteDataUtil", "delete %d local, %d thumb, %d micro, cost %d", Integer.valueOf(i2), Integer.valueOf(i), 0, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            deleteFromDBByIds(GalleryCloudUtils.CLOUD_URI, arrayList2);
            return null;
        }
        return null;
    }

    public static void deleteFromDBByIds(Uri uri, List<Long> list) {
        if (!BaseMiscUtil.isValid(list) || uri == null) {
            return;
        }
        long[] jArr = new long[list.size()];
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("serverId");
        contentValues.putNull("serverTag");
        contentValues.put("localFlag", (Integer) (-1));
        contentValues.put("serverStatus", "cleanLocal");
        contentValues.put("localGroupId", (Long) (-1L));
        GalleryUtils.safeUpdate(uri, contentValues, String.format("%s IN (%s) AND %s IS NOT NULL ", j.c, TextUtils.join(",", list), "serverId"), null);
        DeletionTask deletionTask = new DeletionTask();
        deletionTask.setOnDoProcessExceptionHandler(new ProcessTask.OnDoProcessExceptionHandler<long[]>() { // from class: com.miui.gallery.util.DeleteDataUtil.3
            @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
            public long[] handle(Exception exc) {
                return null;
            }

            @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
            public boolean shouldHandle(Exception exc) {
                return exc.getCause() instanceof StoragePermissionMissingException;
            }
        });
        deletionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new DeletionTask.Param(MiscUtil.ListToArray(list), -1L, "", 59));
        SyncLogger.d("DeleteDataUtil", "delete %d rows from %s", Integer.valueOf(list.size()), uri);
    }

    public static void deleteMicroThumb() {
        File[] listFiles;
        DocumentFile documentFile;
        SyncLogger.d("DeleteDataUtil", "deleteMicroThumb");
        if (!GalleryPreferences.Sync.isNeedCleanMicroThumb()) {
            return;
        }
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        String[] pathsInExternalStorage = StorageUtils.getPathsInExternalStorage(sGetAndroidContext, "/Android/data/com.miui.gallery/cache/microthumbnailFile");
        if (pathsInExternalStorage == null || pathsInExternalStorage.length <= 0) {
            GalleryPreferences.Sync.setNeedCleanMicroThumb(false);
            return;
        }
        Set<String> queryExistMicroFiles = queryExistMicroFiles(sGetAndroidContext);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("DeleteDataUtil", "deleteMicroThumb");
        for (String str : pathsInExternalStorage) {
            File file = new File(str);
            if (file.exists() && file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                for (File file2 : listFiles) {
                    if (file2 != null && file2.isFile() && !file2.getPath().contains(".nomedia") && ((queryExistMicroFiles == null || !queryExistMicroFiles.contains(file2.getAbsolutePath())) && (documentFile = StorageSolutionProvider.get().getDocumentFile(file2.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null)) {
                        documentFile.delete();
                    }
                }
            }
        }
        GalleryPreferences.Sync.setNeedCleanMicroThumb(false);
    }

    public static Set<String> queryExistMicroFiles(Context context) {
        return (Set) SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, new String[]{"microthumbfile"}, "microthumbfile IS NOT NULL AND serverStatus != 'deleted'", (String[]) null, (String) null, DeleteDataUtil$$ExternalSyntheticLambda2.INSTANCE);
    }

    public static /* synthetic */ Set lambda$queryExistMicroFiles$2(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return null;
        }
        HashSet hashSet = new HashSet();
        while (cursor.moveToNext()) {
            String string = cursor.getString(0);
            if (!TextUtils.isEmpty(string)) {
                hashSet.add(string);
            }
        }
        return hashSet;
    }

    /* loaded from: classes2.dex */
    public static class DeletePathQueryJob implements ThreadPool.Job<List<String>> {
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public List<String> mo1807run(ThreadPool.JobContext jobContext) {
            final Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            return (List) SafeDBUtil.safeQuery(sGetAndroidContext, GalleryContract.Cloud.CLOUD_URI, new String[]{"localFile", "thumbnailFile", "localGroupId"}, "localFlag = -1 AND serverStatus = 'cleanLocal'", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<String>>() { // from class: com.miui.gallery.util.DeleteDataUtil.DeletePathQueryJob.1
                {
                    DeletePathQueryJob.this = this;
                }

                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public List<String> mo1808handle(Cursor cursor) {
                    String concat;
                    String concat2;
                    ArrayList arrayList = new ArrayList();
                    if (cursor == null || !cursor.moveToFirst()) {
                        return arrayList;
                    }
                    boolean checkWriteImagesOrVideoAppOps = PermissionUtils.checkWriteImagesOrVideoAppOps(sGetAndroidContext, Process.myUid(), "com.miui.gallery");
                    do {
                        boolean z = false;
                        String string = cursor.getString(0);
                        String string2 = cursor.getString(1);
                        if (cursor.getLong(2) == -1000) {
                            z = true;
                        }
                        if (z) {
                            if (!TextUtils.isEmpty(string)) {
                                if (BaseFileMimeUtil.isMediaTypeFromPath(string)) {
                                    concat2 = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(string), "1.jpg");
                                } else {
                                    concat2 = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(string), "1.txt");
                                }
                                if (!arrayList.contains(concat2) && new File(string).exists() && !StorageSolutionProvider.get().checkPermission(concat2, IStoragePermissionStrategy.Permission.DELETE).granted) {
                                    arrayList.add(concat2);
                                    SyncLogger.d("DeleteDataUtil", "add path, %s", concat2);
                                }
                            }
                            if (!TextUtils.isEmpty(string2)) {
                                if (BaseFileMimeUtil.isMediaTypeFromPath(string2)) {
                                    concat = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(string2), "1.jpg");
                                } else {
                                    concat = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(string2), "1.txt");
                                }
                                if (!arrayList.contains(concat) && new File(string2).exists() && !StorageSolutionProvider.get().checkPermission(concat, IStoragePermissionStrategy.Permission.DELETE).granted) {
                                    arrayList.add(concat);
                                    SyncLogger.d("DeleteDataUtil", "add path, %s", concat);
                                }
                            }
                        } else if (!checkWriteImagesOrVideoAppOps) {
                            if (!TextUtils.isEmpty(string)) {
                                String concat3 = BaseFileUtils.concat(BaseFileUtils.getAbsoluteRootParentFolderPath(GalleryApp.sGetAndroidContext(), string), "1.jpg");
                                if (!arrayList.contains(concat3) && new File(string).exists() && !StorageSolutionProvider.get().checkPermission(concat3, IStoragePermissionStrategy.Permission.DELETE).granted) {
                                    arrayList.add(concat3);
                                    SyncLogger.d("DeleteDataUtil", "add path, %s", concat3);
                                }
                            }
                            if (!TextUtils.isEmpty(string2)) {
                                String concat4 = BaseFileUtils.concat(BaseFileUtils.getAbsoluteRootParentFolderPath(GalleryApp.sGetAndroidContext(), string2), "1.jpg");
                                if (!arrayList.contains(concat4) && new File(string2).exists() && !StorageSolutionProvider.get().checkPermission(concat4, IStoragePermissionStrategy.Permission.DELETE).granted) {
                                    arrayList.add(concat4);
                                    SyncLogger.d("DeleteDataUtil", "add path, %s", concat4);
                                }
                            }
                        }
                    } while (cursor.moveToNext());
                    return arrayList;
                }
            });
        }
    }
}
