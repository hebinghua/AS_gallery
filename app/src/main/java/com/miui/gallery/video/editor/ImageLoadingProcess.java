package com.miui.gallery.video.editor;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.miui.gallery.glide.GlideImageLoadingListener;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ImageLoadingProcess implements GlideImageLoadingListener<Bitmap> {
    public int mColor;
    public ImageView mIcon;

    public ImageLoadingProcess(ImageView imageView) {
        this.mIcon = imageView;
    }

    public void setBgColor(int i) {
        this.mColor = i;
    }

    @Override // com.miui.gallery.glide.GlideImageLoadingListener
    public void onLoadCleared(String str) {
        DefaultLogger.d("ImageLoadingProcess", "load img is cancel.");
    }

    @Override // com.miui.gallery.glide.GlideImageLoadingListener
    public void onLoadStarted(String str) {
        int i = this.mColor;
        if (i != 0) {
            this.mIcon.setBackgroundResource(i);
        }
    }

    @Override // com.miui.gallery.glide.GlideImageLoadingListener
    public void onLoadFailed(String str) {
        DefaultLogger.w("ImageLoadingProcess", "load img is fail.");
    }

    @Override // com.miui.gallery.glide.GlideImageLoadingListener
    public void onResourceReady(String str, Bitmap bitmap) {
        DefaultLogger.d("ImageLoadingProcess", "load img is complete.");
        ImageView imageView = this.mIcon;
        if (imageView != null) {
            imageView.setBackground(null);
        }
    }
}
