package com.miui.gallery.collage.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import com.miui.gallery.collage.render.CollageRender;

/* loaded from: classes.dex */
public class BitmapItemRender {
    public RectF mBitmapBound = new RectF();
    public RectF mBitmapInitBound = new RectF();
    public RectF mBitmapInsideBound = new RectF();
    public Matrix mDrawingMatrix = new Matrix();
    public Matrix mUserMatrix = new Matrix();
    public Matrix mBitmapMatrix = new Matrix();
    public PorterDuffXfermode mPorterDuffModeOut = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    public PorterDuffXfermode mPorterDuffModeIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    public GradientDrawable mGradientDrawable = new GradientDrawable();
    public Paint mPaint = new Paint(3);

    public BitmapItemRender() {
        this.mGradientDrawable.setColor(-1);
    }

    public void drawBitmapItem(CollageRender.BitmapRenderData bitmapRenderData, RectF rectF, Canvas canvas, float f) {
        Bitmap bitmap;
        if (bitmapRenderData == null || (bitmap = bitmapRenderData.bitmap) == null) {
            return;
        }
        this.mBitmapBound.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        if (bitmapRenderData.transition) {
            CollageRender.initBitmapMatrix(this.mBitmapBound, this.mBitmapMatrix, rectF, bitmapRenderData.mirror, bitmapRenderData.rotate, this.mBitmapInitBound);
            this.mBitmapInsideBound.set(bitmapRenderData.bitmapInsideRect);
            RectF rectF2 = this.mBitmapInsideBound;
            float width = rectF2.left * this.mBitmapInitBound.width();
            RectF rectF3 = this.mBitmapInitBound;
            rectF2.left = width + rectF3.left;
            RectF rectF4 = this.mBitmapInsideBound;
            float width2 = rectF4.right * rectF3.width();
            RectF rectF5 = this.mBitmapInitBound;
            rectF4.right = width2 + rectF5.left;
            RectF rectF6 = this.mBitmapInsideBound;
            float height = rectF6.top * rectF5.height();
            RectF rectF7 = this.mBitmapInitBound;
            rectF6.top = height + rectF7.top;
            RectF rectF8 = this.mBitmapInsideBound;
            rectF8.bottom = (rectF8.bottom * rectF7.height()) + this.mBitmapInitBound.top;
            this.mUserMatrix.reset();
            this.mUserMatrix.setRectToRect(this.mBitmapInsideBound, this.mBitmapInitBound, Matrix.ScaleToFit.CENTER);
            this.mDrawingMatrix.reset();
            this.mDrawingMatrix.postConcat(this.mBitmapMatrix);
            this.mDrawingMatrix.postConcat(this.mUserMatrix);
            Drawable drawable = bitmapRenderData.maskDrawable;
            float f2 = bitmapRenderData.radius;
            if (drawable != null) {
                drawable.setBounds(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
                canvas.saveLayer(rectF, null, 31);
                drawable.draw(canvas);
                this.mPaint.setXfermode(this.mPorterDuffModeOut);
                canvas.drawBitmap(bitmap, this.mDrawingMatrix, this.mPaint);
                this.mPaint.setXfermode(null);
                canvas.restore();
                return;
            } else if (f2 > 0.0f) {
                this.mGradientDrawable.setBounds(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
                this.mGradientDrawable.setCornerRadius(f2 * f);
                canvas.saveLayer(rectF, null, 31);
                this.mGradientDrawable.draw(canvas);
                this.mPaint.setXfermode(this.mPorterDuffModeIn);
                canvas.drawBitmap(bitmap, this.mDrawingMatrix, this.mPaint);
                this.mPaint.setXfermode(null);
                canvas.restore();
                return;
            } else {
                canvas.drawBitmap(bitmap, this.mDrawingMatrix, this.mPaint);
                return;
            }
        }
        CollageRender.initBitmapMatrix(this.mBitmapBound, this.mBitmapMatrix, rectF, this.mBitmapInitBound);
        canvas.drawBitmap(bitmap, this.mBitmapMatrix, this.mPaint);
    }
}
