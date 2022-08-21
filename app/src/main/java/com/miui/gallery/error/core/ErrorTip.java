package com.miui.gallery.error.core;

import android.content.Context;

/* loaded from: classes2.dex */
public abstract class ErrorTip {
    public final ErrorCode mCode;

    public abstract void action(Context context, ErrorActionCallback errorActionCallback);

    public abstract CharSequence getActionStr(Context context);

    public abstract CharSequence getMessage(Context context);

    public abstract CharSequence getTitle(Context context);

    public ErrorTip(ErrorCode errorCode) {
        this.mCode = errorCode;
    }

    public ErrorCode getCode() {
        return this.mCode;
    }
}
