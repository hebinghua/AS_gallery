package com.miui.gallery.widget.imageview;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.widget.imageview.MatrixTransition;

/* loaded from: classes2.dex */
public class BitmapGestureParamsHolder {
    public MatrixTransition mMatrixTransition;
    public float mMaxWidth;
    public float mMaxWidthScale;
    public float mMinWidthScale;
    public ParamsChangeListener mParamsChangeListener;
    public View mTarget;
    public RectF mDisplayRect = new RectF();
    public RectF mDisplayInitRect = new RectF();
    public Rect mDisplayInitOffset = new Rect();
    public RectF mBitmapRect = new RectF();
    public RectF mBitmapDisplayInitRect = new RectF();
    public RectF mBitmapDisplayRect = new RectF();
    public RectF mDisplayInsideRect = new RectF();
    public RectF mBitmapDisplayInsideRect = new RectF();
    public Matrix mBitmapToDisplayMatrix = new Matrix();
    public Matrix mDisplayToBitmapMatrix = new Matrix();
    public Matrix mCanvasMatrix = new Matrix();
    public Matrix mCanvasMatrixInvert = new Matrix();
    public Matrix mAnimTargetMatrix = new Matrix();
    public float[] mMatrixValues = new float[9];
    public RectF mRectFTemp = new RectF();
    public float mScaleFocusX = 0.0f;
    public float mScaleFocusY = 0.0f;
    public MatrixTransition.MatrixUpdateListener mMatrixUpdateListener = new MatrixTransition.MatrixUpdateListener() { // from class: com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.1
        @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
        public void onAnimationEnd() {
        }

        @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
        public void onAnimationStart() {
        }

        @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
        public void onMatrixUpdate() {
            BitmapGestureParamsHolder.this.performCanvasMatrixChange();
        }
    };

    /* loaded from: classes2.dex */
    public interface ParamsChangeListener {
        void onBitmapMatrixChanged();

        void onCanvasMatrixChange();
    }

    public BitmapGestureParamsHolder(View view, ParamsChangeListener paramsChangeListener) {
        this.mTarget = view;
        MatrixTransition matrixTransition = new MatrixTransition();
        this.mMatrixTransition = matrixTransition;
        matrixTransition.setMatrixUpdateListener(this.mMatrixUpdateListener);
        this.mParamsChangeListener = paramsChangeListener;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        resetBitmapMatrix();
    }

    public void setDisplayInitOffset(int i, int i2, int i3, int i4) {
        this.mDisplayInitOffset.set(i, i2, i3, i4);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        onSizeChanged(i, i2, i3, i4, true, true);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4, boolean z, boolean z2) {
        RectF rectF = new RectF(this.mBitmapDisplayInitRect);
        RectF rectF2 = new RectF(this.mBitmapDisplayRect);
        this.mDisplayRect.set(0.0f, 0.0f, i, i2);
        RectF rectF3 = this.mDisplayInitRect;
        Rect rect = this.mDisplayInitOffset;
        rectF3.set(rect.left, rect.top, Math.max(0, i - rect.right), Math.max(0, i2 - this.mDisplayInitOffset.bottom));
        resolvePadding(this.mDisplayInitRect);
        if (this.mBitmapRect.isEmpty()) {
            return;
        }
        resetBitmapInitRect();
        if (z) {
            this.mCanvasMatrix.reset();
            this.mBitmapDisplayRect.set(this.mBitmapDisplayInitRect);
            this.mCanvasMatrix.invert(this.mCanvasMatrixInvert);
            this.mDisplayInsideRect.set(this.mDisplayRect);
            this.mBitmapDisplayInsideRect.set(this.mBitmapDisplayRect);
        } else if (rectF2.isEmpty() || rectF.isEmpty()) {
            resetBitmapRect();
        } else {
            Matrix matrix = new Matrix();
            matrix.postTranslate(-rectF.left, -rectF.top);
            float width = this.mBitmapDisplayInitRect.width() / rectF.width();
            matrix.postScale(width, width);
            RectF rectF4 = this.mBitmapDisplayInitRect;
            matrix.postTranslate(rectF4.left, rectF4.top);
            matrix.mapRect(rectF2);
            matrix.setRectToRect(this.mBitmapDisplayInitRect, rectF2, Matrix.ScaleToFit.CENTER);
            this.mCanvasMatrix.set(matrix);
            this.mCanvasMatrix.mapRect(this.mBitmapDisplayRect, this.mBitmapDisplayInitRect);
            this.mCanvasMatrix.invert(this.mCanvasMatrixInvert);
            this.mCanvasMatrixInvert.mapRect(this.mDisplayInsideRect, this.mDisplayRect);
            this.mBitmapDisplayInsideRect.set(this.mBitmapDisplayRect);
            this.mBitmapDisplayInsideRect.intersect(this.mDisplayRect);
            if (z2) {
                fixMatrix(false);
            }
        }
        onBitmapMatrixChanged();
    }

