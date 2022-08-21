package com.miui.gallery.util;

import android.content.SyncStatusObserver;

/* loaded from: classes2.dex */
public class SyncStatusObserverWrapper implements SyncStatusObserver {
    public SyncStatusObserver mWorker;

    public void setSyncStatusObserverWorker(SyncStatusObserver syncStatusObserver) {
        this.mWorker = syncStatusObserver;
    }

    @Override // android.content.SyncStatusObserver
    public void onStatusChanged(int i) {
        SyncStatusObserver syncStatusObserver = this.mWorker;
        if (syncStatusObserver != null) {
            syncStatusObserver.onStatusChanged(i);
        }
    }
}
