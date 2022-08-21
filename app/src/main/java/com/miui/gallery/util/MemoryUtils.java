package com.miui.gallery.util;

import android.app.ActivityManager;
import android.content.Context;

/* loaded from: classes2.dex */
public class MemoryUtils {
    public static float getCurrentUsableRam(Context context) {
        return (float) getMemoryInfo(context).availMem;
    }

    public static float getTotalRam(Context context) {
        return (float) getMemoryInfo(context).totalMem;
    }

    public static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo;
    }
}
