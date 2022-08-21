package com.miui.gallery.util.photoview;

import android.graphics.Rect;
import com.miui.gallery.concurrent.ThreadPool;

/* loaded from: classes2.dex */
public interface TileBitProvider {
    ThreadPool customDecodePool();

    int getImageHeight();

    String getImageMimeType();

    int getImageWidth();

    int getParallelism();

    int getRotation();

    TileBit getTileBit(Rect rect, int i);

    boolean isFlip();

    void release();
}
