package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes3.dex */
public class FCMPushHelper {
    public static void persistIfXmsfSupDecrypt(Context context) {
        boolean z = false;
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        if (com.xiaomi.push.m.b(context) >= 50002000) {
            z = true;
        }
        edit.putBoolean("is_xmsf_sup_decrypt", z).apply();
    }
}
