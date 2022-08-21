package com.miui.gallery.movie.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes2.dex */
public class PlayProgressView extends View {
    public int defaultColor;
    public float mProgress;
    public Paint paint;

    public PlayProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.defaultColor = Color.parseColor("#e5ffffff");
        init();
    }

    public final void init() {
        Paint paint = new Paint();
        this.paint = paint;
        paint.setColor(this.defaultColor);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(10.0f);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0.0f, 0.0f, this.mProgress * getWidth(), 0.0f, this.paint);
    }

    public void setProgress(float f) {
        this.mProgress = f;
        invalidate();
    }
}
