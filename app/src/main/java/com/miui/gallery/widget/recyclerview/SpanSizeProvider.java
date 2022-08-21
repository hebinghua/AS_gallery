package com.miui.gallery.widget.recyclerview;

/* loaded from: classes3.dex */
public interface SpanSizeProvider {
    default int getSpanIndex(int i, int i2) {
        return -1;
    }

    default int getSpanSize(int i) {
        return 1;
    }
}
