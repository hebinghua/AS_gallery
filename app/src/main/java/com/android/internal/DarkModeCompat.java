package com.android.internal;

import android.os.Build;
import android.webkit.WebSettings;

/* loaded from: classes.dex */
public class DarkModeCompat {
    public static void setForceDark(WebSettings webSettings, int i) {
        if (Build.VERSION.SDK_INT >= 29) {
            webSettings.setForceDark(i);
        }
    }
}
