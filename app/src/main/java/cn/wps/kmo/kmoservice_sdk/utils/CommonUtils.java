package cn.wps.kmo.kmoservice_sdk.utils;

import android.util.Log;

/* loaded from: classes.dex */
public class CommonUtils {
    public static boolean DEBUG = false;

    public static void log(String str) {
        if (DEBUG) {
            Log.e("CommonUtils", str);
        }
    }
}
