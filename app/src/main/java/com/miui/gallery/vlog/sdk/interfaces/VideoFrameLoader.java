package com.miui.gallery.vlog.sdk.interfaces;

import android.graphics.Bitmap;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public interface VideoFrameLoader {

    /* loaded from: classes2.dex */
    public interface LoadListener {
        void onLoadFinished(Bitmap bitmap);
    }

    /* loaded from: classes2.dex */
    public interface OnImageLoadedListener {
        void onImageDisplayed();
    }

    Bitmap extractFrame(String str, long j, int i, int i2, boolean z, LoadListener loadListener);

    boolean loadFromCache(ImageView imageView, String str, int i, long j);

    boolean loadImage(ImageView imageView, String str, int i, long j);

    boolean loadImage(ImageView imageView, String str, int i, long j, Bitmap bitmap);

    void release();

    void setListener(OnImageLoadedListener onImageLoadedListener);
}
