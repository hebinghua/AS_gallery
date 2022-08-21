package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;

/* loaded from: classes3.dex */
public class bi extends XMPushService.j {
    public final /* synthetic */ bg.b.c a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public bi(bg.b.c cVar, int i) {
        super(i);
        this.a = cVar;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "clear peer job";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        bg.b.c cVar = this.a;
        if (cVar.a == cVar.f920a.f907a) {
            com.xiaomi.channel.commonutils.logger.b.b("clean peer, chid = " + this.a.f920a.g);
            this.a.f920a.f907a = null;
        }
    }
}
