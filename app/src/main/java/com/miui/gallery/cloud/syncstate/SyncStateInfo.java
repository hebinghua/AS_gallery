package com.miui.gallery.cloud.syncstate;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.InterruptedExceptionWrapper;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.control.BatteryMonitor;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ExpectedException;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.deviceprovider.UploadStatusController;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/* loaded from: classes.dex */
public class SyncStateInfo {
    public long mCloudSpaceRemainingSize;
    public long mCloudSpaceTotalSize;
    public long mDirtySize;
    public volatile boolean mInited;
    public volatile boolean mIsBatteryLow;
    public volatile boolean mIsCloudSpaceFull;
    public volatile boolean mIsDirtyChanged;
    public volatile boolean mIsLocalSpaceFull;
    public volatile boolean mIsMediaChanged;
    public volatile boolean mIsSyncedChanged;
    public Runnable mRefreshTimeRunnable;
    public AsyncTask<Context, Void, SyncStatus> mUpdateTask;
    public int mImageSyncedCount = -1;
    public int mVideoSyncedCount = -1;
    public long mResumeTime = -1;
    public final Object mLock = new Object();
    public Runnable mUpdateRunnable = new Runnable() { // from class: com.miui.gallery.cloud.syncstate.SyncStateInfo.2
        @Override // java.lang.Runnable
        public void run() {
            SyncStateInfo.this.updateSyncStatus(GalleryApp.sGetAndroidContext());
        }
    };
    public SyncType mSyncType = SyncType.UNKNOW;
    public SyncStatus mSyncStatus = SyncStatus.UNAVAILABLE;
    public long mHandledReason = 0;
    public long mSyncReason = 0;
    public final List<OnSyncStateChangeObserver> mObservable = new LinkedList();
    public DirtyCount mDirtyCount = new DirtyCount();
    public final List<ExpectedException> mExpectedExceptions = new LinkedList();
    public final List<Exception> mUnexpectedExceptions = new LinkedList();

    public SyncType getSyncType() {
        return this.mSyncType;
    }

    public SyncStatus getSyncStatus() {
        return this.mSyncStatus;
    }

    public long getSyncReason() {
        return this.mSyncReason;
    }

    public boolean containsReason(long j) {
        long j2 = this.mSyncReason;
        return (j | j2) == j2;
    }

    public int[] getDirtyCount() {
        return new int[]{this.mDirtyCount.getTotalImageCount(), this.mDirtyCount.getTotalVideoCount()};
    }

    public long getDirtySize() {
        return this.mDirtySize;
    }

    public int[] getSyncedCount() {
        return new int[]{this.mImageSyncedCount, this.mVideoSyncedCount};
    }

    public long getCloudSpaceTotalSize() {
        return this.mCloudSpaceTotalSize;
    }

    public long getCloudSpaceRemainingSize() {
        return this.mCloudSpaceRemainingSize;
    }

    public long getResumeInterval() {
        return Math.max(0L, this.mResumeTime - System.currentTimeMillis());
    }

    public List<Exception> getExpectedExceptions() {
        return (List) this.mExpectedExceptions.stream().filter(SyncStateInfo$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList());
    }

    public void handleException(Exception exc) {
        if (exc instanceof ExpectedException) {
            this.mExpectedExceptions.add((ExpectedException) exc);
        } else if (exc instanceof InterruptedExceptionWrapper) {
            handleException(((InterruptedExceptionWrapper) exc).get());
        } else {
            this.mUnexpectedExceptions.add(exc);
        }
    }

    public void setSyncType(SyncType syncType) {
        synchronized (this.mLock) {
            SyncType syncType2 = this.mSyncType;
            if (syncType2 != syncType) {
                DefaultLogger.d("SyncStateInfo", "setSyncType old: %s, new: %s", syncType2, syncType);
                this.mSyncType = syncType;
                invalidate();
            }
        }
    }

    public void setSyncReason(long j) {
        synchronized (this.mLock) {
            long j2 = this.mSyncReason;
            if (j2 != j) {
                DefaultLogger.d("SyncStateInfo", "setSyncReason old: %s, new: %s", Long.toBinaryString(j2), Long.toBinaryString(j));
                this.mSyncReason = j;
            }
        }
    }

    public void setIsBatteryLow(boolean z) {
        if (this.mIsBatteryLow != z) {
            invalidate();
        }
        this.mIsBatteryLow = z;
    }

    public void setIsLocalSpaceFull(boolean z) {
        if (this.mIsLocalSpaceFull != z) {
            invalidate();
        }
        this.mIsLocalSpaceFull = z;
    }

    public void triggerMediaChanged() {
        this.mIsMediaChanged = true;
        invalidate();
    }

    public void onSyncCommandDispatched() {
        invalidate();
    }

