package com.miui.gallery.ui;

import android.content.Context;
import android.graphics.PointF;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;

/* loaded from: classes2.dex */
public abstract class SmoothScrollerController extends LinearSmoothScroller {
    public SmoothScrollerController(Context context) {
        super(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
    public PointF computeScrollVectorForPosition(int i) {
        return getLayoutManager() instanceof LinearLayoutManager ? ((LinearLayoutManager) getLayoutManager()).computeScrollVectorForPosition(i) : new PointF();
    }
}
