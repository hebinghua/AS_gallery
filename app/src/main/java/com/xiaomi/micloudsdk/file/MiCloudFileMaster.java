package com.xiaomi.micloudsdk.file;

import android.content.Context;
import cn.kuaipan.android.kss.download.DownloadDescriptorFile;
import cn.kuaipan.android.kss.download.KssDownloadFile;
import cn.kuaipan.android.kss.upload.KssUploadFile;
import cn.kuaipan.android.kss.upload.UploadDescriptorFile;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.opensdk.file.model.MiCloudFileClient;
import com.xiaomi.opensdk.file.model.MiCloudFileListener;
import com.xiaomi.opensdk.file.model.MiCloudTransferStopper;
import com.xiaomi.opensdk.file.model.UploadContext;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class MiCloudFileMaster<T> {
    public Context mContext;
    public MiCloudFileRequestor<T> mRequestor;

    /* loaded from: classes3.dex */
    public interface DownloadParameterProvider {
        JSONObject getDownloadJSONObject() throws UnretriableException, RetriableException, AuthenticationException, JSONException;
    }

    public MiCloudFileMaster(Context context, MiCloudFileRequestor<T> miCloudFileRequestor) {
        this.mContext = context;
        this.mRequestor = miCloudFileRequestor;
    }

    public T upload(T t, UploadDescriptorFile uploadDescriptorFile, MiCloudFileListener miCloudFileListener) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        return upload((MiCloudFileMaster<T>) t, uploadDescriptorFile, miCloudFileListener, (MiCloudTransferStopper) null);
    }

    public T upload(T t, UploadDescriptorFile uploadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        if (uploadDescriptorFile == null) {
            throw new UnretriableException("参数错误，请参考API文档");
        }
        return upload((MiCloudFileMaster<T>) t, KssUploadFile.createByFileDescriptor(uploadDescriptorFile), miCloudFileListener, miCloudTransferStopper);
    }

    public final T upload(T t, KssUploadFile kssUploadFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        if (MiCloudFileMasterInjector.checkUploadConditions(this.mContext)) {
            if (t == null || kssUploadFile == null) {
                throw new UnretriableException("参数错误，请参考API文档");
            }
            UploadContext uploadContext = new UploadContext(kssUploadFile, miCloudFileListener, miCloudTransferStopper);
            MiCloudFileClient.getInstance(this.mContext).upload(uploadContext);
            for (int i = 0; uploadContext.isNeedRequestUpload() && i < 5; i++) {
                try {
                    JSONObject requestUpload = this.mRequestor.requestUpload(t, MiCloudFileClient.getInstance(this.mContext).getRequestUploadParameter(uploadContext));
                    T handleRequestUploadResultJson = this.mRequestor.handleRequestUploadResultJson(t, requestUpload);
                    if (handleRequestUploadResultJson != null) {
                        return handleRequestUploadResultJson;
                    }
                    uploadContext.setUploadParam(MiCloudFileClient.getInstance(this.mContext).getUploadParameterForSFS(requestUpload.getJSONObject("data")));
                    MiCloudFileClient.getInstance(this.mContext).upload(uploadContext);
                } catch (JSONException e) {
                    throw new UnretriableException(e);
                }
            }
            return this.mRequestor.handleCommitUploadResult(t, this.mRequestor.commitUpload(t, MiCloudFileClient.getInstance(this.mContext).getCommitParameter(uploadContext)));
        }
        throw new UnretriableException("Upload is forbidden by injector");
    }

    public void download(final T t, DownloadDescriptorFile downloadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        download(downloadDescriptorFile, miCloudFileListener, miCloudTransferStopper, new DownloadParameterProvider() { // from class: com.xiaomi.micloudsdk.file.MiCloudFileMaster.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.xiaomi.micloudsdk.file.MiCloudFileMaster.DownloadParameterProvider
            public JSONObject getDownloadJSONObject() throws UnretriableException, RetriableException, AuthenticationException, JSONException {
                JSONObject requestDownload = MiCloudFileMaster.this.mRequestor.requestDownload(t);
                if (MiCloudFileMaster.this.mRequestor.handleRequestDownloadResultJson(t, requestDownload)) {
                    return requestDownload.getJSONObject("data");
                }
                return null;
            }
        });
    }

    public void download(DownloadDescriptorFile downloadDescriptorFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper, DownloadParameterProvider downloadParameterProvider) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        if (downloadDescriptorFile == null) {
            throw new UnretriableException("参数错误，请参考API文档");
        }
        download(KssDownloadFile.createByFileDescriptor(downloadDescriptorFile), miCloudFileListener, miCloudTransferStopper, downloadParameterProvider);
    }

    public final void download(KssDownloadFile kssDownloadFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper, DownloadParameterProvider downloadParameterProvider) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        if (MiCloudFileMasterInjector.checkDownloadConditions(this.mContext)) {
            if (kssDownloadFile == null) {
                throw new UnretriableException("参数错误，请参考API文档");
            }
            try {
                JSONObject downloadJSONObject = downloadParameterProvider.getDownloadJSONObject();
                if (downloadJSONObject == null) {
                    return;
                }
                MiCloudFileClient.getInstance(this.mContext).download(kssDownloadFile, MiCloudFileClient.getInstance(this.mContext).getDownloadParameterForSFS(downloadJSONObject), miCloudFileListener, miCloudTransferStopper);
                return;
            } catch (JSONException e) {
                throw new UnretriableException(e);
            }
        }
        throw new UnretriableException("Upload is forbidden by injector");
    }
}
