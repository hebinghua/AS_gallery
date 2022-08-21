package com.miui.gallery.magic.widget.scroll;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.SmoothScrollerController;

/* loaded from: classes2.dex */
public class ScrollLinearLayoutManager extends LinearLayoutManager {
    public SmoothScrollerController mSmoothScrollerController;

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollLinearLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
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
}
