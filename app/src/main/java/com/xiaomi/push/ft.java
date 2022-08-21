package com.xiaomi.push;

/* loaded from: classes3.dex */
public class ft extends Thread {
    public final /* synthetic */ fs a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ft(fs fsVar, String str) {
        super(str);
        this.a = fsVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        fn fnVar;
        try {
            fnVar = this.a.a;
            fnVar.m2167a();
        } catch (Exception e) {
            this.a.c(9, e);
        }
    }
}
