package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class x implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ long b;
    public final /* synthetic */ long c;
    public final /* synthetic */ MiStatParams d;
    public final /* synthetic */ e e;

    public x(e eVar, String str, long j, long j2, MiStatParams miStatParams) {
        this.e = eVar;
        this.a = str;
        this.b = j;
        this.c = j2;
        this.d = miStatParams;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        String str;
        if (!b.a()) {
            return;
        }
        g = this.e.g(false);
        if (!g || !b.z()) {
            return;
        }
        e eVar = this.e;
        String str2 = this.a;
        long j = this.b;
        MiStatParams miStatParams = this.d;
        str = eVar.b;
        eVar.a(com.xiaomi.stat.a.l.a(str2, j - this.c, j, miStatParams, str));
    }
}
