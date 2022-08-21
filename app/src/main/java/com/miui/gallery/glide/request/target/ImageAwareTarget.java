package com.miui.gallery.glide.request.target;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideImageLoadingListener;
import com.miui.gallery.glide.ImageAware;

/* loaded from: classes2.dex */
public abstract class ImageAwareTarget<T> implements Target<T>, Transition.ViewAdapter {
    public ImageAware mImageAware;
    public GlideImageLoadingListener<T> mImageLoadingListener;
    public String mUri;

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
    }

    @Override // com.bumptech.glide.request.target.Target
    public void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }

    public abstract void setResource(T t);

    public ImageAwareTarget(ImageAware imageAware) {
        Preconditions.checkNotNull(imageAware);
        this.mImageAware = imageAware;
    }

    public ImageAwareTarget(ImageAware imageAware, String str, GlideImageLoadingListener<T> glideImageLoadingListener) {
        this.mImageAware = imageAware;
        this.mUri = str;
        this.mImageLoadingListener = glideImageLoadingListener;
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadStarted(Drawable drawable) {
        setResource(null);
        this.mImageAware.setImageDrawable(drawable);
        notifyLoadStarted();
    }

    public void notifyLoadStarted() {
        GlideImageLoadingListener<T> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onLoadStarted(this.mUri);
        }
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadFailed(Drawable drawable) {
        setResource(null);
        this.mImageAware.setImageDrawable(drawable);
        GlideImageLoadingListener<T> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onLoadFailed(this.mUri);
        }
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
        setResource(null);
        this.mImageAware.setImageDrawable(drawable);
        GlideImageLoadingListener<T> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onLoadCleared(this.mUri);
        }
    }

    @Override // com.bumptech.glide.request.target.Target
    public void onResourceReady(T t, Transition<? super T> transition) {
        if (transition == null || getView() == null || !transition.transition(t, this)) {
            setResource(t);
        }
        GlideImageLoadingListener<T> glideImageLoadingListener = this.mImageLoadingListener;
        if (glideImageLoadingListener != null) {
            glideImageLoadingListener.onResourceReady(this.mUri, t);
        }
    }

    @Override // com.bumptech.glide.request.target.Target
    public void getSize(SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.mImageAware.getWidth(), this.mImageAware.getHeight());
    }

    @Override // com.bumptech.glide.request.target.Target
    public final void setRequest(Request request) {
        setTag(request);
    }

    @Override // com.bumptech.glide.request.target.Target
    public Request getRequest() {
        Object tag = getTag();
        if (tag != null) {
            if (tag instanceof Request) {
                return (Request) tag;
            }
            throw new IllegalArgumentException("You must not pass non-R.id ids to setTag(id)");
        }
        return null;
    }

    public View getView() {
        return this.mImageAware.getWrappedView();
    }

    public final void setTag(Object obj) {
        this.mImageAware.setTag(R.id.glide_custom_view_target_tag, obj);
    }

    public final Object getTag() {
        return this.mImageAware.getTag(R.id.glide_custom_view_target_tag);
    }
}
