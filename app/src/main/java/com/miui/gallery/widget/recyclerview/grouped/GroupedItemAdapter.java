package com.miui.gallery.widget.recyclerview.grouped;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.List;

/* loaded from: classes3.dex */
public interface GroupedItemAdapter<GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> {
    int getChildCount(int i);

    long getChildId(int i, int i2);

    int getChildItemViewType(int i, int i2);

    int getGroupCount();

    long getGroupId(int i);

    int getGroupItemViewType(int i);

    boolean isFlatten();

    void onBindChildViewHolder(CVH cvh, int i, int i2, int i3, List<Object> list);

    void onBindGroupViewHolder(GVH gvh, int i, int i2);

    void onBindGroupViewHolder(GVH gvh, int i, int i2, List<Object> list);

    /* renamed from: onCreateChildViewHolder */
    CVH mo1337onCreateChildViewHolder(ViewGroup viewGroup, int i);

    /* renamed from: onCreateGroupViewHolder */
    GVH mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i);
}
