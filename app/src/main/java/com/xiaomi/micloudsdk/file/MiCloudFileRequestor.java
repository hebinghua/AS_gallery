package com.xiaomi.micloudsdk.file;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.request.utils.Request;
import com.xiaomi.opensdk.exception.AuthenticationException;
import com.xiaomi.opensdk.exception.RetriableException;
import com.xiaomi.opensdk.exception.UnretriableException;
import com.xiaomi.opensdk.file.model.CommitParameter;
import com.xiaomi.opensdk.file.model.RequestUploadParameter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import miui.cloud.log.PrivacyFilter;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public abstract class MiCloudFileRequestor<T> {
    public abstract Map<String, String> getCommitUploadParams(T t, CommitParameter commitParameter) throws JSONException;

    public abstract String getCommitUploadURL(T t, CommitParameter commitParameter);

    public abstract Map<String, String> getRequestDownloadParams(T t) throws JSONException;

    public abstract String getRequestDownloadURL(T t);

    public abstract Map<String, String> getRequestUploadParams(T t, RequestUploadParameter requestUploadParameter) throws JSONException;

    public abstract String getRequestUploadURL(T t, RequestUploadParameter requestUploadParameter);

    public abstract T handleCommitUploadResult(T t, JSONObject jSONObject) throws UnretriableException, RetriableException, AuthenticationException;

    public abstract boolean handleRequestDownloadResultJson(T t, JSONObject jSONObject) throws UnretriableException, RetriableException, AuthenticationException;

    public abstract T handleRequestUploadResultJson(T t, JSONObject jSONObject) throws UnretriableException, RetriableException, AuthenticationException;

    public MiCloudFileRequestor(Context context) {
        Request.init(context);
    }

    public JSONObject requestUpload(T t, RequestUploadParameter requestUploadParameter) throws RetriableException, UnretriableException, AuthenticationException {
        String requestUploadURL = getRequestUploadURL(t, requestUploadParameter);
        if (TextUtils.isEmpty(requestUploadURL)) {
            throw new UnretriableException("requestUploadUrl is null or empty.");
        }
        try {
            Map<String, String> requestUploadParams = getRequestUploadParams(t, requestUploadParameter);
            if (requestUploadParams == null) {
                throw new UnretriableException("getRequestUploadParams() can't return null.");
            }
            try {
                return new JSONObject(getHttpResopnse(requestUploadURL, requestUploadParams, true));
            } catch (JSONException e) {
                throw new UnretriableException("Http 200 返回的不是JSON格式:" + e);
            }
        } catch (JSONException e2) {
            throw new UnretriableException("error in getRequestUploadParams():" + e2);
        }
    }

    public JSONObject commitUpload(T t, CommitParameter commitParameter) throws RetriableException, UnretriableException, AuthenticationException {
        String commitUploadURL = getCommitUploadURL(t, commitParameter);
        if (TextUtils.isEmpty(commitUploadURL)) {
            throw new UnretriableException("commitUploadUrl is null or empty.");
        }
        try {
            Map<String, String> commitUploadParams = getCommitUploadParams(t, commitParameter);
            if (commitUploadParams == null) {
                throw new UnretriableException("getCommitUploadParams() can't return null.");
            }
            try {
                return new JSONObject(getHttpResopnse(commitUploadURL, commitUploadParams, true));
            } catch (JSONException e) {
                throw new UnretriableException("Http 200 返回的不是JSON格式:" + e);
            }
        } catch (JSONException e2) {
            throw new UnretriableException("error in getCommitUploadParams():" + e2);
        }
    }

    public JSONObject requestDownload(T t) throws RetriableException, UnretriableException, AuthenticationException {
        String requestDownloadURL = getRequestDownloadURL(t);
        if (TextUtils.isEmpty(requestDownloadURL)) {
            throw new UnretriableException("requestDownloadUrl is null or empty.");
        }
        try {
            Map<String, String> requestDownloadParams = getRequestDownloadParams(t);
            if (requestDownloadParams == null) {
                throw new UnretriableException("getRequestDownloadParams() can't return null.");
            }
            try {
                return new JSONObject(getHttpResopnse(requestDownloadURL, requestDownloadParams, false));
            } catch (JSONException e) {
                throw new UnretriableException("Http 200 返回的不是JSON格式:" + e);
            }
        } catch (JSONException e2) {
            throw new UnretriableException("error in getRequestDownloadParams():" + e2);
        }
    }

    public final String getHttpResopnse(String str, Map<String, String> map, boolean z) throws UnretriableException, RetriableException, AuthenticationException {
        try {
            return z ? Request.securePost(str, map) : Request.secureGet(str, map);
        } catch (CloudServerException e) {
            int statusCode = e.getStatusCode();
            if (statusCode == 401 || statusCode == 403) {
                throw new AuthenticationException();
            }
            if (statusCode == 500) {
                throw new RetriableException("IOException:" + e, 300000L);
            } else if (statusCode == 503) {
                throw new RetriableException("IOException:" + e, e.retryTime);
            } else {
                throw new UnretriableException("requestServer error:" + e);
            }
        } catch (UnsupportedEncodingException e2) {
            Log.e("MiCloudFileRequestor", "requestServer error: " + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e2)));
            throw new UnretriableException("requestServer error:" + e2);
        } catch (ClientProtocolException e3) {
            Log.e("MiCloudFileRequestor", "requestServer error: " + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e3)));
            throw new UnretriableException("requestServer error:" + e3);
        } catch (IOException e4) {
            Log.e("MiCloudFileRequestor", "requestServer error: " + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e4)));
            if (RetriableException.isRetriableException(e4)) {
                throw new RetriableException("IOException:" + e4, 300000L);
            }
            throw new UnretriableException("requestServer error:" + e4);
        } catch (BadPaddingException e5) {
            Log.e("MiCloudFileRequestor", "requestServer error: " + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e5)));
            throw new UnretriableException("requestServer error:" + e5);
        } catch (IllegalBlockSizeException e6) {
            Log.e("MiCloudFileRequestor", "requestServer error: " + PrivacyFilter.filterPrivacyLog(Log.getStackTraceString(e6)));
            throw new UnretriableException("requestServer error:" + e6);
        }
    }
}
