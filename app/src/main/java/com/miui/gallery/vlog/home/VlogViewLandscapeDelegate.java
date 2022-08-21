package com.miui.gallery.vlog.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;

/* loaded from: classes2.dex */
public class VlogViewLandscapeDelegate implements IVlogViewDelegate {
    public Activity mActivity;
    public final int mHorizontalTranslation;
    public Guideline mLeftGuideline;
    public final int mLeftGuidelineOriginal;
    public Guideline mRightGuideline;
    public final int mRightGuidelineOriginal;
    public ValueAnimator mValueAnimator;
    public final String TAG = "VlogViewLandscapeProxy";
    public final long mAnimatorDuration = 200;
    public boolean mIsLeftAnimatorStarted = false;
    public boolean mIsRightAnimatorStarted = false;

    /* renamed from: $r8$lambda$LDF-uvxdv_J_iIDwI0S_xC0NI90 */
    public static /* synthetic */ void m1795$r8$lambda$LDFuvxdv_J_iIDwI0S_xC0NI90(VlogViewLandscapeDelegate vlogViewLandscapeDelegate, ValueAnimator valueAnimator) {
        vlogViewLandscapeDelegate.lambda$initAnimator$0(valueAnimator);
    }

    public VlogViewLandscapeDelegate(Activity activity) {
        this.mActivity = activity;
        this.mHorizontalTranslation = activity.getResources().getDimensionPixelSize(R$dimen.vlog_main_horizontal_translation);
        this.mLeftGuidelineOriginal = this.mActivity.getResources().getDimensionPixelSize(R$dimen.vlog_main_landscape_panel_left_line);
        this.mRightGuidelineOriginal = this.mActivity.getResources().getDimensionPixelSize(R$dimen.vlog_main_landscape_panel_right_line);
        initAnimator();
    }

    @Override // com.miui.gallery.vlog.home.IVlogViewDelegate
    public void setContentView() {
        this.mActivity.setContentView(R$layout.vlog_activity_layout_land);
        initOrientationView();
    }

    @Override // com.miui.gallery.vlog.home.IVlogViewDelegate
    public void showEffectMenuAnimation(String str) {
        if (this.mActivity.getString(VlogConfig.CLIP_RES_ID).equals(str)) {
            moveRightAnim();
        } else {
            moveLeftAnim();
        }
    }

    @Override // com.miui.gallery.vlog.home.IVlogViewDelegate
    public void release() {
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    public final void initOrientationView() {
        this.mLeftGuideline = (Guideline) this.mActivity.findViewById(R$id.panel_left_line);
        this.mRightGuideline = (Guideline) this.mActivity.findViewById(R$id.panel_right_line);
    }

    public final void initAnimator() {
        if (this.mValueAnimator == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mValueAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.vlog.home.VlogViewLandscapeDelegate$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    VlogViewLandscapeDelegate.m1795$r8$lambda$LDFuvxdv_J_iIDwI0S_xC0NI90(VlogViewLandscapeDelegate.this, valueAnimator);
                }
            });
            this.mValueAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.vlog.home.VlogViewLandscapeDelegate.1
                {
                    VlogViewLandscapeDelegate.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    VlogViewLandscapeDelegate.this.mIsLeftAnimatorStarted = false;
                    VlogViewLandscapeDelegate.this.mIsRightAnimatorStarted = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    VlogViewLandscapeDelegate.this.mIsLeftAnimatorStarted = false;
                    VlogViewLandscapeDelegate.this.mIsRightAnimatorStarted = false;
                }
            });
            this.mValueAnimator.setDuration(200L);
        }
    }

    public /* synthetic */ void lambda$initAnimator$0(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mLeftGuideline.setGuidelineBegin((int) (this.mLeftGuidelineOriginal - (this.mHorizontalTranslation * floatValue)));
        this.mRightGuideline.setGuidelineEnd((int) (this.mRightGuidelineOriginal + (this.mHorizontalTranslation * floatValue)));
    }

    public final void moveLeftAnim() {
        if (this.mIsRightAnimatorStarted) {
            this.mIsRightAnimatorStarted = false;
            this.mIsLeftAnimatorStarted = true;
            this.mValueAnimator.reverse();
        } else if (this.mIsLeftAnimatorStarted || Math.abs(((ConstraintLayout.LayoutParams) this.mLeftGuideline.getLayoutParams()).guideBegin - (this.mLeftGuidelineOriginal - this.mHorizontalTranslation)) <= 1) {
        } else {
            this.mIsLeftAnimatorStarted = true;
            this.mValueAnimator.start();
        }
    }

    public final void moveRightAnim() {
        if (!this.mIsRightAnimatorStarted && Math.abs(((ConstraintLayout.LayoutParams) this.mLeftGuideline.getLayoutParams()).guideBegin - this.mLeftGuidelineOriginal) > 1) {
            this.mIsRightAnimatorStarted = true;
            this.mValueAnimator.reverse();
        }
    }
}
