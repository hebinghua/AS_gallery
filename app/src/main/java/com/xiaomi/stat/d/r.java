package com.xiaomi.stat.d;

import android.content.Context;
import android.os.SystemClock;
import com.xiaomi.stat.ak;
import java.util.Calendar;

/* loaded from: classes3.dex */
public class r {
    public static final long a = 86400000;
    private static final String b = "TimeUtil";
    private static final long c = 300000;
    private static long d;
    private static long e;
    private static long f;

    public static void a() {
        boolean z;
        Context a2 = ak.a();
        long f2 = p.f(a2);
        long g = p.g(a2);
        long h = p.h(a2);
        if (f2 == 0 || g == 0 || h == 0 || Math.abs((System.currentTimeMillis() - g) - (SystemClock.elapsedRealtime() - h)) > c) {
            z = true;
        } else {
            d = f2;
            f = g;
            e = h;
            z = false;
        }
        if (z) {
            com.xiaomi.stat.b.e.a().execute(new s());
        }
        k.b(b, "syncTimeIfNeeded sync: " + z);
    }

    public static long b() {
        long j = d;
        if (j == 0 || e == 0) {
            return System.currentTimeMillis();
        }
        return (j + SystemClock.elapsedRealtime()) - e;
    }

    public static void a(long j) {
        if (j <= 0) {
            return;
        }
        k.b("MI_STAT_TEST", "update server time:" + j);
        d = j;
        e = SystemClock.elapsedRealtime();
        f = System.currentTimeMillis();
        Context a2 = ak.a();
        p.a(a2, d);
        p.b(a2, f);
        p.c(a2, e);
    }

    public static boolean a(long j, long j2) {
        return Math.abs(b() - j) >= j2;
    }

    public static boolean b(long j) {
        k.b("MI_STAT_TEST", "inToday,current ts :" + b());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(b());
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        long timeInMillis = calendar.getTimeInMillis();
        long j2 = timeInMillis + 86400000;
        k.b("MI_STAT_TEST", "[start]:" + timeInMillis + "\n[end]:" + j2 + "duration" + ((j2 - timeInMillis) - 86400000));
        StringBuilder sb = new StringBuilder();
        sb.append("is in today:");
        int i = (timeInMillis > j ? 1 : (timeInMillis == j ? 0 : -1));
        sb.append(i <= 0 && j < j2);
        k.b("MI_STAT_TEST", sb.toString());
        return i <= 0 && j < j2;
    }
}
