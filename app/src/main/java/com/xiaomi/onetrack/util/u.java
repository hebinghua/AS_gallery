package com.xiaomi.onetrack.util;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.onetrack.e.a;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes3.dex */
public class u {
    public static Set<String> c;

    static {
        try {
            HashSet hashSet = new HashSet();
            c = hashSet;
            hashSet.add("android");
            c.add("com.miui.analytics");
            c.add("com.miui.cit");
            c.add("com.xiaomi.finddevice");
            c.add("com.miui.securitycenter");
            c.add("com.android.settings");
            c.add("com.android.vending");
            c.add("com.google.android.gms");
            c.add("com.xiaomi.factory.mmi");
            c.add("com.miui.qr");
            c.add("com.android.contacts");
            c.add("com.qualcomm.qti.autoregistration");
            c.add("com.miui.tsmclient");
            c.add("com.miui.sekeytool");
            c.add("com.android.updater");
            if (!"cn_chinamobile".equals(ab.a("ro.miui.cust_variant")) && !"cn_chinatelecom".equals(ab.a("ro.miui.cust_variant"))) {
                return;
            }
            c.add("com.mobiletools.systemhelper");
            c.add("com.miui.dmregservice");
        } catch (Exception e) {
            Log.e("PermissionUtil", "static initializer: " + e.toString());
        }
    }

    public static boolean a(Context context) {
        if (Build.VERSION.SDK_INT < 29) {
            return a(context, "android.permission.READ_PHONE_STATE");
        }
        if (!a()) {
            return a(context, "android.permission.READ_PRIVILEGED_PHONE_STATE");
        }
        return a(a.e()) && a(context, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    public static boolean b(Context context) {
        if (Build.VERSION.SDK_INT < 29) {
            return a(context, "android.permission.READ_PHONE_STATE");
        }
        return a(context, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }

    public static boolean a() {
        try {
            if (!q.a() || q.x()) {
                return false;
            }
            return "1".equals(ab.a("ro.miui.restrict_imei"));
        } catch (Exception e) {
            p.b("PermissionUtil", "isRestrictIMEI " + e.toString());
            return false;
        }
    }

    public static boolean a(String str) {
        Set<String> set;
        return !TextUtils.isEmpty(str) && (set = c) != null && set.contains(str);
    }

    public static boolean a(Context context, String str) {
        return context.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }
}
