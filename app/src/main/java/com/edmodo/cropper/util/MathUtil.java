package com.edmodo.cropper.util;

/* loaded from: classes.dex */
public class MathUtil {
    public static float calculateDistance(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        float f6 = f4 - f2;
        return (float) Math.sqrt((f5 * f5) + (f6 * f6));
    }
}
