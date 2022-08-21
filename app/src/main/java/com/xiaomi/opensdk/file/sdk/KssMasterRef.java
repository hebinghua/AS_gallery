package com.xiaomi.opensdk.file.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.exception.KscRuntimeException;
import cn.kuaipan.android.exception.ServerMsgException;
import cn.kuaipan.android.http.IKscTransferListener;
import cn.kuaipan.android.http.KscHttpTransmitter;
import cn.kuaipan.android.http.KssTransferStopper;
import cn.kuaipan.android.kss.KssDef;
import cn.kuaipan.android.kss.download.KssDownloadFile;
import cn.kuaipan.android.kss.download.KssDownloader;
import cn.kuaipan.android.kss.upload.KssUploadFile;
import cn.kuaipan.android.kss.upload.KssUploadInfo;
import cn.kuaipan.android.kss.upload.KssUploader;
import cn.kuaipan.android.kss.upload.UploadFileInfo;
import cn.kuaipan.android.kss.upload.UploadTaskStore;
import cn.kuaipan.android.utils.ContextUtils;
import com.xiaomi.opensdk.file.model.DownloadParameter;
import com.xiaomi.opensdk.file.model.MiCloudFileListener;
import com.xiaomi.opensdk.file.model.MiCloudTransferStopper;
import com.xiaomi.opensdk.file.model.UploadContext;
import com.xiaomi.opensdk.file.model.UploadParameter;
import com.xiaomi.opensdk.file.utils.FileSDKUtils;
import org.json.JSONException;

/* loaded from: classes3.dex */
public class KssMasterRef implements KssDef {
    public final String TAG = "KssMasterRef";
    public final KssDownloader mDownloader;
    public final UploadTaskStore mTaskStore;
    public final KssUploader mUploader;

    public KssMasterRef(Context context) {
        UploadTaskStore uploadTaskStore = new UploadTaskStore(context, new FileDataFactory());
        this.mTaskStore = uploadTaskStore;
        KscHttpTransmitter kscHttpTransmitter = new KscHttpTransmitter(context);
        kscHttpTransmitter.setUserAgent(4, getUserAgent(context));
        this.mUploader = new KssUploader(kscHttpTransmitter, uploadTaskStore);
        this.mDownloader = new KssDownloader(kscHttpTransmitter);
    }

    public void upload(UploadContext uploadContext) throws KscException, InterruptedException {
        KssUploadFile localFile = uploadContext.getLocalFile();
        if (localFile.isFileInvalid()) {
            throw new KscRuntimeException(500003, localFile.filePath + " is not a exist file.");
        }
        KscTransferListener kscTransferListener = new KscTransferListener(uploadContext.getListener());
        UploadFileInfo fileInfo = UploadFileInfo.getFileInfo(localFile);
        int uploadHash = getUploadHash(localFile.filePath, localFile.fileAbsPath, fileInfo);
        if (!hasStoredUploadInfo(uploadHash) && uploadContext.getUploadParam() == null) {
            uploadContext.setNeedRequestUpload(true);
            uploadContext.setKssString(fileInfo.getKssString());
            uploadContext.setSha1(fileInfo.getSha1());
            return;
        }
        KssUploadInfo kssUploadInfo = null;
        while (!Thread.interrupted()) {
            if (kssUploadInfo == null) {
                kssUploadInfo = getUploadInfo(fileInfo, uploadContext, uploadHash);
            }
            KssUploadInfo kssUploadInfo2 = kssUploadInfo;
            if (kssUploadInfo2 == null) {
                kscTransferListener.setSendTotal(localFile.fileSize);
                kscTransferListener.setSendPos(localFile.fileSize);
                return;
            } else if (kssUploadInfo2.isBroken()) {
                uploadContext.setNeedRequestUpload(true);
                uploadContext.setUploadParam(null);
                return;
            } else if (kssUploadInfo2.isCompleted()) {
                kscTransferListener.setSendTotal(localFile.fileSize);
                kscTransferListener.setSendPos(localFile.fileSize);
                deleteUploadInfo(uploadHash);
                uploadContext.setNeedRequestUpload(false);
                uploadContext.setCommitString(kssUploadInfo2.getCommitString());
                uploadContext.setUploadId(kssUploadInfo2.getUploadId());
                uploadContext.setSha1(kssUploadInfo2.getFileInfo().getSha1());
                return;
            } else {
                this.mUploader.upload(localFile, kscTransferListener, KssTransferStopper.KssTransferStopperFromMiCloudTransferStopper.get(uploadContext.getStopper()), uploadHash, kssUploadInfo2);
                kssUploadInfo = kssUploadInfo2;
            }
        }
        throw new InterruptedException();
    }

