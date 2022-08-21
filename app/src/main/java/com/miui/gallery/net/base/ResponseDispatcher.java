package com.miui.gallery.net.base;

import android.os.Handler;
import android.os.Looper;

/* loaded from: classes2.dex */
public class ResponseDispatcher {
    public Handler mHandler;
    public ResponseListener mOnResponseListener;

    public ResponseDispatcher(ResponseListener responseListener) {
        this.mHandler = null;
        this.mOnResponseListener = responseListener;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public void onResponse(Object... objArr) {
        ResponseListener responseListener = this.mOnResponseListener;
        if (responseListener != null) {
            try {
                responseListener.onResponse(objArr);
            } catch (Exception unused) {
                onResponseError(ErrorCode.DATA_BIND_ERROR, null, null);
            }
        }
    }

    public void onResponseError(ErrorCode errorCode, String str, Object obj) {
        ResponseListener responseListener = this.mOnResponseListener;
        if (responseListener != null) {
            responseListener.onResponseError(errorCode, str, obj);
        }
    }
}
