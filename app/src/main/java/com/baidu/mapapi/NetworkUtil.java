package com.baidu.mapapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.baidu.mapsdkplatform.comapi.util.SysUpdateObservable;

/* loaded from: classes.dex */
public class NetworkUtil {
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getCurrentNetMode(Context context) {
        NetworkCapabilities networkCapabilities;
        if (context == null) {
            return null;
        }
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        int i = 4;
        if (activeNetworkInfo != null) {
            if (activeNetworkInfo.getType() != 1) {
                if (Build.VERSION.SDK_INT <= 29) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                    if (telephonyManager != null) {
                        switch (telephonyManager.getNetworkType()) {
                            case 1:
                            case 2:
                                i = 6;
                                break;
                            case 3:
                            case 9:
                            case 10:
                            case 15:
                                i = 9;
                                break;
                            case 4:
                                i = 5;
                                break;
                            case 5:
                            case 6:
                            case 7:
                            case 12:
                                i = 7;
                                break;
                            case 8:
                                i = 8;
                                break;
                            case 11:
                                i = 2;
                                break;
                            case 14:
                                i = 10;
                                break;
                        }
                    } else {
                        return Integer.toString(0);
                    }
                } else {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                    if (connectivityManager == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())) == null) {
                        return "mobile";
                    }
                    return networkCapabilities.hasTransport(1) ? "WIFI" : networkCapabilities.hasTransport(0) ? "CELLULAR" : networkCapabilities.hasTransport(3) ? "ETHERNET" : networkCapabilities.hasTransport(6) ? "LoWPAN" : networkCapabilities.hasTransport(4) ? "VPN" : networkCapabilities.hasTransport(5) ? "WifiAware" : "mobile";
                }
            } else {
                i = 1;
            }
            return Integer.toString(i);
        }
        i = 0;
        return Integer.toString(i);
    }

    public static boolean initConnectState() {
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            if (isWifiConnected(context)) {
                return true;
            }
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return false;
            }
            return activeNetworkInfo.isConnectedOrConnecting();
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager;
        if (context == null || (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || 1 != activeNetworkInfo.getType()) {
                return false;
            }
            return activeNetworkInfo.isConnected();
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x000e, code lost:
        if (r3.isConnected() != false) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isWifiConnected(android.net.NetworkInfo r3) {
        /*
            r0 = 1
            r1 = 0
            if (r3 == 0) goto L16
            int r2 = r3.getType()     // Catch: java.lang.Exception -> L11
            if (r0 != r2) goto L16
            boolean r3 = r3.isConnected()     // Catch: java.lang.Exception -> L11
            if (r3 == 0) goto L16
            goto L17
        L11:
            r3 = move-exception
            r3.printStackTrace()
            goto L18
        L16:
            r0 = r1
        L17:
            r1 = r0
        L18:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.mapapi.NetworkUtil.isWifiConnected(android.net.NetworkInfo):boolean");
    }

    public static void updateNetworkProxy(Context context) {
        SysUpdateObservable.getInstance().updateNetworkProxy(context);
    }
}
