package com.xiaomi.push;

import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class ge extends XMPushService.j {
    public final /* synthetic */ long a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ gd f409a;
    public final /* synthetic */ long b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ge(gd gdVar, int i, long j, long j2) {
        super(i);
        this.f409a = gdVar;
        this.a = j;
        this.b = j2;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "check the ping-pong." + this.b;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        Thread.yield();
        if (!this.f409a.m2184c() || this.f409a.a(this.a)) {
            return;
        }
        com.xiaomi.push.service.o.a(this.f409a.b).m2524b();
        this.f409a.b.a(22, (Exception) null);
    }
}
