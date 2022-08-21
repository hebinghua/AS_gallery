package com.baidu.location.b;

import android.location.Location;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class h implements Runnable {
    public final /* synthetic */ Location a;
    public final /* synthetic */ g b;

    public h(g gVar, Location location) {
        this.b = gVar;
        this.a = location;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.b.b(this.a);
    }
}
