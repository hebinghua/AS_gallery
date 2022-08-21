package com.miui.gallery.util;

import android.content.Intent;

/* loaded from: classes2.dex */
public class SplitUtils {
    public static int getMiuiFlags(Intent intent) {
        Object invokeMethod = ReflectUtils.invokeMethod(intent, ReflectUtils.getMethod(intent.getClass().getName(), "getMiuiFlags"), new Object[0]);
        if (invokeMethod == null || !(invokeMethod instanceof Integer)) {
            return 0;
        }
        return ((Integer) invokeMethod).intValue();
    }

    public static void addMiuiFlags(Intent intent, int i) {
        ReflectUtils.invokeMethod(intent, ReflectUtils.getMethod(intent.getClass().getName(), "addMiuiFlags"), Integer.valueOf(i));
    }
}
