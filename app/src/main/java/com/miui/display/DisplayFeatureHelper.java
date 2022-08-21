package com.miui.display;

import com.miui.core.SdkHelper;
import com.miui.internal.GalleryDisplayFeatureManager;

/* loaded from: classes.dex */
public class DisplayFeatureHelper {
    public static void setScreenEffect(boolean z) {
        if (SdkHelper.IS_MIUI) {
            GalleryDisplayFeatureManager.setScreenEffect(z);
        }
    }

    public static void setScreenSceneClassification(int i) {
        if (SdkHelper.IS_MIUI) {
            GalleryDisplayFeatureManager.setScreenSceneClassification(i);
        }
    }
}
