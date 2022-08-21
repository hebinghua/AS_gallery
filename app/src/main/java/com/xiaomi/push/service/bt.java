package com.xiaomi.push.service;

import com.xiaomi.push.fl;
import com.xiaomi.push.gh;
import com.xiaomi.push.hi;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class bt extends XMPushService.j {
    public fl a;

    /* renamed from: a  reason: collision with other field name */
    public XMPushService f928a;

    public bt(XMPushService xMPushService, fl flVar) {
        super(4);
        this.f928a = null;
        this.f928a = xMPushService;
        this.a = flVar;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "send a message.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        try {
            fl flVar = this.a;
            if (flVar == null) {
                return;
            }
            this.f928a.a(flVar);
            if (this.a.f357a == null || !hi.a(this.f928a, 1)) {
                return;
            }
            this.a.f357a.d = System.currentTimeMillis();
            bz.a("category_coord_up", "coord_up", com.xiaomi.stat.c.c.a, this.a.f357a);
        } catch (gh e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            this.f928a.a(10, e);
        }
    }
}
