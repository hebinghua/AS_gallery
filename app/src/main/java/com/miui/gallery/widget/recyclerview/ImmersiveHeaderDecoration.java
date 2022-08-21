package com.miui.gallery.widget.recyclerview;

import android.graphics.Canvas;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class ImmersiveHeaderDecoration extends RecyclerView.ItemDecoration {
    public ImmersiveHeaderDrawer mDrawer;
    public GalleryRecyclerView mRecyclerView;
    public boolean mVisible;

    public ImmersiveHeaderDecoration(GalleryRecyclerView galleryRecyclerView, ImmersiveHeaderDrawer immersiveHeaderDrawer) {
        this.mRecyclerView = galleryRecyclerView;
        this.mDrawer = immersiveHeaderDrawer;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        ImmersiveHeaderDrawer immersiveHeaderDrawer;
        super.onDrawOver(canvas, recyclerView, state);
        if (!this.mVisible || (immersiveHeaderDrawer = this.mDrawer) == null) {
            return;
        }
        if (immersiveHeaderDrawer.getViewWidth() <= 0 || this.mDrawer.getViewWidth() != this.mRecyclerView.getWidth()) {
            this.mDrawer.setViewWidth(this.mRecyclerView.getWidth());
        }
        this.mDrawer.draw(canvas);
    }

    public void attach() {
        setupCallbacks();
        this.mRecyclerView.invalidate();
    }

    public void detach() {
        destroyCallbacks();
    }

    public final void setupCallbacks() {
        this.mRecyclerView.addItemDecoration(this);
    }

    public final void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
    }
}
