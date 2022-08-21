package com.miui.gallery.widget.recyclerview;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

/* loaded from: classes3.dex */
public interface GroupedItemAdapter<GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> extends com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter<GVH, CVH> {
    default int packDataPosition(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            i3 += getChildCount(i4);
        }
        return i3 + i2;
    }
}
