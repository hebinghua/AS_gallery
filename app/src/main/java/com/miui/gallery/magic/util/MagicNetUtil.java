package com.miui.gallery.magic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* loaded from: classes2.dex */
public class MagicNetUtil {
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null && (allNetworkInfo = connectivityManager.getAllNetworkInfo()) != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean IsMobileNetConnect(Context context) {
        try {
            return NetworkInfo.State.CONNECTED == ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(0).getState();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
