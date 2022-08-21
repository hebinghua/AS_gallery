package com.miui.gallery.sdk.download.downloader;

import android.accounts.Account;
import android.net.Uri;
import android.util.Base64;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.ThumbnailPreference;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.sdk.download.assist.RequestItem;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.CryptoUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.video.VideoDecoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import miuix.core.util.FileUtils;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public abstract class AbsThumbnailDownloader implements IDownloader {
    public static final int MAX_TRY_COUNT = CloudControlStrategyHelper.getSyncStrategy().getMaxDownloadTimes();

    public abstract FileItem checkValidFile(RequestItem requestItem);

    public abstract int getFileType();

    public abstract String getInvokerTag();

    public abstract int getRequestHeight();

    public abstract int getRequestWidth();

    public abstract String getTag();

    public boolean handleDownloadTempFile(RequestItem requestItem, String str) {
        return true;
    }

    public boolean shouldWaitUriLock() {
        return true;
    }

    public abstract boolean updateDatabase(RequestItem requestItem, FileItem fileItem);

    @Override // com.miui.gallery.sdk.download.downloader.IDownloader
    public void download(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, List<DownloadItem> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (DownloadItem downloadItem : list) {
            if (!checkConditionPermitted(downloadItem)) {
                DefaultLogger.e(getTag(), "Download condition not ok type %s", downloadItem.getType());
            } else {
                DBImage dBItemForUri = downloadItem.getUriAdapter().getDBItemForUri(downloadItem.getUri());
                RequestItem requestItem = new RequestItem(downloadItem, dBItemForUri);
                if (!checkItemValid(requestItem)) {
                    DefaultLogger.e(getTag(), "Invalid dbImage [%s] for download uri [%s]!", dBItemForUri, downloadItem.getUri());
                } else {
                    FileItem checkValidFile = checkValidFile(requestItem);
                    byte[] bArr = null;
                    if (!checkValidFile.isFileValid()) {
                        if (ThumbnailPreference.containsThumbnailKey(requestItem.mDBItem.getSha1())) {
                            DefaultLogger.e(getTag(), "build error sha1 %s", requestItem.mDBItem.getSha1());
                            fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.THUMBNAIL_BUILD_ERROR, String.format("thumbnail can't build: %s", requestItem.mDBItem.getFileName()), null));
                        } else {
                            arrayList.add(requestItem);
                        }
                    } else if (downloadItem.compareAnsSetStatus(0, 3)) {
                        updateDatabase(requestItem, checkValidFile);
                        DownloadItem downloadItem2 = requestItem.mDownloadItem;
                        String path = checkValidFile.getPath();
                        if (requestItem.mDBItem.isSecretItem()) {
                            bArr = requestItem.mDBItem.getSecretKey();
                        }
                        DownloadItem.callbackSuccess(downloadItem2, new DownloadedItem(path, bArr));
                    }
                }
            }
        }
        if (arrayList.size() <= 0) {
            return;
        }
        for (Map.Entry<String, List<RequestItem>> entry : classifyRequest(arrayList).entrySet()) {
            downloadInternal(account, galleryExtendedAuthToken, entry.getValue());
            DefaultLogger.d(getTag(), "download batch %s", entry.getKey());
        }
    }

    public static HashMap<String, List<RequestItem>> classifyRequest(List<RequestItem> list) {
        HashMap<String, List<RequestItem>> hashMap = new HashMap<>();
        for (RequestItem requestItem : list) {
            String valueOf = requestItem.mDBItem.isShareItem() ? String.valueOf(requestItem.mDBItem.getGroupId()) : "owner";
            List<RequestItem> list2 = hashMap.get(valueOf);
            if (list2 == null) {
                list2 = new LinkedList<>();
                hashMap.put(valueOf, list2);
            }
            list2.add(requestItem);
        }
        return hashMap;
    }

    public final void downloadInternal(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, List<RequestItem> list) {
        JSONObject requestUrls = requestUrls(account, galleryExtendedAuthToken, list);
        if (requestUrls == null) {
            return;
        }
        doFileDownload(list, requestUrls);
    }

    public void doFileDownload(List<RequestItem> list, JSONObject jSONObject) {
        for (RequestItem requestItem : list) {
            downloadFileItem(requestItem, jSONObject.optJSONObject(requestItem.mDBItem.getRequestId()));
        }
    }

    public boolean downloadFileItem(RequestItem requestItem, JSONObject jSONObject) {
        String downloadData;
        DefaultLogger.d(getTag(), "download id %s", requestItem.mDBItem.getId());
        if (Thread.currentThread().isInterrupted()) {
            DefaultLogger.w(getTag(), "for download interrupt %s", Boolean.valueOf(Thread.currentThread().isInterrupted()));
            return false;
        } else if (!requestItem.mDownloadItem.isStatusOk()) {
            DefaultLogger.e(getTag(), "item %s status %s", requestItem.mDownloadItem.getKey(), Integer.valueOf(requestItem.mDownloadItem.getStatus()));
            return false;
        } else {
            String requestId = requestItem.mDBItem.getRequestId();
            byte[] bArr = null;
            if (jSONObject == null) {
                DefaultLogger.e(getTag(), "id: %s not found in content json", requestId);
                ThumbnailPreference.putThumbnailKey(requestItem.mDBItem.getSha1());
                fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.THUMBNAIL_BUILD_ERROR, String.format(Locale.US, "request url for %s error", requestItem.mDBItem.getServerId()), null));
                return false;
            }
            ReentrantLock uriLock = requestItem.mDownloadItem.getUriLock();
            if (uriLock.isLocked()) {
                if (!shouldWaitUriLock()) {
                    DefaultLogger.w(getTag(), "%s is locked, skip download", requestItem.mDownloadItem);
                    return false;
                }
                DefaultLogger.w(getTag(), "%s wait lock", requestItem.mDownloadItem);
            }
            uriLock.lock();
            try {
                FileItem checkValidFile = checkValidFile(requestItem);
                if (checkValidFile.isFileValid()) {
                    DefaultLogger.d(getTag(), "no need download file: %s, thumb: %s", requestItem.mDBItem.getLocalFile(), requestItem.mDBItem.getThumbnailFile());
                    if (requestItem.mDownloadItem.compareAnsSetStatus(0, 3)) {
                        DownloadItem downloadItem = requestItem.mDownloadItem;
                        String path = checkValidFile.getPath();
                        if (requestItem.mDBItem.isSecretItem()) {
                            bArr = requestItem.mDBItem.getSecretKey();
                        }
                        DownloadItem.callbackSuccess(downloadItem, new DownloadedItem(path, bArr));
                    }
                    return true;
                }
                boolean optBoolean = jSONObject.optBoolean("isUrl");
                String optString = jSONObject.optString("data");
                ErrorCode ensureDownloadTempFolder = DownloadUtil.ensureDownloadTempFolder(requestItem.mDBItem, requestItem.mDownloadItem.getType());
                if (ensureDownloadTempFolder != ErrorCode.NO_ERROR) {
                    fireFailEvent(requestItem, null, new DownloadFailReason(ensureDownloadTempFolder, String.format(Locale.US, "error create folder: %s", DownloadUtil.getDownloadTempFolderPath(requestItem.mDBItem, requestItem.mDownloadItem.getType())), null));
                    return false;
                }
                if (optBoolean) {
                    Uri parse = Uri.parse(optString);
                    if (!isValidUrl(parse)) {
                        ThumbnailPreference.putThumbnailKey(requestItem.mDBItem.getSha1());
                        fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.THUMBNAIL_BUILD_ERROR, String.format(Locale.US, "invalid url %s for %s", parse, requestItem.mDBItem.getServerId()), null));
                        return false;
                    }
                    downloadData = downloadFile(requestItem, parse.toString());
                } else {
                    downloadData = downloadData(requestItem, optString);
                }
                onPostDownload(requestItem, downloadData);
                return true;
            } finally {
                uriLock.unlock();
            }
        }
    }

    public final void onPostDownload(RequestItem requestItem, String str) {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag(getInvokerTag(), "onPostDownload");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        if (documentFile == null || !documentFile.exists()) {
            DefaultLogger.w(getTag(), "temp download path not exist");
            return;
        }
        byte[] bArr = null;
        if (!handleDownloadTempFile(requestItem, str)) {
            fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.WRITE_EXIF_ERROR, "write exif error", null));
            return;
        }
        ErrorCode ensureDownloadFolder = DownloadUtil.ensureDownloadFolder(requestItem.mDBItem, requestItem.mDownloadItem.getType());
        if (ensureDownloadFolder != ErrorCode.NO_ERROR) {
            if (ensureDownloadFolder == ErrorCode.STORAGE_NO_WRITE_PERMISSION) {
                fireFailEvent(requestItem, null, new DownloadFailReason(ensureDownloadFolder, FileUtils.normalizeDirectoryName(DownloadUtil.getDownloadFolderPath(requestItem.mDBItem, requestItem.mDownloadItem.getType())), null));
            } else {
                fireFailEvent(requestItem, null, new DownloadFailReason(ensureDownloadFolder, String.format(Locale.US, "error create folder: %s", DownloadUtil.getDownloadFolderPath(requestItem.mDBItem, requestItem.mDownloadItem.getType())), null));
            }
        } else if (requestItem.mDownloadItem.isCancelled()) {
            DefaultLogger.w(getTag(), "downloading for image[%s] is cancelled", requestItem.mDBItem);
            deleteTempFile(requestItem);
        } else {
            File file = new File(DownloadUtil.getDownloadFilePath(requestItem.mDBItem, requestItem.mDownloadItem.getType()));
            if (requestItem.mDBItem.isSecretItem()) {
                boolean encryptFile = CryptoUtil.encryptFile(str, file.getAbsolutePath(), requestItem.mDBItem.getSecretKey());
                DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                if (documentFile2 != null) {
                    documentFile2.delete();
                }
                if (!encryptFile) {
                    fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.FILE_OPERATE_ERROR, "encrypt error", null));
                    return;
                }
            } else if (!StorageSolutionProvider.get().moveFile(str, file.getAbsolutePath(), appendInvokerTag)) {
                fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.FILE_OPERATE_ERROR, "rename error", null));
                return;
            }
            DBImage dBItemForUri = requestItem.mDownloadItem.getUriAdapter().getDBItemForUri(requestItem.mDownloadItem.getUri());
            RequestItem requestItem2 = new RequestItem(requestItem.mDownloadItem, dBItemForUri);
            if (!checkItemValid(requestItem2)) {
                DefaultLogger.w(getTag(), "DBImage [%s] is invalid after download file for uri [%s] finished", dBItemForUri, requestItem2.mDownloadItem.getUri());
                DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                if (documentFile3 == null) {
                    return;
                }
                documentFile3.delete();
            } else if (!updateDatabase(requestItem, new FileItem(getFileType(), file.getAbsolutePath()))) {
                DefaultLogger.w(getTag(), "DB [%s] is invalid when post download file for uri [%s]", dBItemForUri, requestItem2.mDownloadItem.getUri());
                DocumentFile documentFile4 = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                if (documentFile4 != null) {
                    documentFile4.delete();
                }
                fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.NOT_SYNCED, "DB error", null));
            } else if (!requestItem.mDownloadItem.compareAnsSetStatus(0, 3)) {
            } else {
                DownloadItem downloadItem = requestItem.mDownloadItem;
                String absolutePath = file.getAbsolutePath();
                if (requestItem.mDBItem.isSecretItem()) {
                    bArr = requestItem.mDBItem.getSecretKey();
                }
                DownloadItem.callbackSuccess(downloadItem, new DownloadedItem(absolutePath, bArr));
            }
        }
    }

    public final boolean isValidUrl(Uri uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
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

    /* JADX WARN: Removed duplicated region for block: B:42:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:98:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final org.json.JSONObject requestUrls(android.accounts.Account r13, com.miui.gallery.cloud.base.GalleryExtendedAuthToken r14, java.util.List<com.miui.gallery.sdk.download.assist.RequestItem> r15) {
        /*
            Method dump skipped, instructions count: 456
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader.requestUrls(android.accounts.Account, com.miui.gallery.cloud.base.GalleryExtendedAuthToken, java.util.List):org.json.JSONObject");
    }

    public final String getRequestUrl(RequestItem requestItem, String str) {
        return requestItem.mDBItem.isShareItem() ? HostManager.ShareMedia.getRequestThumbnailUrl() : HostManager.OwnerMedia.getRequestThumbnailUrl();
    }

    /* JADX WARN: Code restructure failed: missing block: B:161:0x044a, code lost:
        com.miui.gallery.util.logger.DefaultLogger.e(getTag(), "download for %s break, status: %s", r2.mDBItem.getFileName(), java.lang.Integer.valueOf(r2.mDownloadItem.getStatus()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0461, code lost:
        com.miui.gallery.util.BaseMiscUtil.closeSilently(r6);
        com.miui.gallery.util.BaseMiscUtil.closeSilently(r7);
        r23.disconnect();
        r1 = getTag();
        r3 = new java.lang.Object[4];
        r3[0] = r2.mDownloadItem;
        r3[1] = r2.mDBItem.getFileName();
        r3[2] = java.lang.Long.valueOf(java.lang.System.currentTimeMillis() - r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x048e, code lost:
        if (r11.exists() == false) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0490, code lost:
        r7 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0492, code lost:
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0493, code lost:
        r3[3] = java.lang.Boolean.valueOf(r7);
        com.miui.gallery.util.logger.DefaultLogger.d(r1, "download %s, origin file %s, cost %d, success %s", r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x049d, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x04b8, code lost:
        r7.flush();
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x04bf, code lost:
        r1 = r33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x04c2, code lost:
        com.miui.gallery.stat.SamplingStatHelper.trackHttpEvent(r1, java.lang.System.currentTimeMillis() - r13, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x04cb, code lost:
        r9 = r31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x04cd, code lost:
        r9.decreaseConnTimeout(r2.mDownloadItem.getType());
        com.miui.gallery.sdk.download.util.DownloadUtil.statDownloadSuccess(r2, r13, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x04d3, code lost:
        com.miui.gallery.util.BaseMiscUtil.closeSilently(r6);
        com.miui.gallery.util.BaseMiscUtil.closeSilently(r7);
        r23.disconnect();
        r1 = getTag();
        r3 = new java.lang.Object[4];
        r3[0] = r2.mDownloadItem;
        r3[1] = r2.mDBItem.getFileName();
        r3[2] = java.lang.Long.valueOf(java.lang.System.currentTimeMillis() - r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0500, code lost:
        if (r11.exists() == false) goto L244;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0502, code lost:
        r7 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0504, code lost:
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x0505, code lost:
        r3[3] = java.lang.Boolean.valueOf(r7);
        com.miui.gallery.util.logger.DefaultLogger.d(r1, "download %s, origin file %s, cost %d, success %s", r3);
        r8 = r22 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x0512, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x0514, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x0516, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0518, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0519, code lost:
        r9 = r31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x051c, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x051d, code lost:
        r9 = r31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x0520, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0521, code lost:
        r9 = r31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x053c, code lost:
        r3 = r0;
        r16 = r6;
        r17 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x0549, code lost:
        r3 = r0;
        r16 = r6;
        r17 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x0556, code lost:
        r3 = r0;
        r16 = r6;
        r17 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0925, code lost:
        com.miui.gallery.sdk.download.util.DownloadUtil.statDownloadRetryTimes(r2, r8, com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader.MAX_TRY_COUNT);
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x092e, code lost:
        return r11.getAbsolutePath();
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:288:0x0721 A[Catch: all -> 0x08e0, TryCatch #29 {all -> 0x08e0, blocks: (B:286:0x0709, B:288:0x0721, B:290:0x0740, B:289:0x0736, B:301:0x07ab, B:303:0x07ca, B:304:0x07e0, B:315:0x0849, B:317:0x0868, B:318:0x087e), top: B:343:0x0709 }] */
    /* JADX WARN: Removed duplicated region for block: B:289:0x0736 A[Catch: all -> 0x08e0, TryCatch #29 {all -> 0x08e0, blocks: (B:286:0x0709, B:288:0x0721, B:290:0x0740, B:289:0x0736, B:301:0x07ab, B:303:0x07ca, B:304:0x07e0, B:315:0x0849, B:317:0x0868, B:318:0x087e), top: B:343:0x0709 }] */
    /* JADX WARN: Removed duplicated region for block: B:293:0x075d  */
    /* JADX WARN: Removed duplicated region for block: B:296:0x0788  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x078a  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x07ca A[Catch: all -> 0x08e0, TryCatch #29 {all -> 0x08e0, blocks: (B:286:0x0709, B:288:0x0721, B:290:0x0740, B:289:0x0736, B:301:0x07ab, B:303:0x07ca, B:304:0x07e0, B:315:0x0849, B:317:0x0868, B:318:0x087e), top: B:343:0x0709 }] */
    /* JADX WARN: Removed duplicated region for block: B:307:0x07fd  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x0828  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x082a  */
    /* JADX WARN: Removed duplicated region for block: B:317:0x0868 A[Catch: all -> 0x08e0, TryCatch #29 {all -> 0x08e0, blocks: (B:286:0x0709, B:288:0x0721, B:290:0x0740, B:289:0x0736, B:301:0x07ab, B:303:0x07ca, B:304:0x07e0, B:315:0x0849, B:317:0x0868, B:318:0x087e), top: B:343:0x0709 }] */
    /* JADX WARN: Removed duplicated region for block: B:321:0x089b  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x08c6  */
    /* JADX WARN: Removed duplicated region for block: B:325:0x08c8  */
    /* JADX WARN: Type inference failed for: r2v39, types: [java.lang.Boolean] */
    /* JADX WARN: Type inference failed for: r2v46, types: [java.lang.Boolean] */
    /* JADX WARN: Type inference failed for: r2v51, types: [java.lang.Boolean] */
    /* JADX WARN: Type inference failed for: r4v78, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r4v82, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r4v85, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r6v27, types: [int] */
    /* JADX WARN: Type inference failed for: r6v28 */
    /* JADX WARN: Type inference failed for: r6v29 */
    /* JADX WARN: Type inference failed for: r6v37 */
    /* JADX WARN: Type inference failed for: r6v42 */
    /* JADX WARN: Type inference failed for: r6v45 */
    /* JADX WARN: Type inference failed for: r6v48 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.String downloadFile(com.miui.gallery.sdk.download.assist.RequestItem r32, java.lang.String r33) {
        /*
            Method dump skipped, instructions count: 2351
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader.downloadFile(com.miui.gallery.sdk.download.assist.RequestItem, java.lang.String):java.lang.String");
    }

    public final void increaseConnTimeout(DownloadType downloadType) {
        int connTimeout = GalleryPreferences.FileDownload.getConnTimeout(downloadType) + 3000;
        if (GalleryPreferences.FileDownload.setConnTimeout(downloadType, connTimeout)) {
            DefaultLogger.i(getTag(), "increase conn timeout %d, type %s", Integer.valueOf(connTimeout), downloadType.name());
        }
    }

    public final void decreaseConnTimeout(DownloadType downloadType) {
        int connTimeout = GalleryPreferences.FileDownload.getConnTimeout(downloadType) + VideoDecoder.DecodeCallback.ERROR_START;
        if (GalleryPreferences.FileDownload.setConnTimeout(downloadType, connTimeout)) {
            DefaultLogger.i(getTag(), "decrease conn timeout %d, type %s", Integer.valueOf(connTimeout), downloadType.name());
        }
    }

    public final void fireFailEvent(RequestItem requestItem, String str, DownloadFailReason downloadFailReason) {
        DefaultLogger.e(getTag(), "download fail %s", downloadFailReason.getCause());
        if (downloadFailReason.getCause() != null) {
            DefaultLogger.e(getTag(), downloadFailReason.getCause());
        }
        deleteTempFile(requestItem);
        if (requestItem.mDownloadItem.isStatusOk()) {
            DownloadItem.callbackError(requestItem.mDownloadItem, downloadFailReason);
        }
        DownloadUtil.statDownloadError(requestItem, str, downloadFailReason);
    }

    public final void deleteTempFile(RequestItem requestItem) {
        DBImage dBImage = requestItem.mDBItem;
        if (dBImage == null) {
            return;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(DownloadUtil.getDownloadTempFilePath(dBImage, requestItem.mDownloadItem.getType()), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag(getInvokerTag(), "deleteTempFile"));
        if (documentFile == null) {
            return;
        }
        documentFile.delete();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.miui.gallery.data.DBImage] */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.io.Closeable] */
    public final String downloadData(RequestItem requestItem, String str) {
        Throwable th;
        OutputStream outputStream;
        ?? r2 = requestItem.mDBItem;
        File file = new File(DownloadUtil.getDownloadTempFilePath(r2, requestItem.mDownloadItem.getType()));
        try {
            try {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT, FileHandleRecordHelper.appendInvokerTag(getInvokerTag(), "downloadData"));
                if (documentFile == null) {
                    BaseMiscUtil.closeSilently(null);
                    return null;
                }
                outputStream = StorageSolutionProvider.get().openOutputStream(documentFile);
                try {
                    outputStream.write(Base64.decode(str.getBytes(Keyczar.DEFAULT_ENCODING), 2));
                    BaseMiscUtil.closeSilently(outputStream);
                    return file.getAbsolutePath();
                } catch (FileNotFoundException e) {
                    e = e;
                    DefaultLogger.e(getTag(), "download data error.", e);
                    BaseMiscUtil.closeSilently(outputStream);
                    return null;
                } catch (UnsupportedEncodingException e2) {
                    e = e2;
                    DefaultLogger.e(getTag(), "download data error.", e);
                    BaseMiscUtil.closeSilently(outputStream);
                    return null;
                } catch (IOException e3) {
                    e = e3;
                    DefaultLogger.e(getTag(), "download data error.", e);
                    BaseMiscUtil.closeSilently(outputStream);
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(r2);
                throw th;
            }
        } catch (FileNotFoundException e4) {
            e = e4;
            outputStream = null;
        } catch (UnsupportedEncodingException e5) {
            e = e5;
            outputStream = null;
        } catch (IOException e6) {
            e = e6;
            outputStream = null;
        } catch (Throwable th3) {
            r2 = 0;
            th = th3;
            BaseMiscUtil.closeSilently(r2);
            throw th;
        }
    }

    public final boolean checkItemValid(RequestItem requestItem) {
        DBImage dBImage = requestItem.mDBItem;
        if (DownloadUtil.canDownloadThumbnailStatus(dBImage)) {
            return true;
        }
        if (dBImage == null) {
            fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.PARAMS_ERROR, "dbImage null", null));
        } else if (DownloadUtil.isNotSyncedStatus(dBImage)) {
            fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.NOT_SYNCED, String.format("item invalid server[%s], local[%s]", dBImage.getServerStatus(), Integer.valueOf(dBImage.getLocalFlag())), null));
        } else {
            fireFailEvent(requestItem, null, new DownloadFailReason(ErrorCode.PARAMS_ERROR, String.format("item invalid server[%s], local[%s]", dBImage.getServerStatus(), Integer.valueOf(dBImage.getLocalFlag())), null));
        }
        return false;
    }
}
