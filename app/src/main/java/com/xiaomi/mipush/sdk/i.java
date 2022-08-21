package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.bk;
import com.xiaomi.push.bo;
import com.xiaomi.push.service.ba;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class i {
    public static HashMap<String, String> a = new HashMap<>();

    public static int a() {
        Integer num = (Integer) bk.a("com.xiaomi.assemble.control.AssembleConstants", "ASSEMBLE_VERSION_CODE");
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public static String a(Context context, e eVar) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        String a2 = a(eVar);
        if (!TextUtils.isEmpty(a2)) {
            return sharedPreferences.getString(a2, "");
        }
        return null;
    }

    public static synchronized String a(Context context, String str) {
        String str2;
        synchronized (i.class) {
            str2 = a.get(str);
            if (TextUtils.isEmpty(str2)) {
                str2 = "";
            }
        }
        return str2;
    }

    public static String a(e eVar) {
        int i = k.a[eVar.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return "fcm_push_token_v2";
            }
            if (i == 3) {
                return "cos_push_token";
            }
            if (i == 4) {
                return "ftos_push_token";
            }
            return null;
        }
        return "hms_push_token";
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0058, code lost:
        if (r12 != 0) goto L15;
     */
    /* renamed from: a  reason: collision with other method in class */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.HashMap<java.lang.String, java.lang.String> m1922a(android.content.Context r11, com.xiaomi.mipush.sdk.e r12) {
        /*
            Method dump skipped, instructions count: 309
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.i.m1922a(android.content.Context, com.xiaomi.mipush.sdk.e):java.util.HashMap");
    }

    public static void a(Context context) {
        boolean z = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        String a2 = a(e.ASSEMBLE_PUSH_HUAWEI);
        String a3 = a(e.ASSEMBLE_PUSH_FCM);
        if (!TextUtils.isEmpty(sharedPreferences.getString(a2, "")) && TextUtils.isEmpty(sharedPreferences.getString(a3, ""))) {
            z = true;
        }
        if (z) {
            ao.a(context).a(2, a2);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m1923a(Context context, e eVar) {
        if (l.m1926a(eVar) != null) {
            return ba.a(context).a(l.m1926a(eVar).a(), true);
        }
        return false;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m1924a(e eVar) {
        return eVar == e.ASSEMBLE_PUSH_FTOS || eVar == e.ASSEMBLE_PUSH_FCM;
    }

    public static boolean a(Cif cif, e eVar) {
        if (cif == null || cif.m2293a() == null || cif.m2293a().m2260a() == null) {
            return false;
        }
        return (eVar == e.ASSEMBLE_PUSH_FCM ? "FCM" : "").equalsIgnoreCase(cif.m2293a().m2260a().get("assemble_push_type"));
    }

    public static byte[] a(Context context, Cif cif, e eVar) {
        if (a(cif, eVar)) {
            return bo.m1978a(a(context, eVar));
        }
        return null;
    }

    public static String b(e eVar) {
        return a(eVar) + "_version";
    }

    public static void b(Context context) {
        f.a(context).register();
    }

    public static void b(Context context, e eVar, String str) {
        com.xiaomi.push.al.a(context).a(new j(str, context, eVar));
    }

    public static void c(Context context) {
        f.a(context).unregister();
    }

    public static synchronized void d(Context context, e eVar, String str) {
        synchronized (i.class) {
            String a2 = a(eVar);
            if (TextUtils.isEmpty(a2)) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : can not find the key of token used in sp file");
                return;
            }
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putString(a2, str).putString("last_check_token", b.m1906a(context).c());
            if (m1924a(eVar)) {
                edit.putInt(b(eVar), a());
            }
            com.xiaomi.push.t.a(edit);
            com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : update sp file success!  " + str);
        }
    }
}
