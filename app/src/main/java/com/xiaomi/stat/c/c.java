package com.xiaomi.stat.c;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.xiaomi.stat.ak;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes3.dex */
public class c {
    public static final String a = "com.xiaomi.xmsf";
    public static final String b = "com.xiaomi.xmsf.push.service.HttpService";
    private static final String c = "UploadMode";

    public static String a(String str, Map<String, String> map, boolean z) throws IOException {
        if (com.xiaomi.stat.b.u() && a()) {
            return b(str, map, z);
        }
        return com.xiaomi.stat.d.i.b(str, map, z);
    }

    public static String b(String str, Map<String, String> map, boolean z) {
        if (z) {
            map.put(com.xiaomi.stat.d.f, com.xiaomi.stat.d.i.a(map));
        }
        Intent intent = new Intent();
        intent.setClassName(a, b);
        Context a2 = ak.a();
        String[] strArr = new String[1];
        if (a2 == null) {
            return strArr[0];
        }
        ServiceConnection a3 = a(str, map, strArr);
        boolean bindService = a2.bindService(intent, a3, 1);
        synchronized (i.class) {
            try {
                i.class.wait(15000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!bindService) {
            strArr[0] = null;
        }
        if (bindService) {
            a2.unbindService(a3);
        }
        return strArr[0];
    }

    private static ServiceConnection a(String str, Map<String, String> map, String[] strArr) {
        return new d(strArr, str, map);
    }

    public static boolean a() {
        boolean z;
        boolean z2;
        PackageInfo packageInfo;
        PackageInfo packageInfo2;
        Signature[] signatureArr;
        Signature[] signatureArr2;
        Context a2 = ak.a();
        if (a2 == null) {
            return false;
        }
        try {
            z = (a2.getApplicationInfo().flags & 1) == 1;
            try {
                PackageManager packageManager = a2.getPackageManager();
                packageInfo = packageManager.getPackageInfo(a2.getPackageName(), 64);
                packageInfo2 = packageManager.getPackageInfo("android", 64);
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            z = false;
        }
        if (packageInfo != null && (signatureArr = packageInfo.signatures) != null && signatureArr.length > 0 && packageInfo2 != null && (signatureArr2 = packageInfo2.signatures) != null && signatureArr2.length > 0) {
            z2 = signatureArr2[0].equals(signatureArr[0]);
            return !z || z2;
        }
        z2 = false;
        if (!z) {
        }
    }
}
