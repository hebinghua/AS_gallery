package com.miui.gallery;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class BaseConfig$ScreenConfig {
    public static int sHeightPixels;
    public static int sScreenWidthInDip;
    public static int sWidthPixels;

    public static void init() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) StaticContext.sGetAndroidContext().getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        int i = displayMetrics.widthPixels;
        sWidthPixels = i;
        int i2 = displayMetrics.heightPixels;
        sHeightPixels = i2;
        sScreenWidthInDip = (int) (Math.min(i2, i) / displayMetrics.density);
        DefaultLogger.i("ScreenConfig", "screenWidth: %d, screenHeight: %d, density->%s", Integer.valueOf(sWidthPixels), Integer.valueOf(sHeightPixels), Float.valueOf(displayMetrics.density));
    }

    public static int getScreenWidth() {
        if (sWidthPixels == 0) {
            init();
        }
        return sWidthPixels;
    }

    public static int getScreenHeight() {
        if (sHeightPixels == 0) {
            init();
        }
        return sHeightPixels;
    }

    public static int getScreenWidthInDip() {
        if (sScreenWidthInDip == 0) {
            init();
        }
        return sScreenWidthInDip;
    }
}
