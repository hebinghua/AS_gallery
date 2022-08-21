package com.miui.gallery.error.core;

import android.content.Context;

/* loaded from: classes2.dex */
public interface ErrorCodeTranslator {
    void translate(Context context, ErrorCode errorCode, String str, ErrorTranslateCallback errorTranslateCallback);
}
