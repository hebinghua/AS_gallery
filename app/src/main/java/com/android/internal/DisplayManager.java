package com.android.internal;

import android.content.Context;
import android.util.Log;

/* loaded from: classes.dex */
public class DisplayManager {
    public static void setTemporaryAutoBrightnessAdjustment(Context context, float f) {
        try {
            ((android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class)).setTemporaryAutoBrightnessAdjustment(f);
        } catch (Throwable th) {
            Log.e("DisplayManager", "", th);
        }
    }
}
