package com.xiaomi.mipush.sdk;

import com.xiaomi.mipush.sdk.MiTinyDataClient;
import com.xiaomi.push.hn;

/* loaded from: classes3.dex */
public class aa implements Runnable {
    public final /* synthetic */ MiTinyDataClient.a.C0108a a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ hn f37a;

    public aa(MiTinyDataClient.a.C0108a c0108a, hn hnVar) {
        this.a = c0108a;
        this.f37a = hnVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.a.f32a.add(this.f37a);
        this.a.a();
    }
}
