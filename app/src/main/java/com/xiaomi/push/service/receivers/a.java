package com.xiaomi.push.service.receivers;

import android.content.Context;

/* loaded from: classes3.dex */
public class a implements Runnable {
    public final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ NetworkStatusReceiver f980a;

    public a(NetworkStatusReceiver networkStatusReceiver, Context context) {
        this.f980a = networkStatusReceiver;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f980a.a(this.a);
    }
}
