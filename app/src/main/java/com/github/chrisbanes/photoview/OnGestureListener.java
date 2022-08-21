package com.github.chrisbanes.photoview;

/* loaded from: classes.dex */
public interface OnGestureListener {
    void oScaleEnd();

    void onDrag(float f, float f2);

    void onFling(float f, float f2, float f3, float f4);

    void onScale(float f, float f2, float f3);

    void onScaleBegin();
}
