package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;

/* loaded from: classes2.dex */
public class SlimRotateProgressBar extends SpaceRingProgressBar {
    public SlimRotateProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlimRotateProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setNumber(long j) {
        setNumber(j, false);
    }

    public void setNumber(long j, boolean z) {
        if (z) {
            setNumber(j, false, null);
            return;
        }
        cancelAllAnim();
        changeNumberWithNoAnim((int) (j / 1000));
    }

    public void setNumber(long j, boolean z, final Runnable runnable) {
        final int i = (int) (j / 1000);
        final AnimatorListenerAdapter animatorListenerAdapter = runnable != null ? new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.SlimRotateProgressBar.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                runnable.run();
            }
        } : null;
        if (z) {
            changeNumberWithAnim(getSpaceNumber() + (((i - getSpaceNumber()) * 7) / 10), new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.SlimRotateProgressBar.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    SlimRotateProgressBar.this.changeNumberEndStageAnim(i, animatorListenerAdapter);
                }
            });
        } else {
            changeNumberWithAnim(i, animatorListenerAdapter);
        }
    }

    public void pause() {
        cancelAnimIfRunning(this.mNumberAnimator);
    }

    @Override // com.miui.gallery.ui.SpaceRingProgressBar
    public int getChangeDuration(int i) {
        int abs = Math.abs(i - getSpaceNumber());
        if (abs < 15000) {
            return 500;
        }
        return abs < 500000 ? 2000 : 3000;
    }
}
