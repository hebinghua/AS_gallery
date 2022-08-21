package com.miui.gallery.video.editor.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes2.dex */
public class VideoPlayProgress extends View {
    public int defaultColor;
    public Paint paint;
    public int startX;
    public int startY;
    public int stopX;
    public int stopY;

    public VideoPlayProgress(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.startX = 0;
        this.stopX = 0;
        this.defaultColor = Color.parseColor("#4da0f8");
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

    public void setColor(int i) {
        this.paint.setColor(i);
        invalidate();
    }

    public void updateWidth(int i, int i2, int i3, int i4) {
        this.startX = i;
        this.startY = i2;
        this.stopX = i3;
        this.stopY = i4;
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(this.startX, this.startY, this.stopX, this.stopY, this.paint);
    }
}
