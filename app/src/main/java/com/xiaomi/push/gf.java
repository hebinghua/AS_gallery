package com.xiaomi.push;

import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class gf extends XMPushService.j {
    public final /* synthetic */ gd a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Exception f410a;
    public final /* synthetic */ int b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public gf(gd gdVar, int i, int i2, Exception exc) {
        super(i);
        this.a = gdVar;
        this.b = i2;
        this.f410a = exc;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "shutdown the connection. " + this.b + ", " + this.f410a;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        this.a.b.a(this.b, this.f410a);
    }
}
