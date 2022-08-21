package com.xiaomi.push;

import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class fd extends XMPushService.j {
    public final /* synthetic */ fc a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public fd(fc fcVar, int i) {
        super(i);
        this.a = fcVar;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "Handling bind stats";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        this.a.c();
    }
}
