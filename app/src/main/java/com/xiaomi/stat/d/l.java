package com.xiaomi.stat.d;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.stat.ak;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class l {
    private static final String a = "NetWorkStateUtil";
    private static final int b = 16;
    private static final int c = 17;
    private static final int d = 18;
    private static final int e = 19;
    private static final String f = "2G";
    private static final String g = "3G";
    private static final String h = "4G";
    private static final String i = "WIFI";
    private static final String j = "ETHERNET";
    private static final String k = "UNKNOWN";
    private static final String l = "NOT_CONNECTED";

    public static int a(Context context) {
        if (context == null) {
            return 0;
        }
        String b2 = b(context);
        if (!TextUtils.isEmpty(b2) && !b2.equals(l)) {
            if (b2.equals(f)) {
                return 1;
            }
            if (b2.equals(g)) {
                return 2;
            }
            if (b2.equals(h)) {
                return 4;
            }
            if (b2.startsWith(i)) {
                return 8;
            }
            if (b2.equals(j)) {
                return 16;
            }
        }
        return 0;
    }

    public static boolean a() {
        Context a2 = ak.a();
        if (a2 != null) {
            try {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) a2.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }
                return activeNetworkInfo.isConnectedOrConnecting();
            } catch (Exception unused) {
                k.b("isNetworkConnected exception");
                return false;
            }
        }
        return false;
    }

    public static String b(Context context) {
        try {
        } catch (Exception e2) {
            k.d(a, "getNetworkState error", e2);
        }
        if (e.w(context)) {
            return a.a(context);
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == 1) {
                return i;
            }
            if (activeNetworkInfo.getType() == 0) {
                switch (activeNetworkInfo.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                    case 16:
                        return f;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                    case 17:
                        return g;
                    case 13:
                    case 18:
                    case 19:
                        return h;
                    default:
                        return k;
                }
            }
            return k;
        }
        return l;
    }

    /* loaded from: classes3.dex */
    public static class a {
        private static boolean a(int i) {
            return i > 4900 && i < 5900;
        }

        private static boolean b(int i) {
            return i > 2400 && i < 2500;
        }

        private a() {
        }

        public static String a(Context context) {
            NetworkInfo activeNetworkInfo;
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnected()) {
                return l.l;
            }
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(9);
            if (networkInfo == null || !networkInfo.isConnected()) {
                return (networkInfo2 == null || !networkInfo2.isConnected()) ? l.k : l.j;
            }
            return l.i + b(context);
        }

        private static String b(Context context) {
            try {
                if (Build.VERSION.SDK_INT >= 22) {
                    int frequency = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getFrequency();
                    if (!a(frequency)) {
                        if (!b(frequency)) {
                            return "";
                        }
                        return l.f;
                    }
                    return "5G";
                }
                char c = 65535;
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                String ssid = wifiManager.getConnectionInfo().getSSID();
                String substring = ssid.substring(1, ssid.length() - 1);
                if (ssid.length() > 2) {
                    Iterator<ScanResult> it = wifiManager.getScanResults().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        ScanResult next = it.next();
                        if (next.SSID.equals(substring)) {
                            if (a(next.frequency)) {
                                c = 2;
                            } else if (b(next.frequency)) {
                                c = 1;
                            }
                        }
                    }
                }
                if (c != 2) {
                    if (c != 1) {
                        return "";
                    }
                    return l.f;
                }
                return "5G";
            } catch (Exception e) {
                k.d(l.a, "getWifiFreeBand error", e);
                return "";
            }
        }
    }
}