    public final void resetBitmapMatrix() {
        if (this.mBitmapRect.isEmpty() || this.mDisplayInitRect.isEmpty()) {
            return;
        }
        resetBitmapInitRect();
        resetBitmapRect();
        onBitmapMatrixChanged();
    }

    public final void resetBitmapInitRect() {
        this.mBitmapToDisplayMatrix.reset();
        this.mBitmapToDisplayMatrix.setRectToRect(this.mBitmapRect, this.mDisplayInitRect, Matrix.ScaleToFit.CENTER);
        this.mBitmapDisplayInitRect.set(this.mBitmapRect);
        this.mBitmapToDisplayMatrix.mapRect(this.mBitmapDisplayInitRect);
        this.mDisplayToBitmapMatrix.reset();
        this.mDisplayToBitmapMatrix.setRectToRect(this.mBitmapDisplayInitRect, this.mBitmapRect, Matrix.ScaleToFit.FILL);
        this.mMaxWidth = this.mBitmapDisplayInitRect.width() * 4.0f;
        this.mMaxWidthScale = this.mBitmapDisplayInitRect.width() * 6.0f;
        this.mMinWidthScale = this.mBitmapDisplayInitRect.width() * 0.5f;
    }

    public final void resetBitmapRect() {
        this.mBitmapDisplayRect.set(this.mBitmapDisplayInitRect);
        this.mDisplayInsideRect.set(this.mDisplayRect);
        this.mBitmapDisplayInsideRect.set(this.mBitmapDisplayRect);
    }

    public final void resolvePadding(RectF rectF) {
        rectF.left += this.mTarget.getPaddingLeft();
        rectF.right -= this.mTarget.getPaddingRight();
        rectF.top += this.mTarget.getPaddingTop();
        rectF.bottom -= this.mTarget.getPaddingBottom();
    }

