package com.miui.gallery.util.glide;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.R;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.glide.ImageAware;
import com.miui.gallery.glide.ImageViewAware;
import com.miui.gallery.glide.SlideShowViewAware;
import com.miui.gallery.glide.ViewAware;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.request.target.BitmapImageAwareTarget;
import com.miui.gallery.glide.request.target.ImageAwareClearTarget;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.SlideShowView;

/* loaded from: classes2.dex */
public class BindImageHelper {

    /* loaded from: classes2.dex */
    public interface OnImageLoadingCompleteListener {
        void onLoadingComplete();

        void onLoadingFailed();
    }

    public static /* synthetic */ void access$000(ImageAware imageAware) {
        bindCloudImage(imageAware);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions) {
        bindImage(str, uri, downloadType, imageView, requestOptions, true, true);
    }

    public static void bindImage(String str, DownloadType downloadType, SlideShowView slideShowView, RequestOptions requestOptions) {
        bindImage(str, null, downloadType, new SlideShowViewAware(slideShowView), requestOptions, null, null, null, false, false, null);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, OnImageLoadingCompleteListener onImageLoadingCompleteListener) {
        bindImage(str, uri, downloadType, new ImageViewAware(imageView), requestOptions, null, null, null, true, true, onImageLoadingCompleteListener);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, RequestOptions requestOptions2) {
        bindImage(str, uri, downloadType, new ImageViewAware(imageView), requestOptions, requestOptions2, str, null, true, true, null);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, RequestOptions requestOptions2, String str2, boolean z) {
        bindImage(str, uri, downloadType, new ImageViewAware(imageView), requestOptions, requestOptions2, str2, null, true, true, null, z);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, boolean z, boolean z2) {
        bindImage(str, uri, downloadType, new ImageViewAware(imageView), requestOptions, null, null, null, z, z2, null);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions) {
        bindImage(str, uri, downloadType, imageAware, requestOptions, true, true);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, RequestOptions requestOptions2) {
        bindImage(str, uri, downloadType, imageAware, requestOptions, requestOptions2, str, null, true, true, null);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, boolean z, boolean z2) {
        bindImage(str, uri, downloadType, imageAware, requestOptions, null, null, null, z, z2, null);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, RequestOptions requestOptions2, String str2, TransitionOptions<?, Bitmap> transitionOptions, boolean z, boolean z2, OnImageLoadingCompleteListener onImageLoadingCompleteListener) {
        bindImage(str, uri, downloadType, imageAware, requestOptions, requestOptions2, str2, transitionOptions, z, z2, onImageLoadingCompleteListener, true);
    }

    public static void bindImage(String str, Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, RequestOptions requestOptions2, String str2, TransitionOptions<?, Bitmap> transitionOptions, boolean z, boolean z2, OnImageLoadingCompleteListener onImageLoadingCompleteListener, boolean z3) {
        if (imageAware == null) {
            DefaultLogger.e("BindImageHelper", "bindImage view is null");
            return;
        }
        setCloudHolder(uri, downloadType, imageAware, requestOptions, transitionOptions, z, z2, onImageLoadingCompleteListener);
        RequestManager requestManager = imageAware.getRequestManager();
        if (TextUtils.isEmpty(str)) {
            if (requestManager != null) {
                requestManager.clear(new ImageAwareClearTarget(imageAware));
            }
            bindCloudImage(imageAware);
        } else if (requestManager == null) {
        } else {
            RequestBuilder<Bitmap> requestBuilder = null;
            if (requestOptions2 != null) {
                requestBuilder = requestManager.mo985asBitmap().mo962load(GalleryModel.of(!TextUtils.isEmpty(str2) ? str2 : str)).mo946apply((BaseRequestOptions<?>) requestOptions2);
            }
            requestManager.mo985asBitmap().mo962load(GalleryModel.of(str)).mo946apply((BaseRequestOptions<?>) requestOptions).mo979thumbnail(requestBuilder).mo945addListener(getLocalLoadingListener(imageAware, onImageLoadingCompleteListener)).into((RequestBuilder<Bitmap>) new BitmapImageAwareTarget(imageAware, z3));
        }
    }

    public static void bindDefaultIcon(ImageView imageView, RequestOptions requestOptions) {
        Glide.with(imageView).mo985asBitmap().mo963load("").mo946apply((BaseRequestOptions<?>) requestOptions).into(imageView);
    }

