package miuix.spring.view;

import androidx.annotation.Keep;

/* loaded from: classes3.dex */
public abstract class SpringHelper {
    public AxisHandler mHorizontal = new AxisHandler(0) { // from class: miuix.spring.view.SpringHelper.1
        @Override // miuix.spring.view.SpringHelper.AxisHandler
        public boolean canScroll() {
            return SpringHelper.this.canScrollHorizontally();
        }

        @Override // miuix.spring.view.SpringHelper.AxisHandler
        public int getSize() {
            return SpringHelper.this.getWidth();
        }

        @Override // miuix.spring.view.SpringHelper.AxisHandler
        public void onFlingReachEdge() {
            SpringHelper.this.vibrate();
        }
    };
    public AxisHandler mVertical = new AxisHandler(1) { // from class: miuix.spring.view.SpringHelper.2
        @Override // miuix.spring.view.SpringHelper.AxisHandler
        public boolean canScroll() {
            return SpringHelper.this.canScrollVertically();
        }

        @Override // miuix.spring.view.SpringHelper.AxisHandler
        public int getSize() {
            return SpringHelper.this.getHeight();
        }

        @Override // miuix.spring.view.SpringHelper.AxisHandler
        public void onFlingReachEdge() {
            SpringHelper.this.vibrate();
        }
    };

    public abstract boolean canScrollHorizontally();

    public abstract boolean canScrollVertically();

    public abstract boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3);

    public abstract void dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2);

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract boolean springAvailable();

    @Keep
    public abstract void vibrate();

    public int getHorizontalDistance() {
        return (int) this.mHorizontal.mDistance;
    }

    public int getVerticalDistance() {
        return (int) this.mVertical.mDistance;
    }

    public boolean handleNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3) {
        int i4;
        int i5;
        boolean z;
        int[] iArr3 = {0, 0};
        if (springAvailable()) {
            boolean z2 = i3 == 0;
            int[] iArr4 = {i, i2};
            boolean handleNestedPreScroll = this.mVertical.handleNestedPreScroll(iArr4, iArr3, z2) | this.mHorizontal.handleNestedPreScroll(iArr4, iArr3, z2);
            i4 = iArr4[0];
            i5 = iArr4[1];
            z = handleNestedPreScroll;
        } else {
            i4 = i;
            i5 = i2;
            z = false;
        }
        if (z) {
            i4 -= iArr3[0];
            i5 -= iArr3[1];
        }
        boolean dispatchNestedPreScroll = dispatchNestedPreScroll(i4, i5, iArr, iArr2, i3) | z;
        if (iArr != null) {
            iArr[0] = iArr[0] + iArr3[0];
            iArr[1] = iArr[1] + iArr3[1];
        }
        return dispatchNestedPreScroll;
    }

    public void handleNestedScroll(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2) {
        if (iArr2 == null) {
            iArr2 = new int[]{0, 0};
        }
        dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
        int i6 = i3 - iArr2[0];
        int i7 = i4 - iArr2[1];
        if (i6 == 0 && i7 == 0) {
            return;
        }
        this.mHorizontal.handleNestedScroll(i6, iArr, i5, iArr2);
        this.mVertical.handleNestedScroll(i7, iArr, i5, iArr2);
    }

    /* loaded from: classes3.dex */
    public abstract class AxisHandler {
        public float mAllDistance;
        public int mAxis;
        public float mDistance;

        public abstract boolean canScroll();

        public abstract int getSize();

        public abstract void onFlingReachEdge();

        public AxisHandler(int i) {
            this.mAxis = i;
        }

        public boolean handleNestedPreScroll(int[] iArr, int[] iArr2, boolean z) {
            int i = iArr[this.mAxis];
            if (i != 0 && canScroll()) {
                float f = this.mDistance;
                if (f == 0.0f || Integer.signum((int) f) * i > 0) {
                    return false;
                }
                iArr[this.mAxis] = release(i, iArr2, z);
                return true;
            }
            return false;
        }

        public void handleNestedScroll(int i, int[] iArr, int i2, int[] iArr2) {
            if (SpringHelper.this.springAvailable()) {
                pull(i, iArr2, i2 == 0);
            }
        }

        public final void pull(int i, int[] iArr, boolean z) {
            if (i == 0 || !canScroll()) {
                return;
            }
            float f = i;
            float f2 = this.mAllDistance + f;
            this.mAllDistance = f2;
            if (z) {
                this.mDistance = Math.signum(f2) * obtainSpringBackDistance(Math.abs(this.mAllDistance));
            } else {
                if (this.mDistance == 0.0f) {
                    onFlingReachEdge();
                }
                float f3 = this.mDistance + f;
                this.mDistance = f3;
                this.mAllDistance = Math.signum(f3) * unObtainSpringBackDistance(Math.abs(this.mDistance));
            }
            int i2 = this.mAxis;
            iArr[i2] = iArr[i2] + i;
        }

        public final int release(int i, int[] iArr, boolean z) {
            int i2;
            float f = this.mDistance;
            float f2 = this.mAllDistance;
            float signum = Math.signum(f);
            float f3 = this.mAllDistance + i;
            this.mAllDistance = f3;
            if (z) {
                this.mDistance = Math.signum(f3) * obtainSpringBackDistance(Math.abs(this.mAllDistance));
                int i3 = this.mAxis;
                iArr[i3] = iArr[i3] + (i - i);
            }
            int i4 = (int) (this.mDistance + (this.mAllDistance - f2));
            float f4 = i4;
            if (signum * f4 >= 0.0f) {
                if (!z) {
                    this.mDistance = f4;
                }
                iArr[this.mAxis] = i;
            } else {
                this.mDistance = 0.0f;
                iArr[this.mAxis] = (int) (iArr[i2] + f);
            }
            float f5 = this.mDistance;
            if (f5 == 0.0f) {
                this.mAllDistance = 0.0f;
            }
            if (!z) {
                this.mAllDistance = Math.signum(f5) * unObtainSpringBackDistance(Math.abs(this.mDistance));
            }
            return i4;
        }

        public final float obtainSpringBackDistance(float f) {
            float f2;
            float pow;
            int size = getSize();
            if (size == 0) {
                pow = Math.abs(f);
                f2 = 0.5f;
            } else {
                f2 = size;
                double min = Math.min(Math.abs(f) / f2, 1.0f);
                pow = (float) (((Math.pow(min, 3.0d) / 3.0d) - Math.pow(min, 2.0d)) + min);
            }
            return pow * f2;
        }

        public final float unObtainSpringBackDistance(float f) {
            float f2;
            int size = getSize();
            if (size == 0) {
                return Math.abs(f) * 2.0f;
            }
            if (Math.abs(f) / size > 0.33333334f) {
                return f * 3.0f;
            }
            double d = size;
            return (float) (d - (Math.pow(d, 0.6666666865348816d) * Math.pow(f2 - (Math.abs(f) * 3.0f), 0.3333333432674408d)));
        }
    }
}
