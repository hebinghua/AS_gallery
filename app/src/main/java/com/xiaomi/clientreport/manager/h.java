package com.xiaomi.clientreport.manager;

import com.xiaomi.push.bq;

/* loaded from: classes3.dex */
public class h implements Runnable {
    public final /* synthetic */ a a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ bq f18a;

    public h(a aVar, bq bqVar) {
        this.a = aVar;
        this.f18a = bqVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f18a.run();
    }
}
