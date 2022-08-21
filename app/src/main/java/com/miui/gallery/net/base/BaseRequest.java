package com.miui.gallery.net.base;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class BaseRequest<T> implements ResponseErrorHandler {
    public static ResponseErrorHandler mAppResponseErrorHandler;
    public Map<String, String> mParams;
    public RequestError mRequestError;
    public Object[] mResponse;
    public ResponseDispatcher mListenerDispatcher = null;
    public Object mTag = null;
    public boolean mUseCache = false;
    public long mCacheExpires = -1;
    public long mCacheSoftTtl = -1;

    public abstract void execute();

    public abstract Object[] executeSync() throws RequestError;

    @Override // com.miui.gallery.net.base.ResponseErrorHandler
    public abstract void onRequestError(ErrorCode errorCode, String str, Object obj);

    public final BaseRequest<T> setOnResponseListener(ResponseListener responseListener) {
        this.mListenerDispatcher = new ResponseDispatcher(responseListener);
        return this;
    }

    public void deliverResponse(Object... objArr) {
        this.mResponse = objArr;
        ResponseDispatcher responseDispatcher = this.mListenerDispatcher;
        if (responseDispatcher != null) {
            responseDispatcher.onResponse(objArr);
        }
    }

    public void deliverError(ErrorCode errorCode, String str, Object obj) {
        this.mRequestError = new RequestError(errorCode, str, obj);
        ResponseDispatcher responseDispatcher = this.mListenerDispatcher;
        if (responseDispatcher != null) {
            responseDispatcher.onResponseError(errorCode, str, obj);
        }
        ResponseErrorHandler responseErrorHandler = mAppResponseErrorHandler;
        if (responseErrorHandler != null) {
            responseErrorHandler.onRequestError(errorCode, str, obj);
        }
    }

    public final BaseRequest<T> addParam(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return this;
        }
        if (this.mParams == null) {
            this.mParams = new HashMap();
        }
        if (str2 == null) {
            str2 = "";
        }
        this.mParams.put(str, str2);
        return this;
    }

    public final BaseRequest<T> setUseCache(boolean z) {
        this.mUseCache = z;
        return this;
    }

    public final boolean isUseCache() {
        return this.mUseCache;
    }

    public final BaseRequest<T> setCacheExpires(long j) {
        this.mCacheExpires = j;
        return this;
    }

    public final BaseRequest<T> setCacheSoftTtl(long j) {
        this.mCacheSoftTtl = j;
        return this;
    }

    public final <T> T simpleExecuteSync() throws RequestError {
        Object[] executeSync = executeSync();
        if (executeSync == null || executeSync.length <= 0) {
            return null;
        }
        return (T) executeSync[0];
    }

    public final void execute(ResponseListener responseListener) {
        setOnResponseListener(responseListener);
        execute();
    }

    public Map<String, String> getParams() {
        return this.mParams;
    }

    public BaseRequest<T> setTag(Object obj) {
        this.mTag = obj;
        return this;
    }

    public Object getTag() {
        return this.mTag;
    }
}
