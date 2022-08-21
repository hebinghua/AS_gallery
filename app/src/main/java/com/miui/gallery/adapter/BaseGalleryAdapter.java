package com.miui.gallery.adapter;

import android.content.Context;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;

/* loaded from: classes.dex */
public abstract class BaseGalleryAdapter<M, VH extends BaseViewHolder> extends BaseRecyclerAdapter<M, VH> {
    public Context mContext;

    public BaseGalleryAdapter(Context context) {
        this.mContext = context;
    }
}
