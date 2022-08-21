package com.xiaomi.push;

import android.os.SystemClock;
import com.xiaomi.push.ff;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;
import java.util.Hashtable;

/* loaded from: classes3.dex */
public class fj {
    public static final int a = ez.PING_RTT.a();

    /* renamed from: a  reason: collision with other field name */
    public static long f353a = 0;

    /* loaded from: classes3.dex */
    public static class a {
        public static Hashtable<Integer, Long> a = new Hashtable<>();
    }

    public static void a() {
        if (f353a == 0 || SystemClock.elapsedRealtime() - f353a > 7200000) {
            f353a = SystemClock.elapsedRealtime();
            a(0, a);
        }
    }

    public static void a(int i) {
        fa m2152a = fh.m2151a().m2152a();
        m2152a.a(ez.CHANNEL_STATS_COUNTER.a());
        m2152a.c(i);
        fh.m2151a().a(m2152a);
    }

    public static synchronized void a(int i, int i2) {
        synchronized (fj.class) {
            if (i2 < 16777215) {
                a.a.put(Integer.valueOf((i << 24) | i2), Long.valueOf(System.currentTimeMillis()));
            } else {
                com.xiaomi.channel.commonutils.logger.b.d("stats key should less than 16777215");
            }
        }
    }

    public static void a(int i, int i2, int i3, String str, int i4) {
        fa m2152a = fh.m2151a().m2152a();
        m2152a.a((byte) i);
        m2152a.a(i2);
        m2152a.b(i3);
        m2152a.b(str);
        m2152a.c(i4);
        fh.m2151a().a(m2152a);
    }

    public static synchronized void a(int i, int i2, String str, int i3) {
        synchronized (fj.class) {
            long currentTimeMillis = System.currentTimeMillis();
            int i4 = (i << 24) | i2;
            if (a.a.containsKey(Integer.valueOf(i4))) {
                fa m2152a = fh.m2151a().m2152a();
                m2152a.a(i2);
                m2152a.b((int) (currentTimeMillis - a.a.get(Integer.valueOf(i4)).longValue()));
                m2152a.b(str);
                if (i3 > -1) {
                    m2152a.c(i3);
                }
                fh.m2151a().a(m2152a);
                a.a.remove(Integer.valueOf(i2));
            } else {
                com.xiaomi.channel.commonutils.logger.b.d("stats key not found");
            }
        }
    }

    public static void a(XMPushService xMPushService, bg.b bVar) {
        new fc(xMPushService, bVar).a();
    }

    public static void a(String str, int i, Exception exc) {
        fa m2152a = fh.m2151a().m2152a();
        if (fh.a() != null && fh.a().f345a != null) {
            m2152a.c(bj.c(fh.a().f345a) ? 1 : 0);
        }
        if (i > 0) {
            m2152a.a(ez.GSLB_REQUEST_SUCCESS.a());
            m2152a.b(str);
            m2152a.b(i);
            fh.m2151a().a(m2152a);
            return;
        }
        try {
            ff.a a2 = ff.a(exc);
            m2152a.a(a2.a.a());
            m2152a.c(a2.f342a);
            m2152a.b(str);
            fh.m2151a().a(m2152a);
        } catch (NullPointerException unused) {
        }
    }

    public static void a(String str, Exception exc) {
        try {
            ff.a b = ff.b(exc);
            fa m2152a = fh.m2151a().m2152a();
            m2152a.a(b.a.a());
            m2152a.c(b.f342a);
            m2152a.b(str);
            if (fh.a() != null && fh.a().f345a != null) {
                m2152a.c(bj.c(fh.a().f345a) ? 1 : 0);
            }
            fh.m2151a().a(m2152a);
        } catch (NullPointerException unused) {
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static byte[] m2157a() {
        fb m2153a = fh.m2151a().m2153a();
        if (m2153a != null) {
            return it.a(m2153a);
        }
        return null;
    }

    public static void b() {
        a(0, a, null, -1);
    }

    public static void b(String str, Exception exc) {
        try {
            ff.a d = ff.d(exc);
            fa m2152a = fh.m2151a().m2152a();
            m2152a.a(d.a.a());
            m2152a.c(d.f342a);
            m2152a.b(str);
            if (fh.a() != null && fh.a().f345a != null) {
                m2152a.c(bj.c(fh.a().f345a) ? 1 : 0);
            }
            fh.m2151a().a(m2152a);
        } catch (NullPointerException unused) {
        }
    }
}
