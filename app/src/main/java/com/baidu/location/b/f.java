package com.baidu.location.b;

import com.baidu.location.b.d;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class f extends TimerTask {
    public final /* synthetic */ Timer a;
    public final /* synthetic */ d.b b;

    public f(d.b bVar, Timer timer) {
        this.b = bVar;
        this.a = timer;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        d.b bVar = this.b;
        if (!bVar.d) {
            bVar.c();
        }
        this.a.cancel();
        this.a.purge();
    }
}
