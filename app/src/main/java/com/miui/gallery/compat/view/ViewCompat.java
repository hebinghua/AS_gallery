package com.miui.gallery.compat.view;

import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;

/* loaded from: classes.dex */
public class ViewCompat {
    public static void setTransitionName(View view, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            view.setTransitionName(view.getResources().getString(i));
        }
    }

    public static Object getRootWindowInsets(View view) {
        WindowInsets rootWindowInsets;
        if (view == null || Build.VERSION.SDK_INT < 23 || (rootWindowInsets = view.getRootWindowInsets()) == null) {
            return null;
        }
        Log.d("ViewCompat", "root window insets: " + rootWindowInsets);
        return rootWindowInsets;
    }

    public static int getSystemWindowInsetLeft(View view) {
        Object rootWindowInsets = getRootWindowInsets(view);
        if (rootWindowInsets != null) {
            return WindowInsetsCompat.getSystemWindowInsetLeft(rootWindowInsets);
        }
        return 0;
    }

    public static int getSystemWindowInsetTop(View view) {
        Object rootWindowInsets = getRootWindowInsets(view);
        if (rootWindowInsets != null) {
            return WindowInsetsCompat.getSystemWindowInsetTop(rootWindowInsets);
        }
        return 0;
    }

    public static int getSystemWindowInsetRight(View view) {
        Object rootWindowInsets = getRootWindowInsets(view);
        if (rootWindowInsets != null) {
            return WindowInsetsCompat.getSystemWindowInsetRight(rootWindowInsets);
        }
        return 0;
    }

    public static int getSystemWindowInsetBottom(View view) {
        Object rootWindowInsets = getRootWindowInsets(view);
        if (rootWindowInsets != null) {
            return WindowInsetsCompat.getSystemWindowInsetBottom(rootWindowInsets);
        }
        return 0;
    }

    public static Rect getSystemWindowInsets(View view) {
        Rect rect = new Rect(0, 0, 0, 0);
        Object rootWindowInsets = getRootWindowInsets(view);
        if (rootWindowInsets != null) {
            rect.set(WindowInsetsCompat.getSystemWindowInsetLeft(rootWindowInsets), WindowInsetsCompat.getSystemWindowInsetTop(rootWindowInsets), WindowInsetsCompat.getSystemWindowInsetRight(rootWindowInsets), WindowInsetsCompat.getSystemWindowInsetBottom(rootWindowInsets));
        }
        return rect;
    }
}
