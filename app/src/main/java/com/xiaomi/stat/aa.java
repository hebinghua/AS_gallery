package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class aa implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ MiStatParams b;
    public final /* synthetic */ e c;

    public aa(e eVar, boolean z, MiStatParams miStatParams) {
        this.c = eVar;
        this.a = z;
        this.b = miStatParams;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        String str;
        if (!b.a()) {
            return;
        }
        g = this.c.g(this.a);
        if (!g) {
            return;
        }
        e eVar = this.c;
        MiStatParams miStatParams = this.b;
        boolean z = this.a;
        str = eVar.b;
        eVar.a(com.xiaomi.stat.a.l.a(miStatParams, z, str));
    }
}
