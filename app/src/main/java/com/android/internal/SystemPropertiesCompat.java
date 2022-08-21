package com.android.internal;

import android.os.SystemProperties;

/* loaded from: classes.dex */
public class SystemPropertiesCompat {
    public static String get(String str, String str2) {
        try {
            return SystemProperties.get(str, str2);
        } catch (Throwable th) {
            th.printStackTrace();
            return str2;
        }
    }

    public static int getInt(String str, int i) {
        try {
            return SystemProperties.getInt(str, i);
        } catch (Throwable th) {
            th.printStackTrace();
            return i;
        }
    }

    public static boolean getBoolean(String str, boolean z) {
        try {
            return SystemProperties.getBoolean(str, z);
        } catch (Throwable th) {
            th.printStackTrace();
            return z;
        }
    }
}
