package com.miui.gallery.cloud;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.common.collect.Lists;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.service.ServiceBase;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class AsyncUpDownloadService extends ServiceBase {
    public ForegroundRef mForegroundRef;
    public long mStartTime;
    public WakeLockRef mWakeLockRef;
    public ResourceRef mWifiLockRef;
    public static final boolean WAKELOCK_ONLY_SCREEN_OFF = SyncConditionManager.sGetSyncConfig().isOnlyScreenOffAcquireWakelock();
    public static final boolean WAKELOCK_ONLY_CHARGING = SyncConditionManager.sGetSyncConfig().isOnlyChargingAcquireWakelock();
    public static final List<String> sPendingRequests = new ArrayList();
    public final BroadcastReceiver mConnReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.cloud.AsyncUpDownloadService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            AsyncUpDownloadService.this.tryCancelTasksWhenConnChanged();
        }
    };
    public AtomicBoolean mDoingCancelTasks = new AtomicBoolean(false);
    public final BroadcastReceiver mSyncReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.cloud.AsyncUpDownloadService.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SyncLogger.d("AsyncUpDownloadService", "request sync in background");
            AsyncUpDownloadService.this.handleRequest(intent);
        }
    };
    public BroadcastReceiver mScreenStateReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.cloud.AsyncUpDownloadService.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                AsyncUpDownloadService.this.handleScreenOnOff(false);
            } else if (!"android.intent.action.SCREEN_ON".equals(action)) {
            } else {
                AsyncUpDownloadService.this.handleScreenOnOff(true);
            }
        }
    };
    public BroadcastReceiver mBatteryStateReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.cloud.AsyncUpDownloadService.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("status", -1);
                AsyncUpDownloadService.this.handleChargeStateChanged(intExtra == 2 || intExtra == 5);
            }
        }
    };

    @Override // com.miui.gallery.service.ServiceBase
    public int getNotificationId() {
        return 6;
    }

    /* loaded from: classes.dex */
    public static abstract class ResourceRef {
        public final List<String> mOwners = Lists.newArrayList();
        public final String mTag;

        public abstract void onAcquire();

        public abstract void onRelease();

        public ResourceRef(String str) {
            this.mTag = str;
        }

        public void acquire(String str) {
            if (this.mOwners.isEmpty()) {
                SyncLogger.d(this.mTag, "onAcquire");
                onAcquire();
            }
            String str2 = this.mTag;
            SyncLogger.d(str2, "add owner: " + str);
            this.mOwners.add(str);
        }

        public void release(String str) {
            if (this.mOwners.remove(str)) {
                SyncLogger.d(this.mTag, "remove owner success: %s, left owners: %s", str, this.mOwners);
                if (!this.mOwners.isEmpty()) {
                    return;
                }
                SyncLogger.d(this.mTag, "onRelease");
                onRelease();
                return;
            }
            String str2 = this.mTag;
            SyncLogger.d(str2, "remove owner failed: " + str);
        }

        public void releaseAll() {
            dump();
            List<String> list = this.mOwners;
            if (list != null) {
                list.clear();
                SyncLogger.d(this.mTag, "onRelease");
                onRelease();
            }
        }

        public final void dump() {
            SyncLogger.d(this.mTag, "size=%d", Integer.valueOf(this.mOwners.size()));
            Iterator<String> it = this.mOwners.iterator();
            while (it.hasNext()) {
                String str = this.mTag;
                SyncLogger.d(str, "name=" + it.next());
            }
        }
    }

    /* loaded from: classes.dex */
    public class WifiLockRef extends ResourceRef {
        public WifiManager.WifiLock mWifiLock;

        public WifiLockRef(String str) {
            super(str);
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void onAcquire() {
            WifiManager.WifiLock createWifiLock = ((WifiManager) AsyncUpDownloadService.this.getApplicationContext().getSystemService("wifi")).createWifiLock(3, getClass().getName());
            this.mWifiLock = createWifiLock;
            createWifiLock.setReferenceCounted(false);
            this.mWifiLock.acquire();
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void onRelease() {
            WifiManager.WifiLock wifiLock = this.mWifiLock;
            if (wifiLock != null) {
                wifiLock.release();
                this.mWifiLock = null;
            }
        }
    }

    /* loaded from: classes.dex */
    public class WakeLockRef extends ResourceRef {
        public long mAcquireTime;
        public boolean mIsCharging;
        public boolean mIsScreenOn;
        public final List<String> mPendingOwners;
        public PowerManager.WakeLock mWakeLock;

        public WakeLockRef(String str) {
            super(str);
            this.mPendingOwners = Lists.newArrayList();
            this.mIsScreenOn = isScreenOn();
            this.mIsCharging = isCharging();
        }

        public final boolean isScreenOn() {
            return ((PowerManager) AsyncUpDownloadService.this.getSystemService("power")).isScreenOn();
        }

        public final boolean isCharging() {
            return GalleryPreferences.Sync.getIsPlugged();
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void onRelease() {
            PowerManager.WakeLock wakeLock = this.mWakeLock;
            if (wakeLock != null) {
                wakeLock.release();
                this.mWakeLock = null;
                HashMap hashMap = new HashMap();
                hashMap.put("elapse_time", String.valueOf(Math.round((((float) (System.currentTimeMillis() - this.mAcquireTime)) * 1.0f) / 1000.0f)));
                SamplingStatHelper.recordCountEvent("Sync", "sync_wakelock_time", hashMap);
            }
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void onAcquire() {
            PowerManager.WakeLock wakeLock = this.mWakeLock;
            if (wakeLock != null && wakeLock.isHeld()) {
                SyncLogger.e("AsyncUpDownloadService", new IllegalStateException("wakelock acquire and release not balance"));
                return;
            }
            PowerManager.WakeLock newWakeLock = ((PowerManager) AsyncUpDownloadService.this.getSystemService("power")).newWakeLock(1, this.mTag);
            this.mWakeLock = newWakeLock;
            newWakeLock.setReferenceCounted(false);
            this.mWakeLock.acquire();
            this.mAcquireTime = System.currentTimeMillis();
        }

        public final boolean canScreenAcquire() {
            return !AsyncUpDownloadService.WAKELOCK_ONLY_SCREEN_OFF || !this.mIsScreenOn;
        }

        public final boolean canBatteryAcquire() {
            return !AsyncUpDownloadService.WAKELOCK_ONLY_CHARGING || this.mIsCharging;
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void acquire(String str) {
            boolean canScreenAcquire = canScreenAcquire();
            boolean canBatteryAcquire = canBatteryAcquire();
            if (canScreenAcquire && canBatteryAcquire) {
                super.acquire(str);
                return;
            }
            this.mPendingOwners.add(str);
            SyncLogger.d(this.mTag, "can't acquire wakelock, add pending %s, pendings %s, screen %s, charge %s", str, this.mPendingOwners, Boolean.valueOf(canScreenAcquire), Boolean.valueOf(canBatteryAcquire));
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void release(String str) {
            if (this.mPendingOwners.remove(str)) {
                SyncLogger.d(this.mTag, "remove owner from pending success: %s, left pending owners: %s", str, this.mPendingOwners);
                if (!this.mPendingOwners.isEmpty()) {
                    return;
                }
                SyncLogger.d(this.mTag, "onRelease");
                onRelease();
                return;
            }
            if (this.mPendingOwners.size() > 0) {
                SyncLogger.d(this.mTag, "remove owner from pending fail, pending owners: %s, owners: %s, owner: %s", this.mPendingOwners, this.mOwners, str);
            }
            super.release(str);
        }

        public final void releaseTemp() {
            this.mOwners.clear();
            SyncLogger.d(this.mTag, "onRelease");
            onRelease();
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void releaseAll() {
            if (!this.mPendingOwners.isEmpty()) {
                onRelease();
            }
            super.releaseAll();
        }

        public final void tryAcquireLock() {
            if (this.mPendingOwners.size() > 0) {
                ArrayList<String> arrayList = new ArrayList(this.mPendingOwners);
                this.mPendingOwners.clear();
                for (String str : arrayList) {
                    acquire(str);
                }
            }
        }

        public final void tryReleaseLock() {
            if (this.mOwners.size() > 0) {
                this.mPendingOwners.clear();
                this.mPendingOwners.addAll(this.mOwners);
                releaseTemp();
            }
        }

        public void onScreenOnOff(boolean z) {
            if (!AsyncUpDownloadService.WAKELOCK_ONLY_SCREEN_OFF) {
                return;
            }
            this.mIsScreenOn = z;
            if (z) {
                SyncLogger.d(this.mTag, "screen on");
                tryReleaseLock();
                return;
            }
            SyncLogger.d(this.mTag, "screen off");
            tryAcquireLock();
        }

        public void onChargeStateChanged(boolean z) {
            if (!AsyncUpDownloadService.WAKELOCK_ONLY_CHARGING) {
                return;
            }
            this.mIsCharging = z;
            if (z) {
                SyncLogger.d(this.mTag, "charging");
                tryAcquireLock();
                return;
            }
            SyncLogger.d(this.mTag, "not charging");
            tryReleaseLock();
        }
    }

    /* loaded from: classes.dex */
    public class ForegroundRef extends ResourceRef {
        public ForegroundRef(String str) {
            super(str);
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void onAcquire() {
            AsyncUpDownloadService asyncUpDownloadService = AsyncUpDownloadService.this;
            asyncUpDownloadService.startForeground(4, NotificationHelper.getEmptyNotification(asyncUpDownloadService.getApplicationContext()));
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE_IMMEDIATE");
            AsyncUpDownloadService asyncUpDownloadService2 = AsyncUpDownloadService.this;
            asyncUpDownloadService2.registerReceiver(asyncUpDownloadService2.mConnReceiver, intentFilter);
        }

        @Override // com.miui.gallery.cloud.AsyncUpDownloadService.ResourceRef
        public void onRelease() {
            AsyncUpDownloadService.this.stopForeground(true);
            AsyncUpDownloadService.this.stopSelfSafely();
            AsyncUpDownloadService asyncUpDownloadService = AsyncUpDownloadService.this;
            GalleryUtils.safeUnregisterReceiver(asyncUpDownloadService, asyncUpDownloadService.mConnReceiver);
        }
    }

    public final void tryCancelTasksWhenConnChanged() {
        if (this.mDoingCancelTasks.compareAndSet(false, true)) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.cloud.AsyncUpDownloadService.2
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Object mo1807run(ThreadPool.JobContext jobContext) {
                    for (int i = 0; i < 14; i++) {
                        try {
                            int check = SyncConditionManager.check(i);
                            if (check != 0) {
                                SyncLogger.i("AsyncUpDownloadService", "net work is changed, cancel all, priority=" + i);
                                if (RequestItemBase.isCancellablePriority(i)) {
                                    UpDownloadManager.cancel(i, 2 == check, true);
                                }
                            }
                        } catch (Throwable th) {
                            AsyncUpDownloadService.this.mDoingCancelTasks.set(false);
                            throw th;
                        }
                    }
                    AsyncUpDownloadService.this.mDoingCancelTasks.set(false);
                    return null;
                }
            });
        }
    }

    @Override // com.miui.gallery.service.ServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public void onCreate() {
        super.onCreate();
        SyncLogger.d("AsyncUpDownloadService", "onCreate");
        this.mStartTime = SystemClock.uptimeMillis();
        this.mForegroundRef = new ForegroundRef("AsyncUpDownloadService#Foreground");
        this.mWakeLockRef = new WakeLockRef("AsyncUpDownloadService#WakeLock");
        this.mWifiLockRef = new WifiLockRef("AsyncUpDownloadService#WifiLock");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mSyncReceiver, new IntentFilter("com.miui.gallery.ACTION_SYNC_IN_BACKGROUND"));
        if (WAKELOCK_ONLY_SCREEN_OFF) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            registerReceiver(this.mScreenStateReceiver, intentFilter);
        }
        if (WAKELOCK_ONLY_CHARGING) {
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("android.intent.action.BATTERY_CHANGED");
            registerReceiver(this.mBatteryStateReceiver, intentFilter2);
        }
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        if (intent == null) {
            SyncLogger.e("AsyncUpDownloadService", "null intent parameter in onStartCommand! intent=" + intent + ", flags=" + i + ", startId=" + i2);
            stopSelfSafely();
            return 2;
        }
        handleRequest(intent);
        return 2;
    }

    @Override // com.miui.gallery.service.ServiceBase, android.app.Service
    public void onDestroy() {
        release();
        SyncLogger.d("AsyncUpDownloadService", "onDestroy, elapse time=" + (SystemClock.uptimeMillis() - this.mStartTime));
        super.onDestroy();
        HashMap hashMap = new HashMap();
        hashMap.put("elapse_time", String.valueOf(Math.round((((float) (SystemClock.uptimeMillis() - this.mStartTime)) * 1.0f) / 1000.0f)));
        SamplingStatHelper.recordCountEvent("Sync", "sync_service_time", hashMap);
    }

    public final void release() {
        ResourceRef resourceRef = this.mWifiLockRef;
        if (resourceRef != null) {
            resourceRef.releaseAll();
            this.mWifiLockRef = null;
        }
        WakeLockRef wakeLockRef = this.mWakeLockRef;
        if (wakeLockRef != null) {
            wakeLockRef.releaseAll();
            this.mWakeLockRef = null;
        }
        ForegroundRef foregroundRef = this.mForegroundRef;
        if (foregroundRef != null) {
            foregroundRef.releaseAll();
            this.mForegroundRef = null;
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mSyncReceiver);
        if (WAKELOCK_ONLY_SCREEN_OFF) {
            GalleryUtils.safeUnregisterReceiver(this, this.mScreenStateReceiver);
        }
        if (WAKELOCK_ONLY_CHARGING) {
            GalleryUtils.safeUnregisterReceiver(this, this.mBatteryStateReceiver);
        }
        sPendingRequests.clear();
    }

    public static boolean needHandleRequest(String str) {
        return sPendingRequests.remove(str);
    }

    public final boolean isLockInitialized() {
        return (this.mWifiLockRef == null || this.mWakeLockRef == null || this.mForegroundRef == null) ? false : true;
    }

    public void handleRequest(Intent intent) {
        String action = intent.getAction();
        SyncLogger.d("AsyncUpDownloadService", "handleRequest: action=" + action);
        if ("com.miui.gallery.ACTION_SYNC_IN_BACKGROUND".equals(action)) {
            if (!isLockInitialized()) {
                SyncLogger.w("AsyncUpDownloadService", "service has been released");
                release();
                return;
            }
            String stringExtra = intent.getStringExtra("com.miui.gallery.EXTRA_SYNC_NAME");
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            boolean booleanExtra = intent.getBooleanExtra("com.miui.gallery.EXTRA_SYNC_STOP", true);
            boolean booleanExtra2 = intent.getBooleanExtra("com.miui.gallery.EXTRA_SYNC_WIFI", false);
            boolean booleanExtra3 = intent.getBooleanExtra("com.miui.gallery.EXTRA_SYNC_WAKE", true);
            SyncLogger.d("AsyncUpDownloadService", "owner=%s, is stop=%s, wifiNeeded=%s, wakeupNeeded=%s", stringExtra, Boolean.valueOf(booleanExtra), Boolean.valueOf(booleanExtra2), Boolean.valueOf(booleanExtra3));
            if (booleanExtra) {
                if (booleanExtra2) {
                    this.mWifiLockRef.release(stringExtra);
                }
                if (booleanExtra3) {
                    this.mWakeLockRef.release(stringExtra);
                }
                this.mForegroundRef.release(stringExtra);
            } else if (needHandleRequest(stringExtra)) {
                this.mForegroundRef.acquire(stringExtra);
                if (booleanExtra3) {
                    SyncLogger.d("AsyncUpDownloadService", "handle request, owner: %s", stringExtra);
                    this.mWakeLockRef.acquire(stringExtra);
                }
                if (!booleanExtra2) {
                    return;
                }
                this.mWifiLockRef.acquire(stringExtra);
            } else {
                SyncLogger.w("AsyncUpDownloadService", "no need handle request: %s", stringExtra);
            }
        }
    }

    public final void handleScreenOnOff(boolean z) {
        this.mWakeLockRef.onScreenOnOff(z);
    }

    public final void handleChargeStateChanged(boolean z) {
        this.mWakeLockRef.onChargeStateChanged(z);
    }

    public static void startSyncInBackground(String str, boolean z, boolean z2) {
        sPendingRequests.add(str);
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Intent intent = new Intent("com.miui.gallery.ACTION_SYNC_IN_BACKGROUND");
        intent.setPackage(sGetAndroidContext.getPackageName());
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_NAME", str);
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_STOP", false);
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_WIFI", z);
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_WAKE", z2);
        BackgroundServiceHelper.startForegroundServiceIfNeed(sGetAndroidContext, intent);
    }

    public static void stopSyncInBackground(String str, boolean z, boolean z2) {
        sPendingRequests.remove(str);
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Intent intent = new Intent("com.miui.gallery.ACTION_SYNC_IN_BACKGROUND");
        intent.setPackage(sGetAndroidContext.getPackageName());
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_NAME", str);
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_STOP", true);
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_WIFI", z);
        intent.putExtra("com.miui.gallery.EXTRA_SYNC_WAKE", z2);
        LocalBroadcastManager.getInstance(sGetAndroidContext).sendBroadcast(intent);
    }

    public static SyncLock newSyncLock(String str) {
        return new SyncLock(str);
    }

    /* loaded from: classes.dex */
    public static final class SyncLock {
        public String mIdentifier;
        public final Object mToken = new Object();
        public int mCount = 0;

        public SyncLock(String str) {
            this.mIdentifier = str + "#" + SystemClock.elapsedRealtimeNanos();
        }

        public void acquire() {
            synchronized (this.mToken) {
                int i = this.mCount;
                this.mCount = i + 1;
                if (i == 0) {
                    SyncLogger.d("AsyncUpDownloadService", "thread: %s, %s require lock, identifier: %s", Thread.currentThread(), Integer.valueOf(System.identityHashCode(Thread.currentThread())), this.mIdentifier);
                    ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.AsyncUpDownloadService.SyncLock.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AsyncUpDownloadService.startSyncInBackground(SyncLock.this.mIdentifier, true, true);
                        }
                    });
                }
            }
        }

        public void release() {
            synchronized (this.mToken) {
                int i = this.mCount - 1;
                this.mCount = i;
                if (i == 0) {
                    SyncLogger.d("AsyncUpDownloadService", "thread: %s, %s release lock, identifier: %s", Thread.currentThread(), Integer.valueOf(System.identityHashCode(Thread.currentThread())), this.mIdentifier);
                    ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.AsyncUpDownloadService.SyncLock.2
                        @Override // java.lang.Runnable
                        public void run() {
                            AsyncUpDownloadService.stopSyncInBackground(SyncLock.this.mIdentifier, true, true);
                        }
                    });
                }
                if (this.mCount < 0) {
                    throw new RuntimeException("SyncLock under-locked " + this.mIdentifier);
                }
            }
        }
    }
}
