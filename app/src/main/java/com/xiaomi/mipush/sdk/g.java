package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.ho;
import com.xiaomi.push.service.ba;

/* loaded from: classes3.dex */
public class g extends ba.a {
    public final /* synthetic */ f a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(f fVar, int i, String str) {
        super(i, str);
        this.a = fVar;
    }

    @Override // com.xiaomi.push.service.ba.a
    public void onCallback() {
        Context context;
        boolean z;
        Context context2;
        context = this.a.f69a;
        boolean a = ba.a(context).a(ho.AggregatePushSwitch.a(), true);
        z = this.a.f72a;
        if (z != a) {
            this.a.f72a = a;
            context2 = this.a.f69a;
            i.b(context2);
        }
    }
}
