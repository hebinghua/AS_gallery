package com.miui.gallery.cloud.card.network;

import com.miui.gallery.net.base.ErrorCode;

/* loaded from: classes.dex */
public interface ResponseCallback<T> {
    void onResponse(T t);

    void onResponseError(ErrorCode errorCode, String str, Object obj);
}
