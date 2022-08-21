package com.miui.gallery.widget.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.adapter.itemmodel.FastScrollerTimeCapsuleModel;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.widget.recyclerview.FastScrollerCapsule;
import com.miui.gallery.widget.recyclerview.transition.PhysicBasedInterpolator;

/* loaded from: classes3.dex */
public class FastScrollerTimeCapsuleView extends FastScrollerStringCapsuleView {
    public int mAnimationState;
    public RectF mBackgroundRect;
    public String mDate;
    public int mDateColor;
    public String mDateContent;
    public Rect mDateRect;
    public int mDateTextMarginBottom;
    public int mDateTextSize;
    public PhysicBasedInterpolator mDefaultInterpolator;
    public AnimatorSet mHideLocationAnimator;
    public ObjectAnimator mHideLocationByAlphaAnimator;
    public ObjectAnimator mHideLocationByRadiusAnimator;
    public ObjectAnimator mHideLocationByScaleYAnimator;
    public boolean mIsLocationAnimating;
    public boolean mIsShowLocation;
    public int mLargeBackgroundHeight;
    public float mLargeBackgroundRadius;
    public String mLastLocation;
    public String mLocation;
    public int mLocationColor;
    public Rect mLocationRect;
    public TextPaint mLocationTextPaint;
    public int mLocationTextSize;
    public int mMaxLocationLength;
    public String mMonth;
    public AnimatorSet mShowLocationAnimator;
    public ObjectAnimator mShowLocationByAlphaAnimator;
    public ObjectAnimator mShowLocationByRadiusAnimator;
    public ObjectAnimator mShowLocationByScaleYAnimator;
    public long mTime;
    public String mYear;

