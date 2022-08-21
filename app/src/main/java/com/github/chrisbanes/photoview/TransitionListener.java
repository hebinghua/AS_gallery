package com.github.chrisbanes.photoview;

/* loaded from: classes.dex */
public interface TransitionListener {
    void onTransitEnd();

    default void onTransitUpdate(float f) {
    }
}
