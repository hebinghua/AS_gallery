package miuix.internal.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import miuix.animation.physics.DynamicAnimation;
import miuix.animation.physics.SpringAnimation;
import miuix.animation.property.FloatProperty;

/* loaded from: classes3.dex */
public class CheckWidgetDrawableAnims {
    public int mBackgroundDisableAlpha;
    public int mBackgroundNormalAlpha;
    public CheckWidgetCircleDrawable mBlackDrawable;
    public CheckWidgetCircleDrawable mBlueDrawable;
    public CheckWidgetCircleDrawable mGrayDrawable;
    public boolean mIsSingleSelection;
    public CheckBoxAnimatedStateListDrawable mParent;
    public SpringAnimation mParentCheckedUnPressScaleAnim;
    public SpringAnimation mParentPressAnim;
    public SpringAnimation mParentUnCheckedUnPressScaleAnim;
    public SpringAnimation mParentUnPressAlphaAnim;
    public SpringAnimation mPressedBlackAnim;
    public SpringAnimation mPressedScaleAnim;
    public SpringAnimation mUnPressedBlackAnim;
    public SpringAnimation mUnPressedBlueHideAnim;
    public SpringAnimation mUnPressedBlueShowAnim;
    public SpringAnimation unPressedScaleAnim;
    public float mScale = 1.0f;
    public DynamicAnimation.OnAnimationUpdateListener mParentInvalidListener = new DynamicAnimation.OnAnimationUpdateListener() { // from class: miuix.internal.view.CheckWidgetDrawableAnims$$ExternalSyntheticLambda0
        @Override // miuix.animation.physics.DynamicAnimation.OnAnimationUpdateListener
        public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
            CheckWidgetDrawableAnims.m2617$r8$lambda$C00fPWRGPY_TlAmddm4_6fERDo(CheckWidgetDrawableAnims.this, dynamicAnimation, f, f2);
        }
    };
    public DynamicAnimation.OnAnimationUpdateListener mParentScaleInvalidListener = new DynamicAnimation.OnAnimationUpdateListener() { // from class: miuix.internal.view.CheckWidgetDrawableAnims.1
        {
            CheckWidgetDrawableAnims.this = this;
        }

        @Override // miuix.animation.physics.DynamicAnimation.OnAnimationUpdateListener
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
            CheckWidgetDrawableAnims.this.mParent.setScale(CheckWidgetDrawableAnims.this.getScale());
            CheckWidgetDrawableAnims.this.mParent.invalidateSelf();
        }
    };
    public FloatProperty<CheckBoxAnimatedStateListDrawable> mParentScaleFloatProperty = new FloatProperty<CheckBoxAnimatedStateListDrawable>("Scale") { // from class: miuix.internal.view.CheckWidgetDrawableAnims.2
        {
            CheckWidgetDrawableAnims.this = this;
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable) {
            return CheckWidgetDrawableAnims.this.mParent.getScale();
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable, float f) {
            CheckWidgetDrawableAnims.this.mParent.setScale(f);
        }
    };
    public FloatProperty<CheckBoxAnimatedStateListDrawable> mParentContentAlphaFloatProperty = new FloatProperty<CheckBoxAnimatedStateListDrawable>("ContentAlpha") { // from class: miuix.internal.view.CheckWidgetDrawableAnims.3
        {
            CheckWidgetDrawableAnims.this = this;
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable) {
            return checkBoxAnimatedStateListDrawable.getContentAlpha();
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable, float f) {
            if (f > 1.0f) {
                f = 1.0f;
            }
            if (f < 0.0f) {
                f = 0.0f;
            }
            checkBoxAnimatedStateListDrawable.setContentAlpha(f);
        }
    };
    public FloatProperty<CheckWidgetDrawableAnims> scaleFloatProperty = new FloatProperty<CheckWidgetDrawableAnims>("Scale") { // from class: miuix.internal.view.CheckWidgetDrawableAnims.4
        {
            CheckWidgetDrawableAnims.this = this;
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(CheckWidgetDrawableAnims checkWidgetDrawableAnims) {
            return CheckWidgetDrawableAnims.this.getScale();
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(CheckWidgetDrawableAnims checkWidgetDrawableAnims, float f) {
            CheckWidgetDrawableAnims.this.setScale(f);
        }
    };
    public FloatProperty<CheckWidgetCircleDrawable> mCircleAlphaFloatProperty = new FloatProperty<CheckWidgetCircleDrawable>("Alpha") { // from class: miuix.internal.view.CheckWidgetDrawableAnims.5
        {
            CheckWidgetDrawableAnims.this = this;
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(CheckWidgetCircleDrawable checkWidgetCircleDrawable) {
            return checkWidgetCircleDrawable.getAlpha() / 255;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(CheckWidgetCircleDrawable checkWidgetCircleDrawable, float f) {
            if (f > 1.0f) {
                f = 1.0f;
            }
            if (f < 0.0f) {
                f = 0.0f;
            }
            checkWidgetCircleDrawable.setAlpha((int) (f * 255.0f));
        }
    };

    /* renamed from: $r8$lambda$C00fPW-RGPY_TlAmddm4_6fERDo */
    public static /* synthetic */ void m2617$r8$lambda$C00fPWRGPY_TlAmddm4_6fERDo(CheckWidgetDrawableAnims checkWidgetDrawableAnims, DynamicAnimation dynamicAnimation, float f, float f2) {
        checkWidgetDrawableAnims.lambda$new$0(dynamicAnimation, f, f2);
    }

    public /* synthetic */ void lambda$new$0(DynamicAnimation dynamicAnimation, float f, float f2) {
        this.mParent.invalidateSelf();
    }

    public CheckWidgetDrawableAnims(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.mIsSingleSelection = false;
        this.mBackgroundNormalAlpha = i4;
        this.mBackgroundDisableAlpha = i5;
        this.mIsSingleSelection = z;
        CheckWidgetCircleDrawable checkWidgetCircleDrawable = new CheckWidgetCircleDrawable(i, i4, i5, i6, i7, i8);
        this.mGrayDrawable = checkWidgetCircleDrawable;
        checkWidgetCircleDrawable.setAlpha(this.mBackgroundNormalAlpha);
        CheckWidgetCircleDrawable checkWidgetCircleDrawable2 = new CheckWidgetCircleDrawable(i2, i4, i5);
        this.mBlackDrawable = checkWidgetCircleDrawable2;
        checkWidgetCircleDrawable2.setAlpha(0);
        CheckWidgetCircleDrawable checkWidgetCircleDrawable3 = new CheckWidgetCircleDrawable(i3, i4, i5);
        this.mBlueDrawable = checkWidgetCircleDrawable3;
        checkWidgetCircleDrawable3.setAlpha(255);
        this.mParent = checkBoxAnimatedStateListDrawable;
        initAnim();
    }

    public final void initAnim() {
        SpringAnimation springAnimation = new SpringAnimation(this, this.scaleFloatProperty, 0.6f);
        this.mPressedScaleAnim = springAnimation;
        springAnimation.getSpring().setStiffness(986.96f);
        this.mPressedScaleAnim.getSpring().setDampingRatio(0.99f);
        this.mPressedScaleAnim.getSpring().setFinalPosition(0.6f);
        this.mPressedScaleAnim.setMinimumVisibleChange(0.002f);
        this.mPressedScaleAnim.addUpdateListener(this.mParentScaleInvalidListener);
        SpringAnimation springAnimation2 = new SpringAnimation(this, this.scaleFloatProperty, 1.0f);
        this.unPressedScaleAnim = springAnimation2;
        springAnimation2.getSpring().setStiffness(986.96f);
        this.unPressedScaleAnim.getSpring().setDampingRatio(0.6f);
        this.unPressedScaleAnim.setMinimumVisibleChange(0.002f);
        this.unPressedScaleAnim.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: miuix.internal.view.CheckWidgetDrawableAnims.6
            {
                CheckWidgetDrawableAnims.this = this;
            }

            @Override // miuix.animation.physics.DynamicAnimation.OnAnimationUpdateListener
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                CheckWidgetDrawableAnims.this.mParent.invalidateSelf();
            }
        });
        SpringAnimation springAnimation3 = new SpringAnimation(this.mParent, this.mParentContentAlphaFloatProperty, 0.5f);
        this.mParentPressAnim = springAnimation3;
        springAnimation3.getSpring().setStiffness(986.96f);
        this.mParentPressAnim.getSpring().setDampingRatio(0.99f);
        this.mParentPressAnim.setMinimumVisibleChange(0.00390625f);
        this.mParentPressAnim.addUpdateListener(this.mParentInvalidListener);
        SpringAnimation springAnimation4 = new SpringAnimation(this.mBlackDrawable, this.mCircleAlphaFloatProperty, 0.1f);
        this.mPressedBlackAnim = springAnimation4;
        springAnimation4.getSpring().setStiffness(986.96f);
        this.mPressedBlackAnim.getSpring().setDampingRatio(0.99f);
        this.mPressedBlackAnim.setMinimumVisibleChange(0.00390625f);
        this.mPressedBlackAnim.addUpdateListener(this.mParentInvalidListener);
        SpringAnimation springAnimation5 = new SpringAnimation(this.mBlackDrawable, this.mCircleAlphaFloatProperty, 0.0f);
        this.mUnPressedBlackAnim = springAnimation5;
        springAnimation5.getSpring().setStiffness(986.96f);
        this.mUnPressedBlackAnim.getSpring().setDampingRatio(0.99f);
        this.mUnPressedBlackAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnPressedBlackAnim.addUpdateListener(this.mParentInvalidListener);
        SpringAnimation springAnimation6 = new SpringAnimation(this.mBlueDrawable, this.mCircleAlphaFloatProperty, 1.0f);
        this.mUnPressedBlueShowAnim = springAnimation6;
        springAnimation6.getSpring().setStiffness(986.96f);
        this.mUnPressedBlueShowAnim.getSpring().setDampingRatio(0.7f);
        this.mUnPressedBlueShowAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnPressedBlueShowAnim.addUpdateListener(this.mParentInvalidListener);
        SpringAnimation springAnimation7 = new SpringAnimation(this.mParent, this.mParentContentAlphaFloatProperty, 1.0f);
        this.mParentUnPressAlphaAnim = springAnimation7;
        springAnimation7.getSpring().setStiffness(438.64f);
        this.mParentUnPressAlphaAnim.getSpring().setDampingRatio(0.6f);
        this.mParentUnPressAlphaAnim.setMinimumVisibleChange(0.00390625f);
        this.mParentUnPressAlphaAnim.addUpdateListener(this.mParentInvalidListener);
        SpringAnimation springAnimation8 = new SpringAnimation(this.mBlueDrawable, this.mCircleAlphaFloatProperty, 0.0f);
        this.mUnPressedBlueHideAnim = springAnimation8;
        springAnimation8.getSpring().setStiffness(986.96f);
        this.mUnPressedBlueHideAnim.getSpring().setDampingRatio(0.99f);
        this.mUnPressedBlueHideAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnPressedBlueHideAnim.addUpdateListener(this.mParentInvalidListener);
        SpringAnimation springAnimation9 = new SpringAnimation(this.mParent, this.mParentScaleFloatProperty, 1.0f);
        this.mParentCheckedUnPressScaleAnim = springAnimation9;
        springAnimation9.getSpring().setStiffness(438.64f);
        this.mParentCheckedUnPressScaleAnim.getSpring().setDampingRatio(0.6f);
        this.mParentCheckedUnPressScaleAnim.setMinimumVisibleChange(0.002f);
        this.mParentCheckedUnPressScaleAnim.addUpdateListener(this.mParentInvalidListener);
        if (this.mIsSingleSelection) {
            this.mParentCheckedUnPressScaleAnim.setStartVelocity(5.0f);
        } else {
            this.mParentCheckedUnPressScaleAnim.setStartVelocity(10.0f);
        }
        SpringAnimation springAnimation10 = new SpringAnimation(this.mParent, this.mParentScaleFloatProperty, 0.3f);
        this.mParentUnCheckedUnPressScaleAnim = springAnimation10;
        springAnimation10.getSpring().setStiffness(986.96f);
        this.mParentUnCheckedUnPressScaleAnim.getSpring().setDampingRatio(0.99f);
        this.mParentUnCheckedUnPressScaleAnim.setMinimumVisibleChange(0.002f);
        this.mParentUnCheckedUnPressScaleAnim.addUpdateListener(this.mParentScaleInvalidListener);
    }

    public float getScale() {
        return this.mScale;
    }

    public void setScale(float f) {
        this.mGrayDrawable.setScale(f);
        this.mBlackDrawable.setScale(f);
        this.mBlueDrawable.setScale(f);
        this.mScale = f;
    }

    public void draw(Canvas canvas) {
        this.mGrayDrawable.draw(canvas);
        this.mBlackDrawable.draw(canvas);
        this.mBlueDrawable.draw(canvas);
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        this.mGrayDrawable.setBounds(i, i2, i3, i4);
        this.mBlackDrawable.setBounds(i, i2, i3, i4);
        this.mBlueDrawable.setBounds(i, i2, i3, i4);
    }

    public void setBounds(Rect rect) {
        this.mGrayDrawable.setBounds(rect);
        this.mBlackDrawable.setBounds(rect);
        this.mBlueDrawable.setBounds(rect);
    }

    public void verifyChecked(boolean z, boolean z2) {
        if (z2) {
            if (z) {
                this.mBlueDrawable.setAlpha(255);
                this.mBlackDrawable.setAlpha(25);
            } else {
                this.mBlueDrawable.setAlpha(0);
                this.mBlackDrawable.setAlpha(0);
            }
            this.mGrayDrawable.setAlpha(this.mBackgroundNormalAlpha);
            return;
        }
        this.mBlueDrawable.setAlpha(0);
        this.mBlackDrawable.setAlpha(0);
        this.mGrayDrawable.setAlpha(this.mBackgroundDisableAlpha);
    }

    public void startPressedAnim(boolean z, boolean z2) {
        if (!z2 || Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return;
        }
        if (!this.mPressedScaleAnim.isRunning()) {
            this.mPressedScaleAnim.start();
        }
        if (!this.mParentPressAnim.isRunning()) {
            this.mParentPressAnim.start();
        }
        if (!z && !this.mPressedBlackAnim.isRunning()) {
            this.mPressedBlackAnim.start();
        }
        if (this.mUnPressedBlackAnim.isRunning()) {
            this.mUnPressedBlackAnim.cancel();
        }
        if (this.unPressedScaleAnim.isRunning()) {
            this.unPressedScaleAnim.cancel();
        }
        if (this.mParentUnPressAlphaAnim.isRunning()) {
            this.mParentUnPressAlphaAnim.cancel();
        }
        if (this.mParentCheckedUnPressScaleAnim.isRunning()) {
            this.mParentCheckedUnPressScaleAnim.cancel();
        }
        if (this.mParentUnCheckedUnPressScaleAnim.isRunning()) {
            this.mParentUnCheckedUnPressScaleAnim.cancel();
        }
        if (this.mUnPressedBlueHideAnim.isRunning()) {
            this.mUnPressedBlueHideAnim.cancel();
        }
        if (!this.mUnPressedBlueShowAnim.isRunning()) {
            return;
        }
        this.mUnPressedBlueShowAnim.cancel();
    }

    public void startUnPressedAnim(boolean z, boolean z2) {
        if (!z2 || Thread.currentThread() != Looper.getMainLooper().getThread()) {
            if (z) {
                this.mBlueDrawable.setAlpha((int) (this.mUnPressedBlueShowAnim.getSpring().getFinalPosition() * 255.0f));
                return;
            } else {
                this.mBlueDrawable.setAlpha((int) (this.mUnPressedBlueHideAnim.getSpring().getFinalPosition() * 255.0f));
                return;
            }
        }
        if (this.mPressedScaleAnim.isRunning()) {
            this.mPressedScaleAnim.cancel();
        }
        if (this.mParentPressAnim.isRunning()) {
            this.mParentPressAnim.cancel();
        }
        if (this.mPressedBlackAnim.isRunning()) {
            this.mPressedBlackAnim.cancel();
        }
        if (!this.mUnPressedBlackAnim.isRunning()) {
            this.mUnPressedBlackAnim.start();
        }
        if (z) {
            if (this.mUnPressedBlueHideAnim.isRunning()) {
                this.mUnPressedBlueHideAnim.cancel();
            }
            if (!this.mUnPressedBlueShowAnim.isRunning()) {
                this.mUnPressedBlueShowAnim.start();
            }
            new Handler().postDelayed(new Runnable() { // from class: miuix.internal.view.CheckWidgetDrawableAnims.7
                {
                    CheckWidgetDrawableAnims.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (!CheckWidgetDrawableAnims.this.mParentUnPressAlphaAnim.isRunning()) {
                        CheckWidgetDrawableAnims.this.mParentUnPressAlphaAnim.start();
                    }
                    if (!CheckWidgetDrawableAnims.this.mParentCheckedUnPressScaleAnim.isRunning()) {
                        CheckWidgetDrawableAnims.this.mParentCheckedUnPressScaleAnim.start();
                    }
                }
            }, 50L);
            if (this.mIsSingleSelection) {
                this.unPressedScaleAnim.setStartVelocity(10.0f);
            } else {
                this.unPressedScaleAnim.setStartVelocity(5.0f);
            }
        } else {
            if (this.mUnPressedBlueShowAnim.isRunning()) {
                this.mUnPressedBlueShowAnim.cancel();
            }
            if (!this.mUnPressedBlueHideAnim.isRunning()) {
                this.mUnPressedBlueHideAnim.start();
            }
            if (!this.mParentUnCheckedUnPressScaleAnim.isRunning()) {
                this.mParentUnCheckedUnPressScaleAnim.start();
            }
        }
        this.unPressedScaleAnim.start();
    }
}
