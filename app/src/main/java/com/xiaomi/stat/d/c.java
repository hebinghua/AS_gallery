package com.xiaomi.stat.d;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.xiaomi.stat.ak;

/* loaded from: classes3.dex */
public class c {
    private static boolean a;
    private static int b;
    private static String c;

    public static int a() {
        if (!a) {
            c();
        }
        return b;
    }

    public static String b() {
        if (!a) {
            c();
        }
        return c;
    }

    private static void c() {
        if (a) {
            return;
        }
        a = true;
        Context a2 = ak.a();
        try {
            PackageInfo packageInfo = a2.getPackageManager().getPackageInfo(a2.getPackageName(), 0);
            b = packageInfo.versionCode;
            c = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }
}
