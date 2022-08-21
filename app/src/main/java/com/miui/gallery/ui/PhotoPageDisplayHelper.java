package com.miui.gallery.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class PhotoPageDisplayHelper {
    public ResourceCallback mCallback;
    public BitmapTarget mCurrent;
    public final Handler mHandler = new Handler(Looper.getMainLooper(), new LoaderCallback());
    public boolean mIsCleared;
    public BitmapTarget mNext;
    public BitmapTarget mPrevious;
    public final RequestManager mRequestManager;

    /* loaded from: classes2.dex */
    public interface ResourceCallback {
        void onResourceCleared();

        boolean onResourceReady(String str, Bitmap bitmap, boolean z, boolean z2);
    }

    public PhotoPageDisplayHelper(RequestManager requestManager, ResourceCallback resourceCallback) {
        this.mRequestManager = requestManager;
        this.mCallback = resourceCallback;
    }

    public final void onResourceReady(BitmapTarget bitmapTarget) {
        if (this.mIsCleared) {
            this.mHandler.obtainMessage(2, bitmapTarget).sendToTarget();
            return;
        }
        this.mPrevious = this.mCurrent;
        this.mCurrent = bitmapTarget;
        if (!this.mCallback.onResourceReady(bitmapTarget.getUri(), bitmapTarget.getBitmap(), bitmapTarget.isCancelled(), bitmapTarget.isPreview())) {
            return;
        }
        clearPrevious();
    }

    public void clearPrevious() {
        BitmapTarget bitmapTarget = this.mPrevious;
        if (bitmapTarget == null || bitmapTarget == this.mCurrent) {
            return;
        }
        this.mHandler.obtainMessage(2, bitmapTarget).sendToTarget();
    }

    public void displayPreviewImage(GalleryModel galleryModel, RequestOptions requestOptions) {
        displayImage(galleryModel, requestOptions, null, true);
    }

    public void displayImage(GalleryModel galleryModel, RequestOptions requestOptions) {
        displayImage(galleryModel, requestOptions, null, false);
    }

    public void displayImage(GalleryModel galleryModel, RequestOptions requestOptions, RequestListener<Bitmap> requestListener) {
        displayImage(galleryModel, requestOptions, requestListener, false);
    }

    public final void displayImage(GalleryModel galleryModel, RequestOptions requestOptions, RequestListener<Bitmap> requestListener, boolean z) {
        BitmapTarget bitmapTarget;
        BitmapTarget bitmapTarget2 = this.mNext;
        if (bitmapTarget2 != null && bitmapTarget2 != this.mCurrent && !bitmapTarget2.isPreview()) {
            this.mRequestManager.clear(this.mNext);
        }
        RequestBuilder<Bitmap> mo946apply = this.mRequestManager.mo985asBitmap().mo962load(galleryModel).mo959listener(requestListener).mo946apply((BaseRequestOptions<?>) requestOptions);
        if (galleryModel.isCameraPreview().booleanValue() || galleryModel.isIsJustEditExported()) {
            String path = galleryModel.getPath();
            GalleryModel of = GalleryModel.of(path);
            of.setIsCameraPreview(galleryModel.isCameraPreview().booleanValue());
            of.setIsJustEditExported(galleryModel.isIsJustEditExported());
            mo946apply.mo979thumbnail((RequestBuilder) this.mRequestManager.mo985asBitmap().mo962load(of).mo946apply((BaseRequestOptions<?>) requestOptions).mo978skipMemoryCache(true).mo983useUnlimitedSourceGeneratorsPool(true).mo950diskCacheStrategy(DiskCacheStrategy.NONE));
            DefaultLogger.d("PhotoPageDisplayHelper", "finish build camera preview request for [%s].", galleryModel.getPath());
            DefaultLogger.d("PhotoPageDisplayHelper", "finish build preview request[isCameraPreview: %b, isIsJustEditExported: %b] for [%s].", of.isCameraPreview(), Boolean.valueOf(of.isIsJustEditExported()), of.getPath());
            galleryModel.setIsCameraPreview(false);
            galleryModel.setIsJustEditExported(false);
            DefaultLogger.d("photoPageStartup", "displayImageFromCamera uri:%s ", path);
        }
        if (z) {
            bitmapTarget = new BitmapTarget(this.mHandler, galleryModel.getPath(), z) { // from class: com.miui.gallery.ui.PhotoPageDisplayHelper.1
                @Override // com.miui.gallery.ui.PhotoPageDisplayHelper.BitmapTarget, com.bumptech.glide.request.target.Target
                public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                    onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
                }

                @Override // com.miui.gallery.ui.PhotoPageDisplayHelper.BitmapTarget
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    this.mBitmap = bitmap;
                    this.mIsCancelled = false;
                    PhotoPageDisplayHelper.this.onResourceReady(this);
                }

                @Override // com.miui.gallery.ui.PhotoPageDisplayHelper.BitmapTarget, com.bumptech.glide.request.target.CustomTarget, com.bumptech.glide.request.target.Target
                public void onLoadFailed(Drawable drawable) {
                    this.mBitmap = null;
                    this.mIsCancelled = false;
                    PhotoPageDisplayHelper.this.onResourceReady(this);
                }
            };
        } else {
            bitmapTarget = new BitmapTarget(this.mHandler, galleryModel.getPath(), z);
        }
        mo946apply.into((RequestBuilder<Bitmap>) bitmapTarget);
        this.mNext = bitmapTarget;
    }

    public void attach() {
        this.mIsCleared = false;
    }

    public void detach() {
        this.mIsCleared = true;
    }

    public void onTrimMemory() {
        this.mCallback.onResourceCleared();
        ThreadManager.getWorkHandler().post(new TrimMemoryTask(this.mRequestManager, this.mPrevious, this.mCurrent, this.mNext));
        this.mPrevious = null;
        this.mCurrent = null;
        BitmapTarget bitmapTarget = this.mNext;
        if (bitmapTarget != null) {
            this.mRequestManager.clear(bitmapTarget);
            this.mNext = null;
        }
    }

    /* loaded from: classes2.dex */
    public static class TrimMemoryTask implements Runnable {
        public final BitmapTarget mCurrent;
        public final BitmapTarget mPrevious;
        public final RequestManager mRequestManager;

        public TrimMemoryTask(RequestManager requestManager, BitmapTarget bitmapTarget, BitmapTarget bitmapTarget2, BitmapTarget bitmapTarget3) {
            this.mRequestManager = requestManager;
            this.mPrevious = bitmapTarget;
            this.mCurrent = bitmapTarget2;
        }

        @Override // java.lang.Runnable
        public void run() {
            RequestManager requestManager = this.mRequestManager;
            if (requestManager == null) {
                return;
            }
            BitmapTarget bitmapTarget = this.mPrevious;
            if (bitmapTarget != null) {
                requestManager.clear(bitmapTarget);
            }
            BitmapTarget bitmapTarget2 = this.mCurrent;
            if (bitmapTarget2 == null) {
                return;
            }
            this.mRequestManager.clear(bitmapTarget2);
        }
    }

    /* loaded from: classes2.dex */
    public class LoaderCallback implements Handler.Callback {
        public LoaderCallback() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                PhotoPageDisplayHelper.this.onResourceReady((BitmapTarget) message.obj);
            } else if (i == 2) {
                BitmapTarget bitmapTarget = (BitmapTarget) message.obj;
                PhotoPageDisplayHelper.this.mRequestManager.clear(bitmapTarget);
                if (bitmapTarget == PhotoPageDisplayHelper.this.mPrevious) {
                    PhotoPageDisplayHelper.this.mPrevious = null;
                }
            }
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public static class BitmapTarget extends CustomTarget<Bitmap> {
        public Bitmap mBitmap;
        public final Handler mHandler;
        public boolean mIsCancelled;
        public final boolean mIsPreview;
        public final String mUri;

        @Override // com.bumptech.glide.request.target.Target
        public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
            onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
        }

        public BitmapTarget(Handler handler, String str, boolean z) {
            this.mHandler = handler;
            this.mUri = str;
            this.mIsPreview = z;
        }

        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            this.mBitmap = bitmap;
            this.mIsCancelled = false;
            DefaultLogger.d("photoPageStartup", "send bind bitmap msg");
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, this));
        }

        @Override // com.bumptech.glide.request.target.CustomTarget, com.bumptech.glide.request.target.Target
        public void onLoadFailed(Drawable drawable) {
            this.mBitmap = null;
            this.mIsCancelled = false;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, this));
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onLoadCleared(Drawable drawable) {
            this.mBitmap = null;
            this.mIsCancelled = true;
        }

        public Bitmap getBitmap() {
            return this.mBitmap;
        }

        public String getUri() {
            return this.mUri;
        }

        public boolean isCancelled() {
            return this.mIsCancelled;
        }

        public boolean isPreview() {
            return this.mIsPreview;
        }
    }
}
