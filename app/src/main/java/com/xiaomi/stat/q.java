package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class q implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ String b;
    public final /* synthetic */ e c;

    public q(e eVar, String str, String str2) {
        this.c = eVar;
        this.a = str;
        this.b = str2;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        String str;
        if (!b.a()) {
            return;
        }
        g = this.c.g(false);
        if (!g) {
            return;
        }
        e eVar = this.c;
        String str2 = this.a;
        String str3 = this.b;
        str = eVar.b;
        eVar.a(com.xiaomi.stat.a.l.a(str2, str3, str));
    }
}
