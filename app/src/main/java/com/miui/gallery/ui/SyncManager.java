package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.cloud.syncstate.OnSyncStateChangeObserver;
import com.miui.gallery.cloud.syncstate.SyncStateInfo;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloud.syncstate.SyncStatus;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.OnAppFocusedListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import miui.cloud.util.DeviceUtils;
import miui.os.Build;

/* loaded from: classes2.dex */
public class SyncManager extends SyncDownloadBaseManager implements OnSyncStateChangeObserver, OnAppFocusedListener {
    public static List<SyncStatus> sInstanceAutoShowStatus;
    public static List<SyncStatus> sPersistentAutoShowStatus;
    public static List<SyncStatus> sProcessAutoShowStatus;
    public static List<SyncStatus> sWarnStatus;
    public SyncStatus mCurSyncStatus;
    public SyncStateInfo mCurrentSyncStateInfo;
    public boolean mIsPermanent;
    public SyncStatusListener mSyncStatusListener;

    /* loaded from: classes2.dex */
    public interface SyncStatusListener {
        void onSyncStatusChange(SyncStateInfo syncStateInfo);
    }

    @Override // com.miui.gallery.util.OnAppFocusedListener
    public void onAppFocused() {
    }

    static {
        LinkedList linkedList = new LinkedList();
        sInstanceAutoShowStatus = linkedList;
        linkedList.add(SyncStatus.SYNCING_METADATA);
        List<SyncStatus> list = sInstanceAutoShowStatus;
        SyncStatus syncStatus = SyncStatus.SYNC_ERROR;
        list.add(syncStatus);
        List<SyncStatus> list2 = sInstanceAutoShowStatus;
        SyncStatus syncStatus2 = SyncStatus.CTA_NOT_ALLOW;
        list2.add(syncStatus2);
        List<SyncStatus> list3 = sInstanceAutoShowStatus;
        SyncStatus syncStatus3 = SyncStatus.CLOUD_SPACE_FULL;
        list3.add(syncStatus3);
        LinkedList linkedList2 = new LinkedList();
        sProcessAutoShowStatus = linkedList2;
        linkedList2.add(SyncStatus.SYNCED_WITH_OVERSIZED_FILE);
        sProcessAutoShowStatus.add(SyncStatus.DISCONNECTED);
        sProcessAutoShowStatus.add(SyncStatus.NO_WIFI);
        sProcessAutoShowStatus.add(SyncStatus.BATTERY_LOW);
        sProcessAutoShowStatus.add(SyncStatus.UNKNOWN_ERROR);
        LinkedList linkedList3 = new LinkedList();
        sPersistentAutoShowStatus = linkedList3;
        linkedList3.add(SyncStatus.NO_ACCOUNT);
        sPersistentAutoShowStatus.add(SyncStatus.MASTER_SYNC_OFF);
        sPersistentAutoShowStatus.add(SyncStatus.SYNC_OFF);
        List<SyncStatus> list4 = sPersistentAutoShowStatus;
        SyncStatus syncStatus4 = SyncStatus.SYSTEM_SPACE_LOW;
        list4.add(syncStatus4);
        sPersistentAutoShowStatus.add(syncStatus3);
        LinkedList linkedList4 = new LinkedList();
        sWarnStatus = linkedList4;
        linkedList4.add(syncStatus);
        sWarnStatus.add(syncStatus2);
        sWarnStatus.add(syncStatus4);
        sWarnStatus.add(syncStatus3);
    }

    public SyncManager(Context context, int i) {
        super(context, i);
    }

    public void setSyncStatusListener(SyncStatusListener syncStatusListener) {
        this.mSyncStatusListener = syncStatusListener;
    }

    public void setCurSyncStatus(SyncStatus syncStatus) {
        this.mCurSyncStatus = syncStatus;
    }

    public void onResume() {
        SyncStateManager.getInstance().registerSyncStateObserver(getContext(), this);
    }

    public void onPause() {
        SyncStateManager.getInstance().unregisterSyncStateObserver(getContext(), this);
    }

    public SyncStatus getCurSyncStatus() {
        return SyncStateManager.getInstance().getSyncState().getSyncStatus();
    }

    public SyncStateInfo getCurrentSyncStateInfo() {
        return this.mCurrentSyncStateInfo;
    }

    public void setIsPermanent(boolean z) {
        this.mIsPermanent = z;
    }

    public boolean isPermanent() {
        return this.mIsPermanent;
    }

    @Override // com.miui.gallery.cloud.syncstate.OnSyncStateChangeObserver
    public void onSyncStateChanged(SyncStateInfo syncStateInfo) {
        SyncStateManager.getInstance().trackSyncStateChanged(syncStateInfo);
        this.mCurrentSyncStateInfo = syncStateInfo;
        SyncStatusListener syncStatusListener = this.mSyncStatusListener;
        if (syncStatusListener != null) {
            syncStatusListener.onSyncStatusChange(syncStateInfo);
        }
        GalleryPreferences.TopBar.saveLastSyncStatus(syncStateInfo.getSyncStatus());
    }

    public boolean hasShowSyncStatusPersistent() {
        for (SyncStatus syncStatus : sPersistentAutoShowStatus) {
            if (GalleryPreferences.TopBar.hasShowedSyncStatusTip(syncStatus)) {
                return true;
            }
        }
        return false;
    }

    public void removeSyncStatusPersistent() {
        for (SyncStatus syncStatus : sPersistentAutoShowStatus) {
            GalleryPreferences.TopBar.setHasShowedSyncStatusTip(syncStatus, false);
        }
    }

    public boolean hasShowSyncStatusPersistent(SyncStatus syncStatus) {
        boolean hasShowedSyncStatusTip = GalleryPreferences.TopBar.hasShowedSyncStatusTip(syncStatus);
        if (!hasShowedSyncStatusTip) {
            GalleryPreferences.TopBar.setHasShowedSyncStatusTip(syncStatus, true);
        }
        return hasShowedSyncStatusTip;
    }

    public boolean canAutoShowSyncBar(SyncStatus syncStatus) {
        if (this.mIsPermanent) {
            return true;
        }
        if (sInstanceAutoShowStatus.contains(syncStatus)) {
            return this.mCurSyncStatus != syncStatus;
        } else if (sProcessAutoShowStatus.contains(syncStatus)) {
            return GalleryPreferences.TopBar.getLastSyncStatus() != syncStatus;
        } else if (!sPersistentAutoShowStatus.contains(syncStatus)) {
            return false;
        } else {
            return !hasShowSyncStatusPersistent(syncStatus);
        }
    }

    public boolean needShowSyncBar(SyncStatus syncStatus) {
        if (DeviceUtils.isRedmiDigitSeries() || Build.IS_INTERNATIONAL_BUILD) {
            return (syncStatus == SyncStatus.NO_ACCOUNT || syncStatus == SyncStatus.MASTER_SYNC_OFF || syncStatus == SyncStatus.SYNC_OFF) ? false : true;
        }
        return true;
    }

    public boolean needShow() {
        return needShowSyncBar(SyncStateManager.getInstance().getSyncState().getSyncStatus());
    }

    public void statSyncBarAutoShowEvent(String str) {
        SamplingStatHelper.recordCountEvent("home_sync_bar", String.format(Locale.US, "sync_bar_show_%s", str));
    }
}
