package com.miui.gallery.util.glide;

import android.graphics.Bitmap;
import android.net.Uri;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.glide.ImageAware;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingProgressListener;

/* loaded from: classes2.dex */
public class CloudImageHolder {
    public boolean mDelayRequest;
    public CloudImageLoadingListener mImageLoadingListener;
    public CloudImageLoadingProgressListener mImageLoadingProgressListener;
    public DownloadType mImageType;
    public boolean mIsManualDownload;
    public RequestOptions mRequestOptions;
    public TransitionOptions<?, Bitmap> mTransitionOptions;
    public Uri mUri;
    public boolean mNeedDisplay = true;
    public boolean mShowLoadingImage = true;

    public CloudImageHolder setUri(Uri uri) {
        this.mUri = uri;
        return this;
    }

    public CloudImageHolder setImageType(DownloadType downloadType) {
        this.mImageType = downloadType;
        return this;
    }

    public CloudImageHolder setRequestOptions(RequestOptions requestOptions) {
        this.mRequestOptions = requestOptions;
        return this;
    }

    public CloudImageHolder setTransitionOptions(TransitionOptions<?, Bitmap> transitionOptions) {
        this.mTransitionOptions = transitionOptions;
        return this;
    }

    public CloudImageHolder setImageLoadingListener(CloudImageLoadingListener cloudImageLoadingListener) {
        this.mImageLoadingListener = cloudImageLoadingListener;
        return this;
    }

    public CloudImageHolder setImageLoadingProgressListener(CloudImageLoadingProgressListener cloudImageLoadingProgressListener) {
        this.mImageLoadingProgressListener = cloudImageLoadingProgressListener;
        return this;
    }

    public CloudImageHolder setNeedDisplay(boolean z) {
        this.mNeedDisplay = z;
        return this;
    }

    public CloudImageHolder setDelayRequest(boolean z) {
        this.mDelayRequest = z;
        return this;
    }

    public CloudImageHolder setShowLoadingImage(boolean z) {
        this.mShowLoadingImage = z;
        return this;
    }

    public CloudImageHolder setManualDownload(boolean z) {
        this.mIsManualDownload = z;
        return this;
    }

    public TransitionOptions<?, Bitmap> getTransitionOptions() {
        return this.mTransitionOptions;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public CloudImageLoadingListener getImageLoadingListener() {
        return this.mImageLoadingListener;
    }

    public CloudImageLoadingProgressListener getImageLoadingProgressListener() {
        return this.mImageLoadingProgressListener;
    }

    public DownloadType getImageType() {
        return this.mImageType;
    }

    public RequestOptions getRequestOptions() {
        return this.mRequestOptions;
    }

    public boolean needDisplay() {
        return this.mNeedDisplay;
    }

    public boolean isDelayRequest() {
        return this.mDelayRequest;
    }

    public boolean isShowLoadingImage() {
        return this.mShowLoadingImage;
    }

    public boolean isManualDownload() {
        return this.mIsManualDownload;
    }

    public static CloudImageHolder getImageHolder(ImageAware imageAware) {
        Object tag = imageAware.getTag(R.id.tag_cloud_image_holder);
        if (tag == null) {
            tag = new CloudImageHolder();
            imageAware.setTag(R.id.tag_cloud_image_holder, tag);
        }
        return (CloudImageHolder) tag;
    }
}
