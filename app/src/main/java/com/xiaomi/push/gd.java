package com.xiaomi.push;

import android.os.SystemClock;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.xiaomi.push.service.XMPushService;
import java.io.IOException;
import java.net.Socket;

/* loaded from: classes3.dex */
public abstract class gd extends fw {
    public Exception a;

    /* renamed from: a  reason: collision with other field name */
    public Socket f407a;
    public XMPushService b;
    public int c;

    /* renamed from: c  reason: collision with other field name */
    public String f408c;
    public String d;
    public volatile long e;
    public volatile long f;
    public volatile long g;
    public long h;

    public gd(XMPushService xMPushService, fx fxVar) {
        super(xMPushService, fxVar);
        this.a = null;
        this.f408c = null;
        this.e = 0L;
        this.f = 0L;
        this.g = 0L;
        this.h = 0L;
        this.b = xMPushService;
    }

    @Override // com.xiaomi.push.fw
    public cr a(String str) {
        cr a = cv.a().a(str, false);
        if (!a.b()) {
            gz.a(new gg(this, str));
        }
        return a;
    }

    @Override // com.xiaomi.push.fw
    /* renamed from: a */
    public String mo2190a() {
        return this.d;
    }

    @Override // com.xiaomi.push.fw
    /* renamed from: a  reason: collision with other method in class */
    public Socket mo2190a() {
        return new Socket();
    }

    @Override // com.xiaomi.push.fw
    /* renamed from: a */
    public synchronized void mo2190a() {
        throw null;
    }

    public synchronized void a(int i, Exception exc) {
        if (b() == 2) {
            return;
        }
        a(2, i, exc);
        ((fw) this).f394a = "";
        try {
            this.f407a.close();
        } catch (Throwable unused) {
        }
        this.e = 0L;
        this.f = 0L;
    }

    public final void a(fx fxVar) {
        a(fxVar.c(), fxVar.mo2185a());
    }

    public void a(Exception exc) {
        if (SystemClock.elapsedRealtime() - this.g < 300000) {
            if (!bj.b(this.b)) {
                return;
            }
            int i = this.c + 1;
            this.c = i;
            if (i < 2) {
                return;
            }
            String mo2190a = mo2190a();
            com.xiaomi.channel.commonutils.logger.b.m1859a("max short conn time reached, sink down current host:" + mo2190a);
            a(mo2190a, 0L, exc);
        }
        this.c = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:115:0x0276 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0276 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x027a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01a7  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x01a9  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02c2  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x02db  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02f0  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x02fd  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0300  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0351  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a(java.lang.String r32, int r33) {
        /*
            Method dump skipped, instructions count: 860
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.gd.a(java.lang.String, int):void");
    }

    public void a(String str, long j, Exception exc) {
        cr a = cv.a().a(fx.a(), false);
        if (a != null) {
            a.b(str, j, 0L, exc);
            cv.a().m2033c();
        }
    }

    /* renamed from: a */
    public abstract void mo2171a(boolean z);

    @Override // com.xiaomi.push.fw
    public void b(int i, Exception exc) {
        a(i, exc);
        if ((exc != null || i == 18) && this.g != 0) {
            a(exc);
        }
    }

    @Override // com.xiaomi.push.fw
    public void b(boolean z) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long currentTimeMillis = System.currentTimeMillis();
        mo2171a(z);
        com.xiaomi.push.service.o.a(this.b).m2526c();
        if (!z) {
            this.b.a(new ge(this, 13, elapsedRealtime, currentTimeMillis), AbstractComponentTracker.LINGERING_TIMEOUT);
        }
    }

    @Override // com.xiaomi.push.fw
    public String c() {
        return ((fw) this).f394a;
    }

    public void c(int i, Exception exc) {
        this.b.a(new gf(this, 2, i, exc));
    }

    public synchronized void e() {
        try {
            if (!m2184c() && !m2183b()) {
                a(0, 0, (Exception) null);
                a(((fw) this).f391a);
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("WARNING: current xmpp has connected");
        } catch (IOException e) {
            throw new gh(e);
        }
    }

    public void f() {
        this.e = SystemClock.elapsedRealtime();
    }

    public void g() {
        this.f = SystemClock.elapsedRealtime();
    }
}
