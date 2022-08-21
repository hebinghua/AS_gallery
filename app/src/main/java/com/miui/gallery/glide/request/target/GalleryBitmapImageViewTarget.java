package com.miui.gallery.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.miui.gallery.glide.GlideImageLoadingListener;

/* loaded from: classes2.dex */
public class GalleryBitmapImageViewTarget extends CustomViewTarget<ImageView, Bitmap> implements Transition.ViewAdapter {
    public final GlideImageLoadingListener<Bitmap> mImageLoadingListener;
    public final String mUri;

    @Override // com.bumptech.glide.request.target.Target
    public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
        onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
    }

    public GalleryBitmapImageViewTarget(ImageView imageView, String str, GlideImageLoadingListener<Bitmap> glideImageLoadingListener) {
        super(imageView);
        this.mUri = str;
        this.mImageLoadingListener = glideImageLoadingListener;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadFailed(Drawable drawable) {
        setResource(null);
        setDrawable(drawable);
        GlideImageLoadingListener<Bitmap> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onLoadFailed(this.mUri);
        }
    }

    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
        if (transition == null || !transition.transition(bitmap, this)) {
            setResource(bitmap);
        }
        GlideImageLoadingListener<Bitmap> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onResourceReady(this.mUri, bitmap);
        }
    }

    @Override // com.bumptech.glide.request.target.CustomViewTarget
    public void onResourceLoading(Drawable drawable) {
        super.onResourceLoading(drawable);
        setResource(null);
        setDrawable(drawable);
        GlideImageLoadingListener<Bitmap> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onLoadStarted(this.mUri);
        }
    }

    @Override // com.bumptech.glide.request.target.CustomViewTarget
    public void onResourceCleared(Drawable drawable) {
        setResource(null);
        setDrawable(drawable);
        GlideImageLoadingListener<Bitmap> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onLoadCleared(this.mUri);
        }
    }

    public final void setResource(Bitmap bitmap) {
        ((ImageView) this.view).setImageBitmap(bitmap);
    }

    public void setDrawable(Drawable drawable) {
        ((ImageView) this.view).setImageDrawable(drawable);
    }
}
