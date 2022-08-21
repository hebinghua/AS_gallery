package com.xiaomi.push.service;

import android.content.Context;
import android.os.Messenger;
import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.db;
import com.xiaomi.push.fl;
import com.xiaomi.push.fw;
import com.xiaomi.push.gh;
import com.xiaomi.push.gn;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.hy;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import com.xiaomi.push.iu;
import com.xiaomi.push.iz;
import com.xiaomi.push.service.bg;
import java.nio.ByteBuffer;
import java.util.Map;

/* loaded from: classes3.dex */
public final class ah {
    public static fl a(XMPushService xMPushService, byte[] bArr) {
        Cif cif = new Cif();
        try {
            it.a(cif, bArr);
            return a(u.m2542a((Context) xMPushService), xMPushService, cif);
        } catch (iz e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return null;
        }
    }

    public static fl a(t tVar, Context context, Cif cif) {
        try {
            fl flVar = new fl();
            flVar.a(5);
            flVar.c(tVar.f983a);
            flVar.b(a(cif));
            flVar.a("SECMSG", "message");
            String str = tVar.f983a;
            cif.f611a.f538a = str.substring(0, str.indexOf("@"));
            cif.f611a.f542c = str.substring(str.indexOf(com.xiaomi.stat.b.h.g) + 1);
            flVar.a(it.a(cif), tVar.c);
            flVar.a((short) 1);
            com.xiaomi.channel.commonutils.logger.b.m1859a("try send mi push message. packagename:" + cif.f616b + " action:" + cif.f609a);
            return flVar;
        } catch (NullPointerException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return null;
        }
    }

    public static Cif a(String str, String str2) {
        ii iiVar = new ii();
        iiVar.b(str2);
        iiVar.c("package uninstalled");
        iiVar.a(gn.i());
        iiVar.a(false);
        return a(str, str2, iiVar, hj.Notification);
    }

    public static <T extends iu<T, ?>> Cif a(String str, String str2, T t, hj hjVar) {
        return a(str, str2, t, hjVar, true);
    }

    public static <T extends iu<T, ?>> Cif a(String str, String str2, T t, hj hjVar, boolean z) {
        byte[] a = it.a(t);
        Cif cif = new Cif();
        hy hyVar = new hy();
        hyVar.f537a = 5L;
        hyVar.f538a = "fakeid";
        cif.a(hyVar);
        cif.a(ByteBuffer.wrap(a));
        cif.a(hjVar);
        cif.b(z);
        cif.b(str);
        cif.a(false);
        cif.a(str2);
        return cif;
    }

    public static String a(Cif cif) {
        Map<String, String> map;
        hw hwVar = cif.f610a;
        if (hwVar != null && (map = hwVar.f528b) != null) {
            String str = map.get("ext_traffic_source_pkg");
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return cif.f616b;
    }

    public static String a(String str) {
        return str + ".permission.MIPUSH_RECEIVE";
    }

    public static void a(XMPushService xMPushService) {
        t m2542a = u.m2542a(xMPushService.getApplicationContext());
        if (m2542a != null) {
            bg.b a = u.m2542a(xMPushService.getApplicationContext()).a(xMPushService);
            com.xiaomi.channel.commonutils.logger.b.m1859a("prepare account. " + a.f913a);
            a(xMPushService, a);
            bg.a().a(a);
            bx.a(xMPushService).a(new ai("GAID", 172800L, xMPushService, m2542a));
            a(xMPushService, m2542a, 172800);
        }
    }

    public static void a(XMPushService xMPushService, Cif cif) {
        db.a(cif.b(), xMPushService.getApplicationContext(), cif, -1);
        fw a = xMPushService.a();
        if (a != null) {
            if (!a.m2180a()) {
                throw new gh("Don't support XMPP connection.");
            }
            fl a2 = a(u.m2542a((Context) xMPushService), xMPushService, cif);
            if (a2 == null) {
                return;
            }
            a.b(a2);
            return;
        }
        throw new gh("try send msg while connection is null.");
    }

    public static void a(XMPushService xMPushService, bg.b bVar) {
        bVar.a((Messenger) null);
        bVar.a(new ak(xMPushService));
    }

    public static void a(XMPushService xMPushService, t tVar, int i) {
        bx.a(xMPushService).a(new aj("MSAID", i, xMPushService, tVar));
    }

    public static void a(XMPushService xMPushService, String str, byte[] bArr) {
        db.a(str, xMPushService.getApplicationContext(), bArr);
        fw a = xMPushService.a();
        if (a != null) {
            if (!a.m2180a()) {
                throw new gh("Don't support XMPP connection.");
            }
            fl a2 = a(xMPushService, bArr);
            if (a2 != null) {
                a.b(a2);
                return;
            } else {
                x.a(xMPushService, str, bArr, 70000003, "not a valid message");
                return;
            }
        }
        throw new gh("try send msg while connection is null.");
    }

    public static Cif b(String str, String str2) {
        ii iiVar = new ii();
        iiVar.b(str2);
        iiVar.c(ht.AppDataCleared.f489a);
        iiVar.a(bd.a());
        iiVar.a(false);
        return a(str, str2, iiVar, hj.Notification);
    }

    public static <T extends iu<T, ?>> Cif b(String str, String str2, T t, hj hjVar) {
        return a(str, str2, t, hjVar, false);
    }
}
