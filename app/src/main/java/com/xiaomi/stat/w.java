package com.xiaomi.stat;

/* loaded from: classes3.dex */
class w implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ e b;

    public w(e eVar, boolean z) {
        this.b = eVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        b.b(this.a);
    }
}
