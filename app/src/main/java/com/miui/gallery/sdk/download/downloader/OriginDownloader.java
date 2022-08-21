package com.miui.gallery.sdk.download.downloader;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.sdk.download.assist.RequestItem;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.SecretAlbumCryptoUtils;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.opensdk.file.model.MiCloudFileListener;
import com.xiaomi.opensdk.file.model.MiCloudTransferStopper;
import java.io.File;

/* loaded from: classes2.dex */
public class OriginDownloader implements IDownloader {
    /* JADX WARN: Code restructure failed: missing block: B:59:0x01af, code lost:
        if (r12.mDownloadItem.isManual() != false) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0261, code lost:
        if (r12.mDownloadItem.isManual() != false) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x027f, code lost:
        if (r12.mDownloadItem.isManual() != false) goto L72;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02a4 A[Catch: all -> 0x02be, TRY_LEAVE, TryCatch #3 {all -> 0x02cb, blocks: (B:17:0x0055, B:19:0x0069, B:21:0x0070, B:23:0x0083, B:24:0x00b4, B:26:0x00bf, B:28:0x00cd, B:30:0x00dd, B:32:0x00e9, B:34:0x00f5, B:36:0x00fb, B:38:0x010b, B:39:0x0110, B:41:0x0121, B:42:0x012c, B:44:0x0145, B:45:0x015c, B:47:0x0164, B:48:0x016a, B:50:0x0174, B:53:0x017d, B:58:0x01a9, B:60:0x01b1, B:85:0x025b, B:89:0x0279, B:104:0x02b4, B:96:0x0288, B:98:0x028e, B:100:0x0296, B:101:0x029c, B:103:0x02a4), top: B:120:0x0055 }] */
    /* JADX WARN: Removed duplicated region for block: B:106:0x02bc  */
    @Override // com.miui.gallery.sdk.download.downloader.IDownloader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void download(android.accounts.Account r23, com.miui.gallery.cloud.base.GalleryExtendedAuthToken r24, java.util.List<com.miui.gallery.sdk.download.assist.DownloadItem> r25) {
        /*
            Method dump skipped, instructions count: 721
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.sdk.download.downloader.OriginDownloader.download(android.accounts.Account, com.miui.gallery.cloud.base.GalleryExtendedAuthToken, java.util.List):void");
    }

    public final boolean checkConditionPermitted(DownloadItem downloadItem) {
        DownloadFailReason checkCondition = DownloadUtil.checkCondition(downloadItem);
        if (checkCondition != null) {
            if (!downloadItem.isStatusOk()) {
                return false;
            }
            DownloadItem.callbackError(downloadItem, checkCondition);
            return false;
        }
        return true;
    }

    public final boolean doPostOriginDownloadWork(RequestItem requestItem, RequestCloudItem requestCloudItem, String str, long j) {
        boolean encrypt;
        if (!DownloadUtil.isOriginalFileValid(str, requestCloudItem.dbCloud, true)) {
            DefaultLogger.w("OriginDownloader", "Downloaded original file [%s] is invalid", str);
            if (requestCloudItem.isCloudInvalid() && !"recovery".equals(requestCloudItem.getServerStatus())) {
                fireFailEvent(requestItem, new DownloadFailReason(ErrorCode.SERVER_INVALID, String.format("Downloaded original file [%s], server is invalid", str), null), str);
            } else {
                fireFailEvent(requestItem, new DownloadFailReason(ErrorCode.SERVER_ERROR, String.format("Downloaded original file [%s] is invalid", str), null), str);
            }
            return false;
        }
        DownloadUtil.statDownloadSuccess(requestItem, j, requestItem.mDBItem.getSize());
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("OriginDownloader", "doPostOriginDownloadWork");
        ErrorCode ensureDownloadFolder = DownloadUtil.ensureDownloadFolder(requestCloudItem.dbCloud, requestItem.mDownloadItem.getType());
        if (ensureDownloadFolder != ErrorCode.NO_ERROR) {
            String downloadFolderPath = DownloadUtil.getDownloadFolderPath(requestCloudItem.dbCloud, requestItem.mDownloadItem.getType());
            DefaultLogger.e("OriginDownloader", "Cannot create folder [%s] to download", downloadFolderPath);
            if (ensureDownloadFolder == ErrorCode.STORAGE_NO_WRITE_PERMISSION) {
                fireFailEvent(requestItem, new DownloadFailReason(ensureDownloadFolder, DownloadUtil.getDownloadFolderPath(requestItem.mDBItem, requestItem.mDownloadItem.getType()), null), str);
                return false;
            }
            fireFailEvent(requestItem, new DownloadFailReason(ensureDownloadFolder, String.format("Cannot create folder [%s] to download", downloadFolderPath), null), str);
            return false;
        }
        String downloadFilePath = DownloadUtil.getDownloadFilePath(requestItem.mDownloadItem.getUriAdapter().getDBItemForUri(requestItem.mDownloadItem.getUri()), requestItem.mDownloadItem.getType());
        if (TextUtils.isEmpty(downloadFilePath)) {
            DefaultLogger.e("OriginDownloader", "Cannot find valid download path for image [%s]", requestItem.mDBItem);
            fireFailEvent(requestItem, new DownloadFailReason(ErrorCode.UNKNOWN, String.format("Cannot find valid download path for image [%s]", requestItem.mDBItem), null), str);
            return false;
        }
        if (!requestItem.mDBItem.isSecretItem()) {
            encrypt = StorageSolutionProvider.get().moveFile(str, downloadFilePath, appendInvokerTag);
        } else {
            encrypt = SecretAlbumCryptoUtils.encrypt(str, downloadFilePath, requestItem.mDBItem.isVideoType(), requestItem.mDBItem.getSecretKey());
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile != null) {
                documentFile.delete();
            }
        }
        if (!encrypt) {
            DefaultLogger.e("OriginDownloader", "Move or encrypt failed for image [%s]", requestItem.mDBItem);
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(downloadFilePath, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile2 != null) {
                documentFile2.delete();
            }
            fireFailEvent(requestItem, new DownloadFailReason(ErrorCode.FILE_OPERATE_ERROR, String.format("Move or encrypt failed for image [%s]", requestItem.mDBItem), null), str);
            return false;
        } else if (requestItem.mDownloadItem.isCancelled()) {
            DefaultLogger.e("OriginDownloader", "downloading for image[%s] is cancelled", requestItem.mDBItem);
            DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(downloadFilePath, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile3 != null) {
                documentFile3.delete();
            }
            return false;
        } else {
            if (!requestItem.mDownloadItem.isManual() || !CloudControlStrategyHelper.getSyncStrategy().isFrontForManualDownload()) {
                DocumentFile documentFile4 = StorageSolutionProvider.get().getDocumentFile(downloadFilePath, IStoragePermissionStrategy.Permission.UPDATE, appendInvokerTag);
                if (requestItem.mDBItem.getMixedDateTime() > 0 && documentFile4 != null && documentFile4.exists() && !StorageSolutionProvider.get().setLastModified(documentFile4, requestItem.mDBItem.getMixedDateTime())) {
                    DefaultLogger.w("OriginDownloader", "set last modified error");
                }
            } else {
                DefaultLogger.d("OriginDownloader", "front for manual downloading %s", requestItem.mDBItem.getFileName());
            }
            DBImage dBItemForUri = requestItem.mDownloadItem.getUriAdapter().getDBItemForUri(requestItem.mDownloadItem.getUri());
            if (!checkDBImageValid(new RequestItem(requestItem.mDownloadItem, dBItemForUri))) {
                DefaultLogger.w("OriginDownloader", "DBImage [%s] is invalid after download file for uri [%s] finished", dBItemForUri, requestItem.mDownloadItem.getUri());
                DocumentFile documentFile5 = StorageSolutionProvider.get().getDocumentFile(downloadFilePath, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                if (documentFile5 != null) {
                    documentFile5.delete();
                }
                DownloadUtil.persistDownloadStatus(requestItem.mDBItem, 0);
                return false;
            }
            ContentValues contentValues = new ContentValues();
            if (!requestItem.mDBItem.isShareItem()) {
                contentValues.put("realSize", Long.valueOf(new File(downloadFilePath).length()));
                contentValues.put("realDateModified", Long.valueOf(new File(downloadFilePath).lastModified()));
            }
            if (!requestItem.mDBItem.isSecretItem()) {
                String checkAndReturnValidFilePathForType = DownloadUtil.checkAndReturnValidFilePathForType(requestItem.mDBItem, DownloadType.THUMBNAIL);
                if (!TextUtils.isEmpty(checkAndReturnValidFilePathForType) && !checkAndReturnValidFilePathForType.equalsIgnoreCase(downloadFilePath)) {
                    DefaultLogger.w("OriginDownloader", "Delete thumbnail [%s] of image, because we already have original file", checkAndReturnValidFilePathForType);
                    DocumentFile documentFile6 = StorageSolutionProvider.get().getDocumentFile(checkAndReturnValidFilePathForType, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile6 != null) {
                        documentFile6.delete();
                    }
                }
                if (!TextUtils.isEmpty(requestItem.mDBItem.getThumbnailFile())) {
                    DefaultLogger.w("OriginDownloader", "Delete thumbnail record [%s] of image [%s], because we already have original file", requestItem.mDBItem.getThumbnailFile(), requestItem.mDBItem);
                    requestItem.mDBItem.setThumbnailFile(null);
                    contentValues.putNull("thumbnailFile");
                }
            }
            requestItem.mDBItem.setLocalFile(downloadFilePath);
            contentValues.put("localFile", downloadFilePath);
            if (!requestItem.mDBItem.isSecretItem()) {
                if (requestItem.mDBItem.isImageType()) {
                    contentValues.put("specialTypeFlags", Long.valueOf(SpecialTypeMediaUtils.parseFlagsForImage(downloadFilePath)));
                } else if (requestItem.mDBItem.isVideoType()) {
                    contentValues.put("specialTypeFlags", Long.valueOf(SpecialTypeMediaUtils.parseFlagsForVideo(downloadFilePath)));
                }
            }
            Uri baseUri = requestItem.mDBItem.getBaseUri();
            if (requestItem.mDownloadItem.isManual() && !IncompatibleMediaType.isUnsupportedMediaType(requestItem.mDBItem.getMimeType())) {
                baseUri = baseUri.buildUpon().appendQueryParameter("requireNotifyUri", "true").build();
            }
            if (CloudUtils.updateToLocalDB(baseUri, contentValues, requestItem.mDBItem.getId()) > 0) {
                return true;
            }
            DefaultLogger.e("OriginDownloader", "Update database failed after download original file for image [%s]", requestItem.mDBItem);
            DocumentFile documentFile7 = StorageSolutionProvider.get().getDocumentFile(downloadFilePath, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile7 != null) {
                documentFile7.delete();
            }
            return false;
        }
    }

    public static boolean checkDBImageValid(RequestItem requestItem) {
        DBImage dBImage = requestItem.mDBItem;
        if (DownloadUtil.canDownloadStatus(dBImage)) {
            return true;
        }
        if (dBImage == null) {
            fireFailEvent(requestItem, new DownloadFailReason(ErrorCode.PARAMS_ERROR, "item null", null), null);
        } else {
            String downloadTempFilePath = DownloadUtil.getDownloadTempFilePath(dBImage, requestItem.mDownloadItem.getType());
            if (DownloadUtil.isNotSyncedStatus(dBImage)) {
                fireFailEvent(requestItem, new DownloadFailReason(ErrorCode.NOT_SYNCED, String.format("item invalid server[%s], local[%s]", dBImage.getServerStatus(), Integer.valueOf(dBImage.getLocalFlag())), null), downloadTempFilePath);
            } else {
                fireFailEvent(requestItem, new DownloadFailReason(ErrorCode.PARAMS_ERROR, String.format("item invalid server[%s], local[%s]", dBImage.getServerStatus(), Integer.valueOf(dBImage.getLocalFlag())), null), downloadTempFilePath);
            }
        }
        return false;
    }

    public static void fireFailEvent(RequestItem requestItem, DownloadFailReason downloadFailReason, String str) {
        DefaultLogger.e("OriginDownloader", "download fail %s", downloadFailReason);
        if (downloadFailReason.getCause() != null) {
            DefaultLogger.e("OriginDownloader", downloadFailReason.getCause());
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("OriginDownloader", "fireFailEvent"));
        if (documentFile != null) {
            documentFile.delete();
        }
        if (requestItem.mDownloadItem.isStatusOk()) {
            DownloadItem.callbackError(requestItem.mDownloadItem, downloadFailReason);
        }
        DownloadUtil.statDownloadError(requestItem, null, downloadFailReason);
    }

    public static DownloadCallback createListener(RequestCloudItem requestCloudItem, RequestItem requestItem) {
        return new DownloadCallback(requestCloudItem, requestItem);
    }

    public static DownloadedItem packageDownloadedItem(DBImage dBImage) {
        return new DownloadedItem(dBImage.getLocalFile(), dBImage.isSecretItem() ? dBImage.getSecretKeyNoGenerate() : null);
    }

    /* loaded from: classes2.dex */
    public static class DownloadCallback implements MiCloudFileListener, MiCloudTransferStopper {
        public final RequestCloudItem mRequestCloudItem;
        public final RequestItem mRequestItem;

