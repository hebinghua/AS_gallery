package com.xiaomi.push.service;

/* loaded from: classes3.dex */
public class cm implements Runnable {
    public final /* synthetic */ XMPushService a;

    public cm(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.a.f841a = true;
        try {
            com.xiaomi.channel.commonutils.logger.b.m1859a("try to trigger the wifi digest broadcast.");
            Object systemService = this.a.getApplicationContext().getSystemService("MiuiWifiService");
            if (systemService == null) {
                return;
            }
            com.xiaomi.push.bk.b(systemService, "sendCurrentWifiDigestInfo", new Object[0]);
        } catch (Throwable unused) {
        }
    }
}
