package com.miui.gallery.util.glide;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.Config$ImageDownload;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.glide.GlideImageLoadingListener;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.ImageAware;
import com.miui.gallery.glide.ImageViewAware;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.request.target.BitmapImageAwareTarget;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadItemStatus;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.sdk.download.listener.DownloadListener;
import com.miui.gallery.sdk.download.listener.DownloadProgressListener;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingProgressListener;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.glide.CloudImageLoader;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class CloudImageLoader {
    public final Map<DownloadType, Map<String, Set<ViewAware>>> mDelayRequest;
    public volatile Handler mDispatchHandler;
    public DownloadListener mDownloadListener;
    public Handler mMainThreadHandler;
    public volatile boolean mPauseLoading;
    public final Map<String, Set<ViewAware>> mPendingRequest;
    public DownloadProgressListener mProgressListener;
    public final Map<String, Set<ViewAware>> mRequestingRequest;

    /* loaded from: classes2.dex */
    public interface GetStatusCallBack {
        void onStatusGotten(DownloadItemStatus downloadItemStatus);
    }

    public static /* synthetic */ void $r8$lambda$2ELmgrkmIOkMJA_GBgoDGcLjvAM(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        lambda$onProgressUpdate$5(downloadWrapper, viewAware);
    }

    public static /* synthetic */ void $r8$lambda$4pMtcwxopBZIHRO1vHW792V1Q2E(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        lambda$onSuccessLoading$2(downloadWrapper, viewAware);
    }

    public static /* synthetic */ void $r8$lambda$7uWX6LZIo4d4Pdd7II28tr32oDo(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        lambda$onFailLoading$4(downloadWrapper, viewAware);
    }

    public static /* synthetic */ boolean $r8$lambda$F3j0zLs8Inr7y44jVM68g6wAoFA(CloudImageLoader cloudImageLoader, Message message) {
        return cloudImageLoader.lambda$getDispatchHandler$6(message);
    }

    public static /* synthetic */ boolean $r8$lambda$J7EtNY3j7hDOiWRlLfeeumkltP4(CloudImageLoader cloudImageLoader, Message message) {
        return cloudImageLoader.lambda$new$0(message);
    }

    public static /* synthetic */ void $r8$lambda$aiTCLcrFmvouGlIzrtn2pjM_3ik(ImageAware imageAware, Drawable drawable) {
        imageAware.setImageDrawable(drawable);
    }

    /* renamed from: $r8$lambda$n0bZFpAl40w3-VbYk7ERoWeIAcs */
    public static /* synthetic */ void m1733$r8$lambda$n0bZFpAl40w3VbYk7ERoWeIAcs(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        lambda$onCancelLoading$3(downloadWrapper, viewAware);
    }

    /* renamed from: $r8$lambda$u64ZHaEp-PbA1PxducYKf0KS2kQ */
    public static /* synthetic */ void m1734$r8$lambda$u64ZHaEpPbA1PxducYKf0KS2kQ(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        lambda$onStartLoading$1(downloadWrapper, viewAware);
    }

    public CloudImageLoader(Context context) {
        this.mDownloadListener = new DownloadListener() { // from class: com.miui.gallery.util.glide.CloudImageLoader.1
            {
                CloudImageLoader.this = this;
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadStarted(Uri uri, DownloadType downloadType) {
                DefaultLogger.d("CloudImageLoader", "onDownloadStarted %s", uri);
                CloudImageLoader.this.postOnMainThread(0, new DownloadWrapper(uri, downloadType));
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadSuccess(Uri uri, DownloadType downloadType, DownloadedItem downloadedItem) {
                DefaultLogger.d("CloudImageLoader", "onDownloadSuccess %s", uri);
                CloudImageLoader.this.postOnMainThread(1, new DownloadWrapper(uri, downloadType, downloadedItem));
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadFail(Uri uri, DownloadType downloadType, DownloadFailReason downloadFailReason) {
                DefaultLogger.e("CloudImageLoader", "onDownloadFail: %s reason: %s", uri, downloadFailReason);
                CloudImageLoader.this.postOnMainThread(2, new DownloadWrapper(uri, downloadType, downloadFailReason));
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadListener
            public void onDownloadCancel(Uri uri, DownloadType downloadType) {
                DefaultLogger.d("CloudImageLoader", "onDownloadCancel %s", uri);
                CloudImageLoader.this.postOnMainThread(3, new DownloadWrapper(uri, downloadType));
            }
        };
        this.mProgressListener = new DownloadProgressListener() { // from class: com.miui.gallery.util.glide.CloudImageLoader.2
            {
                CloudImageLoader.this = this;
            }

            @Override // com.miui.gallery.sdk.download.listener.DownloadProgressListener
            public void onDownloadProgress(Uri uri, DownloadType downloadType, long j, long j2) {
                CloudImageLoader.this.postOnMainThread(4, new DownloadWrapper(uri, downloadType, j, j2));
            }
        };
        this.mPendingRequest = new HashMap();
        this.mRequestingRequest = new HashMap();
        this.mDelayRequest = new HashMap();
        this.mMainThreadHandler = new Handler(context.getMainLooper(), new Handler.Callback() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda1
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                return CloudImageLoader.$r8$lambda$J7EtNY3j7hDOiWRlLfeeumkltP4(CloudImageLoader.this, message);
            }
        });
    }

    public /* synthetic */ boolean lambda$new$0(Message message) {
        int i = message.what;
        if (i != 0) {
            if (i == 1) {
                return onSuccessLoading((DownloadWrapper) message.obj);
            }
            if (i == 2) {
                return onFailLoading((DownloadWrapper) message.obj);
            }
            if (i == 3) {
                return onCancelLoading((DownloadWrapper) message.obj);
            }
            if (i == 4) {
                return onProgressUpdate((DownloadWrapper) message.obj);
            }
            return false;
        }
        return onStartLoading((DownloadWrapper) message.obj);
    }

    /* loaded from: classes2.dex */
    public static class DownloadWrapper {
        public final long curSize;
        public final DownloadedItem downloadedItem;
        public final DownloadFailReason failReason;
        public final long totalSize;
        public final DownloadType type;
        public final Uri uri;

        public DownloadWrapper(Uri uri, DownloadType downloadType) {
            this(uri, downloadType, null, null, 0L, 0L);
        }

        public DownloadWrapper(Uri uri, DownloadType downloadType, long j, long j2) {
            this(uri, downloadType, null, null, j, j2);
        }

        public DownloadWrapper(Uri uri, DownloadType downloadType, DownloadedItem downloadedItem) {
            this(uri, downloadType, downloadedItem, null, 0L, 0L);
        }

        public DownloadWrapper(Uri uri, DownloadType downloadType, DownloadFailReason downloadFailReason) {
            this(uri, downloadType, null, downloadFailReason, 0L, 0L);
        }

        public DownloadWrapper(Uri uri, DownloadType downloadType, DownloadedItem downloadedItem, DownloadFailReason downloadFailReason, long j, long j2) {
            this.uri = uri;
            this.type = downloadType;
            this.downloadedItem = downloadedItem;
            this.failReason = downloadFailReason;
            this.curSize = j;
            this.totalSize = j2;
        }
    }

    /* loaded from: classes2.dex */
    public static class DecodeListenerWrapper implements GlideImageLoadingListener<Bitmap> {
        public ViewAware mAware;
        public final DownloadType mType;
        public final Uri mUri;

        @Override // com.miui.gallery.glide.GlideImageLoadingListener
        public void onLoadStarted(String str) {
        }

        public DecodeListenerWrapper(ViewAware viewAware) {
            this.mUri = viewAware.getUri();
            this.mType = viewAware.getDownloadType();
            this.mAware = viewAware;
        }

        public final boolean isViewReused() {
            return !TextUtils.equals(this.mAware.getKey(), CloudImageLoader.generateKey(this.mUri, this.mType));
        }

        @Override // com.miui.gallery.glide.GlideImageLoadingListener
        public void onLoadCleared(String str) {
            CloudImageLoadingListener loadingListener;
            if (isViewReused() || (loadingListener = this.mAware.getLoadingListener()) == null) {
                return;
            }
            loadingListener.onLoadingCancelled(this.mUri, this.mType, this.mAware.getWrappedView());
        }

        @Override // com.miui.gallery.glide.GlideImageLoadingListener
        public void onLoadFailed(String str) {
            CloudImageLoadingListener loadingListener;
            if (isViewReused() || (loadingListener = this.mAware.getLoadingListener()) == null) {
                return;
            }
            loadingListener.onLoadingFailed(this.mUri, this.mType, this.mAware.getWrappedView(), ErrorCode.DECODE_ERROR, null);
        }

        @Override // com.miui.gallery.glide.GlideImageLoadingListener
        public void onResourceReady(String str, Bitmap bitmap) {
            CloudImageLoadingListener loadingListener;
            if (isViewReused() || (loadingListener = this.mAware.getLoadingListener()) == null) {
                return;
            }
            loadingListener.onLoadingComplete(this.mUri, this.mType, this.mAware.getWrappedView(), bitmap);
        }
    }

    public final void dispatchListener(DownloadWrapper downloadWrapper, Consumer<ViewAware> consumer, boolean z) {
        List<ViewAware> matchAware = getMatchAware(downloadWrapper, z);
        for (ViewAware viewAware : matchAware) {
            if (viewAware != null) {
                consumer.accept(viewAware);
            }
        }
        if (matchAware.size() == 0) {
            DefaultLogger.i("CloudImageLoader", "The ref for %s should not be null", downloadWrapper.uri);
        }
    }

    public final boolean onStartLoading(final DownloadWrapper downloadWrapper) {
        DefaultLogger.d("CloudImageLoader", "onStartLoading %s", downloadWrapper.uri);
        dispatchListener(downloadWrapper, new Consumer() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CloudImageLoader.m1734$r8$lambda$u64ZHaEpPbA1PxducYKf0KS2kQ(CloudImageLoader.DownloadWrapper.this, (CloudImageLoader.ViewAware) obj);
            }
        }, false);
        return true;
    }

    public static /* synthetic */ void lambda$onStartLoading$1(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        CloudImageLoadingListener loadingListener = viewAware.getLoadingListener();
        if (loadingListener != null) {
            loadingListener.onLoadingStarted(downloadWrapper.uri, downloadWrapper.type, viewAware.getWrappedView());
        }
    }

    public final boolean onSuccessLoading(final DownloadWrapper downloadWrapper) {
        DefaultLogger.d("CloudImageLoader", "onSuccessLoading, %s", downloadWrapper.uri.toString());
        dispatchListener(downloadWrapper, new Consumer() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CloudImageLoader.$r8$lambda$4pMtcwxopBZIHRO1vHW792V1Q2E(CloudImageLoader.DownloadWrapper.this, (CloudImageLoader.ViewAware) obj);
            }
        }, true);
        return true;
    }

    public static /* synthetic */ void lambda$onSuccessLoading$2(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        RequestManager requestManager;
        String filePath = downloadWrapper.downloadedItem.getFilePath();
        CloudImageLoadingListener loadingListener = viewAware.getLoadingListener();
        if (loadingListener != null) {
            loadingListener.onDownloadComplete(downloadWrapper.uri, downloadWrapper.type, viewAware.getWrappedView(), filePath);
        }
        if (!viewAware.needDisplay()) {
            if (loadingListener == null) {
                return;
            }
            loadingListener.onLoadingComplete(downloadWrapper.uri, downloadWrapper.type, viewAware.getWrappedView(), null);
            return;
        }
        ImageAware imageAware = viewAware.getImageAware();
        if (imageAware == null || imageAware.isCollected() || (requestManager = imageAware.getRequestManager()) == null) {
            return;
        }
        RequestOptions requestOptions = viewAware.getRequestOptions();
        if (downloadWrapper.downloadedItem.getFileCipher() != null) {
            requestOptions = requestOptions != null ? requestOptions.clone().mo975set(GalleryOptions.SECRET_KEY, downloadWrapper.downloadedItem.getFileCipher()) : GlideOptions.secretKeyOf(downloadWrapper.downloadedItem.getFileCipher());
        }
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
        }
        DecodeListenerWrapper decodeListenerWrapper = new DecodeListenerWrapper(viewAware);
        RequestBuilder<Bitmap> mo946apply = requestManager.mo985asBitmap().mo962load(GalleryModel.of(filePath)).mo946apply((BaseRequestOptions<?>) requestOptions);
        if (viewAware.getTransitionOptions() != null) {
            mo946apply = mo946apply.mo981transition(viewAware.getTransitionOptions());
        }
        mo946apply.into((RequestBuilder<Bitmap>) new BitmapImageAwareTarget(imageAware, filePath, decodeListenerWrapper));
    }

    public final boolean onCancelLoading(final DownloadWrapper downloadWrapper) {
        DefaultLogger.i("CloudImageLoader", "onLoadingCancelled %s", downloadWrapper.uri);
        dispatchListener(downloadWrapper, new Consumer() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CloudImageLoader.m1733$r8$lambda$n0bZFpAl40w3VbYk7ERoWeIAcs(CloudImageLoader.DownloadWrapper.this, (CloudImageLoader.ViewAware) obj);
            }
        }, true);
        return true;
    }

    public static /* synthetic */ void lambda$onCancelLoading$3(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        CloudImageLoadingListener loadingListener = viewAware.getLoadingListener();
        if (loadingListener != null) {
            loadingListener.onLoadingCancelled(downloadWrapper.uri, downloadWrapper.type, viewAware.getWrappedView());
        }
    }

    public final boolean onFailLoading(final DownloadWrapper downloadWrapper) {
        DefaultLogger.e("CloudImageLoader", "onLoadingFailed %s", downloadWrapper.uri);
        dispatchListener(downloadWrapper, new Consumer() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda5
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CloudImageLoader.$r8$lambda$7uWX6LZIo4d4Pdd7II28tr32oDo(CloudImageLoader.DownloadWrapper.this, (CloudImageLoader.ViewAware) obj);
            }
        }, true);
        return true;
    }

    public static /* synthetic */ void lambda$onFailLoading$4(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        ImageAware imageAware;
        RequestManager requestManager;
        RequestOptions requestOptions;
        CloudImageLoadingListener loadingListener = viewAware.getLoadingListener();
        if (loadingListener != null) {
            loadingListener.onLoadingFailed(downloadWrapper.uri, downloadWrapper.type, viewAware.getWrappedView(), downloadWrapper.failReason == null ? ErrorCode.UNKNOWN : downloadWrapper.failReason.getCode(), downloadWrapper.failReason == null ? null : downloadWrapper.failReason.getDesc());
        }
        if (!viewAware.needDisplay() || (imageAware = viewAware.getImageAware()) == null || imageAware.isCollected() || (requestManager = imageAware.getRequestManager()) == null || (requestOptions = viewAware.getRequestOptions()) == null) {
            return;
        }
        DecodeListenerWrapper decodeListenerWrapper = new DecodeListenerWrapper(viewAware);
        RequestBuilder<Bitmap> mo946apply = requestManager.mo985asBitmap().mo963load((String) null).mo946apply((BaseRequestOptions<?>) requestOptions);
        if (viewAware.getTransitionOptions() != null) {
            mo946apply = mo946apply.mo981transition(viewAware.getTransitionOptions());
        }
        mo946apply.into((RequestBuilder<Bitmap>) new BitmapImageAwareTarget(imageAware, null, decodeListenerWrapper));
    }

    public final boolean onProgressUpdate(final DownloadWrapper downloadWrapper) {
        DefaultLogger.v("CloudImageLoader", "onProgressUpdate(%d/%d) %s", Long.valueOf(downloadWrapper.curSize), Long.valueOf(downloadWrapper.totalSize), downloadWrapper.uri.toString());
        dispatchListener(downloadWrapper, new Consumer() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CloudImageLoader.$r8$lambda$2ELmgrkmIOkMJA_GBgoDGcLjvAM(CloudImageLoader.DownloadWrapper.this, (CloudImageLoader.ViewAware) obj);
            }
        }, false);
        return true;
    }

    public static /* synthetic */ void lambda$onProgressUpdate$5(DownloadWrapper downloadWrapper, ViewAware viewAware) {
        CloudImageLoadingProgressListener loadingProgressListener = viewAware.getLoadingProgressListener();
        if (loadingProgressListener != null) {
            loadingProgressListener.onProgressUpdate(downloadWrapper.uri, downloadWrapper.type, viewAware.getWrappedView(), (int) downloadWrapper.curSize, (int) downloadWrapper.totalSize);
        }
    }

    public final void postOnMainThread(int i, DownloadWrapper downloadWrapper) {
        Message obtainMessage = this.mMainThreadHandler.obtainMessage();
        obtainMessage.what = i;
        obtainMessage.obj = downloadWrapper;
        this.mMainThreadHandler.sendMessage(obtainMessage);
    }

    public final Handler getDispatchHandler() {
        if (this.mDispatchHandler == null) {
            synchronized (this) {
                if (this.mDispatchHandler == null) {
                    HandlerThread handlerThread = new HandlerThread("cloud-image-loader-dispatcher");
                    handlerThread.start();
                    this.mDispatchHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda0
                        @Override // android.os.Handler.Callback
                        public final boolean handleMessage(Message message) {
                            return CloudImageLoader.$r8$lambda$F3j0zLs8Inr7y44jVM68g6wAoFA(CloudImageLoader.this, message);
                        }
                    });
                }
            }
        }
        return this.mDispatchHandler;
    }

    public /* synthetic */ boolean lambda$getDispatchHandler$6(Message message) {
        int i = message.what;
        if (i == 5) {
            doRequestDelay(DownloadType.MICRO);
            return true;
        } else if (i == 6) {
            doRequestDelay(DownloadType.THUMBNAIL);
            return true;
        } else if (i == 7) {
            doRequestLoading((ViewAware) message.obj);
            return true;
        } else if (i != 8) {
            return false;
        } else {
            doRequestLoading((List) message.obj);
            return true;
        }
    }

    public static String generateKey(Uri uri, DownloadType downloadType) {
        return DownloadUtil.generateKey(uri, downloadType);
    }

    public final boolean isValidRequest(String str, ViewAware viewAware) {
        if (viewAware != null && !TextUtils.isEmpty(viewAware.getKey()) && TextUtils.equals(str, viewAware.getKey())) {
            return viewAware.getImageAware() == null || !viewAware.getImageAware().isCollected();
        }
        return false;
    }

    public static <T> List<T> set2List(Set<T> set) {
        LinkedList linkedList = new LinkedList();
        if (set != null) {
            for (T t : set) {
                linkedList.add(t);
            }
        }
        return linkedList;
    }

    public static <E, T> List<T> map2List(Map<E, Set<T>> map) {
        LinkedList linkedList = new LinkedList();
        for (Map.Entry<E, Set<T>> entry : map.entrySet()) {
            Set<T> value = entry.getValue();
            if (value != null) {
                for (T t : value) {
                    linkedList.add(t);
                }
            }
        }
        return linkedList;
    }

    public final List<ViewAware> getRequestingAwareBy(String str) {
        List<ViewAware> list;
        synchronized (this.mRequestingRequest) {
            list = set2List(this.mRequestingRequest.get(str));
        }
        return list;
    }

    public final List<ViewAware> getAllRequestingAware() {
        List<ViewAware> map2List;
        synchronized (this.mRequestingRequest) {
            map2List = map2List(this.mRequestingRequest);
        }
        return map2List;
    }

    public final List<ViewAware> getMatchAware(DownloadWrapper downloadWrapper, boolean z) {
        List<ViewAware> requestingAwareBy;
        String generateKey = generateKey(downloadWrapper.uri, downloadWrapper.type);
        synchronized (this.mRequestingRequest) {
            requestingAwareBy = getRequestingAwareBy(generateKey);
            if (z) {
                removeMatchAware(downloadWrapper);
            }
        }
        Iterator<ViewAware> it = requestingAwareBy.iterator();
        while (it.hasNext()) {
            if (!isValidRequest(generateKey, it.next())) {
                it.remove();
            }
        }
        return requestingAwareBy;
    }

    public final List<ViewAware> getMatchAware(Uri uri, DownloadType downloadType) {
        List<ViewAware> requestingAwareBy;
        String generateKey = generateKey(uri, downloadType);
        synchronized (this.mRequestingRequest) {
            requestingAwareBy = getRequestingAwareBy(generateKey);
        }
        Iterator<ViewAware> it = requestingAwareBy.iterator();
        while (it.hasNext()) {
            if (!isValidRequest(generateKey, it.next())) {
                it.remove();
            }
        }
        return requestingAwareBy;
    }

    public final void removeMatchAware(DownloadWrapper downloadWrapper) {
        String generateKey = generateKey(downloadWrapper.uri, downloadWrapper.type);
        synchronized (this.mRequestingRequest) {
            this.mRequestingRequest.remove(generateKey);
        }
    }

    /* loaded from: classes2.dex */
    public static class ImageLoaderHolder {
        public static CloudImageLoader sLoader = new CloudImageLoader(StaticContext.sGetAndroidContext());
    }

    public static CloudImageLoader getInstance() {
        return ImageLoaderHolder.sLoader;
    }

    public void displayImage(Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, boolean z, boolean z2) {
        displayImage(uri, downloadType, imageView, requestOptions, (TransitionOptions<?, Bitmap>) null, (CloudImageLoadingListener) null, (CloudImageLoadingProgressListener) null, z, z2);
    }

    public void displayImage(Uri uri, DownloadType downloadType, ImageView imageView, RequestOptions requestOptions, TransitionOptions<?, Bitmap> transitionOptions, CloudImageLoadingListener cloudImageLoadingListener, CloudImageLoadingProgressListener cloudImageLoadingProgressListener, boolean z, boolean z2) {
        displayImage(uri, downloadType, new ImageViewAware(imageView), requestOptions, transitionOptions, cloudImageLoadingListener, cloudImageLoadingProgressListener, z, z2, false);
    }

    public void displayImage(Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, TransitionOptions<?, Bitmap> transitionOptions, CloudImageLoadingListener cloudImageLoadingListener, CloudImageLoadingProgressListener cloudImageLoadingProgressListener, boolean z, boolean z2) {
        displayImage(uri, downloadType, imageAware, requestOptions, transitionOptions, cloudImageLoadingListener, cloudImageLoadingProgressListener, z, z2, false);
    }

    public void displayImage(Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, TransitionOptions<?, Bitmap> transitionOptions, CloudImageLoadingListener cloudImageLoadingListener, CloudImageLoadingProgressListener cloudImageLoadingProgressListener, boolean z, boolean z2, boolean z3) {
        if (uri == null) {
            DefaultLogger.w("CloudImageLoader", "Can't display an image with null uri");
            if (requestOptions != null) {
                if (requestOptions.getPlaceholderDrawable() != null) {
                    directShowImage(imageAware, requestOptions.getPlaceholderDrawable());
                } else if (requestOptions.getPlaceholderId() != 0) {
                    directShowImage(imageAware, getResources(imageAware).getDrawable(requestOptions.getPlaceholderId()));
                }
            }
            onInvalidDownloadItem(uri, downloadType, imageAware.getWrappedView(), cloudImageLoadingListener);
            return;
        }
        long parseId = ContentUris.parseId(uri);
        if (parseId <= 0) {
            DefaultLogger.w("CloudImageLoader", "Can't display an image with illegal id %s", Long.valueOf(parseId));
            if (requestOptions != null) {
                if (requestOptions.getErrorPlaceholder() != null) {
                    directShowImage(imageAware, requestOptions.getErrorPlaceholder());
                } else if (requestOptions.getErrorId() != 0) {
                    directShowImage(imageAware, getResources(imageAware).getDrawable(requestOptions.getErrorId()));
                }
            }
            if (cloudImageLoadingListener == null) {
                return;
            }
            cloudImageLoadingListener.onLoadingFailed(uri, downloadType, imageAware.getWrappedView(), ErrorCode.UNKNOWN, null);
            return;
        }
        if (requestOptions != null && z2) {
            if (requestOptions.getPlaceholderDrawable() != null) {
                directShowImage(imageAware, requestOptions.getPlaceholderDrawable());
            } else if (requestOptions.getPlaceholderId() != 0) {
                directShowImage(imageAware, getResources(imageAware).getDrawable(requestOptions.getPlaceholderId()));
            }
        }
        WeakRefViewAwareImpl weakRefViewAwareImpl = new WeakRefViewAwareImpl(uri, downloadType, imageAware, requestOptions, transitionOptions, cloudImageLoadingListener, cloudImageLoadingProgressListener, z3);
        if (downloadType == DownloadType.MICRO || z) {
            loadImageDelay(weakRefViewAwareImpl);
        } else {
            getDispatchHandler().obtainMessage(7, weakRefViewAwareImpl).sendToTarget();
        }
    }

    public final Resources getResources(ImageAware imageAware) {
        View wrappedView = imageAware.getWrappedView();
        return wrappedView != null ? wrappedView.getResources() : GalleryApp.sGetAndroidContext().getResources();
    }

    public final void directShowImage(final ImageAware imageAware, final Drawable drawable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            imageAware.setImageDrawable(drawable);
        } else {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.util.glide.CloudImageLoader$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    CloudImageLoader.$r8$lambda$aiTCLcrFmvouGlIzrtn2pjM_3ik(ImageAware.this, drawable);
                }
            });
        }
    }

    public void loadImage(Uri uri, DownloadType downloadType, CloudImageLoadingListener cloudImageLoadingListener, CloudImageLoadingProgressListener cloudImageLoadingProgressListener) {
        if (uri == null) {
            onInvalidDownloadItem(uri, downloadType, null, cloudImageLoadingListener);
        } else if (ContentUris.parseId(uri) <= 0) {
            onInvalidDownloadItem(uri, downloadType, null, cloudImageLoadingListener);
        } else {
            NonViewAwareImpl nonViewAwareImpl = new NonViewAwareImpl(uri, downloadType, cloudImageLoadingListener, cloudImageLoadingProgressListener);
            if (downloadType == DownloadType.MICRO) {
                loadImageDelay(nonViewAwareImpl);
            } else {
                getDispatchHandler().obtainMessage(7, nonViewAwareImpl).sendToTarget();
            }
        }
    }

    public void loadImages(List<Uri> list, List<DownloadType> list2, List<CloudImageLoadingListener> list3, List<CloudImageLoadingProgressListener> list4) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (int i = 0; i < list.size(); i++) {
            CloudImageLoadingProgressListener cloudImageLoadingProgressListener = null;
            if (list.get(i) == null) {
                DefaultLogger.w("CloudImageLoader", "Null download uri!");
                onInvalidDownloadItem(list.get(i), list2.get(i), null, list3 == null ? null : list3.get(i));
            } else {
                Uri uri = list.get(i);
                DownloadType downloadType = list2.get(i);
                CloudImageLoadingListener cloudImageLoadingListener = list3 == null ? null : list3.get(i);
                if (list4 != null) {
                    cloudImageLoadingProgressListener = list4.get(i);
                }
                arrayList.add(new NonViewAwareImpl(uri, downloadType, cloudImageLoadingListener, cloudImageLoadingProgressListener));
            }
        }
        getDispatchHandler().obtainMessage(8, arrayList).sendToTarget();
    }

    public final void loadImageDelay(ViewAware viewAware) {
        Map<String, Set<ViewAware>> delayMap = getDelayMap(viewAware.getDownloadType());
        synchronized (delayMap) {
            if (put(delayMap, viewAware)) {
                sendDelayMessage(viewAware.getDownloadType());
            }
        }
    }

    public final Map<String, Set<ViewAware>> getDelayMap(DownloadType downloadType) {
        Map<String, Set<ViewAware>> map;
        synchronized (this.mDelayRequest) {
            map = this.mDelayRequest.get(downloadType);
            if (map == null) {
                map = new HashMap<>();
                this.mDelayRequest.put(downloadType, map);
            }
        }
        return map;
    }

    /* renamed from: com.miui.gallery.util.glide.CloudImageLoader$3 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$sdk$download$DownloadType;

        static {
            int[] iArr = new int[DownloadType.values().length];
            $SwitchMap$com$miui$gallery$sdk$download$DownloadType = iArr;
            try {
                iArr[DownloadType.MICRO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.THUMBNAIL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public final int getDelayMessage(DownloadType downloadType) {
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$sdk$download$DownloadType[downloadType.ordinal()];
        if (i != 1) {
            return i != 2 ? -1 : 6;
        }
        return 5;
    }

    public final void sendDelayMessage(DownloadType downloadType) {
        int delayMessage = getDelayMessage(downloadType);
        if (delayMessage >= 0) {
            Handler dispatchHandler = getDispatchHandler();
            dispatchHandler.removeMessages(delayMessage);
            Message obtainMessage = dispatchHandler.obtainMessage();
            obtainMessage.what = delayMessage;
            dispatchHandler.sendMessageDelayed(obtainMessage, 200L);
        }
    }

    public final void doRequestDelay(DownloadType downloadType) {
        Map<String, Set<ViewAware>> map;
        List<ViewAware> map2List;
        synchronized (this.mDelayRequest) {
            map = this.mDelayRequest.get(downloadType);
        }
        if (map != null) {
            int i = 0;
            synchronized (map) {
                map2List = map2List(map);
                map.clear();
            }
            int size = map2List.size();
            Iterator<ViewAware> it = map2List.iterator();
            while (it.hasNext()) {
                ViewAware next = it.next();
                if (next != null && !isValidRequest(next.getInternalKey(), next)) {
                    it.remove();
                    onInvalidDownloadItem(next.getInternalUri(), next.getInternalDownloadType(), next.getWrappedView(), next.getLoadingListener());
                    i++;
                }
            }
            if (BaseMiscUtil.isValid(map2List)) {
                Collections.sort(map2List);
                if (!requestLoading(map2List)) {
                    for (ViewAware viewAware : map2List) {
                        putToPending(viewAware);
                    }
                }
            }
            DefaultLogger.i("CloudImageLoader", "doRequestDelay, %d of %d are invalid requests", Integer.valueOf(i), Integer.valueOf(size));
        }
    }

    public final void cancelInvalidRequest(DownloadType downloadType) {
        if (downloadType == null) {
            return;
        }
        int i = 0;
        long currentTimeMillis = System.currentTimeMillis();
        List<ViewAware> allRequestingAware = getAllRequestingAware();
        for (ViewAware viewAware : allRequestingAware) {
            if (viewAware.getInternalDownloadType() == downloadType && !isValidRequest(viewAware.getInternalKey(), viewAware)) {
                ImageDownloader.getInstance().cancel(viewAware.getInternalUri(), viewAware.getInternalDownloadType());
                i++;
            }
        }
        DefaultLogger.i("CloudImageLoader", "cancel %d of %d items costs %dms", Integer.valueOf(i), Integer.valueOf(allRequestingAware.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public void cancel(Uri uri, DownloadType downloadType) {
        String generateKey = generateKey(uri, downloadType);
        synchronized (this.mPendingRequest) {
            this.mPendingRequest.remove(generateKey);
        }
        ImageDownloader.getInstance().cancel(uri, downloadType);
        if (isOrigin(downloadType)) {
            DownloadType downloadType2 = DownloadType.ORIGIN;
            if (downloadType == downloadType2) {
                downloadType2 = DownloadType.ORIGIN_FORCE;
            }
            ImageDownloader.getInstance().cancel(uri, downloadType2);
        }
    }

    public void cancelDisplayTask(Uri uri, DownloadType downloadType, ImageView imageView) {
        for (ViewAware viewAware : getMatchAware(uri, downloadType)) {
            if (Objects.equals(viewAware.getWrappedView(), imageView)) {
                ImageAware imageAware = viewAware.getImageAware();
                if (imageAware != null) {
                    CloudImageHolder.getImageHolder(imageAware).setNeedDisplay(false);
                    return;
                } else {
                    viewAware.setLoadingProgressListener(null);
                    viewAware.setLoadingListener(null);
                }
            }
        }
    }

    public static boolean put(Map<String, Set<ViewAware>> map, ViewAware viewAware) {
        String key = viewAware.getKey();
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        Set<ViewAware> set = map.get(key);
        if (set == null) {
            set = new HashSet<>();
            map.put(key, set);
        }
        return set.add(viewAware);
    }

    public final boolean putToRequesting(ViewAware viewAware) {
        boolean put;
        synchronized (this.mRequestingRequest) {
            put = put(this.mRequestingRequest, viewAware);
        }
        return put;
    }

    public final boolean putToPending(ViewAware viewAware) {
        boolean put;
        synchronized (this.mPendingRequest) {
            put = put(this.mPendingRequest, viewAware);
        }
        return put;
    }

    public final boolean doRequestLoading(ViewAware viewAware) {
        if (!requestLoading(viewAware)) {
            putToPending(viewAware);
            return false;
        }
        return false;
    }

    public final boolean doRequestLoading(List<ViewAware> list) {
        if (!requestLoading(list)) {
            for (ViewAware viewAware : list) {
                putToPending(viewAware);
            }
            return false;
        }
        return false;
    }

    public final boolean requestLoading(ViewAware viewAware) {
        boolean z = false;
        if (!this.mPauseLoading) {
            DownloadType downloadType = viewAware.getDownloadType();
            boolean putToRequesting = putToRequesting(viewAware);
            if (isOrigin(downloadType) || isThumbnail(downloadType)) {
                z = true;
            }
            ImageDownloader.getInstance().load(viewAware.getUri(), viewAware.getDownloadType(), new DownloadOptions.Builder().setRequireWLAN(Config$ImageDownload.requireWLAN(downloadType)).setQueueFirst(z).setInterruptExecuting(isOrigin(downloadType)).setManual(viewAware.isManualDownload()).build(), this.mDownloadListener, this.mProgressListener);
            if (putToRequesting && needCancelInvalidRequest(downloadType)) {
                cancelInvalidRequest(downloadType);
            }
            return true;
        }
        return false;
    }

    public final boolean needCancelInvalidRequest(DownloadType downloadType) {
        return !isOrigin(downloadType);
    }

    public final boolean requestLoading(List<ViewAware> list) {
        if (!this.mPauseLoading) {
            int i = 0;
            boolean z = false;
            while (true) {
                boolean z2 = true;
                if (i >= list.size()) {
                    break;
                }
                ViewAware viewAware = list.get(i);
                DownloadType downloadType = viewAware.getDownloadType();
                z |= putToRequesting(viewAware);
                boolean z3 = isOrigin(downloadType) || isThumbnail(downloadType);
                if (!isOrigin(downloadType) || i != 0) {
                    z2 = false;
                }
                ImageDownloader.getInstance().load(viewAware.getUri(), viewAware.getDownloadType(), new DownloadOptions.Builder().setRequireWLAN(Config$ImageDownload.requireWLAN(downloadType)).setQueueFirst(z3).setInterruptExecuting(z2).setManual(viewAware.isManualDownload()).build(), this.mDownloadListener, this.mProgressListener);
                i++;
            }
            if (z && list.size() > 0) {
                DownloadType downloadType2 = list.get(0).getDownloadType();
                if (needCancelInvalidRequest(downloadType2)) {
                    cancelInvalidRequest(downloadType2);
                }
            }
            return true;
        }
        return false;
    }

    public final void onInvalidDownloadItem(Uri uri, DownloadType downloadType, View view, CloudImageLoadingListener cloudImageLoadingListener) {
        if (cloudImageLoadingListener != null) {
            cloudImageLoadingListener.onLoadingFailed(uri, downloadType, view, ErrorCode.PARAMS_ERROR, null);
        }
    }

    public boolean isOrigin(DownloadType downloadType) {
        return downloadType == DownloadType.ORIGIN || downloadType == DownloadType.ORIGIN_FORCE;
    }

    public boolean isThumbnail(DownloadType downloadType) {
        return downloadType == DownloadType.THUMBNAIL;
    }

    public boolean isRequesting(Uri uri, DownloadType downloadType) {
        String generateKey = generateKey(uri, downloadType);
        return this.mPendingRequest.containsKey(generateKey) || this.mRequestingRequest.containsKey(generateKey);
    }

    public AsyncTask<Void, Void, DownloadItemStatus> getStatusAsync(Uri uri, DownloadType downloadType, GetStatusCallBack getStatusCallBack) {
        return new GetStatusAsyncTask(uri, downloadType, getStatusCallBack).execute(new Void[0]);
    }

    /* loaded from: classes2.dex */
    public static class GetStatusAsyncTask extends AsyncTask<Void, Void, DownloadItemStatus> {
        public final DownloadType mDownloadType;
        public WeakReference<GetStatusCallBack> mGetStatusCallBack;
        public final Uri mUri;

        public GetStatusAsyncTask(Uri uri, DownloadType downloadType, GetStatusCallBack getStatusCallBack) {
            this.mUri = uri;
            this.mDownloadType = downloadType;
            this.mGetStatusCallBack = new WeakReference<>(getStatusCallBack);
        }

        @Override // android.os.AsyncTask
        public DownloadItemStatus doInBackground(Void... voidArr) {
            return ImageDownloader.getInstance().getStatusSync(this.mUri, this.mDownloadType);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(DownloadItemStatus downloadItemStatus) {
            WeakReference<GetStatusCallBack> weakReference;
            if (isCancelled() || (weakReference = this.mGetStatusCallBack) == null || weakReference.get() == null) {
                return;
            }
            this.mGetStatusCallBack.get().onStatusGotten(downloadItemStatus);
        }

        @Override // android.os.AsyncTask
        public void onCancelled() {
            super.onCancelled();
            WeakReference<GetStatusCallBack> weakReference = this.mGetStatusCallBack;
            if (weakReference != null) {
                weakReference.clear();
                this.mGetStatusCallBack = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class ViewAware implements Comparable<ViewAware> {
        public final DownloadType mInternalType;
        public final Uri mInternalUri;
        public final long mRequestTime = System.currentTimeMillis();

        public abstract DownloadType getDownloadType();

        public abstract ImageAware getImageAware();

        public abstract String getKey();

        public abstract CloudImageLoadingListener getLoadingListener();

        public abstract CloudImageLoadingProgressListener getLoadingProgressListener();

        public abstract RequestOptions getRequestOptions();

        public abstract TransitionOptions<?, Bitmap> getTransitionOptions();

        public abstract Uri getUri();

        public abstract boolean isManualDownload();

        public abstract boolean needDisplay();

        public void setLoadingListener(CloudImageLoadingListener cloudImageLoadingListener) {
        }

        public void setLoadingProgressListener(CloudImageLoadingProgressListener cloudImageLoadingProgressListener) {
        }

        public ViewAware(Uri uri, DownloadType downloadType) {
            this.mInternalUri = uri;
            this.mInternalType = downloadType;
        }

        public final Uri getInternalUri() {
            return this.mInternalUri;
        }

        public final DownloadType getInternalDownloadType() {
            return this.mInternalType;
        }

        public final String getInternalKey() {
            return CloudImageLoader.generateKey(getInternalUri(), getInternalDownloadType());
        }

        public View getWrappedView() {
            if (getImageAware() != null) {
                return getImageAware().getWrappedView();
            }
            return null;
        }

        public final long getRequestTime() {
            return this.mRequestTime;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ViewAware)) {
                return false;
            }
            return (getWrappedView() == null && ((ViewAware) obj).getWrappedView() == null) ? this == obj : getWrappedView() == ((ViewAware) obj).getWrappedView();
        }

        public int hashCode() {
            View wrappedView = getWrappedView();
            return wrappedView != null ? wrappedView.hashCode() : super.hashCode();
        }

        @Override // java.lang.Comparable
        public int compareTo(ViewAware viewAware) {
            int i;
            if (viewAware != null && getRequestTime() - viewAware.getRequestTime() <= 0) {
                return i < 0 ? -1 : 0;
            }
            return 1;
        }
    }

    /* loaded from: classes2.dex */
    public static class WeakRefViewAwareImpl extends ViewAware {
        public ImageAware mImageAware;

        public WeakRefViewAwareImpl(Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, TransitionOptions<?, Bitmap> transitionOptions, CloudImageLoadingListener cloudImageLoadingListener, CloudImageLoadingProgressListener cloudImageLoadingProgressListener, boolean z, boolean z2) {
            super(uri, downloadType);
            CloudImageHolder.getImageHolder(imageAware).setUri(uri).setImageType(downloadType).setRequestOptions(requestOptions).setTransitionOptions(transitionOptions).setImageLoadingListener(cloudImageLoadingListener).setImageLoadingProgressListener(cloudImageLoadingProgressListener).setNeedDisplay(z).setManualDownload(z2);
            this.mImageAware = imageAware;
        }

        public WeakRefViewAwareImpl(Uri uri, DownloadType downloadType, ImageAware imageAware, RequestOptions requestOptions, TransitionOptions<?, Bitmap> transitionOptions, CloudImageLoadingListener cloudImageLoadingListener, CloudImageLoadingProgressListener cloudImageLoadingProgressListener, boolean z) {
            this(uri, downloadType, imageAware, requestOptions, transitionOptions, cloudImageLoadingListener, cloudImageLoadingProgressListener, true, z);
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public String getKey() {
            return CloudImageLoader.generateKey(getUri(), getDownloadType());
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public ImageAware getImageAware() {
            return this.mImageAware;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public Uri getUri() {
            return CloudImageHolder.getImageHolder(this.mImageAware).getUri();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public DownloadType getDownloadType() {
            return CloudImageHolder.getImageHolder(this.mImageAware).getImageType();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public CloudImageLoadingListener getLoadingListener() {
            return CloudImageHolder.getImageHolder(this.mImageAware).getImageLoadingListener();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public CloudImageLoadingProgressListener getLoadingProgressListener() {
            return CloudImageHolder.getImageHolder(this.mImageAware).getImageLoadingProgressListener();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public RequestOptions getRequestOptions() {
            return CloudImageHolder.getImageHolder(this.mImageAware).getRequestOptions();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public TransitionOptions<?, Bitmap> getTransitionOptions() {
            return CloudImageHolder.getImageHolder(this.mImageAware).getTransitionOptions();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public boolean isManualDownload() {
            return CloudImageHolder.getImageHolder(this.mImageAware).isManualDownload();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public boolean needDisplay() {
            return CloudImageHolder.getImageHolder(this.mImageAware).needDisplay();
        }
    }

    /* loaded from: classes2.dex */
    public static class NonViewAwareImpl extends ViewAware {
        public CloudImageLoadingListener mLoadingListener;
        public CloudImageLoadingProgressListener mProgressListener;

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public ImageAware getImageAware() {
            return null;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public RequestOptions getRequestOptions() {
            return null;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public TransitionOptions<?, Bitmap> getTransitionOptions() {
            return null;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public boolean isManualDownload() {
            return false;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public boolean needDisplay() {
            return false;
        }

        public NonViewAwareImpl(Uri uri, DownloadType downloadType, CloudImageLoadingListener cloudImageLoadingListener, CloudImageLoadingProgressListener cloudImageLoadingProgressListener) {
            super(uri, downloadType);
            this.mLoadingListener = cloudImageLoadingListener;
            this.mProgressListener = cloudImageLoadingProgressListener;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public Uri getUri() {
            return getInternalUri();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public DownloadType getDownloadType() {
            return getInternalDownloadType();
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public String getKey() {
            return CloudImageLoader.generateKey(getUri(), getDownloadType());
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public CloudImageLoadingListener getLoadingListener() {
            return this.mLoadingListener;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public CloudImageLoadingProgressListener getLoadingProgressListener() {
            return this.mProgressListener;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public void setLoadingListener(CloudImageLoadingListener cloudImageLoadingListener) {
            this.mLoadingListener = cloudImageLoadingListener;
        }

        @Override // com.miui.gallery.util.glide.CloudImageLoader.ViewAware
        public void setLoadingProgressListener(CloudImageLoadingProgressListener cloudImageLoadingProgressListener) {
            this.mProgressListener = cloudImageLoadingProgressListener;
        }
    }
}
