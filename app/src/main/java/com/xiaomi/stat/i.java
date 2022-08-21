package com.xiaomi.stat;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class i implements Runnable {
    public final /* synthetic */ boolean a;
    public final /* synthetic */ e b;

    public i(e eVar, boolean z) {
        this.b = eVar;
        this.a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str;
        String str2;
        String str3;
        String str4;
        str = this.b.c;
        if (b.d(str)) {
            int i = 2;
            if (!this.a) {
                str3 = this.b.c;
                if (b.e(str3) != 2) {
                    com.xiaomi.stat.a.c a = com.xiaomi.stat.a.c.a();
                    str4 = this.b.c;
                    a.a(str4);
                }
            }
            str2 = this.b.c;
            if (this.a) {
                i = 1;
            }
            b.a(str2, i);
        }
    }
}
