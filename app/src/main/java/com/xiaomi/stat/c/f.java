package com.xiaomi.stat.c;

import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.m;

/* loaded from: classes3.dex */
public class f {
    public static final int a = 1;
    public static final int b = 2;
    public static final int c = 3;
    private static final String e = "UploadPolicy";
    public boolean d;
    private String f;

    private boolean a(int i) {
        return (i & (-32)) == 0;
    }

    public f(boolean z) {
        this.d = z;
        this.f = ak.b();
    }

    public f(String str, boolean z) {
        this.d = z;
        this.f = str;
    }

    private int b() {
        boolean b2 = m.b(ak.a());
        k.b(e, " getExperiencePlanPolicy: " + b2 + " isInternationalVersion= " + com.xiaomi.stat.b.e() + " isAnonymous= " + this.d);
        if (b2) {
            return 3;
        }
        return (!com.xiaomi.stat.b.e() || !this.d) ? 2 : 3;
    }

    private int c() {
        int e2 = com.xiaomi.stat.b.e(this.f);
        k.b(e, " getCustomPrivacyPolicy: state=" + e2);
        return e2 == 1 ? 3 : 1;
    }

    private int d() {
        if (com.xiaomi.stat.b.d(this.f)) {
            return c();
        }
        return b();
    }

    private int e() {
        int a2 = l.a(ak.a());
        int l = a(com.xiaomi.stat.b.l()) ? com.xiaomi.stat.b.l() : com.xiaomi.stat.b.i();
        StringBuilder sb = new StringBuilder();
        sb.append(" getHttpServicePolicy: currentNet= ");
        sb.append(a2);
        sb.append(" Config.getServerNetworkType= ");
        sb.append(com.xiaomi.stat.b.l());
        sb.append(" Config.getUserNetworkType()= ");
        sb.append(com.xiaomi.stat.b.i());
        sb.append(" (configNet & currentNet) == currentNet ");
        int i = l & a2;
        sb.append(i == a2);
        k.b(e, sb.toString());
        return i == a2 ? 3 : 1;
    }

    public int a() {
        return Math.min(d(), e());
    }
}
