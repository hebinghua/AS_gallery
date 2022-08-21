package com.xiaomi.onetrack.api;

/* loaded from: classes3.dex */
public class f implements Runnable {
    public final /* synthetic */ Thread a;
    public final /* synthetic */ Throwable b;
    public final /* synthetic */ e c;

    public f(e eVar, Thread thread, Throwable th) {
        this.c = eVar;
        this.a = thread;
        this.b = th;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.c.a(this.a, this.b);
    }
}
