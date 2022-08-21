package com.miui.gallery.sdk.download;

import com.miui.gallery.sdk.download.adapter.IUriAdapter;

/* loaded from: classes2.dex */
public class DownloadOptions {
    public final boolean mInterruptExecuting;
    public final boolean mManual;
    public final boolean mQueueFirst;
    public final boolean mRequireCharging;
    public final boolean mRequireDeviceStorage;
    public final boolean mRequirePower;
    public final boolean mRequireWLAN;
    public final IUriAdapter mUriAdapter;

    public DownloadOptions(Builder builder) {
        this.mUriAdapter = builder.mUriAdapter;
        this.mQueueFirst = builder.mQueueFirst;
        this.mInterruptExecuting = builder.mInterruptExecuting;
        this.mRequireWLAN = builder.mRequireWLAN;
        this.mRequirePower = builder.mRequirePower;
        this.mRequireCharging = builder.mRequireCharging;
        this.mRequireDeviceStorage = builder.mRequireDeviceStorage;
        this.mManual = builder.mManual;
    }

    public IUriAdapter getUriAdapter() {
        return this.mUriAdapter;
    }

    public boolean isQueueFirst() {
        return this.mQueueFirst;
    }

    public boolean isInterruptExecuting() {
        return this.mInterruptExecuting;
    }

    public boolean isRequireWLAN() {
        return this.mRequireWLAN;
    }

    public boolean isRequirePower() {
        return this.mRequirePower;
    }

    public boolean isRequireCharging() {
        return this.mRequireCharging;
    }

    public boolean isRequireDeviceStorage() {
        return this.mRequireDeviceStorage;
    }

    public boolean isManual() {
        return this.mManual;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public boolean mInterruptExecuting;
        public boolean mManual;
        public boolean mQueueFirst;
        public boolean mRequireCharging;
        public boolean mRequireDeviceStorage;
        public boolean mRequirePower;
        public boolean mRequireWLAN;
        public IUriAdapter mUriAdapter;

        public Builder setUriAdapter(IUriAdapter iUriAdapter) {
            this.mUriAdapter = iUriAdapter;
            return this;
        }

        public Builder setQueueFirst(boolean z) {
            this.mQueueFirst = z;
            return this;
        }

        public Builder setInterruptExecuting(boolean z) {
            this.mInterruptExecuting = z;
            return this;
        }

        public Builder setRequireWLAN(boolean z) {
            this.mRequireWLAN = z;
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

        public Builder setRequireDeviceStorage(boolean z) {
            this.mRequireDeviceStorage = z;
            return this;
        }

        public Builder setManual(boolean z) {
            this.mManual = z;
            return this;
        }

        public Builder cloneFrom(DownloadOptions downloadOptions) {
            this.mUriAdapter = downloadOptions.mUriAdapter;
            this.mQueueFirst = downloadOptions.mQueueFirst;
            this.mInterruptExecuting = downloadOptions.mInterruptExecuting;
            this.mRequireWLAN = downloadOptions.mRequireWLAN;
            this.mRequirePower = downloadOptions.mRequirePower;
            this.mRequireCharging = downloadOptions.mRequireCharging;
            this.mRequireDeviceStorage = downloadOptions.mRequireDeviceStorage;
            this.mManual = downloadOptions.mManual;
            return this;
        }

        public DownloadOptions build() {
            return new DownloadOptions(this);
        }
    }
}
