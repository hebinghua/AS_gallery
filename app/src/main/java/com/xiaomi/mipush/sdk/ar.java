package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import com.xiaomi.push.bj;
import com.xiaomi.push.service.bn;

/* loaded from: classes3.dex */
public class ar extends ContentObserver {
    public final /* synthetic */ ao a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ar(ao aoVar, Handler handler) {
        super(handler);
        this.a = aoVar;
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z) {
        Context context;
        Integer num;
        Context context2;
        Context context3;
        ao aoVar = this.a;
        context = aoVar.f47a;
        aoVar.f51a = Integer.valueOf(bn.a(context).a());
        num = this.a.f51a;
        if (num.intValue() != 0) {
            context2 = this.a.f47a;
            context2.getContentResolver().unregisterContentObserver(this);
            context3 = this.a.f47a;
            if (!bj.b(context3)) {
                return;
            }
            this.a.m1901c();
        }
    }
}
