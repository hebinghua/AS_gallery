package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.SyncLogger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class RequestOperationBase implements Operation<JSONObject> {
    public String TAG;
    public Context mContext;

    public abstract Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception;

    public SpaceFullHandler.SpaceFullListener getSpaceFullListener() {
        return null;
    }

    public abstract void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception;

    public abstract void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception;

    public RequestOperationBase(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.gallery.cloud.Operation
    public final GallerySyncResult<JSONObject> execute(Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, String str, RequestItemBase requestItemBase) throws Exception {
        long currentTimeMillis = System.currentTimeMillis();
        GallerySyncResult.Builder builder = new GallerySyncResult.Builder();
        if (checkState(requestItemBase) != 0) {
            SyncLogger.e(getTag(), "condition for %s isn't ok", getTag());
            return builder.setCode(GallerySyncCode.CONDITION_INTERRUPTED).build();
        }
        GallerySyncCode onPreRequest = onPreRequest(requestItemBase);
        GallerySyncCode gallerySyncCode = GallerySyncCode.OK;
        if (onPreRequest != gallerySyncCode) {
            SyncLogger.e(getTag(), "onPreExecute error %s", onPreRequest);
            return builder.setCode(onPreRequest).build();
        }
        Request buildRequest = buildRequest(account, requestItemBase);
        if (buildRequest == null || !buildRequest.isValid()) {
            SyncLogger.e(getTag(), "illegal request %s", buildRequest);
            return builder.setCode(GallerySyncCode.NOT_RETRY_ERROR).build();
        }
        JSONObject jSONObject = null;
        if (buildRequest.getMethod() == 1) {
            jSONObject = CloudUtils.getFromXiaomi(buildRequest.getUrl(), buildRequest.getParams(), account, galleryExtendedAuthToken, buildRequest.getRetryTimes(), buildRequest.isNeedReRequest());
        }
        if (buildRequest.getMethod() == 2) {
            jSONObject = CloudUtils.postToXiaomi(buildRequest.getUrl(), buildRequest.getParams(), buildRequest.getPostData(), account, galleryExtendedAuthToken, buildRequest.getRetryTimes(), buildRequest.isNeedReRequest());
        }
        if (jSONObject == null) {
            SyncLogger.e(getTag(), "response null");
            return builder.setCode(GallerySyncCode.NOT_RETRY_ERROR).build();
        }
        GallerySyncResult<JSONObject> checkXMResultCode = CheckResult.checkXMResultCode(jSONObject, requestItemBase, getSpaceFullListener());
        GallerySyncCode gallerySyncCode2 = checkXMResultCode.code;
        if (gallerySyncCode2 != gallerySyncCode) {
            fireFailEvent(gallerySyncCode2, requestItemBase, jSONObject, checkXMResultCode);
            SyncLogger.e(getTag(), "error result %s, retry times %s", checkXMResultCode, Integer.valueOf(requestItemBase.otherRetryTimes));
            return builder.setCode(checkXMResultCode.code).setData(jSONObject).build();
        }
        JSONObject optJSONObject = jSONObject.optJSONObject("data");
        if (optJSONObject == null) {
            SyncLogger.e(getTag(), "response has no data %s", optJSONObject);
            GallerySyncCode gallerySyncCode3 = GallerySyncCode.NOT_RETRY_ERROR;
            fireFailEvent(gallerySyncCode3, requestItemBase, jSONObject, checkXMResultCode);
            return builder.setCode(gallerySyncCode3).build();
        }
        onRequestSuccess(requestItemBase, optJSONObject);
        SyncLogger.d(getTag(), "execute success, cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return builder.setCode(gallerySyncCode).setData(optJSONObject).build();
    }

    public String getTag() {
        if (this.TAG == null) {
            this.TAG = getClass().getSimpleName();
        }
        return this.TAG;
    }

    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        return GallerySyncCode.OK;
    }

    public final void fireFailEvent(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject, GallerySyncResult gallerySyncResult) throws Exception {
        onRequestError(gallerySyncCode, requestItemBase, jSONObject);
        HashMap hashMap = new HashMap();
        hashMap.put("name", getClass().getSimpleName());
        hashMap.put("result", gallerySyncResult == null ? "null" : gallerySyncResult.toString());
        SamplingStatHelper.recordCountEvent("Sync", "sync_error_class", hashMap);
    }

    public int checkState(RequestItemBase requestItemBase) {
        return SyncConditionManager.checkForItem(requestItemBase);
    }

    /* loaded from: classes.dex */
    public static final class Request {
        public final int mMethod;
        public final boolean mNeedReRequest;
        public final List<NameValuePair> mParams;
        public final JSONObject mPostData;
        public final int mRetryTimes;
        public final String mUrl;

        public Request(Builder builder) {
            this.mMethod = builder.mMethod;
            this.mUrl = builder.mUrl;
            this.mParams = builder.mParams;
            this.mPostData = builder.mPostData;
            this.mRetryTimes = builder.mRetryTimes;
            this.mNeedReRequest = builder.mNeedReRequest;
        }

        public int getMethod() {
            return this.mMethod;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public List<NameValuePair> getParams() {
            return this.mParams;
        }

        public JSONObject getPostData() {
            return this.mPostData;
        }

        public int getRetryTimes() {
            return this.mRetryTimes;
        }

        public boolean isNeedReRequest() {
            return this.mNeedReRequest;
        }

        public final boolean isValid() {
            int i = this.mMethod;
            return (i == 1 || i == 2) && !TextUtils.isEmpty(this.mUrl);
        }

        public String toString() {
            return String.format(Locale.US, "mMethod %s, mUrl %s", Integer.valueOf(this.mMethod), this.mUrl);
        }

        /* loaded from: classes.dex */
        public static final class Builder {
            public int mMethod;
            public boolean mNeedReRequest = false;
            public List<NameValuePair> mParams;
            public JSONObject mPostData;
            public int mRetryTimes;
            public String mUrl;

            public Builder setMethod(int i) {
                this.mMethod = i;
                return this;
            }

            public Builder setUrl(String str) {
                this.mUrl = str;
                return this;
            }

            public Builder setParams(List<NameValuePair> list) {
                this.mParams = list;
                return this;
            }

            public Builder setPostData(JSONObject jSONObject) {
                this.mPostData = jSONObject;
                return this;
            }

            public Builder setRetryTimes(int i) {
                this.mRetryTimes = i;
                return this;
            }

            public Builder setNeedReRequest(boolean z) {
                this.mNeedReRequest = z;
                return this;
            }

            public Request build() {
                return new Request(this);
            }
        }
    }
}
