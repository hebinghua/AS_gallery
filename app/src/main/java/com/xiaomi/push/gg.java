package com.xiaomi.push;

/* loaded from: classes3.dex */
public class gg implements Runnable {
    public final /* synthetic */ gd a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f411a;

    public gg(gd gdVar, String str) {
        this.a = gdVar;
        this.f411a = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        cv.a().a(this.f411a, true);
    }
}
