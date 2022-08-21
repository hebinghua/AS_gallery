package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;

/* loaded from: classes3.dex */
public class bj extends XMPushService.j {
    public final /* synthetic */ bg.b.c a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public bj(bg.b.c cVar, int i) {
        super(i);
        this.a = cVar;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "check peer job";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        bg a = bg.a();
        bg.b bVar = this.a.f920a;
        if (a.a(bVar.g, bVar.f916b).f907a == null) {
            XMPushService xMPushService = bg.b.this.f909a;
            bg.b bVar2 = this.a.f920a;
            xMPushService.a(bVar2.g, bVar2.f916b, 2, null, null);
        }
    }
}
