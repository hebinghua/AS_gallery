package com.xiaomi.push.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.eo;
import com.xiaomi.push.fl;
import com.xiaomi.push.gh;
import com.xiaomi.push.gk;
import com.xiaomi.push.gm;
import com.xiaomi.push.gn;
import com.xiaomi.push.hb;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.hz;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import com.xiaomi.push.service.al;
import com.xiaomi.push.service.bg;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class y {
    public static Intent a(byte[] bArr, long j) {
        Cif a = a(bArr);
        if (a == null) {
            return null;
        }
        Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.putExtra("mipush_payload", bArr);
        intent.putExtra("mrt", Long.toString(j));
        intent.setPackage(a.f616b);
        return intent;
    }

    public static Cif a(Context context, Cif cif) {
        return a(context, cif, (Map<String, String>) null);
    }

    public static Cif a(Context context, Cif cif, Map<String, String> map) {
        hz hzVar = new hz();
        hzVar.b(cif.m2294a());
        hw m2293a = cif.m2293a();
        if (m2293a != null) {
            hzVar.a(m2293a.m2259a());
            hzVar.a(m2293a.m2257a());
            if (!TextUtils.isEmpty(m2293a.m2264b())) {
                hzVar.c(m2293a.m2264b());
            }
        }
        hzVar.a(it.a(context, cif));
        Cif a = ah.a(cif.b(), cif.m2294a(), hzVar, hj.AckMessage);
        hw m2293a2 = cif.m2293a();
        if (m2293a2 != null) {
            m2293a2 = br.a(m2293a2.m2258a());
        }
        m2293a2.a("mat", Long.toString(System.currentTimeMillis()));
        if (map != null) {
            try {
                if (map.size() > 0) {
                    for (String str : map.keySet()) {
                        m2293a2.a(str, map.get(str));
                    }
                }
            } catch (Throwable unused) {
            }
        }
        a.a(m2293a2);
        return a;
    }

    public static Cif a(byte[] bArr) {
        Cif cif = new Cif();
        try {
            it.a(cif, bArr);
            return cif;
        } catch (Throwable th) {
            com.xiaomi.channel.commonutils.logger.b.a(th);
            return null;
        }
    }

    public static void a(Context context, Cif cif, byte[] bArr) {
        try {
            al.a(cif);
            cif.m2293a();
            al.c m2459a = al.m2459a(context, cif, bArr);
            if (m2459a.a > 0 && !TextUtils.isEmpty(m2459a.f875a)) {
                hb.a(context, m2459a.f875a, m2459a.a, true, false, System.currentTimeMillis());
            }
            if (!com.xiaomi.push.m.m2400a(context) || !ag.a(context, cif, m2459a.f876a)) {
                b(context, cif, bArr);
                return;
            }
            ag.m2454a(context, cif);
            com.xiaomi.channel.commonutils.logger.b.m1859a("consume this broadcast by tts");
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("notify push msg error " + e);
            e.printStackTrace();
        }
    }

    public static void a(XMPushService xMPushService, Cif cif) {
        xMPushService.a(new z(4, xMPushService, cif));
    }

    public static void a(XMPushService xMPushService, Cif cif, ii iiVar) {
        xMPushService.a(new af(4, iiVar, cif, xMPushService));
    }

    public static void a(XMPushService xMPushService, Cif cif, String str) {
        xMPushService.a(new ad(4, xMPushService, cif, str));
    }

    public static void a(XMPushService xMPushService, Cif cif, String str, String str2) {
        xMPushService.a(new ae(4, xMPushService, cif, str, str2));
    }

    /* JADX WARN: Removed duplicated region for block: B:137:0x03c2  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x03dc  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0426  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x03a4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void a(com.xiaomi.push.service.XMPushService r19, java.lang.String r20, byte[] r21, android.content.Intent r22) {
        /*
            Method dump skipped, instructions count: 1204
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.y.a(com.xiaomi.push.service.XMPushService, java.lang.String, byte[], android.content.Intent):void");
    }

    public static void a(XMPushService xMPushService, byte[] bArr, long j) {
        Map<String, String> m2260a;
        Cif a = a(bArr);
        if (a == null) {
            return;
        }
        if (TextUtils.isEmpty(a.f616b)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("receive a mipush message without package name");
            return;
        }
        Long valueOf = Long.valueOf(System.currentTimeMillis());
        Intent a2 = a(bArr, valueOf.longValue());
        String a3 = al.a(a);
        hb.a(xMPushService, a3, j, true, true, System.currentTimeMillis());
        hw m2293a = a.m2293a();
        if (m2293a != null && m2293a.m2259a() != null) {
            com.xiaomi.channel.commonutils.logger.b.e(String.format("receive a message. appid=%1$s, msgid= %2$s, action=%3$s", a.m2294a(), bd.a(m2293a.m2259a()), a.a()));
        }
        if (m2293a != null) {
            m2293a.a("mrt", Long.toString(valueOf.longValue()));
        }
        hj hjVar = hj.SendMessage;
        String str = "";
        if (hjVar == a.a() && v.a(xMPushService).m2546a(a.f616b) && !al.m2463a(a)) {
            if (m2293a != null) {
                str = m2293a.m2259a();
                if (al.e(a)) {
                    eo.a(xMPushService.getApplicationContext()).a(a.b(), al.b(a), str, "1");
                }
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("Drop a message for unregistered, msgid=" + str);
            a(xMPushService, a, a.f616b);
        } else if (hjVar == a.a() && v.a(xMPushService).m2548c(a.f616b) && !al.m2463a(a)) {
            if (m2293a != null) {
                str = m2293a.m2259a();
                if (al.e(a)) {
                    eo.a(xMPushService.getApplicationContext()).a(a.b(), al.b(a), str, "2");
                }
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("Drop a message for push closed, msgid=" + str);
            a(xMPushService, a, a.f616b);
        } else if (hjVar == a.a() && !TextUtils.equals(xMPushService.getPackageName(), com.xiaomi.stat.c.c.a) && !TextUtils.equals(xMPushService.getPackageName(), a.f616b)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Receive a message with wrong package name, expect " + xMPushService.getPackageName() + ", received " + a.f616b);
            a(xMPushService, a, "unmatched_package", "package should be " + xMPushService.getPackageName() + ", but got " + a.f616b);
            if (m2293a == null || !al.e(a)) {
                return;
            }
            eo.a(xMPushService.getApplicationContext()).a(a.b(), al.b(a), m2293a.m2259a(), "3");
        } else if (hjVar != a.a() || com.xiaomi.push.j.a() != 999 || !com.xiaomi.push.j.a(xMPushService, a3)) {
            if (m2293a == null || (m2260a = m2293a.m2260a()) == null || !m2260a.containsKey("hide") || !"true".equalsIgnoreCase(m2260a.get("hide"))) {
                a(xMPushService, a3, bArr, a2);
            } else {
                b(xMPushService, a);
            }
        } else {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Receive the uninstalled dual app message");
            try {
                ah.a(xMPushService, ah.a(a3, a.m2294a()));
                com.xiaomi.channel.commonutils.logger.b.m1859a("uninstall " + a3 + " msg sent");
            } catch (gh e) {
                com.xiaomi.channel.commonutils.logger.b.d("Fail to send Message: " + e.getMessage());
                xMPushService.a(10, e);
            }
            al.m2460a((Context) xMPushService, a3);
        }
    }

    public static boolean a(Context context, Intent intent) {
        try {
            List<ResolveInfo> queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 32);
            if (queryBroadcastReceivers != null) {
                if (!queryBroadcastReceivers.isEmpty()) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return true;
        }
    }

    public static boolean a(Context context, String str) {
        Intent intent = new Intent("com.xiaomi.mipush.miui.CLICK_MESSAGE");
        intent.setPackage(str);
        Intent intent2 = new Intent("com.xiaomi.mipush.miui.RECEIVE_MESSAGE");
        intent2.setPackage(str);
        PackageManager packageManager = context.getPackageManager();
        try {
            List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent2, 32);
            List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(intent, 32);
            if (queryBroadcastReceivers.isEmpty()) {
                if (queryIntentServices.isEmpty()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return false;
        }
    }

    public static boolean a(Context context, String str, byte[] bArr) {
        if (com.xiaomi.push.h.m2215a(context, str)) {
            Intent intent = new Intent("com.xiaomi.mipush.MESSAGE_ARRIVED");
            intent.putExtra("mipush_payload", bArr);
            intent.setPackage(str);
            try {
                if (context.getPackageManager().queryBroadcastReceivers(intent, 0).isEmpty()) {
                    return false;
                }
                com.xiaomi.channel.commonutils.logger.b.m1859a("broadcast message arrived.");
                context.sendBroadcast(intent, ah.a(str));
                return true;
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("meet error when broadcast message arrived. " + e);
                return false;
            }
        }
        return false;
    }

    public static boolean a(Cif cif) {
        return com.xiaomi.stat.c.c.a.equals(cif.f616b) && cif.m2293a() != null && cif.m2293a().m2260a() != null && cif.m2293a().m2260a().containsKey("miui_package_name");
    }

    public static boolean a(XMPushService xMPushService, String str, Cif cif, hw hwVar) {
        boolean z = true;
        if (hwVar != null && hwVar.m2260a() != null && hwVar.m2260a().containsKey("__check_alive") && hwVar.m2260a().containsKey("__awake")) {
            ii iiVar = new ii();
            iiVar.b(cif.m2294a());
            iiVar.d(str);
            iiVar.c(ht.AwakeSystemApp.f489a);
            iiVar.a(hwVar.m2259a());
            iiVar.f628a = new HashMap();
            boolean m2215a = com.xiaomi.push.h.m2215a(xMPushService.getApplicationContext(), str);
            iiVar.f628a.put("app_running", Boolean.toString(m2215a));
            if (!m2215a) {
                boolean parseBoolean = Boolean.parseBoolean(hwVar.m2260a().get("__awake"));
                iiVar.f628a.put("awaked", Boolean.toString(parseBoolean));
                if (!parseBoolean) {
                    z = false;
                }
            }
            try {
                ah.a(xMPushService, ah.a(cif.b(), cif.m2294a(), iiVar, hj.Notification));
            } catch (gh e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
            }
        }
        return z;
    }

    public static void b(Context context, Cif cif, byte[] bArr) {
        if (al.m2463a(cif)) {
            return;
        }
        String a = al.a(cif);
        if (TextUtils.isEmpty(a) || a(context, a, bArr)) {
            return;
        }
        eo.a(context).b(a, al.b(cif), cif.m2293a().m2259a(), "1");
    }

    public static void b(XMPushService xMPushService, Cif cif) {
        xMPushService.a(new aa(4, xMPushService, cif));
    }

    public static boolean b(Cif cif) {
        Map<String, String> m2260a = cif.m2293a().m2260a();
        return m2260a != null && m2260a.containsKey("notify_effect");
    }

    public static void c(XMPushService xMPushService, Cif cif) {
        xMPushService.a(new ab(4, xMPushService, cif));
    }

    public static boolean c(Cif cif) {
        if (cif.m2293a() == null || cif.m2293a().m2260a() == null) {
            return false;
        }
        return "1".equals(cif.m2293a().m2260a().get("obslete_ads_message"));
    }

    public static void d(XMPushService xMPushService, Cif cif) {
        xMPushService.a(new ac(4, xMPushService, cif));
    }

    public void a(Context context, bg.b bVar, boolean z, int i, String str) {
        t m2542a;
        if (z || (m2542a = u.m2542a(context)) == null || !"token-expired".equals(str)) {
            return;
        }
        u.a(context, m2542a.f, m2542a.d, m2542a.e);
    }

    public void a(XMPushService xMPushService, fl flVar, bg.b bVar) {
        try {
            a(xMPushService, flVar.m2163a(bVar.h), flVar.c());
        } catch (IllegalArgumentException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
    }

    public void a(XMPushService xMPushService, gn gnVar, bg.b bVar) {
        if (!(gnVar instanceof gm)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("not a mipush message");
            return;
        }
        gm gmVar = (gm) gnVar;
        gk a = gmVar.a("s");
        if (a == null) {
            return;
        }
        try {
            a(xMPushService, bp.a(bp.a(bVar.h, gmVar.j()), a.c()), hb.a(gnVar.m2199a()));
        } catch (IllegalArgumentException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
    }
}
