package com.miui.gallery.compat.view;

import android.view.WindowInsets;

/* loaded from: classes.dex */
public class WindowInsetsCompat {
    public static int getSystemWindowInsetBottom(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetBottom();
    }

    public static int getSystemWindowInsetLeft(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetLeft();
    }

    public static int getSystemWindowInsetRight(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetRight();
    }

    public static int getSystemWindowInsetTop(Object obj) {
        return ((WindowInsets) obj).getSystemWindowInsetTop();
    }
}
