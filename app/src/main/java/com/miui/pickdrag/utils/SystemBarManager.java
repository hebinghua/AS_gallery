package com.miui.pickdrag.utils;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes3.dex */
public class SystemBarManager {
    public static void setNavigationBar(Activity activity, boolean z, int i) {
        setLightNavigationBar(activity, z, true);
        setNavigationBarColor(activity, i, false);
    }

    public static void setLightNavigationBar(Activity activity, boolean z, boolean z2) {
        if (activity == null) {
            return;
        }
        if (z2) {
            checkNavigationBarTakeEffect(activity);
        }
        View decorView = activity.getWindow().getDecorView();
        int systemUiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(z ? systemUiVisibility | 16 : systemUiVisibility & (-17));
    }

    public static void setNavigationBarColor(Activity activity, int i, boolean z) {
        if (activity == null) {
            return;
        }
        if (z) {
            checkNavigationBarTakeEffect(activity);
        }
        activity.getWindow().setNavigationBarColor(i);
    }

    public static void checkNavigationBarTakeEffect(Activity activity) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        window.clearFlags(Box.MAX_BOX_SIZE);
        window.addFlags(Integer.MIN_VALUE);
    }
}
