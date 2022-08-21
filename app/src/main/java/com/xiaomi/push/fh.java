package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.bl;
import com.xiaomi.push.jl;
import com.xiaomi.push.service.XMPushService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/* loaded from: classes3.dex */
public class fh {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public long f348a;

    /* renamed from: a  reason: collision with other field name */
    public fg f350a;

    /* renamed from: a  reason: collision with other field name */
    public String f351a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f352a = false;

    /* renamed from: a  reason: collision with other field name */
    public bl f349a = bl.a();

    /* loaded from: classes3.dex */
    public static class a {
        public static final fh a = new fh();
    }

    public static fg a() {
        fg fgVar;
        fh fhVar = a.a;
        synchronized (fhVar) {
            fgVar = fhVar.f350a;
        }
        return fgVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static fh m2151a() {
        return a.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized fa m2152a() {
        fa faVar;
        faVar = new fa();
        faVar.a(bj.m1969a((Context) this.f350a.f345a));
        faVar.f322a = (byte) 0;
        faVar.f326b = 1;
        faVar.d((int) (System.currentTimeMillis() / 1000));
        return faVar;
    }

    public final fa a(bl.a aVar) {
        if (aVar.f132a == 0) {
            Object obj = aVar.f133a;
            if (!(obj instanceof fa)) {
                return null;
            }
            return (fa) obj;
        }
        fa m2152a = m2152a();
        m2152a.a(ez.CHANNEL_STATS_COUNTER.a());
        m2152a.c(aVar.f132a);
        m2152a.c(aVar.f134a);
        return m2152a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized fb m2153a() {
        fb fbVar;
        fbVar = null;
        if (b()) {
            int i = 750;
            if (!bj.e(this.f350a.f345a)) {
                i = 375;
            }
            fbVar = a(i);
        }
        return fbVar;
    }

    public final fb a(int i) {
        ArrayList arrayList = new ArrayList();
        fb fbVar = new fb(this.f351a, arrayList);
        if (!bj.e(this.f350a.f345a)) {
            fbVar.a(j.k(this.f350a.f345a));
        }
        jn jnVar = new jn(i);
        jf a2 = new jl.a().a(jnVar);
        try {
            fbVar.b(a2);
        } catch (iz unused) {
        }
        LinkedList<bl.a> m1975a = this.f349a.m1975a();
        while (m1975a.size() > 0) {
            try {
                fa a3 = a(m1975a.getLast());
                if (a3 != null) {
                    a3.b(a2);
                }
                if (jnVar.a_() > i) {
                    break;
                }
                if (a3 != null) {
                    arrayList.add(a3);
                }
                m1975a.removeLast();
            } catch (iz | NoSuchElementException unused2) {
            }
        }
        return fbVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final void m2154a() {
        if (!this.f352a || System.currentTimeMillis() - this.f348a <= this.a) {
            return;
        }
        this.f352a = false;
        this.f348a = 0L;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2155a(int i) {
        if (i > 0) {
            int i2 = i * 1000;
            if (i2 > 604800000) {
                i2 = 604800000;
            }
            if (this.a == i2 && this.f352a) {
                return;
            }
            this.f352a = true;
            this.f348a = System.currentTimeMillis();
            this.a = i2;
            com.xiaomi.channel.commonutils.logger.b.c("enable dot duration = " + i2 + " start = " + this.f348a);
        }
    }

    public synchronized void a(fa faVar) {
        this.f349a.a(faVar);
    }

    public synchronized void a(XMPushService xMPushService) {
        this.f350a = new fg(xMPushService);
        this.f351a = "";
        com.xiaomi.push.service.bv.a().a(new fi(this));
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2156a() {
        return this.f352a;
    }

    public boolean b() {
        m2154a();
        return this.f352a && this.f349a.m1974a() > 0;
    }
}
