package com.xiaomi.push.service;

import com.xiaomi.push.service.bg;

/* loaded from: classes3.dex */
public final class ak implements bg.b.a {
    public final /* synthetic */ XMPushService a;

    public ak(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    @Override // com.xiaomi.push.service.bg.b.a
    public void a(bg.c cVar, bg.c cVar2, int i) {
        if (cVar2 == bg.c.binded) {
            x.a(this.a, true);
            x.a(this.a);
        } else if (cVar2 != bg.c.unbind) {
        } else {
            com.xiaomi.channel.commonutils.logger.b.m1859a("onChange unbind");
            x.a(this.a, 70000001, " the push is not connected.");
        }
    }
}
