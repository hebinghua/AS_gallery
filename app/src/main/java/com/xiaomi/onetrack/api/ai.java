package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.b.p;

/* loaded from: classes3.dex */
public class ai implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ ah b;

    public ai(ah ahVar, int i) {
        this.b = ahVar;
        this.a = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (com.xiaomi.onetrack.b.h.b() && this.a == 2) {
            p.a().a(0, true);
            p.a().a(1, true);
        }
    }
}
