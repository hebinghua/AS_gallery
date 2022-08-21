package miuix.overscroller.internal.dynamicanimation.animation;

import miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation;

/* loaded from: classes3.dex */
public final class FlingAnimation extends DynamicAnimation<FlingAnimation> {
    public FinalValueListener mFinalValueListener;
    public final DragForce mFlingForce;

    /* loaded from: classes3.dex */
    public interface FinalValueListener {
        void onFinalValueArrived(int i);
    }

    public FlingAnimation(FloatValueHolder floatValueHolder, FinalValueListener finalValueListener) {
        super(floatValueHolder);
        DragForce dragForce = new DragForce();
        this.mFlingForce = dragForce;
        dragForce.setValueThreshold(getValueThreshold());
        this.mFinalValueListener = finalValueListener;
    }

    public FlingAnimation setFriction(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Friction must be positive");
        }
        this.mFlingForce.setFrictionScalar(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setMinValue(float f) {
        super.setMinValue(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setMaxValue(float f) {
        super.setMaxValue(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public FlingAnimation setStartVelocity(float f) {
        super.setStartVelocity(f);
        return this;
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public boolean updateValueAndVelocity(long j) {
        DynamicAnimation.MassState updateValueAndVelocity = this.mFlingForce.updateValueAndVelocity(this.mValue, this.mVelocity, j);
        float f = updateValueAndVelocity.mValue;
        this.mValue = f;
        float f2 = updateValueAndVelocity.mVelocity;
        this.mVelocity = f2;
        float f3 = this.mMinValue;
        if (f < f3) {
            this.mValue = f3;
            return true;
        }
        float f4 = this.mMaxValue;
        if (f > f4) {
            this.mValue = f4;
            return true;
        } else if (!isAtEquilibrium(f, f2)) {
            return false;
        } else {
            this.mFinalValueListener.onFinalValueArrived((int) this.mValue);
            return true;
        }
    }

    public boolean isAtEquilibrium(float f, float f2) {
        return f >= this.mMaxValue || f <= this.mMinValue || this.mFlingForce.isAtEquilibrium(f, f2);
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation
    public void setValueThreshold(float f) {
        this.mFlingForce.setValueThreshold(f);
    }

    public float predictNaturalDest() {
        return (this.mValue - (this.mVelocity / this.mFlingForce.mFriction)) + ((Math.signum(this.mVelocity) * this.mFlingForce.mVelocityThreshold) / this.mFlingForce.mFriction);
    }

    public float predictTimeTo(float f) {
        return predictTimeWithVelocity(((f - this.mValue) + (this.mVelocity / this.mFlingForce.mFriction)) * this.mFlingForce.mFriction);
    }

    public float predictDuration() {
        return predictTimeWithVelocity(Math.signum(this.mVelocity) * this.mFlingForce.mVelocityThreshold);
    }

    public final float predictTimeWithVelocity(float f) {
        return (float) ((Math.log(f / this.mVelocity) * 1000.0d) / this.mFlingForce.mFriction);
    }

    /* loaded from: classes3.dex */
    public static final class DragForce {
        public double mDragRate;
        public float mVelocityThreshold;
        public float mFriction = -4.2f;
        public final DynamicAnimation.MassState mMassState = new DynamicAnimation.MassState();
        public final float MILLISECONDS_PER_SECOND = 1000.0f;

        public void setFrictionScalar(float f) {
            float f2 = f * (-4.2f);
            this.mFriction = f2;
            this.mDragRate = 1.0d - Math.pow(2.718281828459045d, f2);
        }

        public DynamicAnimation.MassState updateValueAndVelocity(float f, float f2, long j) {
            float min = ((float) Math.min(j, 16L)) / 1000.0f;
            double pow = Math.pow(1.0d - this.mDragRate, min);
            DynamicAnimation.MassState massState = this.mMassState;
            float f3 = (float) (f2 * pow);
            massState.mVelocity = f3;
            float f4 = f + (min * f3);
            massState.mValue = f4;
            if (isAtEquilibrium(f4, f3)) {
                this.mMassState.mVelocity = 0.0f;
            }
            return this.mMassState;
        }

        public boolean isAtEquilibrium(float f, float f2) {
            return Math.abs(f2) < this.mVelocityThreshold;
        }

        public void setValueThreshold(float f) {
            this.mVelocityThreshold = f * 62.5f;
        }
    }
}
