package com.miui.gallery.sdk.download.assist;

import com.miui.gallery.sdk.SyncStatus;

/* loaded from: classes2.dex */
public class DownloadItemStatus {
    public final DownloadedItem mItem;
    public final SyncStatus mSyncStatus;

    public DownloadItemStatus(SyncStatus syncStatus, DownloadedItem downloadedItem) {
        this.mSyncStatus = syncStatus;
        this.mItem = downloadedItem;
    }

    public SyncStatus getStatus() {
        return this.mSyncStatus;
    }

    public String getDownloadedPath() {
        DownloadedItem downloadedItem = this.mItem;
        if (downloadedItem != null) {
            return downloadedItem.getFilePath();
        }
        return null;
    }

    public boolean isDownloading() {
        SyncStatus syncStatus = this.mSyncStatus;
        return syncStatus == SyncStatus.STATUS_INIT || syncStatus == SyncStatus.STATUS_INTERRUPT;
    }
}
