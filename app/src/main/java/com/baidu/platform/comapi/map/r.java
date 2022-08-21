package com.baidu.platform.comapi.map;

import android.graphics.Bitmap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class r implements Runnable {
    public final /* synthetic */ Bitmap a;
    public final /* synthetic */ o b;

    public r(o oVar, Bitmap bitmap) {
        this.b = oVar;
        this.a = bitmap;
    }

    @Override // java.lang.Runnable
    public void run() {
        c cVar;
        cVar = this.b.j;
        cVar.a(this.a);
    }
}
