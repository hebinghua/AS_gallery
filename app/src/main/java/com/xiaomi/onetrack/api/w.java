package com.xiaomi.onetrack.api;

/* loaded from: classes3.dex */
public class w implements Runnable {
    public final /* synthetic */ g a;

    public w(g gVar) {
        this.a = gVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        d dVar;
        dVar = this.a.b;
        com.xiaomi.onetrack.b.h.a(dVar);
    }
}
