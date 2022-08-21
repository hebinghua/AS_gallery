package com.miui.gallery.cloud.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.cloud.control.BatteryMonitor;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.trackers.ConstraintListener;
import com.miui.gallery.trackers.NetworkState;
import com.miui.gallery.trackers.Trackers;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public final class DownloadObserver {
    public BroadcastReceiver mCtaReceiver;
    public BroadcastReceiver mDeviceStorageReceiver;
    public final CopyOnWriteArraySet<OnConditionChangeListener> mListeners;
    public NetworkListener mNetworkListener;
    public BroadcastReceiver mPowerStatusReceiver;
    public final AtomicInteger mRegisterCount;

    /* loaded from: classes.dex */
    public interface OnConditionChangeListener {
        void onConditionChanged(Context context);
    }

    public DownloadObserver() {
        this.mRegisterCount = new AtomicInteger(0);
        this.mListeners = new CopyOnWriteArraySet<>();
    }

    public static DownloadObserver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static final class SingletonHolder {
        public static final DownloadObserver INSTANCE = new DownloadObserver();
    }

    public final void ensureObservers() {
        this.mCtaReceiver = new CTAReceiver();
        this.mNetworkListener = new NetworkListener();
        this.mPowerStatusReceiver = new PowerChangedReceiver();
        this.mDeviceStorageReceiver = new DeviceStorageStateReceiver();
    }

    public final void doRegister(Context context) {
        ensureObservers();
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mCtaReceiver, new IntentFilter("com.miui.gallery.action.CTA_CHANGED"));
        Trackers.getNetworkStateTracker().registerListener(this.mNetworkListener);
        context.registerReceiver(this.mPowerStatusReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"), null, ThreadManager.getWorkHandler());
        IntentFilter intentFilter = new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW");
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
        context.registerReceiver(this.mDeviceStorageReceiver, intentFilter, null, ThreadManager.getWorkHandler());
    }

    public void register(Context context, OnConditionChangeListener onConditionChangeListener) {
        DefaultLogger.d("DownloadObserver", "register %s", onConditionChangeListener);
        if (!this.mListeners.add(onConditionChangeListener) || this.mRegisterCount.getAndIncrement() != 0) {
            return;
        }
        final Context applicationContext = context.getApplicationContext();
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.download.DownloadObserver.1
            @Override // java.lang.Runnable
            public void run() {
                DefaultLogger.d("DownloadObserver", "do register");
                DownloadObserver.this.doRegister(applicationContext);
            }
        });
    }

    public final void doUnregister(Context context) {
        if (this.mCtaReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this.mCtaReceiver);
            this.mCtaReceiver = null;
        }
        if (this.mNetworkListener != null) {
            Trackers.getNetworkStateTracker().unregisterListener(this.mNetworkListener);
            this.mNetworkListener = null;
        }
        BroadcastReceiver broadcastReceiver = this.mPowerStatusReceiver;
        if (broadcastReceiver != null) {
            context.unregisterReceiver(broadcastReceiver);
            this.mPowerStatusReceiver = null;
        }
        BroadcastReceiver broadcastReceiver2 = this.mDeviceStorageReceiver;
        if (broadcastReceiver2 != null) {
            context.unregisterReceiver(broadcastReceiver2);
            this.mDeviceStorageReceiver = null;
        }
    }

    public void unregister(Context context, OnConditionChangeListener onConditionChangeListener) {
        DefaultLogger.d("DownloadObserver", "unregister %s", onConditionChangeListener);
        if (!this.mListeners.remove(onConditionChangeListener) || this.mRegisterCount.decrementAndGet() > 0) {
            return;
        }
        final Context applicationContext = context.getApplicationContext();
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.download.DownloadObserver.2
            @Override // java.lang.Runnable
            public void run() {
                DefaultLogger.d("DownloadObserver", "do unregister");
                DownloadObserver.this.doUnregister(applicationContext);
            }
        });
    }

    public final void notifyConditionChanged(Context context) {
        Iterator<OnConditionChangeListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onConditionChanged(context);
        }
    }

    /* loaded from: classes.dex */
    public class CTAReceiver extends BroadcastReceiver {
        public CTAReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            DownloadObserver.this.notifyConditionChanged(context);
        }
    }

    /* loaded from: classes.dex */
    public class NetworkListener implements ConstraintListener<NetworkState> {
        public boolean mIsNetConnected = BaseNetworkUtils.isNetworkConnected();
        public boolean mIsWifiConnected = !BaseNetworkUtils.isActiveNetworkMetered();
        public NetworkState mNetworkState;

        public NetworkListener() {
        }

        @Override // com.miui.gallery.trackers.ConstraintListener
        public void onConstraintChanged(NetworkState networkState) {
            boolean z;
            if (networkState != null && !networkState.equals(this.mNetworkState)) {
                DefaultLogger.d("DownloadObserver", "network changed: pre: %s, curr: %s", this.mNetworkState, networkState);
                this.mNetworkState = networkState;
                boolean isConnected = networkState.isConnected();
                boolean z2 = false;
                boolean z3 = true;
                if (this.mIsNetConnected != isConnected) {
                    this.mIsNetConnected = isConnected;
                    z = true;
                } else {
                    z = false;
                }
                if (this.mIsNetConnected && !networkState.isMetered()) {
                    z2 = true;
                }
                if (this.mIsWifiConnected != z2) {
                    this.mIsWifiConnected = z2;
                } else {
                    z3 = z;
                }
                if (!z3) {
                    return;
                }
                DownloadObserver.this.notifyConditionChanged(StaticContext.sGetAndroidContext());
            }
        }
    }

    /* loaded from: classes.dex */
    public class PowerChangedReceiver extends BroadcastReceiver {
        public boolean mIsBatteryLow = !GalleryPreferences.Sync.getPowerCanSync();

        public PowerChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                boolean isPlugged = GalleryPreferences.Sync.getIsPlugged();
                BatteryMonitor.getInstance();
                boolean z = !BatteryMonitor.isPowerCanSync(context, intent);
                if (z != this.mIsBatteryLow) {
                    this.mIsBatteryLow = z;
                    GalleryPreferences.Sync.setPowerCanSync(!z);
                    DownloadObserver.this.notifyConditionChanged(context);
                    DefaultLogger.d("DownloadObserver", "battery status changed: %s", Boolean.valueOf(z));
                } else if (isPlugged == GalleryPreferences.Sync.getIsPlugged()) {
                } else {
                    DownloadObserver.this.notifyConditionChanged(context);
                    DefaultLogger.d("DownloadObserver", "charging state changed: %s", Boolean.valueOf(!isPlugged));
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class DeviceStorageStateReceiver extends BroadcastReceiver {
        public DeviceStorageStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DefaultLogger.i("DownloadObserver", "DeviceStorageStateReceiver %s", action);
            if ("android.intent.action.DEVICE_STORAGE_LOW".equals(action)) {
                GalleryPreferences.Sync.setDeviceStorageLow(true);
                DownloadObserver.this.notifyConditionChanged(context);
            } else if (!"android.intent.action.DEVICE_STORAGE_OK".equals(action)) {
            } else {
                GalleryPreferences.Sync.setDeviceStorageLow(false);
                DownloadObserver.this.notifyConditionChanged(context);
            }
        }
    }
}
