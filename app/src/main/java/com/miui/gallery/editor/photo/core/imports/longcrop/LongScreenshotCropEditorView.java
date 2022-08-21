package com.miui.gallery.editor.photo.core.imports.longcrop;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;
import java.util.Objects;

/* loaded from: classes2.dex */
public class LongScreenshotCropEditorView extends View implements ValueAnimator.AnimatorUpdateListener {
    public LongCropViewAnimatorHelper$Callback mAnimatorCallback;
    public Paint mBitmapPaint;
    public Matrix mBitmapToDisplayMatrix;
    public Rect mBmpRect;
    public Drawable mBorder;
    public RectF mBounds;
    public Bitmap mCorner;
    public int mCornerBarWidth;
    public RectF mCropRect;
    public float mCropYFirst;
    public int mDragLineEdge;
    public int mDragState;
    public int mMinHeight;
    public Drawable mOriginalBmp;
    public Paint mPaint;
    public int mPaintColor;
    public Bitmap mPreviewBmp;
    public Bitmap mScissor;
    public Drawable mShadow;
    public ValueAnimator mShowOriginalAnimator;
    public float mShowOriginalBmp;
    public RectF mShowRect;
    public boolean mShowShadow;
    public float mTouchDownY;

    public void drawChild(Canvas canvas, RectF rectF) {
    }

