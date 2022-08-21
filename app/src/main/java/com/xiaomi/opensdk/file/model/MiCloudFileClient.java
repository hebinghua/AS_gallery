package com.xiaomi.opensdk.file.model;

import android.content.Context;
import android.text.TextUtils;
import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.exception.KscRuntimeException;
import cn.kuaipan.android.exception.KscTransferStopByCallerException;
import cn.kuaipan.android.exception.NetworkException;
import cn.kuaipan.android.exception.ServerException;
import cn.kuaipan.android.exception.SessionExpiredException;
import cn.kuaipan.android.kss.download.KssDownloadFile;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.TransferStopByCallerException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.opensdk.file.sdk.KssMasterRef;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public final class MiCloudFileClient {
    public static volatile MiCloudFileClient sInstance;
    public KssMasterRef mKssMasterRef;

    public MiCloudFileClient(Context context) {
        this.mKssMasterRef = new KssMasterRef(context);
    }

    public static MiCloudFileClient getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MiCloudFileClient.class) {
                if (sInstance == null) {
                    if (context == null) {
                        throw new IllegalArgumentException("context can't be null");
                    }
                    sInstance = new MiCloudFileClient(context);
                }
            }
        }
        return sInstance;
    }

    public void upload(UploadContext uploadContext) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        try {
            this.mKssMasterRef.upload(uploadContext);
        } catch (KscException e) {
            transferException(e);
        } catch (KscRuntimeException e2) {
            transferException(e2);
        }
    }

    public void download(KssDownloadFile kssDownloadFile, DownloadParameter downloadParameter, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) throws RetriableException, UnretriableException, AuthenticationException, InterruptedException {
        if (!TextUtils.isEmpty(downloadParameter.getKssDownloadString())) {
            try {
                this.mKssMasterRef.download(kssDownloadFile, downloadParameter, miCloudFileListener, miCloudTransferStopper, true);
                return;
            } catch (KscException e) {
                transferException(e);
                return;
            } catch (KscRuntimeException e2) {
                transferException(e2);
                return;
            }
        }
        throw new IllegalArgumentException("Cannot detect download sdk");
    }

    public RequestUploadParameter getRequestUploadParameter(UploadContext uploadContext) {
        return new RequestUploadParameter(uploadContext.getKssString(), null, uploadContext.getSha1(), uploadContext.getFileSize(), uploadContext.getFilePath());
    }

    public UploadParameter getUploadParameterForSFS(JSONObject jSONObject) throws JSONException {
        String string;
        UploadParameter uploadParameter = new UploadParameter();
        if (jSONObject.has("upload_id")) {
            string = jSONObject.getString("upload_id");
        } else if (jSONObject.has("uploadId")) {
            string = jSONObject.getString("uploadId");
        } else {
            throw new JSONException("Missing necessary field : upload_id / uploadId");
        }
        uploadParameter.setUploadId(string);
        uploadParameter.setKssUploadString(jSONObject.getJSONObject("kss").toString());
        return uploadParameter;
    }

    public CommitParameter getCommitParameter(UploadContext uploadContext) {
        return new CommitParameter(uploadContext.getCommitString(), null, uploadContext.getUploadId(), uploadContext.getSha1(), uploadContext.getFileSize());
    }

    public DownloadParameter getDownloadParameterForSFS(JSONObject jSONObject) throws JSONException {
        DownloadParameter downloadParameter = new DownloadParameter();
        downloadParameter.setKssDownloadString(jSONObject.getJSONObject("kss").toString());
        return downloadParameter;
    }

    public final void transferException(Exception exc) throws RetriableException, UnretriableException, AuthenticationException {
        if (exc instanceof KscException) {
            Throwable cause = exc.getCause();
            if (cause instanceof KscTransferStopByCallerException) {
                throw new UnretriableException(new TransferStopByCallerException(cause));
            }
            String simpleMessage = ((KscException) exc).getSimpleMessage();
            if (exc instanceof NetworkException) {
                throw new RetriableException(simpleMessage, 300000L);
            }
            if (exc instanceof ServerException) {
                if (((ServerException) exc).getStatusCode() / 100 == 5) {
                    throw new RetriableException(simpleMessage, 300000L);
                }
            } else if (exc instanceof SessionExpiredException) {
                throw new UnretriableException(exc);
            }
            throw new UnretriableException(simpleMessage);
        } else if (exc instanceof KscRuntimeException) {
            throw new UnretriableException(exc, ((KscRuntimeException) exc).getErrorCode());
        }
    }
}
