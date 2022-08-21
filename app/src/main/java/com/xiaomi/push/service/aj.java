package com.xiaomi.push.service;

import android.text.TextUtils;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import com.xiaomi.push.service.bx;
import java.util.HashMap;

/* loaded from: classes3.dex */
public final class aj extends bx.a {
    public final /* synthetic */ XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ t f869a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public aj(String str, long j, XMPushService xMPushService, t tVar) {
        super(str, j);
        this.a = xMPushService;
        this.f869a = tVar;
    }

    @Override // com.xiaomi.push.service.bx.a
    public void a(bx bxVar) {
        com.xiaomi.push.ba a = com.xiaomi.push.ba.a(this.a);
        String a2 = bxVar.a("MSAID", "msaid");
        String str = a.b() + a.mo1967a() + a.c() + a.d();
        if (TextUtils.isEmpty(str) || TextUtils.equals(a2, str)) {
            return;
        }
        bxVar.a("MSAID", "msaid", str);
        ii iiVar = new ii();
        iiVar.b(this.f869a.d);
        iiVar.c(ht.ClientInfoUpdate.f489a);
        iiVar.a(bd.a());
        iiVar.a(new HashMap());
        a.a(iiVar.m2309a());
        byte[] a3 = it.a(ah.a(this.a.getPackageName(), this.f869a.d, iiVar, hj.Notification));
        XMPushService xMPushService = this.a;
        xMPushService.a(xMPushService.getPackageName(), a3, true);
    }
}
