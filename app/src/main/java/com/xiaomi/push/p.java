package com.xiaomi.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/* loaded from: classes3.dex */
public final class p implements Runnable {
    public final /* synthetic */ ComponentName a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Context f804a;

    public p(Context context, ComponentName componentName) {
        this.f804a = context;
        this.a = componentName;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            PackageManager packageManager = this.f804a.getPackageManager();
            if (packageManager.getComponentEnabledSetting(this.a) == 2) {
                return;
            }
            packageManager.setComponentEnabledSetting(this.a, 2, 1);
        } catch (Throwable th) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("close static register of network status receiver failed:" + th);
        }
    }
}
