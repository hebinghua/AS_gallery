package com.miui.itemdrag.animator.impl;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class ChangeAnimationInfo extends ItemAnimationInfo {
    public int fromX;
    public int fromY;
    public RecyclerView.ViewHolder newHolder;
    public RecyclerView.ViewHolder oldHolder;
    public int toX;
    public int toY;

    public ChangeAnimationInfo(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
        this.oldHolder = viewHolder;
        this.newHolder = viewHolder2;
        this.fromX = i;
        this.fromY = i2;
        this.toX = i3;
        this.toY = i4;
    }

    @Override // com.miui.itemdrag.animator.impl.ItemAnimationInfo
    public RecyclerView.ViewHolder getAvailableViewHolder() {
        RecyclerView.ViewHolder viewHolder = this.oldHolder;
        return viewHolder != null ? viewHolder : this.newHolder;
    }

    @Override // com.miui.itemdrag.animator.impl.ItemAnimationInfo
    public void clear(RecyclerView.ViewHolder viewHolder) {
        if (this.oldHolder == viewHolder) {
            this.oldHolder = null;
        }
        if (this.newHolder == viewHolder) {
            this.newHolder = null;
        }
        if (this.oldHolder == null && this.newHolder == null) {
            this.fromX = 0;
            this.fromY = 0;
            this.toX = 0;
            this.toY = 0;
        }
    }

    public String toString() {
        return "ChangeInfo{, oldHolder=" + this.oldHolder + ", newHolder=" + this.newHolder + ", fromX=" + this.fromX + ", fromY=" + this.fromY + ", toX=" + this.toX + ", toY=" + this.toY + '}';
    }
}
