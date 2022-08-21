package com.miui.gallery.collage.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.collage.render.CollageRender;
import com.miui.gallery.widget.imageview.MatrixTransition;

/* loaded from: classes.dex */
public class CollageImageView extends View {
    public final float MAX_SCALE;
    public final float MAX_SCALE_TOUCH;
    public boolean mActivating;
    public Matrix mAnimTargetMatrix;
    public int mBackgroundColor;
    public Paint mBackgroundPaint;
    public RectF mBitmapDisplayInitRect;
    public RectF mBitmapDisplayRect;
    public Matrix mBitmapMatrix;
    public Paint mBitmapPaint;
    public RectF mBitmapRect;
    public Matrix mCanvasMatrix;
    public RectF mDisplayInitRect;
    public RectF mDisplayRect;
    public boolean mDrawBitmap;
    public Matrix mDrawingMatrix;
    public GradientDrawable mGradientDrawable;
    public Drawable mMask;
    public MatrixListener mMatrixListener;
    public MatrixTransition mMatrixTransition;
    public float[] mMatrixValues;
    public float mMaxWidth;
    public float mMaxWidthScale;
    public boolean mMirror;
    public Bitmap mOriginBitmap;
    public float mRadius;
    public RectF mRectFTemp;
    public int mRotateDegree;
    public PorterDuffXfermode mXfermodeSrcIn;
    public PorterDuffXfermode mXfermodeSrcOut;

    public CollageImageView(Context context) {
        super(context);
        this.mDisplayRect = new RectF();
        this.mDisplayInitRect = new RectF();
        this.mBitmapRect = new RectF();
        this.mBitmapDisplayInitRect = new RectF();
        this.mBitmapDisplayRect = new RectF();
        this.mCanvasMatrix = new Matrix();
        this.mBitmapMatrix = new Matrix();
        this.mDrawingMatrix = new Matrix();
        this.mAnimTargetMatrix = new Matrix();
        this.mMatrixListener = new MatrixListener();
        this.mMatrixValues = new float[9];
        this.mRectFTemp = new RectF();
        this.mBitmapPaint = new Paint(3);
        this.mBackgroundPaint = new Paint();
        this.mRadius = 0.0f;
        this.mXfermodeSrcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        this.mXfermodeSrcOut = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
        this.mGradientDrawable = new GradientDrawable();
        this.mDrawBitmap = true;
        this.mMirror = false;
        this.mRotateDegree = 0;
        this.MAX_SCALE = 4.0f;
        this.MAX_SCALE_TOUCH = 6.0f;
        this.mActivating = false;
        init();
    }

    public final void init() {
        MatrixTransition matrixTransition = new MatrixTransition();
        this.mMatrixTransition = matrixTransition;
        matrixTransition.setMatrixUpdateListener(this.mMatrixListener);
        this.mBackgroundColor = getResources().getColor(R.color.collage_item_background);
        this.mBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mBackgroundPaint.setColor(this.mBackgroundColor);
        this.mGradientDrawable.setColor(-1);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mDisplayRect.set(0.0f, 0.0f, i, i2);
        this.mDisplayInitRect.set(this.mDisplayRect);
        resolvePadding(this.mDisplayInitRect);
        resetBitmapMatrix();
    }

