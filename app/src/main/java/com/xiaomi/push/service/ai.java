package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import com.xiaomi.push.service.bx;
import java.util.HashMap;

/* loaded from: classes3.dex */
public final class ai extends bx.a {
    public final /* synthetic */ XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ t f868a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ai(String str, long j, XMPushService xMPushService, t tVar) {
        super(str, j);
        this.a = xMPushService;
        this.f868a = tVar;
    }

    @Override // com.xiaomi.push.service.bx.a
    public void a(bx bxVar) {
        String a = bxVar.a("GAID", "gaid");
        String a2 = com.xiaomi.push.j.a((Context) this.a);
        if (TextUtils.isEmpty(a2) || TextUtils.equals(a, a2)) {
            return;
        }
        bxVar.a("GAID", "gaid", a2);
        ii iiVar = new ii();
        iiVar.b(this.f868a.d);
        iiVar.c(ht.ClientInfoUpdate.f489a);
        iiVar.a(bd.a());
        iiVar.a(new HashMap());
        iiVar.m2309a().put("gaid", a2);
        byte[] a3 = it.a(ah.a(this.a.getPackageName(), this.f868a.d, iiVar, hj.Notification));
        XMPushService xMPushService = this.a;
        xMPushService.a(xMPushService.getPackageName(), a3, true);
    }
}
