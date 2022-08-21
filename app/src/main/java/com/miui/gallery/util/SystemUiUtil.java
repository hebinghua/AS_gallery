package com.miui.gallery.util;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class SystemUiUtil {
    public static String TAG = "SystemUiUtil";
    public static final String[] sWaterFallDevice = {"umi", "cmi", "cas", "venus", "star", "mars"};

    public static void showSystemBars(View view, boolean z) {
        showSystemBars(view, !BaseMiscUtil.isNightMode(view.getContext()), z);
    }

    public static void showSystemBars(View view, boolean z, boolean z2) {
        int i = Build.VERSION.SDK_INT;
        int i2 = (i < 23 || !z) ? 1792 : 9984;
        if (i >= 28 && z2) {
            i2 &= -5;
        }
        view.setSystemUiVisibility(i2);
    }

    public static void hideSystemBars(View view, boolean z) {
        hideSystemBars(view, !BaseMiscUtil.isNightMode(view.getContext()), z);
    }

    public static void hideSystemBars(View view, boolean z, boolean z2) {
        int i = Build.VERSION.SDK_INT;
        int i2 = (i < 23 || !z) ? 5894 : 14086;
        if (i >= 28 && z2) {
            i2 &= -5;
        }
        view.setSystemUiVisibility(i2);
    }

    public static void hideSystemBars(View view, boolean z, boolean z2, boolean z3) {
        int i = Build.VERSION.SDK_INT;
        int i2 = (i < 23 || !z) ? 5892 : 14084;
        if (i >= 28 && z2) {
            i2 &= -5;
        }
        if (z3) {
            i2 &= 2;
        }
        view.setSystemUiVisibility(i2);
    }

    public static void setLayoutFullScreen(View view, boolean z, boolean z2) {
        int i = !z ? 5894 : 1792;
        int i2 = Build.VERSION.SDK_INT;
        if (i2 > 22 && !BaseMiscUtil.isNightMode(view.getContext())) {
            i |= 8192;
        }
        if (i2 >= 28 && z2) {
            i &= -5;
        }
        view.setSystemUiVisibility(i);
    }

    public static void setDrawSystemBarBackground(Window window) {
        if (Build.VERSION.SDK_INT >= 23 && window != null) {
            window.addFlags(Integer.MIN_VALUE);
        }
    }

    public static void setSystemBarsVisibility(boolean z, View view, boolean z2) {
        boolean z3 = !BaseMiscUtil.isNightMode(view.getContext());
        if (z) {
            showSystemBars(view, z3, z2);
        } else {
            hideSystemBars(view, z3, z2);
        }
    }

    public static void extendToStatusBar(View view) {
        view.setSystemUiVisibility(1284);
    }

    public static boolean setRequestedOrientation(int i, Activity activity) {
        try {
            activity.setRequestedOrientation(i);
            return true;
        } catch (IllegalStateException unused) {
            DefaultLogger.d(TAG, "failed to setRequestedOrientation");
            return false;
        }
    }

    public static void setWindowFullScreenFlag(Activity activity) {
        if (activity == null) {
            return;
        }
        setWindowFullScreenFlag(activity.getWindow());
    }

    public static void setWindowFullScreenFlag(Window window) {
        try {
            window.setFlags(1024, 1024);
        } catch (Exception unused) {
            DefaultLogger.e(TAG, "failed to setWindowFullScreenFlag");
        }
    }

    public static void clearWindowFullScreenFlag(Activity activity) {
        if (activity == null) {
            return;
        }
        clearWindowFullScreenFlag(activity.getWindow());
    }

    public static void clearWindowFullScreenFlag(Window window) {
        try {
            window.clearFlags(1024);
        } catch (Exception unused) {
            DefaultLogger.e(TAG, "failed to clearWindowFullScreenFlag");
        }
    }

    public static void enableSeamlessRotation(Activity activity, boolean z) {
        if (activity != null && Build.VERSION.SDK_INT >= 28) {
            try {
                WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
                attributes.rotationAnimation = z ? 4 : 5;
                attributes.layoutInDisplayCutoutMode = 1;
                activity.getWindow().setFlags(1024, 1024);
                activity.getWindow().setAttributes(attributes);
            } catch (Exception e) {
                DefaultLogger.e(TAG, e);
            }
        }
    }

    public static void disableSeamlessRotation(Activity activity) {
        if (activity != null && Build.VERSION.SDK_INT >= 28) {
            try {
                WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
                attributes.rotationAnimation = 0;
                attributes.layoutInDisplayCutoutMode = 1;
                activity.getWindow().setFlags(1024, 1024);
                activity.getWindow().setAttributes(attributes);
            } catch (Exception e) {
                DefaultLogger.e(TAG, e);
            }
        }
    }

    public static boolean isWaterFallScreen() {
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : sWaterFallDevice) {
                if (str2.equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setTraditionNavigationBarIconMode(Activity activity, boolean z) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        try {
            int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            activity.getWindow().getDecorView().setSystemUiVisibility(z ? systemUiVisibility & (-17) : systemUiVisibility | 16);
        } catch (Exception e) {
            DefaultLogger.e(TAG, e);
        }
    }

    public static void setNavigationBarColor(Activity activity, int i, boolean z) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.getWindow().setNavigationBarColor(i);
            setTraditionNavigationBarIconMode(activity, z);
        }
    }
}
