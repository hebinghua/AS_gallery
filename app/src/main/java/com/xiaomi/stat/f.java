package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class f implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ boolean b;
    public final /* synthetic */ e c;

    public f(e eVar, String str, boolean z) {
        this.c = eVar;
        this.a = str;
        this.b = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean z;
        String str;
        boolean z2;
        String str2;
        com.xiaomi.stat.d.e.a();
        z = this.c.a;
        if (z) {
            b.h(this.a);
        }
        b.d();
        com.xiaomi.stat.b.g.a().a(b.f());
        str = this.c.c;
        b.a(str, this.b);
        b.n();
        z2 = this.c.a;
        if (!z2) {
            str2 = this.c.b;
            b.f(str2);
        }
        this.c.f();
        com.xiaomi.stat.a.c.a().b();
        com.xiaomi.stat.b.e.a().execute(new g(this));
    }
}
