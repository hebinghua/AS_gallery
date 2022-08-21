package com.nexstreaming.nexeditorsdk;

import android.animation.TimeInterpolator;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;

/* loaded from: classes3.dex */
public abstract class nexAnimate {
    public static final int kCoordinateX = 1;
    public static final int kCoordinateY = 2;
    public static final int kCoordinateZ = 3;
    public int mDuration;
    public int mStartTime;
    private TimeInterpolator mTimeInterpolator;
    public int mdX = 0;
    public int mdY = 0;
    public int mdZ = 0;
    public float mAlpha = 1.0f;
    public float mScaledX = 1.0f;
    public float mScaledY = 1.0f;
    public float mScaledZ = 1.0f;
    public float mRotateDegreeX = 0.0f;
    public float mRotateDegreeY = 0.0f;
    public float mRotateDegreeZ = 0.0f;

    /* loaded from: classes3.dex */
    public interface MoveTrackingPath {
        float getTranslatePosition(int i, float f);
    }

    public int getImageResourceId(int i) {
        return 0;
    }

    public boolean onFreeTypeAnimate(int i, nexOverlayItem nexoverlayitem) {
        return false;
    }

    public void setTime(int i, int i2) {
        if (i2 <= 0) {
            throw new InvalidRangeException(i2);
        }
        this.mStartTime = i;
        this.mDuration = i2;
    }

    public nexAnimate setInterpolator(TimeInterpolator timeInterpolator) {
        this.mTimeInterpolator = timeInterpolator;
        return this;
    }

    public nexAnimate(int i, int i2) {
        if (i2 <= 0) {
            throw new InvalidRangeException(i2);
        }
        this.mStartTime = i;
        this.mDuration = i2;
    }

    public float timeRatio(int i) {
        float f = (i - this.mStartTime) / this.mDuration;
        Log.d("timeRatio", "timeRatio =" + f + ",mTime = " + i);
        if (f > 1.0f) {
            f = 1.0f;
        }
        if (f < 0.0f) {
            f = 0.0f;
        }
        TimeInterpolator timeInterpolator = this.mTimeInterpolator;
        return timeInterpolator != null ? timeInterpolator.getInterpolation(f) : f;
    }

    public int getEndTime() {
        return this.mStartTime + this.mDuration;
    }

    public float getTranslatePosition(int i, int i2) {
        int i3;
        if (i2 == 1) {
            i3 = this.mdX;
        } else if (i2 == 2) {
            i3 = this.mdY;
        } else if (i2 != 3) {
            return 0.0f;
        } else {
            i3 = this.mdZ;
        }
        return i3;
    }

    public float getAlpha(int i) {
        return this.mAlpha;
    }

    public float getAngleDegree(int i, float f, int i2) {
        if (i2 == 1) {
            return this.mRotateDegreeX;
        }
        if (i2 == 2) {
            return this.mRotateDegreeY;
        }
        if (i2 != 3) {
            return 0.0f;
        }
        return this.mRotateDegreeZ;
    }

    public float getScaledRatio(int i, float f, int i2) {
        if (i2 == 1) {
            return this.mScaledX;
        }
        if (i2 == 2) {
            return this.mScaledY;
        }
        if (i2 != 3) {
            return 1.0f;
        }
        return this.mScaledZ;
    }

    public void resetFreeTypeAnimate() {
        this.mdX = 0;
        this.mdY = 0;
        this.mdZ = 0;
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
    }

    public static nexAnimate getAnimateImages(int i, int i2, int... iArr) {
        return new AnimateImages(i, i2, iArr);
    }

    /* loaded from: classes3.dex */
    public static class AnimateImages extends nexAnimate {
        private final int[] resourceIds;

        public AnimateImages(int i, int i2, int... iArr) {
            super(i, i2);
            this.resourceIds = iArr;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAnimate
        public int getImageResourceId(int i) {
            int[] iArr = this.resourceIds;
            int length = ((i - this.mStartTime) / 33) % iArr.length;
            if (length < 0) {
                length = 0;
            }
            return iArr[length];
        }
    }

