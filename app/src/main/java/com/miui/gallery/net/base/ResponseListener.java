package com.miui.gallery.net.base;

/* loaded from: classes2.dex */
public interface ResponseListener {
    void onResponse(Object... objArr);

    void onResponseError(ErrorCode errorCode, String str, Object obj);
}
