package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.fx;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class cu extends XMPushService.j {
    public final /* synthetic */ XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f953a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ byte[] f954a;
    public final /* synthetic */ int b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public cu(XMPushService xMPushService, int i, int i2, String str, byte[] bArr) {
        super(i);
        this.a = xMPushService;
        this.b = i2;
        this.f953a = str;
        this.f954a = bArr;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "clear account cache.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        fx fxVar;
        u.m2544a((Context) this.a);
        bg.a().m2493a("5");
        com.xiaomi.push.ae.a(this.b);
        fxVar = this.a.f826a;
        fxVar.c(fx.a());
        com.xiaomi.channel.commonutils.logger.b.m1859a("clear account and start registration. " + this.f953a);
        this.a.a(this.f954a, this.f953a);
    }
}
