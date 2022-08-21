package com.miui.gallery.compat.interpolator;

import android.os.Build;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;

/* loaded from: classes.dex */
public class PathInterpolatorCompat {
    public static Interpolator getPathInterpolator(float f, float f2, float f3, float f4) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new PathInterpolator(f, f2, f3, f4);
        }
        return new LinearInterpolator();
    }
}