    public static nexAnimate getMove(int i, int i2, MoveTrackingPath moveTrackingPath) {
        return new Move(i, i2, moveTrackingPath);
    }

    public static nexAnimate getAlpha(int i, int i2, float f, float f2) {
        return new Alpha(i, i2, f, f2);
    }

    /* loaded from: classes3.dex */
    public static class Move extends nexAnimate {
        private MoveTrackingPath mPath;

        public Move(int i, int i2, MoveTrackingPath moveTrackingPath) {
            super(i, i2);
            this.mPath = moveTrackingPath;
            if (moveTrackingPath == null) {
                this.mPath = new MoveTrackingPath() { // from class: com.nexstreaming.nexeditorsdk.nexAnimate.Move.1
                    @Override // com.nexstreaming.nexeditorsdk.nexAnimate.MoveTrackingPath
                    public float getTranslatePosition(int i3, float f) {
                        return 0.0f;
                    }
                };
            }
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAnimate
        public float getTranslatePosition(int i, int i2) {
            return this.mPath.getTranslatePosition(i2, timeRatio(i));
        }
    }

    /* loaded from: classes3.dex */
    public static class Alpha extends nexAnimate {
        private final float mEnd;
        private final float mStart;

        public Alpha(int i, int i2, float f, float f2) {
            super(i, i2);
            this.mStart = f;
            this.mEnd = f2;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAnimate
        public float getAlpha(int i) {
            float timeRatio = ((this.mEnd - this.mStart) * timeRatio(i)) + this.mStart;
            if (timeRatio > 1.0f) {
                return 1.0f;
            }
            if (timeRatio >= 0.0f) {
                return timeRatio;
            }
            return 0.0f;
        }
    }

    public static nexAnimate getRotate(int i, int i2, boolean z, float f, MoveTrackingPath moveTrackingPath) {
        return new Rotate(i, i2, z, f, 3, moveTrackingPath);
    }

    /* loaded from: classes3.dex */
    public static class Rotate extends nexAnimate {
        private MoveTrackingPath mCenter;
        private final boolean mClockWise;
        private final float mXAxisRotateDegree;
        private final float mYAxisRotateDegree;
        private final float mZAxisRotateDegree;

