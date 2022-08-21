package com.miui.gallery.editor.photo.screen.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import com.miui.gallery.R;
import com.miui.gallery.util.BitmapUtils;
import miuix.view.animation.QuarticEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class ScreenEditViewAnimatorHelper {
    public AnimatorState mAnimatorState;
    public Context mContext;
    public Bitmap mPreBitmap;
    public float mRoundRadius;
    public int mStartBottom;
    public int mStartLeft;
    public int mStartRight;
    public int mStartTop;
    public int[] mThumbnailRect = {0, 0, 0, 0};
    public RectF mDstRect = new RectF();
    public Rect mSrcRect = new Rect();
    public Matrix mMatrix = new Matrix();

    /* loaded from: classes2.dex */
    public interface Callback {
        Bitmap getOriginBitmap();

        RectF getShowRect();

        void onAnimationStart();

        void onAnimationUpdate(float f);

        void onInvalidate();
    }

    public static /* synthetic */ void $r8$lambda$n_fpQxngWdmhIjFh1YNBD9SAbIs(ScreenEditViewAnimatorHelper screenEditViewAnimatorHelper, Callback callback, ValueAnimator valueAnimator) {
        screenEditViewAnimatorHelper.lambda$animatorStart$0(callback, valueAnimator);
    }

    public void startEnterAnimation(Context context, Callback callback, int[] iArr) {
        this.mContext = context;
        for (int i = 0; iArr != null && i < iArr.length; i++) {
            this.mThumbnailRect[i] = iArr[i];
        }
        this.mPreBitmap = callback.getOriginBitmap();
        animatorPreStart(callback);
    }

    public final void animatorPreStart(Callback callback) {
        this.mAnimatorState = AnimatorState.ANIMATOR_PRE_START;
        this.mSrcRect.set(0, 0, callback.getOriginBitmap().getWidth(), callback.getOriginBitmap().getHeight());
        int[] iArr = this.mThumbnailRect;
        int i = iArr[0];
        this.mStartLeft = i;
        int i2 = iArr[1];
        this.mStartTop = i2;
        this.mStartRight = i + iArr[2];
        this.mStartBottom = i2 + iArr[3];
        this.mRoundRadius = this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_editor_thumbnail_btn_radius);
        this.mDstRect.set(this.mStartLeft, this.mStartTop, this.mStartRight, this.mStartBottom);
        callback.onInvalidate();
        animatorStart(callback);
    }

    public final void animatorStart(final Callback callback) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(450L);
        ofFloat.setInterpolator(new QuarticEaseInOutInterpolator());
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenEditViewAnimatorHelper.$r8$lambda$n_fpQxngWdmhIjFh1YNBD9SAbIs(ScreenEditViewAnimatorHelper.this, callback, valueAnimator);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenEditViewAnimatorHelper.1
            {
                ScreenEditViewAnimatorHelper.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScreenEditViewAnimatorHelper.this.mAnimatorState = AnimatorState.ANIMATOR_END;
                callback.onInvalidate();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenEditViewAnimatorHelper.this.mAnimatorState = AnimatorState.ANIMATOR_START;
                callback.onAnimationStart();
            }
        });
        ofFloat.start();
    }

    public /* synthetic */ void lambda$animatorStart$0(Callback callback, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mAnimatorState = AnimatorState.ANIMATOR_UPDATE;
        this.mRoundRadius = this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_editor_thumbnail_btn_radius) * (1.0f - floatValue);
        changeRect(callback, floatValue);
        callback.onAnimationUpdate(floatValue);
        callback.onInvalidate();
    }

    public final void changeRect(Callback callback, float f) {
        RectF rectF = this.mDstRect;
        float f2 = callback.getShowRect().left;
        int i = this.mStartLeft;
        rectF.left = Math.round(((f2 - i) * f) + i);
        RectF rectF2 = this.mDstRect;
        float f3 = callback.getShowRect().top;
        int i2 = this.mStartTop;
        rectF2.top = Math.round(((f3 - i2) * f) + i2);
        RectF rectF3 = this.mDstRect;
        float f4 = callback.getShowRect().right;
        int i3 = this.mStartRight;
        rectF3.right = Math.round(((f4 - i3) * f) + i3);
        RectF rectF4 = this.mDstRect;
        float f5 = callback.getShowRect().bottom;
        int i4 = this.mStartBottom;
        rectF4.bottom = Math.round(((f5 - i4) * f) + i4);
    }

    public boolean isAnimatorEnd() {
        return this.mAnimatorState == AnimatorState.ANIMATOR_END;
    }

    public void draw(Canvas canvas) {
        this.mMatrix.reset();
        AnimatorState animatorState = this.mAnimatorState;
        if (animatorState == AnimatorState.ANIMATOR_PRE_START || animatorState == AnimatorState.ANIMATOR_UPDATE) {
            Bitmap bitmap = this.mPreBitmap;
            float f = this.mRoundRadius;
            int[] iArr = this.mThumbnailRect;
            Bitmap roundedCornerBitmap = BitmapUtils.getRoundedCornerBitmap(bitmap, f, iArr[2], iArr[3]);
            if (roundedCornerBitmap == null) {
                return;
            }
            this.mMatrix.setRectToRect(new RectF(0.0f, 0.0f, roundedCornerBitmap.getWidth(), roundedCornerBitmap.getHeight()), this.mDstRect, Matrix.ScaleToFit.CENTER);
            canvas.drawBitmap(roundedCornerBitmap, this.mMatrix, null);
            return;
        }
        this.mMatrix.setRectToRect(new RectF(this.mSrcRect), this.mDstRect, Matrix.ScaleToFit.CENTER);
        canvas.drawBitmap(this.mPreBitmap, this.mMatrix, null);
    }
}
