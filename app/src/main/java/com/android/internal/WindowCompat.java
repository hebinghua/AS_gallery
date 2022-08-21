package com.android.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.nexstreaming.nexeditorsdk.nexEngine;

/* loaded from: classes.dex */
public class WindowCompat {
    public static int DEFAULT_NOTCH_HEIGHT = 80;
    public static String FORCE_BLACK = "force_black_";
    public static String FORCE_BLACK_V2 = "force_black_v2";
    public static Boolean sIsNotch;
    public static Integer sTopNotchHeight;

    public static void setCutoutModeShortEdges(Window window) {
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = 1;
            window.setAttributes(attributes);
        }
    }

    public static void setCutoutDefaultMode(Window window) {
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.layoutInDisplayCutoutMode = 0;
            window.setAttributes(attributes);
        }
    }

    public static int getTopNotchHeight(Activity activity) {
        WindowInsets rootWindowInsets;
        Integer num = sTopNotchHeight;
        if (num == null) {
            if (!isNotch(activity)) {
                return 0;
            }
            if (Build.VERSION.SDK_INT >= 28) {
                View decorView = activity.getWindow().getDecorView();
                if (decorView == null || (rootWindowInsets = decorView.getRootWindowInsets()) == null) {
                    return 0;
                }
                DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
                if (displayCutout == null) {
                    sTopNotchHeight = Integer.valueOf(getTopNotchHeightSDKVersionBelow28(activity));
                } else {
                    sTopNotchHeight = Integer.valueOf(displayCutout.getSafeInsetTop());
                }
                return sTopNotchHeight.intValue();
            }
            return getTopNotchHeightSDKVersionBelow28(activity);
        }
        return num.intValue();
    }

    public static int getTopNotchHeightSDKVersionBelow28(Activity activity) {
        if (activity == null) {
            return 0;
        }
        int identifier = activity.getResources().getIdentifier("notch_height", "dimen", "android");
        if (identifier > 0) {
            return activity.getResources().getDimensionPixelSize(identifier);
        }
        return DEFAULT_NOTCH_HEIGHT;
    }

    public static boolean isNotch(Activity activity) {
        View decorView;
        WindowInsets rootWindowInsets;
        Boolean bool = sIsNotch;
        if (bool == null) {
            boolean z = true;
            if (Build.VERSION.SDK_INT >= 28) {
                if (activity == null || activity.isDestroyed() || (decorView = activity.getWindow().getDecorView()) == null || (rootWindowInsets = decorView.getRootWindowInsets()) == null) {
                    return false;
                }
                if (rootWindowInsets.getDisplayCutout() == null && SystemPropertiesCompat.getInt("ro.miui.notch", 0) != 1) {
                    z = false;
                }
                Boolean valueOf = Boolean.valueOf(z);
                sIsNotch = valueOf;
                return valueOf.booleanValue();
            }
            if (SystemPropertiesCompat.getInt("ro.miui.notch", 0) != 1) {
                z = false;
            }
            Boolean valueOf2 = Boolean.valueOf(z);
            sIsNotch = valueOf2;
            return valueOf2.booleanValue();
        }
        return bool.booleanValue();
    }

    public static void setShowWhenLocked(Activity activity, boolean z) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 27) {
            activity.setShowWhenLocked(z);
        } else if (z) {
            activity.getWindow().addFlags(nexEngine.ExportHEVCHighTierLevel52);
        } else {
            activity.getWindow().clearFlags(nexEngine.ExportHEVCHighTierLevel52);
        }
    }

    public static boolean isHiddenNotch(Context context) {
        if (context == null) {
            return false;
        }
        return Settings.Global.getInt(context.getContentResolver(), Build.VERSION.SDK_INT >= 28 ? FORCE_BLACK_V2 : FORCE_BLACK, 0) == 1;
    }
}
