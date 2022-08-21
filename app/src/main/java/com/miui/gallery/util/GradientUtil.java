package com.miui.gallery.util;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

/* loaded from: classes2.dex */
public class GradientUtil {
    public static Drawable makeCubicGradientScrimDrawable(int i, int i2, int i3) {
        final float f;
        final float f2;
        float f3;
        final float f4;
        int max = Math.max(i2, 2);
        PaintDrawable paintDrawable = new PaintDrawable();
        paintDrawable.setShape(new RectShape());
        final int[] iArr = new int[max];
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        int alpha = Color.alpha(i);
        int i4 = 0;
        while (true) {
            f = 0.0f;
            if (i4 >= max) {
                break;
            }
            iArr[i4] = Color.argb((int) (alpha * constrain(0.0f, 1.0f, (float) Math.pow((i4 * 1.0f) / (max - 1), 3.0d))), red, green, blue);
            i4++;
        }
        int i5 = i3 & 7;
        if (i5 == 3) {
            f2 = 1.0f;
            f3 = 0.0f;
        } else if (i5 != 5) {
            f3 = 0.0f;
            f2 = 0.0f;
        } else {
            f3 = 1.0f;
            f2 = 0.0f;
        }
        int i6 = i3 & 112;
        if (i6 == 48) {
            f4 = 1.0f;
        } else if (i6 != 80) {
            f4 = 0.0f;
        } else {
            f4 = 0.0f;
            f = 1.0f;
        }
        final float f5 = f3;
        paintDrawable.setShaderFactory(new ShapeDrawable.ShaderFactory() { // from class: com.miui.gallery.util.GradientUtil.1
            @Override // android.graphics.drawable.ShapeDrawable.ShaderFactory
            public Shader resize(int i7, int i8) {
                float f6 = i7;
                float f7 = i8;
                return new LinearGradient(f6 * f2, f7 * f4, f6 * f5, f7 * f, iArr, (float[]) null, Shader.TileMode.CLAMP);
            }
        });
        return paintDrawable;
    }

    public static float constrain(float f, float f2, float f3) {
        return Math.max(f, Math.min(f2, f3));
    }
}
