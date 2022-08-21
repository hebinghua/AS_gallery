package com.miui.internal;

import android.os.Build;
import android.util.Log;
import miui.hardware.display.DisplayFeatureManager;

/* loaded from: classes3.dex */
public class GalleryDisplayFeatureManager {
    public static void setScreenEffect(boolean z) {
        if (isDeviceSupportSetEffect()) {
            try {
                DisplayFeatureManager.getInstance().setScreenEffect(14, z ? 1 : 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setScreenSceneClassification(int i) {
        try {
            Log.d("GalleryDisplayFeatureManager", String.format("classify setScreenSceneClassification %s", Integer.valueOf(i)));
            DisplayFeatureManager.getInstance().setScreenEffect(36, i);
        } catch (Exception e) {
            Log.d("GalleryDisplayFeatureManager", String.format("gdfm catch exception for %s", Integer.valueOf(i)));
            e.printStackTrace();
        }
    }

    public static boolean isDeviceSupportSetEffect() {
        return Build.DEVICE.equalsIgnoreCase("vela") && Build.VERSION.SDK_INT >= 24;
    }
}
