package com.miui.itemdrag.animator.impl;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class RemoveAnimationInfo extends ItemAnimationInfo {
    public RecyclerView.ViewHolder holder;

    public RemoveAnimationInfo(RecyclerView.ViewHolder viewHolder) {
        this.holder = viewHolder;
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
        return "RemoveAnimationInfo{holder=" + this.holder + '}';
    }
}
