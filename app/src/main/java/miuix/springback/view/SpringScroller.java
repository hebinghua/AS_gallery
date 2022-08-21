package miuix.springback.view;

import android.view.animation.AnimationUtils;

/* loaded from: classes3.dex */
public class SpringScroller {
    public double mCurrX;
    public double mCurrY;
    public long mCurrentTime;
    public double mEndX;
    public double mEndY;
    public boolean mFinished = true;
    public int mFirstStep;
    public boolean mLastStep;
    public int mOrientation;
    public double mOriginStartX;
    public double mOriginStartY;
    public double mOriginVelocity;
    public SpringOperator mSpringOperator;
    public long mStartTime;
    public double mStartX;
    public double mStartY;
    public double mVelocity;

    public void scrollByFling(float f, float f2, float f3, float f4, float f5, int i, boolean z) {
        this.mFinished = false;
        this.mLastStep = false;
        double d = f;
        this.mStartX = d;
        this.mOriginStartX = d;
        this.mEndX = f2;
        double d2 = f3;
        this.mStartY = d2;
        this.mOriginStartY = d2;
        this.mCurrY = (int) d2;
        this.mEndY = f4;
        double d3 = f5;
        this.mOriginVelocity = d3;
        this.mVelocity = d3;
        if (Math.abs(d3) <= 5000.0d || z) {
            this.mSpringOperator = new SpringOperator(1.0f, 0.4f);
        } else {
            this.mSpringOperator = new SpringOperator(1.0f, 0.55f);
        }
        this.mOrientation = i;
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
    }

    public boolean computeScrollOffset() {
        if (this.mSpringOperator == null || this.mFinished) {
            return false;
        }
        int i = this.mFirstStep;
        if (i != 0) {
            if (this.mOrientation == 1) {
                this.mCurrX = i;
                this.mStartX = i;
            } else {
                this.mCurrY = i;
                this.mStartY = i;
            }
            this.mFirstStep = 0;
            return true;
        } else if (this.mLastStep) {
            this.mFinished = true;
            return true;
        } else {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mCurrentTime = currentAnimationTimeMillis;
            float f = 0.016f;
            float min = Math.min(((float) (currentAnimationTimeMillis - this.mStartTime)) / 1000.0f, 0.016f);
            if (min != 0.0f) {
                f = min;
            }
            this.mStartTime = this.mCurrentTime;
            if (this.mOrientation == 2) {
                double updateVelocity = this.mSpringOperator.updateVelocity(this.mVelocity, f, this.mEndY, this.mStartY);
                double d = this.mStartY + (f * updateVelocity);
                this.mCurrY = d;
                this.mVelocity = updateVelocity;
                if (isAtEquilibrium(d, this.mOriginStartY, this.mEndY)) {
                    this.mLastStep = true;
                    this.mCurrY = this.mEndY;
                } else {
                    this.mStartY = this.mCurrY;
                }
            } else {
                double updateVelocity2 = this.mSpringOperator.updateVelocity(this.mVelocity, f, this.mEndX, this.mStartX);
                double d2 = this.mStartX + (f * updateVelocity2);
                this.mCurrX = d2;
                this.mVelocity = updateVelocity2;
                if (isAtEquilibrium(d2, this.mOriginStartX, this.mEndX)) {
                    this.mLastStep = true;
                    this.mCurrX = this.mEndX;
                } else {
                    this.mStartX = this.mCurrX;
                }
            }
            return true;
        }
    }

    public boolean isAtEquilibrium(double d, double d2, double d3) {
        if (d2 >= d3 || d <= d3) {
            int i = (d2 > d3 ? 1 : (d2 == d3 ? 0 : -1));
            if (i > 0 && d < d3) {
                return true;
            }
            return (i == 0 && Math.signum(this.mOriginVelocity) != Math.signum(d)) || Math.abs(d - d3) < 1.0d;
        }
        return true;
    }

    public final int getCurrX() {
        return (int) this.mCurrX;
    }

    public final int getCurrY() {
        return (int) this.mCurrY;
    }

    public final boolean isFinished() {
        return this.mFinished;
    }

    public final void forceStop() {
        this.mFinished = true;
        this.mFirstStep = 0;
    }

    public void setFirstStep(int i) {
        this.mFirstStep = i;
    }
}
