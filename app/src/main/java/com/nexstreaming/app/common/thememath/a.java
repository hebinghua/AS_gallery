package com.nexstreaming.app.common.thememath;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* compiled from: NexTheme_Math.java */
/* loaded from: classes3.dex */
public class a {
    public static C0102a a = new C0102a(1.164d, (double) SearchStatUtils.POW, 1.596d, -0.871d, 1.164d, -0.392d, -0.813d, 0.53d, 1.164d, 2.017d, (double) SearchStatUtils.POW, -1.081d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
    public static C0102a b = new C0102a(1.164d, (double) SearchStatUtils.POW, 1.596d, (double) SearchStatUtils.POW, 1.164d, -0.392d, -0.813d, (double) SearchStatUtils.POW, 1.164d, 2.017d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
    public static C0102a c = new C0102a(0.257d, 0.504d, 0.098d, (double) SearchStatUtils.POW, -0.148d, -0.291d, 0.439d, (double) SearchStatUtils.POW, 0.439d, -0.368d, -0.071d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
    public static C0102a d = new C0102a(0.257d, 0.504d, 0.098d, 0.0625d, -0.148d, -0.291d, 0.439d, 0.5d, 0.439d, -0.368d, -0.071d, 0.5d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
    public static C0102a e = new C0102a(1.0d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);

    /* compiled from: NexTheme_Math.java */
    /* renamed from: com.nexstreaming.app.common.thememath.a$a  reason: collision with other inner class name */
    /* loaded from: classes3.dex */
    public static class C0102a {
        public float[] a;

        public C0102a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16) {
            this.a = new float[]{f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16};
        }

        public C0102a(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15, double d16) {
            this.a = new float[]{(float) d, (float) d2, (float) d3, (float) d4, (float) d5, (float) d6, (float) d7, (float) d8, (float) d9, (float) d10, (float) d11, (float) d12, (float) d13, (float) d14, (float) d15, (float) d16};
        }
    }

    /* compiled from: NexTheme_Math.java */
    /* loaded from: classes3.dex */
    public static class b {
        public float[] a;

        public b(float f, float f2, float f3, float f4) {
            this.a = new float[]{f, f2, f3, f4};
        }

        public b(double d, double d2, double d3, double d4) {
            this.a = new float[]{(float) d, (float) d2, (float) d3, (float) d4};
        }
    }

    public static C0102a a() {
        return new C0102a(1.0d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
    }

    public static C0102a a(float f, float f2, float f3) {
        return new C0102a(f, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, f2, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, f3, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
    }

    public static C0102a a(b bVar) {
        float[] fArr = bVar.a;
        return new C0102a(1.0d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, fArr[0], (double) SearchStatUtils.POW, 1.0d, (double) SearchStatUtils.POW, fArr[1], (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d, fArr[2], (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
    }

    public static C0102a a(float f, float f2, float f3, b bVar, boolean z) {
        float f4;
        C0102a c0102a;
        C0102a a2;
        C0102a a3;
        float f5 = f;
        double d2 = f5;
        if (d2 < -1.0d) {
            f4 = f2;
            f5 = -1.0f;
        } else if (d2 > 1.0d) {
            f4 = f2;
            f5 = 1.0f;
        } else {
            f4 = f2;
        }
        double d3 = f4;
        if (d3 < -1.0d) {
            f4 = -1.0f;
        } else if (d3 > 1.0d) {
            f4 = 1.0f;
        }
        if (z) {
            c0102a = new C0102a(1.0d, (double) SearchStatUtils.POW, 1.402d, (double) SearchStatUtils.POW, 1.0d, -0.344d, -0.714d, (double) SearchStatUtils.POW, 1.0d, 1.772d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
        } else {
            c0102a = new C0102a(1.164d, (double) SearchStatUtils.POW, 1.596d, (double) SearchStatUtils.POW, 1.164d, -0.392d, -0.813d, (double) SearchStatUtils.POW, 1.164d, 2.017d, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
        }
        C0102a a4 = a(new b(f5, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW));
        float f6 = f4 + 1.0f;
        C0102a a5 = a(a(a(new b(0.5d, 0.5d, 0.5d, (double) SearchStatUtils.POW)), a(f6, f6, f6)), a(new b(-0.5d, -0.5d, -0.5d, (double) SearchStatUtils.POW)));
        if (z) {
            a2 = a(new b((double) SearchStatUtils.POW, -0.5d, -0.5d, (double) SearchStatUtils.POW));
        } else {
            a2 = a(new b(-0.0625d, -0.5d, -0.5d, (double) SearchStatUtils.POW));
        }
        float f7 = f3 + 1.0f;
        C0102a a6 = a(1.0f, f7, f7);
        float[] fArr = bVar.a;
        float f8 = fArr[0];
        float f9 = fArr[1];
        float f10 = fArr[2];
        float f11 = f8 + f9 + f10;
        int i = (f11 > 0.0f ? 1 : (f11 == 0.0f ? 0 : -1));
        float f12 = i == 0 ? 1.0f : (f8 / f11) * 3.0f;
        float f13 = i == 0 ? 1.0f : (f9 / f11) * 3.0f;
        float f14 = i == 0 ? 1.0f : (f10 / f11) * 3.0f;
        if (f3 < 0.0f) {
            float f15 = f13;
            a3 = new C0102a(f12 * 0.241f, f12 * 0.691f, f12 * 0.068f, (double) SearchStatUtils.POW, f15 * 0.241f, f15 * 0.691f, f15 * 0.068f, (double) SearchStatUtils.POW, f14 * 0.241f, f14 * 0.691f, f14 * 0.068f, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, (double) SearchStatUtils.POW, 1.0d);
            C0102a a7 = a();
            for (int i2 = 0; i2 < 16; i2++) {
                float[] fArr2 = a3.a;
                fArr2[i2] = (fArr2[i2] * (1.0f - f7)) + (a7.a[i2] * f7);
            }
        } else {
            a3 = a();
        }
        return a(a3, a(a(a(c0102a, a(a6, a2)), a4), a5));
    }

    public static ColorMatrix a(float f, float f2, float f3, int i) {
        float[] fArr = a(f, f2, f3, new b(Color.red(i) / 255.0f, Color.green(i) / 255.0f, Color.blue(i) / 255.0f, 1.0f)).a;
        return new ColorMatrix(new float[]{fArr[0], fArr[1], fArr[2], 0.0f, fArr[3], fArr[4], fArr[5], fArr[6], 0.0f, fArr[7], fArr[8], fArr[9], fArr[10], 0.0f, fArr[11], fArr[12], fArr[13], fArr[14], 1.0f, 0.0f});
    }

    public static C0102a a(float f, float f2, float f3, b bVar) {
        return a(a(f, f2, f3, bVar, false), d);
    }

    public static C0102a a(C0102a c0102a, C0102a c0102a2) {
        float[] fArr = c0102a2.a;
        float[] fArr2 = c0102a.a;
        return new C0102a((fArr[0] * fArr2[0]) + (fArr[4] * fArr2[1]) + (fArr[8] * fArr2[2]) + (fArr[12] * fArr2[3]), (fArr[1] * fArr2[0]) + (fArr[5] * fArr2[1]) + (fArr[9] * fArr2[2]) + (fArr[13] * fArr2[3]), (fArr[2] * fArr2[0]) + (fArr[6] * fArr2[1]) + (fArr[10] * fArr2[2]) + (fArr[14] * fArr2[3]), (fArr[3] * fArr2[0]) + (fArr[7] * fArr2[1]) + (fArr[11] * fArr2[2]) + (fArr[15] * fArr2[3]), (fArr[0] * fArr2[4]) + (fArr[4] * fArr2[5]) + (fArr[8] * fArr2[6]) + (fArr[12] * fArr2[7]), (fArr[1] * fArr2[4]) + (fArr[5] * fArr2[5]) + (fArr[9] * fArr2[6]) + (fArr[13] * fArr2[7]), (fArr[2] * fArr2[4]) + (fArr[6] * fArr2[5]) + (fArr[10] * fArr2[6]) + (fArr[14] * fArr2[7]), (fArr[3] * fArr2[4]) + (fArr[7] * fArr2[5]) + (fArr[11] * fArr2[6]) + (fArr[15] * fArr2[7]), (fArr[0] * fArr2[8]) + (fArr[4] * fArr2[9]) + (fArr[8] * fArr2[10]) + (fArr[12] * fArr2[11]), (fArr[1] * fArr2[8]) + (fArr[5] * fArr2[9]) + (fArr[9] * fArr2[10]) + (fArr[13] * fArr2[11]), (fArr[2] * fArr2[8]) + (fArr[6] * fArr2[9]) + (fArr[10] * fArr2[10]) + (fArr[14] * fArr2[11]), (fArr[3] * fArr2[8]) + (fArr[7] * fArr2[9]) + (fArr[11] * fArr2[10]) + (fArr[15] * fArr2[11]), (fArr[0] * fArr2[12]) + (fArr[4] * fArr2[13]) + (fArr[8] * fArr2[14]) + (fArr[12] * fArr2[15]), (fArr[1] * fArr2[12]) + (fArr[5] * fArr2[13]) + (fArr[9] * fArr2[14]) + (fArr[13] * fArr2[15]), (fArr[2] * fArr2[12]) + (fArr[6] * fArr2[13]) + (fArr[10] * fArr2[14]) + (fArr[14] * fArr2[15]), (fArr[3] * fArr2[12]) + (fArr[7] * fArr2[13]) + (fArr[11] * fArr2[14]) + (fArr[15] * fArr2[15]));
    }
}
