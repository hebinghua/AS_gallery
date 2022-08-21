package com.xiaomi.push;

import android.os.SystemClock;
import android.util.Pair;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes3.dex */
public abstract class fw {
    public static final AtomicInteger a = new AtomicInteger(0);

    /* renamed from: a  reason: collision with other field name */
    public static boolean f388a;

    /* renamed from: a  reason: collision with other field name */
    public fx f391a;

    /* renamed from: a  reason: collision with other field name */
    public XMPushService f393a;

    /* renamed from: a  reason: collision with other field name */
    public int f389a = 0;

    /* renamed from: a  reason: collision with other field name */
    public long f390a = -1;

    /* renamed from: b  reason: collision with other field name */
    public volatile long f398b = 0;

    /* renamed from: c  reason: collision with other field name */
    public volatile long f401c = 0;

    /* renamed from: a  reason: collision with other field name */
    public LinkedList<Pair<Integer, Long>> f396a = new LinkedList<>();

    /* renamed from: a  reason: collision with other field name */
    public final Collection<fz> f395a = new CopyOnWriteArrayList();

    /* renamed from: a  reason: collision with other field name */
    public final Map<gb, a> f397a = new ConcurrentHashMap();

    /* renamed from: b  reason: collision with other field name */
    public final Map<gb, a> f400b = new ConcurrentHashMap();

    /* renamed from: a  reason: collision with other field name */
    public gi f392a = null;

    /* renamed from: a  reason: collision with other field name */
    public String f394a = "";

    /* renamed from: b  reason: collision with other field name */
    public String f399b = "";
    public int c = 2;
    public final int b = a.getAndIncrement();
    public long e = 0;
    public long d = 0;

    /* loaded from: classes3.dex */
    public static class a {
        public gb a;

        /* renamed from: a  reason: collision with other field name */
        public gj f402a;

        public a(gb gbVar, gj gjVar) {
            this.a = gbVar;
            this.f402a = gjVar;
        }

        public void a(fl flVar) {
            this.a.a(flVar);
        }

        public void a(gn gnVar) {
            gj gjVar = this.f402a;
            if (gjVar == null || gjVar.mo2175a(gnVar)) {
                this.a.mo2175a(gnVar);
            }
        }
    }

    static {
        f388a = false;
        try {
            f388a = Boolean.getBoolean("smack.debugEnabled");
        } catch (Exception unused) {
        }
        gc.m2188a();
    }

    public fw(XMPushService xMPushService, fx fxVar) {
        this.f391a = fxVar;
        this.f393a = xMPushService;
        m2182b();
    }

    /* renamed from: a */
    public int mo2190a() {
        return this.f389a;
    }

    public long a() {
        return this.f401c;
    }

    /* renamed from: a  reason: collision with other method in class */
    public fx m2176a() {
        return this.f391a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2177a() {
        return this.f391a.c();
    }

    public final String a(int i) {
        return i == 1 ? "connected" : i == 0 ? "connecting" : i == 2 ? "disconnected" : "unknown";
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<gb, a> m2178a() {
        return this.f397a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final void m2179a(int i) {
        synchronized (this.f396a) {
            if (i == 1) {
                this.f396a.clear();
            } else {
                this.f396a.add(new Pair<>(Integer.valueOf(i), Long.valueOf(System.currentTimeMillis())));
                if (this.f396a.size() > 6) {
                    this.f396a.remove(0);
                }
            }
        }
    }

    public void a(int i, int i2, Exception exc) {
        int i3 = this.c;
        if (i != i3) {
            com.xiaomi.channel.commonutils.logger.b.m1859a(String.format("update the connection status. %1$s -> %2$s : %3$s ", a(i3), a(i), com.xiaomi.push.service.bk.a(i2)));
        }
        if (bj.b(this.f393a)) {
            m2179a(i);
        }
        if (i == 1) {
            this.f393a.a(10);
            if (this.c != 0) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("try set connected while not connecting.");
            }
            this.c = i;
            for (fz fzVar : this.f395a) {
                fzVar.b(this);
            }
        } else if (i == 0) {
            if (this.c != 2) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("try set connecting while not disconnected.");
            }
            this.c = i;
            for (fz fzVar2 : this.f395a) {
                fzVar2.a(this);
            }
        } else if (i == 2) {
            this.f393a.a(10);
            int i4 = this.c;
            if (i4 == 0) {
                for (fz fzVar3 : this.f395a) {
                    fzVar3.a(this, exc == null ? new CancellationException("disconnect while connecting") : exc);
                }
            } else if (i4 == 1) {
                for (fz fzVar4 : this.f395a) {
                    fzVar4.a(this, i2, exc);
                }
            }
            this.c = i;
        }
    }

    public void a(fz fzVar) {
        if (fzVar != null && !this.f395a.contains(fzVar)) {
            this.f395a.add(fzVar);
        }
    }

    public void a(gb gbVar, gj gjVar) {
        Objects.requireNonNull(gbVar, "Packet listener is null.");
        this.f397a.put(gbVar, new a(gbVar, gjVar));
    }

    public abstract void a(gn gnVar);

    public abstract void a(bg.b bVar);

    public synchronized void a(String str) {
        if (this.c == 0) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("setChallenge hash = " + bo.a(str).substring(0, 8));
            this.f394a = str;
            a(1, 0, null);
        } else {
            com.xiaomi.channel.commonutils.logger.b.m1859a("ignore setChallenge because connection was disconnected");
        }
    }

    public abstract void a(String str, String str2);

    public abstract void a(fl[] flVarArr);

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2180a() {
        return false;
    }

    public synchronized boolean a(long j) {
        return this.e >= j;
    }

    public int b() {
        return this.c;
    }

    /* renamed from: b  reason: collision with other method in class */
    public String m2181b() {
        return this.f391a.b();
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2182b() {
        String str;
        if (!this.f391a.m2186a() || this.f392a != null) {
            return;
        }
        Class<?> cls = null;
        try {
            str = System.getProperty("smack.debuggerClass");
        } catch (Throwable unused) {
            str = null;
        }
        if (str != null) {
            try {
                cls = Class.forName(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cls == null) {
            this.f392a = new fu(this);
            return;
        }
        try {
            this.f392a = (gi) cls.getConstructor(fw.class, Writer.class, Reader.class).newInstance(this);
        } catch (Exception e2) {
            throw new IllegalArgumentException("Can't initialize the configured debugger!", e2);
        }
    }

    public abstract void b(int i, Exception exc);

    public abstract void b(fl flVar);

    public void b(fz fzVar) {
        this.f395a.remove(fzVar);
    }

    public void b(gb gbVar, gj gjVar) {
        Objects.requireNonNull(gbVar, "Packet listener is null.");
        this.f400b.put(gbVar, new a(gbVar, gjVar));
    }

    public abstract void b(boolean z);

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2183b() {
        return this.c == 0;
    }

    public synchronized void c() {
        this.e = SystemClock.elapsedRealtime();
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2184c() {
        return this.c == 1;
    }

    public void d() {
        synchronized (this.f396a) {
            this.f396a.clear();
        }
    }
}
