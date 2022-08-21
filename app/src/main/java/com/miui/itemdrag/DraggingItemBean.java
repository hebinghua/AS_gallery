package com.miui.itemdrag;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class DraggingItemBean {
    public final int grabbedPositionX;
    public final int grabbedPositionY;
    public final int height;
    public final long id;
    public final int initialItemLeft;
    public final int initialItemTop;
    public int mDraggingItemRealPosition;
    public final Rect margins;
    public final int spanSize;
    public RecyclerView.ViewHolder viewHolder;
    public final int width;

    public DraggingItemBean(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int i, int i2) {
        this.viewHolder = viewHolder;
        this.width = viewHolder.itemView.getWidth();
        this.height = viewHolder.itemView.getHeight();
        this.id = viewHolder.getItemId();
        int left = viewHolder.itemView.getLeft();
        this.initialItemLeft = left;
        int top = viewHolder.itemView.getTop();
        this.initialItemTop = top;
        this.grabbedPositionX = i - left;
        this.grabbedPositionY = i2 - top;
        this.mDraggingItemRealPosition = viewHolder.getLayoutPosition();
        Rect rect = new Rect();
        this.margins = rect;
        RecyclerViewUtils.getLayoutMargins(viewHolder.itemView, rect);
        this.spanSize = RecyclerViewUtils.getSpanSize(viewHolder);
    }
}
