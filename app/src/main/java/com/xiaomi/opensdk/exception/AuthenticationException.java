package com.xiaomi.opensdk.exception;

import android.text.TextUtils;

/* loaded from: classes3.dex */
public class AuthenticationException extends Exception {
    private static final long serialVersionUID = 1;

    @Override // java.lang.Throwable
    public String toString() {
        if (!TextUtils.isEmpty(getMessage())) {
            return getClass().getSimpleName() + ": 鉴权失败, " + getMessage();
        }
        return getClass().getSimpleName() + ": 鉴权失败";
    }
}
