package com.miui.gallery.util;

/* loaded from: classes2.dex */
public class ClickUtils {
    public static long sLastClickTime;

    public static synchronized boolean isDoubleClick() {
        boolean isDoubleClick;
        synchronized (ClickUtils.class) {
            isDoubleClick = isDoubleClick(400L);
        }
        return isDoubleClick;
    }

    public static synchronized boolean isDoubleClick(long j) {
        boolean z;
        synchronized (ClickUtils.class) {
            long currentTimeMillis = System.currentTimeMillis();
            z = currentTimeMillis - sLastClickTime <= j;
            sLastClickTime = currentTimeMillis;
        }
        return z;
    }
}
