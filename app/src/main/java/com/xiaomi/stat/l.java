package com.xiaomi.stat;

import android.text.TextUtils;

/* loaded from: classes3.dex */
class l implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ String b;
    public final /* synthetic */ e c;

    public l(e eVar, boolean z, String str) {
        this.c = eVar;
        this.a = z;
        this.b = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (!com.xiaomi.stat.d.m.a()) {
            b.c(this.a);
            com.xiaomi.stat.b.g.a().a(this.a);
        }
        if (b.e() && !TextUtils.isEmpty(this.b)) {
            b.a(this.b);
            com.xiaomi.stat.b.g.a().a(this.b);
        }
    }
}
