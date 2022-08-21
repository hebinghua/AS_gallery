package com.miui.gallery.signature.core.pen;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.signature.core.bean.ControlPoint;

/* loaded from: classes2.dex */
public class SteelPen extends BasePen {
    public RectF mOvalRect;

    @Override // com.miui.gallery.signature.core.pen.BasePen
    public void doDown(Canvas canvas) {
    }

    public SteelPen(Paint paint) {
        super(paint);
        this.mOvalRect = new RectF();
    }

    @Override // com.miui.gallery.signature.core.pen.BasePen
    public void doMove(double d) {
        addDrawPoint(d);
    }

    @Override // com.miui.gallery.signature.core.pen.BasePen
    public void doUp(double d) {
        addDrawPoint(d, 5);
    }

    public final void addDrawPoint(double d, int i) {
        double ceil = 1.0d / ((int) Math.ceil(d / i));
        for (double d2 = SearchStatUtils.POW; d2 < 1.0d; d2 += ceil) {
            this.mPointList.add(this.mBezierControl.getPoint(d2));
        }
        Log.d("SteelPen", "addDrawPoint: " + this.mPointList);
    }

    public final void addDrawPoint(double d) {
        double ceil = 1.0d / ((int) Math.ceil(d / 8.0d));
        for (double d2 = SearchStatUtils.POW; d2 < 1.0d; d2 += ceil) {
            this.mPointList.add(this.mBezierControl.getPoint(d2));
        }
        Log.d("SteelPen", "addDrawPoint: " + this.mPointList);
    }

    @Override // com.miui.gallery.signature.core.pen.BasePen
    public void doDraw(Canvas canvas) {
        this.mCurrentPoint = this.mPointList.get(0);
        for (int i = 0; i < this.mPointList.size(); i++) {
            ControlPoint controlPoint = this.mPointList.get(i);
            drawLine(canvas, controlPoint, this.mPaint);
            this.mCurrentPoint = controlPoint;
        }
    }

    public final void drawLine(Canvas canvas, ControlPoint controlPoint, Paint paint) {
        ControlPoint controlPoint2 = this.mCurrentPoint;
        if (controlPoint2.x == controlPoint.x && controlPoint2.y == controlPoint.y) {
            return;
        }
        drawLine(canvas, controlPoint2, controlPoint, paint);
    }

    public final void drawLine(Canvas canvas, ControlPoint controlPoint, ControlPoint controlPoint2, Paint paint) {
        double d;
        double d2 = controlPoint.x;
        double d3 = controlPoint.y;
        double d4 = controlPoint.width;
        double d5 = controlPoint2.x;
        double d6 = controlPoint2.y;
        double d7 = controlPoint2.width;
        double hypot = Math.hypot(d2 - d5, d3 - d6);
        double d8 = 2.0d;
        if (paint.getStrokeWidth() < 6.0f) {
            d = hypot / 2.0d;
        } else {
            d = hypot / (paint.getStrokeWidth() > 60.0f ? 4.0d : 3.0d);
        }
        int i = ((int) d) + 1;
        double d9 = i;
        double d10 = (d5 - d2) / d9;
        double d11 = (d6 - d3) / d9;
        double d12 = (d7 - d4) / d9;
        int i2 = 0;
        while (i2 < i) {
            double d13 = d4 / d8;
            double d14 = d12;
            this.mOvalRect.set((float) (d2 - d13), (float) (d3 - d13), (float) (d2 + d13), (float) (d3 + d13));
            canvas.drawOval(this.mOvalRect, paint);
            d2 += d10;
            d3 += d11;
            i2++;
            i = i;
            d4 += d14;
            d12 = d14;
            d8 = 2.0d;
        }
    }
}
