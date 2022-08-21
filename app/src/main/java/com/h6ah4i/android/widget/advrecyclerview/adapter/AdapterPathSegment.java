package com.h6ah4i.android.widget.advrecyclerview.adapter;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class AdapterPathSegment {
    public final RecyclerView.Adapter adapter;
    public final Object tag;

    public AdapterPathSegment(RecyclerView.Adapter adapter, Object obj) {
        this.adapter = adapter;
        this.tag = obj;
    }
}
