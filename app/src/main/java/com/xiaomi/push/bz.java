package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.al;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public class bz extends al.a {
    public final /* synthetic */ bx a;

    public bz(bx bxVar) {
        this.a = bxVar;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "10054";
    }

    @Override // java.lang.Runnable
    public void run() {
        String c;
        Context context;
        Context context2;
        com.xiaomi.channel.commonutils.logger.b.c("exec== DbSizeControlJob");
        c = this.a.c();
        context = this.a.f139a;
        cc ccVar = new cc(c, new WeakReference(context));
        context2 = this.a.f139a;
        cj.a(context2).a(ccVar);
        this.a.b("check_time");
    }
}
