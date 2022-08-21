package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.Cif;
import com.xiaomi.push.gh;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public final class ad extends XMPushService.j {
    public final /* synthetic */ Cif a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ XMPushService f861a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f862a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ad(int i, XMPushService xMPushService, Cif cif, String str) {
        super(i);
        this.f861a = xMPushService;
        this.a = cif;
        this.f862a = str;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "send app absent ack message for message.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        try {
            Cif a = y.a((Context) this.f861a, this.a);
            a.m2293a().a("absent_target_package", this.f862a);
            ah.a(this.f861a, a);
        } catch (gh e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            this.f861a.a(10, e);
        }
    }
}
