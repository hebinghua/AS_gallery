package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.u;

/* loaded from: classes3.dex */
public class ct implements u.a {
    public final /* synthetic */ XMPushService.j a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ XMPushService f952a;

    public ct(XMPushService xMPushService, XMPushService.j jVar) {
        this.f952a = xMPushService;
        this.a = jVar;
    }

    @Override // com.xiaomi.push.service.u.a
    public void a() {
        this.f952a.a(this.a);
    }
}
