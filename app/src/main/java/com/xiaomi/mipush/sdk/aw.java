package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.ba;
import com.xiaomi.push.bp;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.ii;
import com.xiaomi.push.service.bd;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public final class aw implements Runnable {
    public final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ boolean f58a;

    public aw(Context context, boolean z) {
        this.a = context;
        this.f58a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        String d;
        String d2;
        Map<String, String> map;
        String d3;
        String str;
        String c;
        String c2;
        com.xiaomi.channel.commonutils.logger.b.m1859a("do sync info");
        ii iiVar = new ii(bd.a(), false);
        b m1906a = b.m1906a(this.a);
        iiVar.c(ht.SyncInfo.f489a);
        iiVar.b(m1906a.m1907a());
        iiVar.d(this.a.getPackageName());
        HashMap hashMap = new HashMap();
        iiVar.f628a = hashMap;
        Context context = this.a;
        com.xiaomi.push.n.a(hashMap, "app_version", com.xiaomi.push.h.m2213a(context, context.getPackageName()));
        Map<String, String> map2 = iiVar.f628a;
        Context context2 = this.a;
        com.xiaomi.push.n.a(map2, "app_version_code", Integer.toString(com.xiaomi.push.h.a(context2, context2.getPackageName())));
        com.xiaomi.push.n.a(iiVar.f628a, "push_sdk_vn", "4_9_1");
        com.xiaomi.push.n.a(iiVar.f628a, "push_sdk_vc", Integer.toString(40091));
        com.xiaomi.push.n.a(iiVar.f628a, "token", m1906a.b());
        if (!com.xiaomi.push.m.m2405d()) {
            String a = bp.a(com.xiaomi.push.j.d(this.a));
            String f = com.xiaomi.push.j.f(this.a);
            if (!TextUtils.isEmpty(f)) {
                a = a + "," + f;
            }
            if (!TextUtils.isEmpty(a)) {
                com.xiaomi.push.n.a(iiVar.f628a, "imei_md5", a);
            }
        }
        ba.a(this.a).a(iiVar.f628a);
        com.xiaomi.push.n.a(iiVar.f628a, "reg_id", m1906a.c());
        com.xiaomi.push.n.a(iiVar.f628a, "reg_secret", m1906a.d());
        com.xiaomi.push.n.a(iiVar.f628a, "accept_time", MiPushClient.getAcceptTime(this.a).replace(",", "-"));
        if (this.f58a) {
            Map<String, String> map3 = iiVar.f628a;
            c = av.c(MiPushClient.getAllAlias(this.a));
            com.xiaomi.push.n.a(map3, "aliases_md5", c);
            Map<String, String> map4 = iiVar.f628a;
            c2 = av.c(MiPushClient.getAllTopic(this.a));
            com.xiaomi.push.n.a(map4, "topics_md5", c2);
            map = iiVar.f628a;
            d3 = av.c(MiPushClient.getAllUserAccount(this.a));
            str = "accounts_md5";
        } else {
            Map<String, String> map5 = iiVar.f628a;
            d = av.d(MiPushClient.getAllAlias(this.a));
            com.xiaomi.push.n.a(map5, "aliases", d);
            Map<String, String> map6 = iiVar.f628a;
            d2 = av.d(MiPushClient.getAllTopic(this.a));
            com.xiaomi.push.n.a(map6, "topics", d2);
            map = iiVar.f628a;
            d3 = av.d(MiPushClient.getAllUserAccount(this.a));
            str = "user_accounts";
        }
        com.xiaomi.push.n.a(map, str, d3);
        ao.a(this.a).a((ao) iiVar, hj.Notification, false, (hw) null);
    }
}
