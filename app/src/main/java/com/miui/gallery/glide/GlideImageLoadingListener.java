package com.miui.gallery.glide;

/* loaded from: classes2.dex */
public interface GlideImageLoadingListener<Resource> {
    void onLoadCleared(String str);

    void onLoadFailed(String str);

    void onLoadStarted(String str);

    void onResourceReady(String str, Resource resource);
}
