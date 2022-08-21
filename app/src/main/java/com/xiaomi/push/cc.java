package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.cj;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class cc implements Runnable {
    public String a;

    /* renamed from: a  reason: collision with other field name */
    public WeakReference<Context> f149a;

    public cc(String str, WeakReference<Context> weakReference) {
        this.a = str;
        this.f149a = weakReference;
    }

    @Override // java.lang.Runnable
    public void run() {
        Context context;
        WeakReference<Context> weakReference = this.f149a;
        if (weakReference == null || (context = weakReference.get()) == null) {
            return;
        }
        if (cp.a(this.a) <= cb.a) {
            com.xiaomi.channel.commonutils.logger.b.b("=====> do not need clean db");
            return;
        }
        cf a = cf.a(this.a);
        ce a2 = ce.a(this.a);
        a.a(a2);
        a2.a(cd.a(context, this.a, 1000));
        cj.a(context).a((cj.a) a);
    }
}
