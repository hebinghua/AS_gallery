package com.miui.gallery.ui.actionBar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

/* loaded from: classes2.dex */
public class ActionBarAnimator {
    public AnimatorListener mAnimatorListener;
    public int mDuration;
    public int mEndValue;
    public TimeInterpolator mInterpolator;
    public Object mObject;
    public ObjectAnimator mObjectAnimator;
    public String mPropertyName;
    public int mStartValue;

    /* loaded from: classes2.dex */
    public interface AnimatorListener {
        void onAnimatorEnd();

        void onAnimatorInverseEnd();

        void onAnimatorUpdate();
    }

    public ActionBarAnimator(Object obj, int i, TimeInterpolator timeInterpolator, String str, int i2, int i3) {
        this.mObject = obj;
        this.mDuration = i;
        this.mInterpolator = timeInterpolator;
        this.mPropertyName = str;
        this.mStartValue = i2;
        this.mEndValue = i3;
    }

    public void startByAnimation(final boolean z) {
        cancelAnimation();
        if (this.mObjectAnimator == null) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            this.mObjectAnimator = objectAnimator;
            objectAnimator.setTarget(this.mObject);
            this.mObjectAnimator.setPropertyName(this.mPropertyName);
            this.mObjectAnimator.setDuration(this.mDuration);
            TimeInterpolator timeInterpolator = this.mInterpolator;
            if (timeInterpolator != null) {
                this.mObjectAnimator.setInterpolator(timeInterpolator);
            }
            this.mObjectAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.ui.actionBar.ActionBarAnimator.1
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
                        if (ActionBarAnimator.this.mAnimatorListener == null) {
                            return;
                        }
                        if (!z) {
                            ActionBarAnimator.this.mAnimatorListener.onAnimatorEnd();
                            return;
                        } else {
                            ActionBarAnimator.this.mAnimatorListener.onAnimatorInverseEnd();
                            return;
                        }
                    }
                    this.mCanceled = false;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    this.mCanceled = true;
                }
            });
            this.mObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.ui.actionBar.ActionBarAnimator.2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (ActionBarAnimator.this.mAnimatorListener != null) {
                        ActionBarAnimator.this.mAnimatorListener.onAnimatorUpdate();
                    }
                }
            });
        }
        if (!z) {
            this.mObjectAnimator.setIntValues(this.mStartValue, this.mEndValue);
        } else {
            this.mObjectAnimator.setIntValues(this.mEndValue, this.mStartValue);
        }
        this.mObjectAnimator.start();
    }

    public void cancelAnimation() {
        ObjectAnimator objectAnimator = this.mObjectAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }
}
