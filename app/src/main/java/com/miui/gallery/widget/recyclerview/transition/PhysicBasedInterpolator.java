package com.miui.gallery.widget.recyclerview.transition;

import android.view.animation.Interpolator;

/* loaded from: classes3.dex */
public class PhysicBasedInterpolator implements Interpolator {
    public float c;
    public float c2;
    public float k;
    public float r;
    public float w;
    public float mInitial = -1.0f;
    public float m = 1.0f;
    public float c1 = -1.0f;

    public PhysicBasedInterpolator(float f, float f2) {
        float f3;
        float f4;
        double d = f2;
        double pow = Math.pow(6.283185307179586d / d, 2.0d);
        float f5 = this.m;
        this.k = (float) (pow * f5);
        this.c = (float) (((f * 12.566370614359172d) * f5) / d);
        float f6 = this.m;
        float sqrt = ((float) Math.sqrt(((f5 * 4.0f) * f3) - (f4 * f4))) / (f6 * 2.0f);
        this.w = sqrt;
        float f7 = -((this.c / 2.0f) * f6);
        this.r = f7;
        this.c2 = (0.0f - (f7 * this.mInitial)) / sqrt;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        return (float) ((Math.pow(2.718281828459045d, this.r * f) * ((this.c1 * Math.cos(this.w * f)) + (this.c2 * Math.sin(this.w * f)))) + 1.0d);
    }
}
