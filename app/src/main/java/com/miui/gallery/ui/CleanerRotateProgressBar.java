package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.miui.gallery.R;
import com.miui.gallery.util.concurrent.ThreadManager;
import miuix.view.animation.CubicEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class CleanerRotateProgressBar extends SpaceRingProgressBar {
    public boolean mHasShowEmptyAnim;

    public CleanerRotateProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CleanerRotateProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHasShowEmptyAnim = false;
        setDescription(getContext().getString(R.string.cleaner_space_description));
    }

    public void showRingIcon(boolean z) {
        if (z) {
            appearRingIconAnim();
        } else {
            this.mRingIcon.setAlpha(1.0f);
        }
        setDetailDescription(null);
    }

    public void hideRingIcon(boolean z) {
        if (z) {
            disappearRingIconAnim();
        } else {
            this.mRingIcon.setAlpha(0.0f);
        }
        setDetailDescription(getContext().getString(R.string.cleaner_space_detail_description));
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

    public void changeConfiguration() {
        ImageView imageView = this.mRingIcon;
        if (imageView != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.cleaner_item_ring_margin);
            layoutParams.rightMargin = dimensionPixelOffset;
            layoutParams.leftMargin = dimensionPixelOffset;
            this.mRingIcon.setLayoutParams(layoutParams);
        }
        this.mSpaceNumberView.setTextSize(0, getContext().getResources().getDimensionPixelOffset(R.dimen.rotate_ring_text_size));
        this.mSpaceNumberView.setCornerTextSize(getContext().getResources().getDimensionPixelOffset(R.dimen.rotate_ring_corner_text_size));
    }

    public void setNumber(long j, boolean z, final Runnable runnable) {
        final int i = (int) (j / 1000);
        final AnimatorListenerAdapter animatorListenerAdapter = runnable != null ? new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.CleanerRotateProgressBar.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                runnable.run();
            }
        } : null;
        if (z) {
            changeNumberWithAnim(getSpaceNumber() + (((i - getSpaceNumber()) * 7) / 10), new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.CleanerRotateProgressBar.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    CleanerRotateProgressBar.this.changeNumberEndStageAnim(i, animatorListenerAdapter);
                }
            });
        } else {
            changeNumberWithAnim(i, animatorListenerAdapter);
        }
    }

    public void showEmptyAnim() {
        showEmptyAnim(0L);
    }

    public void showEmptyAnim(long j) {
        if (this.mHasShowEmptyAnim) {
            return;
        }
        ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.ui.CleanerRotateProgressBar.3
            @Override // java.lang.Runnable
            public void run() {
                CleanerRotateProgressBar.this.mHasShowEmptyAnim = true;
                CleanerRotateProgressBar.this.cancelAllAnim();
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(CleanerRotateProgressBar.this.mSpaceNumberView, "alpha", 1.0f, 0.0f);
                ofFloat.setDuration(250L);
                ofFloat.setInterpolator(new CubicEaseInOutInterpolator());
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.CleanerRotateProgressBar.3.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        CleanerRotateProgressBar.this.mRingIcon.setAlpha(0.0f);
                        CleanerRotateProgressBar cleanerRotateProgressBar = CleanerRotateProgressBar.this;
                        cleanerRotateProgressBar.setDescription(cleanerRotateProgressBar.getContext().getString(R.string.cleaner_empty_value_description));
                        CleanerRotateProgressBar.this.setDetailDescription(null);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        CleanerRotateProgressBar.this.mSpaceNumberView.setVisibility(4);
                        CleanerRotateProgressBar.this.mEmptyValueView.setVisibility(0);
                    }
                });
                ofFloat.start();
            }
        }, j);
    }
}
