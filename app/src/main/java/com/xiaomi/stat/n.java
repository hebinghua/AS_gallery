package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class n implements Runnable {
    public final /* synthetic */ NetAvailableEvent a;
    public final /* synthetic */ e b;

    public n(e eVar, NetAvailableEvent netAvailableEvent) {
        this.b = eVar;
        this.a = netAvailableEvent;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        String str;
        if (!b.a()) {
            return;
        }
        g = this.b.g(false);
        if (!g || !b.y()) {
            return;
        }
        e eVar = this.b;
        NetAvailableEvent netAvailableEvent = this.a;
        str = eVar.b;
        eVar.a(com.xiaomi.stat.a.l.a(netAvailableEvent, str));
    }
}
