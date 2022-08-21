package com.miui.gallery.scanner.core.scanner;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.backup.GalleryBackupHelper;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.cloudcontrol.strategies.WhiteAlbumsStrategy;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.ContentProviderBatchOperator;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.scanner.core.ScanContracts$SQL;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.core.bulkoperator.IBulkInserter;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.NoMediaUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/* loaded from: classes2.dex */
public class MediaScannerHelper {
    public static final Map<String, Boolean> sScannableDirectoryCache = new Hashtable();

    public static boolean isScannableDirectory(File file) {
        if (file == null) {
            return false;
        }
        Map<String, Boolean> map = sScannableDirectoryCache;
        Boolean bool = map.get(file.getAbsolutePath());
        if (bool != null) {
            return bool.booleanValue();
        }
        File file2 = new File(file, ".nomedia");
        String relativePath = StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), file.getAbsolutePath());
        if (relativePath == null) {
            map.put(file.getAbsolutePath(), Boolean.FALSE);
            return false;
        }
        AlbumsStrategy.Album albumByPath = CloudControlStrategyHelper.getAlbumByPath(StorageUtils.ensureCommonRelativePath(relativePath));
        if (!isRoot(relativePath) && !isSystemAlbum(relativePath) && (albumByPath == null || !albumByPath.getAttributes().isInWhiteList())) {
            for (File file3 = file; file3 != null; file3 = file3.getParentFile()) {
                if (!StorageUtils.isInExternalStorage(GalleryApp.sGetAndroidContext(), file3.getAbsolutePath())) {
                    sScannableDirectoryCache.put(file.getAbsolutePath(), Boolean.TRUE);
                    return true;
                } else if (!checkDirectoryAccessible(file3)) {
                    sScannableDirectoryCache.put(file.getAbsolutePath(), Boolean.FALSE);
                    return false;
                }
            }
            return true;
        }
        if (file2.exists() && !NoMediaUtil.isManualHideAlbum(relativePath)) {
            NoMediaUtil.removeNoMediaForFolder(file.getAbsolutePath());
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("MediaScannerHelper", "isScannableDirectory"));
            if (documentFile != null) {
                StorageSolutionProvider.get().apply(documentFile);
            }
            DefaultLogger.d("MediaScannerHelper", "Directory [%s] is in root/system album/white list but contains .nomedia file, remove it.", file.getAbsolutePath());
            HashMap hashMap = new HashMap();
            hashMap.put(Action.FILE_ATTRIBUTE, relativePath);
            SamplingStatHelper.recordCountEvent("media_scanner", "unexpected_nomedia_found", hashMap);
        }
        map.put(file.getAbsolutePath(), Boolean.TRUE);
        return true;
    }

    public static boolean checkDirectoryAccessible(File file) {
        File file2 = new File(file, ".nomedia");
        String relativePath = StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), file.getAbsolutePath());
        if (file2.exists() && !NoMediaUtil.isManualHideAlbum(relativePath)) {
            DefaultLogger.d("MediaScannerHelper", "Directory [%s] contains .nomedia file, skip scan", file.getAbsolutePath());
            return false;
        } else if (file.isHidden()) {
            DefaultLogger.d("MediaScannerHelper", "Directory [%s] is hidden, skip scan", file.getAbsolutePath());
            return false;
        } else if (StorageUtils.isInExternalStorage(GalleryApp.sGetAndroidContext(), file.getAbsolutePath())) {
            return true;
        } else {
            DefaultLogger.d("MediaScannerHelper", "Directory [%s] is in internal storage, skip scan", file.getAbsolutePath());
            return false;
        }
    }

    public static boolean isRoot(String str) {
        return GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH.equalsIgnoreCase(str);
    }

    public static AlbumsStrategy.Attributes getAttributesByPath(String str) {
        return CloudControlStrategyHelper.getAlbumAttributesByPath(str);
    }

    public static boolean isInHideList(String str) {
        return CloudControlStrategyHelper.isInHideList(str);
    }

    public static boolean isOnlyLinkFileFolder(String str) {
        return DownloadPathHelper.isShareFolderRelativePath(str);
    }

    public static boolean isInRubbishList(String str) {
        AlbumsStrategy.Album albumByPath = CloudControlStrategyHelper.getAlbumByPath(StorageUtils.ensureCommonRelativePath(str));
        return ((albumByPath == null || albumByPath.getAttributes() == null) && getAttributesByPath(str) == null) && isInHideList(str);
    }

    public static boolean isInWhiteList(AlbumsStrategy.Attributes attributes, String str) {
        WhiteAlbumsStrategy whiteAlbumsStrategy;
        return (attributes == null || (whiteAlbumsStrategy = CloudControlStrategyHelper.getWhiteAlbumsStrategy()) == null || !whiteAlbumsStrategy.isWhiteAlbum(str)) ? false : true;
    }

    public static boolean isSystemAlbum(String str) {
        return tryGetSystemAlbumServerId(str) != -1;
    }

    public static long tryGetSystemAlbumServerId(String str) {
        if (AlbumDataHelper.getCameraLocalPath().equalsIgnoreCase(str)) {
            return 1L;
        }
        return AlbumDataHelper.getScreenshotsLocalPath().equalsIgnoreCase(str) ? 2L : -1L;
    }

    public static OwnerAlbumEntry queryAndUpdateAlbum(Context context, String str, ContentValues contentValues) {
        OwnerAlbumEntry fromLocalPath;
        String relativePath = StorageUtils.getRelativePath(context, str);
        if (TextUtils.isEmpty(relativePath)) {
            DefaultLogger.w("MediaScannerHelper", "Couldn't get relative path from %s", str);
            return null;
        }
        try {
            long tryGetSystemAlbumServerId = tryGetSystemAlbumServerId(relativePath);
            int i = (tryGetSystemAlbumServerId > 0L ? 1 : (tryGetSystemAlbumServerId == 0L ? 0 : -1));
            if (i > 0) {
                fromLocalPath = OwnerAlbumEntry.fromServerId(context, tryGetSystemAlbumServerId);
            } else {
                fromLocalPath = OwnerAlbumEntry.fromLocalPath(context, relativePath);
            }
            if (fromLocalPath != null) {
                fromLocalPath.isOnlyLinkFolder = isOnlyLinkFileFolder(relativePath);
                fromLocalPath.isShareAlbum = DownloadPathHelper.isShareFolderRelativePath(relativePath);
                if (i <= 0) {
                    updateAlbumByServerConfig(context, fromLocalPath, relativePath, contentValues);
                } else if (contentValues != null) {
                    updateSystemAlbumConfig(context, tryGetSystemAlbumServerId, contentValues);
                }
            }
            return fromLocalPath;
        } catch (Exception e) {
            DefaultLogger.w("MediaScannerHelper", e);
            return null;
        }
    }

    public static void updateSystemAlbumConfig(Context context, long j, ContentValues contentValues) {
        contentValues.remove("name");
        if (contentValues.size() > 0) {
            SafeDBUtil.safeUpdate(context, GalleryContract.Album.URI, contentValues, "serverId=?", new String[]{String.valueOf(j)});
            DefaultLogger.d("MediaScannerHelper", "Override config for system album: %s, values: %s", Long.valueOf(j), contentValues);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c8, code lost:
        if (r24.mAlbumName.toLowerCase().startsWith((r2 + "_").toLowerCase()) == false) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x016e, code lost:
        if (r24.mAlbumName.toLowerCase().startsWith((r2 + "_").toLowerCase()) == false) goto L118;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void updateAlbumByServerConfig(android.content.Context r23, com.miui.gallery.scanner.core.model.OwnerAlbumEntry r24, java.lang.String r25, android.content.ContentValues r26) {
        /*
            Method dump skipped, instructions count: 642
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.scanner.core.scanner.MediaScannerHelper.updateAlbumByServerConfig(android.content.Context, com.miui.gallery.scanner.core.model.OwnerAlbumEntry, java.lang.String, android.content.ContentValues):void");
    }

    public static int checkAlbumNameConflict(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return 1;
        }
        if (CloudControlStrategyHelper.getServerUnModifyAlbumsStrategy().containsName(str) || CloudControlStrategyHelper.getServerReservedAlbumNamesStrategy().containsName(str)) {
            return 2;
        }
        try {
            Cursor query = context.getContentResolver().query(GalleryContract.Album.URI, ScanContracts$SQL.ALBUM_NAME_CONFLICT_PROJECTION, "name = ? COLLATE NOCASE AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{str}, null);
            if (query == null) {
                throw new IllegalStateException("query album cursor null");
            }
            if (!query.moveToFirst() || query.getInt(0) <= 0) {
                BaseMiscUtil.closeSilently(query);
                return 0;
            }
            BaseMiscUtil.closeSilently(query);
            return 3;
        } catch (Throwable th) {
            BaseMiscUtil.closeSilently(null);
            throw th;
        }
    }

    public static boolean tryRenameConflictAlbums(Context context, String str) {
        try {
            Cursor query = context.getContentResolver().query(GalleryContract.Album.URI, ScanContracts$SQL.ALBUM_PROJECTION, "name = ? COLLATE NOCASE AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{str}, null);
            if (query == null) {
                throw new IllegalStateException("query album cursor null");
            }
            query.moveToFirst();
            while (!query.isAfterLast()) {
                long j = query.getLong(1);
                if (j != 1 && j != 2) {
                    int i = query.getInt(3);
                    long j2 = query.getLong(0);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("name", renameAlbum(context, str));
                    if (i == 0) {
                        contentValues.put("localFlag", (Integer) 10);
                    }
                    SafeDBUtil.safeUpdate(context, GalleryContract.Album.URI, contentValues, "_id=?", new String[]{String.valueOf(j2)});
                    DefaultLogger.d("MediaScannerHelper", "Rename conflict album according to server config file: %s", str);
                    query.moveToNext();
                }
                BaseMiscUtil.closeSilently(query);
                return false;
            }
            BaseMiscUtil.closeSilently(query);
            return true;
        } catch (Throwable th) {
            BaseMiscUtil.closeSilently(null);
            throw th;
        }
    }

    public static String renameAlbum(Context context, String str) {
        String str2;
        int i = 0;
        while (true) {
            String valueOf = String.valueOf(System.currentTimeMillis());
            str2 = str + "_" + valueOf.substring(valueOf.length() - 2);
            if (checkAlbumNameConflict(context, str2) == 0) {
                break;
            }
            int i2 = i + 1;
            if (i >= 3) {
                i = i2;
                break;
            }
            i = i2;
        }
        if (i >= 3) {
            return str + "_" + System.currentTimeMillis();
        }
        return str2;
    }

    public static long mergeAttribute(AlbumsStrategy.Attributes attributes, long j, long j2, String str) {
        long mapAttributeBit = mapAttributeBit(attributes, j2, str);
        long longValue = AlbumManager.getAlbumAttributes().get(Long.valueOf(j2)).longValue();
        DefaultLogger.d("MediaScannerHelper", "mergeAttribute, path: [%s], updateAttribute: [%d], manualBit: [%d].", str, Long.valueOf(mapAttributeBit), Long.valueOf(longValue));
        if (mapAttributeBit >= 0) {
            return (j & longValue) == longValue ? (j & j2) | longValue : mapAttributeBit;
        }
        return (j & j2) | (j & longValue);
    }

    public static long mapAttributeBit(AlbumsStrategy.Attributes attributes, long j, String str) {
        if (attributes == null) {
            return -1L;
        }
        if (j == 1) {
            return attributes.isAutoUpload() ? 1L : 0L;
        } else if (j == 4) {
            return attributes.isShowInPhotosTab() ? 4L : 0L;
        } else if (j == 2048) {
            return (isInWhiteList(attributes, str) || !attributes.isHide()) ? 0L : 2048L;
        } else if (j != 64) {
            return -1L;
        } else {
            boolean isInWhiteList = isInWhiteList(attributes, str);
            DefaultLogger.d("MediaScannerHelper", "mapAttributeBit,path:%s,isInWhiteList:%b", str, Boolean.valueOf(isInWhiteList));
            return isInWhiteList ? 0L : 64L;
        }
    }

    public static DeleteRecord checkAndUpdateFileInfo(Context context, String str, String str2, long j, ContentProviderBatchOperator contentProviderBatchOperator, ScanTaskConfig scanTaskConfig, String str3) {
        DeleteRecord deleteRecord;
        ContentValues contentValues = new ContentValues();
        File file = null;
        if (TextUtils.isEmpty(str2)) {
            deleteRecord = null;
        } else if (!new File(str2).exists()) {
            contentValues.put("thumbnailFile", "");
            deleteRecord = new DeleteRecord(13, str2, str3);
        } else {
            file = new File(str2);
            deleteRecord = null;
        }
        if (!TextUtils.isEmpty(str)) {
            if (!new File(str).exists()) {
                contentValues.put("localFile", "");
                deleteRecord = new DeleteRecord(13, str, str3);
            } else {
                file = new File(str);
            }
        }
        if (file != null) {
            contentValues.put("realSize", Long.valueOf(scanTaskConfig.getFileState() == null ? file.length() : scanTaskConfig.getFileState().size));
            contentValues.put("realDateModified", Long.valueOf(scanTaskConfig.getFileState() == null ? file.lastModified() : scanTaskConfig.getFileState().modified));
        }
        if (contentValues.size() > 0) {
            if (contentProviderBatchOperator != null) {
                contentProviderBatchOperator.add(context, ContentProviderOperation.newUpdate(GalleryContract.CloudWriteBulkNotify.CLOUD_WRITE_BULK_NOTIFY_URI).withSelection("_id=?", new String[]{String.valueOf(j)}).withValues(contentValues).build());
            } else {
                SafeDBUtil.safeUpdate(context, GalleryContract.CloudWriteBulkNotify.CLOUD_WRITE_BULK_NOTIFY_URI, contentValues, "_id=?", new String[]{String.valueOf(j)});
            }
        }
        return deleteRecord;
    }

    public static void deleteLocalItem(Context context, long j) {
        SafeDBUtil.safeDelete(context, GalleryContract.Cloud.CLOUD_URI, "_id=?", new String[]{String.valueOf(j)});
    }

    public static OwnerAlbumEntry whileNewMediaFoundInDeletedAlbum(Context context, OwnerAlbumEntry ownerAlbumEntry, long j) {
        DefaultLogger.w("MediaScannerHelper", "new image found in deleted album: %s, %s", ownerAlbumEntry.mAlbumName, Integer.valueOf(ownerAlbumEntry.mLocalFlag));
        OwnerAlbumEntry queryOrInsertAlbum = queryOrInsertAlbum(context, ownerAlbumEntry.mLocalPath);
        if (queryOrInsertAlbum != null) {
            DefaultLogger.w("MediaScannerHelper", "deleted album changed: %s", Boolean.valueOf(ownerAlbumEntry.mId != queryOrInsertAlbum.mId));
            if (ownerAlbumEntry.mId != queryOrInsertAlbum.mId) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("localGroupId", Long.valueOf(queryOrInsertAlbum.mId));
                SafeDBUtil.safeUpdate(context, GalleryContract.Cloud.CLOUD_URI, contentValues, "_id=?", new String[]{String.valueOf(j)});
            } else {
                int i = TextUtils.isEmpty(ownerAlbumEntry.mServerID) ? 7 : 0;
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("localFlag", Integer.valueOf(i));
                SafeDBUtil.safeUpdate(context, GalleryContract.Album.URI, contentValues2, "_id=?", new String[]{String.valueOf(ownerAlbumEntry.mId)});
                queryOrInsertAlbum.mLocalFlag = i;
            }
        }
        return queryOrInsertAlbum != null ? queryOrInsertAlbum : ownerAlbumEntry;
    }

    public static OwnerAlbumEntry whileNewMediaFoundInDeletedAlbum(Context context, OwnerAlbumEntry ownerAlbumEntry, IBulkInserter iBulkInserter) {
        DefaultLogger.w("MediaScannerHelper", "bulk insert: new image found in deleted album: %s, %s", ownerAlbumEntry.mAlbumName, Integer.valueOf(ownerAlbumEntry.mLocalFlag));
        OwnerAlbumEntry queryOrInsertAlbum = queryOrInsertAlbum(context, ownerAlbumEntry.mLocalPath);
        if (queryOrInsertAlbum != null) {
            DefaultLogger.w("MediaScannerHelper", "bulk insert: deleted album changed: %s", Boolean.valueOf(ownerAlbumEntry.mId != queryOrInsertAlbum.mId));
            if (ownerAlbumEntry.mId != queryOrInsertAlbum.mId) {
                for (ContentValues contentValues : iBulkInserter.getValues()) {
                    if (contentValues.getAsLong("localGroupId").longValue() == ownerAlbumEntry.mId) {
                        contentValues.put("localGroupId", Long.valueOf(queryOrInsertAlbum.mId));
                    }
                }
            } else {
                int i = TextUtils.isEmpty(ownerAlbumEntry.mServerID) ? 7 : 0;
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("localFlag", Integer.valueOf(i));
                SafeDBUtil.safeUpdate(context, GalleryContract.Album.URI, contentValues2, "_id=?", new String[]{String.valueOf(ownerAlbumEntry.mId)});
                queryOrInsertAlbum.mLocalFlag = i;
            }
        }
        return queryOrInsertAlbum != null ? queryOrInsertAlbum : ownerAlbumEntry;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0104 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0106  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.scanner.core.model.OwnerAlbumEntry insertAlbum(android.content.Context r18, java.lang.String r19, android.content.ContentValues r20) {
        /*
            Method dump skipped, instructions count: 305
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.scanner.core.scanner.MediaScannerHelper.insertAlbum(android.content.Context, java.lang.String, android.content.ContentValues):com.miui.gallery.scanner.core.model.OwnerAlbumEntry");
    }

    public static OwnerAlbumEntry queryOrInsertAlbum(Context context, String str) {
        return updateOrInsertAlbum(context, str, null);
    }

    public static OwnerAlbumEntry updateOrInsertAlbum(Context context, String str, ContentValues contentValues) {
        OwnerAlbumEntry queryAndUpdateAlbum = queryAndUpdateAlbum(context, str, contentValues);
        return queryAndUpdateAlbum == null ? insertAlbum(context, str, contentValues) : queryAndUpdateAlbum;
    }

    public static void clearScannableDirectoryCache() {
        sScannableDirectoryCache.clear();
    }

    public static void checkMiMoverStopped(Context context) {
        Boolean bool = (Boolean) ScanCache.getInstance().remove("key_mi_mover_event_stop");
        if (bool == null || !bool.booleanValue()) {
            return;
        }
        ScanCache.getInstance().remove("key_mi_mover_event_start");
        ScanCache.getInstance().remove("key_mi_mover_cloud_profiles");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(GalleryBackupHelper.TEMP_BACKUP_STORED, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("MediaScannerHelper", "checkMiMoverStopped"));
        if (documentFile != null) {
            documentFile.delete();
        }
        ScanCache.getInstance().remove("key_mi_mover_cloud_sha1_cache");
        ScanCache.getInstance().remove("key_mi_mover_cloud_path_cache");
        SyncUtil.requestSync(context);
        ScannerEngine.getInstance().triggerScan();
    }
}
