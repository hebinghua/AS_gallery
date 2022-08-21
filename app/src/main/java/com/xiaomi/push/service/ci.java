package com.xiaomi.push.service;

import com.xiaomi.push.fl;
import com.xiaomi.push.gb;
import com.xiaomi.push.gn;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class ci implements gb {
    public final /* synthetic */ XMPushService a;

    public ci(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    @Override // com.xiaomi.push.gb
    public void a(fl flVar) {
        XMPushService xMPushService = this.a;
        xMPushService.a(new XMPushService.d(flVar));
    }

    @Override // com.xiaomi.push.gb, com.xiaomi.push.gj
    /* renamed from: a */
    public void mo2175a(gn gnVar) {
        XMPushService xMPushService = this.a;
        xMPushService.a(new XMPushService.m(gnVar));
    }
}
