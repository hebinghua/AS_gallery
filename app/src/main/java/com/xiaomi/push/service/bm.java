package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.dy;
import com.xiaomi.push.ed;
import com.xiaomi.push.eh;
import com.xiaomi.push.hg;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class bm implements eh {
    @Override // com.xiaomi.push.eh
    public void a(Context context, HashMap<String, String> hashMap) {
        ii iiVar = new ii();
        iiVar.b(ed.a(context).m2124a());
        iiVar.d(ed.a(context).b());
        iiVar.c(ht.AwakeAppResponse.f489a);
        iiVar.a(bd.a());
        iiVar.f628a = hashMap;
        byte[] a = it.a(ah.a(iiVar.c(), iiVar.b(), iiVar, hj.Notification));
        if (!(context instanceof XMPushService)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("MoleInfo : context is not correct in pushLayer " + iiVar.m2308a());
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("MoleInfo : send data directly in pushLayer " + iiVar.m2308a());
        ((XMPushService) context).a(context.getPackageName(), a, true);
    }

    @Override // com.xiaomi.push.eh
    public void b(Context context, HashMap<String, String> hashMap) {
        hg a = hg.a(context);
        if (a != null) {
            a.a("category_awake_app", "wake_up_app", 1L, dy.a(hashMap));
        }
    }

    @Override // com.xiaomi.push.eh
    public void c(Context context, HashMap<String, String> hashMap) {
        com.xiaomi.channel.commonutils.logger.b.m1859a("MoleInfo：\u3000" + dy.b(hashMap));
    }
}
