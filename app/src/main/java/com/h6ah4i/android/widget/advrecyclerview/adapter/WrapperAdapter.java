package com.h6ah4i.android.widget.advrecyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.List;

/* loaded from: classes.dex */
public interface WrapperAdapter<VH extends RecyclerView.ViewHolder> extends WrappedAdapter<VH> {
    void getWrappedAdapters(List<RecyclerView.Adapter> list);
}
