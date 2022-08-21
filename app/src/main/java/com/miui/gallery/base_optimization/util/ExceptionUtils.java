package com.miui.gallery.base_optimization.util;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class ExceptionUtils {
    public static String getStackTraceString(Throwable th) {
        return TextUtils.join("\n\t", th.getStackTrace());
    }
}
