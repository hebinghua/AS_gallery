package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.miui.gallery.ui.CenterSmoothScrollerController;

/* loaded from: classes2.dex */
public class CentralStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    public CenterSmoothScrollerController mSmoothScrollerController;

    public CentralStaggeredGridLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mSmoothScrollerController = new CenterSmoothScrollerController(context);
    }

    @Override // androidx.recyclerview.widget.StaggeredGridLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
        super.smoothScrollToPosition(recyclerView, state, i);
        CenterSmoothScrollerController centerSmoothScrollerController = this.mSmoothScrollerController;
        if (centerSmoothScrollerController != null) {
            centerSmoothScrollerController.setTargetPosition(i);
            startSmoothScroll(this.mSmoothScrollerController);
        }
    }
}
