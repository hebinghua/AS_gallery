package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.SmoothScrollerController;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes3.dex */
public class CustomScrollerLinearLayoutManager extends LinearLayoutManager {
    public boolean isScrollEnable;
    public SmoothScrollerController mSmoothScrollerController;

    public CustomScrollerLinearLayoutManager(Context context) {
        super(context);
        this.isScrollEnable = true;
    }

    public CustomScrollerLinearLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
        this.isScrollEnable = true;
    }

    public CustomScrollerLinearLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.isScrollEnable = true;
    }

    public void setSmoothScroller(SmoothScrollerController smoothScrollerController) {
        this.mSmoothScrollerController = smoothScrollerController;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        SmoothScrollerController smoothScrollerController = this.mSmoothScrollerController;
        if (smoothScrollerController != null) {
            smoothScrollerController.setTargetPosition(i);
            startSmoothScroll(this.mSmoothScrollerController);
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            DefaultLogger.e("CustomScrollerLinearLayoutManager", e);
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollHorizontally() {
        return this.isScrollEnable && super.canScrollHorizontally();
    }

    public void setScrollEnable(boolean z) {
        this.isScrollEnable = z;
    }
}
