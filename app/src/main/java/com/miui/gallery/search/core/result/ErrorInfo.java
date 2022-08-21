package com.miui.gallery.search.core.result;

/* loaded from: classes2.dex */
public class ErrorInfo {
    public Object mErrorData;
    public int mErrorStatus;

    public ErrorInfo(int i) {
        this(i, null);
    }

    public ErrorInfo(int i, Object obj) {
        this.mErrorStatus = -1;
        this.mErrorStatus = i;
        this.mErrorData = obj;
    }

    public int getErrorStatus() {
        return this.mErrorStatus;
    }

    public String toString() {
        return super.toString() + " code : " + this.mErrorStatus + ", message : " + this.mErrorData;
    }
}
