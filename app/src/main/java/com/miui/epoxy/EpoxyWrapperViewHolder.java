package com.miui.epoxy;

import android.view.View;
import com.miui.epoxy.EpoxyViewHolder;

/* loaded from: classes.dex */
public abstract class EpoxyWrapperViewHolder<VH extends EpoxyViewHolder> extends EpoxyViewHolder {
    public final VH childViewHolder;

    public VH getChildViewHolder() {
        return this.childViewHolder;
    }

    public EpoxyWrapperViewHolder(View view, VH vh) {
        super(view);
        this.childViewHolder = vh;
    }
}
