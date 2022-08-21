package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class ck extends XMPushService.j {
    public final /* synthetic */ XMPushService a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ck(XMPushService xMPushService, int i) {
        super(i);
        this.a = xMPushService;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "disconnect for service destroy.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        if (this.a.f825a != null) {
            this.a.f825a.b(15, (Exception) null);
            this.a.f825a = null;
        }
    }
}
