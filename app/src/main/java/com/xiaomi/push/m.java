package com.xiaomi.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes3.dex */
public class m {
    public static volatile int a = 0;

    /* renamed from: a  reason: collision with other field name */
    public static Map<String, q> f802a = null;
    public static int b = -1;

    /* JADX WARN: Removed duplicated region for block: B:13:0x0024  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int a() {
        /*
            int r0 = com.xiaomi.push.m.a
            if (r0 != 0) goto L47
            r0 = 0
            java.lang.String r1 = "ro.miui.ui.version.code"
            java.lang.String r1 = m2397a(r1)     // Catch: java.lang.Throwable -> L29
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> L29
            r2 = 1
            if (r1 == 0) goto L21
            java.lang.String r1 = "ro.miui.ui.version.name"
            java.lang.String r1 = m2397a(r1)     // Catch: java.lang.Throwable -> L29
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Throwable -> L29
            if (r1 != 0) goto L1f
            goto L21
        L1f:
            r1 = r0
            goto L22
        L21:
            r1 = r2
        L22:
            if (r1 == 0) goto L25
            goto L26
        L25:
            r2 = 2
        L26:
            com.xiaomi.push.m.a = r2     // Catch: java.lang.Throwable -> L29
            goto L31
        L29:
            r1 = move-exception
            java.lang.String r2 = "get isMIUI failed"
            com.xiaomi.channel.commonutils.logger.b.a(r2, r1)
            com.xiaomi.push.m.a = r0
        L31:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "isMIUI's value is: "
            r0.append(r1)
            int r1 = com.xiaomi.push.m.a
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.xiaomi.channel.commonutils.logger.b.b(r0)
        L47:
            int r0 = com.xiaomi.push.m.a
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.m.a():int");
    }

    public static int a(Context context) {
        String m2397a = m2397a("ro.miui.ui.version.code");
        if (TextUtils.isEmpty(m2397a) || !TextUtils.isDigitsOnly(m2397a)) {
            return 0;
        }
        return Integer.parseInt(m2397a);
    }

    public static q a(String str) {
        q b2 = b(str);
        return b2 == null ? q.Global : b2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m2396a() {
        int a2 = v.a();
        return (!m2399a() || a2 <= 0) ? "" : a2 < 2 ? "alpha" : a2 < 3 ? "development" : "stable";
    }

    public static String a(Intent intent) {
        if (intent == null) {
            return null;
        }
        return intent.toString() + " " + a(intent.getExtras());
    }

    public static String a(Bundle bundle) {
        String a2;
        StringBuilder sb = new StringBuilder("Bundle[");
        if (bundle == null) {
            sb.append("null");
        } else {
            boolean z = true;
            for (String str : bundle.keySet()) {
                if (!z) {
                    sb.append(", ");
                }
                sb.append(str);
                sb.append('=');
                Object obj = bundle.get(str);
                if (obj instanceof int[]) {
                    a2 = Arrays.toString((int[]) obj);
                } else if (obj instanceof byte[]) {
                    a2 = Arrays.toString((byte[]) obj);
                } else if (obj instanceof boolean[]) {
                    a2 = Arrays.toString((boolean[]) obj);
                } else if (obj instanceof short[]) {
                    a2 = Arrays.toString((short[]) obj);
                } else if (obj instanceof long[]) {
                    a2 = Arrays.toString((long[]) obj);
                } else if (obj instanceof float[]) {
                    a2 = Arrays.toString((float[]) obj);
                } else if (obj instanceof double[]) {
                    a2 = Arrays.toString((double[]) obj);
                } else if (obj instanceof String[]) {
                    a2 = Arrays.toString((String[]) obj);
                } else if (obj instanceof CharSequence[]) {
                    a2 = Arrays.toString((CharSequence[]) obj);
                } else if (obj instanceof Parcelable[]) {
                    a2 = Arrays.toString((Parcelable[]) obj);
                } else if (obj instanceof Bundle) {
                    a2 = a((Bundle) obj);
                } else {
                    sb.append(obj);
                    z = false;
                }
                sb.append(a2);
                z = false;
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m2397a(String str) {
        try {
            try {
                return (String) bk.a("android.os.SystemProperties", CallMethod.METHOD_GET, str, "");
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.d("fail to get property. " + e);
                return null;
            }
        } catch (Throwable unused) {
            return null;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m2398a() {
        if (f802a != null) {
            return;
        }
        HashMap hashMap = new HashMap();
        f802a = hashMap;
        hashMap.put("CN", q.China);
        Map<String, q> map = f802a;
        q qVar = q.Europe;
        map.put("FI", qVar);
        f802a.put("SE", qVar);
        f802a.put("NO", qVar);
        f802a.put("FO", qVar);
        f802a.put("EE", qVar);
        f802a.put("LV", qVar);
        f802a.put("LT", qVar);
        f802a.put("BY", qVar);
        f802a.put("MD", qVar);
        f802a.put("UA", qVar);
        f802a.put("PL", qVar);
        f802a.put("CZ", qVar);
        f802a.put("SK", qVar);
        f802a.put("HU", qVar);
        f802a.put("DE", qVar);
        f802a.put("AT", qVar);
        f802a.put("CH", qVar);
        f802a.put("LI", qVar);
        f802a.put("GB", qVar);
        f802a.put("IE", qVar);
        f802a.put("NL", qVar);
        f802a.put("BE", qVar);
        f802a.put("LU", qVar);
        f802a.put("FR", qVar);
        f802a.put("RO", qVar);
        f802a.put("BG", qVar);
        f802a.put("RS", qVar);
        f802a.put("MK", qVar);
        f802a.put("AL", qVar);
        f802a.put("GR", qVar);
        f802a.put("SI", qVar);
        f802a.put("HR", qVar);
        f802a.put("IT", qVar);
        f802a.put("SM", qVar);
        f802a.put("MT", qVar);
        f802a.put("ES", qVar);
        f802a.put("PT", qVar);
        f802a.put("AD", qVar);
        f802a.put("CY", qVar);
        f802a.put("DK", qVar);
        f802a.put("IS", qVar);
        f802a.put("UK", qVar);
        f802a.put("EL", qVar);
        f802a.put("RU", q.Russia);
        f802a.put("IN", q.India);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2399a() {
        return a() == 1;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2400a(Context context) {
        return context != null && m2401a(context.getPackageName());
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2401a(String str) {
        return com.xiaomi.stat.c.c.a.equals(str);
    }

    public static int b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(com.xiaomi.stat.c.c.a, 0).versionCode;
        } catch (Exception unused) {
            return 0;
        }
    }

    public static q b(String str) {
        m2398a();
        return f802a.get(str.toUpperCase());
    }

    public static String b() {
        String a2 = u.a("ro.miui.region", "");
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("persist.sys.oppo.region", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("ro.oppo.regionmark", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("ro.vendor.oplus.regionmark", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("ro.hw.country", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("ro.csc.countryiso_code", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = m2402b(u.a("ro.product.country.region", ""));
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("gsm.vivo.countrycode", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("persist.sys.oem.region", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("ro.product.locale.region", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = u.a("persist.sys.country", "");
        }
        if (!TextUtils.isEmpty(a2)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("get region from system, region = " + a2);
        }
        if (TextUtils.isEmpty(a2)) {
            String country = Locale.getDefault().getCountry();
            com.xiaomi.channel.commonutils.logger.b.m1859a("locale.default.country = " + country);
            return country;
        }
        return a2;
    }

    /* renamed from: b  reason: collision with other method in class */
    public static String m2402b(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("-");
            return split.length > 0 ? split[0] : str;
        }
        return str;
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m2403b() {
        return a() == 2;
    }

    public static String c() {
        return m2397a("ro.miui.ui.version.name");
    }

    /* renamed from: c  reason: collision with other method in class */
    public static boolean m2404c() {
        if (b < 0) {
            b = !m2406e() ? 1 : 0;
        }
        return b > 0;
    }

    public static String d() {
        return m2397a("ro.build.characteristics");
    }

    /* renamed from: d  reason: collision with other method in class */
    public static boolean m2405d() {
        return !q.China.name().equalsIgnoreCase(a(b()).name());
    }

    public static String e() {
        return m2397a("ro.product.manufacturer");
    }

    /* renamed from: e  reason: collision with other method in class */
    public static boolean m2406e() {
        String str = "";
        try {
            str = u.a("ro.miui.ui.version.code", str);
        } catch (Exception unused) {
        }
        return !TextUtils.isEmpty(str);
    }
}
