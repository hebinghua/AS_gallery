package miuix.core.view;

import android.view.View;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;

/* loaded from: classes3.dex */
public class NestedScrollingChildHelper extends androidx.core.view.NestedScrollingChildHelper {
    public boolean mIsNestedScrollingEnabled;
    public ViewParent mNestedScrollingParentNonTouch;
    public ViewParent mNestedScrollingParentTouch;
    public int[] mTempNestedScrollConsumed;
    public final View mView;

    public NestedScrollingChildHelper(View view) {
        super(view);
        this.mView = view;
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public void setNestedScrollingEnabled(boolean z) {
        if (this.mIsNestedScrollingEnabled) {
            ViewCompat.stopNestedScroll(this.mView);
        }
        this.mIsNestedScrollingEnabled = z;
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean isNestedScrollingEnabled() {
        return this.mIsNestedScrollingEnabled;
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean hasNestedScrollingParent() {
        return hasNestedScrollingParent(0);
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean hasNestedScrollingParent(int i) {
        return getNestedScrollingParentForType(i) != null;
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean startNestedScroll(int i) {
        return startNestedScroll(i, 0);
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean startNestedScroll(int i, int i2) {
        if (hasNestedScrollingParent(i2)) {
            return true;
        }
        if (!isNestedScrollingEnabled()) {
            return false;
        }
        View view = this.mView;
        for (ViewParent parent = this.mView.getParent(); parent != null; parent = parent.getParent()) {
            if (ViewParentCompat.onStartNestedScroll(parent, view, this.mView, i, i2)) {
                setNestedScrollingParentForType(i2, parent);
                ViewParentCompat.onNestedScrollAccepted(parent, view, this.mView, i, i2);
                return true;
            }
            if (parent instanceof View) {
                view = (View) parent;
            }
        }
        return false;
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public void stopNestedScroll() {
        stopNestedScroll(0);
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public void stopNestedScroll(int i) {
        ViewParent nestedScrollingParentForType = getNestedScrollingParentForType(i);
        if (nestedScrollingParentForType != null) {
            ViewParentCompat.onStopNestedScroll(nestedScrollingParentForType, this.mView, i);
            setNestedScrollingParentForType(i, null);
        }
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        return dispatchNestedScrollInternal(i, i2, i3, i4, iArr, 0, null);
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public void dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2) {
        dispatchNestedScrollInternal(i, i2, i3, i4, iArr, i5, iArr2);
    }

    private boolean dispatchNestedScrollInternal(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2) {
        ViewParent nestedScrollingParentForType;
        int i6;
        int i7;
        int[] iArr3;
        if (!isNestedScrollingEnabled() || (nestedScrollingParentForType = getNestedScrollingParentForType(i5)) == null) {
            return false;
        }
        if (i == 0 && i2 == 0 && i3 == 0 && i4 == 0) {
            if (iArr != null) {
                iArr[0] = 0;
                iArr[1] = 0;
            }
            return false;
        }
        if (iArr != null) {
            this.mView.getLocationInWindow(iArr);
            i6 = iArr[0];
            i7 = iArr[1];
        } else {
            i6 = 0;
            i7 = 0;
        }
        if (iArr2 == null) {
            int[] tempNestedScrollConsumed = getTempNestedScrollConsumed();
            tempNestedScrollConsumed[0] = 0;
            tempNestedScrollConsumed[1] = 0;
            iArr3 = tempNestedScrollConsumed;
        } else {
            iArr3 = iArr2;
        }
        ViewParentCompat.onNestedScroll(nestedScrollingParentForType, this.mView, i, i2, i3, i4, i5, iArr3);
        if (iArr != null) {
            this.mView.getLocationInWindow(iArr);
            iArr[0] = iArr[0] - i6;
            iArr[1] = iArr[1] - i7;
        }
        return true;
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return dispatchNestedPreScroll(i, i2, iArr, iArr2, 0);
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3) {
        ViewParent nestedScrollingParentForType;
        int i4;
        int i5;
        if (!isNestedScrollingEnabled() || (nestedScrollingParentForType = getNestedScrollingParentForType(i3)) == null) {
            return false;
        }
        if (i == 0 && i2 == 0) {
            if (iArr2 == null) {
                return false;
            }
            iArr2[0] = 0;
            iArr2[1] = 0;
            return false;
        }
        if (iArr2 != null) {
            this.mView.getLocationInWindow(iArr2);
            i4 = iArr2[0];
            i5 = iArr2[1];
        } else {
            i4 = 0;
            i5 = 0;
        }
        if (iArr == null) {
            iArr = getTempNestedScrollConsumed();
        }
        iArr[0] = 0;
        iArr[1] = 0;
        ViewParentCompat.onNestedPreScroll(nestedScrollingParentForType, this.mView, i, i2, iArr, i3);
        if (iArr2 != null) {
            this.mView.getLocationInWindow(iArr2);
            iArr2[0] = iArr2[0] - i4;
            iArr2[1] = iArr2[1] - i5;
        }
        return (iArr[0] == 0 && iArr[1] == 0) ? false : true;
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        ViewParent nestedScrollingParentForType;
        if (!isNestedScrollingEnabled() || (nestedScrollingParentForType = getNestedScrollingParentForType(0)) == null) {
            return false;
        }
        return ViewParentCompat.onNestedFling(nestedScrollingParentForType, this.mView, f, f2, z);
    }

    @Override // androidx.core.view.NestedScrollingChildHelper
    public boolean dispatchNestedPreFling(float f, float f2) {
        ViewParent nestedScrollingParentForType;
        if (!isNestedScrollingEnabled() || (nestedScrollingParentForType = getNestedScrollingParentForType(0)) == null) {
            return false;
        }
        return ViewParentCompat.onNestedPreFling(nestedScrollingParentForType, this.mView, f, f2);
    }

    private ViewParent getNestedScrollingParentForType(int i) {
        if (i != 0) {
            if (i == 1) {
                return this.mNestedScrollingParentNonTouch;
            }
            return null;
        }
        return this.mNestedScrollingParentTouch;
    }

    private void setNestedScrollingParentForType(int i, ViewParent viewParent) {
        if (i == 0) {
            this.mNestedScrollingParentTouch = viewParent;
        } else if (i != 1) {
        } else {
            this.mNestedScrollingParentNonTouch = viewParent;
        }
    }

    private int[] getTempNestedScrollConsumed() {
        if (this.mTempNestedScrollConsumed == null) {
            this.mTempNestedScrollConsumed = new int[2];
        }
        return this.mTempNestedScrollConsumed;
    }

    public static boolean isSupportMiuiNestedScrollingParent() {
        try {
            Class.forName("miui.core.view.NestedScrollingParent3");
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static androidx.core.view.NestedScrollingChildHelper obtain(View view) {
        if (isSupportMiuiNestedScrollingParent()) {
            return new NestedScrollingChildHelper(view);
        }
        return new androidx.core.view.NestedScrollingChildHelper(view);
    }
}