        public Rotate(int i, int i2, boolean z, float f, int i3, MoveTrackingPath moveTrackingPath) {
            super(i, i2);
            this.mClockWise = z;
            if (i3 == 1) {
                this.mXAxisRotateDegree = f;
                this.mYAxisRotateDegree = 0.0f;
                this.mZAxisRotateDegree = 0.0f;
            } else if (i3 == 2) {
                this.mXAxisRotateDegree = 0.0f;
                this.mYAxisRotateDegree = f;
                this.mZAxisRotateDegree = 0.0f;
            } else {
                this.mXAxisRotateDegree = 0.0f;
                this.mYAxisRotateDegree = 0.0f;
                this.mZAxisRotateDegree = f;
            }
            this.mCenter = moveTrackingPath;
            if (moveTrackingPath == null) {
                this.mCenter = new MoveTrackingPath() { // from class: com.nexstreaming.nexeditorsdk.nexAnimate.Rotate.1
                    @Override // com.nexstreaming.nexeditorsdk.nexAnimate.MoveTrackingPath
                    public float getTranslatePosition(int i4, float f2) {
                        return 0.0f;
                    }
                };
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x0024  */
        @Override // com.nexstreaming.nexeditorsdk.nexAnimate
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public float getAngleDegree(int r2, float r3, int r4) {
            /*
                r1 = this;
                r0 = 3
                if (r4 != r0) goto Lb
                float r4 = r1.mZAxisRotateDegree
                float r2 = r1.timeRatio(r2)
            L9:
                float r4 = r4 * r2
                goto L20
            Lb:
                r0 = 1
                if (r4 != r0) goto L15
                float r4 = r1.mXAxisRotateDegree
                float r2 = r1.timeRatio(r2)
                goto L9
            L15:
                r0 = 2
                if (r4 != r0) goto L1f
                float r4 = r1.mYAxisRotateDegree
                float r2 = r1.timeRatio(r2)
                goto L9
            L1f:
                r4 = 0
            L20:
                boolean r2 = r1.mClockWise
                if (r2 != 0) goto L27
                r2 = -1082130432(0xffffffffbf800000, float:-1.0)
                float r4 = r4 * r2
            L27:
                float r3 = r3 + r4
                r2 = 1135869952(0x43b40000, float:360.0)
                float r3 = r3 % r2
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexAnimate.Rotate.getAngleDegree(int, float, int):float");
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAnimate
        public float getTranslatePosition(int i, int i2) {
            return this.mCenter.getTranslatePosition(i2, timeRatio(i));
        }
    }

    public static nexAnimate getScale(int i, int i2, float f, float f2) {
        return new Scale(i, i2, f, f2, 1.0f);
    }

    public static nexAnimate getScale(int i, int i2, float f, float f2, float f3, float f4) {
        return new Scale(i, i2, f, f2, 1.0f, f3, f4, 1.0f);
    }

    public static nexAnimate getScale(int i, int i2, MoveTrackingPath moveTrackingPath) {
        return new Scale(i, i2, moveTrackingPath);
    }

    /* loaded from: classes3.dex */
    public static class Scale extends nexAnimate {
        public final float mLastScaledX;
        public final float mLastScaledY;
        public final float mLastScaledZ;
        private MoveTrackingPath mPath;
        public final boolean mSetStart;
        public final float mStartScaledX;
        public final float mStartScaledY;
        public final float mStartScaledZ;

        public Scale(int i, int i2, float f, float f2, float f3) {
            super(i, i2);
            this.mStartScaledX = 0.0f;
            this.mStartScaledY = 0.0f;
            this.mStartScaledZ = 0.0f;
            this.mLastScaledX = f;
            this.mLastScaledY = f2;
            this.mLastScaledZ = f3;
            this.mSetStart = false;
        }

        public Scale(int i, int i2, float f, float f2, float f3, float f4, float f5, float f6) {
            super(i, i2);
            this.mStartScaledX = f;
            this.mStartScaledY = f2;
            this.mStartScaledZ = f3;
            this.mLastScaledX = f4;
            this.mLastScaledY = f5;
            this.mLastScaledZ = f6;
            this.mSetStart = true;
        }

        public Scale(int i, int i2, MoveTrackingPath moveTrackingPath) {
            super(i, i2);
            this.mStartScaledX = 0.0f;
            this.mStartScaledY = 0.0f;
            this.mStartScaledZ = 0.0f;
            this.mLastScaledX = 0.0f;
            this.mLastScaledY = 0.0f;
            this.mLastScaledZ = 0.0f;
            this.mPath = moveTrackingPath;
            this.mSetStart = false;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAnimate
        public float getScaledRatio(int i, float f, int i2) {
            MoveTrackingPath moveTrackingPath = this.mPath;
            if (moveTrackingPath != null) {
                return f + moveTrackingPath.getTranslatePosition(i2, timeRatio(i));
            }
            float f2 = 0.0f;
            if (i2 == 1) {
                f2 = this.mLastScaledX;
                if (this.mSetStart) {
                    f = this.mStartScaledX;
                }
            } else if (i2 == 2) {
                f2 = this.mLastScaledY;
                if (this.mSetStart) {
                    f = this.mStartScaledY;
                }
            } else if (i2 == 3) {
                f2 = this.mLastScaledZ;
                if (this.mSetStart) {
                    f = this.mStartScaledZ;
                }
            }
            if (f2 > f) {
                return f + ((f2 - f) * timeRatio(i));
            }
            return f - ((f - f2) * timeRatio(i));
        }
    }
}
