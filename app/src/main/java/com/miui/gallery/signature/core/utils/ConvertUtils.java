package com.miui.gallery.signature.core.utils;

import android.content.Context;

/* loaded from: classes2.dex */
public class ConvertUtils {
    public static int dp2px(Context context, int i) {
        return dp2px(context, i);
    }

    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
