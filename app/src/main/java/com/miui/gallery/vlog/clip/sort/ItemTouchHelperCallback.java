package com.miui.gallery.vlog.clip.sort;

import android.graphics.Canvas;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.LinearMotorHelper;

/* loaded from: classes2.dex */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public boolean isCanDrag = false;
    public boolean isCanSwipe = false;
    public final RecyclerView.Adapter mAdapter;
    public boolean mLastActive;

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    }

    public ItemTouchHelperCallback(RecyclerView.Adapter adapter, boolean z, boolean z2) {
        this.mAdapter = adapter;
        setDragEnable(z);
        setSwipeEnable(z2);
    }

    public void setDragEnable(boolean z) {
        this.isCanDrag = z;
    }

    public void setSwipeEnable(boolean z) {
        this.isCanSwipe = z;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isLongPressDragEnabled() {
        return this.isCanDrag;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean isItemViewSwipeEnabled() {
        return this.isCanSwipe;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int i = 0;
        if (layoutManager instanceof GridLayoutManager) {
            int i2 = 51;
            if (!this.isCanDrag) {
                i2 = 0;
            }
            return ItemTouchHelper.Callback.makeMovementFlags(i2, 0);
        } else if (!(layoutManager instanceof LinearLayoutManager)) {
            return 0;
        } else {
            int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            int i3 = 48;
            int i4 = 3;
            if (orientation != 0) {
                if (orientation == 1) {
                    i4 = 48;
                    i3 = 3;
                } else {
                    i3 = 0;
                    i4 = 0;
                }
            }
            if (!this.isCanSwipe) {
                i4 = 0;
            }
            if (this.isCanDrag) {
                i = i3;
            }
            return ItemTouchHelper.Callback.makeMovementFlags(i, i4);
        }
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        if (this.mAdapter instanceof ItemTouchHelperAdapter) {
            LinearMotorHelper.performHapticFeedback(recyclerView, LinearMotorHelper.HAPTIC_MESH_NORMAL);
            ((ItemTouchHelperAdapter) this.mAdapter).onMove(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder == null) {
            return;
        }
        RecyclerView.Adapter adapter = this.mAdapter;
        if (!(adapter instanceof ItemTouchHelperAdapter)) {
            return;
        }
        ((ItemTouchHelperAdapter) adapter).onSelectedChanged(viewHolder, viewHolder.getAdapterPosition());
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder == null) {
            return;
        }
        RecyclerView.Adapter adapter = this.mAdapter;
        if (!(adapter instanceof ItemTouchHelperAdapter)) {
            return;
        }
        ((ItemTouchHelperAdapter) adapter).onMoveFinished(viewHolder, viewHolder.getAdapterPosition());
    }

    @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
        super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        if (this.mLastActive && !z) {
            ((ItemTouchHelperAdapter) this.mAdapter).onChildDraw(viewHolder, false);
        }
        this.mLastActive = z;
    }
}
