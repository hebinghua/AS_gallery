package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class RoundRectProgressView extends View {
    public final int DEFAULT_BASE_COLOR;
    public final int DEFAULT_MAX_PROGRESS;
    public final int DEFAULT_PROGRESS_COLOR;
    public int mBaseColor;
    public LinearGradient mGradient;
    public long mMaxProgress;
    public Paint mPaint;
    public long mProgress;
    public int mProgressColorEnd;
    public int mProgressColorStart;
    public float mRadius;
    public RectF mRectF;

    public RoundRectProgressView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundRectProgressView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.DEFAULT_PROGRESS_COLOR = 0;
        this.DEFAULT_BASE_COLOR = 0;
        this.DEFAULT_MAX_PROGRESS = 100;
        this.mRectF = new RectF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RoundRectProgressView);
        this.mProgressColorStart = obtainStyledAttributes.getColor(4, 0);
        this.mProgressColorEnd = obtainStyledAttributes.getColor(3, 0);
        this.mBaseColor = obtainStyledAttributes.getColor(0, 0);
        this.mProgress = obtainStyledAttributes.getInteger(2, 0);
        this.mMaxProgress = obtainStyledAttributes.getInteger(1, 100);
        this.mRadius = obtainStyledAttributes.getDimension(5, 0.0f);
        obtainStyledAttributes.recycle();
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mGradient = new LinearGradient(0.0f, 0.0f, 100.0f, 100.0f, new int[]{this.mProgressColorStart, this.mProgressColorEnd}, (float[]) null, Shader.TileMode.CLAMP);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setColor(this.mBaseColor);
        float f = 0;
        this.mRectF.set(f, f, getWidth(), getHeight());
        RectF rectF = this.mRectF;
        float f2 = this.mRadius;
        canvas.drawRoundRect(rectF, f2, f2, this.mPaint);
        this.mPaint.setColor(this.mProgressColorStart);
        this.mPaint.setShader(this.mGradient);
        long j = this.mProgress;
        long j2 = this.mMaxProgress;
        if (j < j2) {
            this.mRectF.set(f, f, (((float) j) / ((float) j2)) * getWidth(), getHeight());
        } else {
            this.mRectF.set(f, f, getWidth(), getHeight());
        }
        RectF rectF2 = this.mRectF;
        float f3 = this.mRadius;
        canvas.drawRoundRect(rectF2, f3, f3, this.mPaint);
    }

    public long getProgress() {
        return this.mProgress;
    }

    public void setProgress(long j) {
        this.mProgress = j;
        invalidate();
    }

    public void setProgress(long j, long j2) {
        this.mProgress = j;
        this.mMaxProgress = j2;
        invalidate();
    }
}
