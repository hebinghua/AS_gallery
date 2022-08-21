package com.miui.pickdrag.blur.compat;

import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;

/* loaded from: classes3.dex */
public class BlurSettingsCompatBelowS {
    public static final String TAG = "BlurSettingsCompatBelowS";

    public static final boolean setBlurRatio(Window window, float f) {
        try {
            WindowManager.LayoutParams reflectSetWindowAttributes = reflectSetWindowAttributes(window, "blurRatio", Float.valueOf(f));
            if (reflectSetWindowAttributes == null) {
                return false;
            }
            if (f <= 0.0f) {
                reflectSetWindowAttributes.flags &= -5;
            } else {
                reflectSetWindowAttributes.flags |= 4;
            }
            window.setAttributes(reflectSetWindowAttributes);
            return f > 0.0f;
        } catch (Exception e) {
            Log.w(TAG, "setBlurRatio failed", e);
            return false;
        }
    }

    public static final WindowManager.LayoutParams reflectSetWindowAttributes(Window window, String str, Object obj) {
        if (window != null && window.getAttributes() != null && !TextUtils.isEmpty(str)) {
            try {
                WindowManager.LayoutParams attributes = window.getAttributes();
                Field declaredField = attributes.getClass().getDeclaredField(str);
                declaredField.setAccessible(true);
                declaredField.set(attributes, obj);
                return attributes;
            } catch (Exception e) {
                String str2 = TAG;
                Log.w(str2, "reflectSetWindowAttributes failed, field: " + str + ", value: " + obj, e);
            }
        }
        return null;
    }
}
