package com.xiaomi.onetrack.e;

/* loaded from: classes3.dex */
public final class e implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ String b;
    public final /* synthetic */ String c;
    public final /* synthetic */ String d;

    public e(String str, String str2, String str3, String str4) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
    }

    @Override // java.lang.Runnable
    public void run() {
        com.xiaomi.onetrack.b.b.a().a(com.xiaomi.onetrack.d.b.a(this.a, this.b, this.c, this.d));
    }
}
