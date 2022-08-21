package com.xiaomi.clientreport.manager;

import com.xiaomi.push.br;

/* loaded from: classes3.dex */
public class i implements Runnable {
    public final /* synthetic */ a a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ br f19a;

    public i(a aVar, br brVar) {
        this.a = aVar;
        this.f19a = brVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f19a.run();
    }
}
