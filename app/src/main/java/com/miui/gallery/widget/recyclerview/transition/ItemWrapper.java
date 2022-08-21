package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.RectF;

/* loaded from: classes3.dex */
public class ItemWrapper implements ITransitItem {
    public final ITransitItem mItem;
    public final int mSpanGroupIndex;
    public final int mSpanIndex;

    public ItemWrapper(ITransitItem iTransitItem, int i, int i2) {
        this.mItem = iTransitItem;
        this.mSpanGroupIndex = i;
        this.mSpanIndex = i2;
    }

    public ITransitItem getWrapped() {
        return this.mItem;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public long getTransitId() {
        return this.mItem.getTransitId();
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public RectF getTransitFrame() {
        return this.mItem.getTransitFrame();
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public void release() {
        this.mItem.release();
    }

    public boolean equals(Object obj) {
        return (obj instanceof ItemWrapper) && getTransitId() == ((ItemWrapper) obj).getTransitId();
    }

    public int hashCode() {
        return (int) getTransitId();
    }
}
