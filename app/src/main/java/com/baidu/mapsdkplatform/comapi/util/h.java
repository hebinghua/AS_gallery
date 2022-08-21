package com.baidu.mapsdkplatform.comapi.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import com.baidu.lbsapi.auth.LBSAuthManager;
import com.baidu.mapapi.VersionInfo;
import com.baidu.mapsdkplatform.comjni.util.AppMD5;
import com.baidu.platform.comapi.util.JsonBuilder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes.dex */
public class h {
    public static Context a = null;
    public static String d = null;
    private static final String e = "h";
    private static String h;
    private static String i;
    private static String j;
    private static String k;
    private static int l;
    private static int m;
    private static int n;
    private static int o;
    private static int p;
    private static int q;
    private static String r;
    private static String w;
    private static String x;
    private static com.baidu.mapsdkplatform.comjni.util.a f = new com.baidu.mapsdkplatform.comjni.util.a();
    private static String g = "02";
    private static String s = "baidu";
    private static String t = "";
    private static String u = "";
    private static String v = "";
    private static String y = "-1";
    private static String z = "-1";
    public static final int b = Integer.parseInt(Build.VERSION.SDK);
    public static float c = 1.0f;
    private static String A = "";
    private static Map<String, String> B = new HashMap();
    private static String C = "";

    public static void a() {
        e();
    }

    public static void a(String str) {
        r = str;
        g();
    }

    public static void a(String str, String str2) {
        y = str2;
        z = str;
        g();
    }

