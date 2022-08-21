package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class u implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ long b;
    public final /* synthetic */ long c;
    public final /* synthetic */ e d;

    public u(e eVar, String str, long j, long j2) {
        this.d = eVar;
        this.a = str;
        this.b = j;
        this.c = j2;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        if (!b.a()) {
            return;
        }
        g = this.d.g();
        if (!g || !b.z()) {
            return;
        }
        this.d.a(com.xiaomi.stat.a.l.a(this.a, this.b, this.c));
    }
}
