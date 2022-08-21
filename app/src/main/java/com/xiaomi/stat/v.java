package com.xiaomi.stat;

/* loaded from: classes3.dex */
class v implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ e b;

    public v(e eVar, boolean z) {
        this.b = eVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        b.a(this.a);
    }
}
