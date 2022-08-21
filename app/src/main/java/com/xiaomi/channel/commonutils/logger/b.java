package com.xiaomi.channel.commonutils.logger;

import android.content.Context;
import android.os.Process;
import android.util.Log;
import com.xiaomi.push.m;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes3.dex */
public abstract class b {
    public static int a = 2;

    /* renamed from: a  reason: collision with other field name */
    public static Context f0a = null;

    /* renamed from: a  reason: collision with other field name */
    public static boolean f6a = false;

    /* renamed from: b  reason: collision with other field name */
    public static boolean f7b = false;

    /* renamed from: a  reason: collision with other field name */
    public static String f3a = "XMPush-" + Process.myPid();

    /* renamed from: a  reason: collision with other field name */
    public static LoggerInterface f1a = new a();

    /* renamed from: a  reason: collision with other field name */
    public static final HashMap<Integer, Long> f4a = new HashMap<>();
    public static final HashMap<Integer, String> b = new HashMap<>();

    /* renamed from: a  reason: collision with other field name */
    public static final Integer f2a = -1;

    /* renamed from: a  reason: collision with other field name */
    public static AtomicInteger f5a = new AtomicInteger(1);

    /* loaded from: classes3.dex */
    public static class a implements LoggerInterface {
        public String a = b.f3a;

        @Override // com.xiaomi.channel.commonutils.logger.LoggerInterface
        public void log(String str) {
            Log.v(this.a, str);
        }

        @Override // com.xiaomi.channel.commonutils.logger.LoggerInterface
        public void log(String str, Throwable th) {
            Log.v(this.a, str, th);
        }
    }

    public static int a() {
        return a;
    }

    public static Integer a(String str) {
        if (a <= 1) {
            Integer valueOf = Integer.valueOf(f5a.incrementAndGet());
            f4a.put(valueOf, Long.valueOf(System.currentTimeMillis()));
            b.put(valueOf, str);
            LoggerInterface loggerInterface = f1a;
            loggerInterface.log(str + " starts");
            return valueOf;
        }
        return f2a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m1858a(String str) {
        return b() + str;
    }

    public static String a(String str, String str2) {
        return "[" + str + "] " + str2;
    }

    public static void a(int i) {
        if (i < 0 || i > 5) {
            a(2, "set log level as " + i);
        }
        a = i;
    }

    public static void a(int i, String str) {
        if (i >= a) {
            f1a.log(str);
        }
    }

    public static void a(int i, String str, Throwable th) {
        if (i >= a) {
            f1a.log(str, th);
        }
    }

    public static void a(int i, Throwable th) {
        if (i >= a) {
            f1a.log("", th);
        }
    }

    public static void a(Context context) {
        f0a = context;
        if (m.m2400a(context)) {
            f6a = true;
        }
        if (m.m2399a()) {
            f7b = true;
        }
    }

    public static void a(LoggerInterface loggerInterface) {
        f1a = loggerInterface;
    }

    public static void a(Integer num) {
        if (a <= 1) {
            HashMap<Integer, Long> hashMap = f4a;
            if (!hashMap.containsKey(num)) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis() - hashMap.remove(num).longValue();
            LoggerInterface loggerInterface = f1a;
            loggerInterface.log(b.remove(num) + " ends in " + currentTimeMillis + " ms");
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m1859a(String str) {
        a(2, m1858a(str));
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m1860a(String str, String str2) {
        a(2, b(str, str2));
    }

    public static void a(String str, Throwable th) {
        a(4, m1858a(str), th);
    }

    public static void a(Throwable th) {
        a(4, th);
    }

    public static String b() {
        return "[Tid:" + Thread.currentThread().getId() + "] ";
    }

    public static String b(String str, String str2) {
        return b() + a(str, str2);
    }

    public static void b(String str) {
        a(0, m1858a(str));
    }

    public static void c(String str) {
        a(1, m1858a(str));
    }

    public static void d(String str) {
        a(4, m1858a(str));
    }

    public static void e(String str) {
        if (!f6a) {
            Log.w(f3a, m1858a(str));
            if (f7b) {
                return;
            }
        }
        m1859a(str);
    }
}