    public FastScrollerTimeCapsuleView(Context context) {
        super(context);
        this.mLocationTextPaint = new TextPaint();
        this.mIsShowLocation = true;
        this.mIsLocationAnimating = false;
        this.mDefaultInterpolator = new PhysicBasedInterpolator(0.9f, 0.857f);
        this.mAnimationState = 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView, com.miui.gallery.widget.recyclerview.DrawView
    public void setStyle(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(null, R$styleable.CustomFastCapsule, R.attr.customFastScrollCapsuleStyle, i);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i2 = 0; i2 < indexCount; i2++) {
            int index = obtainStyledAttributes.getIndex(i2);
            switch (index) {
                case 0:
                    this.mBackgroundBorderColor = obtainStyledAttributes.getInteger(index, 0);
                    break;
                case 1:
                    this.mBackgroundBorderWidth = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 3:
                    this.mBackgroundHeight = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 4:
                    this.mBackgroundRadius = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 5:
                    this.mBackgroundWidth = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 6:
                    this.mBackgroundColor = obtainStyledAttributes.getInteger(index, 0);
                    break;
                case 7:
                    this.mBottomMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 8:
                    this.mEndMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 9:
                    this.mMaxWidth = obtainStyledAttributes.getDimensionPixelSize(index, 0);
                    break;
                case 10:
                    this.mShadowDrawable = obtainStyledAttributes.getDrawable(index);
                    break;
                case 11:
                    this.mStartMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 14:
                    this.mTopMargin = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 15:
                    this.mDateColor = obtainStyledAttributes.getInteger(index, 0);
                    break;
                case 16:
                    this.mDateTextMarginBottom = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 19:
                    this.mDateTextSize = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 20:
                    this.mLargeBackgroundHeight = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 21:
                    this.mLargeBackgroundRadius = obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 22:
                    this.mLocationColor = obtainStyledAttributes.getInteger(index, 0);
                    break;
                case 23:
                    this.mLocationTextSize = (int) obtainStyledAttributes.getDimension(index, 0.0f);
                    break;
                case 24:
                    this.mCapsuleMarginToThumb = (int) obtainStyledAttributes.getDimension(index, 25.0f);
                    break;
            }
        }
        obtainStyledAttributes.recycle();
        this.mViewWidth = Math.max(this.mDefaultTimeCapsuleWidth, this.mBackgroundWidth);
        this.mViewHeight = Math.max(this.mDefaultTimeCapsuleHeight, this.mBackgroundHeight);
        this.mBackgroundColorAlpha = Color.alpha(this.mBackgroundColor);
        this.mBackgroundBorderColorAlpha = Color.alpha(this.mBackgroundBorderColor);
        this.mRadius = this.mBackgroundRadius;
        this.mPaint.setAntiAlias(true);
        this.mLocationRect = new Rect();
        this.mDateRect = new Rect();
        this.mBackgroundRect = new RectF(0.0f, 0.0f, this.mViewWidth, this.mViewHeight);
        this.mLocationTextPaint.setAntiAlias(true);
        this.mLocationTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        this.mLocationTextPaint.setColor(this.mLocationColor);
        this.mLocationTextPaint.setTextSize(this.mLocationTextSize);
        this.mMaxLocationLength = (this.mMaxWidth - this.mStartMargin) - this.mEndMargin;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView, com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void setContent(FastScrollerCapsuleContentProvider fastScrollerCapsuleContentProvider) {
        FastScrollerTimeCapsuleModel fastScrollerTimeCapsuleModel = (FastScrollerTimeCapsuleModel) fastScrollerCapsuleContentProvider;
        long longValue = fastScrollerTimeCapsuleModel.getContent().longValue();
        if (Math.abs(this.mTime - longValue) > 0.1d || this.mYear == null || this.mMonth == null || this.mDate == null) {
            this.mTime = longValue;
            this.mYear = GalleryDateUtils.formatRelativeOnlyYear(longValue);
            this.mMonth = GalleryDateUtils.formatRelativeOnlyMonth(this.mTime);
            this.mDate = GalleryDateUtils.formatRelativeOnlyDate(this.mTime);
            String str = this.mYear + this.mMonth + this.mDate;
            if (!TextUtils.equals(this.mDateContent, str)) {
                this.mDateContent = str;
            }
        }
        String location = fastScrollerTimeCapsuleModel.getLocation();
        if (!TextUtils.equals(location, this.mLastLocation)) {
            this.mLastLocation = location;
            if (!TextUtils.isEmpty(location)) {
                String generateTitleLine = LocationManager.getInstance().generateTitleLine(location);
                float measureText = this.mLocationTextPaint.measureText(generateTitleLine);
                int i = this.mMaxLocationLength;
                if (measureText > i) {
                    this.mLocation = TextUtils.ellipsize(generateTitleLine, this.mLocationTextPaint, i, TextUtils.TruncateAt.END).toString();
                } else {
                    this.mLocation = generateTitleLine;
                }
                FastScrollerCapsule.OnLocationChangedListener onLocationChangedListener = this.mOnLocationChangedListener;
                if (onLocationChangedListener == null) {
                    return;
                }
                onLocationChangedListener.onTimeCapsuleShowLocation();
                return;
            }
            this.mLocation = location;
            FastScrollerCapsule.OnLocationChangedListener onLocationChangedListener2 = this.mOnLocationChangedListener;
            if (onLocationChangedListener2 == null) {
                return;
            }
            onLocationChangedListener2.onTimeCapsuleHideLocation();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView, com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void showLocationByAnimation() {
        if (!this.mIsShowLocation) {
            return;
        }
        int i = this.mAnimationState;
        if (i == 0) {
            showLocationAnimation();
        } else if (i != 1) {
        } else {
            cancelHideLocationAnimation();
            showLocationAnimation();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView, com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void hideLocationByAnimation() {
        int i = this.mAnimationState;
        if (i == 2) {
            hideLocationAnimation();
        } else if (i != 3) {
        } else {
            cancelShowLocationAnimation();
            hideLocationAnimation();
        }
    }

    public final void showLocationAnimation() {
        cancelShowLocationAnimation();
        if (this.mShowLocationByRadiusAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mShowLocationByRadiusAnimator = objectAnimator;
            objectAnimator.setTarget(this);
            this.mShowLocationByRadiusAnimator.setPropertyName("radius");
            this.mShowLocationByRadiusAnimator.setInterpolator(this.mDefaultInterpolator);
            this.mShowLocationByRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerTimeCapsuleView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerTimeCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener != null) {
                        onAnimatorListener.onTimeCapsuleAnimatorUpdate();
                    }
                }
            });
        }
        this.mShowLocationByRadiusAnimator.setFloatValues(getRadius(), this.mLargeBackgroundRadius);
        if (this.mShowLocationByScaleYAnimator == null) {
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            this.mShowLocationByScaleYAnimator = objectAnimator2;
            objectAnimator2.setTarget(this);
            this.mShowLocationByScaleYAnimator.setPropertyName("scaleY");
            this.mShowLocationByScaleYAnimator.setInterpolator(this.mDefaultInterpolator);
        }
        this.mShowLocationByScaleYAnimator.setFloatValues((this.mBackgroundHeight * getScaleY()) / this.mLargeBackgroundHeight, 1.0f);
        if (this.mShowLocationByAlphaAnimator == null) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "locationAlpha", 0, 255);
            this.mShowLocationByAlphaAnimator = ofInt;
            ofInt.setInterpolator(this.mDefaultInterpolator);
        }
        if (this.mShowLocationAnimator == null) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mShowLocationAnimator = animatorSet;
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerTimeCapsuleView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    FastScrollerTimeCapsuleView.this.mAnimationState = 2;
                    FastScrollerTimeCapsuleView.this.mIsLocationAnimating = false;
                    super.onAnimationEnd(animator);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    FastScrollerTimeCapsuleView.this.mAnimationState = 3;
                    FastScrollerTimeCapsuleView.this.mIsLocationAnimating = true;
                    super.onAnimationStart(animator);
                }
            });
            this.mShowLocationAnimator.play(this.mShowLocationByRadiusAnimator).with(this.mShowLocationByScaleYAnimator).with(this.mShowLocationByAlphaAnimator);
        }
        this.mShowLocationAnimator.start();
    }

    public final void hideLocationAnimation() {
        cancelHideLocationAnimation();
        if (this.mHideLocationByRadiusAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mHideLocationByRadiusAnimator = objectAnimator;
            objectAnimator.setTarget(this);
            this.mHideLocationByRadiusAnimator.setPropertyName("radius");
            this.mHideLocationByRadiusAnimator.setInterpolator(this.mDefaultInterpolator);
            this.mHideLocationByRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerTimeCapsuleView.3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FastScrollerCapsule.OnAnimatorListener onAnimatorListener = FastScrollerTimeCapsuleView.this.mOnAnimatorListener;
                    if (onAnimatorListener != null) {
                        onAnimatorListener.onTimeCapsuleAnimatorUpdate();
                    }
                }
            });
        }
        this.mHideLocationByRadiusAnimator.setFloatValues(getRadius(), this.mBackgroundRadius);
        if (this.mHideLocationByScaleYAnimator == null) {
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            this.mHideLocationByScaleYAnimator = objectAnimator2;
            objectAnimator2.setTarget(this);
            this.mHideLocationByScaleYAnimator.setPropertyName("scaleY");
            this.mHideLocationByScaleYAnimator.setInterpolator(this.mDefaultInterpolator);
        }
        this.mHideLocationByScaleYAnimator.setFloatValues((this.mLargeBackgroundHeight * getScaleY()) / this.mBackgroundHeight, 1.0f);
        if (this.mHideLocationByAlphaAnimator == null) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "locationAlpha", 255, 0);
            this.mHideLocationByAlphaAnimator = ofInt;
            ofInt.setInterpolator(this.mDefaultInterpolator);
        }
        if (this.mHideLocationAnimator == null) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mHideLocationAnimator = animatorSet;
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.FastScrollerTimeCapsuleView.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    FastScrollerTimeCapsuleView.this.mAnimationState = 0;
                    FastScrollerTimeCapsuleView.this.mIsLocationAnimating = false;
                    super.onAnimationEnd(animator);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    FastScrollerTimeCapsuleView.this.mAnimationState = 1;
                    FastScrollerTimeCapsuleView.this.mIsLocationAnimating = true;
                    super.onAnimationStart(animator);
                }
            });
            this.mHideLocationAnimator.play(this.mHideLocationByRadiusAnimator).with(this.mHideLocationByScaleYAnimator).with(this.mHideLocationByAlphaAnimator);
        }
        this.mHideLocationAnimator.start();
    }

    public final void cancelShowLocationAnimation() {
        AnimatorSet animatorSet = this.mShowLocationAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    public final void cancelHideLocationAnimation() {
        AnimatorSet animatorSet = this.mHideLocationAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView, com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public void setIsShowLocation(boolean z) {
        this.mIsShowLocation = z;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView, com.miui.gallery.widget.recyclerview.FastScrollerCapsule
    public boolean getIsShowLocation() {
        return this.mIsShowLocation && !TextUtils.isEmpty(this.mLocation);
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView
    public void setInvisible() {
        super.setInvisible();
        this.mYear = null;
        this.mMonth = null;
        this.mDate = null;
        this.mLocation = null;
        this.mDateContent = null;
        this.mRadius = this.mBackgroundRadius;
    }

    @Override // com.miui.gallery.widget.recyclerview.FastScrollerStringCapsuleView, com.miui.gallery.widget.recyclerview.DrawView
    public void draw(Canvas canvas) {
        float f;
        if (!TextUtils.isEmpty(this.mDateContent)) {
            int i = this.mViewWidth;
            int i2 = (int) (i * this.mScaleX);
            int i3 = i - i2;
            int i4 = 0;
            boolean z = this.mIsShowLocation && !TextUtils.isEmpty(this.mLocation);
            int i5 = this.mBackgroundHeight;
            if (z) {
                i5 = this.mLargeBackgroundHeight;
            }
            int i6 = (int) (i5 * this.mScaleY);
            int i7 = (this.mViewHeight - i6) >> 1;
            float f2 = this.mIsLocationAnimating ? this.mRadius : z ? this.mLargeBackgroundRadius : this.mBackgroundRadius;
            int i8 = this.mBackgroundColorAlpha;
            int i9 = this.mAlpha;
            if (i8 >= i9) {
                i8 = i9;
            }
            int i10 = this.mBackgroundBorderColorAlpha;
            if (i10 < i9) {
                i9 = i10;
            }
            canvas.save();
            if (this.mIsInRight) {
                canvas.translate(this.mTranslationX + i3, i7);
            } else {
                canvas.translate(this.mTranslationX, i7);
            }
            Drawable drawable = this.mShadowDrawable;
            if (drawable != null) {
                int i11 = (int) (i2 * 1.35f);
                int i12 = (int) (i6 * 1.45f);
                int i13 = (-(i11 - i2)) >> 1;
                int i14 = (-(i12 - i6)) >> 1;
                drawable.setBounds(i13, i14, i11 + i13, i12 + i14);
                this.mShadowDrawable.setAlpha(this.mAlpha);
                this.mShadowDrawable.draw(canvas);
            }
            RectF rectF = this.mBackgroundRect;
            rectF.right = i2;
            rectF.bottom = i6;
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mBackgroundColor);
            this.mPaint.setAlpha(i8);
            canvas.drawRoundRect(this.mBackgroundRect, f2, f2, this.mPaint);
            this.mPaint.setColor(this.mBackgroundBorderColor);
            this.mPaint.setAlpha(i9);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.mBackgroundBorderWidth);
            canvas.drawRoundRect(this.mBackgroundRect, f2, f2, this.mPaint);
            canvas.restore();
            canvas.save();
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            this.mPaint.setColor(this.mDateColor);
            this.mPaint.setAlpha(this.mAlpha);
            this.mPaint.setTextSize(this.mDateTextSize);
            Paint paint = this.mPaint;
            String str = this.mDateContent;
            paint.getTextBounds(str, 0, str.length(), this.mDateRect);
            Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
            int i15 = this.mStartMargin;
            int width = this.mDateRect.width() + i15 + this.mEndMargin;
            canvas.drawText(this.mDateContent, this.mTranslationX + i15, (int) ((this.mTopMargin - fontMetrics.top) + i7), this.mPaint);
            canvas.restore();
            canvas.save();
            int height = ((i6 - this.mDateRect.height()) - this.mTopMargin) - this.mBottomMargin;
            if (z && height >= this.mLocationRect.height()) {
                this.mLocationTextPaint.setAlpha(this.mLocationAlpha);
                TextPaint textPaint = this.mLocationTextPaint;
                String str2 = this.mLocation;
                textPaint.getTextBounds(str2, 0, str2.length(), this.mLocationRect);
                canvas.drawText(this.mLocation, this.mTranslationX + i15, (int) (((f + fontMetrics.bottom) - this.mLocationTextPaint.getFontMetrics().top) + this.mDateTextMarginBottom), this.mLocationTextPaint);
                i4 = i15 + ((int) this.mLocationTextPaint.measureText(this.mLocation)) + this.mEndMargin;
            }
            this.mViewWidth = Math.max(width, i4);
            this.mViewHeight = z ? this.mLargeBackgroundHeight : this.mBackgroundHeight;
            canvas.restore();
        }
    }
}
