package com.miui.gallery.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ReceiverUtils {
    public static boolean safeUnregisterReceiver(Context context, BroadcastReceiver broadcastReceiver) {
        if (context == null || broadcastReceiver == null) {
            return false;
        }
        try {
            context.unregisterReceiver(broadcastReceiver);
            return true;
        } catch (Exception e) {
            DefaultLogger.e("ReceiverUtils", "fail to unregister receiver: " + broadcastReceiver + e.toString());
            return false;
        }
    }

    public static void registerReceiver(Context context, BroadcastReceiver broadcastReceiver, String... strArr) {
        registerReceiver(context, broadcastReceiver, 0, null, strArr);
    }

    public static void registerReceiver(Context context, BroadcastReceiver broadcastReceiver, int i, String str, String... strArr) {
        if (broadcastReceiver == null || strArr == null || strArr.length <= 0) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(i);
        if (!TextUtils.isEmpty(str)) {
            intentFilter.addDataScheme(str);
        }
        for (String str2 : strArr) {
            intentFilter.addAction(str2);
        }
        context.registerReceiver(broadcastReceiver, intentFilter);
    }
}
