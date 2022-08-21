package com.xiaomi.push.service;

import com.xiaomi.push.al;
import com.xiaomi.push.hj;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class b extends al.a {
    public ii a;

    /* renamed from: a  reason: collision with other field name */
    public WeakReference<XMPushService> f898a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f899a;

    public b(ii iiVar, WeakReference<XMPushService> weakReference, boolean z) {
        this.f899a = false;
        this.a = iiVar;
        this.f898a = weakReference;
        this.f899a = z;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "22";
    }

    @Override // java.lang.Runnable
    public void run() {
        XMPushService xMPushService;
        WeakReference<XMPushService> weakReference = this.f898a;
        if (weakReference == null || this.a == null || (xMPushService = weakReference.get()) == null) {
            return;
        }
        this.a.a(bd.a());
        this.a.a(false);
        com.xiaomi.channel.commonutils.logger.b.c("MoleInfo aw_ping : send aw_Ping msg " + this.a.m2308a());
        try {
            String c = this.a.c();
            xMPushService.a(c, it.a(ah.a(c, this.a.b(), this.a, hj.Notification)), this.f899a);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.d("MoleInfo aw_ping : send help app ping error" + e.toString());
        }
    }
}
