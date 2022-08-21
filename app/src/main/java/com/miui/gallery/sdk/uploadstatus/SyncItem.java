package com.miui.gallery.sdk.uploadstatus;

import com.miui.gallery.sdk.SyncStatus;

/* loaded from: classes2.dex */
public abstract class SyncItem {
    public final SyncStatus mStatus;

    public SyncItem(SyncStatus syncStatus) {
        this.mStatus = syncStatus;
    }
}
