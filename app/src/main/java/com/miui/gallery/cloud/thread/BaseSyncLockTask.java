package com.miui.gallery.cloud.thread;

import com.miui.gallery.cloud.AsyncUpDownloadService;
import com.miui.gallery.cloud.thread.RequestCommandQueue;

/* loaded from: classes.dex */
public abstract class BaseSyncLockTask<T> extends BaseTask<T> {
    public AsyncUpDownloadService.SyncLock mSyncLock;

    public BaseSyncLockTask(int i, int i2, int i3, int i4, RequestCommandQueue.OnItemChangedListener onItemChangedListener) {
        super(i, i2, i3, i4, onItemChangedListener);
    }

    public final void acquireLock() {
        if (this.mSyncLock == null) {
            this.mSyncLock = AsyncUpDownloadService.newSyncLock(this.TAG);
        }
        this.mSyncLock.acquire();
    }

    public final void releaseLock() {
        AsyncUpDownloadService.SyncLock syncLock = this.mSyncLock;
        if (syncLock != null) {
            syncLock.release();
            this.mSyncLock = null;
        }
    }
}
