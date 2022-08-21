package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.MatrixUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Random;

/* loaded from: classes2.dex */
public class SlideShowView extends View {
    public boolean isEnter;
    public boolean isEnterItemFinished;
    public AlphaAnimation mAlphaAnim;
    public float mBaseScale;
    public boolean mCanInvalidate;
    public SlideAnimation mCurrentAnim;
    public Bitmap mCurrentBitmap;
    public Matrix mCurrentMatrix;
    public int mEnterIndex;
    public boolean mIsAnimEnable;
    public boolean mIsScaleOnlyMode;
    public Paint mPaint;
    public SlideAnimation mPreAnim;
    public Bitmap mPreBitmap;
    public Matrix mPreMatrix;
    public OnRefreshedListener mRefreshedListener;
    public int mScaleMode;
    public int mSlideDuration;
    public float mTargetHeight;
    public Transformation mTransFormation;

    /* loaded from: classes2.dex */
    public interface OnRefreshedListener {
        void onRefreshed();
    }

    /* renamed from: $r8$lambda$Rh7vhBqccl0UA-2Ua2fIHC3k5Kw */
    public static /* synthetic */ void m1809$r8$lambda$Rh7vhBqccl0UA2Ua2fIHC3k5Kw(SlideShowView slideShowView) {
        slideShowView.refill();
    }

