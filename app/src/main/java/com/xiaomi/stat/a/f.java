package com.xiaomi.stat.a;

import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class f implements Runnable {
    public final /* synthetic */ ArrayList a;
    public final /* synthetic */ c b;

    public f(c cVar, ArrayList arrayList) {
        this.b = cVar;
        this.a = arrayList;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.b.b(this.a);
    }
}
