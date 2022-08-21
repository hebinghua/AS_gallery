package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class y implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ String b;
    public final /* synthetic */ String c;
    public final /* synthetic */ MiStatParams d;
    public final /* synthetic */ e e;

    public y(e eVar, boolean z, String str, String str2, MiStatParams miStatParams) {
        this.e = eVar;
        this.a = z;
        this.b = str;
        this.c = str2;
        this.d = miStatParams;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        String str;
        if (!b.a()) {
            return;
        }
        g = this.e.g(this.a);
        if (!g || !b.A()) {
            return;
        }
        e eVar = this.e;
        String str2 = this.b;
        String str3 = this.c;
        MiStatParams miStatParams = this.d;
        str = eVar.b;
        eVar.a(com.xiaomi.stat.a.l.a(str2, str3, miStatParams, str, this.a));
    }
}
