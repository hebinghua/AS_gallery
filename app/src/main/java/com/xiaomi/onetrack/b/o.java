package com.xiaomi.onetrack.b;

import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes3.dex */
public class o implements Runnable {
    public final /* synthetic */ l a;

    public o(l lVar) {
        this.a = lVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        AtomicBoolean atomicBoolean;
        AtomicBoolean atomicBoolean2;
        atomicBoolean = this.a.i;
        if (atomicBoolean.get()) {
            com.xiaomi.onetrack.a.d.b();
        }
        atomicBoolean2 = this.a.i;
        atomicBoolean2.set(true);
    }
}
