package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.dy;
import com.xiaomi.push.ed;
import com.xiaomi.push.eh;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.ii;
import com.xiaomi.push.service.bd;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class c implements eh {
    @Override // com.xiaomi.push.eh
    public void a(Context context, HashMap<String, String> hashMap) {
        ii iiVar = new ii();
        iiVar.b(ed.a(context).m2124a());
        iiVar.d(ed.a(context).b());
        iiVar.c(ht.AwakeAppResponse.f489a);
        iiVar.a(bd.a());
        iiVar.f628a = hashMap;
        ao.a(context).a((ao) iiVar, hj.Notification, true, (hw) null, true);
        com.xiaomi.channel.commonutils.logger.b.m1859a("MoleInfo：\u3000send data in app layer");
    }

    @Override // com.xiaomi.push.eh
    public void b(Context context, HashMap<String, String> hashMap) {
        MiTinyDataClient.upload("category_awake_app", "wake_up_app", 1L, dy.a(hashMap));
        com.xiaomi.channel.commonutils.logger.b.m1859a("MoleInfo：\u3000send data in app layer");
    }

    @Override // com.xiaomi.push.eh
    public void c(Context context, HashMap<String, String> hashMap) {
        com.xiaomi.channel.commonutils.logger.b.m1859a("MoleInfo：\u3000" + dy.b(hashMap));
        String str = hashMap.get("awake_info");
        if (String.valueOf((int) com.xiaomi.stat.c.b.g).equals(hashMap.get("event_type"))) {
            o.a(context, str);
        }
    }
}
