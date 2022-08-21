package com.miui.gallery.editor.photo.core.common.model;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen;

/* loaded from: classes2.dex */
public abstract class BaseDrawNode implements IDrawNode {
    public RectF mDisplayBitmapRect;
    public RectF mOriginBitmapRect;
    public Matrix mTempMatrix;

    public abstract void draw(Canvas canvas);

    public DoodlePen getDoodlePen() {
        return null;
    }

    public void reset() {
    }

    public void setDoodlePen(DoodlePen doodlePen) {
    }

    public void draw(Canvas canvas, RectF rectF) {
        if (this.mOriginBitmapRect == null || this.mDisplayBitmapRect == null || rectF == null) {
            return;
        }
        canvas.save();
        this.mTempMatrix.setRectToRect(this.mOriginBitmapRect, rectF, Matrix.ScaleToFit.FILL);
        canvas.concat(this.mTempMatrix);
        RectF rectF2 = this.mDisplayBitmapRect;
        canvas.translate(rectF2.left, rectF2.top);
        draw(canvas);
        canvas.restore();
    }

    public void setBitmapRects(RectF rectF, RectF rectF2) {
        this.mTempMatrix = new Matrix();
        this.mOriginBitmapRect = rectF;
        this.mDisplayBitmapRect = rectF2;
    }

    public int getAlpha() {
        if (getDoodlePen() != null) {
            float alpha = getDoodlePen().getAlpha();
            return (int) ((getDoodlePen().getType().equals("MarkPen_01") ? alpha * 0.5d : alpha) * 255.0d);
        }
        return 255;
    }
}
