package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.BaseMiscUtil;

/* loaded from: classes2.dex */
public class ColorRingProgress extends View {
    public int mBackColor;
    public int mColorInterval;
    public float mColorIntervalPercent;
    public int mForeColor;
    public Paint mPaint;
    public float mProgress;
    public RectF mRectF;
    public int mThickness;

    public ColorRingProgress(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0, 0);
    }

    public final void init(Context context, AttributeSet attributeSet, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ColorRingProgress, i, i2);
        this.mForeColor = obtainStyledAttributes.getColor(2, -1);
        this.mBackColor = obtainStyledAttributes.getColor(0, -6899465);
        this.mThickness = obtainStyledAttributes.getDimensionPixelSize(3, 14);
        this.mColorInterval = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        obtainStyledAttributes.recycle();
        this.mRectF = new RectF();
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mThickness);
    }

    public void setProgress(float f) {
        this.mProgress = Math.min(Math.max(f, 0.0f), 1.0f);
        invalidate();
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.mBackColor = i;
        invalidate();
    }

    public void setForegroundColor(int i) {
        this.mForeColor = i;
        invalidate();
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        RectF rectF = this.mRectF;
        int i5 = this.mThickness;
        rectF.set(i5, i5, getWidth() - this.mThickness, getHeight() - this.mThickness);
        this.mColorIntervalPercent = (float) (this.mColorInterval / (getWidth() * 3.141592653589793d));
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() * getHeight() <= 0) {
            return;
        }
        float f = this.mColorIntervalPercent;
        float f2 = (f * 360.0f) / 2.0f;
        float f3 = this.mProgress;
        if (1.0f - f3 < f * 2.0f || BaseMiscUtil.floatEquals(f3, 0.0f)) {
            f2 = 0.0f;
        }
        float f4 = (-90.0f) - f2;
        this.mPaint.setColor(0);
        float f5 = 2.0f * f2;
        int i = (f5 > 0.0f ? 1 : (f5 == 0.0f ? 0 : -1));
        if (i > 0) {
            canvas.drawArc(this.mRectF, f4, f5, false, this.mPaint);
        }
        this.mPaint.setColor(this.mForeColor);
        float f6 = f4 + f5;
        float f7 = (this.mProgress * 360.0f) - f2;
        if (f6 + f7 > 270.0f) {
            f7 = 270.0f - f6;
        }
        if (f7 > 0.0f) {
            canvas.drawArc(this.mRectF, f6, f7, false, this.mPaint);
        }
        this.mPaint.setColor(0);
        float f8 = f6 + f7;
        if (i > 0) {
            canvas.drawArc(this.mRectF, f8, f5, false, this.mPaint);
        }
        this.mPaint.setColor(this.mBackColor);
        float f9 = f8 + f5;
        float f10 = (270.0f - f9) - f2;
        if (f10 <= 0.0f) {
            return;
        }
        canvas.drawArc(this.mRectF, f9, f10, false, this.mPaint);
    }
}
