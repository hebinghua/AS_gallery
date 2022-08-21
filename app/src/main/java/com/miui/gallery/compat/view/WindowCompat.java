package com.miui.gallery.compat.view;

import android.os.Build;
import android.transition.Transition;
import android.view.Window;

/* loaded from: classes.dex */
public class WindowCompat {
    public static boolean requestActivityTransition(Window window) {
        return Build.VERSION.SDK_INT >= 21 && window.requestFeature(13);
    }

    public static boolean supportActivityTransitions(Window window) {
        return Build.VERSION.SDK_INT >= 21 && window.hasFeature(13);
    }

    public static void setSharedElementEnterTransition(Window window, Transition transition) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.setSharedElementEnterTransition(transition);
        }
    }

    public static void setSharedElementReturnTransition(Window window, Transition transition) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.setSharedElementReturnTransition(transition);
        }
    }

    public static void setNavigationBarColor(Window window, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.setNavigationBarColor(i);
        }
    }
}
