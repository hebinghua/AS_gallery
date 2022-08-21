package com.xiaomi.push.service;

import com.xiaomi.push.Cif;
import com.xiaomi.push.gh;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public final class z extends XMPushService.j {
    public final /* synthetic */ Cif a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ XMPushService f990a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public z(int i, XMPushService xMPushService, Cif cif) {
        super(i);
        this.f990a = xMPushService;
        this.a = cif;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "send app absent message.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        try {
            ah.a(this.f990a, ah.a(this.a.b(), this.a.m2294a()));
        } catch (gh e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            this.f990a.a(10, e);
        }
    }
}
