package com.baidu.mapsdkplatform.comapi.map.a;

import com.baidu.mapapi.map.track.TraceOverlay;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class f implements Runnable {
    public final /* synthetic */ TraceOverlay a;
    public final /* synthetic */ c b;

    public f(c cVar, TraceOverlay traceOverlay) {
        this.b = cVar;
        this.a = traceOverlay;
    }

    @Override // java.lang.Runnable
    public void run() {
        a aVar;
        this.b.c(this.a);
        aVar = this.b.a;
        aVar.a();
    }
}
