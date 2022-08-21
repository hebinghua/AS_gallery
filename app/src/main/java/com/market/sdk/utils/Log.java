package com.market.sdk.utils;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class Log {
    public static void d(String str, String str2) {
        log(addPrefix(str), str2, 3);
    }

    public static void e(String str, String str2) {
        log(addPrefix(str), str2, 0);
    }

    public static void e(String str, String str2, Throwable th) {
        log(addPrefix(str), str2, th, 0);
    }

    public static void i(String str, String str2) {
        log(addPrefix(str), str2, 2);
    }

    public static void log(String str, String str2, int i) {
        if (TextUtils.isEmpty(str2) || str2.length() <= 3000) {
            logSubMessage(str, str2, i);
            return;
        }
        int i2 = 0;
        while (i2 <= str2.length() / 3000) {
            int i3 = i2 * 3000;
            i2++;
            int min = Math.min(str2.length(), i2 * 3000);
            if (i3 < min) {
                logSubMessage(str, str2.substring(i3, min), i);
            }
        }
    }

    public static void log(String str, String str2, Throwable th, int i) {
        if (TextUtils.isEmpty(str2) || str2.length() <= 3000) {
            logSubMessage(str, str2, th, i);
            return;
        }
        int i2 = 0;
        while (i2 <= str2.length() / 3000) {
            int i3 = i2 * 3000;
            i2++;
            int min = Math.min(str2.length(), i2 * 3000);
            if (i3 < min) {
                logSubMessage(str, str2.substring(i3, min), th, i);
            }
        }
    }

    public static void logSubMessage(String str, String str2, int i) {
        if (str2 == null) {
            str2 = "";
        }
        if (i == 0) {
            android.util.Log.e(str, str2);
        } else if (i == 1) {
            android.util.Log.w(str, str2);
        } else if (i == 2) {
            android.util.Log.i(str, str2);
        } else if (i == 3) {
            android.util.Log.d(str, str2);
        } else if (i != 4) {
        } else {
            android.util.Log.v(str, str2);
        }
    }

    public static void logSubMessage(String str, String str2, Throwable th, int i) {
        if (str2 == null) {
            str2 = "";
        }
        if (i == 0) {
            android.util.Log.e(str, str2, th);
        } else if (i == 1) {
            android.util.Log.w(str, str2, th);
        } else if (i == 2) {
            android.util.Log.i(str, str2, th);
        } else if (i == 3) {
            android.util.Log.d(str, str2, th);
        } else if (i != 4) {
        } else {
            android.util.Log.v(str, str2, th);
        }
    }

    public static String addPrefix(String str) {
        return "MarketSdk-" + str;
    }
}
