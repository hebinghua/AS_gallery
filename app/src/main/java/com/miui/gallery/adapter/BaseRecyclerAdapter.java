package com.miui.gallery.adapter;

import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;

/* loaded from: classes.dex */
public abstract class BaseRecyclerAdapter<M, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> implements IBaseRecyclerAdapter<M> {
    /* renamed from: getItem */
    public abstract M mo1558getItem(int i);
}
