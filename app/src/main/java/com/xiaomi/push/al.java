package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class al {
    public static volatile al a;

    /* renamed from: a  reason: collision with other field name */
    public SharedPreferences f86a;

    /* renamed from: a  reason: collision with other field name */
    public ScheduledThreadPoolExecutor f89a = new ScheduledThreadPoolExecutor(1);

    /* renamed from: a  reason: collision with other field name */
    public Map<String, ScheduledFuture> f88a = new HashMap();

    /* renamed from: a  reason: collision with other field name */
    public Object f87a = new Object();

    /* loaded from: classes3.dex */
    public static abstract class a implements Runnable {
        /* renamed from: a */
        public abstract String mo2050a();
    }

    /* loaded from: classes3.dex */
    public static class b implements Runnable {
        public a a;

        public b(a aVar) {
            this.a = aVar;
        }

        public void a() {
        }

        public void b() {
            throw null;
        }

        @Override // java.lang.Runnable
        public void run() {
            a();
            this.a.run();
            b();
        }
    }

    public al(Context context) {
        this.f86a = context.getSharedPreferences("mipush_extra", 0);
    }

    public static al a(Context context) {
        if (a == null) {
            synchronized (al.class) {
                if (a == null) {
                    a = new al(context);
                }
            }
        }
        return a;
    }

    public static String a(String str) {
        return "last_job_time" + str;
    }

    public final ScheduledFuture a(a aVar) {
        ScheduledFuture scheduledFuture;
        synchronized (this.f87a) {
            scheduledFuture = this.f88a.get(aVar.mo2050a());
        }
        return scheduledFuture;
    }

    public void a(Runnable runnable) {
        a(runnable, 0);
    }

    public void a(Runnable runnable, int i) {
        this.f89a.schedule(runnable, i, TimeUnit.SECONDS);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m1935a(a aVar) {
        return b(aVar, 0);
    }

    public boolean a(a aVar, int i) {
        return a(aVar, i, 0);
    }

    public boolean a(a aVar, int i, int i2) {
        return a(aVar, i, i2, false);
    }

    public boolean a(a aVar, int i, int i2, boolean z) {
        if (aVar == null || a(aVar) != null) {
            return false;
        }
        String a2 = a(aVar.mo2050a());
        am amVar = new am(this, aVar, z, a2);
        if (!z) {
            long abs = Math.abs(System.currentTimeMillis() - this.f86a.getLong(a2, 0L)) / 1000;
            if (abs < i - i2) {
                i2 = (int) (i - abs);
            }
        }
        try {
            ScheduledFuture<?> scheduleAtFixedRate = this.f89a.scheduleAtFixedRate(amVar, i2, i, TimeUnit.SECONDS);
            synchronized (this.f87a) {
                this.f88a.put(aVar.mo2050a(), scheduleAtFixedRate);
            }
            return true;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return true;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m1936a(String str) {
        synchronized (this.f87a) {
            ScheduledFuture scheduledFuture = this.f88a.get(str);
            if (scheduledFuture == null) {
                return false;
            }
            this.f88a.remove(str);
            return scheduledFuture.cancel(false);
        }
    }

    public boolean b(a aVar, int i) {
        if (aVar == null || a(aVar) != null) {
            return false;
        }
        ScheduledFuture<?> schedule = this.f89a.schedule(new an(this, aVar), i, TimeUnit.SECONDS);
        synchronized (this.f87a) {
            this.f88a.put(aVar.mo2050a(), schedule);
        }
        return true;
    }
}
