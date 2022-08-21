package com.xiaomi.stat.d;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes3.dex */
public class p {
    public static final String a = "netSpeed";
    public static final String b = "net_speed_time_stamp";
    private static final String c = "mi_stat_pref";
    private static SharedPreferences d = null;
    private static SharedPreferences.Editor e = null;
    private static final String f = "imei1";
    private static final String g = "imei2";
    private static final String h = "meid";
    private static final String i = "mac";
    private static final String j = "serial";
    private static final String k = "s_t";
    private static final String l = "l_t";
    private static final String m = "e_t";
    private static final String n = "od_checked";
    private static final String o = "od_v";
    private static final String p = "resued_old_instanced_id";
    private static final String q = "netSpeedTotalRxBytes";

    private static void o(Context context) {
        if (e != null) {
            return;
        }
        synchronized (p.class) {
            if (e == null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(c, 0);
                d = sharedPreferences;
                e = sharedPreferences.edit();
            }
        }
    }

    private static String a(Context context, String str, String str2) {
        o(context);
        return d.getString(str, str2);
    }

    private static void b(Context context, String str, String str2) {
        o(context);
        e.putString(str, str2).apply();
    }

    private static long a(Context context, String str, long j2) {
        o(context);
        return d.getLong(str, j2);
    }

    private static void b(Context context, String str, long j2) {
        o(context);
        e.putLong(str, j2).apply();
    }

    private static boolean a(Context context, String str, boolean z) {
        o(context);
        return d.getBoolean(str, z);
    }

    private static void b(Context context, String str, boolean z) {
        o(context);
        e.putBoolean(str, z).apply();
    }

    private static void a(Context context, String str, float f2) {
        o(context);
        e.putFloat(str, f2).apply();
    }

    private static float b(Context context, String str, float f2) {
        o(context);
        return d.getFloat(str, f2);
    }

    public static String a(Context context) {
        return a(context, f, "");
    }

    public static void a(Context context, String str) {
        b(context, f, str);
    }

    public static String b(Context context) {
        return a(context, g, "");
    }

    public static void b(Context context, String str) {
        b(context, g, str);
    }

    public static String c(Context context) {
        return a(context, h, "");
    }

    public static void c(Context context, String str) {
        b(context, h, str);
    }

    public static String d(Context context) {
        return a(context, i, "");
    }

    public static void d(Context context, String str) {
        b(context, i, str);
    }

    public static String e(Context context) {
        return a(context, j, "");
    }

    public static void e(Context context, String str) {
        b(context, j, str);
    }

    public static long f(Context context) {
        return a(context, k, 0L);
    }

    public static void a(Context context, long j2) {
        b(context, k, j2);
    }

    public static long g(Context context) {
        return a(context, l, 0L);
    }

    public static void b(Context context, long j2) {
        b(context, l, j2);
    }

    public static long h(Context context) {
        return a(context, m, 0L);
    }

    public static void c(Context context, long j2) {
        b(context, m, j2);
    }

    public static boolean i(Context context) {
        return a(context, n, false);
    }

    public static void a(Context context, boolean z) {
        b(context, n, z);
    }

    public static String j(Context context) {
        return a(context, o, (String) null);
    }

    public static void f(Context context, String str) {
        b(context, o, str);
    }

    public static boolean k(Context context) {
        return a(context, p, false);
    }

    public static void b(Context context, boolean z) {
        b(context, p, z);
    }

    public static void a(Context context, float f2) {
        a(context, a, f2);
    }

    public static float l(Context context) {
        return b(context, a, 0.0f);
    }

    public static void d(Context context, long j2) {
        b(context, q, j2);
    }

    public static long m(Context context) {
        return a(context, q, 0L);
    }

    public static void e(Context context, long j2) {
        b(context, b, j2);
    }

    public static long n(Context context) {
        return a(context, b, 0L);
    }
}
