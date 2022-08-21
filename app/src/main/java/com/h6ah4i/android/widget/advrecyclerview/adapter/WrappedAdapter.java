package com.h6ah4i.android.widget.advrecyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

/* loaded from: classes.dex */
public interface WrappedAdapter<VH extends RecyclerView.ViewHolder> {
    boolean onFailedToRecycleView(VH vh, int i);

    void onViewAttachedToWindow(VH vh, int i);

    void onViewDetachedFromWindow(VH vh, int i);

    void onViewRecycled(VH vh, int i);
}
