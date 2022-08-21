package com.xiaomi.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import java.util.HashMap;

/* loaded from: classes3.dex */
public final class ed {
    public static volatile ed a;

    /* renamed from: a  reason: collision with other field name */
    public int f290a;

    /* renamed from: a  reason: collision with other field name */
    public Context f291a;

    /* renamed from: a  reason: collision with other field name */
    public eh f292a;

    /* renamed from: a  reason: collision with other field name */
    public String f293a;

    /* renamed from: a  reason: collision with other field name */
    public HashMap<ef, eg> f294a;
    public String b;

    public ed(Context context) {
        HashMap<ef, eg> hashMap = new HashMap<>();
        this.f294a = hashMap;
        this.f291a = context;
        hashMap.put(ef.SERVICE_ACTION, new ej());
        this.f294a.put(ef.SERVICE_COMPONENT, new ek());
        this.f294a.put(ef.ACTIVITY, new eb());
        this.f294a.put(ef.PROVIDER, new ei());
    }

    public static ed a(Context context) {
        if (a == null) {
            synchronized (ed.class) {
                if (a == null) {
                    a = new ed(context);
                }
            }
        }
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2122a(Context context) {
        return com.xiaomi.push.service.al.m2461a(context, context.getPackageName());
    }

    public int a() {
        return this.f290a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public eh m2123a() {
        return this.f292a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2124a() {
        return this.f293a;
    }

    public void a(int i) {
        this.f290a = i;
    }

    public void a(Context context, String str, int i, String str2, String str3) {
        if (context != null && !TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            a(i);
            al.a(this.f291a).a(new ee(this, str, context, str2, str3));
            return;
        }
        dz.a(context, "" + str, com.xiaomi.stat.c.b.h, "A receive a incorrect message");
    }

    public void a(ef efVar, Context context, Intent intent, String str) {
        if (efVar != null) {
            this.f294a.get(efVar).a(context, intent, str);
        } else {
            dz.a(context, "null", com.xiaomi.stat.c.b.h, "A receive a incorrect message with empty type");
        }
    }

    public final void a(ef efVar, Context context, ec ecVar) {
        this.f294a.get(efVar).a(context, ecVar);
    }

    public void a(eh ehVar) {
        this.f292a = ehVar;
    }

    public void a(String str) {
        this.f293a = str;
    }

    public void a(String str, String str2, int i, eh ehVar) {
        a(str);
        b(str2);
        a(i);
        a(ehVar);
    }

    public String b() {
        return this.b;
    }

    public void b(String str) {
        this.b = str;
    }
}
