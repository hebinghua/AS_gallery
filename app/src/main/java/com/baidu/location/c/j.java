package com.baidu.location.c;

import com.baidu.location.b.o;
import com.baidu.location.b.s;
import com.baidu.location.b.x;
import com.baidu.location.c.i;

/* loaded from: classes.dex */
class j implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ i.a b;

    public j(i.a aVar, boolean z) {
        this.b = aVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean z;
        z = i.this.k;
        if (!z) {
            i.this.k = this.a;
        }
        i.this.q();
        o.c().i();
        if (System.currentTimeMillis() - s.b() <= 5000) {
            x.a().c();
        }
    }
}
