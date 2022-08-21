package com.miui.gallery.editor.photo.screen.home;

/* loaded from: classes2.dex */
public interface ThumbnailAnimatorCallback {
    int[] getThumbnailRect();

    int getThumbnailStartLeft();

    int getThumbnailStartTop();

    void onAnimationStart();

    void onAnimationUpdate(float f);

    void onPrepareAnimatorStart();
}