    public static void bindBase64Image(String str, ImageView imageView, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(str) || !FileUtils.isBase64Url(str)) {
            DefaultLogger.e("BindImageHelper", "bindBase64Image error,url is %s", str);
            return;
        }
        ImageViewAware imageViewAware = new ImageViewAware(imageView);
        RequestManager requestManager = imageViewAware.getRequestManager();
        if (requestManager == null) {
            return;
        }
        requestManager.mo985asBitmap().mo963load(str).mo946apply((BaseRequestOptions<?>) requestOptions).into((RequestBuilder<Bitmap>) new BitmapImageAwareTarget(imageViewAware));
    }

    public static void bindImage(String str, ImageView imageView, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.e("BindImageHelper", "bindImage resource error,resourceName is %s", str);
        } else {
            bindImage(Uri.parse(str), imageView, requestOptions);
        }
    }

    public static void bindImage(Uri uri, ImageView imageView, RequestOptions requestOptions) {
        if (uri == null) {
            DefaultLogger.e("BindImageHelper", "bindImage resource error,resourceName is %s", uri);
            return;
        }
        ImageViewAware imageViewAware = new ImageViewAware(imageView);
        RequestManager requestManager = imageViewAware.getRequestManager();
        if (requestManager == null) {
            return;
        }
        requestManager.mo985asBitmap().mo960load(uri).mo946apply((BaseRequestOptions<?>) requestOptions).into((RequestBuilder<Bitmap>) new BitmapImageAwareTarget(imageViewAware));
    }

    public static void bindFaceImage(String str, Uri uri, ImageView imageView, RequestOptions requestOptions, boolean z) {
        bindFaceImage(str, uri, new ImageViewAware(imageView), requestOptions, z);
    }

    public static void bindFaceImage(String str, Uri uri, ImageAware imageAware, RequestOptions requestOptions, boolean z) {
        DownloadType downloadType = DownloadType.THUMBNAIL;
        bindImage(str, uri, downloadType, imageAware, requestOptions);
        if (TextUtils.isEmpty(str) || !str.contains("/Android/data/com.miui.gallery/cache/microthumbnailFile")) {
            return;
        }
        bindImage((String) null, uri, downloadType, imageAware, requestOptions, z, false);
    }

    public static void cancel(ImageView imageView) {
        RequestManager safeGet = GlideRequestManagerHelper.safeGet(imageView);
        if (safeGet != null) {
            safeGet.clear(new ImageAwareClearTarget(new ImageViewAware(imageView)));
        }
    }

    public static void bindCloudImage(ImageAware imageAware) {
        if (imageAware == null) {
            DefaultLogger.e("BindImageHelper", "bindCloudImage view is null");
            return;
        }
        CloudImageHolder imageHolder = CloudImageHolder.getImageHolder(imageAware);
        DefaultLogger.i("BindImageHelper", "downloadImage %s", imageHolder.getUri());
        CloudImageLoader.getInstance().displayImage(imageHolder.getUri(), imageHolder.getImageType(), imageAware, imageHolder.getRequestOptions(), imageHolder.getTransitionOptions(), imageHolder.getImageLoadingListener(), imageHolder.getImageLoadingProgressListener(), imageHolder.isDelayRequest(), imageHolder.isShowLoadingImage());
    }

    public static void setCloudHolder(Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, TransitionOptions<?, Bitmap> transitionOptions, boolean z, boolean z2, final OnImageLoadingCompleteListener onImageLoadingCompleteListener) {
        CloudImageHolder imageHolder = CloudImageHolder.getImageHolder(imageAware);
        imageHolder.setUri(uri).setImageType(downloadType).setRequestOptions(requestOptions).setTransitionOptions(transitionOptions).setNeedDisplay(true).setDelayRequest(z).setShowLoadingImage(z2);
        if (onImageLoadingCompleteListener != null) {
            imageHolder.setImageLoadingListener(new CloudImageLoadingListener() { // from class: com.miui.gallery.util.glide.BindImageHelper.1
                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onDownloadComplete(Uri uri2, DownloadType downloadType2, View view, String str) {
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingCancelled(Uri uri2, DownloadType downloadType2, View view) {
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingStarted(Uri uri2, DownloadType downloadType2, View view) {
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingFailed(Uri uri2, DownloadType downloadType2, View view, ErrorCode errorCode, String str) {
                    onImageLoadingCompleteListener.onLoadingFailed();
                }

                @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
                public void onLoadingComplete(Uri uri2, DownloadType downloadType2, View view, Bitmap bitmap) {
                    onImageLoadingCompleteListener.onLoadingComplete();
                }
            });
        }
    }

    public static RequestListener<Bitmap> getLocalLoadingListener(ImageAware imageAware, OnImageLoadingCompleteListener onImageLoadingCompleteListener) {
        Object tag = imageAware.getTag(R.id.tag_local_loading_listener);
        if (tag == null) {
            tag = new AnonymousClass2(imageAware, onImageLoadingCompleteListener);
            imageAware.setTag(R.id.tag_local_loading_listener, tag);
        }
        return (RequestListener) tag;
    }

    /* renamed from: com.miui.gallery.util.glide.BindImageHelper$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RequestListener<Bitmap> {
        public final /* synthetic */ ImageAware val$aware;
        public final /* synthetic */ OnImageLoadingCompleteListener val$listener;

        /* renamed from: $r8$lambda$YEjAhBA2Rbey-VVoJY03HDkciog */
        public static /* synthetic */ void m1732$r8$lambda$YEjAhBA2RbeyVVoJY03HDkciog(ImageAware imageAware) {
            BindImageHelper.access$000(imageAware);
        }

        public AnonymousClass2(ImageAware imageAware, OnImageLoadingCompleteListener onImageLoadingCompleteListener) {
            this.val$aware = imageAware;
            this.val$listener = onImageLoadingCompleteListener;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            ImageAware imageAware = this.val$aware;
            if (!(imageAware instanceof ViewAware) || imageAware.getWrappedView() != null) {
                final ImageAware imageAware2 = this.val$aware;
                Runnable runnable = new Runnable() { // from class: com.miui.gallery.util.glide.BindImageHelper$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BindImageHelper.AnonymousClass2.m1732$r8$lambda$YEjAhBA2RbeyVVoJY03HDkciog(ImageAware.this);
                    }
                };
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    runnable.run();
                } else {
                    ThreadManager.getMainHandler().post(runnable);
                }
            }
            OnImageLoadingCompleteListener onImageLoadingCompleteListener = this.val$listener;
            if (onImageLoadingCompleteListener != null) {
                onImageLoadingCompleteListener.onLoadingFailed();
            }
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            OnImageLoadingCompleteListener onImageLoadingCompleteListener = this.val$listener;
            if (onImageLoadingCompleteListener != null) {
                onImageLoadingCompleteListener.onLoadingComplete();
                return false;
            }
            return false;
        }
    }
}
