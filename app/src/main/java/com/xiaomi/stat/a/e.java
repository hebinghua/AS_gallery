package com.xiaomi.stat.a;

import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class e implements Callable<k> {
    public final /* synthetic */ b[] a;
    public final /* synthetic */ c b;

    public e(c cVar, b[] bVarArr) {
        this.b = cVar;
        this.a = bVarArr;
    }

    @Override // java.util.concurrent.Callable
    /* renamed from: a */
    public k call() throws Exception {
        k b;
        b = this.b.b(this.a);
        return b;
    }
}
