package com.xiaomi.onetrack.b;

/* loaded from: classes3.dex */
public final class i implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ String b;

    public i(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    @Override // java.lang.Runnable
    public void run() {
        h.c(this.a, this.b);
    }
}
