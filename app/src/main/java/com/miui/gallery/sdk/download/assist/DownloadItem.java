package com.miui.gallery.sdk.download.assist;

import android.net.Uri;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.adapter.IUriAdapter;
import com.miui.gallery.sdk.download.downloader.IDownloader;
import com.miui.gallery.sdk.download.listener.DownloadListener;
import com.miui.gallery.sdk.download.listener.DownloadProgressListener;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class DownloadItem {
    public final DownloadListener mDownloadListener;
    public final IDownloader mDownloader;
    public final boolean mManual;
    public final DownloadProgressListener mProgressListener;
    public final boolean mRequireCharging;
    public final boolean mRequireDeviceStorage;
    public final boolean mRequirePower;
    public final boolean mRequireWLAN;
    public long mStartTime;
    public AtomicInteger mStatus;
    public final DownloadType mType;
    public final Uri mUri;
    public final IUriAdapter mUriAdapter;
    public final ReentrantLock mUriLock;

    public DownloadItem(Builder builder) {
        this.mStatus = new AtomicInteger(0);
        this.mUri = builder.mUri;
        this.mUriAdapter = builder.mUriAdapter;
        this.mUriLock = builder.mUriLock;
        this.mType = builder.mType;
        this.mDownloadListener = builder.mDownloadListener;
        this.mProgressListener = builder.mProgressListener;
        this.mDownloader = builder.mDownloader;
        this.mRequirePower = builder.mRequirePower;
        this.mRequireCharging = builder.mRequireCharging;
        this.mRequireWLAN = builder.mRequireWLAN;
        this.mRequireDeviceStorage = builder.mRequireDeviceStorage;
        this.mManual = builder.mManual;
        this.mStartTime = System.currentTimeMillis();
    }

    public DownloadItem(DownloadItem downloadItem) {
        this(new Builder().cloneFrom(downloadItem));
        this.mStartTime = downloadItem.mStartTime;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public DownloadType getType() {
        return this.mType;
    }

    public IUriAdapter getUriAdapter() {
        return this.mUriAdapter;
    }

    public ReentrantLock getUriLock() {
        return this.mUriLock;
    }

    public DownloadListener getDownloadListener() {
        return this.mDownloadListener;
    }

    public DownloadProgressListener getProgressListener() {
        return this.mProgressListener;
    }

    public IDownloader getDownloader() {
        return this.mDownloader;
    }

    public boolean isRequirePower() {
        return this.mRequirePower;
    }

    public boolean isRequireCharging() {
        return this.mRequireCharging;
    }

    public boolean isRequireWLAN() {
        return this.mRequireWLAN;
    }

    public boolean isRequireDeviceStorage() {
        return this.mRequireDeviceStorage;
    }

    public boolean isManual() {
        return this.mManual;
    }

    public String getKey() {
        return DownloadUtil.generateKey(this.mUri, this.mType);
    }

    public int getPriority() {
        int i = !isManual() ? 999 : 1000;
        if (isRequireWLAN()) {
            i -= 3;
        }
        if (isRequirePower()) {
            i -= 5;
        }
        if (isRequireDeviceStorage()) {
            i -= 11;
        }
        return isRequireCharging() ? i - 21 : i;
    }

    public int getStatus() {
        return this.mStatus.get();
    }

    public boolean compareAnsSetStatus(int i, int i2) {
        return this.mStatus.compareAndSet(i, i2);
    }

    public boolean isStatusOk() {
        return getStatus() == 0;
    }

    public boolean isCancelled() {
        return getStatus() == 1;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public String toString() {
        return String.format(Locale.US, "uri[%s], type[%s]", this.mUri, this.mType.name());
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public DownloadListener mDownloadListener;
        public IDownloader mDownloader;
        public boolean mManual;
        public DownloadProgressListener mProgressListener;
        public boolean mRequireCharging;
        public boolean mRequireDeviceStorage;
        public boolean mRequirePower;
        public boolean mRequireWLAN;
        public DownloadType mType;
        public Uri mUri;
        public IUriAdapter mUriAdapter;
        public ReentrantLock mUriLock;

        public Builder setUri(Uri uri) {
            this.mUri = uri;
            return this;
        }

        public Builder setUriAdapter(IUriAdapter iUriAdapter) {
            this.mUriAdapter = iUriAdapter;
            return this;
        }

        public Builder setUriLock(ReentrantLock reentrantLock) {
            this.mUriLock = reentrantLock;
            return this;
        }

        public Builder setType(DownloadType downloadType) {
            this.mType = downloadType;
            return this;
        }

        public Builder setDownloadListener(DownloadListener downloadListener) {
            this.mDownloadListener = downloadListener;
            return this;
        }

        public Builder setProgressListener(DownloadProgressListener downloadProgressListener) {
            this.mProgressListener = downloadProgressListener;
            return this;
        }

        public Builder setDownloader(IDownloader iDownloader) {
            this.mDownloader = iDownloader;
            return this;
        }

        public Builder setRequirePower(boolean z) {
            this.mRequirePower = z;
            return this;
        }

        public Builder setRequireCharging(boolean z) {
            this.mRequireCharging = z;
            return this;
        }

        public Builder setRequireWLAN(boolean z) {
            this.mRequireWLAN = z;
            return this;
        }

        public Builder setRequireDeviceStorage(boolean z) {
            this.mRequireDeviceStorage = z;
            return this;
        }

        public Builder setManual(boolean z) {
            this.mManual = z;
            return this;
        }

        public Builder cloneFrom(DownloadItem downloadItem) {
            this.mUri = downloadItem.getUri();
            this.mType = downloadItem.getType();
            this.mUriAdapter = downloadItem.getUriAdapter();
            this.mUriLock = downloadItem.getUriLock();
            this.mDownloadListener = downloadItem.getDownloadListener();
            this.mProgressListener = downloadItem.getProgressListener();
            this.mDownloader = downloadItem.getDownloader();
            this.mRequirePower = downloadItem.isRequirePower();
            this.mRequireCharging = downloadItem.isRequireCharging();
            this.mRequireWLAN = downloadItem.isRequireWLAN();
            this.mRequireDeviceStorage = downloadItem.isRequireDeviceStorage();
            this.mManual = downloadItem.isManual();
            return this;
        }

        public DownloadItem build() {
            return new DownloadItem(this);
        }
    }

    public static void callbackStarted(DownloadItem downloadItem) {
        DownloadListener downloadListener = downloadItem.getDownloadListener();
        if (downloadListener != null) {
            downloadListener.onDownloadStarted(downloadItem.getUri(), downloadItem.getType());
        }
    }

    public static void callbackSuccess(DownloadItem downloadItem, DownloadedItem downloadedItem) {
        DownloadListener downloadListener = downloadItem.getDownloadListener();
        if (downloadListener != null) {
            downloadListener.onDownloadSuccess(downloadItem.getUri(), downloadItem.getType(), downloadedItem);
        }
    }

    public static void callbackError(DownloadItem downloadItem, DownloadFailReason downloadFailReason) {
        DownloadListener downloadListener = downloadItem.getDownloadListener();
        if (downloadListener != null) {
            downloadListener.onDownloadFail(downloadItem.getUri(), downloadItem.getType(), downloadFailReason);
        }
    }

    public static void callbackCancel(DownloadItem downloadItem) {
        DownloadListener downloadListener = downloadItem.getDownloadListener();
        if (downloadListener != null) {
            downloadListener.onDownloadCancel(downloadItem.getUri(), downloadItem.getType());
        }
    }

    public static void callbackProgress(DownloadItem downloadItem, long j, long j2) {
        DownloadProgressListener progressListener = downloadItem.getProgressListener();
        if (progressListener != null) {
            progressListener.onDownloadProgress(downloadItem.getUri(), downloadItem.getType(), j, j2);
        }
    }
}