        @Override // com.xiaomi.opensdk.file.model.MiCloudFileListener
        public void onDataSended(long j, long j2) {
        }

        public DownloadCallback(RequestCloudItem requestCloudItem, RequestItem requestItem) {
            this.mRequestCloudItem = requestCloudItem;
            this.mRequestItem = requestItem;
        }

        @Override // com.xiaomi.opensdk.file.model.MiCloudFileListener
        public void onDataReceived(long j, long j2) {
            RequestCloudItem requestCloudItem = this.mRequestCloudItem;
            requestCloudItem.mDownloadedSize = j;
            requestCloudItem.mTotalSize = j2;
            DownloadItem.callbackProgress(this.mRequestItem.mDownloadItem, j, j2);
        }

        @Override // com.xiaomi.opensdk.file.model.MiCloudTransferStopper
        public boolean checkStop() {
            if (!this.mRequestItem.mDownloadItem.isStatusOk()) {
                DefaultLogger.d("OriginDownloader", "download for %s stopped, status: %s", this.mRequestCloudItem.getFileName(), Integer.valueOf(this.mRequestItem.mDownloadItem.getStatus()));
                return true;
            }
            int check = SyncConditionManager.check(this.mRequestCloudItem.priority);
            if (check == 0) {
                return false;
            }
            DefaultLogger.d("OriginDownloader", "download for %s stopped, condition: %s", this.mRequestCloudItem.getFileName(), Integer.valueOf(check));
            return true;
        }
    }
}
