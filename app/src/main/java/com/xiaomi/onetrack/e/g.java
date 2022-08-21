package com.xiaomi.onetrack.e;

import android.content.Context;

/* loaded from: classes3.dex */
public class g implements Runnable {
    public final /* synthetic */ Context a;
    public final /* synthetic */ f b;

    public g(f fVar, Context context) {
        this.b = fVar;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public void run() {
        f.c(this.a);
        com.xiaomi.onetrack.b.b.a(this.a);
    }
}
