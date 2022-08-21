package com.xiaomi.stat;

import android.text.TextUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class h implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ e b;

    public h(e eVar, String str) {
        this.b = eVar;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean g;
        if (b.a() && !TextUtils.equals(b.h(), this.a)) {
            b.b(this.a);
            g = this.b.g();
            if (!g) {
                return;
            }
            this.b.a(com.xiaomi.stat.a.l.a(this.a));
        }
    }
}
