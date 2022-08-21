package com.miui.gallery.util;

import android.content.Context;
import android.os.Build;
import com.android.internal.DisplayManager;

/* loaded from: classes2.dex */
public class BrightnessUtils {
    public static void setTemporaryAutoBrightness(Context context, float f) {
        if (Build.VERSION.SDK_INT >= 28) {
            DisplayManager.setTemporaryAutoBrightnessAdjustment(context, f);
        } else {
            BaseMiscUtil.invokeSafely(context.getSystemService("power"), "setTemporaryAutoBrightnessAdjustmentRatio", new Class[]{Float.TYPE}, Float.valueOf(f));
        }
    }
}