    public void performScale(float f, float f2, float f3) {
        float f4 = f * f;
        this.mCanvasMatrix.postScale(f4, f4, f2, f3);
        this.mScaleFocusX = f2;
        this.mScaleFocusY = f3;
        performCanvasMatrixChange();
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void performTransition(float r9, float r10) {
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
            r8.performCanvasMatrixChange()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.imageview.BitmapGestureParamsHolder.performTransition(float, float):void");
    }

    public void performCanvasMatrixChange() {
        refreshBitmapDisplayRect();
        onCanvasMatrixChange();
    }

    public final void refreshBitmapDisplayRect() {
        this.mCanvasMatrix.mapRect(this.mBitmapDisplayRect, this.mBitmapDisplayInitRect);
        this.mCanvasMatrix.invert(this.mCanvasMatrixInvert);
        this.mCanvasMatrixInvert.mapRect(this.mDisplayInsideRect, this.mDisplayRect);
        this.mBitmapDisplayInsideRect.set(this.mBitmapDisplayRect);
        this.mBitmapDisplayInsideRect.intersect(this.mDisplayRect);
    }

    public void fixMatrix(boolean z) {
        this.mCanvasMatrix.getValues(this.mMatrixValues);
        if (this.mMatrixValues[0] <= 1.0f) {
            countAnimMatrixWhenZoomOut(this.mAnimTargetMatrix);
        } else {
            countAnimMatrixWhenZoomIn(this.mAnimTargetMatrix);
        }
        if (z) {
            this.mMatrixTransition.animMatrix(this.mCanvasMatrix, this.mAnimTargetMatrix);
        } else {
            this.mCanvasMatrix.set(this.mAnimTargetMatrix);
        }
    }

    public void fixMatrixWithAnim() {
        fixMatrix(true);
    }

    public float[] getCanvasMatrixValues() {
        this.mCanvasMatrix.getValues(this.mMatrixValues);
        return this.mMatrixValues;
    }

    public float[] getMatrixValues() {
        Matrix matrix = new Matrix(this.mBitmapToDisplayMatrix);
        matrix.postConcat(this.mCanvasMatrix);
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues;
    }

    public Matrix getMatrix() {
        Matrix matrix = new Matrix(this.mBitmapToDisplayMatrix);
        matrix.postConcat(this.mCanvasMatrix);
        return matrix;
    }

    public void countAnimMatrixWhenZoomOut(Matrix matrix) {
        matrix.reset();
    }

    public void countAnimMatrixWhenZoomIn(Matrix matrix) {
        float f;
        float f2;
        matrix.set(this.mCanvasMatrix);
        this.mRectFTemp.set(this.mBitmapDisplayRect);
        RectF rectF = this.mRectFTemp;
        float width = rectF.width();
        float f3 = this.mMaxWidth;
        if (width > f3) {
            float width2 = f3 / rectF.width();
            matrix.postScale(width2, width2, this.mScaleFocusX, this.mScaleFocusY);
            matrix.mapRect(rectF, this.mBitmapDisplayInitRect);
        }
        float width3 = rectF.width();
        float height = rectF.height();
        float f4 = rectF.left;
        float f5 = rectF.right;
        float f6 = rectF.top;
        float f7 = rectF.bottom;
        float width4 = this.mDisplayRect.width();
        float height2 = this.mDisplayRect.height();
        RectF rectF2 = this.mDisplayRect;
        float f8 = rectF2.left;
        float f9 = rectF2.right;
        float f10 = rectF2.top;
        float f11 = rectF2.bottom;
        float f12 = 0.0f;
        if (f4 > f8) {
            f = width3 > width4 ? -(f4 - f8) : (-(f4 - f8)) + ((width4 - width3) * ((this.mBitmapDisplayInitRect.left - f8) / (rectF2.width() - this.mBitmapDisplayInitRect.width())));
        } else if (f5 < f9) {
            f = width3 > width4 ? f9 - f5 : (f9 - f5) - ((width4 - width3) * (1.0f - ((this.mBitmapDisplayInitRect.left - f8) / (rectF2.width() - this.mBitmapDisplayInitRect.width()))));
        } else {
            f = 0.0f;
        }
        if (f6 > f10) {
            if (height <= height2) {
                float f13 = this.mBitmapDisplayInitRect.top;
                RectF rectF3 = this.mDisplayRect;
                f12 = (-(f6 - f10)) + ((height2 - height) * ((f13 - rectF3.top) / (rectF3.height() - this.mBitmapDisplayInitRect.height())));
            } else {
                f2 = -(f6 - f10);
                matrix.postTranslate(f, f2);
            }
        } else if (f7 < f11) {
            if (height > height2) {
                f12 = f11 - f7;
            } else {
                float f14 = this.mBitmapDisplayInitRect.top;
                RectF rectF4 = this.mDisplayRect;
                f12 = (f11 - f7) - ((height2 - height) * (1.0f - ((f14 - rectF4.top) / (rectF4.height() - this.mBitmapDisplayInitRect.height()))));
            }
        }
        f2 = f12;
        matrix.postTranslate(f, f2);
    }

    public void convertPointToViewPortCoordinate(float[] fArr) {
        this.mCanvasMatrixInvert.mapPoints(fArr);
    }

    public void convertPointToBitmapCoordinate(float[] fArr) {
        convertPointToViewPortCoordinate(fArr);
        this.mDisplayToBitmapMatrix.mapPoints(fArr);
    }

    public void convertPointToBitmapCoordinate(MotionEvent motionEvent, float[] fArr) {
        fArr[0] = motionEvent.getX();
        fArr[1] = motionEvent.getY();
        convertPointToBitmapCoordinate(fArr);
    }

    public float convertDistanceX(float f) {
        return f * (this.mDisplayInsideRect.width() / this.mDisplayRect.width());
    }

    public float convertDistanceY(float f) {
        return f * (this.mDisplayInsideRect.height() / this.mDisplayRect.height());
    }

    public float convertDistanceInBitmap(float f) {
        return Math.signum(f) * this.mDisplayToBitmapMatrix.mapRadius(this.mCanvasMatrixInvert.mapRadius(f));
    }

    public final void onBitmapMatrixChanged() {
        ParamsChangeListener paramsChangeListener = this.mParamsChangeListener;
        if (paramsChangeListener != null) {
            paramsChangeListener.onBitmapMatrixChanged();
        }
    }

    public final void onCanvasMatrixChange() {
        ParamsChangeListener paramsChangeListener = this.mParamsChangeListener;
        if (paramsChangeListener != null) {
            paramsChangeListener.onCanvasMatrixChange();
        }
    }
}
