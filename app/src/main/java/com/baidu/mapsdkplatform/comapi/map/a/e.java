package com.baidu.mapsdkplatform.comapi.map.a;

import com.baidu.mapapi.map.track.TraceOverlay;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class e implements Runnable {
    public final /* synthetic */ TraceOverlay a;
    public final /* synthetic */ c b;

    public e(c cVar, TraceOverlay traceOverlay) {
        this.b = cVar;
        this.a = traceOverlay;
    }

    @Override // java.lang.Runnable
    public void run() {
        a aVar;
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        a aVar2;
        aVar = this.b.a;
        if (aVar != null) {
            cVar = this.b.b;
            if (cVar == null) {
                return;
            }
            this.b.c(this.a);
            aVar2 = this.b.a;
            aVar2.a();
        }
    }
}
