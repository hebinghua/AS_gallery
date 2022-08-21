package com.miui.gallery.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public class ImageViewAware extends ViewAware<ImageView> {
    public ImageViewAware(ImageView imageView) {
        super(imageView);
    }

    @Override // com.miui.gallery.glide.ViewAware
    public void setImageDrawableInto(Drawable drawable, ImageView imageView) {
        imageView.setImageDrawable(drawable);
    }

    @Override // com.miui.gallery.glide.ViewAware
    public void setImageBitmapInto(Bitmap bitmap, ImageView imageView) {
        imageView.setImageBitmap(bitmap);
    }
}
