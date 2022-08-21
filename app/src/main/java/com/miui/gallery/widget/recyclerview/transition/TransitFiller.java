package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.RectF;

/* loaded from: classes3.dex */
public final class TransitFiller implements ITransitItem {
    public final RectF mFrame = new RectF();

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public long getTransitId() {
        return -2147483648L;
    }

    public static TransitFiller obtain(RectF rectF) {
        TransitFiller transitFiller = new TransitFiller();
        transitFiller.mFrame.set(rectF);
        return transitFiller;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public RectF getTransitFrame() {
        return this.mFrame;
    }
}
