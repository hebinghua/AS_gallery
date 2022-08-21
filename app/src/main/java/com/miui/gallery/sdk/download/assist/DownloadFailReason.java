package com.miui.gallery.sdk.download.assist;

import android.util.Log;
import com.miui.gallery.error.core.ErrorCode;

/* loaded from: classes2.dex */
public class DownloadFailReason {
    public final Throwable mCause;
    public final ErrorCode mCode;
    public final String mDesc;

    public DownloadFailReason(ErrorCode errorCode, String str, Throwable th) {
        this.mCode = errorCode;
        this.mDesc = str;
        this.mCause = th;
    }

    public ErrorCode getCode() {
        return this.mCode;
    }

    public String getDesc() {
        return this.mDesc;
    }

    public Throwable getCause() {
        return this.mCause;
    }

    public String toString() {
        return "code: " + this.mCode + " desc: " + this.mDesc + " cause: " + Log.getStackTraceString(this.mCause);
    }
}
