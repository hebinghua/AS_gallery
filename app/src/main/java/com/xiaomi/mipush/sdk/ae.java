package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.al;
import com.xiaomi.push.hj;
import com.xiaomi.push.hp;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.ib;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import com.xiaomi.push.service.ba;
import com.xiaomi.push.service.bb;

/* loaded from: classes3.dex */
public class ae extends al.a {
    public Context a;

    public ae(Context context) {
        this.a = context;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "2";
    }

    @Override // java.lang.Runnable
    public void run() {
        ba a = ba.a(this.a);
        ib ibVar = new ib();
        ibVar.a(bb.a(a, hp.MISC_CONFIG));
        ibVar.b(bb.a(a, hp.PLUGIN_CONFIG));
        ii iiVar = new ii("-1", false);
        iiVar.c(ht.DailyCheckClientConfig.f489a);
        iiVar.a(it.a(ibVar));
        ao.a(this.a).a((ao) iiVar, hj.Notification, (hw) null);
    }
}
