package com.xiaomi.micloudsdk.sync.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/* loaded from: classes3.dex */
public class SyncTimeUtils {
    public static synchronized long getGuardDelayUntilInSec(Context context, String str) {
        synchronized (SyncTimeUtils.class) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            long currentTimeMillis = System.currentTimeMillis();
            String format = String.format("SyncWindowStart_%s", str);
            String format2 = String.format("SyncTimes_%s", str);
            long j = defaultSharedPreferences.getLong(format, 0L);
            int i = defaultSharedPreferences.getInt(format2, 1);
            if (Math.abs(currentTimeMillis - j) < 600000) {
                if (i > 30) {
                    return ((j + 600000) - currentTimeMillis) / 1000;
                }
                defaultSharedPreferences.edit().putInt(format2, i + 1).commit();
                return 0L;
            }
            SharedPreferences.Editor edit = defaultSharedPreferences.edit();
            edit.putLong(format, currentTimeMillis);
            edit.putInt(format2, 1);
            edit.commit();
            return 0L;
        }
    }
}
