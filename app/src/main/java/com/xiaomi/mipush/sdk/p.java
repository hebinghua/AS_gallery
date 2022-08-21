package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.al;
import com.xiaomi.push.hj;
import com.xiaomi.push.hw;
import com.xiaomi.push.ii;
import com.xiaomi.push.service.bd;

/* loaded from: classes3.dex */
public final class p extends al.a {
    public final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ ii f75a;

    public p(ii iiVar, Context context) {
        this.f75a = iiVar;
        this.a = context;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "22";
    }

    @Override // java.lang.Runnable
    public void run() {
        ii iiVar = this.f75a;
        if (iiVar != null) {
            iiVar.a(bd.a());
            ao.a(this.a.getApplicationContext()).a((ao) this.f75a, hj.Notification, true, (hw) null, true);
        }
    }
}
