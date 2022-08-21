package com.xiaomi.stat.c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import ch.qos.logback.core.util.FileSize;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.p;
import com.xiaomi.stat.d.r;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes3.dex */
public class g extends Handler {
    private static final String c = "UploadTimer";
    private static final int d = 15000;
    private static final int e = 5;
    private static final int f = 86400;
    private static final int g = 1;
    private static final int h = 2;
    private static final int i = 3;
    public AtomicBoolean a;
    public BroadcastReceiver b;
    private int j;
    private int k;
    private long l;
    private boolean m;
    private int n;

    private int a(int i2) {
        if (i2 < 0) {
            return 0;
        }
        if (i2 > 0 && i2 < 5) {
            return 5;
        }
        return i2 > f ? f : i2;
    }

    public g(Looper looper) {
        super(looper);
        this.j = 300000;
        this.a = new AtomicBoolean(true);
        this.k = 15000;
        this.l = r.b();
        this.m = true;
        this.b = new h(this);
        this.k = 60000;
        sendEmptyMessageDelayed(1, 60000);
        a(ak.a());
        k.b(c, " UploadTimer: " + this.k);
    }

    private int f() {
        int a = a(com.xiaomi.stat.b.m());
        if (a > 0) {
            return a * 1000;
        }
        int a2 = a(com.xiaomi.stat.b.j());
        if (a2 <= 0) {
            return 15000;
        }
        return a2 * 1000;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        int i2 = message.what;
        if (i2 == 1) {
            g();
            sendEmptyMessageDelayed(1, this.k);
        } else if (i2 == 2) {
            i();
        } else if (i2 != 3) {
        } else {
            h();
        }
    }

    private void g() {
        i.a().c();
        e();
    }

    public long a() {
        return this.k;
    }

    public void a(boolean z) {
        if (!z && !this.m) {
            b();
        }
        this.m = false;
    }

    public void b() {
        if (this.k == this.n) {
            return;
        }
        int f2 = f();
        this.n = f2;
        this.k = f2;
        if (r.b() - this.l > ((long) this.k)) {
            removeMessages(1);
            sendEmptyMessageDelayed(1, this.k);
            this.l = r.b();
        }
        k.b(c, " resetBackgroundInterval: " + this.k);
    }

    private void a(Context context) {
        if (context == null) {
            return;
        }
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            context.registerReceiver(this.b, intentFilter);
        } catch (Exception e2) {
            k.b(c, "registerNetReceiver: " + e2);
        }
    }

    private void h() {
        if (l.a()) {
            b();
        }
    }

    public void b(boolean z) {
        if (z) {
            b();
        }
        long c2 = com.xiaomi.stat.a.c.a().c();
        int i2 = (c2 > 0L ? 1 : (c2 == 0L ? 0 : -1));
        if (i2 == 0) {
            this.a.set(true);
        }
        k.b(c, " totalCount=" + c2 + " deleteData=" + z);
        int i3 = this.k;
        if (i3 >= this.j) {
            return;
        }
        if (i2 != 0 && z) {
            return;
        }
        this.k = i3 + 15000;
    }

    public void c() {
        this.k = this.j;
    }

    public void d() {
        if (!this.a.get()) {
            return;
        }
        sendEmptyMessage(2);
    }

    private void i() {
        int i2 = (com.xiaomi.stat.a.c.a().c() > 0L ? 1 : (com.xiaomi.stat.a.c.a().c() == 0L ? 0 : -1));
        if (i2 < 0) {
            return;
        }
        if (i2 > 0) {
            b();
            this.a.set(false);
        } else {
            this.a.set(true);
        }
        k.b(c, " checkDatabase mIsDatabaseEmpty=" + this.a.get());
    }

    public void e() {
        try {
            Context a = ak.a();
            long n = p.n(a);
            long m = p.m(a);
            long totalRxBytes = TrafficStats.getTotalRxBytes() == -1 ? 0L : TrafficStats.getTotalRxBytes() / FileSize.KB_COEFFICIENT;
            long b = r.b();
            p.e(a, b);
            p.d(a, totalRxBytes);
            p.a(a, ((float) ((totalRxBytes - m) * 1000)) / ((float) (b - n)));
        } catch (Throwable unused) {
        }
    }
}
