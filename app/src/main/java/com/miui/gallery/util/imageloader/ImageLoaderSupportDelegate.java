package com.miui.gallery.util.imageloader;

import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.search.core.display.icon.IconImageLoader;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.glide.BindImageHelper;

/* loaded from: classes2.dex */
public class ImageLoaderSupportDelegate implements IImageLoaderSupport {
    public static ImageLoaderSupportDelegate getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final ImageLoaderSupportDelegate INSTANCE = new ImageLoaderSupportDelegate();
    }

    public ImageLoaderSupportDelegate() {
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, String str, Uri uri, RequestOptions requestOptions) {
        if (checkBind(imageView, str, uri, requestOptions)) {
            BindImageHelper.bindImage(str, uri, DownloadType.MICRO, imageView, requestOptions);
        }
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, String str, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        BindImageHelper.bindImage(str, imageView, requestOptions);
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, RequestOptions requestOptions) {
        checkBind(imageView, null, null, requestOptions);
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void unbindImage(ImageView imageView) {
        BindImageHelper.cancel(imageView);
    }

    public final boolean checkBind(ImageView imageView, String str, Uri uri, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(str) && uri == null) {
            Glide.with(imageView).mo985asBitmap().mo963load(str).mo946apply((BaseRequestOptions<?>) requestOptions).into(imageView);
            return false;
        } else if (uri != null && TextUtils.isEmpty(str) && !TextUtils.isEmpty(uri.getQueryParameter("serverId"))) {
            IconImageLoader.getInstance().displayImageEager(imageView.getContext(), uri, DownloadType.MICRO, imageView, requestOptions);
            return false;
        } else if (uri != null || TextUtils.isEmpty(str) || !ResourceUtils.isGalleryDrawableResourcePath(str)) {
            return true;
        } else {
            bindImage(imageView, str, requestOptions);
            return false;
        }
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, Uri uri, RequestOptions requestOptions) {
        BindImageHelper.bindImage(uri, imageView, requestOptions);
    }
}
