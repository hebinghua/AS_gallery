package com.miui.gallery.glide.request.target;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.request.transition.Transition;
import com.miui.gallery.glide.ImageAware;

/* loaded from: classes2.dex */
public class ImageAwareClearTarget extends ImageAwareTarget<Object> {
    @Override // com.miui.gallery.glide.request.target.ImageAwareTarget, com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
    }

    @Override // com.miui.gallery.glide.request.target.ImageAwareTarget, com.bumptech.glide.request.target.Target
    public void onLoadFailed(Drawable drawable) {
    }

    @Override // com.miui.gallery.glide.request.target.ImageAwareTarget, com.bumptech.glide.request.target.Target
    public void onResourceReady(Object obj, Transition<? super Object> transition) {
    }

    @Override // com.miui.gallery.glide.request.target.ImageAwareTarget
    public void setResource(Object obj) {
    }

    public ImageAwareClearTarget(ImageAware imageAware) {
        super(imageAware);
    }
}
