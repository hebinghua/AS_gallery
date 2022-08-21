package com.xiaomi.push.service;

import com.xiaomi.push.dx$b;
import com.xiaomi.push.fx;
import com.xiaomi.push.ga;
import java.util.Map;

/* loaded from: classes3.dex */
public class cp extends fx {
    public final /* synthetic */ XMPushService a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public cp(XMPushService xMPushService, Map map, int i, String str, ga gaVar) {
        super(map, i, str, gaVar);
        this.a = xMPushService;
    }

    @Override // com.xiaomi.push.fx
    /* renamed from: a */
    public byte[] mo2185a() {
        try {
            dx$b dx_b = new dx$b();
            dx_b.a(bv.a().m2508a());
            return dx_b.a();
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("getOBBString err: " + e.toString());
            return null;
        }
    }
}
