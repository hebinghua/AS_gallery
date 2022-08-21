package com.miui.gallery.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.miui.gallery.glide.GlideImageLoadingListener;
import com.miui.gallery.glide.ImageAware;

/* loaded from: classes2.dex */
public class BitmapImageAwareTarget extends ImageAwareTarget<Bitmap> {
    public boolean mIsClearCurrentImageBeforeLoadStart;

    public BitmapImageAwareTarget(ImageAware imageAware) {
        super(imageAware);
    }

    public BitmapImageAwareTarget(ImageAware imageAware, boolean z) {
        super(imageAware);
        this.mIsClearCurrentImageBeforeLoadStart = z;
    }

    public BitmapImageAwareTarget(ImageAware imageAware, String str, GlideImageLoadingListener<Bitmap> glideImageLoadingListener) {
        super(imageAware, str, glideImageLoadingListener);
    }

    @Override // com.miui.gallery.glide.request.target.ImageAwareTarget, com.bumptech.glide.request.target.Target
    public void onLoadStarted(Drawable drawable) {
        if (this.mIsClearCurrentImageBeforeLoadStart) {
            super.onLoadStarted(drawable);
        } else {
            notifyLoadStarted();
        }
    }

    @Override // com.miui.gallery.glide.request.target.ImageAwareTarget
    public void setResource(Bitmap bitmap) {
        this.mImageAware.setImageBitmap(bitmap);
    }
}
