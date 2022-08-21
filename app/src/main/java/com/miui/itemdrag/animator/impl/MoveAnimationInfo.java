package com.miui.itemdrag.animator.impl;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class MoveAnimationInfo extends ItemAnimationInfo {
    public final int fromX;
    public final int fromY;
    public RecyclerView.ViewHolder holder;
    public final int toX;
    public final int toY;

    public MoveAnimationInfo(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        this.holder = viewHolder;
        this.fromX = i;
        this.fromY = i2;
        this.toX = i3;
        this.toY = i4;
    }

    @Override // com.miui.itemdrag.animator.impl.ItemAnimationInfo
    public RecyclerView.ViewHolder getAvailableViewHolder() {
        return this.holder;
    }

    @Override // com.miui.itemdrag.animator.impl.ItemAnimationInfo
    public void clear(RecyclerView.ViewHolder viewHolder) {
        if (this.holder == viewHolder) {
            this.holder = null;
        }
    }

    public String toString() {
        return "MoveAnimationInfo{holder=" + this.holder + ", fromX=" + this.fromX + ", fromY=" + this.fromY + ", toX=" + this.toX + ", toY=" + this.toY + '}';
    }
}
