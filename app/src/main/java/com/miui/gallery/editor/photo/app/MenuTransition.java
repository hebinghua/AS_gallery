package com.miui.gallery.editor.photo.app;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.res.Resources;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.editor.utils.EditorOrientationHelper;

/* loaded from: classes2.dex */
public class MenuTransition extends Transition {
    public boolean mEnter;
    public int mExitAlphaDuration;
    public int mExitTranslateDuration;
    public boolean mPortrait;
    public float mTranslate;

    @Override // android.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
    }

    @Override // android.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
    }

    @Override // android.transition.Transition
    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        return true;
    }

    public MenuTransition(float f, boolean z, Resources resources) {
        this.mTranslate = f;
        this.mEnter = z;
        this.mExitTranslateDuration = resources.getInteger(R.integer.photo_editor_exit_transition_duration);
        this.mExitAlphaDuration = resources.getInteger(R.integer.photo_editor_exit_transition_menu_alpha_duration);
        this.mPortrait = EditorOrientationHelper.isLayoutPortrait(resources.getConfiguration());
    }

    @Override // android.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null) {
            return null;
        }
        if (this.mEnter) {
            return createEnterAnimator(transitionValues2);
        }
        return createExitAnimator(transitionValues);
    }

    public final Animator createEnterAnimator(TransitionValues transitionValues) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setValues(PropertyValuesHolder.ofFloat(this.mPortrait ? View.TRANSLATION_Y : View.TRANSLATION_X, this.mTranslate, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
        objectAnimator.setTarget(transitionValues.view);
        return objectAnimator;
    }

    public final Animator createExitAnimator(TransitionValues transitionValues) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[1];
        propertyValuesHolderArr[0] = PropertyValuesHolder.ofFloat(this.mPortrait ? View.TRANSLATION_Y : View.TRANSLATION_X, 0.0f, this.mTranslate);
        objectAnimator.setValues(propertyValuesHolderArr);
        objectAnimator.setDuration(this.mExitTranslateDuration);
        objectAnimator.setTarget(transitionValues.view);
        ObjectAnimator objectAnimator2 = new ObjectAnimator();
        objectAnimator2.setValues(PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
        objectAnimator2.setDuration(this.mExitAlphaDuration);
        objectAnimator2.setTarget(transitionValues.view);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator, objectAnimator2);
        return animatorSet;
    }
}
