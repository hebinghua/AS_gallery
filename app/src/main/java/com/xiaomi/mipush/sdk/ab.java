package com.xiaomi.mipush.sdk;

import com.xiaomi.mipush.sdk.MiTinyDataClient;
import java.util.concurrent.ScheduledFuture;

/* loaded from: classes3.dex */
public class ab implements Runnable {
    public final /* synthetic */ MiTinyDataClient.a.C0108a a;

    public ab(MiTinyDataClient.a.C0108a c0108a) {
        this.a = c0108a;
    }

    @Override // java.lang.Runnable
    public void run() {
        ScheduledFuture scheduledFuture;
        ScheduledFuture scheduledFuture2;
        if (this.a.f32a.size() != 0) {
            this.a.b();
            return;
        }
        scheduledFuture = this.a.f33a;
        if (scheduledFuture == null) {
            return;
        }
        scheduledFuture2 = this.a.f33a;
        scheduledFuture2.cancel(false);
        this.a.f33a = null;
    }
}
