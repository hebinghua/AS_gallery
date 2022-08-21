package com.xiaomi.onetrack.api;

/* loaded from: classes3.dex */
public class ak implements Runnable {
    public final /* synthetic */ aj a;

    public ak(aj ajVar) {
        this.a = ajVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (!com.xiaomi.onetrack.b.h.b()) {
            return;
        }
        this.a.b();
    }
}
