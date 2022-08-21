package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.hj;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;

/* loaded from: classes3.dex */
public final class ch implements Runnable {
    public final /* synthetic */ ii a;

    public ch(ii iiVar) {
        this.a = iiVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        Context context;
        Context context2;
        byte[] a = it.a(ah.a(this.a.c(), this.a.b(), this.a, hj.Notification));
        context = cg.a;
        if (!(context instanceof XMPushService)) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("UNDatas UploadNotificationDatas failed because not xmsf");
            return;
        }
        context2 = cg.a;
        ((XMPushService) context2).a(this.a.c(), a, true);
    }
}
