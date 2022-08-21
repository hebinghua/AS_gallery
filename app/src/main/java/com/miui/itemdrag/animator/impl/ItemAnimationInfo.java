package com.miui.itemdrag.animator.impl;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public abstract class ItemAnimationInfo {
    public abstract void clear(RecyclerView.ViewHolder viewHolder);

    public abstract RecyclerView.ViewHolder getAvailableViewHolder();
}
