package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.drawable.Drawable;
import android.util.Size;
import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;

/* loaded from: classes3.dex */
public interface ImageTransitItem extends ITransitItem {
    Size getImageSize();

    RequestOptions getOptions();

    RequestOptions getPreviewOptions();

    Drawable getTransitDrawable();

    String getTransitPath();

    ImageView.ScaleType getTransitScaleType();

    default String getTransitPreviewPath() {
        return getTransitPath();
    }
}
