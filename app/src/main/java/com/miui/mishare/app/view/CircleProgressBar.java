package com.miui.mishare.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.miui.mishare.R$styleable;

/* loaded from: classes3.dex */
public class CircleProgressBar extends View {
    public float percent;
    public int progressColor;
    public Paint progressPaint;
    public float radius;
    public RectF rect;
    public int ringColor;
    public Paint ringPaint;
    public float strokeWidth;

    public CircleProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.ringPaint = new Paint();
        this.progressPaint = new Paint();
        this.percent = 0.0f;
        this.rect = new RectF();
        initAttrs(context, attributeSet);
        initVariable();
    }

    public final void initAttrs(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CircleProgressBar, 0, 0);
        this.strokeWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.CircleProgressBar_strokeWidth, 10);
        this.ringColor = obtainStyledAttributes.getColor(R$styleable.CircleProgressBar_ringColor, 0);
        this.progressColor = obtainStyledAttributes.getColor(R$styleable.CircleProgressBar_progressColor, 16777215);
        obtainStyledAttributes.recycle();
    }

    public final void initVariable() {
        this.ringPaint.setAntiAlias(true);
        this.ringPaint.setColor(this.ringColor);
        this.ringPaint.setStyle(Paint.Style.STROKE);
        this.ringPaint.setStrokeCap(Paint.Cap.ROUND);
        this.ringPaint.setStrokeWidth(this.strokeWidth);
        this.progressPaint.setAntiAlias(true);
        this.progressPaint.setColor(this.progressColor);
        this.progressPaint.setStyle(Paint.Style.STROKE);
        this.progressPaint.setStrokeCap(Paint.Cap.ROUND);
        this.progressPaint.setStrokeWidth(this.strokeWidth);
    }

    public final void calculateRadius() {
        int width = getWidth();
        int height = getHeight();
        float min = (Math.min(width, height) / 2.0f) - (this.strokeWidth / 2.0f);
        this.radius = min;
        float f = width / 2.0f;
        float f2 = height / 2.0f;
        this.rect.set(f - min, f2 - min, f + min, f2 + min);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.radius <= 0.0f) {
            calculateRadius();
        }
        if (this.ringColor != 0) {
            canvas.drawArc(this.rect, 0.0f, 360.0f, false, this.ringPaint);
        }
        float f = this.percent;
        if (f > 0.0f) {
            canvas.drawArc(this.rect, -90.0f, f * 360.0f, false, this.progressPaint);
        }
    }

    public void setProgressPercent(float f) {
        this.percent = f;
        postInvalidate();
    }

    public void setProgressColor(int i) {
        int color = getContext().getColor(i);
        this.progressColor = color;
        this.progressPaint.setColor(color);
    }
}
