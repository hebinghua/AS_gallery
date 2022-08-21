package com.baidu.lbsapi.auth;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class g {
    private Context a;
    private String b = null;
    private HashMap<String, String> c = null;
    private String d = null;

    public g(Context context) {
        this.a = context;
    }

    private String a(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 29) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities == null) {
                    return "wifi";
                }
                return networkCapabilities.hasTransport(1) ? "WIFI" : networkCapabilities.hasTransport(0) ? "CELLULAR" : networkCapabilities.hasTransport(3) ? "ETHERNET" : networkCapabilities.hasTransport(6) ? "LoWPAN" : networkCapabilities.hasTransport(4) ? "VPN" : networkCapabilities.hasTransport(5) ? "WifiAware" : "wifi";
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                String extraInfo = activeNetworkInfo.getExtraInfo();
                return extraInfo != null ? (extraInfo.trim().toLowerCase().equals("cmwap") || extraInfo.trim().toLowerCase().equals("uniwap") || extraInfo.trim().toLowerCase().equals("3gwap") || extraInfo.trim().toLowerCase().equals("ctwap")) ? extraInfo.trim().toLowerCase().equals("ctwap") ? "ctwap" : "cmwap" : "wifi" : "wifi";
            }
            return null;
        } catch (Exception e) {
            if (a.a) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:110:0x0189, code lost:
        if (com.baidu.lbsapi.auth.a.a == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x01b9, code lost:
        if (com.baidu.lbsapi.auth.a.a == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0158, code lost:
        if (com.baidu.lbsapi.auth.a.a == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x015a, code lost:
        r14.printStackTrace();
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0164 A[Catch: all -> 0x0128, TryCatch #11 {all -> 0x0128, blocks: (B:7:0x002f, B:88:0x012d, B:90:0x0131, B:91:0x0134, B:101:0x0160, B:103:0x0164, B:104:0x0167, B:114:0x018e, B:116:0x0192, B:117:0x0195), top: B:153:0x002f }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0192 A[Catch: all -> 0x0128, TryCatch #11 {all -> 0x0128, blocks: (B:7:0x002f, B:88:0x012d, B:90:0x0131, B:91:0x0134, B:101:0x0160, B:103:0x0164, B:104:0x0167, B:114:0x018e, B:116:0x0192, B:117:0x0195), top: B:153:0x002f }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01be A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:131:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x00f4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0151 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x01b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0182 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00b0 A[Catch: all -> 0x0103, TryCatch #2 {all -> 0x0103, blocks: (B:44:0x00ac, B:46:0x00b0, B:47:0x00cb), top: B:147:0x00ac }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0106 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0131 A[Catch: all -> 0x0128, TryCatch #11 {all -> 0x0128, blocks: (B:7:0x002f, B:88:0x012d, B:90:0x0131, B:91:0x0134, B:101:0x0160, B:103:0x0164, B:104:0x0167, B:114:0x018e, B:116:0x0192, B:117:0x0195), top: B:153:0x002f }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(javax.net.ssl.HttpsURLConnection r14) {
        /*
            Method dump skipped, instructions count: 546
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.lbsapi.auth.g.a(javax.net.ssl.HttpsURLConnection):void");
    }

    private static String b(HashMap<String, String> hashMap) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), Keyczar.DEFAULT_ENCODING));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), Keyczar.DEFAULT_ENCODING));
        }
        return sb.toString();
    }

    private HttpsURLConnection b() {
        String str;
        try {
            URL url = new URL(this.b);
            a.a("https URL: " + this.b);
            String a = a(this.a);
            if (a != null && !a.equals("")) {
                a.a("checkNetwork = " + a);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) (a.equals("cmwap") ? url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80))) : a.equals("ctwap") ? url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80))) : url.openConnection());
                httpsURLConnection.setHostnameVerifier(new h(this));
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setConnectTimeout(50000);
                httpsURLConnection.setReadTimeout(50000);
                return httpsURLConnection;
            }
            a.c("Current network is not available.");
            this.d = ErrorMessage.a(-10, "Current network is not available.");
            return null;
        } catch (MalformedURLException e) {
            if (a.a) {
                e.printStackTrace();
                a.a(e.getMessage());
            }
            str = "Auth server could not be parsed as a URL.";
            this.d = ErrorMessage.a(-11, str);
            return null;
        } catch (Exception e2) {
            if (a.a) {
                e2.printStackTrace();
                a.a(e2.getMessage());
            }
            str = "Init httpsurlconnection failed.";
            this.d = ErrorMessage.a(-11, str);
            return null;
        }
    }

    private HashMap<String, String> c(HashMap<String, String> hashMap) {
        HashMap<String, String> hashMap2 = new HashMap<>();
        for (String str : hashMap.keySet()) {
            String str2 = str.toString();
            hashMap2.put(str2, hashMap.get(str2));
        }
        return hashMap2;
    }

    public String a(HashMap<String, String> hashMap) {
        HashMap<String, String> c = c(hashMap);
        this.c = c;
        this.b = c.get(MapBundleKey.MapObjKey.OBJ_URL);
        HttpsURLConnection b = b();
        if (b == null) {
            a.c("syncConnect failed,httpsURLConnection is null");
        } else {
            a(b);
        }
        return this.d;
    }

    public boolean a() {
        a.a("checkNetwork start");
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.a.getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 29) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return networkCapabilities != null && networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16);
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                return false;
            }
            a.a("checkNetwork end");
            return true;
        } catch (Exception e) {
            if (a.a) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
