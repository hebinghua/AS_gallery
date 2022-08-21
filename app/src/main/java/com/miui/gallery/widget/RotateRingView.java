package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class RotateRingView extends View {
    public static final float[] HEAD_POSITIONS = {0.75f, 1.0f};
    public int[] mChangeColors;
    public int mCircleX;
    public int mCircleY;
    public int mHeadMaxAlpha;
    public Paint mPaint;
    public int mProgress;
    public int mRadius;
    public RectF mRectF;
    public Matrix mRotateMatrix;
    public int mStrokeWidth;

    public int getHeadMinAlpha() {
        return 96;
    }

    public RotateRingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RotateRingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRotateMatrix = new Matrix();
        this.mRectF = new RectF();
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.rotate_ring_stroke_width);
        this.mStrokeWidth = dimensionPixelSize;
        this.mPaint.setStrokeWidth(dimensionPixelSize);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mRadius = context.getResources().getDimensionPixelSize(R.dimen.rotate_ring_radius);
        this.mChangeColors = new int[]{Color.argb(96, 255, 255, 255), Color.argb(96, 255, 255, 255)};
    }

    public int getCircleHeight() {
        return (this.mRadius * 2) + (this.mStrokeWidth * 2);
    }

    public void setHeadMaxAlpha(int i) {
        this.mHeadMaxAlpha = i;
        this.mChangeColors[1] = Color.argb(i, 255, 255, 255);
        invalidate();
    }

    public int getHeadMaxAlpha() {
        return this.mHeadMaxAlpha;
    }

    public void setProgress(int i) {
        this.mProgress = i;
        invalidate();
    }

    public int getProgress() {
        return this.mProgress;
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5 = i / 2;
        this.mCircleX = i5;
        int i6 = i2 / 2;
        this.mCircleY = i6;
        RectF rectF = this.mRectF;
        int i7 = this.mRadius;
        rectF.set(i5 - i7, i6 - i7, i5 + i7, i6 + i7);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawRing(canvas);
        canvas.restore();
    }

    public void drawRing(Canvas canvas) {
        this.mRotateMatrix.setRotate(this.mProgress, this.mCircleX, this.mCircleY);
        SweepGradient sweepGradient = new SweepGradient(this.mCircleX, this.mCircleY, this.mChangeColors, HEAD_POSITIONS);
        sweepGradient.setLocalMatrix(this.mRotateMatrix);
        this.mPaint.setShader(sweepGradient);
        canvas.drawOval(this.mRectF, this.mPaint);
    }
}
