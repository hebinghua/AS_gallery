package com.xiaomi.push.service;

import com.xiaomi.push.fl;
import com.xiaomi.push.gh;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class c extends XMPushService.j {
    public XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    public fl[] f942a;

    public c(XMPushService xMPushService, fl[] flVarArr) {
        super(4);
        this.a = null;
        this.a = xMPushService;
        this.f942a = flVarArr;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "batch send message.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        try {
            fl[] flVarArr = this.f942a;
            if (flVarArr == null) {
                return;
            }
            this.a.a(flVarArr);
        } catch (gh e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            this.a.a(10, e);
        }
    }
}