    public static byte[] a(Context context) {
        Signature[] signatureArr;
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                SigningInfo signingInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), Box.MAX_BOX_SIZE).signingInfo;
                if (signingInfo == null) {
                    return null;
                }
                signatureArr = signingInfo.hasMultipleSigners() ? context.getPackageManager().getPackageInfo(context.getPackageName(), Box.MAX_BOX_SIZE).signingInfo.getApkContentsSigners() : context.getPackageManager().getPackageInfo(context.getPackageName(), Box.MAX_BOX_SIZE).signingInfo.getSigningCertificateHistory();
            } else {
                signatureArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures;
            }
            if (signatureArr != null && signatureArr.length > 0) {
                return signatureArr[0].toByteArray();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static String b() {
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.object();
        jsonBuilder.putStringValue("cpu", t);
        jsonBuilder.putStringValue("resid", g);
        jsonBuilder.putStringValue("channel", s);
        jsonBuilder.putStringValue("glr", u);
        jsonBuilder.putStringValue("glv", v);
        jsonBuilder.putStringValue("mb", h());
        jsonBuilder.putStringValue(com.xiaomi.stat.d.b, j());
        jsonBuilder.putStringValue(com.xiaomi.stat.d.l, l());
        jsonBuilder.key("dpi_x").value(m());
        jsonBuilder.key("dpi_y").value(m());
        jsonBuilder.putStringValue("net", r);
        jsonBuilder.putStringValue("cuid", A);
        jsonBuilder.key("signature").arrayValue();
        byte[] a2 = a(a);
        if (a2 != null) {
            for (byte b2 : a2) {
                jsonBuilder.value((int) b2);
            }
        }
        jsonBuilder.endArrayValue();
        jsonBuilder.putStringValue("pcn", a.getPackageName());
        jsonBuilder.key("screen_x").value(i());
        jsonBuilder.key("screen_y").value(k());
        jsonBuilder.endObject();
        String json = jsonBuilder.getJson();
        C = json;
        return json;
    }

    public static void b(Context context) {
        a = context;
        if (context.getFilesDir() != null) {
            w = context.getFilesDir().getAbsolutePath();
        }
        if (context.getCacheDir() != null) {
            x = context.getCacheDir().getAbsolutePath();
        }
        i = Build.MODEL;
        j = "Android" + Build.VERSION.SDK;
        h = context.getPackageName();
        c(context);
        d(context);
        r();
        A = q();
        B.put("resid", AppMD5.encodeUrlParamsValue(g));
        B.put("channel", AppMD5.encodeUrlParamsValue(n()));
        B.put("mb", AppMD5.encodeUrlParamsValue(h()));
        B.put(com.xiaomi.stat.d.b, AppMD5.encodeUrlParamsValue(j()));
        B.put(com.xiaomi.stat.d.l, AppMD5.encodeUrlParamsValue(l()));
        B.put("dpi", AppMD5.encodeUrlParamsValue(String.format("%d,%d", Integer.valueOf(m()), Integer.valueOf(m()))));
        B.put("cuid", AppMD5.encodeUrlParamsValue(A));
        B.put("pcn", AppMD5.encodeUrlParamsValue(a.getPackageName()));
        B.put("screen", AppMD5.encodeUrlParamsValue(String.format("%d,%d", Integer.valueOf(i()), Integer.valueOf(k()))));
        com.baidu.mapsdkplatform.comjni.util.a aVar = f;
        if (aVar != null) {
            aVar.a();
        }
    }

    public static String c() {
        return C;
    }

    private static void c(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String apiVersion = VersionInfo.getApiVersion();
            k = apiVersion;
            if (apiVersion != null && !apiVersion.equals("")) {
                k = k.replace('_', CoreConstants.DOT);
            }
            l = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException unused) {
            k = "1.0.0";
            l = 1;
        }
    }

    public static String d() {
        if (B == null) {
            return null;
        }
        Date date = new Date();
        long time = date.getTime() + (date.getSeconds() * 1000);
        B.put("ctm", AppMD5.encodeUrlParamsValue(String.format("%f", Double.valueOf((time / 1000) + ((time % 1000) / 1000.0d)))));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : B.entrySet()) {
            sb.append("&");
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }

    private static void d(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display defaultDisplay = windowManager != null ? windowManager.getDefaultDisplay() : null;
        if (defaultDisplay != null) {
            m = defaultDisplay.getWidth();
            n = defaultDisplay.getHeight();
            defaultDisplay.getMetrics(displayMetrics);
        }
        c = displayMetrics.density;
        o = (int) displayMetrics.xdpi;
        p = (int) displayMetrics.ydpi;
        if (b > 3) {
            q = displayMetrics.densityDpi;
        } else {
            q = SyslogConstants.LOG_LOCAL4;
        }
        if (q == 0) {
            q = SyslogConstants.LOG_LOCAL4;
        }
    }

    public static void e() {
        com.baidu.mapsdkplatform.comjni.util.a aVar = f;
        if (aVar != null) {
            aVar.b();
        }
    }

    public static String f() {
        return r;
    }

    public static void g() {
        B.put("net", AppMD5.encodeUrlParamsValue(f()));
        B.put("appid", AppMD5.encodeUrlParamsValue(y));
        B.put("bduid", "");
        JsonBuilder jsonBuilder = new JsonBuilder();
        jsonBuilder.object();
        jsonBuilder.putStringValue("cpu", t);
        jsonBuilder.putStringValue("resid", g);
        jsonBuilder.putStringValue("channel", s);
        jsonBuilder.putStringValue("glr", u);
        jsonBuilder.putStringValue("glv", v);
        jsonBuilder.putStringValue("mb", h());
        jsonBuilder.putStringValue(com.xiaomi.stat.d.b, j());
        jsonBuilder.putStringValue(com.xiaomi.stat.d.l, l());
        jsonBuilder.key("dpi_x").value(m());
        jsonBuilder.key("dpi_y").value(m());
        jsonBuilder.putStringValue("net", r);
        jsonBuilder.putStringValue("cuid", A);
        jsonBuilder.putStringValue("pcn", a.getPackageName());
        jsonBuilder.key("screen_x").value(i());
        jsonBuilder.key("screen_y").value(k());
        jsonBuilder.putStringValue("appid", y);
        jsonBuilder.putStringValue("duid", z);
        if (!TextUtils.isEmpty(d)) {
            jsonBuilder.putStringValue("token", d);
        }
        jsonBuilder.endObject();
        SysUpdateObservable.getInstance().updatePhoneInfo(jsonBuilder.getJson());
    }

    public static String h() {
        return i;
    }

    public static int i() {
        return m;
    }

    public static String j() {
        return k;
    }

    public static int k() {
        return n;
    }

    public static String l() {
        return j;
    }

    public static int m() {
        return q;
    }

    public static String n() {
        return s;
    }

    public static String o() {
        return h;
    }

    public static String p() {
        return w;
    }

    public static String q() {
        String str;
        try {
            str = LBSAuthManager.getInstance(a).getCUID();
        } catch (Exception unused) {
            str = "";
        }
        return str == null ? "" : str;
    }

    private static void r() {
        r = "0";
    }
}
