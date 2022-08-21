package com.miui.itemdrag;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class ScrollOnDraggingProcessRunnable implements Runnable {
    public final WeakReference<RecyclerViewDragItemManager> mHolderRef;
    public boolean mStarted;

    public ScrollOnDraggingProcessRunnable(RecyclerViewDragItemManager recyclerViewDragItemManager) {
        this.mHolderRef = new WeakReference<>(recyclerViewDragItemManager);
    }

    public void start() {
        RecyclerViewDragItemManager recyclerViewDragItemManager;
        RecyclerView recyclerView;
        if (this.mStarted || (recyclerViewDragItemManager = this.mHolderRef.get()) == null || (recyclerView = recyclerViewDragItemManager.getRecyclerView()) == null) {
            return;
        }
        ViewCompat.postOnAnimation(recyclerView, this);
        this.mStarted = true;
    }

    public void stop() {
        if (!this.mStarted) {
            return;
        }
        this.mStarted = false;
    }

    public void release() {
        this.mHolderRef.clear();
        this.mStarted = false;
    }

    @Override // java.lang.Runnable
    public void run() {
        RecyclerViewDragItemManager recyclerViewDragItemManager = this.mHolderRef.get();
        if (recyclerViewDragItemManager != null && this.mStarted) {
            recyclerViewDragItemManager.handleScrollOnDragging();
            RecyclerView recyclerView = recyclerViewDragItemManager.getRecyclerView();
            if (recyclerView != null && this.mStarted) {
                ViewCompat.postOnAnimation(recyclerView, this);
            } else {
                this.mStarted = false;
            }
        }
    }
}
