package com.miui.gallery.widget.recyclerview.transition;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public interface HeaderTransitItem extends ITransitItem {
    RecyclerView.ViewHolder getViewHolder();

    boolean isSticky();
}
