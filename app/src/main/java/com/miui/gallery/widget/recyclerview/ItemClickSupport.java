package com.miui.gallery.widget.recyclerview;

import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;

/* loaded from: classes3.dex */
public class ItemClickSupport {
    public boolean mIsHandleEvent = true;
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mItemLongClickListener;
    public final RecyclerView mRecyclerView;
    public final TouchListener mTouchListener;

    /* loaded from: classes3.dex */
    public interface OnItemClickListener {
        boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2);
    }

    /* loaded from: classes3.dex */
    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z);
    }

    public ItemClickSupport(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        this.mTouchListener = new TouchListener(recyclerView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        if (!this.mRecyclerView.isLongClickable()) {
            this.mRecyclerView.setLongClickable(true);
        }
        this.mItemLongClickListener = onItemLongClickListener;
    }

    public boolean onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (!this.mIsHandleEvent) {
            return false;
        }
        return this.mTouchListener.onTouchEvent(recyclerView, motionEvent);
    }

    public static ItemClickSupport addTo(RecyclerView recyclerView) {
        ItemClickSupport from = from(recyclerView);
        if (from == null) {
            ItemClickSupport itemClickSupport = new ItemClickSupport(recyclerView);
            recyclerView.setTag(R.id.tag_item_click_support, itemClickSupport);
            return itemClickSupport;
        }
        return from;
    }

    public static void removeFromTemp(RecyclerView recyclerView) {
        ItemClickSupport from = from(recyclerView);
        if (from == null) {
            return;
        }
        from.mIsHandleEvent = false;
    }

    public static void recoverFromTemp(RecyclerView recyclerView) {
        ItemClickSupport from = from(recyclerView);
        if (from == null) {
            return;
        }
        from.mIsHandleEvent = true;
    }

    public static ItemClickSupport from(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return null;
        }
        return (ItemClickSupport) recyclerView.getTag(R.id.tag_item_click_support);
    }

    public void setIsClickedItemRecyclable(boolean z) {
        this.mTouchListener.setIsClickedItemRecyclable(z);
    }

    public void setTakeOverUnhandledLongPress(boolean z) {
        this.mTouchListener.setTakeOverUnhandledLongPress(z);
    }

    /* loaded from: classes3.dex */
    public class TouchListener extends ClickItemTouchListener {
        public TouchListener(RecyclerView recyclerView) {
            super(recyclerView);
        }

        @Override // com.miui.gallery.widget.recyclerview.ClickItemTouchListener
        public boolean performItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
            if (ItemClickSupport.this.mItemClickListener != null) {
                boolean onItemClick = ItemClickSupport.this.mItemClickListener.onItemClick(recyclerView, view, i, j, f, f2);
                if (onItemClick) {
                    view.playSoundEffect(0);
                }
                return onItemClick;
            }
            return false;
        }

        @Override // com.miui.gallery.widget.recyclerview.ClickItemTouchListener
        public boolean performItemLongClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z) {
            if (ItemClickSupport.this.mItemLongClickListener != null) {
                boolean onItemLongClick = ItemClickSupport.this.mItemLongClickListener.onItemLongClick(recyclerView, view, i, j, f, f2, z);
                if (onItemLongClick) {
                    recyclerView.performHapticFeedback(0);
                }
                return onItemLongClick;
            }
            return false;
        }
    }
}
