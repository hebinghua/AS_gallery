package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.hj;
import com.xiaomi.push.ht;
import com.xiaomi.push.ii;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class s {
    public static volatile s a;

    /* renamed from: a  reason: collision with other field name */
    public final Context f76a;

    public s(Context context) {
        this.f76a = context.getApplicationContext();
    }

    public static s a(Context context) {
        if (a == null) {
            synchronized (s.class) {
                if (a == null) {
                    a = new s(context);
                }
            }
        }
        return a;
    }

    public static void a(Context context, Cif cif) {
        a(context).a(cif, 0, true);
    }

    public static void a(Context context, Cif cif, boolean z) {
        a(context).a(cif, 1, z);
    }

    public static void b(Context context, Cif cif, boolean z) {
        a(context).a(cif, 2, z);
    }

    public static void c(Context context, Cif cif, boolean z) {
        a(context).a(cif, 3, z);
    }

    public static void d(Context context, Cif cif, boolean z) {
        a(context).a(cif, 4, z);
    }

    public static void e(Context context, Cif cif, boolean z) {
        s a2;
        int i;
        b m1906a = b.m1906a(context);
        if (TextUtils.isEmpty(m1906a.c()) || TextUtils.isEmpty(m1906a.d())) {
            a2 = a(context);
            i = 6;
        } else {
            boolean f = m1906a.f();
            a2 = a(context);
            i = f ? 7 : 5;
        }
        a2.a(cif, i, z);
    }

    public final void a(Cif cif, int i, boolean z) {
        if (com.xiaomi.push.m.m2400a(this.f76a) || !com.xiaomi.push.m.m2399a() || cif == null || cif.f609a != hj.SendMessage || cif.m2293a() == null || !z) {
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("click to start activity result:" + String.valueOf(i));
        ii iiVar = new ii(cif.m2293a().m2259a(), false);
        iiVar.c(ht.SDK_START_ACTIVITY.f489a);
        iiVar.b(cif.m2294a());
        iiVar.d(cif.f616b);
        HashMap hashMap = new HashMap();
        iiVar.f628a = hashMap;
        hashMap.put("result", String.valueOf(i));
        ao.a(this.f76a).a(iiVar, hj.Notification, false, false, null, true, cif.f616b, cif.f612a, true, false);
    }
}
