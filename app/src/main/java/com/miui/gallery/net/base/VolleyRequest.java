package com.miui.gallery.net.base;

import android.os.Looper;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miui.gallery.net.HttpManager;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes2.dex */
public abstract class VolleyRequest<T, E> extends BaseRequest<E> implements Response.Listener<T>, Response.ErrorListener {
    public Request<T> mRequest;
    public CountDownLatch mSyncExecuteLock = null;

    public abstract Request<T> createVolleyRequest(Response.Listener<T> listener, Response.ErrorListener errorListener);

    public abstract void handleResponse(T t);

    @Override // com.miui.gallery.net.base.BaseRequest
    public void execute() {
        Request<T> createVolleyRequest = createVolleyRequest(this, this);
        this.mRequest = createVolleyRequest;
        if (createVolleyRequest == null) {
            return;
        }
        createVolleyRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        this.mRequest.setShouldCache(isUseCache());
        Object tag = getTag();
        if (this.mRequest.getTag() == null && tag != null) {
            this.mRequest.setTag(tag);
        }
        HttpManager.getInstance().addToRequestQueue(this.mRequest);
    }

    @Override // com.miui.gallery.net.base.BaseRequest
    public Object[] executeSync() throws RequestError {
        if (Looper.myLooper() == Looper.getMainLooper() || Looper.myLooper() == ThreadManager.getNetworkRequestLooper()) {
            throw new RuntimeException("executeSync could not call on main thread or request thread.");
        }
        this.mSyncExecuteLock = new CountDownLatch(1);
        execute();
        try {
            CountDownLatch countDownLatch = this.mSyncExecuteLock;
            if (countDownLatch != null) {
                countDownLatch.await();
            }
        } catch (InterruptedException unused) {
        }
        RequestError requestError = this.mRequestError;
        if (requestError != null) {
            throw requestError;
        }
        return this.mResponse;
    }

    public final void releaseSyncExecuteLock() {
        CountDownLatch countDownLatch = this.mSyncExecuteLock;
        if (countDownLatch != null) {
            countDownLatch.countDown();
            this.mSyncExecuteLock = null;
        }
    }

    public final void cancel() {
        releaseSyncExecuteLock();
        setOnResponseListener(null);
        Request<T> request = this.mRequest;
        if (request != null) {
            request.cancel();
        }
    }

    @Override // com.android.volley.Response.Listener
    public final void onResponse(T t) {
        handleResponse(t);
        releaseSyncExecuteLock();
        Request<T> request = this.mRequest;
        if (request instanceof Cacheable) {
            Cacheable cacheable = (Cacheable) request;
            if (this.mCacheExpires <= 0 || cacheable.isFromCache()) {
                return;
            }
            HttpManager.getInstance().putToCache(this.mRequest.getCacheKey(), cacheable.getData(), this.mCacheExpires, this.mCacheSoftTtl);
        }
    }

    @Override // com.android.volley.Response.ErrorListener
    public final void onErrorResponse(VolleyError volleyError) {
        ErrorCode errorCode = ErrorCode.NET_ERROR;
        if (volleyError instanceof RequestError) {
            errorCode = ((RequestError) volleyError).getErrorCode();
        }
        Throwable cause = volleyError.getCause();
        if (cause != null) {
            volleyError = cause;
        }
        handleError(errorCode, volleyError.getMessage(), volleyError);
        releaseSyncExecuteLock();
    }

    public final void handleError(ErrorCode errorCode, String str, Object obj) {
        this.mCacheExpires = -1L;
        onRequestError(errorCode, str, obj);
    }
}
