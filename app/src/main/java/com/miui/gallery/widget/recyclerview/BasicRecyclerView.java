package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;

/* loaded from: classes3.dex */
public class BasicRecyclerView extends RecyclerView implements ItemClickSupport.OnItemLongClickListener {
    public ItemClickSupport mItemClickSupport;
    public ItemClickSupport.OnItemLongClickListener mOnItemLongClickListener;

    public BasicRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BasicRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initItemClick();
    }

    public void initItemClick() {
        ItemClickSupport addTo = ItemClickSupport.addTo(this);
        this.mItemClickSupport = addTo;
        addTo.setOnItemLongClickListener(this);
    }

    public void setOnItemClickListener(ItemClickSupport.OnItemClickListener onItemClickListener) {
        this.mItemClickSupport.setOnItemClickListener(onItemClickListener);
    }

    public void setOnItemLongClickListener(ItemClickSupport.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemLongClickListener
    public boolean onItemLongClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z) {
        ItemClickSupport.OnItemLongClickListener onItemLongClickListener = this.mOnItemLongClickListener;
        if (onItemLongClickListener != null) {
            return onItemLongClickListener.onItemLongClick(recyclerView, view, i, j, f, f2, z);
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (getScrollState() != 0) {
            return super.onTouchEvent(motionEvent);
        }
        if (super.onTouchEvent(motionEvent)) {
            return this.mItemClickSupport.onTouchEvent(this, motionEvent) | true;
        }
        return this.mItemClickSupport.onTouchEvent(this, motionEvent);
    }
}