    public LongScreenshotCropEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBounds = new RectF();
        this.mPaint = new Paint();
        this.mPaintColor = -1;
        this.mShowShadow = true;
        this.mBitmapPaint = new Paint(3);
        this.mBitmapToDisplayMatrix = new Matrix();
        this.mAnimatorCallback = new LongCropViewAnimatorHelper$Callback() { // from class: com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView.1
        };
        this.mDragLineEdge = getResources().getDimensionPixelSize(R.dimen.longscreenshot_crop_drag_edge);
        this.mCornerBarWidth = getResources().getDimensionPixelSize(R.dimen.longscreenshot_crop_cornor_bar_width);
        this.mMinHeight = getResources().getDimensionPixelSize(R.dimen.longscreenshot_crop_min_height);
        this.mPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 0.7f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mShowOriginalAnimator = ofFloat;
        ofFloat.addUpdateListener(this);
        this.mBorder = getResources().getDrawable(R.drawable.long_screenshot_cut_photo_border);
        this.mShadow = getResources().getDrawable(R.drawable.long_screenshot_cut_photo_shadow);
        this.mCorner = BitmapFactory.decodeResource(getResources(), R.drawable.long_screenshot_cut_corner);
        this.mScissor = BitmapFactory.decodeResource(getResources(), R.drawable.long_screenshot_cut_scissor);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mBounds.set(getPaddingLeft(), getPaddingTop(), i - getPaddingRight(), i2 - getPaddingBottom());
        RectF rectF = this.mBounds;
        int i5 = this.mCornerBarWidth;
        rectF.inset(i5, i5);
        refreshBounds();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        Bitmap bitmap;
        float f3;
        float height;
        float f4;
        if (this.mPreviewBmp == null) {
            return;
        }
        this.mPaint.setColor(this.mPaintColor);
        int i = (int) (this.mShowOriginalBmp * 153.0f);
        if (i > 0 && this.mOriginalBmp != null) {
            int i2 = this.mDragState;
            if (i2 == 1) {
                float f5 = this.mCropRect.bottom;
                RectF rectF = this.mShowRect;
                height = ((f5 - rectF.top) / rectF.height()) * this.mOriginalBmp.getBounds().height();
                f4 = this.mCropRect.bottom;
            } else if (i2 == 2) {
                float f6 = this.mCropRect.top;
                RectF rectF2 = this.mShowRect;
                height = ((f6 - rectF2.top) / rectF2.height()) * this.mOriginalBmp.getBounds().height();
                f4 = this.mCropRect.top;
            } else {
                f3 = 0.0f;
                canvas.translate(0.0f, -f3);
                this.mOriginalBmp.setAlpha(i);
                this.mOriginalBmp.draw(canvas);
                canvas.translate(0.0f, f3);
            }
            f3 = height - f4;
            canvas.translate(0.0f, -f3);
            this.mOriginalBmp.setAlpha(i);
            this.mOriginalBmp.draw(canvas);
            canvas.translate(0.0f, f3);
        }
        if (this.mShowShadow) {
            this.mBorder.draw(canvas);
            this.mShadow.draw(canvas);
        }
        this.mPaint.setAlpha((int) (this.mShowOriginalBmp * 178.0f));
        int i3 = this.mDragState;
        if (i3 == 1) {
            canvas.drawRect(0.0f, this.mCropRect.bottom, getWidth(), getHeight(), this.mPaint);
        } else if (i3 == 2) {
            canvas.drawRect(0.0f, 0.0f, getWidth(), this.mCropRect.top, this.mPaint);
        }
        canvas.drawBitmap(this.mPreviewBmp, this.mBitmapToDisplayMatrix, this.mBitmapPaint);
        canvas.save();
        canvas.clipRect(this.mShowRect);
        drawChild(canvas, this.mShowRect);
        canvas.restore();
        if (this.mShowShadow) {
            this.mPaint.setAlpha((int) (((1.0f - this.mShowOriginalBmp) * 77.0f) + 127.0f));
            RectF rectF3 = this.mShowRect;
            canvas.drawRect(rectF3.left, rectF3.top, rectF3.right, this.mCropRect.top, this.mPaint);
            RectF rectF4 = this.mShowRect;
            canvas.drawRect(rectF4.left, this.mCropRect.bottom, rectF4.right, rectF4.bottom, this.mPaint);
        }
        if (i > 0) {
            this.mPaint.setColor(-1996488705);
            this.mPaint.setAlpha(i);
            int i4 = this.mDragState;
            if (i4 == 1) {
                f = this.mCropRect.bottom;
            } else if (i4 == 2) {
                f = this.mCropRect.top;
            } else {
                f2 = 0.0f;
                canvas.drawLine(0.0f, f2, getWidth(), f2, this.mPaint);
                canvas.drawBitmap(this.mScissor, 0.0f, f2 - (bitmap.getHeight() / 2), this.mPaint);
            }
            f2 = f;
            canvas.drawLine(0.0f, f2, getWidth(), f2, this.mPaint);
            canvas.drawBitmap(this.mScissor, 0.0f, f2 - (bitmap.getHeight() / 2), this.mPaint);
        }
        float f7 = this.mShowRect.left;
        int i5 = this.mCornerBarWidth;
        canvas.translate(f7 - i5, this.mCropRect.top - i5);
        canvas.drawBitmap(this.mCorner, 0.0f, 0.0f, (Paint) null);
        canvas.translate(this.mShowRect.width() + (this.mCornerBarWidth * 2), 0.0f);
        canvas.scale(-1.0f, 1.0f);
        canvas.drawBitmap(this.mCorner, 0.0f, 0.0f, (Paint) null);
        canvas.translate(0.0f, this.mCropRect.height() + (this.mCornerBarWidth * 2));
        canvas.scale(1.0f, -1.0f);
        canvas.drawBitmap(this.mCorner, 0.0f, 0.0f, (Paint) null);
        canvas.translate(this.mShowRect.width() + (this.mCornerBarWidth * 2), 0.0f);
        canvas.scale(-1.0f, 1.0f);
        canvas.drawBitmap(this.mCorner, 0.0f, 0.0f, (Paint) null);
    }

    public static void calcFixCenter(int i, int i2, int[] iArr) {
        float f = iArr[0] / i;
        float f2 = iArr[1] / i2;
        if (f > f2) {
            iArr[0] = i;
            iArr[1] = (int) ((iArr[1] / f) + 0.5f);
            return;
        }
        iArr[0] = (int) ((iArr[0] / f2) + 0.5f);
        iArr[1] = i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x000d, code lost:
        if (r0 != 3) goto L7;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            int r0 = r6.getAction()
            r1 = 2
            r2 = 1
            if (r0 == 0) goto L72
            if (r0 == r2) goto L68
            if (r0 == r1) goto L11
            r6 = 3
            if (r0 == r6) goto L68
            goto L9c
        L11:
            float r6 = r6.getY()
            float r0 = r5.mTouchDownY
            float r6 = r6 - r0
            r0 = 1056964608(0x3f000000, float:0.5)
            float r6 = r6 + r0
            float r0 = r5.mCropYFirst
            float r0 = r0 + r6
            int r6 = r5.mDragState
            if (r6 == r2) goto L45
            if (r6 == r1) goto L25
            goto L64
        L25:
            android.graphics.RectF r6 = r5.mShowRect
            float r6 = r6.top
            float r6 = java.lang.Math.max(r0, r6)
            android.graphics.RectF r0 = r5.mCropRect
            float r0 = r0.bottom
            int r1 = r5.mMinHeight
            float r1 = (float) r1
            float r0 = r0 - r1
            float r6 = java.lang.Math.min(r6, r0)
            android.graphics.RectF r0 = r5.mCropRect
            float r1 = r0.left
            float r3 = r0.right
            float r4 = r0.bottom
            r0.set(r1, r6, r3, r4)
            goto L64
        L45:
            android.graphics.RectF r6 = r5.mShowRect
            float r6 = r6.bottom
            float r6 = java.lang.Math.min(r0, r6)
            android.graphics.RectF r0 = r5.mCropRect
            float r0 = r0.top
            int r1 = r5.mMinHeight
            float r1 = (float) r1
            float r0 = r0 + r1
            float r6 = java.lang.Math.max(r6, r0)
            android.graphics.RectF r0 = r5.mCropRect
            float r1 = r0.left
            float r3 = r0.top
            float r4 = r0.right
            r0.set(r1, r3, r4, r6)
        L64:
            r5.invalidate()
            goto L9c
        L68:
            int r6 = r5.mDragState
            if (r6 == 0) goto L9c
            android.animation.ValueAnimator r6 = r5.mShowOriginalAnimator
            r6.reverse()
            goto L9c
        L72:
            float r0 = r6.getY()
            int r0 = r5.detectBeginDragState(r0)
            r5.mDragState = r0
            float r6 = r6.getY()
            r5.mTouchDownY = r6
            int r6 = r5.mDragState
            if (r6 != r2) goto L8d
            android.graphics.RectF r0 = r5.mCropRect
            float r0 = r0.bottom
            r5.mCropYFirst = r0
            goto L95
        L8d:
            if (r6 != r1) goto L95
            android.graphics.RectF r0 = r5.mCropRect
            float r0 = r0.top
            r5.mCropYFirst = r0
        L95:
            if (r6 == 0) goto L9c
            android.animation.ValueAnimator r6 = r5.mShowOriginalAnimator
            r6.start()
        L9c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.longcrop.LongScreenshotCropEditorView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final int detectBeginDragState(float f) {
        if (Math.abs(f - this.mCropRect.top) <= this.mDragLineEdge) {
            return 2;
        }
        return Math.abs(f - this.mCropRect.bottom) <= ((float) this.mDragLineEdge) ? 1 : 0;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mPreviewBmp = bitmap;
        refreshBounds();
    }

    public void setOriginalBitmap(Bitmap bitmap, float f, float f2) {
        this.mOriginalBmp = new LongBitmapDrawable(bitmap);
        Rect rect = new Rect();
        rect.left = 0;
        rect.right = bitmap.getWidth();
        rect.top = Math.round(bitmap.getHeight() * f);
        rect.bottom = Math.round(bitmap.getHeight() * f2);
        this.mOriginalBmp.setBounds(rect);
    }

    public void setBounds(RectF rectF) {
        this.mBounds = rectF;
        refreshBounds();
    }

    public void refreshBounds() {
        if (this.mPreviewBmp == null || this.mBounds == null) {
            return;
        }
        Rect rect = new Rect(0, 0, this.mPreviewBmp.getWidth(), this.mPreviewBmp.getHeight());
        this.mBmpRect = rect;
        int[] iArr = {rect.width(), this.mBmpRect.height()};
        calcFixCenter((int) this.mBounds.width(), (int) this.mBounds.height(), iArr);
        RectF rectF = this.mBounds;
        int width = ((int) rectF.left) + ((((int) rectF.width()) - iArr[0]) / 2);
        RectF rectF2 = this.mBounds;
        int height = ((int) rectF2.top) + ((((int) rectF2.height()) - iArr[1]) / 2);
        RectF rectF3 = new RectF(this.mShowRect);
        this.mShowRect = new RectF(width, height, width + iArr[0], height + iArr[1]);
        RectF rectF4 = this.mCropRect;
        if (rectF4 == null || rectF4.isEmpty() || rectF3.isEmpty()) {
            this.mCropRect = new RectF(this.mShowRect);
        } else {
            Matrix matrix = new Matrix();
            matrix.setRectToRect(rectF3, this.mShowRect, Matrix.ScaleToFit.FILL);
            matrix.mapRect(this.mCropRect);
        }
        this.mBitmapToDisplayMatrix.setRectToRect(new RectF(this.mBmpRect), this.mBounds, Matrix.ScaleToFit.CENTER);
        Rect rect2 = new Rect();
        this.mBorder.getPadding(rect2);
        Drawable drawable = this.mBorder;
        RectF rectF5 = this.mShowRect;
        drawable.setBounds(((int) rectF5.left) - rect2.left, ((int) rectF5.top) - rect2.top, ((int) rectF5.right) + rect2.right, ((int) rectF5.bottom) + rect2.bottom);
        this.mShadow.getPadding(rect2);
        Drawable drawable2 = this.mShadow;
        RectF rectF6 = this.mShowRect;
        drawable2.setBounds(((int) rectF6.left) - rect2.left, ((int) rectF6.top) - rect2.top, ((int) rectF6.right) + rect2.right, ((int) rectF6.bottom) + rect2.bottom);
        invalidate();
    }

    public boolean isEmpty() {
        return this.mCropRect.contains(new RectF(this.mShowRect));
    }

    public Entry export() {
        float f = this.mCropRect.top;
        RectF rectF = this.mShowRect;
        float height = (f - rectF.top) / rectF.height();
        float f2 = this.mCropRect.bottom;
        RectF rectF2 = this.mShowRect;
        return new Entry(height, (f2 - rectF2.top) / rectF2.height());
    }

    public void setPaintColor(int i) {
        this.mPaintColor = i;
    }

    public void setShowShadow(boolean z) {
        this.mShowShadow = z;
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.mShowOriginalBmp = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* loaded from: classes2.dex */
    public static class Entry {
        public float mBottomRatio;
        public float mTopRatio;

        public Entry(float f, float f2) {
            this.mTopRatio = f;
            this.mBottomRatio = f2;
        }

        public Entry() {
            this(0.0f, 1.0f);
        }

        public Bitmap apply(Bitmap bitmap) {
            if (!isModified()) {
                return bitmap;
            }
            int height = (int) ((bitmap.getHeight() * this.mTopRatio) + 0.5f);
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), ((int) ((bitmap.getHeight() * this.mBottomRatio) + 0.5f)) - height, Bitmap.Config.ARGB_8888);
            new Canvas(createBitmap).drawBitmap(bitmap, 0.0f, -height, (Paint) null);
            return createBitmap;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                Entry entry = (Entry) obj;
                return this.mTopRatio == entry.mTopRatio && this.mBottomRatio == entry.mBottomRatio;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Float.valueOf(this.mTopRatio), Float.valueOf(this.mBottomRatio));
        }

        public boolean isModified() {
            return (this.mTopRatio == 0.0f && this.mBottomRatio == 1.0f) ? false : true;
        }
    }
}