    public void registerObserver(OnSyncStateChangeObserver onSyncStateChangeObserver) {
        if (onSyncStateChangeObserver == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized (this.mObservable) {
            if (this.mObservable.contains(onSyncStateChangeObserver)) {
                throw new IllegalStateException("Observer " + onSyncStateChangeObserver + " is already registered.");
            }
            this.mObservable.add(onSyncStateChangeObserver);
        }
        if (this.mSyncStatus == SyncStatus.UNAVAILABLE) {
            return;
        }
        onSyncStateChangeObserver.onSyncStateChanged(this);
    }

    public void unregisterObserver(OnSyncStateChangeObserver onSyncStateChangeObserver) {
        if (onSyncStateChangeObserver == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized (this.mObservable) {
            int indexOf = this.mObservable.indexOf(onSyncStateChangeObserver);
            if (indexOf == -1) {
                throw new IllegalStateException("Observer " + onSyncStateChangeObserver + " was not registered.");
            }
            this.mObservable.remove(indexOf);
        }
    }

    public final void notifyObservers() {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.syncstate.SyncStateInfo.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (SyncStateInfo.this.mObservable) {
                    for (OnSyncStateChangeObserver onSyncStateChangeObserver : SyncStateInfo.this.mObservable) {
                        onSyncStateChangeObserver.onSyncStateChanged(SyncStateInfo.this);
                    }
                }
            }
        });
    }

    public void invalidate() {
        if (ThreadManager.getMainHandler().hasCallbacksCompat(this.mUpdateRunnable)) {
            DefaultLogger.d("SyncStateInfo", "has pending runnable, ignore");
        } else {
            ThreadManager.getMainHandler().postDelayed(this.mUpdateRunnable, 300L);
        }
    }

    public final void updateSyncStatus(Context context) {
        AsyncTask<Context, Void, SyncStatus> asyncTask = this.mUpdateTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        AsyncTask<Context, Void, SyncStatus> asyncTask2 = new AsyncTask<Context, Void, SyncStatus>() { // from class: com.miui.gallery.cloud.syncstate.SyncStateInfo.3
            @Override // android.os.AsyncTask
            public SyncStatus doInBackground(Context... contextArr) {
                Context context2 = contextArr[0];
                SyncStatus updateSyncStatusInternal = SyncStateInfo.this.updateSyncStatusInternal(context2);
                if (needTriggerSync(context2, updateSyncStatusInternal)) {
                    SyncStateInfo.this.mExpectedExceptions.clear();
                    SyncStateInfo.this.mUnexpectedExceptions.clear();
                    SyncStateInfo.this.triggerSync(context2);
                } else if (needTriggerCardSync(context2, updateSyncStatusInternal)) {
                    SyncStateInfo.this.triggerCardSync(context2);
                }
                return updateSyncStatusInternal;
            }

            public final boolean needNotifyObservers(SyncStatus syncStatus) {
                return SyncStateInfo.this.mSyncStatus != syncStatus || SyncStateInfo.this.mSyncStatus == SyncStatus.SYNC_PAUSE || SyncStateInfo.this.mIsDirtyChanged || SyncStateInfo.this.mIsSyncedChanged;
            }

            public final boolean isOuterConditionLimit(SyncStatus syncStatus) {
                return syncStatus == SyncStatus.DISCONNECTED || syncStatus == SyncStatus.NO_WIFI || syncStatus == SyncStatus.BATTERY_LOW || syncStatus == SyncStatus.SYSTEM_SPACE_LOW || syncStatus == SyncStatus.CLOUD_SPACE_FULL || syncStatus == SyncStatus.SYNC_PAUSE;
            }

            public final boolean needTriggerSync(Context context2, SyncStatus syncStatus) {
                if (syncStatus != SyncStatus.UNKNOWN_ERROR || !isOuterConditionLimit(SyncStateInfo.this.mSyncStatus)) {
                    return false;
                }
                DefaultLogger.d("SyncStateInfo", "condition -> ok, trigger sync");
                return true;
            }

            public final boolean needTriggerCardSync(Context context2, SyncStatus syncStatus) {
                if (syncStatus != SyncStatus.SYNCED || !GalleryPreferences.Assistant.isCardSyncDirty()) {
                    return false;
                }
                DefaultLogger.d("SyncStateInfo", "need Trigger Card Sync");
                return true;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(SyncStatus syncStatus) {
                DefaultLogger.i("SyncStateInfo", "update status old: %s, new: %s, syncType: %s, mIsDirtyChanged: %s, mIsSyncedChanged: %s", SyncStateInfo.this.mSyncStatus, syncStatus, SyncStateInfo.this.mSyncType, Boolean.valueOf(SyncStateInfo.this.mIsDirtyChanged), Boolean.valueOf(SyncStateInfo.this.mIsSyncedChanged));
                if (needNotifyObservers(syncStatus)) {
                    SyncStateInfo.this.mSyncStatus = syncStatus;
                    SyncStateInfo.this.mIsDirtyChanged = false;
                    SyncStateInfo.this.mIsSyncedChanged = false;
                    SyncStateInfo.this.notifyObservers();
                }
            }
        };
        this.mUpdateTask = asyncTask2;
        asyncTask2.execute(context.getApplicationContext());
    }

    public final void init(Context context) {
        boolean z = true;
        if (this.mInited) {
            synchronized (this.mObservable) {
                if (this.mObservable.size() > 0) {
                    return;
                }
            }
        } else {
            this.mInited = true;
        }
        this.mIsCloudSpaceFull = SpaceFullHandler.isOwnerSpaceFull();
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW"));
        this.mIsLocalSpaceFull = registerReceiver != null ? "android.intent.action.DEVICE_STORAGE_LOW".equals(registerReceiver.getAction()) : false;
        Intent registerReceiver2 = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver2 == null) {
            z = GalleryPreferences.Sync.getPowerCanSync();
        } else if (BatteryMonitor.isPowerCanSync(context, registerReceiver2)) {
            z = false;
        }
        this.mIsBatteryLow = z;
    }

    public final SyncStatus updateSyncStatusInternal(Context context) {
        boolean z = this.mIsMediaChanged;
        this.mIsMediaChanged = false;
        init(context);
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            return SyncStatus.CTA_NOT_ALLOW;
        }
        Account account = AccountCache.getAccount();
        if (account == null) {
            return SyncStatus.NO_ACCOUNT;
        }
        if (!SyncStateUtil.isMasterSyncAutomatically()) {
            return SyncStatus.MASTER_SYNC_OFF;
        }
        if (!SyncStateUtil.isSyncAutomatically()) {
            return SyncStatus.SYNC_OFF;
        }
        Boolean bool = (Boolean) ScanCache.getInstance().get("key_mi_mover_event_start");
        if (bool != null && bool.booleanValue()) {
            return SyncStatus.MI_MOVER_RUNNING;
        }
        long resumeTime = SyncUtil.getResumeTime(context);
        if (resumeTime > System.currentTimeMillis()) {
            DefaultLogger.d("SyncStateInfo", "start refresh time");
            this.mResumeTime = resumeTime;
            startRefreshTime();
            if (z) {
                updateDirtyCount(context);
            }
            if (this.mDirtyCount.getSyncableCount() > 0) {
                return SyncStatus.SYNC_PAUSE;
            }
        } else {
            DefaultLogger.d("SyncStateInfo", "stop refresh time");
            this.mResumeTime = -1L;
            stopRefreshTime();
        }
        if (!Preference.sIsFirstSynced()) {
            if (checkIsSyncingMetaData(account)) {
                updateSyncedCount(context);
                return SyncStatus.SYNCING_METADATA;
            } else if (this.mIsLocalSpaceFull) {
                return SyncStatus.SYSTEM_SPACE_LOW;
            } else {
                if (checkIsPendingMetaData(account) && isMeetPullCondition()) {
                    if (this.mImageSyncedCount == -1 && this.mVideoSyncedCount == -1) {
                        updateSyncedCount(context);
                    }
                    return SyncStatus.SYNC_META_PENDING;
                } else if (!BaseNetworkUtils.isNetworkConnected()) {
                    return SyncStatus.DISCONNECTED;
                } else {
                    if (this.mIsBatteryLow && !this.mSyncType.isForce()) {
                        return SyncStatus.BATTERY_LOW;
                    }
                }
            }
        } else {
            if (z) {
                updateDirtyCount(context);
            }
            if (this.mDirtyCount.getSyncableCount() == 0) {
                if (this.mDirtyCount.getOversizedCount() > 0) {
                    return SyncStatus.SYNCED_WITH_OVERSIZED_FILE;
                }
                SyncStatus syncStatus = this.mSyncStatus;
                SyncStatus syncStatus2 = SyncStatus.SYNCED;
                if (syncStatus != syncStatus2 || z) {
                    updateSyncedCount(context);
                }
                return GalleryPreferences.Sync.getSlimTextLinkShouldShow().booleanValue() ? SyncStatus.SYSTEM_SPACE_LOW : syncStatus2;
            } else if (checkIsSyncing(account) && isMeetPushCondition()) {
                return SyncStatus.SYNCING;
            } else {
                if (this.mIsLocalSpaceFull) {
                    return SyncStatus.SYSTEM_SPACE_LOW;
                }
                if (checkIsPendingUpload(account) && isMeetPushCondition()) {
                    return SyncStatus.SYNC_PENDING;
                }
                if (!BaseNetworkUtils.isNetworkConnected()) {
                    return SyncStatus.DISCONNECTED;
                }
                if (SpaceFullHandler.isOwnerSpaceFull()) {
                    SyncStateUtil.CloudSpaceInfo cloudSpaceInfo = SyncStateUtil.getCloudSpaceInfo(context);
                    long total = cloudSpaceInfo.getTotal();
                    this.mCloudSpaceTotalSize = total;
                    this.mCloudSpaceRemainingSize = total - cloudSpaceInfo.getUsed();
                    return SyncStatus.CLOUD_SPACE_FULL;
                }
                if (BaseNetworkUtils.isActiveNetworkMetered()) {
                    if (this.mSyncType == SyncType.GPRS_FORCE) {
                        return SyncStatus.SYNC_ERROR;
                    }
                    if (!GalleryPreferences.Sync.getBackupOnlyInWifi()) {
                        if (this.mSyncStatus == SyncStatus.NO_WIFI) {
                            triggerSync(context);
                        }
                    } else {
                        return SyncStatus.NO_WIFI;
                    }
                }
                if (this.mIsBatteryLow) {
                    if (this.mSyncType.isForce()) {
                        return SyncStatus.SYNC_ERROR;
                    }
                    return SyncStatus.BATTERY_LOW;
                }
            }
        }
        if (this.mExpectedExceptions.size() > 0) {
            return SyncStatus.EXCEPTED_ERROR;
        }
        return SyncStatus.UNKNOWN_ERROR;
    }

    public final void triggerSync(Context context) {
        DefaultLogger.d("SyncStateInfo", "triggerSync");
        SyncUtil.requestSync(context, new SyncRequest.Builder().setSyncType(this.mSyncType).setSyncReason(Long.MAX_VALUE).setDelayUpload(false).build());
    }

    public final void triggerCardSync(Context context) {
        DefaultLogger.d("SyncStateInfo", "triggerCardSync");
        GalleryPreferences.Assistant.setCardSyncDirty(false);
        SyncUtil.requestSync(context, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(68L).build());
        SamplingStatHelper.recordCountEvent("assistant", "request_sync_card_syncstate");
    }

    public final void updateDirtyCount(Context context) {
        DirtyCount dirtyCount = SyncStateUtil.getDirtyCount(context);
        if (!Objects.equals(dirtyCount, this.mDirtyCount)) {
            this.mDirtyCount = dirtyCount;
            this.mIsDirtyChanged = true;
            updateDirtySize(context);
        }
    }

    public final void updateDirtySize(Context context) {
        long[] dirtySize = SyncStateUtil.getDirtySize(context);
        this.mDirtySize = dirtySize[0] + dirtySize[1];
    }

    public final void updateSyncedCount(Context context) {
        int[] syncedCount = SyncStateUtil.getSyncedCount(context);
        int i = syncedCount[0];
        int i2 = syncedCount[1];
        if (i == this.mImageSyncedCount && i2 == this.mVideoSyncedCount) {
            return;
        }
        this.mIsSyncedChanged = true;
        this.mImageSyncedCount = i;
        this.mVideoSyncedCount = i2;
    }

    public final boolean checkIsPendingMetaData(Account account) {
        return SyncStateUtil.isSyncPending();
    }

    public final boolean checkIsSyncingMetaData(Account account) {
        return SyncStateUtil.isSyncActive() && containsReason(1L);
    }

    public final boolean checkIsSyncing(Account account) {
        return (SyncStateUtil.isSyncActive() && (containsReason(32L) || containsReason(512L))) || SyncStateUtil.isUploading();
    }

    public final boolean checkIsPendingUpload(Account account) {
        return checkIsPendingMetaData(account) || UploadStatusController.getInstance().isPending();
    }

    public final boolean isMeetPullCondition() {
        return SyncConditionManager.checkIgnoreCancel(1, SyncUtil.wrapSyncType(this.mSyncType)) == 0;
    }

    public final boolean isMeetPushCondition() {
        return SyncConditionManager.checkIgnoreCancel(4, SyncUtil.wrapSyncType(this.mSyncType)) == 0;
    }

    public final void doRefreshTime() {
        invalidate();
    }

    public final void startRefreshTime() {
        if (this.mRefreshTimeRunnable == null) {
            this.mRefreshTimeRunnable = new Runnable() { // from class: com.miui.gallery.cloud.syncstate.SyncStateInfo.4
                @Override // java.lang.Runnable
                public void run() {
                    SyncStateInfo.this.doRefreshTime();
                }
            };
        }
        stopRefreshTime();
        synchronized (this.mObservable) {
            if (this.mObservable.size() > 0) {
                ThreadManager.getMainHandler().postDelayed(this.mRefreshTimeRunnable, 60000L);
            }
        }
    }

    public final void stopRefreshTime() {
        if (this.mRefreshTimeRunnable != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mRefreshTimeRunnable);
        }
    }
}
