package com.miui.gallery.widget.recyclerview;

import androidx.recyclerview.widget.GridLayoutManager;

/* loaded from: classes3.dex */
public class IrregularSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    public final SpanSizeProvider mSpanSizeProvider;

    public IrregularSpanSizeLookup(SpanSizeProvider spanSizeProvider) {
        this.mSpanSizeProvider = spanSizeProvider;
    }

    public static GridLayoutManager.SpanSizeLookup create(SpanSizeProvider spanSizeProvider) {
        IrregularSpanSizeLookup irregularSpanSizeLookup = new IrregularSpanSizeLookup(spanSizeProvider);
        irregularSpanSizeLookup.setSpanIndexCacheEnabled(true);
        return irregularSpanSizeLookup;
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
    public int getSpanSize(int i) {
        return this.mSpanSizeProvider.getSpanSize(i);
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
    public int getSpanIndex(int i, int i2) {
        int spanIndex = this.mSpanSizeProvider.getSpanIndex(i, i2);
        return spanIndex >= 0 ? spanIndex : super.getSpanIndex(i, i2);
    }
}
