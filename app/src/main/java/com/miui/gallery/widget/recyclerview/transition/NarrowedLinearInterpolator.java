package com.miui.gallery.widget.recyclerview.transition;

import android.view.animation.Interpolator;

/* loaded from: classes3.dex */
public class NarrowedLinearInterpolator implements Interpolator {
    public final float mEnd;
    public final float mRange;
    public final float mStart;

    public NarrowedLinearInterpolator(float f, float f2) {
        if (f > f2) {
            throw new IllegalArgumentException("start shouldn't greater than end");
        }
        this.mStart = f;
        this.mEnd = f2;
        this.mRange = f2 - f;
    }

    public float getStart() {
        return this.mStart;
    }

    public float getEnd() {
        return this.mEnd;
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        if (Float.compare(f, this.mStart) <= 0) {
            return 0.0f;
        }
        if (Float.compare(f, this.mEnd) < 0) {
            return (f - this.mStart) / this.mRange;
        }
        return 1.0f;
    }
}
