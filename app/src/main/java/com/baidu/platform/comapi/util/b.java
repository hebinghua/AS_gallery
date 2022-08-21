package com.baidu.platform.comapi.util;

import com.baidu.platform.comapi.util.a;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class b implements Runnable {
    public final /* synthetic */ a.C0025a a;
    public final /* synthetic */ Object b;
    public final /* synthetic */ a c;

    public b(a aVar, a.C0025a c0025a, Object obj) {
        this.c = aVar;
        this.a = c0025a;
        this.b = obj;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.c.a(this.a, this.b);
    }
}
