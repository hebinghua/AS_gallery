package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.Cif;
import com.xiaomi.push.gh;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public final class ae extends XMPushService.j {
    public final /* synthetic */ Cif a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ XMPushService f863a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f864a;
    public final /* synthetic */ String b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ae(int i, XMPushService xMPushService, Cif cif, String str, String str2) {
        super(i);
        this.f863a = xMPushService;
        this.a = cif;
        this.f864a = str;
        this.b = str2;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "send wrong message ack for message.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        try {
            Cif a = y.a((Context) this.f863a, this.a);
            a.f610a.a("error", this.f864a);
            a.f610a.a("reason", this.b);
            ah.a(this.f863a, a);
        } catch (gh e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            this.f863a.a(10, e);
        }
    }
}
