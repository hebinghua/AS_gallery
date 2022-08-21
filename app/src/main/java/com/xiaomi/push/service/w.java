package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.gh;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;
import java.util.Collection;

/* loaded from: classes3.dex */
public class w extends XMPushService.j {
    public XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    public String f987a;

    /* renamed from: a  reason: collision with other field name */
    public byte[] f988a;
    public String b;
    public String c;

    public w(XMPushService xMPushService, String str, String str2, String str3, byte[] bArr) {
        super(9);
        this.a = xMPushService;
        this.f987a = str;
        this.f988a = bArr;
        this.b = str2;
        this.c = str3;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "register app";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        bg.b next;
        t m2542a = u.m2542a((Context) this.a);
        if (m2542a == null) {
            try {
                m2542a = u.a(this.a, this.f987a, this.b, this.c);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.d("fail to register push account. " + e);
            }
        }
        if (m2542a == null) {
            com.xiaomi.channel.commonutils.logger.b.d("no account for registration.");
            x.a(this.a, 70000002, "no account.");
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("do registration now.");
        Collection<bg.b> m2490a = bg.a().m2490a("5");
        if (m2490a.isEmpty()) {
            next = m2542a.a(this.a);
            ah.a(this.a, next);
            bg.a().a(next);
        } else {
            next = m2490a.iterator().next();
        }
        if (!this.a.m2428c()) {
            x.a(this.f987a, this.f988a);
            this.a.a(true);
            return;
        }
        try {
            bg.c cVar = next.f911a;
            if (cVar == bg.c.binded) {
                ah.a(this.a, this.f987a, this.f988a);
            } else if (cVar == bg.c.unbind) {
                x.a(this.f987a, this.f988a);
                XMPushService xMPushService = this.a;
                xMPushService.getClass();
                xMPushService.a(new XMPushService.b(next));
            }
        } catch (gh e2) {
            com.xiaomi.channel.commonutils.logger.b.d("meet error, disconnect connection. " + e2);
            this.a.a(10, e2);
        }
    }
}
