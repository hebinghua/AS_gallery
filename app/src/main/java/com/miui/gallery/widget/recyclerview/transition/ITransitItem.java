package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.RectF;

/* loaded from: classes3.dex */
public interface ITransitItem {
    RectF getTransitFrame();

    long getTransitId();

    default void release() {
    }
}
