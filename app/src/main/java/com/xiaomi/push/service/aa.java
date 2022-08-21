package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.Cif;
import com.xiaomi.push.gh;
import com.xiaomi.push.service.XMPushService;
import java.util.Map;

/* loaded from: classes3.dex */
public final class aa extends XMPushService.j {
    public final /* synthetic */ Cif a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ XMPushService f858a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aa(int i, XMPushService xMPushService, Cif cif) {
        super(i);
        this.f858a = xMPushService;
        this.a = cif;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "send ack message for message.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        Map<String, String> map = null;
        try {
            if (com.xiaomi.push.m.m2400a((Context) this.f858a)) {
                try {
                    map = ag.a((Context) this.f858a, this.a);
                } catch (Throwable unused) {
                }
            }
            ah.a(this.f858a, y.a(this.f858a, this.a, map));
        } catch (gh e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            this.f858a.a(10, e);
        }
    }
}
