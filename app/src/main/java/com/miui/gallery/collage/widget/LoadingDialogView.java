package com.miui.gallery.collage.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class LoadingDialogView extends View {
    public ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    public RectF mBitmapRect;
    public Paint mColorPaint;
    public int mDegree;
    public int mLoadingPadding;
    public Bitmap mLoadingProgress;
    public Matrix mMatrix;
    public Paint mPaint;
    public RectF mProgressRect;
    public ValueAnimator mValueAnimator;
    public RectF mViewRect;

    public LoadingDialogView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mMatrix = new Matrix();
        this.mBitmapRect = new RectF();
        this.mViewRect = new RectF();
        this.mProgressRect = new RectF();
        this.mPaint = new Paint(3);
        this.mColorPaint = new Paint(1);
        this.mDegree = 0;
        this.mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.collage.widget.LoadingDialogView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LoadingDialogView.this.mDegree = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                LoadingDialogView.this.invalidate();
            }
        };
        init();
    }

    public void init() {
        this.mColorPaint.setColor(-1);
        this.mColorPaint.setStyle(Paint.Style.FILL);
        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.collage_loading_progress);
        this.mLoadingProgress = decodeResource;
        this.mBitmapRect.set(0.0f, 0.0f, decodeResource.getWidth(), this.mLoadingProgress.getHeight());
        ValueAnimator ofInt = ValueAnimator.ofInt(0, 359);
        this.mValueAnimator = ofInt;
        ofInt.setDuration(800L);
        this.mValueAnimator.setRepeatCount(-1);
        this.mValueAnimator.addUpdateListener(this.mAnimatorUpdateListener);
        this.mValueAnimator.setInterpolator(new LinearInterpolator());
        this.mLoadingPadding = getResources().getDimensionPixelSize(R.dimen.collage_loading_progress_size_padding);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mViewRect.set(0.0f, 0.0f, i, i2);
        this.mProgressRect.set(this.mViewRect);
        RectF rectF = this.mProgressRect;
        int i5 = this.mLoadingPadding;
        rectF.inset(i5, i5);
        this.mMatrix.reset();
        this.mMatrix.setRectToRect(this.mBitmapRect, this.mViewRect, Matrix.ScaleToFit.CENTER);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        canvas.drawOval(this.mViewRect, this.mColorPaint);
        canvas.save();
        canvas.rotate(this.mDegree, this.mViewRect.centerX(), this.mViewRect.centerY());
        canvas.drawBitmap(this.mLoadingProgress, this.mMatrix, this.mPaint);
        canvas.restore();
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mValueAnimator.start();
        DefaultLogger.d("LoadingDialogView", "onAttachedToWindow");
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mValueAnimator.cancel();
        DefaultLogger.d("LoadingDialogView", "onDetachedFromWindow");
    }
}
