package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.RectF;

/* loaded from: classes3.dex */
public final class FakeItemWrapper extends ItemWrapper {
    public final RectF mFakeFrame;

    @Override // com.miui.gallery.widget.recyclerview.transition.ItemWrapper, com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public long getTransitId() {
        return -2147483648L;
    }

    public FakeItemWrapper(ITransitItem iTransitItem) {
        super(iTransitItem, -1, -1);
        this.mFakeFrame = new RectF(iTransitItem.getTransitFrame());
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ItemWrapper, com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public RectF getTransitFrame() {
        return this.mFakeFrame;
    }
}
