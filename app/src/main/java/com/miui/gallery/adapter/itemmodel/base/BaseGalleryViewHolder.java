package com.miui.gallery.adapter.itemmodel.base;

import android.view.View;
import com.miui.epoxy.EpoxyViewHolder;

/* loaded from: classes.dex */
public class BaseGalleryViewHolder extends EpoxyViewHolder {
    @Deprecated
    public boolean supportRecyclable() {
        return true;
    }

    public BaseGalleryViewHolder(View view) {
        super(view);
        setIsRecyclable(supportRecyclable());
    }

    public <V extends View> V findViewById(int i) {
        return (V) this.itemView.findViewById(i);
    }
}
