package com.xiaomi.milab.videosdk.utils;

import android.util.DisplayMetrics;
import com.xiaomi.milab.videosdk.XmsContext;

/* loaded from: classes3.dex */
public class ScreenUtils {
    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = XmsContext.getInstance().getContext().getResources().getDisplayMetrics();
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = XmsContext.getInstance().getContext().getResources().getDisplayMetrics();
        return Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }
}
