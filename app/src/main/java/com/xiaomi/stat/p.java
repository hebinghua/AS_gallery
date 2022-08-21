package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class p implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ int b;
    public final /* synthetic */ long c;
    public final /* synthetic */ long d;
    public final /* synthetic */ e e;

    public p(e eVar, int i, int i2, long j, long j2) {
        this.e = eVar;
        this.a = i;
        this.b = i2;
        this.c = j;
        this.d = j2;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        if (!b.a()) {
            return;
        }
        g = this.e.g();
        if (!g) {
            return;
        }
        this.e.a(com.xiaomi.stat.a.l.a(this.a, this.b, this.c, this.d));
    }
}
