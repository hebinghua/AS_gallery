package com.miui.gallery.assistant.manager.request.param;

import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.sdk.download.DownloadType;

/* loaded from: classes.dex */
public abstract class ImageFeatureRequestParams<T> implements RequestParams<T> {
    public boolean mAllowedOverMetered;
    public DownloadType mDownloadType;
    public MediaFeatureItem mMediaFeatureItem;
    public boolean mShouldDownloadIfNotExist;

    public ImageFeatureRequestParams(MediaFeatureItem mediaFeatureItem) {
        this.mMediaFeatureItem = mediaFeatureItem;
        this.mShouldDownloadIfNotExist = false;
    }

    public ImageFeatureRequestParams(MediaFeatureItem mediaFeatureItem, boolean z, boolean z2, DownloadType downloadType) {
        this.mMediaFeatureItem = mediaFeatureItem;
        this.mShouldDownloadIfNotExist = z;
        this.mAllowedOverMetered = z2;
        this.mDownloadType = downloadType;
    }

    public MediaFeatureItem getMediaFeatureItem() {
        return this.mMediaFeatureItem;
    }

    public boolean isShouldDownloadIfNotExist() {
        return this.mShouldDownloadIfNotExist;
    }

    public boolean isAllowedOverMetered() {
        return this.mAllowedOverMetered;
    }

    public DownloadType getDownloadType() {
        return this.mDownloadType;
    }
}
