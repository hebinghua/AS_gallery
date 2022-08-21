package com.miui.gallery.cloud.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.ReceiverUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class BatteryMonitor {
    public int mCount;
    public BroadcastReceiver mPowerIntentReceiver = new BroadcastReceiver() { // from class: com.miui.gallery.cloud.control.BatteryMonitor.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                GalleryPreferences.Sync.setPowerCanSync(BatteryMonitor.isPowerCanSync(context, intent));
            }
        }
    };

    /* loaded from: classes.dex */
    public static class Holder {
        public static final BatteryMonitor sInstance = new BatteryMonitor();
    }

    public static BatteryMonitor getInstance() {
        return Holder.sInstance;
    }

    public static boolean isPowerCanSync(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("status", -1);
        boolean z = intExtra == 2 || intExtra == 5;
        DefaultLogger.d("BatteryMonitor", "ACTION_BATTERY_CHANGED, plugged:" + z);
        if (context != null) {
            if (z) {
                GalleryPreferences.Sync.setIsPlugged(true);
                DefaultLogger.d("BatteryMonitor", "power is connected");
            } else {
                GalleryPreferences.Sync.setIsPlugged(false);
                DefaultLogger.d("BatteryMonitor", "power disconnected");
            }
        }
        if (!z) {
            int intExtra2 = (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
            DefaultLogger.d("BatteryMonitor", "ACTION_BATTERY_CHANGED, power remaining:" + intExtra2);
            return intExtra2 > 20;
        }
        return true;
    }

    public synchronized void start() {
        if (this.mCount == 0) {
            GalleryApp.sGetAndroidContext().registerReceiver(this.mPowerIntentReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }
        this.mCount++;
    }

    public synchronized void end() {
        int i = this.mCount - 1;
        this.mCount = i;
        if (i == 0) {
            ReceiverUtils.safeUnregisterReceiver(GalleryApp.sGetAndroidContext(), this.mPowerIntentReceiver);
        }
    }
}