    public SlideShowView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlideShowView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSlideDuration = 3500;
        this.mIsAnimEnable = true;
        this.mCanInvalidate = true;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SlideShowView, i, 0);
        this.mScaleMode = obtainStyledAttributes.getInt(0, 0);
        obtainStyledAttributes.recycle();
        initParams();
    }

    public void setSlideDuration(int i) {
        this.mSlideDuration = Math.max(i, 3500);
    }

    public void setSlideAnimEnable(boolean z) {
        this.mIsAnimEnable = z;
    }

    public void setScaleOnlyMode(boolean z) {
        this.mIsScaleOnlyMode = z;
    }

    public void setRefreshListener(OnRefreshedListener onRefreshedListener) {
        this.mRefreshedListener = onRefreshedListener;
    }

    public final void invalidateView() {
        if (!this.mCanInvalidate) {
            return;
        }
        invalidate();
        OnRefreshedListener onRefreshedListener = this.mRefreshedListener;
        if (onRefreshedListener == null) {
            return;
        }
        onRefreshedListener.onRefreshed();
    }

    public final void initParams() {
        this.mPreMatrix = new Matrix();
        this.mCurrentMatrix = new Matrix();
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setDither(false);
        this.mTransFormation = new Transformation();
        this.mAlphaAnim = new FloatAnimation(0.0f, 1.0f, 1000L);
    }

    public void setTargetHeight(float f) {
        this.mTargetHeight = f;
    }

    public void checkAndNext(Bitmap bitmap, int i, int i2) {
        if (!this.isEnter || i2 != this.mEnterIndex) {
            this.isEnterItemFinished = true;
            next(bitmap, i);
        }
    }

    public void next(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return;
        }
        this.mAlphaAnim.start();
        if (isPreValid()) {
            this.mPreBitmap.recycle();
            this.mPreBitmap = null;
        }
        this.mPreMatrix.set(this.mCurrentMatrix);
        this.mCurrentMatrix.reset();
        this.mPreBitmap = this.mCurrentBitmap;
        this.mPreAnim = this.mCurrentAnim;
        this.mCurrentBitmap = bitmap;
        if (((i / 90) & 1) == 0) {
            this.mCurrentAnim = new SlideAnimation(bitmap.getWidth(), bitmap.getHeight(), i, this.mSlideDuration);
        } else {
            this.mCurrentAnim = new SlideAnimation(bitmap.getHeight(), bitmap.getWidth(), i, this.mSlideDuration);
        }
        this.mCurrentAnim.start();
        invalidateView();
    }

    public void setCanInvalidate(boolean z) {
        this.mCanInvalidate = z;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        long drawingTime = getDrawingTime();
        this.mTransFormation.clear();
        boolean transformation = this.mAlphaAnim.getTransformation(drawingTime, this.mTransFormation);
        float alpha = this.mPreBitmap == null ? 1.0f : this.mTransFormation.getAlpha();
        if (!this.mIsAnimEnable) {
            Bitmap bitmap = null;
            if (isPreValid()) {
                bitmap = this.mPreBitmap;
            } else if (isCurValid()) {
                bitmap = this.mCurrentBitmap;
            }
            if (bitmap == null) {
                return;
            }
            int width = getWidth();
            int height = getHeight();
            int width2 = bitmap.getWidth();
            int height2 = bitmap.getHeight();
            this.mCurrentMatrix.reset();
            this.mCurrentMatrix.setTranslate(Math.round((width - width2) * 0.5f), Math.round((height - height2) * 0.5f));
            canvas.save();
            canvas.concat(this.mCurrentMatrix);
            this.mPaint.setAlpha(255);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mPaint);
            canvas.restore();
            invalidateView();
            return;
        }
        if (isPreValid() && alpha < 1.0f) {
            this.mTransFormation.clear();
            transformation |= this.mPreAnim.getTransformation(drawingTime, this.mTransFormation);
            canvas.save();
            canvas.concat(this.mTransFormation.getMatrix());
            this.mPaint.setAlpha((int) ((1.0f - alpha) * 255.0f));
            canvas.drawBitmap(this.mPreBitmap, this.mPreMatrix, this.mPaint);
            canvas.restore();
        }
        if (isCurValid() && alpha > 0.0f) {
            this.mTransFormation.clear();
            transformation |= this.mCurrentAnim.getTransformation(drawingTime, this.mTransFormation);
            canvas.save();
            canvas.concat(this.mTransFormation.getMatrix());
            this.mPaint.setAlpha((int) (alpha * 255.0f));
            canvas.drawBitmap(this.mCurrentBitmap, this.mCurrentMatrix, this.mPaint);
            canvas.restore();
        }
        if (!transformation) {
            return;
        }
        invalidateView();
    }

    public final boolean isPreValid() {
        Bitmap bitmap = this.mPreBitmap;
        return bitmap != null && !bitmap.isRecycled();
    }

    public final boolean isCurValid() {
        Bitmap bitmap = this.mCurrentBitmap;
        return bitmap != null && !bitmap.isRecycled();
    }

    public void stop() {
        AlphaAnimation alphaAnimation = this.mAlphaAnim;
        if (alphaAnimation != null) {
            alphaAnimation.cancel();
        }
        SlideAnimation slideAnimation = this.mPreAnim;
        if (slideAnimation != null) {
            slideAnimation.cancel();
        }
        SlideAnimation slideAnimation2 = this.mCurrentAnim;
        if (slideAnimation2 != null) {
            slideAnimation2.cancel();
        }
    }

    public void release() {
        if (isPreValid()) {
            this.mPreBitmap.recycle();
            this.mPreBitmap = null;
        }
        if (isCurValid()) {
            this.mCurrentBitmap.recycle();
            this.mCurrentBitmap = null;
        }
        AlphaAnimation alphaAnimation = this.mAlphaAnim;
        if (alphaAnimation != null) {
            alphaAnimation.cancel();
        }
        SlideAnimation slideAnimation = this.mPreAnim;
        if (slideAnimation != null) {
            slideAnimation.cancel();
        }
        SlideAnimation slideAnimation2 = this.mCurrentAnim;
        if (slideAnimation2 != null) {
            slideAnimation2.cancel();
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        post(new Runnable() { // from class: com.miui.gallery.widget.SlideShowView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SlideShowView.m1809$r8$lambda$Rh7vhBqccl0UA2Ua2fIHC3k5Kw(SlideShowView.this);
            }
        });
    }

    public final void refill() {
        if (isPreValid()) {
            this.mPreAnim = new SlideAnimation(this.mPreBitmap.getWidth(), this.mPreBitmap.getHeight(), this.mPreAnim.mRotation, this.mSlideDuration);
        }
        if (isCurValid()) {
            this.mCurrentAnim = new SlideAnimation(this.mCurrentBitmap.getWidth(), this.mCurrentBitmap.getHeight(), this.mCurrentAnim.mRotation, this.mSlideDuration);
        }
    }

    public void scaleBitmap(int i, int i2, int i3, int i4, int i5, int i6) {
        if (this.mCurrentBitmap == null) {
            return;
        }
        if (this.mCurrentMatrix == null) {
            this.mCurrentMatrix = new Matrix();
        }
        this.mCurrentMatrix.reset();
        if (this.isEnter) {
            float f = ((i3 - i) * 1.0f) / i6;
            this.mBaseScale = f;
            this.mCurrentMatrix.postScale(f, f, 0.0f, 0.0f);
            return;
        }
        float f2 = (i4 - i2) * 1.0f;
        float f3 = this.mTargetHeight;
        float f4 = (f2 / f3) - 1.0f;
        if (f4 > 0.0f) {
            boolean z = this.isEnterItemFinished;
            float f5 = z ? f2 / f3 : this.mBaseScale + f4;
            this.mCurrentMatrix.postScale(f5, f5, z ? ((i3 - i) * 1.0f) / 2.0f : 0.0f, 0.0f);
            float scale = MatrixUtil.getScale(this.mCurrentMatrix);
            float scale2 = MatrixUtil.getScale(this.mPreMatrix);
            if (!this.isEnterItemFinished || scale < scale2) {
                return;
            }
            if (this.mPreMatrix == null) {
                this.mPreMatrix = new Matrix();
            }
            this.mPreMatrix.set(this.mCurrentMatrix);
            return;
        }
        float f6 = this.isEnterItemFinished ? f2 / f3 : this.mBaseScale;
        this.mCurrentMatrix.postScale(f6, f6, 0.0f, 0.0f);
    }

    public void setIsEnter(boolean z) {
        this.isEnter = z;
    }

    public void setEnterIndex(int i) {
        this.mEnterIndex = i;
    }

    /* loaded from: classes2.dex */
    public static class FloatAnimation extends AlphaAnimation {
        public FloatAnimation(float f, float f2, long j) {
            super(f, f2);
            setDuration(j);
            setInterpolator(new LinearInterpolator());
        }
    }

    /* loaded from: classes2.dex */
    public class SlideAnimation extends Animation {
        public Matrix mBaseMatrix;
        public PointF mMovingVector;
        public int mRotation;
        public PointF mScalePoint;

        public SlideAnimation(int i, int i2, int i3, long j) {
            RectF rectF;
            SlideShowView.this = r8;
            DefaultLogger.d("SlideShowView", "width=%d, height=%d, rotation=%d, duration=%d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Long.valueOf(j));
            this.mRotation = i3;
            Random random = new Random();
            float f = i;
            float f2 = i2;
            this.mMovingVector = new PointF(f * 0.2f * (random.nextFloat() - 0.5f), 0.2f * f2 * (random.nextFloat() - 0.5f));
            RectF rectF2 = new RectF(0.0f, 0.0f, r8.getWidth(), r8.getHeight());
            if (r8.mScaleMode == 1) {
                if (i2 == 0 || i == 0 || r8.getWidth() == 0 || r8.getHeight() == 0) {
                    rectF = new RectF(0.0f, 0.0f, f, f2);
                } else {
                    float height = r8.getHeight() / r8.getWidth();
                    if (f2 / f > height) {
                        float f3 = height * f;
                        rectF = new RectF(0.0f, (f2 - f3) * 0.5f, f, (f2 + f3) * 0.5f);
                    } else {
                        float f4 = f2 / height;
                        rectF = new RectF((f - f4) * 0.5f, 0.0f, (f + f4) * 0.5f, f2);
                    }
                }
            } else {
                rectF = new RectF(0.0f, 0.0f, f, f2);
            }
            Matrix matrix = new Matrix();
            this.mBaseMatrix = matrix;
            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
            setDuration(j);
            setInterpolator(new LinearInterpolator());
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f, Transformation transformation) {
            float width;
            float height;
            float f2;
            float f3;
            float f4 = 0.0f;
            if (SlideShowView.this.mIsScaleOnlyMode) {
                if (this.mScalePoint == null) {
                    Random random = new Random();
                    int i = SlideShowView.this.isEnter ? 1 : 2;
                    this.mScalePoint = new PointF(random.nextInt(i) * SlideShowView.this.getWidth(), random.nextInt(i) * SlideShowView.this.getHeight());
                }
                PointF pointF = this.mScalePoint;
                width = pointF.x;
                height = pointF.y;
                f3 = (f * 0.2f) + 1.0f;
                f2 = 0.0f;
            } else {
                width = SlideShowView.this.getWidth() / 2.0f;
                height = SlideShowView.this.getHeight() / 2.0f;
                PointF pointF2 = this.mMovingVector;
                float f5 = pointF2.x * f;
                f2 = f * pointF2.y;
                f3 = (0.2f * f) + 1.0f;
                f4 = f5;
            }
            if (this.mRotation > 0) {
                transformation.getMatrix().setRotate(this.mRotation, width, height);
            }
            transformation.getMatrix().postConcat(this.mBaseMatrix);
            transformation.getMatrix().postTranslate(f4, f2);
            transformation.getMatrix().postScale(f3, f3, width, height);
        }
    }
}