    public void setBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        this.mOriginBitmap = bitmap;
        this.mBitmapRect.set(0.0f, 0.0f, width, height);
        resetBitmapMatrix();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.mOriginBitmap == null) {
            return;
        }
        this.mDrawingMatrix.reset();
        this.mDrawingMatrix.postConcat(this.mBitmapMatrix);
        this.mDrawingMatrix.postConcat(this.mCanvasMatrix);
        canvas.save();
        canvas.clipRect(this.mDisplayRect);
        if (this.mMask != null) {
            drawBitmapWithMask(canvas);
        } else if (this.mRadius > 0.0f) {
            drawBitmapWithRadius(canvas);
        } else {
            drawBitmap(canvas);
        }
        canvas.restore();
    }

    public final void drawBitmap(Canvas canvas) {
        canvas.drawRect(this.mDisplayRect, this.mBackgroundPaint);
        if (!this.mDrawBitmap) {
            return;
        }
        canvas.drawBitmap(this.mOriginBitmap, this.mDrawingMatrix, this.mBitmapPaint);
    }

    public final void drawBitmapWithMaskDrawable(Canvas canvas, Drawable drawable, boolean z) {
        canvas.saveLayer(this.mDisplayRect, null, 31);
        drawable.draw(canvas);
        this.mBackgroundPaint.setXfermode(z ? this.mXfermodeSrcOut : this.mXfermodeSrcIn);
        this.mBitmapPaint.setXfermode(this.mXfermodeSrcIn);
        drawBitmap(canvas);
        this.mBitmapPaint.setXfermode(null);
        this.mBackgroundPaint.setXfermode(null);
        canvas.restore();
    }

    public final void drawBitmapWithRadius(Canvas canvas) {
        this.mGradientDrawable.setBounds(0, 0, getWidth(), getHeight());
        this.mGradientDrawable.setCornerRadius(this.mRadius);
        drawBitmapWithMaskDrawable(canvas, this.mGradientDrawable, false);
    }

    public final void drawBitmapWithMask(Canvas canvas) {
        this.mMask.setBounds(0, 0, getWidth(), getHeight());
        drawBitmapWithMaskDrawable(canvas, this.mMask, true);
    }

    public final void resetBitmapMatrix() {
        if (this.mBitmapRect.isEmpty() || this.mDisplayInitRect.isEmpty()) {
            return;
        }
        CollageRender.initBitmapMatrix(this.mBitmapRect, this.mBitmapMatrix, this.mDisplayInitRect, this.mMirror, this.mRotateDegree, this.mBitmapDisplayInitRect);
        resetInitParams();
        this.mCanvasMatrix.reset();
        invalidate();
    }

    public final void resetBitmapMatrixWithAnim() {
        if (this.mBitmapRect.isEmpty() || this.mDisplayInitRect.isEmpty()) {
            return;
        }
        CollageRender.initBitmapMatrix(this.mBitmapRect, this.mAnimTargetMatrix, this.mDisplayInitRect, this.mMirror, this.mRotateDegree, this.mBitmapDisplayInitRect);
        resetInitParams();
        this.mMatrixTransition.animMatrix(this.mBitmapMatrix, this.mAnimTargetMatrix, this.mCanvasMatrix, new Matrix());
    }

    public final void resetInitParams() {
        this.mBitmapDisplayRect.set(this.mBitmapDisplayInitRect);
        this.mMaxWidth = this.mBitmapDisplayInitRect.width() * 4.0f;
        this.mMaxWidthScale = this.mBitmapDisplayInitRect.width() * 6.0f;
    }

    public final void resetMatrixWithAnim() {
        this.mActivating = true;
        this.mCanvasMatrix.getValues(this.mMatrixValues);
        if (this.mMatrixValues[0] <= 1.0f) {
            countAnimMatrixWhenZoomOut(this.mAnimTargetMatrix);
        } else {
            countAnimMatrixWhenZoomIn(this.mAnimTargetMatrix);
        }
        this.mMatrixTransition.animMatrix(this.mCanvasMatrix, this.mAnimTargetMatrix);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void countAnimMatrixWhenZoomOut(android.graphics.Matrix r8) {
        /*
            r7 = this;
            android.graphics.Matrix r0 = r7.mCanvasMatrix
            r8.set(r0)
            float[] r0 = r7.mMatrixValues
            r8.getValues(r0)
            android.graphics.RectF r0 = r7.mRectFTemp
            android.graphics.RectF r1 = r7.mBitmapDisplayRect
            r0.set(r1)
            float[] r1 = r7.mMatrixValues
            r2 = 0
            r1 = r1[r2]
            r2 = 1065353216(0x3f800000, float:1.0)
            float r2 = r2 / r1
            android.graphics.RectF r1 = r7.mDisplayInitRect
            float r1 = r1.centerX()
            android.graphics.RectF r3 = r7.mDisplayInitRect
            float r3 = r3.centerY()
            r8.postScale(r2, r2, r1, r3)
            android.graphics.RectF r1 = r7.mBitmapDisplayInitRect
            r8.mapRect(r0, r1)
            float r1 = r0.left
            android.graphics.RectF r2 = r7.mDisplayInitRect
            float r3 = r2.left
            int r4 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            r5 = 0
            if (r4 <= 0) goto L3a
        L38:
            float r3 = r3 - r1
            goto L44
        L3a:
            float r1 = r0.right
            float r3 = r2.right
            int r4 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r4 >= 0) goto L43
            goto L38
        L43:
            r3 = r5
        L44:
            float r1 = r0.top
            float r4 = r2.top
            int r6 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r6 <= 0) goto L4f
            float r5 = r4 - r1
            goto L59
        L4f:
            float r0 = r0.bottom
            float r1 = r2.bottom
            int r2 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r2 >= 0) goto L59
            float r5 = r1 - r0
        L59:
            r8.postTranslate(r3, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.collage.widget.CollageImageView.countAnimMatrixWhenZoomOut(android.graphics.Matrix):void");
    }

    public void countAnimMatrixWhenZoomIn(Matrix matrix) {
        matrix.set(this.mCanvasMatrix);
        this.mRectFTemp.set(this.mBitmapDisplayRect);
        RectF rectF = this.mRectFTemp;
        float width = rectF.width();
        float f = this.mMaxWidth;
        if (width > f) {
            float width2 = f / rectF.width();
            matrix.postScale(width2, width2, this.mDisplayInitRect.centerX(), this.mDisplayInitRect.centerY());
            matrix.mapRect(rectF, this.mBitmapDisplayInitRect);
        }
        float width3 = rectF.width();
        float height = rectF.height();
        float f2 = rectF.left;
        float f3 = rectF.right;
        float f4 = rectF.top;
        float f5 = rectF.bottom;
        float width4 = this.mDisplayRect.width();
        float height2 = this.mDisplayRect.height();
        RectF rectF2 = this.mDisplayRect;
        float f6 = rectF2.left;
        float f7 = rectF2.right;
        float f8 = rectF2.top;
        float f9 = rectF2.bottom;
        float f10 = 0.0f;
        float f11 = f2 > f6 ? width3 > width4 ? -(f2 - f6) : (-(f2 - f6)) + ((width4 - width3) * 0.5f) : f3 < f7 ? width3 > width4 ? f7 - f3 : (f7 - f3) - ((width4 - width3) * 0.5f) : 0.0f;
        if (f4 > f8) {
            f10 = height > height2 ? -(f4 - f8) : (-(f4 - f8)) + ((height2 - height) * 0.5f);
        } else if (f5 < f9) {
            f10 = height > height2 ? f9 - f5 : (f9 - f5) - ((height2 - height) * 0.5f);
        }
        matrix.postTranslate(f11, f10);
    }

    public final void resolvePadding(RectF rectF) {
        rectF.left += getPaddingLeft();
        rectF.right -= getPaddingRight();
        rectF.top += getPaddingTop();
        rectF.bottom -= getPaddingBottom();
    }

    public Bitmap getBitmap() {
        return this.mOriginBitmap;
    }

    public void setDrawBitmap(boolean z) {
        this.mDrawBitmap = z;
        invalidate();
    }

    public void rotate() {
        int i = this.mRotateDegree - 90;
        this.mRotateDegree = i;
        this.mRotateDegree = i % 360;
        resetBitmapMatrixWithAnim();
    }

    public void mirror() {
        this.mMirror = !this.mMirror;
        resetBitmapMatrix();
    }

    public void resetDrawData(Bitmap bitmap, int i, boolean z) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        this.mOriginBitmap = bitmap;
        this.mBitmapRect.set(0.0f, 0.0f, width, height);
        this.mRotateDegree = i;
        this.mMirror = z;
        this.mDrawBitmap = true;
        resetBitmapMatrix();
    }

    /* loaded from: classes.dex */
    public class MatrixListener implements MatrixTransition.MatrixUpdateListener {
        @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
        public void onAnimationStart() {
        }

        public MatrixListener() {
        }

        @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
        public void onMatrixUpdate() {
            CollageImageView.this.invalidate();
            CollageImageView.this.refreshBitmapDisplayRect();
        }

        @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
        public void onAnimationEnd() {
            CollageImageView.this.mActivating = false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void transition(float r9, float r10) {
        /*
            r8 = this;
            android.graphics.RectF r0 = r8.mDisplayRect
            float r0 = r0.width()
            r1 = 1073741824(0x40000000, float:2.0)
            float r0 = r0 / r1
            android.graphics.RectF r2 = r8.mDisplayRect
            float r2 = r2.height()
            float r2 = r2 / r1
            android.graphics.RectF r1 = r8.mDisplayRect
            float r1 = r1.centerX()
            android.graphics.RectF r3 = r8.mDisplayRect
            float r3 = r3.centerY()
            r4 = 0
            int r5 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            r6 = 1065353216(0x3f800000, float:1.0)
            if (r5 <= 0) goto L32
            android.graphics.RectF r5 = r8.mBitmapDisplayRect
            float r5 = r5.left
            android.graphics.RectF r7 = r8.mDisplayRect
            float r7 = r7.left
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r7 <= 0) goto L32
            float r1 = r1 - r5
            float r1 = r1 / r0
            goto L47
        L32:
            int r5 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            if (r5 >= 0) goto L46
            android.graphics.RectF r5 = r8.mBitmapDisplayRect
            float r5 = r5.right
            android.graphics.RectF r7 = r8.mDisplayRect
            float r7 = r7.right
            int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r7 >= 0) goto L46
            float r5 = r5 - r1
            float r1 = r5 / r0
            goto L47
        L46:
            r1 = r6
        L47:
            int r0 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r0 <= 0) goto L5b
            android.graphics.RectF r0 = r8.mBitmapDisplayRect
            float r0 = r0.top
            android.graphics.RectF r5 = r8.mDisplayRect
            float r5 = r5.top
            int r5 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r5 <= 0) goto L5b
            float r3 = r3 - r0
            float r6 = r3 / r2
            goto L6e
        L5b:
            int r0 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r0 >= 0) goto L6e
            android.graphics.RectF r0 = r8.mBitmapDisplayRect
            float r0 = r0.bottom
            android.graphics.RectF r4 = r8.mDisplayRect
            float r4 = r4.bottom
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 >= 0) goto L6e
            float r0 = r0 - r3
            float r6 = r0 / r2
        L6e:
            float r9 = r9 * r1
            float r10 = r10 * r6
            android.graphics.Matrix r0 = r8.mCanvasMatrix
            r0.postTranslate(r9, r10)
            r8.refreshBitmapDisplayRect()
            r8.invalidate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.collage.widget.CollageImageView.transition(float, float):void");
    }

    public void appendScale(float f, float f2, float f3) {
        if (f <= 1.0f || this.mBitmapDisplayRect.width() <= this.mMaxWidthScale) {
            this.mCanvasMatrix.postScale(f, f, f2 - getLeft(), f3 - getTop());
            refreshBitmapDisplayRect();
            invalidate();
        }
    }

    public void resetBitmapLocation() {
        resetMatrixWithAnim();
    }

    public final void refreshBitmapDisplayRect() {
        this.mCanvasMatrix.mapRect(this.mBitmapDisplayRect, this.mBitmapDisplayInitRect);
    }

    public CollageRender.BitmapRenderData generateBitmapRenderData() {
        Matrix matrix = new Matrix();
        RectF rectF = new RectF();
        this.mCanvasMatrix.invert(matrix);
        matrix.mapRect(rectF, this.mBitmapDisplayInitRect);
        float f = rectF.left;
        RectF rectF2 = this.mBitmapDisplayInitRect;
        rectF.left = (f - rectF2.left) / rectF2.width();
        float f2 = rectF.right;
        RectF rectF3 = this.mBitmapDisplayInitRect;
        rectF.right = (f2 - rectF3.left) / rectF3.width();
        float f3 = rectF.top;
        RectF rectF4 = this.mBitmapDisplayInitRect;
        rectF.top = (f3 - rectF4.top) / rectF4.height();
        float f4 = rectF.bottom;
        RectF rectF5 = this.mBitmapDisplayInitRect;
        rectF.bottom = (f4 - rectF5.top) / rectF5.height();
        CollageRender.BitmapRenderData bitmapRenderData = new CollageRender.BitmapRenderData();
        bitmapRenderData.bitmap = this.mOriginBitmap;
        bitmapRenderData.rotate = this.mRotateDegree;
        bitmapRenderData.mirror = this.mMirror;
        bitmapRenderData.bitmapInsideRect.set(rectF);
        bitmapRenderData.transition = true;
        bitmapRenderData.maskDrawable = this.mMask;
        bitmapRenderData.radius = this.mRadius;
        return bitmapRenderData;
    }

    public void setMask(Drawable drawable) {
        this.mMask = drawable;
        invalidate();
    }

    public void setRadius(float f) {
        this.mRadius = f;
        invalidate();
    }

    public void getCanvasMatrix(Matrix matrix) {
        matrix.set(this.mCanvasMatrix);
    }

    public void getDisplayRect(RectF rectF) {
        rectF.set(this.mDisplayRect);
    }

    public boolean isMirror() {
        return this.mMirror;
    }

    public int getRotateDegree() {
        return this.mRotateDegree;
    }

    public boolean isActivating() {
        return this.mActivating;
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
    }
}
