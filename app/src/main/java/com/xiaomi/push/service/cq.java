package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;

/* loaded from: classes3.dex */
public class cq implements bg.a {
    public final /* synthetic */ XMPushService a;

    public cq(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    @Override // com.xiaomi.push.service.bg.a
    public void a() {
        this.a.m2430e();
        if (bg.a().m2488a() <= 0) {
            XMPushService xMPushService = this.a;
            xMPushService.a(new XMPushService.g(12, null));
        }
    }
}
