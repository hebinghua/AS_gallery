package com.baidu.platform.comapi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class NetworkUtil {
    public static final int NETYPE_2G = 2;
    public static final int NETYPE_3G = 3;
    public static final int NETYPE_4G = 4;
    public static final int NETYPE_4G_UNKNOWN = 10;
    public static final int NETYPE_MOBILE_3G = 8;
    public static final int NETYPE_MOBILE_UNICOM_2G = 6;
    public static final int NETYPE_NOCON = -1;
    public static final int NETYPE_TELECOM_2G = 5;
    public static final int NETYPE_TELECOM_3G = 7;
    public static final int NETYPE_UNICOM_3G = 9;
    public static final int NETYPE_UNKNOWN = 0;
    public static final int NETYPE_WIFI = 1;
    public static String mProxyHost = "";
    public static int mProxyPort = 0;
    public static boolean mUseProxy = false;

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager;
        if (context != null && (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) != null) {
            try {
                return connectivityManager.getActiveNetworkInfo();
            } catch (Exception unused) {
            }
        }
        return null;
    }

    public static NetworkInfo[] getAllNetworkInfo(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return ((ConnectivityManager) context.getSystemService("connectivity")).getAllNetworkInfo();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getCurrentNetMode(Context context) {
        NetworkCapabilities networkCapabilities;
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        int i = -1;
        if (activeNetworkInfo != null) {
            int type = activeNetworkInfo.getType();
            if (type != 1) {
                if (type == 0 || type == 3 || type == 4 || type == 5) {
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
                                case 12:
                                    i = 7;
                                    break;
                                case 7:
                                case 11:
                                case 16:
                                    i = 2;
                                    break;
                                case 8:
                                case 17:
                                    i = 8;
                                    break;
                                case 13:
                                case 18:
                                    i = 4;
                                    break;
                                case 14:
                                    i = 3;
                                    break;
                                default:
                                    i = 0;
                                    break;
                            }
                        }
                    } else {
                        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                        if (connectivityManager == null || (networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())) == null) {
                            return "mobile";
                        }
                        return networkCapabilities.hasTransport(1) ? "WIFI" : networkCapabilities.hasTransport(0) ? "CELLULAR" : networkCapabilities.hasTransport(3) ? "ETHERNET" : networkCapabilities.hasTransport(6) ? "LoWPAN" : networkCapabilities.hasTransport(4) ? "VPN" : networkCapabilities.hasTransport(5) ? "WifiAware" : "mobile";
                    }
                }
            } else {
                i = 1;
            }
        }
        return Integer.toString(i);
    }

    public static String getNetworkOperatorInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!TextUtils.isEmpty(networkOperator)) {
                try {
                    StringBuilder sb = new StringBuilder(networkOperator);
                    sb.insert(3, ":");
                    return sb.toString();
                } catch (Exception unused) {
                }
            }
        }
        return "";
    }

    public static int getNetworkOperatorType(Context context) {
        String networkOperatorInfo = getNetworkOperatorInfo(context);
        if (!TextUtils.isEmpty(networkOperatorInfo)) {
            if (networkOperatorInfo.startsWith("460:00") || networkOperatorInfo.startsWith("460:02")) {
                return 0;
            }
            if (networkOperatorInfo.startsWith("460:01")) {
                return 1;
            }
            return networkOperatorInfo.startsWith("460:03") ? 2 : -1;
        }
        return -1;
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
        NetworkInfo activeNetworkInfo;
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || 1 != activeNetworkInfo.getType()) {
                return false;
            }
            return activeNetworkInfo.isConnected();
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    @android.annotation.SuppressLint({"MissingPermission"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isWifiState(android.content.Context r5) {
        /*
            r0 = 0
            if (r5 != 0) goto L4
            return r0
        L4:
            android.content.Context r1 = r5.getApplicationContext()
            java.lang.String r2 = "wifi"
            java.lang.Object r1 = r1.getSystemService(r2)
            android.net.wifi.WifiManager r1 = (android.net.wifi.WifiManager) r1
            r2 = 1
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Exception -> L25
            r4 = 23
            if (r3 < r4) goto L20
            java.lang.String r3 = "android.permission.ACCESS_WIFI_STATE"
            int r5 = r5.checkSelfPermission(r3)     // Catch: java.lang.Exception -> L25
            if (r5 != 0) goto L25
        L20:
            int r5 = r1.getWifiState()     // Catch: java.lang.Exception -> L25
            goto L26
        L25:
            r5 = r2
        L26:
            r1 = 3
            if (r5 != r1) goto L2a
            r0 = r2
        L2a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.util.NetworkUtil.isWifiState(android.content.Context):boolean");
    }

    public static void updateNetworkProxy(Context context) {
    }
}
