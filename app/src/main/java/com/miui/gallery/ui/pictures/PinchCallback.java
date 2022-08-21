package com.miui.gallery.ui.pictures;

/* loaded from: classes2.dex */
public interface PinchCallback {
    boolean isPinchAvailable();

    boolean isTargetModeReachable(PictureViewMode pictureViewMode);

    void onPictureViewModeChanged(PictureViewMode pictureViewMode);

    default void onPostTransition() {
    }

    default void onPreTransition() {
    }

    default void onTransitionFinish(boolean z) {
    }

    default void onTransitionUpdate(float f) {
    }

    int[] unpackGroupIndexAndOffset(int i);
}
