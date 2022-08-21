package com.android.internal;

import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import com.miui.internal.hidden.WindowInsetsReflect;

/* loaded from: classes.dex */
public class WindowInsetsCompat {
    public static boolean sNoMethodDetected;

    public static boolean shouldAlwaysConsumeSystemBars(View view) {
        WindowInsets rootWindowInsets;
        if (sNoMethodDetected) {
            return false;
        }
        try {
            int i = Build.VERSION.SDK_INT;
            if (i >= 29) {
                WindowInsets rootWindowInsets2 = view.getRootWindowInsets();
                if (rootWindowInsets2 != null) {
                    return WindowInsetsReflect.shouldAlwaysConsumeSystemBars(rootWindowInsets2);
                }
            } else if (i >= 24 && (rootWindowInsets = view.getRootWindowInsets()) != null) {
                return WindowInsetsReflect.shouldAlwaysConsumeNavBar(rootWindowInsets);
            }
        } catch (NoSuchMethodError e) {
            sNoMethodDetected = true;
            e.printStackTrace();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return false;
    }

    public static int getDisplayCutoutInsetsRight(View view) {
        WindowInsets rootWindowInsets;
        DisplayCutout displayCutout;
        if (Build.VERSION.SDK_INT < 28 || (rootWindowInsets = view.getRootWindowInsets()) == null || (displayCutout = rootWindowInsets.getDisplayCutout()) == null) {
            return 0;
        }
        return displayCutout.getSafeInsetRight();
    }
}
