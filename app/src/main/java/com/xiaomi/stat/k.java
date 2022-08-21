package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class k implements Runnable {
    public final /* synthetic */ e a;

    public k(e eVar) {
        this.a = eVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        boolean a;
        if (!b.a()) {
            return;
        }
        g = this.a.g();
        if (!g || !b.z()) {
            return;
        }
        long b = com.xiaomi.stat.d.r.b();
        a = this.a.a(b.r(), b);
        if (!a) {
            return;
        }
        b.a(b);
        this.a.a(com.xiaomi.stat.a.l.a());
    }
}