    public void download(KssDownloadFile kssDownloadFile, DownloadParameter downloadParameter, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper, boolean z) throws KscException, InterruptedException {
        if (kssDownloadFile == null) {
            throw new KscRuntimeException(500003, "downloadFile can't be null.");
        }
        KscTransferListener kscTransferListener = new KscTransferListener(miCloudFileListener);
        try {
            FileDownloadRequestResult fileDownloadRequestResult = new FileDownloadRequestResult(FileSDKUtils.contentKssJsonToMap(downloadParameter.toJsonObject()));
            if (fileDownloadRequestResult.getStatus() != 0) {
                String message = fileDownloadRequestResult.getMessage();
                if (TextUtils.isEmpty(message)) {
                    throw new KscException(503000, "Unknow error when requestDownload.");
                }
                throw new ServerMsgException(200, message, "Failed on requestDownload");
            }
            this.mDownloader.download(kssDownloadFile, z, kscTransferListener, KssTransferStopper.KssTransferStopperFromMiCloudTransferStopper.get(miCloudTransferStopper), fileDownloadRequestResult);
        } catch (JSONException e) {
            throw KscException.newException(e, "download failed");
        }
    }

    public final KssUploadInfo getUploadInfo(UploadFileInfo uploadFileInfo, UploadContext uploadContext, int i) throws KscException, InterruptedException {
        UploadTaskStore uploadTaskStore = this.mTaskStore;
        KssUploadInfo uploadInfo = uploadTaskStore == null ? null : uploadTaskStore.getUploadInfo(i);
        if (uploadInfo == null) {
            UploadParameter uploadParam = uploadContext.getUploadParam();
            if (uploadParam == null) {
                throw new KscRuntimeException(500003, "uploadParam null");
            }
            try {
                KssUploadInfo kssUploadInfo = new KssUploadInfo(uploadFileInfo, new FileUploadRequestResult(FileSDKUtils.contentKssJsonToMap(uploadParam.toJsonObject())));
                kssUploadInfo.setUploadId(uploadParam.getUploadId());
                UploadTaskStore uploadTaskStore2 = this.mTaskStore;
                if (uploadTaskStore2 != null) {
                    uploadTaskStore2.putUploadInfo(i, kssUploadInfo);
                }
                uploadInfo = kssUploadInfo;
            } catch (JSONException e) {
                throw KscException.newException(e, "getUploadInfo failed");
            }
        }
        uploadInfo.setMaxChunkSize(uploadContext.getMaxChunkSize());
        Log.w("KssMasterRef", "KssUploadInfo Return:" + uploadInfo.getUploadId());
        return uploadInfo;
    }

    public boolean hasStoredUploadInfo(int i) throws InterruptedException {
        Boolean valueOf;
        UploadTaskStore uploadTaskStore = this.mTaskStore;
        if (uploadTaskStore == null) {
            valueOf = null;
        } else {
            valueOf = Boolean.valueOf(uploadTaskStore.getUploadInfo(i) != null);
        }
        return valueOf.booleanValue();
    }

    public final void deleteUploadInfo(int i) throws InterruptedException {
        UploadTaskStore uploadTaskStore = this.mTaskStore;
        if (uploadTaskStore == null) {
            return;
        }
        uploadTaskStore.removeUploadInfo(i);
    }

    public static String getUserAgent(Context context) {
        return String.format("KssRC4/1.0 %s/%s S3SDK/%s", context.getPackageName(), ContextUtils.getAppVersion(context), "0.9.0a");
    }

    public static int getUploadHash(String str, String str2, UploadFileInfo uploadFileInfo) {
        String sha1 = uploadFileInfo == null ? "" : uploadFileInfo.getSha1();
        return (str + ":" + str2 + ":" + sha1).hashCode();
    }

    /* loaded from: classes3.dex */
    public class KscTransferListener extends IKscTransferListener.KscTransferListener {
        public MiCloudFileListener mMiCloudFileListener;

        public KscTransferListener(MiCloudFileListener miCloudFileListener) {
            this.mMiCloudFileListener = miCloudFileListener;
        }

        @Override // cn.kuaipan.android.http.IKscTransferListener.KscTransferListener
        public void onDataSended(long j, long j2) {
            MiCloudFileListener miCloudFileListener = this.mMiCloudFileListener;
            if (miCloudFileListener != null) {
                miCloudFileListener.onDataSended(j, j2);
            }
        }

        @Override // cn.kuaipan.android.http.IKscTransferListener.KscTransferListener
        public void onDataReceived(long j, long j2) {
            MiCloudFileListener miCloudFileListener = this.mMiCloudFileListener;
            if (miCloudFileListener != null) {
                miCloudFileListener.onDataReceived(j, j2);
            }
        }
    }
}
