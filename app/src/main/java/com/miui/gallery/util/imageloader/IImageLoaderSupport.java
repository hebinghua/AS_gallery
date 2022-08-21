package com.miui.gallery.util.imageloader;

import android.net.Uri;
import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;

/* loaded from: classes2.dex */
public interface IImageLoaderSupport {
    void bindImage(ImageView imageView, Uri uri, RequestOptions requestOptions);

    void bindImage(ImageView imageView, RequestOptions requestOptions);

    void bindImage(ImageView imageView, String str, Uri uri, RequestOptions requestOptions);

    void bindImage(ImageView imageView, String str, RequestOptions requestOptions);

    void unbindImage(ImageView imageView);
}
