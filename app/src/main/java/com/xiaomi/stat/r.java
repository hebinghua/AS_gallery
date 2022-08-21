package com.xiaomi.stat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.SystemClock;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class r implements Application.ActivityLifecycleCallbacks {
    public final /* synthetic */ e a;
    private int b;

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public r(e eVar) {
        this.a = eVar;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        int i;
        Executor executor;
        i = this.a.i;
        if (i == 0) {
            this.a.l = SystemClock.elapsedRealtime();
            this.a.j = 0;
            this.a.k = 0;
            executor = this.a.e;
            executor.execute(new s(this));
        }
        e.g(this.a);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        e.h(this.a);
        this.b = System.identityHashCode(activity);
        this.a.f = SystemClock.elapsedRealtime();
        this.a.h();
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        long j;
        long d;
        e.j(this.a);
        if (this.b != System.identityHashCode(activity)) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        j = this.a.f;
        d = this.a.d();
        this.a.a(activity.getClass().getName(), d - (elapsedRealtime - j), d);
        this.a.h();
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        int i;
        long j;
        int i2;
        int i3;
        Executor executor;
        e.m(this.a);
        i = this.a.i;
        if (i == 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            j = this.a.l;
            long j2 = elapsedRealtime - j;
            long b = com.xiaomi.stat.d.r.b();
            e eVar = this.a;
            i2 = eVar.j;
            i3 = this.a.k;
            eVar.a(i2, i3, b - j2, b);
            executor = this.a.e;
            executor.execute(new t(this));
        }
    }
}
