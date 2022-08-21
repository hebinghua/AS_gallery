package com.miui.gallery.cloud.syncstate;

import android.content.Context;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class SyncStateManager {
    public final SyncStateInfo mSyncStateInfo;
    public SyncStateObserver mSyncStateObserver;

    public SyncStateManager() {
        SyncStateInfo syncStateInfo = new SyncStateInfo();
        this.mSyncStateInfo = syncStateInfo;
        syncStateInfo.invalidate();
        this.mSyncStateObserver = new SyncStateObserver();
    }

    /* loaded from: classes.dex */
    public static class Singleton {
        public static SyncStateManager INSTANCE = new SyncStateManager();
    }

    public static SyncStateManager getInstance() {
        return Singleton.INSTANCE;
    }

    public SyncStateInfo getSyncState() {
        return this.mSyncStateInfo;
    }

    public SyncType getSyncType() {
        SyncType syncType;
        synchronized (this.mSyncStateInfo) {
            syncType = this.mSyncStateInfo.getSyncType();
        }
        return syncType;
    }

    public boolean containsReason(long j) {
        return this.mSyncStateInfo.containsReason(j);
    }

    public void handleException(Exception exc) {
        this.mSyncStateInfo.handleException(exc);
    }

    public void mergeReason(long j) {
        this.mSyncStateInfo.setSyncReason(j | this.mSyncStateInfo.getSyncReason());
    }

    public void unmergeReason(long j) {
        this.mSyncStateInfo.setSyncReason((~j) & this.mSyncStateInfo.getSyncReason());
    }

    public long getSyncReason() {
        return this.mSyncStateInfo.getSyncReason();
    }

    public void setSyncType(SyncType syncType, boolean z) {
        DefaultLogger.i("SyncStateManager", "setSyncType old: %s, new: %s, force: %s", getSyncType(), syncType, Boolean.valueOf(z));
        if (z || syncType.isForce()) {
            this.mSyncStateInfo.setSyncType(syncType);
            return;
        }
        SyncType syncType2 = getSyncType();
        if (syncType2.isForce()) {
            if (SyncConditionManager.checkIgnoreCancel(4, syncType) != 0) {
                return;
            }
            this.mSyncStateInfo.setSyncType(syncType);
        } else if (SyncType.compare(syncType, syncType2) <= 0) {
        } else {
            this.mSyncStateInfo.setSyncType(syncType);
        }
    }

    public void registerSyncStateObserver(Context context, OnSyncStateChangeObserver onSyncStateChangeObserver) {
        this.mSyncStateInfo.registerObserver(onSyncStateChangeObserver);
        this.mSyncStateObserver.register(context);
        updateSyncStatus();
    }

    public void unregisterSyncStateObserver(Context context, OnSyncStateChangeObserver onSyncStateChangeObserver) {
        this.mSyncStateInfo.unregisterObserver(onSyncStateChangeObserver);
        this.mSyncStateObserver.unregister(context);
    }

    public void setIsBatteryLow(boolean z) {
        this.mSyncStateInfo.setIsBatteryLow(z);
    }

    public void setIsLocalSpaceFull(boolean z) {
        this.mSyncStateInfo.setIsLocalSpaceFull(z);
    }

    public void triggerMediaChanged() {
        this.mSyncStateInfo.triggerMediaChanged();
    }

    public void updateSyncStatus() {
        this.mSyncStateInfo.invalidate();
    }

    public void onSyncCommandDispatched() {
        this.mSyncStateInfo.onSyncCommandDispatched();
    }

    public void trackSyncStateChanged(SyncStateInfo syncStateInfo) {
        SyncStatus syncStatus = syncStateInfo.getSyncStatus();
        if (syncStatus == SyncStatus.SYNC_PENDING) {
            TimeMonitor.createNewTimeMonitor("403.12.0.1.13797");
        } else if (syncStatus == SyncStatus.SYNCED) {
            TimeMonitor.trackTimeMonitor("403.12.0.1.13797");
        } else if (syncStatus != SyncStatus.SYNC_PAUSE && syncStatus != SyncStatus.SYNC_ERROR) {
        } else {
            TimeMonitor.cancelTimeMonitor("403.12.0.1.13797");
        }
    }
}
