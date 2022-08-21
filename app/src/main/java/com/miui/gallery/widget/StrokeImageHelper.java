package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.miui.gallery.baseui.R$color;
import com.miui.gallery.baseui.R$dimen;

/* loaded from: classes2.dex */
public class StrokeImageHelper {
    public RectF mContentBounds = new RectF();
    public Paint mStrokePaint;

    public StrokeImageHelper(Context context) {
        Paint paint = new Paint(1);
        this.mStrokePaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mStrokePaint.setStrokeWidth(context.getResources().getDimensionPixelSize(R$dimen.photo_editor_preview_stroke_width));
        this.mStrokePaint.setColor(context.getResources().getColor(R$color.photo_editor_preview_stroke_color));
    }

    public void draw(Canvas canvas, RectF rectF) {
        this.mContentBounds.set(rectF);
        fixContentBounds();
        canvas.drawRect(this.mContentBounds, this.mStrokePaint);
    }

    public void draw(Canvas canvas, Rect rect, Matrix matrix) {
        this.mContentBounds.set(rect);
        draw(canvas, matrix);
    }

    public void draw(Canvas canvas, RectF rectF, Matrix matrix) {
        this.mContentBounds.set(rectF);
        draw(canvas, matrix);
    }

    public final void draw(Canvas canvas, Matrix matrix) {
        matrix.mapRect(this.mContentBounds);
        fixContentBounds();
        canvas.drawRect(this.mContentBounds, this.mStrokePaint);
    }

    public final void fixContentBounds() {
        RectF rectF = this.mContentBounds;
        rectF.set(Math.round(rectF.left) + 0.5f, Math.round(this.mContentBounds.top) + 0.5f, Math.round(this.mContentBounds.right) - 0.5f, Math.round(this.mContentBounds.bottom) - 0.5f);
    }
}
