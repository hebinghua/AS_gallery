package com.miui.gallery.ui.actionBar;

import android.animation.TimeInterpolator;
import android.graphics.drawable.Drawable;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class AnimDrawableWrapper implements IAnimDrawable {
    public static AnimDrawableBuilder mDefaultAnimDrawable = new AnimDrawableBuilder().setDuration(250).setPropertyName("alpha").setInterpolator(new CubicEaseOutInterpolator()).setStartValue(0).setEndValue(255);
    public ActionBarAnimator mAppearOrHideAnimator;
    public AnimDrawableBuilder mBuilder;
    public Drawable mDrawable;
    public int mDuration;
    public int mEndValue;
    public TimeInterpolator mInterpolator;
    public String mPropertyName;
    public int mStartValue;

    public AnimDrawableWrapper(Drawable drawable, AnimDrawableBuilder animDrawableBuilder) {
        this.mDrawable = drawable;
        this.mBuilder = animDrawableBuilder;
        this.mDuration = animDrawableBuilder.getDuration();
        TimeInterpolator interpolator = animDrawableBuilder.getInterpolator();
        this.mInterpolator = interpolator;
        if (interpolator == null) {
            this.mInterpolator = mDefaultAnimDrawable.getInterpolator();
        }
        String propertyName = animDrawableBuilder.getPropertyName();
        this.mPropertyName = propertyName;
        if (propertyName == null) {
            this.mPropertyName = mDefaultAnimDrawable.getPropertyName();
        }
        this.mStartValue = animDrawableBuilder.getStartValue();
        int endValue = animDrawableBuilder.getEndValue();
        this.mEndValue = endValue;
        this.mAppearOrHideAnimator = new ActionBarAnimator(this.mDrawable, this.mDuration, this.mInterpolator, this.mPropertyName, this.mStartValue, endValue);
    }

    @Override // com.miui.gallery.ui.actionBar.IAnimDrawable
    public void showByAnimator() {
        this.mAppearOrHideAnimator.startByAnimation(false);
    }

    @Override // com.miui.gallery.ui.actionBar.IAnimDrawable
    public void hideByAnimator() {
        this.mAppearOrHideAnimator.startByAnimation(true);
    }

    @Override // com.miui.gallery.ui.actionBar.IAnimDrawable
    public void setVisible() {
        this.mDrawable.setAlpha(255);
    }

    @Override // com.miui.gallery.ui.actionBar.IAnimDrawable
    public void setInvisible() {
        this.mDrawable.setAlpha(0);
    }

    @Override // com.miui.gallery.ui.actionBar.IAnimDrawable
    public Drawable getDrawable() {
        return this.mDrawable;
    }

    /* loaded from: classes2.dex */
    public static class AnimDrawableBuilder {
        public int mDuration;
        public int mEndValue;
        public TimeInterpolator mInterpolator;
        public String mPropertyName;
        public int mStartValue;

        public int getDuration() {
            return this.mDuration;
        }

        public AnimDrawableBuilder setDuration(int i) {
            this.mDuration = i;
            return this;
        }

        public TimeInterpolator getInterpolator() {
            return this.mInterpolator;
        }

        public AnimDrawableBuilder setInterpolator(TimeInterpolator timeInterpolator) {
            this.mInterpolator = timeInterpolator;
            return this;
        }

        public String getPropertyName() {
            return this.mPropertyName;
        }

        public AnimDrawableBuilder setPropertyName(String str) {
            this.mPropertyName = str;
            return this;
        }

        public int getStartValue() {
            return this.mStartValue;
        }

        public AnimDrawableBuilder setStartValue(int i) {
            this.mStartValue = i;
            return this;
        }

        public int getEndValue() {
            return this.mEndValue;
        }

        public AnimDrawableBuilder setEndValue(int i) {
            this.mEndValue = i;
            return this;
        }
    }
}
