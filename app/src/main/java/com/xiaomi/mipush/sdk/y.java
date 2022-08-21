package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
public final class y implements Runnable {
    public final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Intent f80a;

    public y(Context context, Intent intent) {
        this.a = context;
        this.f80a = intent;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.a.startService(this.f80a);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a(e.getMessage());
        }
    }
}
