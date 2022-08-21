package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Canvas;
import android.graphics.RectF;

/* loaded from: classes3.dex */
public interface ITransitionRender {
    void draw(Canvas canvas, float f, float f2);

    RectF getFromFrame();

    RectF getToFrame();

    default void invalidate() {
    }

    default void onPostTransition() {
    }

    default void onPreTransition() {
    }

    default void onTransition(float f) {
    }

    int sortFactor();
}
