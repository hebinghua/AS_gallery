package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.core.math.MathUtils;
import com.miui.gallery.util.BaseMiscUtil;

/* loaded from: classes3.dex */
public abstract class BaseTransitionRender implements ITransitionRender {
    public static final Interpolator DEFAULT_INTERPOLATOR = new LinearInterpolator();
    public final Interpolator mAlphaInterpolator;
    public int mAnimAlpha;
    public RectF mAnimFrame;
    public final int mFromAlpha;
    public final RectF mFromFrame;
    public RectF mTemp1;
    public RectF mTemp2;
    public final int mToAlpha;
    public final RectF mToFrame;

    public void evaluateCustomValues(float f) {
    }

    public abstract void onDraw(Canvas canvas, RectF rectF);

    public abstract void onPreDraw(RectF rectF, int i, float f);

    public abstract boolean skipDraw();

    public BaseTransitionRender(RectF rectF, RectF rectF2, int i, int i2, Interpolator interpolator) {
        this.mFromFrame = rectF;
        this.mToFrame = rectF2;
        this.mFromAlpha = i;
        this.mToAlpha = i2;
        this.mAlphaInterpolator = interpolator == null ? DEFAULT_INTERPOLATOR : interpolator;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public RectF getFromFrame() {
        return this.mFromFrame;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public RectF getToFrame() {
        return this.mToFrame;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public final void draw(Canvas canvas, float f, float f2) {
        if (skipDraw()) {
            return;
        }
        evaluateFrame(f);
        evaluateAlpha(f, f2);
        evaluateCustomValues(f);
        onPreDraw(this.mAnimFrame, this.mAnimAlpha, f);
        onDraw(canvas, this.mAnimFrame);
    }

    public final RectF getAnimFrame() {
        return this.mAnimFrame;
    }

    public final RectF getTemp1() {
        if (this.mTemp1 == null) {
            this.mTemp1 = new RectF();
        }
        return this.mTemp1;
    }

    public final RectF getTemp2() {
        if (this.mTemp2 == null) {
            this.mTemp2 = new RectF();
        }
        return this.mTemp2;
    }

    public void evaluateFrame(float f) {
        if (this.mAnimFrame == null) {
            this.mAnimFrame = new RectF();
        }
        if (BaseMiscUtil.floatEquals(f, 0.0f)) {
            this.mAnimFrame.set(this.mFromFrame);
        } else if (BaseMiscUtil.floatEquals(f, 1.0f)) {
            this.mAnimFrame.set(this.mToFrame);
        } else {
            RectF rectF = this.mToFrame;
            float f2 = rectF.left;
            RectF rectF2 = this.mFromFrame;
            float f3 = rectF2.left;
            float f4 = ((f2 - f3) * f) + f3;
            float f5 = rectF.top;
            float f6 = rectF2.top;
            float f7 = ((f5 - f6) * f) + f6;
            float f8 = rectF.right;
            float f9 = rectF2.right;
            float f10 = rectF.bottom;
            float f11 = rectF2.bottom;
            this.mAnimFrame.set(f4, f7, ((f8 - f9) * f) + f9, ((f10 - f11) * f) + f11);
        }
    }

    public void evaluateAlpha(float f, float f2) {
        int i = this.mFromAlpha;
        int i2 = this.mToAlpha;
        if (i == i2) {
            this.mAnimAlpha = i;
        } else if (f2 == -1.0f) {
            this.mAnimAlpha = MathUtils.clamp(i + ((int) ((i2 - i) * this.mAlphaInterpolator.getInterpolation(f))), 0, 255);
        } else {
            this.mAnimAlpha = MathUtils.clamp(i + ((int) ((i2 - i) * f2)), 0, 255);
        }
    }
}
