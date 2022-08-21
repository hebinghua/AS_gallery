package com.miui.gallery.widget.overscroll;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/* loaded from: classes2.dex */
public abstract class OverScrollBounceEffectDecoratorBase implements View.OnTouchListener {
    public final BounceBackState mBounceBackState;
    public IDecoratorState mCurrentState;
    public final IdleState mIdleState;
    public final OverScrollingState mOverScrollingState;
    public final OverScrollStartAttributes mStartAttr = new OverScrollStartAttributes();
    public float mVelocity;
    public final IOverScrollInterface$IOverScrollDecoratorAdapter mViewAdapter;

    /* loaded from: classes2.dex */
    public static abstract class AnimationAttributes {
        public float mAbsOffset;
        public float mMaxOffset;
        public Property<View, Float> mProperty;

        public abstract void init(View view);
    }

    /* loaded from: classes2.dex */
    public interface IDecoratorState {
        void handleEntryTransition(IDecoratorState iDecoratorState);

        boolean handleMoveTouchEvent(MotionEvent motionEvent);

        boolean handleUpOrCancelTouchEvent(MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    public static abstract class MotionAttributes {
        public float mAbsOffset;
        public float mDeltaOffset;
        public boolean mDir;

        public abstract boolean init(View view, MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    public static class OverScrollStartAttributes {
        public float mAbsOffset;
        public boolean mDir;
        public int mPointerId;
    }

    public abstract AnimationAttributes createAnimationAttributes();

    public abstract MotionAttributes createMotionAttributes();

    public abstract void translateView(View view, float f);

    public abstract void translateViewAndEvent(View view, float f, MotionEvent motionEvent);

    /* loaded from: classes2.dex */
    public class IdleState implements IDecoratorState {
        public final MotionAttributes mMoveAttr;

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public void handleEntryTransition(IDecoratorState iDecoratorState) {
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public boolean handleUpOrCancelTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public IdleState() {
            this.mMoveAttr = OverScrollBounceEffectDecoratorBase.this.createMotionAttributes();
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public boolean handleMoveTouchEvent(MotionEvent motionEvent) {
            if (!this.mMoveAttr.init(OverScrollBounceEffectDecoratorBase.this.mViewAdapter.getView(), motionEvent)) {
                return false;
            }
            if ((!OverScrollBounceEffectDecoratorBase.this.mViewAdapter.isInAbsoluteStart() || !this.mMoveAttr.mDir) && (!OverScrollBounceEffectDecoratorBase.this.mViewAdapter.isInAbsoluteEnd() || this.mMoveAttr.mDir)) {
                return false;
            }
            OverScrollBounceEffectDecoratorBase.this.mStartAttr.mPointerId = motionEvent.getPointerId(0);
            OverScrollBounceEffectDecoratorBase overScrollBounceEffectDecoratorBase = OverScrollBounceEffectDecoratorBase.this;
            OverScrollStartAttributes overScrollStartAttributes = overScrollBounceEffectDecoratorBase.mStartAttr;
            MotionAttributes motionAttributes = this.mMoveAttr;
            overScrollStartAttributes.mAbsOffset = motionAttributes.mAbsOffset;
            overScrollStartAttributes.mDir = motionAttributes.mDir;
            overScrollBounceEffectDecoratorBase.issueStateTransition(overScrollBounceEffectDecoratorBase.mOverScrollingState);
            return OverScrollBounceEffectDecoratorBase.this.mOverScrollingState.handleMoveTouchEvent(motionEvent);
        }
    }

    /* loaded from: classes2.dex */
    public class OverScrollingState implements IDecoratorState {
        public int mCurrDragState;
        public final MotionAttributes mMoveAttr;
        public final float mTouchDragRatioBck;
        public final float mTouchDragRatioFwd;

        public OverScrollingState(float f, float f2) {
            this.mMoveAttr = OverScrollBounceEffectDecoratorBase.this.createMotionAttributes();
            this.mTouchDragRatioFwd = f;
            this.mTouchDragRatioBck = f2;
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public boolean handleMoveTouchEvent(MotionEvent motionEvent) {
            if (OverScrollBounceEffectDecoratorBase.this.mStartAttr.mPointerId != motionEvent.getPointerId(0)) {
                OverScrollBounceEffectDecoratorBase overScrollBounceEffectDecoratorBase = OverScrollBounceEffectDecoratorBase.this;
                overScrollBounceEffectDecoratorBase.issueStateTransition(overScrollBounceEffectDecoratorBase.mBounceBackState);
                return true;
            }
            View view = OverScrollBounceEffectDecoratorBase.this.mViewAdapter.getView();
            if (!this.mMoveAttr.init(view, motionEvent)) {
                return true;
            }
            MotionAttributes motionAttributes = this.mMoveAttr;
            float f = motionAttributes.mDeltaOffset;
            boolean z = motionAttributes.mDir;
            OverScrollBounceEffectDecoratorBase overScrollBounceEffectDecoratorBase2 = OverScrollBounceEffectDecoratorBase.this;
            OverScrollStartAttributes overScrollStartAttributes = overScrollBounceEffectDecoratorBase2.mStartAttr;
            boolean z2 = overScrollStartAttributes.mDir;
            float f2 = f / (z == z2 ? this.mTouchDragRatioFwd : this.mTouchDragRatioBck);
            float f3 = motionAttributes.mAbsOffset + f2;
            if ((z2 && !z && f3 <= overScrollStartAttributes.mAbsOffset) || (!z2 && z && f3 >= overScrollStartAttributes.mAbsOffset)) {
                overScrollBounceEffectDecoratorBase2.translateViewAndEvent(view, overScrollStartAttributes.mAbsOffset, motionEvent);
                OverScrollBounceEffectDecoratorBase overScrollBounceEffectDecoratorBase3 = OverScrollBounceEffectDecoratorBase.this;
                overScrollBounceEffectDecoratorBase3.issueStateTransition(overScrollBounceEffectDecoratorBase3.mIdleState);
                return true;
            }
            if (view.getParent() != null) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            long eventTime = motionEvent.getEventTime() - motionEvent.getHistoricalEventTime(0);
            if (eventTime > 0) {
                OverScrollBounceEffectDecoratorBase.this.mVelocity = f2 / ((float) eventTime);
            }
            OverScrollBounceEffectDecoratorBase.this.translateView(view, f3);
            return true;
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public boolean handleUpOrCancelTouchEvent(MotionEvent motionEvent) {
            OverScrollBounceEffectDecoratorBase overScrollBounceEffectDecoratorBase = OverScrollBounceEffectDecoratorBase.this;
            overScrollBounceEffectDecoratorBase.issueStateTransition(overScrollBounceEffectDecoratorBase.mBounceBackState);
            return false;
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public void handleEntryTransition(IDecoratorState iDecoratorState) {
            this.mCurrDragState = OverScrollBounceEffectDecoratorBase.this.mStartAttr.mDir ? 1 : 2;
        }
    }

    /* loaded from: classes2.dex */
    public class BounceBackState implements IDecoratorState, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
        public final AnimationAttributes mAnimAttributes;
        public final Interpolator mBounceBackInterpolator = new DecelerateInterpolator();
        public final float mDecelerateFactor;
        public final float mDoubleDecelerateFactor;

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public boolean handleMoveTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public boolean handleUpOrCancelTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
        }

        public BounceBackState(float f) {
            this.mDecelerateFactor = f;
            this.mDoubleDecelerateFactor = f * 2.0f;
            this.mAnimAttributes = OverScrollBounceEffectDecoratorBase.this.createAnimationAttributes();
        }

        @Override // com.miui.gallery.widget.overscroll.OverScrollBounceEffectDecoratorBase.IDecoratorState
        public void handleEntryTransition(IDecoratorState iDecoratorState) {
            Animator createAnimator = createAnimator();
            createAnimator.addListener(this);
            createAnimator.start();
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            OverScrollBounceEffectDecoratorBase overScrollBounceEffectDecoratorBase = OverScrollBounceEffectDecoratorBase.this;
            overScrollBounceEffectDecoratorBase.issueStateTransition(overScrollBounceEffectDecoratorBase.mIdleState);
        }

        public Animator createAnimator() {
            View view = OverScrollBounceEffectDecoratorBase.this.mViewAdapter.getView();
            this.mAnimAttributes.init(view);
            OverScrollBounceEffectDecoratorBase overScrollBounceEffectDecoratorBase = OverScrollBounceEffectDecoratorBase.this;
            float f = overScrollBounceEffectDecoratorBase.mVelocity;
            float f2 = 0.0f;
            if (f == 0.0f || ((f < 0.0f && overScrollBounceEffectDecoratorBase.mStartAttr.mDir) || (f > 0.0f && !overScrollBounceEffectDecoratorBase.mStartAttr.mDir))) {
                return createBounceBackAnimator(this.mAnimAttributes.mAbsOffset);
            }
            float f3 = (-f) / this.mDecelerateFactor;
            if (f3 >= 0.0f) {
                f2 = f3;
            }
            float f4 = this.mAnimAttributes.mAbsOffset + (((-f) * f) / this.mDoubleDecelerateFactor);
            ObjectAnimator createSlowdownAnimator = createSlowdownAnimator(view, (int) f2, f4);
            ObjectAnimator createBounceBackAnimator = createBounceBackAnimator(f4);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(createSlowdownAnimator, createBounceBackAnimator);
            return animatorSet;
        }

        public ObjectAnimator createSlowdownAnimator(View view, int i, float f) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, this.mAnimAttributes.mProperty, f);
            ofFloat.setDuration(i);
            ofFloat.setInterpolator(this.mBounceBackInterpolator);
            ofFloat.addUpdateListener(this);
            return ofFloat;
        }

        public ObjectAnimator createBounceBackAnimator(float f) {
            View view = OverScrollBounceEffectDecoratorBase.this.mViewAdapter.getView();
            float abs = Math.abs(f);
            AnimationAttributes animationAttributes = this.mAnimAttributes;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, animationAttributes.mProperty, OverScrollBounceEffectDecoratorBase.this.mStartAttr.mAbsOffset);
            ofFloat.setDuration(Math.max((int) ((abs / animationAttributes.mMaxOffset) * 800.0f), 200));
            ofFloat.setInterpolator(this.mBounceBackInterpolator);
            ofFloat.addUpdateListener(this);
            return ofFloat;
        }
    }

    public OverScrollBounceEffectDecoratorBase(IOverScrollInterface$IOverScrollDecoratorAdapter iOverScrollInterface$IOverScrollDecoratorAdapter, float f, float f2, float f3) {
        this.mViewAdapter = iOverScrollInterface$IOverScrollDecoratorAdapter;
        this.mBounceBackState = new BounceBackState(f);
        this.mOverScrollingState = new OverScrollingState(f2, f3);
        IdleState idleState = new IdleState();
        this.mIdleState = idleState;
        this.mCurrentState = idleState;
        attach();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action != 1) {
            if (action == 2) {
                return this.mCurrentState.handleMoveTouchEvent(motionEvent);
            }
            if (action != 3) {
                return false;
            }
        }
        return this.mCurrentState.handleUpOrCancelTouchEvent(motionEvent);
    }

    public View getView() {
        return this.mViewAdapter.getView();
    }

    public void issueStateTransition(IDecoratorState iDecoratorState) {
        IDecoratorState iDecoratorState2 = this.mCurrentState;
        this.mCurrentState = iDecoratorState;
        iDecoratorState.handleEntryTransition(iDecoratorState2);
    }

    public void attach() {
        getView().setOnTouchListener(this);
        getView().setOverScrollMode(2);
    }
}
