package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/* loaded from: classes2.dex */
public class CircleStrokeProgressBar extends CircleProgressBar {
    public int[] mMiddleStrokeColors;
    public Paint mMiddleStrokePaint;
    public float mMiddleStrokeWidth;

    public CircleStrokeProgressBar(Context context) {
        this(context, null);
    }

    public CircleStrokeProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleStrokeProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setIndeterminate(false);
        Paint paint = new Paint(1);
        this.mMiddleStrokePaint = paint;
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setMiddleStrokeColors(int[] iArr, float f) {
        this.mMiddleStrokeColors = iArr;
        this.mMiddleStrokeWidth = f;
    }

    @Override // com.miui.gallery.widget.CircleProgressBar, android.widget.ProgressBar, android.view.View
    public synchronized void onDraw(Canvas canvas) {
        Drawable middleDrawable;
        if (this.mMiddleStrokeColors != null && this.mMiddleStrokeWidth > 0.0f && (middleDrawable = getMiddleDrawable(getCurrentLevel())) != null) {
            this.mMiddleStrokePaint.setColor(this.mMiddleStrokeColors[getCurrentLevel()]);
            this.mMiddleStrokePaint.setStrokeWidth(this.mMiddleStrokeWidth);
            canvas.drawArc(new RectF(middleDrawable.getBounds()), -90.0f, getRate() * 360.0f, true, this.mMiddleStrokePaint);
        }
        super.onDraw(canvas);
    }
}
