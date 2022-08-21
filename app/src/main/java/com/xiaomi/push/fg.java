package com.xiaomi.push;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import com.xiaomi.push.service.XMPushService;

/* loaded from: classes3.dex */
public class fg implements fz {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public fw f344a;

    /* renamed from: a  reason: collision with other field name */
    public XMPushService f345a;

    /* renamed from: a  reason: collision with other field name */
    public Exception f346a;
    public long e;
    public long f;

    /* renamed from: a  reason: collision with other field name */
    public long f343a = 0;
    public long b = 0;
    public long c = 0;
    public long d = 0;

    /* renamed from: a  reason: collision with other field name */
    public String f347a = "";

    public fg(XMPushService xMPushService) {
        this.e = 0L;
        this.f = 0L;
        this.f345a = xMPushService;
        b();
        int myUid = Process.myUid();
        try {
            this.f = TrafficStats.getUidRxBytes(myUid);
            this.e = TrafficStats.getUidTxBytes(myUid);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Failed to obtain traffic data during initialization: " + e);
            this.f = -1L;
            this.e = -1L;
        }
    }

    public Exception a() {
        return this.f346a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m2150a() {
        XMPushService xMPushService = this.f345a;
        if (xMPushService == null) {
            return;
        }
        String m1969a = bj.m1969a((Context) xMPushService);
        boolean c = bj.c(this.f345a);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.f343a;
        if (j > 0) {
            this.b += elapsedRealtime - j;
            this.f343a = 0L;
        }
        long j2 = this.c;
        if (j2 != 0) {
            this.d += elapsedRealtime - j2;
            this.c = 0L;
        }
        if (c) {
            if ((!TextUtils.equals(this.f347a, m1969a) && this.b > 30000) || this.b > 5400000) {
                c();
            }
            this.f347a = m1969a;
            if (this.f343a == 0) {
                this.f343a = elapsedRealtime;
            }
            if (this.f345a.m2428c()) {
                this.c = elapsedRealtime;
            }
        }
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar) {
        this.a = 0;
        this.f346a = null;
        this.f344a = fwVar;
        this.f347a = bj.m1969a((Context) this.f345a);
        fj.a(0, ez.CONN_SUCCESS.a());
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar, int i, Exception exc) {
        long j;
        if (this.a == 0 && this.f346a == null) {
            this.a = i;
            this.f346a = exc;
            fj.b(fwVar.m2177a(), exc);
        }
        if (i == 22 && this.c != 0) {
            long a = fwVar.a() - this.c;
            if (a < 0) {
                a = 0;
            }
            this.d += a + (gc.b() / 2);
            this.c = 0L;
        }
        m2150a();
        int myUid = Process.myUid();
        long j2 = -1;
        try {
            j2 = TrafficStats.getUidRxBytes(myUid);
            j = TrafficStats.getUidTxBytes(myUid);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Failed to obtain traffic data: " + e);
            j = -1L;
        }
        com.xiaomi.channel.commonutils.logger.b.c("Stats rx=" + (j2 - this.f) + ", tx=" + (j - this.e));
        this.f = j2;
        this.e = j;
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar, Exception exc) {
        fj.a(0, ez.CHANNEL_CON_FAIL.a(), 1, fwVar.m2177a(), bj.c(this.f345a) ? 1 : 0);
        m2150a();
    }

    public final void b() {
        this.b = 0L;
        this.d = 0L;
        this.f343a = 0L;
        this.c = 0L;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (bj.b(this.f345a)) {
            this.f343a = elapsedRealtime;
        }
        if (this.f345a.m2428c()) {
            this.c = elapsedRealtime;
        }
    }

    @Override // com.xiaomi.push.fz
    public void b(fw fwVar) {
        m2150a();
        this.c = SystemClock.elapsedRealtime();
        fj.a(0, ez.CONN_SUCCESS.a(), fwVar.m2177a(), fwVar.mo2190a());
    }

    public final synchronized void c() {
        com.xiaomi.channel.commonutils.logger.b.c("stat connpt = " + this.f347a + " netDuration = " + this.b + " ChannelDuration = " + this.d + " channelConnectedTime = " + this.c);
        fa faVar = new fa();
        faVar.f322a = (byte) 0;
        faVar.a(ez.CHANNEL_ONLINE_RATE.a());
        faVar.a(this.f347a);
        faVar.d((int) (System.currentTimeMillis() / 1000));
        faVar.b((int) (this.b / 1000));
        faVar.c((int) (this.d / 1000));
        fh.m2151a().a(faVar);
        b();
    }
}
