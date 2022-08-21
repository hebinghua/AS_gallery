package com.xiaomi.stat.b;

import com.xiaomi.stat.d.l;

/* loaded from: classes3.dex */
class c implements Runnable {
    public final /* synthetic */ b a;

    public c(b bVar) {
        this.a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (l.a()) {
            this.a.a.a(false, false);
            i.a().a(false);
            d.a().b();
        }
    }
}
