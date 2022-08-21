package com.xiaomi.onetrack.e;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.xiaomi.onetrack.util.j;

/* loaded from: classes3.dex */
public class a {
    public static Context a = null;
    public static Context b = null;
    public static int c = 0;
    public static String d = null;
    public static String e = null;
    public static long f = 0;
    public static volatile boolean g = false;

    public static void a(Context context) {
        if (g) {
            return;
        }
        synchronized (a.class) {
            if (g) {
                return;
            }
            a = context;
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(a.getPackageName(), 0);
                c = packageInfo.versionCode;
                d = packageInfo.versionName;
                f = packageInfo.lastUpdateTime;
                e = a.getPackageName();
            } catch (PackageManager.NameNotFoundException e2) {
                e2.printStackTrace();
            }
            g = true;
        }
    }

    public static Context a() {
        if (j.d(a)) {
            Context context = b;
            if (context != null) {
                return context;
            }
            synchronized (a.class) {
                if (b == null) {
                    b = j.a(a);
                }
            }
            return b;
        }
        return a;
    }

    public static Context b() {
        return a;
    }

    public static String c() {
        return d;
    }

    public static int d() {
        return c;
    }

    public static String e() {
        return e;
    }

    public static long f() {
        return f;
    }
}
