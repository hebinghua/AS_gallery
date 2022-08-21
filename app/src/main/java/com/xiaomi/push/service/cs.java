package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class cs extends XMPushService.j {
    public final /* synthetic */ XMPushService a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public cs(XMPushService xMPushService, int i) {
        super(i);
        this.a = xMPushService;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "prepare the mi push account.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        ah.a(this.a);
        if (com.xiaomi.push.bj.b(this.a)) {
            this.a.a(true);
        }
    }
}
