package com.xiaomi.push.service;

import com.xiaomi.push.Cif;
import com.xiaomi.push.gh;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.ia;
import com.xiaomi.push.ii;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public final class af extends XMPushService.j {
    public final /* synthetic */ Cif a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ ii f865a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ XMPushService f866a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public af(int i, ii iiVar, Cif cif, XMPushService xMPushService) {
        super(i);
        this.f865a = iiVar;
        this.a = cif;
        this.f866a = xMPushService;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "send ack message for clear push message.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        try {
            ia iaVar = new ia();
            iaVar.c(ht.CancelPushMessageACK.f489a);
            iaVar.a(this.f865a.m2308a());
            iaVar.a(this.f865a.a());
            iaVar.b(this.f865a.b());
            iaVar.e(this.f865a.c());
            iaVar.a(0L);
            iaVar.d("success clear push message.");
            ah.a(this.f866a, ah.b(this.a.b(), this.a.m2294a(), iaVar, hj.Notification));
        } catch (gh e) {
            com.xiaomi.channel.commonutils.logger.b.d("clear push message. " + e);
            this.f866a.a(10, e);
        }
    }
}
