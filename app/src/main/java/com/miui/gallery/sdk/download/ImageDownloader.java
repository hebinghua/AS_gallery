package com.miui.gallery.sdk.download;

import android.net.Uri;
import android.util.Log;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.sdk.SyncStatus;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.adapter.IUriAdapter;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.assist.DownloadItemStatus;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.sdk.download.downloader.IDownloader;
import com.miui.gallery.sdk.download.listener.DownloadListener;
import com.miui.gallery.sdk.download.listener.DownloadProgressListener;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class ImageDownloader {
    public static volatile ImageDownloader sInstance;
    public final DownloadEngine mEngine = new DownloadEngine();

    public static ImageDownloader getInstance() {
        if (sInstance == null) {
            synchronized (ImageDownloader.class) {
                if (sInstance == null) {
                    sInstance = new ImageDownloader();
                }
            }
        }
        return sInstance;
    }

    public void load(Uri uri, DownloadType downloadType, DownloadOptions downloadOptions) {
        load(uri, downloadType, downloadOptions, null);
    }

    public void load(Uri uri, DownloadType downloadType, DownloadOptions downloadOptions, DownloadListener downloadListener) {
        load(uri, downloadType, downloadOptions, downloadListener, null);
    }

    public void load(Uri uri, DownloadType downloadType, DownloadOptions downloadOptions, DownloadListener downloadListener, DownloadProgressListener downloadProgressListener) {
        if (uri == null || downloadType == null) {
            if (downloadListener == null) {
                return;
            }
            downloadListener.onDownloadFail(uri, downloadType, new DownloadFailReason(ErrorCode.PARAMS_ERROR, "", null));
            return;
        }
        DownloadOptions ensureDownloadOptions = ensureDownloadOptions(downloadOptions);
        IUriAdapter uriAdapter = ensureDownloadOptions.getUriAdapter();
        ReentrantLock lockForUri = this.mEngine.getLockForUri(uri, downloadType);
        this.mEngine.download(new DownloadItem.Builder().setUri(uri).setType(downloadType).setUriAdapter(uriAdapter).setUriLock(lockForUri).setDownloadListener(downloadListener).setProgressListener(downloadProgressListener).setDownloader(this.mEngine.getDownloader(downloadType)).setRequirePower(ensureDownloadOptions.isRequirePower()).setRequireCharging(ensureDownloadOptions.isRequireCharging()).setRequireWLAN(ensureDownloadOptions.isRequireWLAN()).setRequireDeviceStorage(ensureDownloadOptions.isRequireDeviceStorage()).setManual(ensureDownloadOptions.isManual()).build(), ensureDownloadOptions.isQueueFirst(), ensureDownloadOptions.isInterruptExecuting());
    }

    public DownloadedItem loadSync(Uri uri, DownloadType downloadType, DownloadOptions downloadOptions, DownloadProgressListener downloadProgressListener) {
        return loadSync(uri, downloadType, downloadOptions, downloadProgressListener, null);
    }

    public DownloadedItem loadSync(Uri uri, DownloadType downloadType, DownloadOptions downloadOptions, DownloadProgressListener downloadProgressListener, final DownloadListener downloadListener) {
        if (uri == null || downloadType == null) {
            return null;
        }
        DownloadOptions ensureDownloadOptions = ensureDownloadOptions(downloadOptions);
        IUriAdapter uriAdapter = ensureDownloadOptions.getUriAdapter();
        ReentrantLock lockForUri = this.mEngine.getLockForUri(uri, downloadType);
        IDownloader downloader = this.mEngine.getDownloader(downloadType);
        AccountCache.AccountInfo accountInfo = AccountCache.getAccountInfo();
        if (accountInfo == null) {
            return null;
        }
        final DownloadedItem[] downloadedItemArr = {null};
        DownloadItem build = new DownloadItem.Builder().setUri(uri).setType(downloadType).setUriAdapter(uriAdapter).setUriLock(lockForUri).setProgressListener(downloadProgressListener).setDownloadListener(new DownloadListener() { // from class: com.miui.gallery.sdk.download.ImageDownloader.1
            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadStarted(Uri uri2, DownloadType downloadType2) {
                DownloadListener downloadListener2 = downloadListener;
                if (downloadListener2 != null) {
                    downloadListener2.onDownloadStarted(uri2, downloadType2);
                }
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadSuccess(Uri uri2, DownloadType downloadType2, DownloadedItem downloadedItem) {
                downloadedItemArr[0] = downloadedItem;
                DownloadListener downloadListener2 = downloadListener;
                if (downloadListener2 != null) {
                    downloadListener2.onDownloadSuccess(uri2, downloadType2, downloadedItem);
                }
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadFail(Uri uri2, DownloadType downloadType2, DownloadFailReason downloadFailReason) {
                DownloadListener downloadListener2 = downloadListener;
                if (downloadListener2 != null) {
                    downloadListener2.onDownloadFail(uri2, downloadType2, downloadFailReason);
                }
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadCancel(Uri uri2, DownloadType downloadType2) {
                DownloadListener downloadListener2 = downloadListener;
                if (downloadListener2 != null) {
                    downloadListener2.onDownloadCancel(uri2, downloadType2);
                }
            }
        }).setDownloader(downloader).setRequirePower(ensureDownloadOptions.isRequirePower()).setRequireCharging(ensureDownloadOptions.isRequireCharging()).setRequireWLAN(ensureDownloadOptions.isRequireWLAN()).setRequireDeviceStorage(ensureDownloadOptions.isRequireDeviceStorage()).setManual(ensureDownloadOptions.isManual()).build();
        LinkedList linkedList = new LinkedList();
        linkedList.add(build);
        downloader.download(accountInfo.mAccount, accountInfo.mToken, linkedList);
        return downloadedItemArr[0];
    }

    public DownloadOptions ensureDownloadOptions(DownloadOptions downloadOptions) {
        if (downloadOptions == null) {
            Log.d("ImageDownloader", "download options is null, use default");
            downloadOptions = this.mEngine.mDefaultDownloadOptions;
        }
        return downloadOptions.getUriAdapter() == null ? new DownloadOptions.Builder().cloneFrom(downloadOptions).setUriAdapter(IUriAdapter.DEFAULT).build() : downloadOptions;
    }

    public void cancel(Uri uri, DownloadType downloadType) {
        this.mEngine.cancel(new DownloadItem.Builder().setUri(uri).setType(downloadType).build());
    }

    public void cancelAll(DownloadType downloadType) {
        this.mEngine.cancelAll(downloadType);
    }

    public void cancelAll() {
        this.mEngine.cancelAll();
    }

    public boolean contains(Uri uri, DownloadType downloadType) {
        return this.mEngine.contains(new DownloadItem.Builder().setUri(uri).setType(downloadType).build());
    }

    public DownloadItem peek(Uri uri, DownloadType downloadType) {
        return this.mEngine.peek(new DownloadItem.Builder().setUri(uri).setType(downloadType).build());
    }

    public boolean isOrigin(DownloadType downloadType) {
        return downloadType != null && (downloadType == DownloadType.ORIGIN || downloadType == DownloadType.ORIGIN_FORCE);
    }

    public DownloadItemStatus getStatusSync(Uri uri, DownloadType downloadType) {
        SyncStatus syncStatus = SyncStatus.STATUS_NONE;
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ImageDownloader", "getStatusSync");
        if (getInstance().contains(uri, downloadType)) {
            return new DownloadItemStatus(SyncStatus.STATUS_INIT, null);
        }
        if (isOrigin(downloadType)) {
            DBImage dBItemForUri = IUriAdapter.DEFAULT.getDBItemForUri(uri);
            if (dBItemForUri == null) {
                return new DownloadItemStatus(syncStatus, null);
            }
            String verifiedDownloadFilePathForRead = new RequestCloudItem(downloadType.getPriority(), dBItemForUri).getVerifiedDownloadFilePathForRead();
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(verifiedDownloadFilePathForRead, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile == null || !documentFile.exists()) {
                int downloadFileStatusFromDB = CloudUtils.getDownloadFileStatusFromDB(dBItemForUri);
                if (2 == downloadFileStatusFromDB) {
                    downloadFileStatusFromDB = -1;
                    DownloadUtil.persistDownloadStatus(dBItemForUri, 0);
                }
                return new DownloadItemStatus(SyncStatus.toSyncStatus(downloadFileStatusFromDB), null);
            }
            return new DownloadItemStatus(SyncStatus.STATUS_SUCCESS, new DownloadedItem(verifiedDownloadFilePathForRead, dBItemForUri.getSecretKeyNoGenerate()));
        }
        DBImage dBItemForUri2 = IUriAdapter.DEFAULT.getDBItemForUri(uri);
        if (dBItemForUri2 == null) {
            return new DownloadItemStatus(syncStatus, null);
        }
        String verifiedDownloadFilePathForRead2 = new RequestCloudItem(downloadType.getPriority(), dBItemForUri2).getVerifiedDownloadFilePathForRead();
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(verifiedDownloadFilePathForRead2, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        if (documentFile2 == null || !documentFile2.exists()) {
            return new DownloadItemStatus(syncStatus, null);
        }
        return new DownloadItemStatus(SyncStatus.STATUS_SUCCESS, new DownloadedItem(verifiedDownloadFilePathForRead2, dBItemForUri2.getSecretKeyNoGenerate()));
    }
}
