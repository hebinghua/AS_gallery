package com.miui.gallery.net.base;

import com.android.volley.VolleyError;

/* loaded from: classes2.dex */
public class RequestError extends VolleyError {
    private ErrorCode mErrorCode;
    private Object mResponseData;

    public RequestError(ErrorCode errorCode, String str, Object obj) {
        super(str);
        this.mErrorCode = errorCode;
        this.mResponseData = obj;
    }

    public ErrorCode getErrorCode() {
        return this.mErrorCode;
    }

    public Object getResponseData() {
        return this.mResponseData;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        Object obj = this.mResponseData;
        if (obj instanceof Throwable) {
            return (Throwable) obj;
        }
        return super.getCause();
    }

    @Override // java.lang.Throwable
    public String toString() {
        return super.toString() + " code : " + this.mErrorCode;
    }
}
