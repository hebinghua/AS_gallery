package com.miui.gallery.transition;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public class CrossFade extends Transition {
    public boolean mIsEnter;
    public Drawable mSrcDrawable;

    public CrossFade(boolean z, Drawable drawable) {
        this.mIsEnter = z;
        this.mSrcDrawable = drawable;
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        if (this.mIsEnter) {
            captureValues(transitionValues);
        }
    }

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        if (!this.mIsEnter) {
            captureValues(transitionValues);
        }
    }

    public final void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (view instanceof ImageView) {
            transitionValues.values.put("drawable", ((ImageView) view).getDrawable());
        }
    }

    @Override // android.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues != null && transitionValues2 != null) {
            final View view = transitionValues2.view;
            if (!(view instanceof ImageView)) {
                return null;
            }
            final Drawable drawable = (Drawable) (this.mIsEnter ? transitionValues.values : transitionValues2.values).get("drawable");
            Drawable drawable2 = this.mSrcDrawable;
            if (drawable2 != null && !drawable2.equals(drawable)) {
                final LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{drawable, this.mSrcDrawable});
                ValueAnimator ofInt = ValueAnimator.ofInt(0, 255);
                ofInt.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.transition.CrossFade.1
                    @Override // android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override // android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                    }

                    @Override // android.animation.Animator.AnimatorListener
                    public void onAnimationRepeat(Animator animator) {
                    }

                    @Override // android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        ((ImageView) view).setImageDrawable(layerDrawable);
                    }
                });
                ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.transition.CrossFade.2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                        if (!CrossFade.this.mIsEnter) {
                            CrossFade.this.mSrcDrawable.setAlpha(intValue);
                            return;
                        }
                        Drawable drawable3 = drawable;
                        if (drawable3 == null) {
                            return;
                        }
                        drawable3.setAlpha(intValue);
                    }
                });
                return ofInt;
            }
        }
        return null;
    }
}
