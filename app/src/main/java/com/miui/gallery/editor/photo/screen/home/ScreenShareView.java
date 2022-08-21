package com.miui.gallery.editor.photo.screen.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.ScreenUtils;
import miuix.view.animation.QuarticEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class ScreenShareView extends View {
    public AnimatorState mAnimatorState;
    public RectF mDstRect;
    public int mEnterFrom;
    public Rect mFirstInSrcRect;
    public GestureDetector mGestureDetector;
    public boolean mIsFirstIn;
    public OnClickShareViewListener mOnClickShareViewListener;
    public Paint mPaint;
    public Matrix mResultMatrix;
    public RectF mResultRect;
    public float mRoundRadius;
    public Bitmap mShareBitmap;
    public Rect mSrcRect;
    public int mThumbnailDstRectStartLeft;
    public int mThumbnailDstRectStartTop;
    public int[] mThumbnailRect;
    public ValueAnimator mValueAnimator;

    /* loaded from: classes2.dex */
    public interface OnClickShareViewListener {
        void onClick();
    }

    public ScreenShareView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mResultMatrix = new Matrix();
        this.mThumbnailRect = new int[4];
        this.mEnterFrom = 0;
        this.mIsFirstIn = true;
        init(context);
    }

    public final void init(Context context) {
        this.mPaint = new Paint(1);
        this.mFirstInSrcRect = new Rect();
        this.mSrcRect = new Rect();
        this.mDstRect = new RectF(0.0f, 0.0f, ScreenUtils.getScreenWidth(), ScreenUtils.getExactScreenHeight((Activity) context));
        this.mResultRect = new RectF();
        this.mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenShareView.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (ScreenShareView.this.isInResultRect(motionEvent.getX(), motionEvent.getY()) && ScreenShareView.this.mOnClickShareViewListener != null) {
                    ScreenShareView.this.mOnClickShareViewListener.onClick();
                }
                return super.onSingleTapUp(motionEvent);
            }
        });
    }

    public void setShareBitmap(Bitmap bitmap, boolean z) {
        this.mShareBitmap = bitmap;
        this.mSrcRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        refreshResultRectF();
        if (z) {
            invalidate();
        }
    }

    public void startShareViewAnimator(ThumbnailAnimatorCallback thumbnailAnimatorCallback) {
        this.mThumbnailDstRectStartLeft = thumbnailAnimatorCallback.getThumbnailStartLeft();
        this.mThumbnailDstRectStartTop = thumbnailAnimatorCallback.getThumbnailStartTop();
        this.mRoundRadius = getResources().getDimensionPixelSize(R.dimen.screen_editor_thumbnail_btn_radius);
        this.mAnimatorState = AnimatorState.ANIMATOR_PRE_START;
        int[] thumbnailRect = thumbnailAnimatorCallback.getThumbnailRect();
        this.mThumbnailRect = thumbnailRect;
        changeTransformRect(0.0f, thumbnailRect);
        thumbnailAnimatorCallback.onPrepareAnimatorStart();
        this.mEnterFrom = 1;
        invalidate();
        startAnimator(thumbnailAnimatorCallback);
    }

    public final void startAnimator(final ThumbnailAnimatorCallback thumbnailAnimatorCallback) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mValueAnimator = ofFloat;
        ofFloat.setDuration(450L);
        this.mValueAnimator.setInterpolator(new QuarticEaseInOutInterpolator());
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenShareView.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                if (thumbnailAnimatorCallback != null) {
                    ScreenShareView.this.mAnimatorState = AnimatorState.ANIMATOR_UPDATE;
                    ScreenShareView screenShareView = ScreenShareView.this;
                    screenShareView.mRoundRadius = screenShareView.getResources().getDimensionPixelSize(R.dimen.screen_editor_thumbnail_btn_radius) * (1.0f - floatValue);
                    ScreenShareView screenShareView2 = ScreenShareView.this;
                    screenShareView2.changeTransformRect(floatValue, screenShareView2.mThumbnailRect);
                    thumbnailAnimatorCallback.onAnimationUpdate(floatValue);
                    ScreenShareView.this.invalidate();
                }
            }
        });
        this.mValueAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenShareView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (thumbnailAnimatorCallback != null) {
                    ScreenShareView.this.mAnimatorState = AnimatorState.ANIMATOR_END;
                    ScreenShareView.this.invalidate();
                }
                ScreenShareView.this.mIsFirstIn = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                if (thumbnailAnimatorCallback != null) {
                    ScreenShareView.this.mAnimatorState = AnimatorState.ANIMATOR_START;
                    thumbnailAnimatorCallback.onAnimationStart();
                }
            }
        });
        postDelayed(new Runnable() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenShareView.4
            @Override // java.lang.Runnable
            public void run() {
                ScreenShareView.this.mValueAnimator.start();
            }
        }, 30L);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        int i5 = this.mEnterFrom;
        if (i5 == 0 || (i5 == 1 && !this.mIsFirstIn)) {
            this.mDstRect.set(0.0f, 0.0f, i, i2);
            refreshResultRectF();
        }
    }

    public final void changeTransformRect(float f, int[] iArr) {
        RectF rectF = this.mDstRect;
        RectF rectF2 = this.mResultRect;
        float f2 = rectF2.left;
        int i = this.mThumbnailDstRectStartLeft;
        rectF.left = (int) (((f2 - i) * f) + i);
        float f3 = rectF2.top;
        int i2 = this.mThumbnailDstRectStartTop;
        rectF.top = (int) (((f3 - i2) * f) + i2);
        rectF.right = (int) (((rectF2.right - (iArr[2] + i)) * f) + i + iArr[2]);
        rectF.bottom = (int) (((rectF2.bottom - (iArr[3] + i2)) * f) + i2 + iArr[3]);
    }

    public final void refreshResultRectF() {
        this.mResultMatrix.reset();
        this.mResultMatrix.setRectToRect(new RectF(this.mSrcRect), this.mDstRect, Matrix.ScaleToFit.CENTER);
        this.mResultRect.set(this.mSrcRect);
        this.mResultMatrix.mapRect(this.mResultRect);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = this.mShareBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        int i = this.mEnterFrom;
        if (i == 0 || (i == 1 && !this.mIsFirstIn)) {
            canvas.drawBitmap(this.mShareBitmap, this.mSrcRect, this.mResultRect, this.mPaint);
            return;
        }
        AnimatorState animatorState = this.mAnimatorState;
        if (animatorState == AnimatorState.ANIMATOR_END) {
            canvas.drawBitmap(this.mShareBitmap, this.mSrcRect, this.mDstRect, this.mPaint);
        } else if (animatorState == AnimatorState.ANIMATOR_PRE_START || animatorState == AnimatorState.ANIMATOR_UPDATE) {
            Bitmap bitmap2 = this.mShareBitmap;
            float f = this.mRoundRadius;
            int[] iArr = this.mThumbnailRect;
            Bitmap roundedCornerBitmap = BitmapUtils.getRoundedCornerBitmap(bitmap2, f, iArr[2], iArr[3]);
            this.mFirstInSrcRect.set(0, 0, roundedCornerBitmap.getWidth(), roundedCornerBitmap.getHeight());
            canvas.drawBitmap(roundedCornerBitmap, this.mFirstInSrcRect, this.mDstRect, this.mPaint);
        } else if (animatorState != AnimatorState.ANIMATOR_START) {
        } else {
            canvas.drawBitmap(this.mShareBitmap, this.mSrcRect, this.mDstRect, this.mPaint);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    public final boolean isInResultRect(float f, float f2) {
        return this.mResultRect.contains(f, f2);
    }

    public void setOnClickShareViewListener(OnClickShareViewListener onClickShareViewListener) {
        this.mOnClickShareViewListener = onClickShareViewListener;
    }
}
