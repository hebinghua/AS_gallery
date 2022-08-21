package com.baidu.platform.comapi.map;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.baidu.platform.comapi.map.ah;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ar implements ag {
    public boolean b;
    private int c;
    private a d;
    private WeakReference<SurfaceView> g;
    private boolean e = true;
    private final WeakReference<ar> f = new WeakReference<>(this);
    public ap a = null;
    private int h = 60;

    /* loaded from: classes.dex */
    public class a extends Thread {
        private WeakReference<ar> b;
        private boolean e;
        private boolean g;
        private ap m;
        private SurfaceHolder n;
        private Runnable p;
        private long r;
        private Object c = new Object();
        private boolean f = false;
        private boolean h = false;
        private ArrayList<Runnable> o = new ArrayList<>();
        private AtomicBoolean q = new AtomicBoolean(false);
        private boolean d = false;
        private int i = 0;
        private int j = 0;
        private boolean l = true;
        private int k = 1;

        public a(WeakReference<ar> weakReference) {
            this.r = 0L;
            this.b = weakReference;
            ar arVar = weakReference.get();
            this.m = arVar.a;
            SurfaceHolder a = arVar.a();
            this.n = a;
            this.r = VulkanDetect.getNativeWindow(a.getSurface());
            setPriority(10);
        }

        /* JADX WARN: Code restructure failed: missing block: B:32:0x0065, code lost:
            r0 = r7.p;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x0068, code lost:
            if (r0 == null) goto L65;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x006a, code lost:
            r7.p = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x006d, code lost:
            r0 = null;
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x006e, code lost:
            if (r1 <= 0) goto L64;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x0070, code lost:
            if (r2 <= 0) goto L63;
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x0072, code lost:
            r1 = java.lang.System.currentTimeMillis();
            r7.m.a(r7);
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x007b, code lost:
            if (r0 == null) goto L45;
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x007d, code lost:
            r0.run();
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x0080, code lost:
            r0 = ((com.baidu.platform.comapi.map.ar) r7.a.f.get()).e();
            r3 = java.lang.System.currentTimeMillis();
         */
        /* JADX WARN: Code restructure failed: missing block: B:42:0x0096, code lost:
            if (r0 >= 60) goto L62;
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x0098, code lost:
            if (r0 <= 0) goto L61;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x009a, code lost:
            r5 = (1000 / r0) - (r3 - r1);
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x00a4, code lost:
            if (r5 <= 1) goto L60;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x00a6, code lost:
            r0 = r7.c;
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x00a8, code lost:
            monitor-enter(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:48:0x00a9, code lost:
            r7.c.wait(r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x00ae, code lost:
            monitor-exit(r0);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void f() throws java.lang.InterruptedException {
            /*
                r7 = this;
            L0:
                boolean r0 = r7.d     // Catch: java.lang.Throwable -> Lbf
                if (r0 != 0) goto Lb7
                java.lang.Object r0 = r7.c     // Catch: java.lang.Throwable -> Lbf
                monitor-enter(r0)     // Catch: java.lang.Throwable -> Lbf
            L7:
                java.lang.Runnable r1 = r7.h()     // Catch: java.lang.Throwable -> Lb4
                if (r1 == 0) goto L11
                r1.run()     // Catch: java.lang.Throwable -> Lb4
                goto L7
            L11:
                boolean r1 = r7.g()     // Catch: java.lang.Throwable -> Lb4
                if (r1 == 0) goto L40
                boolean r1 = r7.f     // Catch: java.lang.Throwable -> Lb4
                r2 = 1
                if (r1 != 0) goto L27
                boolean r1 = r7.g     // Catch: java.lang.Throwable -> Lb4
                if (r1 != 0) goto L27
                r7.g = r2     // Catch: java.lang.Throwable -> Lb4
                java.lang.Object r1 = r7.c     // Catch: java.lang.Throwable -> Lb4
                r1.notifyAll()     // Catch: java.lang.Throwable -> Lb4
            L27:
                com.baidu.platform.comapi.map.ar r1 = com.baidu.platform.comapi.map.ar.this     // Catch: java.lang.Throwable -> Lb4
                boolean r1 = com.baidu.platform.comapi.map.ar.a(r1)     // Catch: java.lang.Throwable -> Lb4
                if (r1 == 0) goto L3a
                boolean r1 = r7.h     // Catch: java.lang.Throwable -> Lb4
                if (r1 != 0) goto L3a
                r7.h = r2     // Catch: java.lang.Throwable -> Lb4
                java.lang.Object r1 = r7.c     // Catch: java.lang.Throwable -> Lb4
                r1.notifyAll()     // Catch: java.lang.Throwable -> Lb4
            L3a:
                java.lang.Object r1 = r7.c     // Catch: java.lang.Throwable -> Lb4
                r1.wait()     // Catch: java.lang.Throwable -> Lb4
                goto L11
            L40:
                boolean r1 = r7.d     // Catch: java.lang.Throwable -> Lb4
                if (r1 == 0) goto L47
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb4
                goto Lb7
            L47:
                com.baidu.platform.comapi.map.ar r1 = com.baidu.platform.comapi.map.ar.this     // Catch: java.lang.Throwable -> Lb4
                com.baidu.platform.comapi.map.ar.a(r1)     // Catch: java.lang.Throwable -> Lb4
                int r1 = r7.i     // Catch: java.lang.Throwable -> Lb4
                int r2 = r7.j     // Catch: java.lang.Throwable -> Lb4
                com.baidu.platform.comapi.map.ar r3 = com.baidu.platform.comapi.map.ar.this     // Catch: java.lang.Throwable -> Lb4
                r4 = 0
                com.baidu.platform.comapi.map.ar.a(r3, r4)     // Catch: java.lang.Throwable -> Lb4
                r7.h = r4     // Catch: java.lang.Throwable -> Lb4
                r7.l = r4     // Catch: java.lang.Throwable -> Lb4
                boolean r3 = r7.f     // Catch: java.lang.Throwable -> Lb4
                if (r3 == 0) goto L64
                boolean r3 = r7.g     // Catch: java.lang.Throwable -> Lb4
                if (r3 == 0) goto L64
                r7.g = r4     // Catch: java.lang.Throwable -> Lb4
            L64:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb4
                java.lang.Runnable r0 = r7.p     // Catch: java.lang.Throwable -> Lbf
                r3 = 0
                if (r0 == 0) goto L6d
                r7.p = r3     // Catch: java.lang.Throwable -> Lbf
                goto L6e
            L6d:
                r0 = r3
            L6e:
                if (r1 <= 0) goto L0
                if (r2 <= 0) goto L0
                long r1 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> Lbf
                com.baidu.platform.comapi.map.ap r3 = r7.m     // Catch: java.lang.Throwable -> Lbf
                r3.a(r7)     // Catch: java.lang.Throwable -> Lbf
                if (r0 == 0) goto L80
                r0.run()     // Catch: java.lang.Throwable -> Lbf
            L80:
                com.baidu.platform.comapi.map.ar r0 = com.baidu.platform.comapi.map.ar.this     // Catch: java.lang.Throwable -> Lbf
                java.lang.ref.WeakReference r0 = com.baidu.platform.comapi.map.ar.b(r0)     // Catch: java.lang.Throwable -> Lbf
                java.lang.Object r0 = r0.get()     // Catch: java.lang.Throwable -> Lbf
                com.baidu.platform.comapi.map.ar r0 = (com.baidu.platform.comapi.map.ar) r0     // Catch: java.lang.Throwable -> Lbf
                int r0 = r0.e()     // Catch: java.lang.Throwable -> Lbf
                long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> Lbf
                r5 = 60
                if (r0 >= r5) goto L0
                if (r0 <= 0) goto L0
                r5 = 1000(0x3e8, float:1.401E-42)
                int r5 = r5 / r0
                long r5 = (long) r5     // Catch: java.lang.Throwable -> Lbf
                long r3 = r3 - r1
                long r5 = r5 - r3
                r0 = 1
                int r0 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
                if (r0 <= 0) goto L0
                java.lang.Object r0 = r7.c     // Catch: java.lang.Throwable -> Lbf
                monitor-enter(r0)     // Catch: java.lang.Throwable -> Lbf
                java.lang.Object r1 = r7.c     // Catch: java.lang.Throwable -> Lb1
                r1.wait(r5)     // Catch: java.lang.Throwable -> Lb1
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb1
                goto L0
            Lb1:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb1
                throw r1     // Catch: java.lang.Throwable -> Lbf
            Lb4:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> Lb4
                throw r1     // Catch: java.lang.Throwable -> Lbf
            Lb7:
                java.lang.String r0 = "VulkanSurfaceView"
                java.lang.String r1 = "destroySurface"
                android.util.Log.i(r0, r1)
                return
            Lbf:
                r0 = move-exception
                java.lang.String r1 = "VulkanSurfaceView"
                java.lang.String r2 = "destroySurface"
                android.util.Log.i(r1, r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.platform.comapi.map.ar.a.f():void");
        }

        private boolean g() {
            if (this.d) {
                return false;
            }
            return this.e || !this.f || ar.this.e || this.i <= 0 || this.j <= 0 || (!this.l && this.k != 1);
        }

        private Runnable h() {
            synchronized (this) {
                if (this.o.size() > 0) {
                    return this.o.remove(0);
                }
                return null;
            }
        }

        public int a() {
            int i;
            synchronized (this.c) {
                i = this.k;
            }
            return i;
        }

        public void a(int i) {
            if (i < 0 || i > 1) {
                throw new IllegalArgumentException("renderMode");
            }
            synchronized (this.c) {
                this.k = i;
                if (i == 1) {
                    this.c.notifyAll();
                }
            }
        }

        public void a(SurfaceHolder surfaceHolder) {
            synchronized (this.c) {
                long nativeWindow = VulkanDetect.getNativeWindow(surfaceHolder.getSurface());
                if (this.r != nativeWindow) {
                    this.r = nativeWindow;
                    this.q.set(true);
                    this.m.a(surfaceHolder, 1, 1, 1);
                }
                this.f = true;
                this.c.notifyAll();
            }
        }

        public void a(SurfaceHolder surfaceHolder, int i, int i2) {
            synchronized (this.c) {
                this.i = i;
                this.j = i2;
                ar.this.e = true;
                this.c.notifyAll();
                while (!this.h && isAlive()) {
                    try {
                        this.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
                this.m.a(i, i2);
                ar.this.e = false;
                this.c.notifyAll();
            }
        }

        public void a(Runnable runnable) {
            synchronized (this.c) {
                if (Thread.currentThread() == this) {
                    return;
                }
                this.l = true;
                this.p = runnable;
                this.c.notifyAll();
            }
        }

        public void b() {
            synchronized (this.c) {
                this.l = true;
                this.c.notifyAll();
            }
        }

        public void b(SurfaceHolder surfaceHolder) {
            synchronized (this.c) {
                this.f = false;
                this.c.notifyAll();
                while (!this.g && isAlive()) {
                    try {
                        this.c.wait();
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
                this.m.a(surfaceHolder);
            }
        }

        public void b(Runnable runnable) {
            synchronized (this) {
                this.o.add(runnable);
            }
        }

        public void c() {
            synchronized (this.c) {
                this.e = true;
            }
        }

        public void d() {
            synchronized (this.c) {
                this.e = false;
                this.c.notifyAll();
            }
        }

        public void e() {
            synchronized (this.c) {
                this.d = true;
                this.c.notifyAll();
            }
            try {
                join();
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            setName("VkThread " + getId());
            try {
                f();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ar(SurfaceView surfaceView) {
        this.g = new WeakReference<>(surfaceView);
    }

    private void c() {
        if (this.d == null) {
            return;
        }
        throw new IllegalStateException("setRenderer has already been called for this instance.");
    }

    @Override // com.baidu.platform.comapi.map.ag
    public Bitmap a(int i, int i2, int i3, int i4, Object obj, Bitmap.Config config) {
        return null;
    }

    public SurfaceHolder a() {
        return this.g.get().getHolder();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void a(int i) {
        if (i <= 0) {
            return;
        }
        if (i > 60) {
            i = 60;
        }
        this.h = i;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void a(ap apVar) {
        c();
        this.a = apVar;
        a aVar = new a(this.f);
        this.d = aVar;
        aVar.start();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void a(Runnable runnable) {
        this.d.b(runnable);
    }

    @Override // com.baidu.platform.comapi.map.ag
    public ah.a b() {
        return ah.a.VULKAN;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void b(int i) {
        this.c = i;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void d(int i) {
        this.d.a(i);
    }

    @Override // com.baidu.platform.comapi.map.ag
    public int e() {
        return this.h;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public int f() {
        return this.c;
    }

    public void finalize() throws Throwable {
        try {
            a aVar = this.d;
            if (aVar != null) {
                aVar.e();
            }
        } finally {
            super.finalize();
        }
    }

    @Override // com.baidu.platform.comapi.map.ag
    public int g() {
        return this.d.a();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void h() {
        this.d.b();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void i() {
        this.d.c();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void j() {
        this.d.d();
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void k() {
        if (this.b && this.a != null) {
            a aVar = this.d;
            int a2 = aVar != null ? aVar.a() : 1;
            a aVar2 = new a(this.f);
            this.d = aVar2;
            if (a2 != 1) {
                aVar2.a(a2);
            }
            this.d.start();
        }
        this.b = false;
    }

    @Override // com.baidu.platform.comapi.map.ag
    public void l() {
        a aVar = this.d;
        if (aVar != null) {
            aVar.e();
        }
        this.b = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.d.a(surfaceHolder, i2, i3);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.d.a(surfaceHolder);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.d.b(surfaceHolder);
    }

    @Override // android.view.SurfaceHolder.Callback2
    @Deprecated
    public void surfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
    }

    @Override // android.view.SurfaceHolder.Callback2
    public void surfaceRedrawNeededAsync(SurfaceHolder surfaceHolder, Runnable runnable) {
        a aVar = this.d;
        if (aVar != null) {
            aVar.a(runnable);
        }
    }
}
