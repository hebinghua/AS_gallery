package com.xiaomi.push;

import android.content.Context;
import java.lang.reflect.Method;

/* loaded from: classes3.dex */
public class ay implements au {
    public Context a;

    /* renamed from: a  reason: collision with other field name */
    public Class<?> f114a;

    /* renamed from: a  reason: collision with other field name */
    public Object f115a;

    /* renamed from: a  reason: collision with other field name */
    public Method f116a = null;
    public Method b = null;
    public Method c = null;
    public Method d = null;

    public ay(Context context) {
        this.a = context;
        a(context);
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public String mo1967a() {
        return a(this.a, this.b);
    }

    public final String a(Context context, Method method) {
        Object obj = this.f115a;
        if (obj == null || method == null) {
            return null;
        }
        try {
            Object invoke = method.invoke(obj, context);
            if (invoke == null) {
                return null;
            }
            return (String) invoke;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("miui invoke error", e);
            return null;
        }
    }

    public final void a(Context context) {
        try {
            Class<?> a = v.a(context, "com.android.id.impl.IdProviderImpl");
            this.f114a = a;
            this.f115a = a.newInstance();
            this.f116a = this.f114a.getMethod("getUDID", Context.class);
            this.b = this.f114a.getMethod("getOAID", Context.class);
            this.c = this.f114a.getMethod("getVAID", Context.class);
            this.d = this.f114a.getMethod("getAAID", Context.class);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("miui load class error", e);
        }
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo1967a() {
        return (this.f114a == null || this.f115a == null) ? false : true;
    }
}
