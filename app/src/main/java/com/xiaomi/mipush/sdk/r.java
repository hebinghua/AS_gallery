package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.dm;
import com.xiaomi.push.hj;
import com.xiaomi.push.hw;
import com.xiaomi.push.ii;

/* loaded from: classes3.dex */
public class r implements dm {
    public Context a;

    public r(Context context) {
        this.a = context;
    }

    @Override // com.xiaomi.push.dm
    public String a() {
        return b.m1906a(this.a).d();
    }

    @Override // com.xiaomi.push.dm
    public void a(ii iiVar, hj hjVar, hw hwVar) {
        ao.a(this.a).a((ao) iiVar, hjVar, hwVar);
    }
}
