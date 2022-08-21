package com.baidu.location.c;

import com.baidu.location.c.b;

/* loaded from: classes.dex */
class c implements Runnable {
    public final /* synthetic */ b.a a;

    public c(b.a aVar) {
        this.a = aVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            b.this.k();
        } catch (Exception unused) {
        }
    }
}
