package com.xiaomi.onetrack.a;

import com.xiaomi.onetrack.util.p;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class h implements Runnable {
    public final /* synthetic */ ArrayList a;
    public final /* synthetic */ g b;

    public h(g gVar, ArrayList arrayList) {
        this.b = gVar;
        this.a = arrayList;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (p.a) {
            p.a("ConfigDbManager", "update: " + this.a);
        }
        this.b.b(this.a);
    }
}
