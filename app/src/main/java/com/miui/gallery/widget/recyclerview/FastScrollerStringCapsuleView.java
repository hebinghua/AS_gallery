package com.miui.gallery.widget.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.adapter.itemmodel.FastScrollerStringCapsuleModel;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsule;
import com.miui.gallery.widget.recyclerview.transition.PhysicBasedInterpolator;

/* loaded from: classes3.dex */
public class FastScrollerStringCapsuleView extends FastScrollerCapsule {
    public int mAlpha;
    public Drawable mBackground;
    public int mBackgroundBorderColor;
    public int mBackgroundBorderColorAlpha;
    public int mBackgroundBorderWidth;
    public int mBackgroundColor;
    public int mBackgroundColorAlpha;
    public int mBackgroundHeight;
    public float mBackgroundRadius;
    public int mBackgroundWidth;
    public int mBottomMargin;
    public String mContent;
    public int mContentColor;
    public Rect mContentRect;
    public int mContentTextSize;
    public int mDefaultTimeCapsuleHeight;
    public int mDefaultTimeCapsuleWidth;
    public int mEndMargin;
    public AnimatorSet mHideAnimator;
    public ObjectAnimator mHideByAlphaAnimator;
    public ObjectAnimator mHideByScaleXAnimator;
    public ObjectAnimator mHideByScaleYAnimator;
    public ObjectAnimator mHideByTranslateAnimator;
    public boolean mIsInRight;
    public int mLocationAlpha;
    public int mMaxWidth;
    public FastScrollerCapsule.OnAnimatorListener mOnAnimatorListener;
    public FastScrollerCapsule.OnLocationChangedListener mOnLocationChangedListener;
    public Paint mPaint;
    public float mRadius;
    public float mScaleX;
    public float mScaleY;
    public Drawable mShadowDrawable;
    public AnimatorSet mShowAnimator;
    public ObjectAnimator mShowByAlphaAnimator;
    public ObjectAnimator mShowByScaleXAnimator;
    public ObjectAnimator mShowByScaleYAnimator;
    public ObjectAnimator mShowByTranslateAnimator;
    public int mStartMargin;
    public int mTopMargin;
    public int mTranslationX;
    public int mViewHeight;
    public int mViewWidth;

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public boolean getIsShowLocation() {
        return false;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void hideLocationByAnimation() {
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void setIsShowLocation(boolean z) {
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void showLocationByAnimation() {
    }

    public FastScrollerStringCapsuleView(Context context) {
        super(context);
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mTranslationX = 0;
        this.mAlpha = 255;
        this.mIsInRight = true;
        this.mDefaultTimeCapsuleWidth = 0;
        this.mDefaultTimeCapsuleHeight = 0;
        this.mPaint = new Paint();
        this.mMaxWidth = 440;
        this.mLocationAlpha = 255;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void setStyle(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R$styleable.CustomFastCapsule, R.attr.customFastScrollCapsuleStyle, i);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            if (index == 2) {
                this.mBackground = obtainStyledAttributes.getDrawable(index);
            } else if (index == 24) {
                this.mCapsuleMarginToThumb = (int) obtainStyledAttributes.getDimension(index, 25.0f);
            } else if (index == 7) {
                this.mBottomMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
            } else if (index == 8) {
                this.mEndMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
            } else {
                switch (index) {
                    case 10:
                        this.mShadowDrawable = obtainStyledAttributes.getDrawable(index);
                        continue;
                    case 11:
                        this.mStartMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                        continue;
                    case 12:
                        this.mContentColor = obtainStyledAttributes.getInteger(index, 0);
                        continue;
                    case 13:
                        this.mContentTextSize = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                        continue;
                    case 14:
                        this.mTopMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                        continue;
                }
            }
        }
        obtainStyledAttributes.recycle();
        int i3 = this.mDefaultTimeCapsuleWidth;
        this.mViewWidth = i3;
        this.mViewHeight = this.mDefaultTimeCapsuleHeight;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            this.mViewWidth = Math.max(i3, drawable.getIntrinsicWidth());
            this.mViewHeight = Math.max(this.mDefaultTimeCapsuleHeight, this.mBackground.getIntrinsicHeight());
        }
        this.mPaint.setAntiAlias(true);
        this.mContentRect = new Rect();
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void showCapsuleByAnimator() {
        cancelShowCapsule();
        if (this.mShowByScaleXAnimator == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "scaleX", 0.38f, 1.0f);
            this.mShowByScaleXAnimator = ofFloat;
            ofFloat.setDuration(450L);
            this.mShowByScaleXAnimator.setInterpolator(new PhysicBasedInterpolator(0.7f, 0.666f));
        }
        if (this.mShowByScaleYAnimator == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "scaleY", 0.5f, 1.0f);
            this.mShowByScaleYAnimator = ofFloat2;
            ofFloat2.setDuration(450L);
            this.mShowByScaleYAnimator.setInterpolator(new PhysicBasedInterpolator(0.7f, 0.666f));
        }
        if (this.mShowByTranslateAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mShowByTranslateAnimator = objectAnimator;
            objectAnimator.setTarget(this);
            this.mShowByTranslateAnimator.setPropertyName("translationX");
            this.mShowByTranslateAnimator.setDuration(450L);
            this.mShowByTranslateAnimator.setInterpolator(new PhysicBasedInterpolator(0.7f, 0.666f));
            this.mShowByTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerStringCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener != null) {
                        onAnimatorListener.onTimeCapsuleAnimatorUpdate();
                    }
                }
            });
            this.mShowByTranslateAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerStringCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener != null) {
                        onAnimatorListener.onTimeCapsuleAnimatorFadeInStart();
                    }
                    super.onAnimationStart(animator);
                }
            });
        }
        if (this.mIsInRight) {
            this.mShowByTranslateAnimator.setIntValues(96, 0);
        } else {
            this.mShowByTranslateAnimator.setIntValues(-96, 0);
        }
        if (this.mShowByAlphaAnimator == null) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "alpha", 0, 255);
            this.mShowByAlphaAnimator = ofInt;
            ofInt.setDuration(150L);
            this.mShowByAlphaAnimator.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.8f));
        }
        if (this.mShowAnimator == null) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mShowAnimator = animatorSet;
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView.3
                public boolean mCanceled = false;

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (this.mCanceled) {
                        this.mCanceled = false;
                        return;
                    }
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerStringCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener == null) {
                        return;
                    }
                    onAnimatorListener.onTimeCapsuleAnimatorFadeInEnd();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                }
            });
            this.mShowAnimator.play(this.mShowByTranslateAnimator).with(this.mShowByScaleXAnimator).with(this.mShowByScaleYAnimator).with(this.mShowByAlphaAnimator);
        }
        this.mShowAnimator.start();
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void hideCapsuleByAnimator(long j) {
        cancelHideCapsule();
        if (this.mHideByScaleXAnimator == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 0.38f);
            this.mHideByScaleXAnimator = ofFloat;
            ofFloat.setDuration(450L);
            this.mHideByScaleXAnimator.setInterpolator(new PhysicBasedInterpolator(0.7f, 0.666f));
        }
        if (this.mHideByScaleYAnimator == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 0.5f);
            this.mHideByScaleYAnimator = ofFloat2;
            ofFloat2.setDuration(450L);
            this.mHideByScaleYAnimator.setInterpolator(new PhysicBasedInterpolator(0.7f, 0.666f));
        }
        if (this.mHideByTranslateAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mHideByTranslateAnimator = objectAnimator;
            objectAnimator.setTarget(this);
            this.mHideByTranslateAnimator.setPropertyName("translationX");
            this.mHideByTranslateAnimator.setDuration(450L);
            this.mHideByTranslateAnimator.setInterpolator(new PhysicBasedInterpolator(0.7f, 0.666f));
            this.mHideByTranslateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView.4
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerStringCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener != null) {
                        onAnimatorListener.onTimeCapsuleAnimatorUpdate();
                    }
                }
            });
            this.mHideByTranslateAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerStringCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener != null) {
                        onAnimatorListener.onTimeCapsuleAnimatorFadeOutStart();
                    }
                    super.onAnimationStart(animator);
                }
            });
        }
        if (this.mIsInRight) {
            this.mHideByTranslateAnimator.setIntValues(0, 96);
        } else {
            this.mHideByTranslateAnimator.setIntValues(0, -96);
        }
        if (this.mHideByAlphaAnimator == null) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "alpha", 255, 0);
            this.mHideByAlphaAnimator = ofInt;
            ofInt.setDuration(150L);
            this.mHideByAlphaAnimator.setInterpolator(new PhysicBasedInterpolator(0.9f, 0.8f));
        }
        if (this.mHideAnimator == null) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mHideAnimator = animatorSet;
            animatorSet.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView.6
                public boolean mCanceled = false;

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (this.mCanceled) {
                        this.mCanceled = false;
                        return;
                    }
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerStringCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener == null) {
                        return;
                    }
                    onAnimatorListener.onTimeCapsuleAnimatorFadeOutEnd();
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                }
            });
            this.mHideAnimator.play(this.mHideByTranslateAnimator).with(this.mHideByScaleXAnimator).with(this.mHideByScaleYAnimator).with(this.mHideByAlphaAnimator);
        }
        this.mHideAnimator.setStartDelay(j);
        this.mHideAnimator.start();
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void hideCapsule() {
        setInvisible();
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void cancelShowCapsule() {
        AnimatorSet animatorSet = this.mShowAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void cancelHideCapsule() {
        AnimatorSet animatorSet = this.mHideAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void setOnAnimatorListener(FastScrollerCapsule.OnAnimatorListener onAnimatorListener) {
        this.mOnAnimatorListener = onAnimatorListener;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void setOnLocationChangedListener(FastScrollerCapsule.OnLocationChangedListener onLocationChangedListener) {
        this.mOnLocationChangedListener = onLocationChangedListener;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public int getViewWidth() {
        return this.mViewWidth;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public int getViewHeight() {
        return this.mViewHeight;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void setIsInRight(boolean z) {
        this.mIsInRight = z;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void setContent(FastScrollerCapsuleContentProvider fastScrollerCapsuleContentProvider) {
        String content = ((FastScrollerStringCapsuleModel) fastScrollerCapsuleContentProvider).getContent();
        if (this.mContent == null || !TextUtils.isEmpty(content)) {
            this.mContent = content;
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void setVisible() {
        this.mAlpha = 255;
        this.mLocationAlpha = 255;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mTranslationX = 0;
    }

    public void setInvisible() {
        this.mAlpha = 0;
        this.mLocationAlpha = 0;
        this.mScaleX = 1.0f;
        this.mScaleY = 1.0f;
        this.mTranslationX = 0;
        this.mContent = null;
    }

    @Keep
    public void setScaleX(float f) {
        this.mScaleX = f;
    }

    @Keep
    public float getScaleX() {
        return this.mScaleX;
    }

    @Keep
    public void setScaleY(float f) {
        this.mScaleY = f;
    }

    @Keep
    public float getScaleY() {
        return this.mScaleY;
    }

    @Keep
    public void setTranslationX(int i) {
        this.mTranslationX = i;
    }

    @Keep
    public void setAlpha(int i) {
        this.mAlpha = i;
        this.mLocationAlpha = i;
    }

    @Keep
    public int getTranslationX() {
        return this.mTranslationX;
    }

    @Keep
    public int getAlpha() {
        return this.mAlpha;
    }

    @Keep
    public void setRadius(float f) {
        this.mRadius = f;
    }

    @Keep
    public float getRadius() {
        return this.mRadius;
    }

    @Override // com.miui.gallery.widget.recyclerview.DrawView
    public void draw(Canvas canvas) {
        if (!TextUtils.isEmpty(this.mContent)) {
            Drawable drawable = this.mBackground;
            if (drawable != null) {
                int i = this.mViewWidth;
                int i2 = (int) (i * this.mScaleX);
                int i3 = i - i2;
                int i4 = this.mViewHeight;
                int i5 = (int) (i4 * this.mScaleY);
                int i6 = (i4 - i5) >> 1;
                int i7 = (int) (i2 * 1.35f);
                int i8 = (int) (i5 * 1.45f);
                int i9 = (-(i7 - i2)) >> 1;
                int i10 = (-(i8 - i5)) >> 1;
                if (this.mIsInRight) {
                    int i11 = this.mTranslationX;
                    drawable.setBounds(i3 + i11, 0, i + i11, i5);
                } else {
                    int i12 = this.mTranslationX;
                    drawable.setBounds(i12, 0, i2 + i12, i5);
                }
                this.mBackground.setAlpha(this.mAlpha);
                canvas.save();
                canvas.translate(0.0f, i6);
                Drawable drawable2 = this.mShadowDrawable;
                if (drawable2 != null) {
                    if (this.mIsInRight) {
                        int i13 = this.mTranslationX;
                        drawable2.setBounds(i13 + i9, i10, i13 + i9 + i7, i8 + i10);
                    } else {
                        int i14 = this.mTranslationX;
                        drawable2.setBounds(i14, i10, i7 + i14, i8 + i10);
                    }
                    this.mShadowDrawable.setAlpha(this.mAlpha);
                    this.mShadowDrawable.draw(canvas);
                }
                this.mBackground.draw(canvas);
                canvas.restore();
            }
            canvas.save();
            this.mPaint.setColor(this.mContentColor);
            this.mPaint.setAlpha(this.mAlpha);
            this.mPaint.setTextSize(this.mContentTextSize);
            Paint paint = this.mPaint;
            String str = this.mContent;
            paint.getTextBounds(str, 0, str.length(), this.mContentRect);
            Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
            int i15 = this.mStartMargin;
            canvas.drawText(this.mContent, this.mTranslationX + i15, (int) (this.mTopMargin - fontMetrics.top), this.mPaint);
            this.mViewWidth = i15 + this.mContentRect.width() + this.mEndMargin;
            this.mViewHeight = (int) ((this.mTopMargin - fontMetrics.top) + fontMetrics.bottom + this.mBottomMargin);
            canvas.restore();
        }
    }
}
