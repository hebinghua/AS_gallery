package com.miui.gallery.widget.recyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.Keep;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes3.dex */
public class FastScrollerThumbView implements DrawView {
    public PropertyValuesHolder mAlphaAnimator;
    public ObjectAnimator mAppearAnimator;
    public int mArrowMargin;
    public int mArrowTranslate;
    public Drawable mBottomArrowDrawable;
    public int mBottomArrowHeight;
    public int mBottomArrowWidth;
    public Context mContext;
    public int mDefaultThumbHeight;
    public int mDefaultThumbWidth;
    public ObjectAnimator mHideAnimator;
    public Drawable mHorizontalThumbDrawable;
    public int mHorizontalThumbHeight;
    public int mHorizontalThumbWidth;
    public OnAnimatorListener mOnAnimatorListener;
    public PropertyValuesHolder mScaleXAnimator;
    public PropertyValuesHolder mScaleYAnimator;
    public Drawable mShadowDrawable;
    public int mShadowDrawableHeight;
    public int mShadowDrawableWidth;
    public int mThumbMargin;
    public Drawable mTopArrowDrawable;
    public int mTopArrowHeight;
    public int mTopArrowWidth;
    public Drawable mVerticalThumbDrawable;
    public int mVerticalThumbHeight;
    public int mVerticalThumbWidth;
    public int mViewHeight;
    public int mViewWidth;
    public float mMyScaleX = 1.0f;
    public float mMyScaleY = 1.0f;
    public int mAlpha = 255;

    /* loaded from: classes3.dex */
    public interface OnAnimatorListener {
        void onAnimatorFadeInEnd();

        void onAnimatorFadeOutEnd();

        void onAnimatorUpdate();
    }

