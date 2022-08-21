package com.miui.gallery.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

/* loaded from: classes2.dex */
public class CenterSmoothScrollerController extends SmoothScrollerController {
    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return 0.5f;
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    public int getVerticalSnapPreference() {
        return -1;
    }

    public CenterSmoothScrollerController(Context context) {
        super(context);
    }

    @Override // androidx.recyclerview.widget.LinearSmoothScroller
    public int calculateDtToFit(int i, int i2, int i3, int i4, int i5) {
        View findViewByPosition;
        if (getLayoutManager() != null && (findViewByPosition = getLayoutManager().findViewByPosition(getTargetPosition())) != null) {
            i += getLayoutManager().getLeftDecorationWidth(findViewByPosition);
            i2 -= getLayoutManager().getRightDecorationWidth(findViewByPosition);
        }
        return ((i4 + i3) / 2) - ((i2 + i) / 2);
    }
}
