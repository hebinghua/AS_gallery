package com.xiaomi.stat;

import java.util.concurrent.Callable;

/* loaded from: classes3.dex */
class o implements Callable<String> {
    public final /* synthetic */ e a;

    public o(e eVar) {
        this.a = eVar;
    }

    @Override // java.util.concurrent.Callable
    /* renamed from: a */
    public String call() throws Exception {
        return com.xiaomi.stat.b.d.a().b();
    }
}
