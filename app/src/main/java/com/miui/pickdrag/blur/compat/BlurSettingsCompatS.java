package com.miui.pickdrag.blur.compat;

import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Method;

/* loaded from: classes3.dex */
public class BlurSettingsCompatS {
    public static final String TAG = "BlurSettingsCompatS";

    public static boolean setBlurRadius(Window window, float f) {
        if (window == null) {
            return false;
        }
        try {
            WindowManager.LayoutParams attributes = window.getAttributes();
            if (f <= 0.0f) {
                attributes.flags &= -5;
            } else {
                attributes.flags |= 4;
            }
            Method declaredMethod = attributes.getClass().getDeclaredMethod("setBlurBehindRadius", Integer.TYPE);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(attributes, Integer.valueOf((int) (100.0f * f)));
            window.setAttributes(attributes);
            return f > 0.0f;
        } catch (Exception e) {
            Log.e(TAG, "setBlurRadius", e);
            return false;
        }
    }
}
