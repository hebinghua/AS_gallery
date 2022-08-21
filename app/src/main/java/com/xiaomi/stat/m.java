package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class m implements Runnable {
    public final /* synthetic */ HttpEvent a;
    public final /* synthetic */ e b;

    public m(e eVar, HttpEvent httpEvent) {
        this.b = eVar;
        this.a = httpEvent;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        String str;
        if (!b.a()) {
            return;
        }
        g = this.b.g(false);
        if (!g) {
            return;
        }
        e eVar = this.b;
        HttpEvent httpEvent = this.a;
        str = eVar.b;
        eVar.a(com.xiaomi.stat.a.l.a(httpEvent, str));
    }
}
