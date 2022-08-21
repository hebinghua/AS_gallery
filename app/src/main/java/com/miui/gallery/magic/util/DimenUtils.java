package com.miui.gallery.magic.util;

import android.content.Context;
import android.util.TypedValue;

/* loaded from: classes2.dex */
public class DimenUtils {
    public static float dp2px(int i, Context context) {
        return TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
    }
}
