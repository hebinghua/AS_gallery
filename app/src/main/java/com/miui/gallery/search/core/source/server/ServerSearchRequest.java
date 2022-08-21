package com.miui.gallery.search.core.source.server;

import android.os.SystemClock;
import android.text.TextUtils;
import com.google.gson.JsonObject;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.ResponseListener;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.MiscUtil;
import com.xiaomi.stat.b.h;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ServerSearchRequest extends BaseGalleryRequest {
    public boolean mReportError;
    public long mRequestCreateTime;
    public ResponseResult mResponseResult;

    public ServerSearchRequest(Builder builder) {
        super(builder.getMethod(), builder.getQueryPath());
        if (builder.getUserId() != null) {
            addParam("userId", builder.getUserId());
        }
        addParam("clientVersion", String.valueOf(MiscUtil.getAppVersionCode()));
        addParam("language", Locale.getDefault().toString());
        if (builder.getQueryParams() != null) {
            for (Map.Entry<String, String> entry : builder.getQueryParams().entrySet()) {
                addParam(entry.getKey(), entry.getValue());
            }
        }
        setOnResponseListener(builder.mResponseListener);
        setUseCache(builder.mUseCache);
        this.mReportError = builder.reportError();
        this.mResponseResult = new ResponseResult(builder.mResultDataType);
        this.mRequestCreateTime = SystemClock.elapsedRealtime();
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest
    public void onRequestSuccess(JSONObject jSONObject) throws Exception {
        super.onRequestSuccess(jSONObject);
        if (this.mRequestCreateTime > 0 && this.mReportError) {
            HttpUtils.reportTimeoutIfNeeded(this, SystemClock.elapsedRealtime() - this.mRequestCreateTime);
        }
        if (jSONObject != null) {
            this.mResponseResult.setData(jSONObject.toString());
            if (this.mResponseResult.getData() != null && (this.mResponseResult.getData() instanceof Cacheable)) {
                setCacheExpires(((Cacheable) this.mResponseResult.getData()).getExpireMills());
                SearchLog.d("ServerSearchRequest", "set expire time %dms for request %s", Long.valueOf(((Cacheable) this.mResponseResult.getData()).getExpireMills()), getUrl());
            }
            deliverResponse(this.mResponseResult.getData());
            return;
        }
        deliverError(ErrorCode.BODY_EMPTY, null, null);
    }

    @Override // com.miui.gallery.net.BaseGalleryRequest, com.miui.gallery.net.json.BaseJsonRequest, com.miui.gallery.net.base.BaseRequest, com.miui.gallery.net.base.ResponseErrorHandler
    public void onRequestError(ErrorCode errorCode, String str, Object obj) {
        deliverError(errorCode, str, obj);
    }

    @Override // com.miui.gallery.net.base.BaseRequest
    public void deliverResponse(Object... objArr) {
        super.deliverResponse(objArr);
    }

    @Override // com.miui.gallery.net.base.BaseRequest
    public void deliverError(ErrorCode errorCode, String str, Object obj) {
        if (this.mReportError) {
            HttpUtils.reportRequestError(this, errorCode, str, obj);
        }
        super.deliverError(errorCode, str, obj);
    }

    /* loaded from: classes2.dex */
    public static class ResponseResult {
        public Object mData;
        public Type mDataType;
        public String mRawData;

        public ResponseResult(Type type) {
            this.mDataType = type;
        }

        public void setData(String str) {
            if (this.mDataType != null) {
                this.mData = HttpUtils.createGson().fromJson(str, this.mDataType);
            } else {
                this.mData = str;
            }
            this.mRawData = str;
        }

        public String getRawData() {
            return this.mRawData;
        }

        public Object getData() {
            return this.mData;
        }
    }

    public Object getResponseData() {
        return this.mResponseResult.getData();
    }

    public String getResponseRawData() {
        return this.mResponseResult.getRawData();
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public String mQueryAppendPath;
        public Map<String, String> mQueryParams;
        public ResponseListener mResponseListener;
        public String mUserId;
        public String mUserPath;
        public String mQueryPathPrefix = HostManager.Search.getSearchUrlHost();
        public Type mResultDataType = JsonObject.class;
        public int mMethod = 1001;
        public boolean mUseCache = false;
        public boolean mReportError = true;

        public Map<String, String> getQueryParams() {
            return this.mQueryParams;
        }

        public String getQueryPath() {
            StringBuilder sb = new StringBuilder(this.mQueryPathPrefix);
            if (!TextUtils.isEmpty(this.mUserPath)) {
                sb.append(h.g);
                sb.append(this.mUserPath);
            }
            if (!TextUtils.isEmpty(this.mQueryAppendPath)) {
                sb.append(h.g);
                sb.append(this.mQueryAppendPath);
            }
            return sb.toString();
        }

        public Builder setQueryAppendPath(String str) {
            this.mQueryAppendPath = str;
            return this;
        }

        public Builder setUserPath(String str) {
            this.mUserPath = str;
            return this;
        }

        public Builder setUserId(String str) {
            this.mUserId = str;
            return this;
        }

        public String getUserId() {
            return this.mUserId;
        }

        public static String getDefaultUserPath(String str) {
            if (!TextUtils.isEmpty(str)) {
                return "user";
            }
            return null;
        }

        public Builder setQueryPathPrefix(String str) {
            this.mQueryPathPrefix = str;
            return this;
        }

        public Builder addQueryParams(Map<String, String> map) {
            if (map != null) {
                if (this.mQueryParams == null) {
                    this.mQueryParams = new HashMap();
                }
                this.mQueryParams.putAll(map);
            }
            return this;
        }

        public Builder addQueryParam(String str, String str2) {
            if (str != null) {
                if (this.mQueryParams == null) {
                    this.mQueryParams = new HashMap();
                }
                this.mQueryParams.put(str, str2);
            }
            return this;
        }

        public Builder setResultDataType(Type type) {
            this.mResultDataType = type;
            return this;
        }

        public Builder setMethod(int i) {
            this.mMethod = i;
            return this;
        }

        public int getMethod() {
            return this.mMethod;
        }

        public Builder setUseCache(boolean z) {
            this.mUseCache = z;
            return this;
        }

        public Builder setReportError(boolean z) {
            this.mReportError = z;
            return this;
        }

        public boolean reportError() {
            return this.mReportError;
        }

        public ServerSearchRequest build() {
            return new ServerSearchRequest(this);
        }
    }
}
