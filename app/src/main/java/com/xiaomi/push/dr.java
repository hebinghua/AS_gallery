package com.xiaomi.push;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public class dr {
    public static volatile dr a;

    /* renamed from: a  reason: collision with other field name */
    public Context f218a;

    /* renamed from: a  reason: collision with other field name */
    public a f219a;

    /* loaded from: classes3.dex */
    public interface a {
        void a();
    }

    public dr(Context context) {
        this.f218a = context;
    }

    public static int a(int i) {
        return Math.max(60, i);
    }

    public static dr a(Context context) {
        if (a == null) {
            synchronized (dr.class) {
                if (a == null) {
                    a = new dr(context);
                }
            }
        }
        return a;
    }

    public void a() {
        al.a(this.f218a).a(new ds(this));
    }

    public final void a(com.xiaomi.push.service.ba baVar, al alVar, boolean z) {
        if (baVar.a(ho.UploadSwitch.a(), true)) {
            dv dvVar = new dv(this.f218a);
            if (z) {
                alVar.a((al.a) dvVar, a(baVar.a(ho.UploadFrequency.a(), 86400)));
            } else {
                alVar.m1935a((al.a) dvVar);
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public final boolean m2045a() {
        try {
            Context context = this.f218a;
            if (!(context instanceof Application)) {
                context = context.getApplicationContext();
            }
            ((Application) context).registerActivityLifecycleCallbacks(new dl(this.f218a, String.valueOf(System.currentTimeMillis() / 1000)));
            return true;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return false;
        }
    }

    public final void b() {
        a aVar;
        al a2 = al.a(this.f218a);
        com.xiaomi.push.service.ba a3 = com.xiaomi.push.service.ba.a(this.f218a);
        SharedPreferences sharedPreferences = this.f218a.getSharedPreferences("mipush_extra", 0);
        long currentTimeMillis = System.currentTimeMillis();
        long j = sharedPreferences.getLong("first_try_ts", currentTimeMillis);
        if (j == currentTimeMillis) {
            sharedPreferences.edit().putLong("first_try_ts", currentTimeMillis).commit();
        }
        if (Math.abs(currentTimeMillis - j) < 172800000) {
            return;
        }
        a(a3, a2, false);
        if (a3.a(ho.StorageCollectionSwitch.a(), true)) {
            int a4 = a(a3.a(ho.StorageCollectionFrequency.a(), 86400));
            a2.a(new du(this.f218a, a4), a4, 0);
        }
        if (m.m2400a(this.f218a) && (aVar = this.f219a) != null) {
            aVar.a();
        }
        if (a3.a(ho.ActivityTSSwitch.a(), false)) {
            m2045a();
        }
        a(a3, a2, true);
    }
}
