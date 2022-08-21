package miuix.overscroller.widget;

import android.content.Context;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import com.miui.gallery.search.statistics.SearchStatUtils;
import miuix.animation.physics.SpringOperator;

/* loaded from: classes3.dex */
public class OverScroller {
    public final boolean mFlywheel;
    public Interpolator mInterpolator;
    public int mMode;
    public final SplineOverScroller mScrollerX;
    public final SplineOverScroller mScrollerY;

    public OverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    public OverScroller(Context context, Interpolator interpolator, boolean z) {
        if (interpolator == null) {
            this.mInterpolator = new ViscousFluidInterpolator();
        } else {
            this.mInterpolator = interpolator;
        }
        this.mFlywheel = z;
        this.mScrollerX = new DynamicScroller(context);
        this.mScrollerY = new DynamicScroller(context);
    }

    public int getMode() {
        return this.mMode;
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final int getCurrX() {
        return (int) this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return (int) this.mScrollerY.mCurrentPosition;
    }

    public float getCurrVelocity() {
        return (float) Math.hypot(this.mScrollerX.mCurrVelocity, this.mScrollerY.mCurrVelocity);
    }

    public float getCurrVelocityX() {
        return (float) this.mScrollerX.mCurrVelocity;
    }

    public float getCurrVelocityY() {
        return (float) this.mScrollerY.mCurrVelocity;
    }

    public final int getStartX() {
        return (int) this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return (int) this.mScrollerY.mStart;
    }

    public final int getFinalX() {
        return (int) this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return (int) this.mScrollerY.mFinal;
    }

    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        int i = this.mMode;
        if (i == 0) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.mScrollerX.mStartTime;
            int i2 = this.mScrollerX.mDuration;
            if (currentAnimationTimeMillis < i2) {
                float interpolation = this.mInterpolator.getInterpolation(((float) currentAnimationTimeMillis) / i2);
                this.mScrollerX.updateScroll(interpolation);
                this.mScrollerY.updateScroll(interpolation);
            } else {
                abortAnimation();
            }
        } else if (i == 1) {
            if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                this.mScrollerX.finish();
            }
            if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                this.mScrollerY.finish();
            }
        } else if (i == 2) {
            return this.mScrollerY.computeScrollOffset() || this.mScrollerX.computeScrollOffset();
        }
        return true;
    }

    public void startScrollByFling(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mMode = 2;
        this.mScrollerX.startScrollByFling(i, i3, i5);
        this.mScrollerY.startScrollByFling(i2, i4, i6);
    }

    public boolean springBack(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mMode = 1;
        return this.mScrollerX.springback(i, i3, i4) || this.mScrollerY.springback(i2, i5, i6);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        fling(i, i2, i3, i4, i5, i6, i7, i8, 0, 0);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        int i11;
        int i12;
        int i13;
        int i14;
        if (!this.mFlywheel || isFinished()) {
            i11 = i3;
        } else {
            float f = (float) this.mScrollerX.mCurrVelocity;
            float f2 = (float) this.mScrollerY.mCurrVelocity;
            i11 = i3;
            float f3 = i11;
            if (Math.signum(f3) == Math.signum(f)) {
                i12 = i4;
                float f4 = i12;
                if (Math.signum(f4) == Math.signum(f2)) {
                    i13 = (int) (f4 + f2);
                    i14 = (int) (f3 + f);
                    this.mMode = 1;
                    this.mScrollerX.fling(i, i14, i5, i6, i9);
                    this.mScrollerY.fling(i2, i13, i7, i8, i10);
                }
                i13 = i12;
                i14 = i11;
                this.mMode = 1;
                this.mScrollerX.fling(i, i14, i5, i6, i9);
                this.mScrollerY.fling(i2, i13, i7, i8, i10);
            }
        }
        i12 = i4;
        i13 = i12;
        i14 = i11;
        this.mMode = 1;
        this.mScrollerX.fling(i, i14, i5, i6, i9);
        this.mScrollerY.fling(i2, i13, i7, i8, i10);
    }

    public void notifyHorizontalEdgeReached(int i, int i2, int i3) {
        this.mScrollerX.notifyEdgeReached(i, i2, i3);
    }

    public void notifyVerticalEdgeReached(int i, int i2, int i3) {
        this.mScrollerY.notifyEdgeReached(i, i2, i3);
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    /* loaded from: classes3.dex */
    public static class SplineOverScroller {
        public static float DECELERATION_RATE = (float) (Math.log(0.78d) / Math.log(0.9d));
        public static final float[] SPLINE_POSITION = new float[101];
        public static final float[] SPLINE_TIME = new float[101];
        public Context mContext;
        public double mCurrVelocity;
        public double mCurrentPosition;
        public int mDuration;
        public double mFinal;
        public boolean mLastStep;
        public double mOriginStart;
        public float mPhysicalCoeff;
        public SpringOperator mSpringOperator;
        public double[] mSpringParams;
        public double mStart;
        public long mStartTime;
        public double mVelocity;
        public float mFlingFriction = ViewConfiguration.getScrollFriction();
        public int mState = 0;
        public boolean mFinished = true;

        public boolean continueWhenFinished() {
            throw null;
        }

        public void finish() {
            throw null;
        }

        public void fling(int i, int i2, int i3, int i4, int i5) {
            throw null;
        }

        public void notifyEdgeReached(int i, int i2, int i3) {
            throw null;
        }

        public boolean springback(int i, int i2, int i3) {
            throw null;
        }

        public boolean update() {
            throw null;
        }

        static {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            float f9;
            float f10;
            float f11 = 0.0f;
            float f12 = 0.0f;
            for (int i = 0; i < 100; i++) {
                float f13 = i / 100.0f;
                float f14 = 1.0f;
                while (true) {
                    f = 2.0f;
                    f2 = ((f14 - f11) / 2.0f) + f11;
                    f3 = 3.0f;
                    f4 = 1.0f - f2;
                    f5 = f2 * 3.0f * f4;
                    f6 = f2 * f2 * f2;
                    float f15 = (((f4 * 0.175f) + (f2 * 0.35000002f)) * f5) + f6;
                    if (Math.abs(f15 - f13) < 1.0E-5d) {
                        break;
                    } else if (f15 > f13) {
                        f14 = f2;
                    } else {
                        f11 = f2;
                    }
                }
                SPLINE_POSITION[i] = (f5 * ((f4 * 0.5f) + f2)) + f6;
                float f16 = 1.0f;
                while (true) {
                    f7 = ((f16 - f12) / f) + f12;
                    f8 = 1.0f - f7;
                    f9 = f7 * f3 * f8;
                    f10 = f7 * f7 * f7;
                    float f17 = (((f8 * 0.5f) + f7) * f9) + f10;
                    if (Math.abs(f17 - f13) < 1.0E-5d) {
                        break;
                    }
                    if (f17 > f13) {
                        f16 = f7;
                    } else {
                        f12 = f7;
                    }
                    f = 2.0f;
                    f3 = 3.0f;
                }
                SPLINE_TIME[i] = (f9 * ((f8 * 0.175f) + (f7 * 0.35000002f))) + f10;
            }
            float[] fArr = SPLINE_POSITION;
            SPLINE_TIME[100] = 1.0f;
            fArr[100] = 1.0f;
        }

        public SplineOverScroller(Context context) {
            this.mContext = context;
            this.mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        }

        public void updateScroll(float f) {
            double d = this.mStart;
            this.mCurrentPosition = d + Math.round(f * (this.mFinal - d));
        }

        public void startScrollByFling(float f, int i, int i2) {
            this.mFinished = false;
            this.mLastStep = false;
            setState(0);
            double d = f;
            this.mOriginStart = d;
            this.mStart = d;
            this.mCurrentPosition = d;
            this.mFinal = f + i;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            double d2 = i2;
            this.mVelocity = d2;
            this.mCurrVelocity = d2;
            SpringOperator springOperator = new SpringOperator();
            this.mSpringOperator = springOperator;
            double[] dArr = new double[2];
            this.mSpringParams = dArr;
            springOperator.getParameters(new float[]{0.99f, 0.4f}, dArr);
        }

        public boolean computeScrollOffset() {
            if (this.mSpringOperator == null || this.mFinished) {
                return false;
            }
            if (this.mLastStep) {
                this.mFinished = true;
                this.mCurrentPosition = this.mFinal;
                return true;
            }
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            double d = 0.01600000075995922d;
            double min = Math.min(((float) (currentAnimationTimeMillis - this.mStartTime)) / 1000.0f, 0.01600000075995922d);
            if (min != SearchStatUtils.POW) {
                d = min;
            }
            this.mStartTime = currentAnimationTimeMillis;
            SpringOperator springOperator = this.mSpringOperator;
            double d2 = this.mCurrVelocity;
            double[] dArr = this.mSpringParams;
            double updateVelocity = springOperator.updateVelocity(d2, dArr[0], dArr[1], d, this.mFinal, this.mStart);
            double d3 = this.mStart + (d * updateVelocity);
            this.mCurrentPosition = d3;
            this.mCurrVelocity = updateVelocity;
            if (isAtEquilibrium(d3, this.mFinal)) {
                this.mLastStep = true;
            } else {
                this.mStart = this.mCurrentPosition;
            }
            return true;
        }

        public boolean isAtEquilibrium(double d, double d2) {
            return Math.abs(d - d2) < 1.0d;
        }

        public void setFinalPosition(int i) {
            this.mFinal = i;
            this.mFinished = false;
        }

        public final boolean isFinished() {
            return this.mFinished;
        }

        public final void setFinished(boolean z) {
            this.mFinished = z;
        }

        public final int getCurrentPosition() {
            return (int) this.mCurrentPosition;
        }

        public final void setCurrentPosition(int i) {
            this.mCurrentPosition = i;
        }

        public final float getCurrVelocity() {
            return (float) this.mCurrVelocity;
        }

        public final void setCurrVelocity(float f) {
            this.mCurrVelocity = f;
        }

        public final int getStart() {
            return (int) this.mStart;
        }

        public final void setStart(int i) {
            this.mStart = i;
        }

        public final int getFinal() {
            return (int) this.mFinal;
        }

        public final void setFinal(int i) {
            this.mFinal = i;
        }

        public final void setDuration(int i) {
            this.mDuration = i;
        }

        public final int getState() {
            return this.mState;
        }

        public final void setState(int i) {
            this.mState = i;
        }

        public final void setStartTime(long j) {
            this.mStartTime = j;
        }
    }

    /* loaded from: classes3.dex */
    public static class ViscousFluidInterpolator implements Interpolator {
        public static final float VISCOUS_FLUID_NORMALIZE;
        public static final float VISCOUS_FLUID_OFFSET;

        static {
            float viscousFluid = 1.0f / viscousFluid(1.0f);
            VISCOUS_FLUID_NORMALIZE = viscousFluid;
            VISCOUS_FLUID_OFFSET = 1.0f - (viscousFluid * viscousFluid(1.0f));
        }

        public static float viscousFluid(float f) {
            float f2 = f * 8.0f;
            if (f2 < 1.0f) {
                return f2 - (1.0f - ((float) Math.exp(-f2)));
            }
            return ((1.0f - ((float) Math.exp(1.0f - f2))) * 0.63212055f) + 0.36787945f;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            float viscousFluid = VISCOUS_FLUID_NORMALIZE * viscousFluid(f);
            return viscousFluid > 0.0f ? viscousFluid + VISCOUS_FLUID_OFFSET : viscousFluid;
        }
    }
}
