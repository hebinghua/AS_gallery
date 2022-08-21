package com.xiaomi.push;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class ao {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public Handler f92a;

    /* renamed from: a  reason: collision with other field name */
    public a f93a;

    /* renamed from: a  reason: collision with other field name */
    public volatile b f94a;

    /* renamed from: a  reason: collision with other field name */
    public volatile boolean f95a;
    public final boolean b;

    /* loaded from: classes3.dex */
    public class a extends Thread {

        /* renamed from: a  reason: collision with other field name */
        public final LinkedBlockingQueue<b> f96a;

        public a() {
            super("PackageProcessor");
            this.f96a = new LinkedBlockingQueue<>();
        }

        public final void a(int i, b bVar) {
            try {
                ao.this.f92a.sendMessage(ao.this.f92a.obtainMessage(i, bVar));
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
            }
        }

        public void a(b bVar) {
            try {
                this.f96a.add(bVar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            long j = ao.this.a > 0 ? ao.this.a : Long.MAX_VALUE;
            while (!ao.this.f95a) {
                try {
                    b poll = this.f96a.poll(j, TimeUnit.SECONDS);
                    ao.this.f94a = poll;
                    if (poll != null) {
                        a(0, poll);
                        poll.mo2039b();
                        a(1, poll);
                    } else if (ao.this.a > 0) {
                        ao.this.a();
                    }
                } catch (InterruptedException e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class b {
        public void a() {
        }

        /* renamed from: b */
        public abstract void mo2039b();

        /* renamed from: c */
        public void mo2040c() {
        }
    }

    public ao(boolean z) {
        this(z, 0);
    }

    public ao(boolean z, int i) {
        this.f92a = null;
        this.f95a = false;
        this.a = 0;
        this.f92a = new ap(this, Looper.getMainLooper());
        this.b = z;
        this.a = i;
    }

    public final synchronized void a() {
        this.f93a = null;
        this.f95a = true;
    }

    public synchronized void a(b bVar) {
        if (this.f93a == null) {
            a aVar = new a();
            this.f93a = aVar;
            aVar.setDaemon(this.b);
            this.f95a = false;
            this.f93a.start();
        }
        this.f93a.a(bVar);
    }

    public void a(b bVar, long j) {
        this.f92a.postDelayed(new aq(this, bVar), j);
    }
}
