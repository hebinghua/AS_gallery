package com.miui.gallery.signature.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatRadioButton;

/* loaded from: classes2.dex */
public class SelectColorView extends AppCompatRadioButton {
    public float mArcStrokeWidth;
    public int mInnerCircleColor;
    public float mInnerCircleRadius;
    public int mOutSelectColor;
    public Paint mPaint;

    public SelectColorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mInnerCircleColor = -16777216;
        this.mOutSelectColor = -16776961;
    }

    public void init(int i, int i2, float f, float f2) {
        this.mInnerCircleColor = i;
        this.mOutSelectColor = i2;
        this.mInnerCircleRadius = f;
        this.mArcStrokeWidth = f2;
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        super.setChecked(z);
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        if (this.mPaint == null) {
            this.mPaint = new Paint(1);
        }
        drawInnerCircle(canvas, width, height);
        if (isChecked()) {
            drawOutArc(canvas, width, height);
        }
    }

    public final void drawInnerCircle(Canvas canvas, float f, float f2) {
        this.mPaint.setColor(this.mInnerCircleColor);
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(f / 2.0f, f2 / 2.0f, this.mInnerCircleRadius, this.mPaint);
    }

    public final void drawOutArc(Canvas canvas, float f, float f2) {
        this.mPaint.setColor(this.mInnerCircleColor);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mArcStrokeWidth);
        float f3 = this.mArcStrokeWidth;
        canvas.drawArc(f3 / 2.0f, f3 / 2.0f, f - (f3 / 2.0f), f2 - (f3 / 2.0f), 0.0f, 360.0f, false, this.mPaint);
    }
}
