package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class j implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ int b;
    public final /* synthetic */ e c;

    public j(e eVar, int i, int i2) {
        this.c = eVar;
        this.a = i;
        this.b = i2;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        if (!b.a()) {
            return;
        }
        g = this.c.g();
        if (!g) {
            return;
        }
        b.e(this.a);
        this.c.a(com.xiaomi.stat.a.l.a(this.b));
    }
}
