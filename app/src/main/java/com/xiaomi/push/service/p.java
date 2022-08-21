package com.xiaomi.push.service;

import android.content.Intent;
import android.os.SystemClock;
import com.xiaomi.push.service.XMPushService;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;

/* loaded from: classes3.dex */
public class p {
    public static long a;
    public static long b;

    /* renamed from: a  reason: collision with other field name */
    public final a f970a;

    /* renamed from: a  reason: collision with other field name */
    public final c f971a;

    /* loaded from: classes3.dex */
    public static final class a {
        public final c a;

        public a(c cVar) {
            this.a = cVar;
        }

        public void finalize() {
            try {
                synchronized (this.a) {
                    this.a.c = true;
                    this.a.notify();
                }
            } finally {
                super.finalize();
            }
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class b implements Runnable {
        public int a;

        public b(int i) {
            this.a = i;
        }
    }

    /* loaded from: classes3.dex */
    public static final class c extends Thread {

        /* renamed from: b  reason: collision with other field name */
        public boolean f974b;
        public boolean c;
        public volatile long a = 0;

        /* renamed from: a  reason: collision with other field name */
        public volatile boolean f973a = false;
        public long b = 50;

        /* renamed from: a  reason: collision with other field name */
        public a f972a = new a();

        /* loaded from: classes3.dex */
        public static final class a {
            public int a;

            /* renamed from: a  reason: collision with other field name */
            public d[] f975a;
            public int b;
            public int c;

            public a() {
                this.a = 256;
                this.f975a = new d[256];
                this.b = 0;
                this.c = 0;
            }

            public final int a(d dVar) {
                int i = 0;
                while (true) {
                    d[] dVarArr = this.f975a;
                    if (i < dVarArr.length) {
                        if (dVarArr[i] == dVar) {
                            return i;
                        }
                        i++;
                    } else {
                        return -1;
                    }
                }
            }

            public d a() {
                return this.f975a[0];
            }

            /* renamed from: a  reason: collision with other method in class */
            public void m2536a() {
                this.f975a = new d[this.a];
                this.b = 0;
            }

            public void a(int i) {
                for (int i2 = 0; i2 < this.b; i2++) {
                    d[] dVarArr = this.f975a;
                    if (dVarArr[i2].a == i) {
                        dVarArr[i2].a();
                    }
                }
                b();
            }

            public void a(int i, b bVar) {
                for (int i2 = 0; i2 < this.b; i2++) {
                    d[] dVarArr = this.f975a;
                    if (dVarArr[i2].f977a == bVar) {
                        dVarArr[i2].a();
                    }
                }
                b();
            }

            /* renamed from: a  reason: collision with other method in class */
            public void m2537a(d dVar) {
                d[] dVarArr = this.f975a;
                int length = dVarArr.length;
                int i = this.b;
                if (length == i) {
                    d[] dVarArr2 = new d[i * 2];
                    System.arraycopy(dVarArr, 0, dVarArr2, 0, i);
                    this.f975a = dVarArr2;
                }
                d[] dVarArr3 = this.f975a;
                int i2 = this.b;
                this.b = i2 + 1;
                dVarArr3[i2] = dVar;
                c();
                b(dVar);
            }

            /* renamed from: a  reason: collision with other method in class */
            public boolean m2538a() {
                return this.b == 0;
            }

            /* renamed from: a  reason: collision with other method in class */
            public boolean m2539a(int i) {
                for (int i2 = 0; i2 < this.b; i2++) {
                    if (this.f975a[i2].a == i) {
                        return true;
                    }
                }
                return false;
            }

            public void b() {
                int i = 0;
                while (i < this.b) {
                    if (this.f975a[i].f979a) {
                        this.c++;
                        b(i);
                        i--;
                    }
                    i++;
                }
            }

            public void b(int i) {
                int i2;
                if (i < 0 || i >= (i2 = this.b)) {
                    return;
                }
                d[] dVarArr = this.f975a;
                int i3 = i2 - 1;
                this.b = i3;
                dVarArr[i] = dVarArr[i3];
                dVarArr[i3] = null;
                c(i);
            }

            public final void b(d dVar) {
                Intent mo2550a;
                b bVar = dVar.f977a;
                int i = bVar.a;
                if (i == 8) {
                    XMPushService.d dVar2 = (XMPushService.d) bVar;
                    if (dVar2.mo2550a().f357a == null) {
                        return;
                    }
                    dVar2.mo2550a().f357a.f882b = System.currentTimeMillis();
                    dVar2.mo2550a().f357a.b = a(dVar);
                } else if (i != 15 || (mo2550a = ((XMPushService.i) bVar).mo2550a()) == null || !"10".equals(mo2550a.getStringExtra("ext_chid"))) {
                } else {
                    mo2550a.putExtra("enqueue", System.currentTimeMillis());
                    mo2550a.putExtra("num", a(dVar));
                }
            }

            public final void c() {
                int i = this.b - 1;
                int i2 = (i - 1) / 2;
                while (true) {
                    d[] dVarArr = this.f975a;
                    if (dVarArr[i].f976a < dVarArr[i2].f976a) {
                        d dVar = dVarArr[i];
                        dVarArr[i] = dVarArr[i2];
                        dVarArr[i2] = dVar;
                        int i3 = i2;
                        i2 = (i2 - 1) / 2;
                        i = i3;
                    } else {
                        return;
                    }
                }
            }

            public final void c(int i) {
                int i2 = (i * 2) + 1;
                while (true) {
                    int i3 = this.b;
                    if (i2 >= i3 || i3 <= 0) {
                        return;
                    }
                    int i4 = i2 + 1;
                    if (i4 < i3) {
                        d[] dVarArr = this.f975a;
                        if (dVarArr[i4].f976a < dVarArr[i2].f976a) {
                            i2 = i4;
                        }
                    }
                    d[] dVarArr2 = this.f975a;
                    if (dVarArr2[i].f976a < dVarArr2[i2].f976a) {
                        return;
                    }
                    d dVar = dVarArr2[i];
                    dVarArr2[i] = dVarArr2[i2];
                    dVarArr2[i2] = dVar;
                    int i5 = i2;
                    i2 = (i2 * 2) + 1;
                    i = i5;
                }
            }
        }

        public c(String str, boolean z) {
            setName(str);
            setDaemon(z);
            start();
        }

        public synchronized void a() {
            this.f974b = true;
            this.f972a.m2536a();
            notify();
        }

        public final void a(d dVar) {
            this.f972a.m2537a(dVar);
            notify();
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m2535a() {
            return this.f973a && SystemClock.uptimeMillis() - this.a > 600000;
        }

        /* JADX WARN: Code restructure failed: missing block: B:50:0x008e, code lost:
            r10.a = android.os.SystemClock.uptimeMillis();
            r10.f973a = true;
            r2.f977a.run();
            r10.f973a = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x009f, code lost:
            r1 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x00a0, code lost:
            monitor-enter(r10);
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x00a1, code lost:
            r10.f974b = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x00a4, code lost:
            throw r1;
         */
        @Override // java.lang.Thread, java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                r10 = this;
            L0:
                monitor-enter(r10)
                boolean r0 = r10.f974b     // Catch: java.lang.Throwable -> Lae
                if (r0 == 0) goto L7
                monitor-exit(r10)     // Catch: java.lang.Throwable -> Lae
                return
            L7:
                com.xiaomi.push.service.p$c$a r0 = r10.f972a     // Catch: java.lang.Throwable -> Lae
                boolean r0 = r0.m2538a()     // Catch: java.lang.Throwable -> Lae
                if (r0 == 0) goto L1a
                boolean r0 = r10.c     // Catch: java.lang.Throwable -> Lae
                if (r0 == 0) goto L15
                monitor-exit(r10)     // Catch: java.lang.Throwable -> Lae
                return
            L15:
                r10.wait()     // Catch: java.lang.InterruptedException -> L18 java.lang.Throwable -> Lae
            L18:
                monitor-exit(r10)     // Catch: java.lang.Throwable -> Lae
                goto L0
            L1a:
                long r0 = com.xiaomi.push.service.p.a()     // Catch: java.lang.Throwable -> Lae
                com.xiaomi.push.service.p$c$a r2 = r10.f972a     // Catch: java.lang.Throwable -> Lae
                com.xiaomi.push.service.p$d r2 = r2.a()     // Catch: java.lang.Throwable -> Lae
                java.lang.Object r3 = r2.f978a     // Catch: java.lang.Throwable -> Lae
                monitor-enter(r3)     // Catch: java.lang.Throwable -> Lae
                boolean r4 = r2.f979a     // Catch: java.lang.Throwable -> Lab
                r5 = 0
                if (r4 == 0) goto L33
                com.xiaomi.push.service.p$c$a r0 = r10.f972a     // Catch: java.lang.Throwable -> Lab
                r0.b(r5)     // Catch: java.lang.Throwable -> Lab
                monitor-exit(r3)     // Catch: java.lang.Throwable -> Lab
                goto L18
            L33:
                long r6 = r2.f976a     // Catch: java.lang.Throwable -> Lab
                long r6 = r6 - r0
                monitor-exit(r3)     // Catch: java.lang.Throwable -> Lab
                r0 = 0
                int r3 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                r8 = 50
                if (r3 <= 0) goto L55
                long r0 = r10.b     // Catch: java.lang.Throwable -> Lae
                int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r2 <= 0) goto L46
                r6 = r0
            L46:
                long r0 = r0 + r8
                r10.b = r0     // Catch: java.lang.Throwable -> Lae
                r2 = 500(0x1f4, double:2.47E-321)
                int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r0 <= 0) goto L51
                r10.b = r2     // Catch: java.lang.Throwable -> Lae
            L51:
                r10.wait(r6)     // Catch: java.lang.InterruptedException -> L18 java.lang.Throwable -> Lae
                goto L18
            L55:
                r10.b = r8     // Catch: java.lang.Throwable -> Lae
                java.lang.Object r3 = r2.f978a     // Catch: java.lang.Throwable -> Lae
                monitor-enter(r3)     // Catch: java.lang.Throwable -> Lae
                com.xiaomi.push.service.p$c$a r4 = r10.f972a     // Catch: java.lang.Throwable -> La8
                com.xiaomi.push.service.p$d r4 = r4.a()     // Catch: java.lang.Throwable -> La8
                long r6 = r4.f976a     // Catch: java.lang.Throwable -> La8
                long r8 = r2.f976a     // Catch: java.lang.Throwable -> La8
                int r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r4 == 0) goto L6f
                com.xiaomi.push.service.p$c$a r4 = r10.f972a     // Catch: java.lang.Throwable -> La8
                int r4 = com.xiaomi.push.service.p.c.a.a(r4, r2)     // Catch: java.lang.Throwable -> La8
                goto L70
            L6f:
                r4 = r5
            L70:
                boolean r6 = r2.f979a     // Catch: java.lang.Throwable -> La8
                if (r6 == 0) goto L7f
                com.xiaomi.push.service.p$c$a r0 = r10.f972a     // Catch: java.lang.Throwable -> La8
                int r1 = com.xiaomi.push.service.p.c.a.a(r0, r2)     // Catch: java.lang.Throwable -> La8
                r0.b(r1)     // Catch: java.lang.Throwable -> La8
                monitor-exit(r3)     // Catch: java.lang.Throwable -> La8
                goto L18
            L7f:
                long r6 = r2.f976a     // Catch: java.lang.Throwable -> La8
                r2.a(r6)     // Catch: java.lang.Throwable -> La8
                com.xiaomi.push.service.p$c$a r6 = r10.f972a     // Catch: java.lang.Throwable -> La8
                r6.b(r4)     // Catch: java.lang.Throwable -> La8
                r2.f976a = r0     // Catch: java.lang.Throwable -> La8
                monitor-exit(r3)     // Catch: java.lang.Throwable -> La8
                monitor-exit(r10)     // Catch: java.lang.Throwable -> Lae
                r0 = 1
                long r3 = android.os.SystemClock.uptimeMillis()     // Catch: java.lang.Throwable -> L9f
                r10.a = r3     // Catch: java.lang.Throwable -> L9f
                r10.f973a = r0     // Catch: java.lang.Throwable -> L9f
                com.xiaomi.push.service.p$b r1 = r2.f977a     // Catch: java.lang.Throwable -> L9f
                r1.run()     // Catch: java.lang.Throwable -> L9f
                r10.f973a = r5     // Catch: java.lang.Throwable -> L9f
                goto L0
            L9f:
                r1 = move-exception
                monitor-enter(r10)
                r10.f974b = r0     // Catch: java.lang.Throwable -> La5
                monitor-exit(r10)     // Catch: java.lang.Throwable -> La5
                throw r1
            La5:
                r0 = move-exception
                monitor-exit(r10)     // Catch: java.lang.Throwable -> La5
                throw r0
            La8:
                r0 = move-exception
                monitor-exit(r3)     // Catch: java.lang.Throwable -> La8
                throw r0     // Catch: java.lang.Throwable -> Lae
            Lab:
                r0 = move-exception
                monitor-exit(r3)     // Catch: java.lang.Throwable -> Lab
                throw r0     // Catch: java.lang.Throwable -> Lae
            Lae:
                r0 = move-exception
                monitor-exit(r10)     // Catch: java.lang.Throwable -> Lae
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.p.c.run():void");
        }
    }

    /* loaded from: classes3.dex */
    public static class d {
        public int a;

        /* renamed from: a  reason: collision with other field name */
        public long f976a;

        /* renamed from: a  reason: collision with other field name */
        public b f977a;

        /* renamed from: a  reason: collision with other field name */
        public final Object f978a = new Object();

        /* renamed from: a  reason: collision with other field name */
        public boolean f979a;
        public long b;

        public void a(long j) {
            synchronized (this.f978a) {
                this.b = j;
            }
        }

        public boolean a() {
            boolean z;
            synchronized (this.f978a) {
                z = !this.f979a && this.f976a > 0;
                this.f979a = true;
            }
            return z;
        }
    }

    static {
        long j = 0;
        if (SystemClock.elapsedRealtime() > 0) {
            j = SystemClock.elapsedRealtime();
        }
        a = j;
        b = j;
    }

    public p(String str) {
        this(str, false);
    }

    public p(String str, boolean z) {
        Objects.requireNonNull(str, "name == null");
        c cVar = new c(str, z);
        this.f971a = cVar;
        this.f970a = new a(cVar);
    }

    public static synchronized long a() {
        long j;
        synchronized (p.class) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long j2 = b;
            if (elapsedRealtime > j2) {
                a += elapsedRealtime - j2;
            }
            b = elapsedRealtime;
            j = a;
        }
        return j;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2531a() {
        com.xiaomi.channel.commonutils.logger.b.m1859a("quit. finalizer:" + this.f970a);
        this.f971a.a();
    }

    public void a(int i) {
        synchronized (this.f971a) {
            this.f971a.f972a.a(i);
        }
    }

    public void a(int i, b bVar) {
        synchronized (this.f971a) {
            this.f971a.f972a.a(i, bVar);
        }
    }

    public void a(b bVar) {
        if (com.xiaomi.channel.commonutils.logger.b.a() >= 1 || Thread.currentThread() == this.f971a) {
            bVar.run();
        } else {
            com.xiaomi.channel.commonutils.logger.b.d("run job outside job job thread");
            throw new RejectedExecutionException("Run job outside job thread");
        }
    }

    public void a(b bVar, long j) {
        if (j >= 0) {
            b(bVar, j);
            return;
        }
        throw new IllegalArgumentException("delay < 0: " + j);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2532a() {
        return this.f971a.m2535a();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2533a(int i) {
        boolean m2539a;
        synchronized (this.f971a) {
            m2539a = this.f971a.f972a.m2539a(i);
        }
        return m2539a;
    }

    public void b() {
        synchronized (this.f971a) {
            this.f971a.f972a.m2536a();
        }
    }

    public final void b(b bVar, long j) {
        synchronized (this.f971a) {
            if (this.f971a.f974b) {
                throw new IllegalStateException("Timer was canceled");
            }
            long a2 = j + a();
            if (a2 < 0) {
                throw new IllegalArgumentException("Illegal delay to start the TimerTask: " + a2);
            }
            d dVar = new d();
            dVar.a = bVar.a;
            dVar.f977a = bVar;
            dVar.f976a = a2;
            this.f971a.a(dVar);
        }
    }
}
