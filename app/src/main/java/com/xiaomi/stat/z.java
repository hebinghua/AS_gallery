package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class z implements Runnable {
    public final /* synthetic */ Throwable a;
    public final /* synthetic */ String b;
    public final /* synthetic */ boolean c;
    public final /* synthetic */ e d;

    public z(e eVar, Throwable th, String str, boolean z) {
        this.d = eVar;
        this.a = th;
        this.b = str;
        this.c = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        String str;
        if (!b.a()) {
            return;
        }
        g = this.d.g(false);
        if (!g) {
            return;
        }
        e eVar = this.d;
        Throwable th = this.a;
        String str2 = this.b;
        boolean z = this.c;
        str = eVar.b;
        eVar.a(com.xiaomi.stat.a.l.a(th, str2, z, str));
    }
}
