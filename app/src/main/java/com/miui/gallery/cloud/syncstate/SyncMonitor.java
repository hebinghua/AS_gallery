package com.miui.gallery.cloud.syncstate;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.control.UploadInfo;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.b;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public final class SyncMonitor {
    public AtomicInteger isSyncStarted;
    public Context mContext;
    public Worker mWorker;

    public SyncMonitor() {
        this.isSyncStarted = new AtomicInteger();
        this.mContext = StaticContext.sGetAndroidContext();
        this.mWorker = new Worker(this.mContext);
        monitorProcessLifecycle();
        monitorNetworkingAccepted();
    }

    /* loaded from: classes.dex */
    public static final class Singleton {
        public static final SyncMonitor INSTANCE = new SyncMonitor();
    }

    public static final SyncMonitor getInstance() {
        return Singleton.INSTANCE;
    }

    public final boolean isEnable() {
        return CloudControlStrategyHelper.getSyncStrategy().isMonitorEnable();
    }

    public void onSyncStart(SyncType syncType, long j) {
        if (!isEnable()) {
            return;
        }
        if (this.isSyncStarted.incrementAndGet() == 1) {
            DefaultLogger.d("SyncMonitor", "monitor sync start, syncType %s, reason %s", syncType, Long.toBinaryString(j));
            this.mWorker.onSyncStart(syncType, j);
            return;
        }
        DefaultLogger.d("SyncMonitor", "sync has started, append syncType %s, reason %s", syncType, Long.toBinaryString(j));
        this.mWorker.onSyncStartAppend(syncType, j);
    }

    public void onSyncEnd() {
        if (!isEnable()) {
            return;
        }
        if (this.isSyncStarted.decrementAndGet() == 0) {
            DefaultLogger.d("SyncMonitor", "monitor sync stop");
            this.mWorker.onSyncEnd();
            return;
        }
        DefaultLogger.d("SyncMonitor", "sync hasn't stopped");
    }

    public void onHandleReason(long j) {
        if (!isEnable()) {
            return;
        }
        DefaultLogger.d("SyncMonitor", "monitor handle reason %s", Long.toBinaryString(j));
        this.mWorker.onHandleReason(j);
    }

    public void onUpload(UploadInfo uploadInfo) {
        if (!isEnable()) {
            return;
        }
        this.mWorker.onUpload(uploadInfo);
    }

    public void onSyncThrowable(Throwable th) {
        if (!isEnable()) {
            return;
        }
        this.mWorker.onSyncThrowable(th);
    }

    public void onNetworkingAcceptedToggled(boolean z) {
        if (!isEnable()) {
            return;
        }
        this.mWorker.onNetworkingAcceptedToggled(z);
    }

    public void onProcessLifecycleToggled(boolean z) {
        if (!isEnable()) {
            return;
        }
        this.mWorker.onProcessLifecycleToggled(z);
    }

    public final void monitorProcessLifecycle() {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.1
            @Override // java.lang.Runnable
            public void run() {
                ProcessLifecycleOwner.get().getLifecycle().addObserver(new DefaultLifecycleObserver() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.1.1
                    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
                    public void onStart(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner == ProcessLifecycleOwner.get()) {
                            SyncMonitor.this.onProcessLifecycleToggled(true);
                        }
                    }

                    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
                    public void onStop(LifecycleOwner lifecycleOwner) {
                        if (lifecycleOwner == ProcessLifecycleOwner.get()) {
                            SyncMonitor.this.onProcessLifecycleToggled(false);
                        }
                    }
                });
            }
        });
    }

    public final void monitorNetworkingAccepted() {
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(new BroadcastReceiver() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                SyncMonitor.this.onNetworkingAcceptedToggled(AgreementsUtils.isNetworkingAgreementAccepted());
            }
        }, new IntentFilter("com.miui.gallery.action.CTA_CHANGED"));
    }

    /* loaded from: classes.dex */
    public static final class Worker implements Handler.Callback {
        public static int TRAFFIC_STEP_INIT = 1;
        public final Object LOCK;
        public boolean isFirstBackground;
        public long mBackgroundStartTime;
        public Context mContext;
        public Handler mHandler;
        public long mLastCheckTime;
        public long mSyncStartTime;
        public Throwable mSyncThrowable;
        public int mTrafficSteps;
        public UploadInfoAnalyzer mUploadInfoAnalyzer;

        public static String messageDesc(int i) {
            switch (i) {
                case 1:
                    return "RECORD_SYNC_START";
                case 2:
                    return "RECORD_UPLOAD_INFO";
                case 3:
                    return "RECORD_THROWABLE";
                case 4:
                    return "RECORD_SYNC_STOP";
                case 5:
                    return "RECORD_APP_FOREGROUND";
                case 6:
                    return "RECORD_APP_BACKGROUND";
                case 7:
                    return "RECORD_NETWORKING_ACCEPTED";
                case 8:
                    return "RECORD_NETWORKING_DENIED";
                case 9:
                    return "MSG_ON_HANDLE_REASON";
                case 10:
                    return "MSG_ON_SYNC_START_APPEND";
                default:
                    return "UNKNOWN";
            }
        }

        public final void handleAppBackground() {
        }

        public final void handleNetworkingDenied() {
        }

        public Worker(Context context) {
            this.LOCK = new Object();
            this.mTrafficSteps = TRAFFIC_STEP_INIT;
            this.isFirstBackground = true;
            this.mContext = context;
        }

        public final Handler getHandler() {
            Handler handler;
            synchronized (this.LOCK) {
                if (this.mHandler == null) {
                    HandlerThread handlerThread = new HandlerThread("sync_monitor", 10);
                    handlerThread.start();
                    this.mHandler = new Handler(handlerThread.getLooper(), this);
                }
                handler = this.mHandler;
            }
            return handler;
        }

        /* loaded from: classes.dex */
        public static class SyncInfo {
            public final long reason;
            public final SyncType type;

            public SyncInfo(SyncType syncType, long j) {
                this.type = syncType;
                this.reason = j;
            }
        }

        public void onSyncStart(SyncType syncType, long j) {
            getHandler().obtainMessage(1, new SyncInfo(syncType, j)).sendToTarget();
        }

        public void onSyncEnd() {
            getHandler().obtainMessage(4).sendToTarget();
        }

        public void onHandleReason(long j) {
            getHandler().obtainMessage(9, Long.valueOf(j)).sendToTarget();
        }

        public void onSyncStartAppend(SyncType syncType, long j) {
            getHandler().obtainMessage(10, new SyncInfo(syncType, j)).sendToTarget();
        }

        public void onUpload(UploadInfo uploadInfo) {
            getHandler().obtainMessage(2, uploadInfo).sendToTarget();
        }

        public void onSyncThrowable(Throwable th) {
            getHandler().obtainMessage(3, th).sendToTarget();
        }

        public void onNetworkingAcceptedToggled(boolean z) {
            if (z) {
                getHandler().obtainMessage(7).sendToTarget();
            } else {
                getHandler().obtainMessage(8).sendToTarget();
            }
        }

        public void onProcessLifecycleToggled(boolean z) {
            if (z) {
                getHandler().obtainMessage(5).sendToTarget();
            } else {
                getHandler().obtainMessage(6).sendToTarget();
            }
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    handleSyncStart((SyncInfo) message.obj);
                    break;
                case 2:
                    handleUploadInfo((UploadInfo) message.obj);
                    break;
                case 3:
                    handleSyncThrowable((Throwable) message.obj);
                    break;
                case 4:
                    handleSyncStop();
                    break;
                case 5:
                    handleAppForeground();
                    break;
                case 6:
                    handleAppBackground();
                    break;
                case 7:
                    handleNetworkingAccepted();
                    break;
                case 8:
                    handleNetworkingDenied();
                    break;
                case 10:
                    handleSyncStartAppend((SyncInfo) message.obj);
                    break;
            }
            DefaultLogger.d("SyncMonitor", messageDesc(message.what));
            return true;
        }

        public final void handleSyncStart(SyncInfo syncInfo) {
            this.mSyncThrowable = null;
            this.mSyncStartTime = System.currentTimeMillis();
            statSyncStart();
            SyncStateManager.getInstance().setSyncType(syncInfo.type, false);
            SyncStateManager.getInstance().mergeReason(syncInfo.reason);
        }

        public final void handleSyncStartAppend(SyncInfo syncInfo) {
            SyncStateManager.getInstance().setSyncType(syncInfo.type, false);
            SyncStateManager.getInstance().mergeReason(syncInfo.reason);
        }

        public final void handleUploadInfo(UploadInfo uploadInfo) {
            getAnalyzer().addUploadInfo(uploadInfo);
            tryCheck();
        }

        public final void handleSyncThrowable(Throwable th) {
            this.mSyncThrowable = th;
        }

        public final void handleSyncStop() {
            DefaultLogger.d("SyncMonitor", "sync time %s", Long.valueOf(System.currentTimeMillis() - this.mSyncStartTime));
            this.mLastCheckTime = 0L;
            this.mSyncStartTime = 0L;
            this.mTrafficSteps = TRAFFIC_STEP_INIT;
            this.mBackgroundStartTime = 0L;
            this.isFirstBackground = true;
            UploadInfoAnalyzer uploadInfoAnalyzer = this.mUploadInfoAnalyzer;
            if (uploadInfoAnalyzer != null) {
                uploadInfoAnalyzer.clear();
            }
            SyncStateManager.getInstance().setSyncType(SyncType.UNKNOW, true);
            SyncStateManager.getInstance().unmergeReason(Long.MAX_VALUE);
        }

        public final void handleAppForeground() {
            int activePullTimes;
            Account account = AccountCache.getAccount();
            if (account == null || !SyncUtil.isGalleryCloudSyncable(this.mContext)) {
                return;
            }
            boolean isSyncActive = SyncStateUtil.isSyncActive();
            boolean isUploading = SyncStateUtil.isUploading();
            boolean isSyncPending = SyncStateUtil.isSyncPending();
            long j = Long.MAX_VALUE;
            if (SyncStateUtil.getDirtyCount(this.mContext).getSyncableCount() <= 0) {
                if (!isSyncActive && !isSyncPending) {
                    if (!Preference.sIsFirstSynced()) {
                        DefaultLogger.d("SyncMonitor", "Hasn't synced first, need request");
                    } else {
                        long lastSyncTimestamp = GalleryPreferences.Sync.getLastSyncTimestamp();
                        long currentTimeMillis = System.currentTimeMillis() - lastSyncTimestamp;
                        if (currentTimeMillis < 0 || currentTimeMillis > CloudControlStrategyHelper.getSyncStrategy().getIntervalForActivePull()) {
                            DefaultLogger.d("SyncMonitor", "pull actively");
                            j = 1;
                            if (lastSyncTimestamp > 0 && !GalleryDateUtils.isSameDate(new Date(System.currentTimeMillis()), new Date(lastSyncTimestamp)) && (activePullTimes = GalleryPreferences.Sync.getActivePullTimes()) > 0) {
                                statActivePullTimes(activePullTimes);
                                GalleryPreferences.Sync.clearActivePullTimes();
                            }
                            GalleryPreferences.Sync.increaseActivePullTimes();
                        }
                    }
                }
                j = 0;
            } else if (isSyncActive || isUploading) {
                if (isUploading) {
                    SyncType syncType = SyncStateManager.getInstance().getSyncType();
                    long syncReason = SyncStateManager.getInstance().getSyncReason();
                    SyncStateManager.getInstance().setSyncType(SyncType.NORMAL, false);
                    DefaultLogger.d("SyncMonitor", "uploading: swap sync type, cur type[%s], reason[%s]", syncType, Long.toBinaryString(syncReason));
                    if (isSyncPending) {
                        DefaultLogger.d("SyncMonitor", "uploading, has pending sync, need request");
                        j = 33;
                    }
                    j = 0;
                } else if (SyncStateManager.getInstance().containsReason(32L)) {
                    SyncType syncType2 = SyncStateManager.getInstance().getSyncType();
                    long syncReason2 = SyncStateManager.getInstance().getSyncReason();
                    SyncStateManager.getInstance().setSyncType(SyncType.NORMAL, false);
                    DefaultLogger.d("SyncMonitor", "sync active: swap sync type, cur type[%s], reason[%s]", syncType2, Long.toBinaryString(syncReason2));
                    j = 0;
                } else {
                    DefaultLogger.d("SyncMonitor", "sync active, won't upload, need request");
                    j = 33;
                }
            } else if (isSyncPending) {
                ContentResolver.cancelSync(account, "com.miui.gallery.cloud.provider");
                DefaultLogger.d("SyncMonitor", "sync pending: cancel existing, request again.");
            } else {
                DefaultLogger.d("SyncMonitor", "no request: request again.");
            }
            if (j == 0) {
                return;
            }
            SyncUtil.requestSync(this.mContext, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(j).setDelayUpload(false).build());
            DefaultLogger.d("SyncMonitor", "app focused, request sync with reason[%s]", Long.toBinaryString(j));
        }

        public final void handleNetworkingAccepted() {
            if (Preference.sIsFirstSynced() || !SyncUtil.isGalleryCloudSyncable(this.mContext)) {
                return;
            }
            DefaultLogger.d("SyncMonitor", "request sync after networking accepted");
            SyncUtil.requestSync(this.mContext, new SyncRequest.Builder().setSyncType(SyncType.POWER_FORCE).setSyncReason(Long.MAX_VALUE).build());
        }

        public final UploadInfoAnalyzer getAnalyzer() {
            if (this.mUploadInfoAnalyzer == null) {
                this.mUploadInfoAnalyzer = new UploadInfoAnalyzer();
            }
            return this.mUploadInfoAnalyzer;
        }

        public final void tryCheck() {
            if (System.currentTimeMillis() - this.mLastCheckTime < getCheckInterval()) {
                return;
            }
            this.mLastCheckTime = System.currentTimeMillis();
            check();
        }

        public static long getCheckInterval() {
            return CloudControlStrategyHelper.getSyncStrategy().getMonitorCheckInterval();
        }

        public static long getTrafficStepValue() {
            return CloudControlStrategyHelper.getSyncStrategy().getMonitorTrafficStep();
        }

        public static long getBackgroundBufferTime() {
            return CloudControlStrategyHelper.getSyncStrategy().getMonitorBackBufferTime();
        }

        public static long getBackgroundUpperLimitTime() {
            return CloudControlStrategyHelper.getSyncStrategy().getMonitorBackUpperTime();
        }

        public static long getSyncUpperLimitTime() {
            return CloudControlStrategyHelper.getSyncStrategy().getMonitorSyncUpperTime();
        }

        public static boolean needMonitorTraffic() {
            return CloudControlStrategyHelper.getSyncStrategy().isMonitorTraffic();
        }

        public static boolean needMonitorBackground() {
            return CloudControlStrategyHelper.getSyncStrategy().isMonitorBackground();
        }

        public static boolean needMonitorSyncTime() {
            return CloudControlStrategyHelper.getSyncStrategy().isMonitorSyncTime();
        }

        public static boolean isCharging(Context context) {
            long currentTimeMillis = System.currentTimeMillis();
            int intExtra = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("status", -1);
            DefaultLogger.d("SyncMonitor", "judge charging state cost %s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return intExtra == 2 || intExtra == 5;
        }

        public final void check() {
            DefaultLogger.d("SyncMonitor", "check");
            SyncType syncType = SyncStateManager.getInstance().getSyncType();
            if (syncType != null && syncType.isForce()) {
                DefaultLogger.d("SyncMonitor", "sync type %s, ignore monitor", syncType);
                return;
            }
            if (needMonitorTraffic() && BaseNetworkUtils.isActiveNetworkMetered()) {
                long uploadTraffic = getAnalyzer().getUploadTraffic();
                if (uploadTraffic > this.mTrafficSteps * getTrafficStepValue()) {
                    DefaultLogger.w("SyncMonitor", "upload traffic %s", Long.valueOf(uploadTraffic));
                    this.mTrafficSteps++;
                }
            }
            if (needMonitorSyncTime()) {
                long syncUpperLimitTime = getSyncUpperLimitTime();
                if (syncUpperLimitTime > 0 && System.currentTimeMillis() - this.mSyncStartTime > syncUpperLimitTime) {
                    DefaultLogger.w("SyncMonitor", "sync time has reached upper limit");
                }
            }
            if (!needMonitorBackground()) {
                return;
            }
            if (!isCharging(this.mContext)) {
                if (MiscUtil.isAppProcessInForeground()) {
                    if (this.mBackgroundStartTime <= 0) {
                        return;
                    }
                    DefaultLogger.d("SyncMonitor", "monitor app focused, reset background start time");
                    this.mBackgroundStartTime = 0L;
                    return;
                }
                if (this.isFirstBackground) {
                    this.isFirstBackground = false;
                    statSyncInBackground();
                }
                DefaultLogger.d("SyncMonitor", "sync type %s, app not in foreground", syncType);
                if (this.mBackgroundStartTime == 0) {
                    DefaultLogger.d("SyncMonitor", "monitor process changes to background");
                    this.mBackgroundStartTime = System.currentTimeMillis();
                    return;
                }
                long backgroundBufferTime = getBackgroundBufferTime();
                if (backgroundBufferTime < 0 || System.currentTimeMillis() - this.mBackgroundStartTime < backgroundBufferTime) {
                    DefaultLogger.d("SyncMonitor", "monitor background in buffer time %s", Long.valueOf(backgroundBufferTime));
                } else if (getBackgroundUpperLimitTime() < 0) {
                    DefaultLogger.d("SyncMonitor", "monitor background in upper limit time %s", Long.valueOf(getBackgroundUpperLimitTime()));
                } else {
                    long[] dirtySize = SyncStateUtil.getDirtySize(this.mContext);
                    long j = dirtySize[0] + dirtySize[1];
                    long min = Math.min(getAnalyzer().getAvgSpeed(), getAnalyzer().getRecentSpeed());
                    if (min > 0) {
                        long j2 = (((float) j) * 1000.0f) / ((float) min);
                        DefaultLogger.d("SyncMonitor", "need upload size %s, speed %s, predicted time %s", Long.valueOf(j), Long.valueOf(min), Long.valueOf(j2));
                        long currentTimeMillis = j2 + (System.currentTimeMillis() - this.mBackgroundStartTime);
                        DefaultLogger.d("SyncMonitor", "predicted upper time %s, limit time %s", Long.valueOf(currentTimeMillis), Long.valueOf(getBackgroundUpperLimitTime()));
                        if (currentTimeMillis < getBackgroundUpperLimitTime()) {
                            DefaultLogger.d("SyncMonitor", "monitor background in upper limit time");
                            return;
                        }
                    }
                    DefaultLogger.w("SyncMonitor", "background sync time has reached upper limit, change sync policy");
                    SyncUtil.stopSync(this.mContext);
                    SyncUtil.requestSync(this.mContext, new SyncRequest.Builder().setSyncType(SyncType.BACKGROUND).setSyncReason(Long.MAX_VALUE).setDelayUpload(false).build());
                    statPolicyChanged();
                }
            } else if (this.mBackgroundStartTime <= 0) {
            } else {
                DefaultLogger.d("SyncMonitor", "monitor charging, reset background start time");
                this.mBackgroundStartTime = 0L;
            }
        }

        public final void statSyncStart() {
            SamplingStatHelper.recordCountEvent("sync_monitor", "monitor_sync_start");
        }

        public final void statSyncInBackground() {
            SamplingStatHelper.recordCountEvent("sync_monitor", "monitor_sync_in_background");
            long[] dirtySize = SyncStateUtil.getDirtySize(this.mContext);
            long j = dirtySize[0] + dirtySize[1];
            long min = Math.min(getAnalyzer().getAvgSpeed(), getAnalyzer().getRecentSpeed());
            if (min > 0) {
                HashMap hashMap = new HashMap();
                hashMap.put(b.j, String.valueOf((((float) j) * 1.0f) / ((float) min)));
                SamplingStatHelper.recordCountEvent("sync_monitor", "monitor_background_predicted_time", hashMap);
            }
        }

        public final void statPolicyChanged() {
            long[] dirtySize = SyncStateUtil.getDirtySize(this.mContext);
            HashMap hashMap = new HashMap();
            hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.valueOf(dirtySize[0] + dirtySize[1]));
            SamplingStatHelper.recordCountEvent("sync_monitor", "monitor_sync_policy_changed_dirty", hashMap);
        }

        public final void statActivePullTimes(int i) {
            HashMap hashMap = new HashMap();
            hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
            SamplingStatHelper.recordCountEvent("sync_monitor", "sync_active_pull_times", hashMap);
        }
    }

    /* loaded from: classes.dex */
    public static class UploadInfoAnalyzer {
        public long mAvgSpeed;
        public long mMaxSpeed;
        public long mRecentSpeed;
        public ReadWriteLock mLock = new ReentrantReadWriteLock();
        public List<UploadInfo> mInfos = new LinkedList();

        /* loaded from: classes.dex */
        public interface Func<T> {
            /* renamed from: doFunc */
            T mo706doFunc();
        }

        public final long calculateSpeed(long j, long j2) {
            return (((float) j) * 1000.0f) / ((float) j2);
        }

        public long getRecentSpeed() {
            return ((Long) safeRun(new Func<Long>() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.Func
                /* renamed from: doFunc */
                public Long mo706doFunc() {
                    return Long.valueOf(UploadInfoAnalyzer.this.mRecentSpeed);
                }
            }, false)).longValue();
        }

        public long getAvgSpeed() {
            return ((Long) safeRun(new Func<Long>() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.Func
                /* renamed from: doFunc */
                public Long mo706doFunc() {
                    return Long.valueOf(UploadInfoAnalyzer.this.mAvgSpeed);
                }
            }, false)).longValue();
        }

        public long getUploadTraffic() {
            return ((Long) safeRun(new Func<Long>() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.Func
                /* renamed from: doFunc */
                public Long mo706doFunc() {
                    long j = 0;
                    for (UploadInfo uploadInfo : UploadInfoAnalyzer.this.mInfos) {
                        j += uploadInfo.size;
                    }
                    return Long.valueOf(j);
                }
            }, false)).longValue();
        }

        public final boolean isValidInfo(UploadInfo uploadInfo) {
            return uploadInfo != null && uploadInfo.size > 0 && uploadInfo.timely > 0;
        }

        public void addUploadInfo(final UploadInfo uploadInfo) {
            if (isValidInfo(uploadInfo)) {
                safeRun(new Func<Void>() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.5
                    @Override // com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.Func
                    /* renamed from: doFunc  reason: collision with other method in class */
                    public Void mo706doFunc() {
                        UploadInfoAnalyzer uploadInfoAnalyzer = UploadInfoAnalyzer.this;
                        UploadInfo uploadInfo2 = uploadInfo;
                        uploadInfoAnalyzer.mRecentSpeed = uploadInfoAnalyzer.calculateSpeed(uploadInfo2.size, uploadInfo2.timely);
                        if (UploadInfoAnalyzer.this.mRecentSpeed > UploadInfoAnalyzer.this.mMaxSpeed) {
                            UploadInfoAnalyzer uploadInfoAnalyzer2 = UploadInfoAnalyzer.this;
                            uploadInfoAnalyzer2.mMaxSpeed = uploadInfoAnalyzer2.mRecentSpeed;
                        }
                        UploadInfoAnalyzer.this.mInfos.add(uploadInfo);
                        long j = 0;
                        long j2 = 0;
                        for (UploadInfo uploadInfo3 : UploadInfoAnalyzer.this.mInfos) {
                            j += uploadInfo3.size;
                            j2 += uploadInfo3.timely;
                        }
                        UploadInfoAnalyzer uploadInfoAnalyzer3 = UploadInfoAnalyzer.this;
                        uploadInfoAnalyzer3.mAvgSpeed = uploadInfoAnalyzer3.calculateSpeed(j, j2);
                        return null;
                    }
                }, true);
            }
        }

        public final void clear() {
            safeRun(new Func<Void>() { // from class: com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.6
                @Override // com.miui.gallery.cloud.syncstate.SyncMonitor.UploadInfoAnalyzer.Func
                /* renamed from: doFunc  reason: collision with other method in class */
                public Void mo706doFunc() {
                    UploadInfoAnalyzer uploadInfoAnalyzer = UploadInfoAnalyzer.this;
                    uploadInfoAnalyzer.mMaxSpeed = uploadInfoAnalyzer.mAvgSpeed = uploadInfoAnalyzer.mRecentSpeed = 0L;
                    UploadInfoAnalyzer.this.mInfos.clear();
                    return null;
                }
            }, true);
        }

        public final void lock(boolean z) {
            (z ? this.mLock.writeLock() : this.mLock.readLock()).lock();
        }

        public final void unlock(boolean z) {
            (z ? this.mLock.writeLock() : this.mLock.readLock()).unlock();
        }

        public <T> T safeRun(Func<T> func, boolean z) {
            lock(z);
            try {
                return func.mo706doFunc();
            } finally {
                unlock(z);
            }
        }
    }
}
