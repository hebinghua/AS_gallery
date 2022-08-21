package com.miui.gallery.cloud.syncstate;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncStatusObserver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.cloud.control.BatteryMonitor;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.trackers.ConstraintListener;
import com.miui.gallery.trackers.NetworkState;
import com.miui.gallery.trackers.Trackers;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class SyncStateObserver {
    public BroadcastReceiver mCtaReceiver;
    public BroadcastReceiver mDeviceStorageReceiver;
    public BroadcastReceiver mFirstPulledReceiver;
    public ContentObserver mMediaObserver;
    public NetworkListener mNetworkListener;
    public BroadcastReceiver mPowerStatusReceiver;
    public int mRegisterCount = 0;
    public BroadcastReceiver mSyncCommandDispatchedReceiver;
    public ContentObserver mSyncOnlyWifiObserver;
    public Object mSyncSettingObserver;
    public ContentObserver mUploadingObserver;

    public final void ensureObservers() {
        if (this.mNetworkListener == null) {
            this.mCtaReceiver = new CTAReceiver();
            this.mNetworkListener = new NetworkListener();
            this.mPowerStatusReceiver = new PowerChangedReceiver();
            this.mDeviceStorageReceiver = new DeviceStorageStateReceiver();
            this.mSyncCommandDispatchedReceiver = new SyncRequestDispatchedReceiver();
            this.mUploadingObserver = new UploadingObserver(ThreadManager.getWorkHandler());
            this.mMediaObserver = new MediaObserver(ThreadManager.getWorkHandler());
            this.mSyncOnlyWifiObserver = new SyncContentObserver(ThreadManager.getWorkHandler());
            this.mSyncSettingObserver = new SyncSettingObserver();
            this.mFirstPulledReceiver = new FirstPulledReceiver();
        }
    }

    public final void initState(Context context) {
        Intent registerReceiver = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            GalleryPreferences.Sync.setPowerCanSync(BatteryMonitor.isPowerCanSync(context, registerReceiver));
        }
        this.mMediaObserver.onChange(false);
    }

    public final void doRegister(Context context) {
        ensureObservers();
        initState(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mCtaReceiver, new IntentFilter("com.miui.gallery.action.CTA_CHANGED"));
        Trackers.getNetworkStateTracker().registerListener(this.mNetworkListener);
        context.registerReceiver(this.mPowerStatusReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"), null, ThreadManager.getWorkHandler());
        IntentFilter intentFilter = new IntentFilter("android.intent.action.DEVICE_STORAGE_LOW");
        intentFilter.addAction("android.intent.action.DEVICE_STORAGE_OK");
        context.registerReceiver(this.mDeviceStorageReceiver, intentFilter, null, ThreadManager.getWorkHandler());
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mSyncCommandDispatchedReceiver, new IntentFilter("com.miui.gallery.SYNC_COMMAND_DISPATCHED"));
        context.getContentResolver().registerContentObserver(GalleryCloudProvider.UPLOAD_STATE_URI, true, this.mUploadingObserver);
        context.getContentResolver().registerContentObserver(GalleryContract.Media.URI_ALL, true, this.mMediaObserver);
        context.getContentResolver().registerContentObserver(GalleryContract.Media.URI, true, this.mMediaObserver);
        context.getContentResolver().registerContentObserver(GalleryContract.ShareImage.SHARE_URI, true, this.mMediaObserver);
        context.getContentResolver().registerContentObserver(GalleryCloudProvider.SYNC_SETTINGS_URI, false, this.mSyncOnlyWifiObserver);
        this.mSyncSettingObserver = ContentResolver.addStatusChangeListener(1, new SyncSettingObserver());
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mFirstPulledReceiver, new IntentFilter("com.miui.gallery.action.FIRST_SYNC_FINISHED"));
    }

    public void register(Context context) {
        DefaultLogger.d("SyncStateObserver", "register %s", context);
        int i = this.mRegisterCount;
        this.mRegisterCount = i + 1;
        if (i == 0) {
            final Context applicationContext = context.getApplicationContext();
            ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.syncstate.SyncStateObserver.1
                @Override // java.lang.Runnable
                public void run() {
                    DefaultLogger.d("SyncStateObserver", "do register");
                    SyncStateObserver.this.doRegister(applicationContext);
                }
            });
        }
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
        if (this.mSyncCommandDispatchedReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this.mSyncCommandDispatchedReceiver);
            this.mSyncCommandDispatchedReceiver = null;
        }
        if (this.mUploadingObserver != null) {
            context.getContentResolver().unregisterContentObserver(this.mUploadingObserver);
            this.mUploadingObserver = null;
        }
        if (this.mMediaObserver != null) {
            context.getContentResolver().unregisterContentObserver(this.mMediaObserver);
            this.mMediaObserver = null;
        }
        if (this.mSyncOnlyWifiObserver != null) {
            context.getContentResolver().unregisterContentObserver(this.mSyncOnlyWifiObserver);
            this.mSyncOnlyWifiObserver = null;
        }
        Object obj = this.mSyncSettingObserver;
        if (obj != null) {
            ContentResolver.removeStatusChangeListener(obj);
            this.mSyncSettingObserver = null;
        }
        if (this.mFirstPulledReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this.mFirstPulledReceiver);
            this.mFirstPulledReceiver = null;
        }
    }

    public void unregister(Context context) {
        DefaultLogger.d("SyncStateObserver", "unregister %s", context);
        int i = this.mRegisterCount - 1;
        this.mRegisterCount = i;
        if (i <= 0) {
            final Context applicationContext = context.getApplicationContext();
            ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.cloud.syncstate.SyncStateObserver.2
                @Override // java.lang.Runnable
                public void run() {
                    DefaultLogger.d("SyncStateObserver", "do unregister");
                    SyncStateObserver.this.doUnregister(applicationContext);
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public static class CTAReceiver extends BroadcastReceiver {
        public CTAReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SyncStateManager.getInstance().updateSyncStatus();
        }
    }

    /* loaded from: classes.dex */
    public static class NetworkListener implements ConstraintListener<NetworkState> {
        public boolean mIsNetConnected = BaseNetworkUtils.isNetworkConnected();
        public boolean mIsWifiConnected = !BaseNetworkUtils.isActiveNetworkMetered();
        public NetworkState mNetworkState;

        @Override // com.miui.gallery.trackers.ConstraintListener
        public void onConstraintChanged(NetworkState networkState) {
            boolean z;
            if (networkState != null && !networkState.equals(this.mNetworkState)) {
                DefaultLogger.d("SyncStateObserver", "network changed: pre: %s, curr: %s", this.mNetworkState, networkState);
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
                SyncStateManager.getInstance().updateSyncStatus();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class PowerChangedReceiver extends BroadcastReceiver {
        public boolean mIsBatteryLow = !GalleryPreferences.Sync.getPowerCanSync();

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                BatteryMonitor.getInstance();
                boolean z = !BatteryMonitor.isPowerCanSync(context, intent);
                if (z != this.mIsBatteryLow) {
                    this.mIsBatteryLow = z;
                    GalleryPreferences.Sync.setPowerCanSync(!z);
                    DefaultLogger.d("SyncStateObserver", "battery status changed: %s", Boolean.valueOf(z));
                }
                SyncStateManager.getInstance().setIsBatteryLow(z);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class DeviceStorageStateReceiver extends BroadcastReceiver {
        public DeviceStorageStateReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DefaultLogger.i("SyncStateObserver", "DeviceStorageStateReceiver %s", action);
            if ("android.intent.action.DEVICE_STORAGE_LOW".equals(action)) {
                GalleryPreferences.Sync.setDeviceStorageLow(true);
                SyncStateManager.getInstance().setIsLocalSpaceFull(true);
            } else if (!"android.intent.action.DEVICE_STORAGE_OK".equals(action)) {
            } else {
                GalleryPreferences.Sync.setDeviceStorageLow(false);
                SyncStateManager.getInstance().setIsLocalSpaceFull(false);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SyncRequestDispatchedReceiver extends BroadcastReceiver {
        public SyncRequestDispatchedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            DefaultLogger.d("SyncStateObserver", "SyncRequestDispatchedReceiver: %s", intent.getAction());
            if ("com.miui.gallery.SYNC_COMMAND_DISPATCHED".equals(intent.getAction())) {
                SyncStateManager.getInstance().onSyncCommandDispatched();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class UploadingObserver extends ContentObserver {
        public UploadingObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            DefaultLogger.d("SyncStateObserver", "UploadingObserver");
            SyncStateManager.getInstance().updateSyncStatus();
        }
    }

    /* loaded from: classes.dex */
    public static class SyncContentObserver extends ContentObserver {
        public SyncContentObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            SyncStateManager.getInstance().updateSyncStatus();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            DefaultLogger.d("SyncStateObserver", "SyncContentObserver %s", uri);
            super.onChange(z, uri);
        }
    }

    /* loaded from: classes.dex */
    public static class MediaObserver extends SyncContentObserver {
        public MediaObserver(Handler handler) {
            super(handler);
        }

        @Override // com.miui.gallery.cloud.syncstate.SyncStateObserver.SyncContentObserver, android.database.ContentObserver
        public void onChange(boolean z) {
            SyncStateManager.getInstance().triggerMediaChanged();
        }
    }

    /* loaded from: classes.dex */
    public static class SyncSettingObserver implements SyncStatusObserver {
        public SyncSettingObserver() {
        }

        @Override // android.content.SyncStatusObserver
        public void onStatusChanged(int i) {
            if (i == 1) {
                DefaultLogger.d("SyncStateObserver", "SyncSettingObserver");
                SyncStateManager.getInstance().updateSyncStatus();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class FirstPulledReceiver extends BroadcastReceiver {
        public FirstPulledReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            SyncStateManager.getInstance().updateSyncStatus();
        }
    }
}
