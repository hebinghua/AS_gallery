package com.miui.gallery.cloud;

import android.content.Context;
import cn.kuaipan.android.kss.download.DownloadDescriptorFile;
import cn.kuaipan.android.kss.upload.UploadDescriptorFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.xiaomi.micloudsdk.file.MiCloudFileMaster;
import com.xiaomi.micloudsdk.file.MiCloudFileRequestor;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.opensdk.file.model.MiCloudFileListener;
import com.xiaomi.opensdk.file.model.MiCloudTransferStopper;

/* loaded from: classes.dex */
public class GalleryKssManager {
    public static MiCloudFileMaster<RequestCloudItem>[] sMaster = new MiCloudFileMaster[4];

    public static synchronized void reset() {
        synchronized (GalleryKssManager.class) {
            int i = 0;
            while (true) {
                MiCloudFileMaster<RequestCloudItem>[] miCloudFileMasterArr = sMaster;
                if (i < miCloudFileMasterArr.length) {
                    miCloudFileMasterArr[i] = null;
                    i++;
                }
            }
        }
    }

    public static synchronized MiCloudFileMaster<RequestCloudItem> createRequestor(boolean z, boolean z2) {
        MiCloudFileRequestor cloudGalleryOwnerRequestor;
        synchronized (GalleryKssManager.class) {
            int i = (!z ? 1 : 0) + (z2 ? 0 : 2);
            if (sMaster[i] == null) {
                AccountCache.AccountInfo accountInfo = AccountCache.getAccountInfo();
                if (accountInfo == null) {
                    return null;
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                CloudUrlProvider urlProvider = CloudUrlProvider.getUrlProvider(z, z2);
                if (z) {
                    cloudGalleryOwnerRequestor = new CloudGallerySharerRequestor(accountInfo.mAccount, urlProvider);
                } else {
                    cloudGalleryOwnerRequestor = new CloudGalleryOwnerRequestor(accountInfo.mAccount, urlProvider);
                }
                sMaster[i] = new MiCloudFileMaster<>(sGetAndroidContext, cloudGalleryOwnerRequestor);
            }
            return sMaster[i];
        }
    }

    public static void doOwnerUpload(RequestCloudItem requestCloudItem, UploadDescriptorFile uploadDescriptorFile) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        MiCloudFileMaster<RequestCloudItem> createRequestor = createRequestor(false, requestCloudItem.dbCloud.isVideoType());
        if (createRequestor != null) {
            ApplicationHelper.getMiCloudProvider().doFileSDKUpload(createRequestor, requestCloudItem, uploadDescriptorFile, new UploadTransferListener(requestCloudItem));
        }
    }

    public static void doSharerUpload(RequestCloudItem requestCloudItem, UploadDescriptorFile uploadDescriptorFile) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        MiCloudFileMaster<RequestCloudItem> createRequestor = createRequestor(true, requestCloudItem.dbCloud.isVideoType());
        if (createRequestor != null) {
            ApplicationHelper.getMiCloudProvider().doFileSDKUpload(createRequestor, requestCloudItem, uploadDescriptorFile, new UploadTransferListener(requestCloudItem));
        }
    }

    public static void doOwnerDownload(RequestCloudItem requestCloudItem, DownloadDescriptorFile downloadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        doDownload(createRequestor(false, requestCloudItem.dbCloud.isVideoType()), requestCloudItem, downloadDescriptorFile, miCloudFileListener, miCloudTransferStopper);
    }

    public static void doSharerDownload(RequestCloudItem requestCloudItem, DownloadDescriptorFile downloadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        doDownload(createRequestor(true, requestCloudItem.dbCloud.isVideoType()), requestCloudItem, downloadDescriptorFile, miCloudFileListener, miCloudTransferStopper);
    }

    public static void doDownload(MiCloudFileMaster<RequestCloudItem> miCloudFileMaster, RequestCloudItem requestCloudItem, DownloadDescriptorFile downloadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        if (miCloudFileMaster == null) {
            return;
        }
        ApplicationHelper.getMiCloudProvider().doFileSDKDownload(miCloudFileMaster, requestCloudItem, downloadDescriptorFile, miCloudFileListener, miCloudTransferStopper);
    }
}
