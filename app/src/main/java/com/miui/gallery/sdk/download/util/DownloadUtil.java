package com.miui.gallery.sdk.download.util;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.joran.action.Action;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.LocalUbifocus;
import com.miui.gallery.data.UbiIndexMapper;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.assist.RequestItem;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes2.dex */
public class DownloadUtil {
    public static final int[] DOWNLOAD_FILE_PRIORITY = {9, 10, 11};

    public static boolean isOriginalFileValid(String str, DBImage dBImage) {
        return isOriginalFileValid(str, dBImage, false);
    }

    public static boolean isOriginalFileValid(String str, DBImage dBImage, boolean z) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.d("DownloadUtil", "Empty original file path for %s", dBImage);
            return false;
        }
        File file = new File(str);
        if (!file.exists() || !file.isFile()) {
            DefaultLogger.d("DownloadUtil", "File [%s] doesn't exist or is not a file %s", str, dBImage);
            return false;
        }
        long length = file.length();
        if (length < dBImage.getSize()) {
            DefaultLogger.d("DownloadUtil", "File [%s] size [%d] is smaller than expected [%d] for image %s", str, Long.valueOf(length), Long.valueOf(dBImage.getSize()), dBImage);
            if (z) {
                HashMap hashMap = new HashMap();
                hashMap.put(CallMethod.ARG_EXTRA_STRING, String.format(Locale.US, "ServerId:%s, expectedSize:%d, realSize:%d", dBImage.getServerId(), Long.valueOf(dBImage.getSize()), Long.valueOf(length)));
                SamplingStatHelper.recordCountEvent("file_download_origin", "file_download_wrong_size", hashMap);
            }
            return false;
        }
        String sha1 = FileUtils.getSha1(str);
        if (sha1 != null && sha1.equalsIgnoreCase(dBImage.getSha1())) {
            return true;
        }
        DefaultLogger.d("DownloadUtil", "File [%s]'s sha1 [%s] is different from [%s]", str, sha1 == null ? "null" : sha1, dBImage.getSha1());
        if (z) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(CallMethod.ARG_EXTRA_STRING, String.format("ServerId:%s, expectedSha1:%s, realSha1:%s", dBImage.getServerId(), dBImage.getSha1(), sha1));
            SamplingStatHelper.recordCountEvent("file_download_origin", "file_download_wrong_sha1", hashMap2);
        }
        return false;
    }

    public static boolean isThumbnailFileValid(String str, DBImage dBImage) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return TextUtils.equals(dBImage.getSha1(), ExifUtil.getUserCommentSha1(str));
    }

    public static boolean isMicro(DownloadType downloadType) {
        return downloadType == DownloadType.MICRO || downloadType == DownloadType.MICRO_BATCH;
    }

    public static boolean isThumbnail(DownloadType downloadType) {
        return downloadType == DownloadType.THUMBNAIL || downloadType == DownloadType.THUMBNAIL_BATCH;
    }

    public static boolean isOrigin(DownloadType downloadType) {
        return downloadType == DownloadType.ORIGIN || downloadType == DownloadType.ORIGIN_FORCE || downloadType == DownloadType.ORIGIN_BATCH;
    }

    public static String checkAndReturnValidFilePathForType(DBImage dBImage, DownloadType downloadType) {
        if (dBImage == null || downloadType == null) {
            return null;
        }
        if (isMicro(downloadType)) {
            return checkAndReturnValidMicroFilePath(dBImage, downloadType);
        }
        if (isThumbnail(downloadType)) {
            return checkAndReturnValidThumbnailFilePath(dBImage, downloadType);
        }
        if (isOrigin(downloadType)) {
            return checkAndReturnValidOriginalFilePath(dBImage, downloadType);
        }
        throw new UnsupportedOperationException("bad checktype, checktype=" + downloadType);
    }

    public static String getDownloadTempFilePath(DBImage dBImage, DownloadType downloadType) {
        if (dBImage != null && downloadType != null) {
            if (isMicro(downloadType)) {
                return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/microthumbnailFile" + File.separator + dBImage.getFileName() + ".temp");
            } else if (isThumbnail(downloadType)) {
                return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/downloadThumbnail" + File.separator + dBImage.getSha1() + "." + dBImage.getId());
            } else if (isOrigin(downloadType)) {
                String safePriorOriginTempDirectory = StorageUtils.getSafePriorOriginTempDirectory();
                return BaseFileUtils.concat(safePriorOriginTempDirectory, dBImage.getSha1() + "." + dBImage.getId());
            }
        }
        return null;
    }

    public static ErrorCode ensureDownloadFolder(DBImage dBImage, DownloadType downloadType) {
        if (dBImage == null || downloadType == null) {
            return ErrorCode.UNKNOWN;
        }
        return ensureFolder(getDownloadFolderPath(dBImage, downloadType));
    }

    public static String getDownloadFolderPath(DBImage dBImage, DownloadType downloadType) {
        if (dBImage == null || downloadType == null) {
            return null;
        }
        if (isMicro(downloadType)) {
            return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/microthumbnailFile");
        }
        if (!isThumbnail(downloadType) && !isOrigin(downloadType)) {
            return null;
        }
        return DownloadPathHelper.getDownloadFolderPath(dBImage);
    }

    public static String getDownloadFilePath(DBImage dBImage, DownloadType downloadType) {
        String concat;
        if (dBImage == null || downloadType == null) {
            return null;
        }
        String downloadFileName = getDownloadFileName(dBImage, downloadType);
        if (isOrigin(downloadType) && (dBImage.isUbiFocus() || dBImage.isSubUbiFocus())) {
            downloadFileName = LocalUbifocus.createInnerFileName(downloadFileName, UbiIndexMapper.cloudToLocal(dBImage.getSubUbiIndex(), dBImage.getSubUbiImageCount() + 1), dBImage.getSubUbiImageCount() + 1);
        }
        if (dBImage.isDeleteItem() && !isMicro(downloadType)) {
            String trashBinPath = TrashManager.getTrashBinPath();
            if (!dBImage.isSecretItem()) {
                downloadFileName = dBImage.getSha1() + "." + BaseFileUtils.getExtension(downloadFileName);
            }
            concat = StorageUtils.getFilePathUnder(trashBinPath, downloadFileName);
        } else {
            concat = BaseFileUtils.concat(getDownloadFolderPath(dBImage, downloadType), downloadFileName);
        }
        if ((!isThumbnail(downloadType) && !isOrigin(downloadType)) || !new File(concat).exists() || isOriginalFileValid(concat, dBImage) || isThumbnailFileValid(concat, dBImage)) {
            return concat;
        }
        String format = String.format("%s_%s.%s", BaseFileUtils.getFileTitle(downloadFileName), Long.valueOf(System.currentTimeMillis()), BaseFileUtils.getExtension(downloadFileName));
        String concat2 = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(concat), format);
        DefaultLogger.d("DownloadUtil", "There exist a file with same name that doesn't belong to image [%s], so we rename current to %s", dBImage, format);
        return concat2;
    }

    public static ErrorCode ensureDownloadTempFolder(DBImage dBImage, DownloadType downloadType) {
        if (dBImage == null || downloadType == null) {
            return ErrorCode.UNKNOWN;
        }
        return ensureFolder(getDownloadTempFolderPath(dBImage, downloadType));
    }

    public static ErrorCode ensureFolder(String str) {
        if (TextUtils.isEmpty(str)) {
            return ErrorCode.UNKNOWN;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("DownloadUtil", "ensureFolder");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT_DIRECTORY;
        if (storageStrategyManager.getDocumentFile(str, permission, appendInvokerTag) != null) {
            return ErrorCode.NO_ERROR;
        }
        if (StorageSolutionProvider.get().checkPermission(str, permission).applicable) {
            return ErrorCode.STORAGE_NO_WRITE_PERMISSION;
        }
        if (StorageUtils.isInPrimaryStorage(str)) {
            return ErrorCode.PRIMARY_STORAGE_WRITE_ERROR;
        }
        if (StorageUtils.isInSecondaryStorage(str)) {
            return ErrorCode.SECONDARY_STORAGE_WRITE_ERROR;
        }
        DefaultLogger.w("DownloadUtil", "Failed to create folder under unknown volume %s", str);
        return ErrorCode.UNKNOWN;
    }

    public static String getDownloadTempFolderPath(DBImage dBImage, DownloadType downloadType) {
        if (dBImage != null && downloadType != null) {
            if (isMicro(downloadType)) {
                return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/microthumbnailFile");
            }
            if (isThumbnail(downloadType)) {
                return StorageUtils.getPathInPriorStorage("/Android/data/com.miui.gallery/cache/downloadThumbnail");
            }
            if (isOrigin(downloadType)) {
                return StorageUtils.getSafePriorOriginTempDirectory();
            }
        }
        return null;
    }

    public static String getDownloadFileName(DBImage dBImage, DownloadType downloadType) {
        if (dBImage == null || downloadType == null) {
            return null;
        }
        if (isMicro(downloadType)) {
            return dBImage.isSecretItem() ? dBImage.getSha1ThumbnailSA() : dBImage.getSha1Thumbnail();
        } else if (isThumbnail(downloadType)) {
            return dBImage.isSecretItem() ? dBImage.getSha1ThumbnailSA() : DownloadPathHelper.getThumbnailDownloadFileNameNotSecret(dBImage);
        } else if (isOrigin(downloadType)) {
            return dBImage.isSecretItem() ? dBImage.getEncodedFileName() : DownloadPathHelper.getOriginDownloadFileNameNotSecret(dBImage);
        } else {
            throw new UnsupportedOperationException("bad checktype, checktype=" + downloadType);
        }
    }

    public static String checkAndReturnValidOriginalFilePath(DBImage dBImage, DownloadType downloadType) {
        String filePathForRead;
        if (new File(dBImage.getLocalFile()).exists()) {
            filePathForRead = dBImage.getLocalFile();
        } else {
            filePathForRead = DownloadPathHelper.getFilePathForRead(DownloadPathHelper.getDownloadFolderRelativePath(dBImage), getDownloadFileName(dBImage, downloadType));
        }
        if (!TextUtils.isEmpty(filePathForRead)) {
            boolean z = true;
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(filePathForRead, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("DownloadUtil", "checkAndReturnValidOriginalFilePath"));
            if (documentFile != null && documentFile.length() <= 0) {
                return null;
            }
            if (!dBImage.isSecretItem()) {
                z = isOriginalFileValid(filePathForRead, dBImage);
            }
            if (z && !filePathForRead.equalsIgnoreCase(dBImage.getLocalFile()) && (z = updateImageColumnValue(dBImage, "localFile", filePathForRead))) {
                dBImage.setLocalFile(filePathForRead);
            }
            if (!z) {
                return null;
            }
            return filePathForRead;
        }
        return null;
    }

    public static String checkAndReturnValidThumbnailFilePath(DBImage dBImage, DownloadType downloadType) {
        String filePathForRead;
        String str;
        if (new File(dBImage.getThumbnailFile()).exists()) {
            filePathForRead = dBImage.getThumbnailFile();
        } else if (dBImage.isDeleteItem()) {
            String trashBinPath = TrashManager.getTrashBinPath();
            if (dBImage.isSecretItem()) {
                str = getDownloadFileName(dBImage, downloadType);
            } else {
                str = dBImage.getSha1() + "." + BaseFileUtils.getExtension(getDownloadFileName(dBImage, downloadType));
            }
            filePathForRead = StorageUtils.getFilePathUnder(trashBinPath, str);
        } else {
            filePathForRead = DownloadPathHelper.getFilePathForRead(DownloadPathHelper.getDownloadFolderRelativePath(dBImage), getDownloadFileName(dBImage, downloadType));
        }
        if (!TextUtils.isEmpty(filePathForRead)) {
            boolean z = true;
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(filePathForRead, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("DownloadUtil", "checkAndReturnValidThumbnailFilePath"));
            if (documentFile != null && documentFile.length() <= 0) {
                return null;
            }
            if (!dBImage.isSecretItem() || dBImage.isDeleteItem()) {
                z = isThumbnailFileValid(filePathForRead, dBImage);
            }
            if (z && !filePathForRead.equalsIgnoreCase(dBImage.getThumbnailFile()) && (z = updateImageColumnValue(dBImage, "thumbnailFile", filePathForRead))) {
                dBImage.setThumbnailFile(filePathForRead);
            }
            if (!z) {
                return null;
            }
            return filePathForRead;
        }
        return null;
    }

    public static String checkAndReturnValidMicroFilePath(DBImage dBImage, DownloadType downloadType) {
        String filePathForRead;
        if (new File(dBImage.getMicroThumbnailFile()).exists()) {
            filePathForRead = dBImage.getMicroThumbnailFile();
        } else {
            filePathForRead = DownloadPathHelper.getFilePathForRead(StorageUtils.getMicroThumbnailDirectories(GalleryApp.sGetAndroidContext()), getDownloadFileName(dBImage, downloadType));
        }
        if (!TextUtils.isEmpty(filePathForRead)) {
            boolean z = true;
            if (!filePathForRead.equalsIgnoreCase(dBImage.getMicroThumbnailFile()) && (z = updateImageColumnValue(dBImage, "microthumbfile", filePathForRead))) {
                dBImage.setMicroThumbFile(filePathForRead);
                GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, false);
            }
            if (!z) {
                return null;
            }
            return filePathForRead;
        }
        return null;
    }

    public static boolean updateImageColumnValue(DBImage dBImage, String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(str, str2);
        return CloudUtils.updateToLocalDB(dBImage.getBaseUri(), contentValues, dBImage.getId()) >= 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0015, code lost:
        if (r0 != 3) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void persistDownloadStatus(com.miui.gallery.sdk.download.assist.RequestItem r5) {
        /*
            com.miui.gallery.sdk.download.assist.DownloadItem r0 = r5.mDownloadItem
            int r0 = r0.getStatus()
            com.miui.gallery.data.DBImage r5 = r5.mDBItem
            if (r5 != 0) goto Lb
            return
        Lb:
            r1 = 3
            r2 = 2
            r3 = 1
            r4 = 0
            if (r0 == 0) goto L1b
            if (r0 == r3) goto L17
            if (r0 == r2) goto L19
            if (r0 == r1) goto L1c
        L17:
            r1 = r4
            goto L1c
        L19:
            r1 = r2
            goto L1c
        L1b:
            r1 = r3
        L1c:
            persistDownloadStatus(r5, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.sdk.download.util.DownloadUtil.persistDownloadStatus(com.miui.gallery.sdk.download.assist.RequestItem):void");
    }

    public static void persistDownloadStatus(DBImage dBImage, int i) {
        DefaultLogger.d("DownloadUtil", "persistDownloadStatus id %s, status %d", dBImage.getId(), Integer.valueOf(i));
        if (i == 1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("downloadFileTime", Long.valueOf(System.currentTimeMillis()));
            SafeDBUtil.safeUpdate(GalleryApp.sGetAndroidContext(), dBImage.getBaseUri(), contentValues, String.format(Locale.US, "(%s) AND (%s is null OR %s=%d)", "_id=?", "downloadFileStatus", "downloadFileStatus", 0), new String[]{dBImage.getId()});
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("downloadFileStatus", Integer.valueOf(i));
        SafeDBUtil.safeUpdate(GalleryApp.sGetAndroidContext(), dBImage.getBaseUri(), contentValues2, "_id=?", new String[]{dBImage.getId()});
    }

    public static int resumeInterrupted() {
        for (int i : DOWNLOAD_FILE_PRIORITY) {
            if (SyncConditionManager.check(i) != 0) {
                return 0;
            }
        }
        Uri[] uriArr = {GalleryContract.Cloud.CLOUD_URI, GalleryContract.ShareImage.SHARE_URI};
        final ArrayList<DownloadItem> arrayList = new ArrayList();
        String[] strArr = {j.c, "serverType"};
        for (int i2 = 0; i2 < 2; i2++) {
            Uri uri = uriArr[i2];
            final boolean z = uri == GalleryContract.ShareImage.SHARE_URI;
            SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), uri, strArr, "(downloadFileStatus=? OR downloadFileStatus=?) AND (localFlag = 0 AND serverStatus = 'custom')", new String[]{String.valueOf(2), String.valueOf(1)}, "downloadFileTime DESC", new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.sdk.download.util.DownloadUtil.1
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public Object mo1808handle(Cursor cursor) {
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            long convertToMediaId = z ? ShareMediaManager.convertToMediaId(cursor.getLong(0)) : cursor.getLong(0);
                            cursor.getInt(1);
                            arrayList.add(new DownloadItem.Builder().setUri(CloudUriAdapter.getDownloadUri(convertToMediaId)).setType(DownloadType.ORIGIN).build());
                        }
                        return null;
                    }
                    return null;
                }
            });
        }
        DownloadOptions build = new DownloadOptions.Builder().setRequireWLAN(true).build();
        for (DownloadItem downloadItem : arrayList) {
            DefaultLogger.d("DownloadUtil", "resume item %s", downloadItem);
            ImageDownloader.getInstance().load(downloadItem.getUri(), downloadItem.getType(), build);
        }
        return arrayList.size();
    }

    public static boolean canDownloadStatus(DBImage dBImage) {
        return dBImage != null && "custom".equals(dBImage.getServerStatus()) && dBImage.getLocalFlag() == 0;
    }

    public static boolean canDownloadThumbnailStatus(DBImage dBImage) {
        return dBImage != null && !"purged".equals(dBImage.getServerStatus()) && dBImage.getLocalFlag() == 0;
    }

    public static boolean isNotSyncedStatus(DBImage dBImage) {
        return (dBImage == null || dBImage.getLocalFlag() == 11 || dBImage.getLocalFlag() == 2 || dBImage.getLocalFlag() == -1) ? false : true;
    }

    public static DownloadFailReason checkCondition(DownloadItem downloadItem) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            return new DownloadFailReason(ErrorCode.NO_CTA_PERMISSION, "no cta permission", null);
        }
        if (!BaseNetworkUtils.isNetworkConnected()) {
            return new DownloadFailReason(ErrorCode.NO_NETWORK, "no network", null);
        }
        if (downloadItem.isRequireWLAN() && BaseNetworkUtils.isActiveNetworkMetered()) {
            return new DownloadFailReason(ErrorCode.NO_WIFI_CONNECTED, "no wifi", null);
        }
        if (downloadItem.isRequirePower() && !GalleryPreferences.Sync.getPowerCanSync()) {
            return new DownloadFailReason(ErrorCode.POWER_LOW, "power low", null);
        }
        if (downloadItem.isRequireCharging() && !GalleryPreferences.Sync.getIsPlugged()) {
            return new DownloadFailReason(ErrorCode.NO_CHARGING, "not charging", null);
        }
        if (downloadItem.isRequireDeviceStorage() && GalleryPreferences.Sync.isDeviceStorageLow()) {
            return new DownloadFailReason(ErrorCode.STORAGE_LOW, "storage low", null);
        }
        String checkStorageInsertPermission = checkStorageInsertPermission(downloadItem);
        if (TextUtils.isEmpty(checkStorageInsertPermission)) {
            return null;
        }
        return new DownloadFailReason(ErrorCode.STORAGE_NO_WRITE_PERMISSION, checkStorageInsertPermission, null);
    }

    public static String checkStorageInsertPermission(DownloadItem downloadItem) {
        IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(getDownloadFilePath(downloadItem.getUriAdapter().getDBItemForUri(downloadItem.getUri()), downloadItem.getType()), IStoragePermissionStrategy.Permission.INSERT);
        if (checkPermission.granted) {
            return null;
        }
        return checkPermission.path;
    }

    public static void statDownloadError(RequestItem requestItem, String str, DownloadFailReason downloadFailReason) {
        HashMap hashMap = new HashMap();
        hashMap.put("state", String.valueOf(false));
        DBImage dBImage = requestItem.mDBItem;
        String str2 = "unknown";
        hashMap.put("media_type", dBImage != null ? MiscUtil.serverType2Text(dBImage.getServerType()) : str2);
        DownloadItem downloadItem = requestItem.mDownloadItem;
        if (downloadItem != null) {
            str2 = String.valueOf(downloadItem.getType().name());
        }
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str2);
        if (!TextUtils.isEmpty(str)) {
            hashMap.put("from", str);
        }
        hashMap.put("error", String.valueOf(downloadFailReason.getCode()));
        hashMap.put("error_type", String.valueOf(downloadFailReason.getDesc()));
        if (downloadFailReason.getCause() != null) {
            hashMap.put("error_extra", downloadFailReason.getCause().getMessage());
            NetworkInfo activeNetworkInfo = BaseNetworkUtils.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                hashMap.put(CallMethod.ARG_EXTRA_STRING, activeNetworkInfo.getTypeName() + "_" + activeNetworkInfo.getExtraInfo());
            }
        }
        SamplingStatHelper.recordCountEvent("file_download", "file_download_status_v2", hashMap);
    }

    public static void statDownloadSuccess(RequestItem requestItem, long j, long j2) {
        if (requestItem.mDownloadItem == null || requestItem.mDBItem == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("state", String.valueOf(true));
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(requestItem.mDownloadItem.getType().name()));
        Locale locale = Locale.US;
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.format(locale, "%.2f", Float.valueOf((((float) j2) * 1.0f) / 1024.0f)));
        hashMap.put(Action.FILE_ATTRIBUTE, MiscUtil.serverType2Text(requestItem.mDBItem.getServerType()));
        hashMap.put("cost_time", String.format(locale, "%.2f", Float.valueOf((((float) (System.currentTimeMillis() - j)) * 1.0f) / 1000.0f)));
        NetworkInfo activeNetworkInfo = BaseNetworkUtils.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            hashMap.put(CallMethod.ARG_EXTRA_STRING, String.valueOf(activeNetworkInfo.getTypeName()));
        }
        SamplingStatHelper.recordCountEvent("file_download", "file_download_status_v2", hashMap);
    }

    public static void statDownloadRetryTimes(RequestItem requestItem, int i, int i2) {
        if (requestItem.mDownloadItem == null || requestItem.mDBItem == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(requestItem.mDownloadItem.getType().name()));
        hashMap.put(Action.FILE_ATTRIBUTE, MiscUtil.serverType2Text(requestItem.mDBItem.getServerType()));
        NetworkInfo activeNetworkInfo = BaseNetworkUtils.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            hashMap.put(CallMethod.ARG_EXTRA_STRING, String.valueOf(activeNetworkInfo.getTypeName()));
        }
        hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
        SamplingStatHelper.recordCountEvent("file_download", "file_download_retry_times_v2", hashMap);
    }

    public static String generateKey(Uri uri, DownloadType downloadType) {
        if (uri == null || downloadType == null) {
            return null;
        }
        return uri.buildUpon().appendQueryParameter(nexExportFormat.TAG_FORMAT_TYPE, downloadType.name()).build().toString();
    }
}