    public FastScrollerThumbView(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void setStyle(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R$styleable.CustomFastScroll, R.attr.customFastScrollStyle, i);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            if (index == 0) {
                this.mArrowMargin = obtainStyledAttributes.getDimensionPixelOffset(index, 0);
            } else if (index == 1) {
                this.mBottomArrowDrawable = obtainStyledAttributes.getDrawable(index);
            } else if (index == 3) {
                this.mDefaultThumbHeight = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == 4) {
                this.mDefaultThumbWidth = obtainStyledAttributes.getDimensionPixelSize(index, 0);
            } else if (index == 6) {
                this.mHorizontalThumbDrawable = obtainStyledAttributes.getDrawable(index);
            } else if (index == 15) {
                this.mVerticalThumbDrawable = obtainStyledAttributes.getDrawable(index);
            } else {
                switch (index) {
                    case 9:
                        this.mShadowDrawable = obtainStyledAttributes.getDrawable(index);
                        continue;
                    case 10:
                        this.mShadowDrawableHeight = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                        continue;
                    case 11:
                        this.mShadowDrawableWidth = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                        continue;
                    case 12:
                        this.mThumbMargin = obtainStyledAttributes.getDimensionPixelSize(index, 36);
                        continue;
                    case 13:
                        this.mTopArrowDrawable = obtainStyledAttributes.getDrawable(index);
                        continue;
                }
            }
        }
        obtainStyledAttributes.recycle();
        Drawable drawable = this.mTopArrowDrawable;
        if (drawable != null) {
            this.mTopArrowWidth = drawable.getIntrinsicWidth();
            this.mTopArrowHeight = this.mTopArrowDrawable.getIntrinsicHeight();
        }
        Drawable drawable2 = this.mBottomArrowDrawable;
        if (drawable2 != null) {
            this.mBottomArrowWidth = drawable2.getIntrinsicWidth();
            this.mBottomArrowHeight = this.mBottomArrowDrawable.getIntrinsicHeight();
        }
        Drawable drawable3 = this.mVerticalThumbDrawable;
        if (drawable3 != null) {
            this.mVerticalThumbWidth = Math.max(this.mDefaultThumbWidth, drawable3.getIntrinsicWidth());
            int max = Math.max(this.mDefaultThumbHeight, this.mVerticalThumbDrawable.getIntrinsicHeight());
            this.mVerticalThumbHeight = max;
            this.mViewWidth = this.mVerticalThumbWidth;
            this.mViewHeight = max;
        }
        Drawable drawable4 = this.mHorizontalThumbDrawable;
        if (drawable4 != null) {
            this.mHorizontalThumbWidth = Math.max(this.mDefaultThumbWidth, drawable4.getIntrinsicWidth());
            this.mHorizontalThumbHeight = Math.max(this.mDefaultThumbHeight, this.mHorizontalThumbDrawable.getIntrinsicWidth());
            this.mViewWidth = this.mVerticalThumbWidth;
            this.mViewHeight = this.mVerticalThumbHeight;
        }
        Drawable drawable5 = this.mShadowDrawable;
        if (drawable5 != null) {
            int i3 = this.mShadowDrawableWidth;
            int i4 = (-(i3 - this.mVerticalThumbWidth)) >> 1;
            int i5 = this.mShadowDrawableHeight;
            int i6 = (-(i5 - this.mVerticalThumbHeight)) >> 1;
            drawable5.setBounds(i4, i6, i3 + i4, i5 + i6);
        }
    }

    public boolean isVertical() {
        return this.mVerticalThumbDrawable != null;
    }

    public void showScrollerBarAnimator() {
        cancelShowScrollerBarAnimator();
        if (this.mAppearAnimator == null) {
            this.mScaleXAnimator = PropertyValuesHolder.ofFloat("myScaleX", 0.5f, 1.0f);
            this.mScaleYAnimator = PropertyValuesHolder.ofFloat("myScaleY", 0.5f, 1.0f);
            PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt("myAlpha", 0, 255);
            this.mAlphaAnimator = ofInt;
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, this.mScaleXAnimator, this.mScaleYAnimator, ofInt);
            this.mAppearAnimator = ofPropertyValuesHolder;
            ofPropertyValuesHolder.setDuration(200L);
            this.mAppearAnimator.setInterpolator(new CubicEaseOutInterpolator());
            this.mAppearAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerThumbView.1
                public boolean mCanceled = false;

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!this.mCanceled) {
                        if (FastScrollerThumbView.this.mOnAnimatorListener == null) {
                            return;
                        }
                        FastScrollerThumbView.this.mOnAnimatorListener.onAnimatorFadeInEnd();
                        return;
                    }
                    this.mCanceled = false;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                }
            });
            this.mAppearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerThumbView.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (FastScrollerThumbView.this.mOnAnimatorListener != null) {
                        FastScrollerThumbView.this.mOnAnimatorListener.onAnimatorUpdate();
                    }
                }
            });
        }
        this.mAppearAnimator.start();
    }

    public void cancelShowScrollerBarAnimator() {
        ObjectAnimator objectAnimator = this.mAppearAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    public void hideScrollerBarAnimator(long j) {
        cancelHideScrollerBarAnimator();
        if (this.mHideAnimator == null) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "myAlpha", 255, 0);
            this.mHideAnimator = ofInt;
            ofInt.setDuration(200L);
            this.mHideAnimator.setInterpolator(new CubicEaseOutInterpolator());
            this.mHideAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerThumbView.3
                public boolean mCanceled = false;

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!this.mCanceled) {
                        if (FastScrollerThumbView.this.mOnAnimatorListener == null) {
                            return;
                        }
                        FastScrollerThumbView.this.mOnAnimatorListener.onAnimatorFadeOutEnd();
                        return;
                    }
                    this.mCanceled = false;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                }
            });
            this.mHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerThumbView.4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (FastScrollerThumbView.this.mOnAnimatorListener != null) {
                        FastScrollerThumbView.this.mOnAnimatorListener.onAnimatorUpdate();
                    }
                }
            });
        }
        this.mHideAnimator.setStartDelay(j);
        this.mHideAnimator.start();
    }

    public void cancelHideScrollerBarAnimator() {
        ObjectAnimator objectAnimator = this.mHideAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    public int getThumbMargin() {
        return this.mThumbMargin;
    }

    @Keep
    public int getArrowTranslate() {
        return this.mArrowTranslate;
    }

    @Keep
    public void setArrowTranslate(int i) {
        this.mArrowTranslate = i;
    }

    @Keep
    public float getMyScaleX() {
        return this.mMyScaleX;
    }

    @Keep
    public void setMyScaleX(float f) {
        this.mMyScaleX = f;
    }

    @Keep
    public float getMyScaleY() {
        return this.mMyScaleY;
    }

    @Keep
    public void setMyScaleY(float f) {
        this.mMyScaleY = f;
    }

    @Keep
    public void setMyAlpha(int i) {
        this.mAlpha = i;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void setVisible() {
        this.mAlpha = 255;
        this.mMyScaleX = 1.0f;
        this.mMyScaleY = 1.0f;
        this.mArrowTranslate = 0;
    }

    public void setInvisible() {
        this.mAlpha = 0;
        this.mMyScaleX = 1.0f;
        this.mMyScaleY = 1.0f;
        this.mArrowTranslate = 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public int getViewWidth() {
        return this.mViewWidth;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public int getViewHeight() {
        return this.mViewHeight;
    }

    public void setOnAnimatorListener(OnAnimatorListener onAnimatorListener) {
        this.mOnAnimatorListener = onAnimatorListener;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void draw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        if (this.mVerticalThumbDrawable != null) {
            canvas.save();
            this.mVerticalThumbDrawable.setBounds(0, 0, this.mVerticalThumbWidth, this.mVerticalThumbHeight);
            canvas.scale(this.mMyScaleX, this.mMyScaleY, this.mVerticalThumbWidth / 2, this.mVerticalThumbHeight / 2);
            Drawable drawable = this.mShadowDrawable;
            if (drawable != null) {
                drawable.setAlpha(this.mAlpha);
                this.mShadowDrawable.draw(canvas);
            }
            this.mVerticalThumbDrawable.setAlpha(this.mAlpha);
            this.mVerticalThumbDrawable.draw(canvas);
            canvas.restore();
        }
        Drawable drawable2 = this.mHorizontalThumbDrawable;
        if (drawable2 != null) {
            drawable2.setBounds(0, 0, (int) (this.mHorizontalThumbWidth * this.mMyScaleX), (int) (this.mHorizontalThumbHeight * this.mMyScaleY));
            this.mHorizontalThumbDrawable.setAlpha(this.mAlpha);
            this.mHorizontalThumbDrawable.draw(canvas);
        }
        Drawable drawable3 = this.mTopArrowDrawable;
        if (drawable3 != null && (i3 = this.mViewWidth) > (i4 = this.mTopArrowWidth)) {
            int i5 = this.mViewHeight;
            int i6 = this.mTopArrowHeight;
            if (i5 - i6 > 0) {
                int i7 = (i3 - i4) / 2;
                int i8 = (((i5 - this.mArrowMargin) / 2) - i6) - this.mArrowTranslate;
                drawable3.setBounds(i7, i8, i4 + i7, i6 + i8);
                this.mTopArrowDrawable.setAlpha(this.mAlpha);
                this.mTopArrowDrawable.draw(canvas);
            }
        }
        Drawable drawable4 = this.mBottomArrowDrawable;
        if (drawable4 == null || (i = this.mViewWidth) <= (i2 = this.mBottomArrowWidth)) {
            return;
        }
        int i9 = this.mViewHeight;
        int i10 = this.mBottomArrowHeight;
        if (i9 - i10 <= 0) {
            return;
        }
        int i11 = (i - i2) / 2;
        int i12 = ((i9 + this.mArrowMargin) / 2) + this.mArrowTranslate;
        drawable4.setBounds(i11, i12, i2 + i11, i10 + i12);
        this.mBottomArrowDrawable.setAlpha(this.mAlpha);
        this.mBottomArrowDrawable.draw(canvas);
    }
}
