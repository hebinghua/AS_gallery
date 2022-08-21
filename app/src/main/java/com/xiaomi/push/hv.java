package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class hv implements iu<hv, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f500a;

    /* renamed from: a  reason: collision with other field name */
    public hw f501a;

    /* renamed from: a  reason: collision with other field name */
    public hy f502a;

    /* renamed from: a  reason: collision with other field name */
    public String f503a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f504a = new BitSet(4);

    /* renamed from: a  reason: collision with other field name */
    public boolean f505a = false;

    /* renamed from: b  reason: collision with other field name */
    public long f506b;

    /* renamed from: b  reason: collision with other field name */
    public String f507b;

    /* renamed from: c  reason: collision with other field name */
    public long f508c;

    /* renamed from: c  reason: collision with other field name */
    public String f509c;

    /* renamed from: d  reason: collision with other field name */
    public String f510d;

    /* renamed from: e  reason: collision with other field name */
    public String f511e;

    /* renamed from: f  reason: collision with other field name */
    public String f512f;

    /* renamed from: g  reason: collision with other field name */
    public String f513g;

    /* renamed from: h  reason: collision with other field name */
    public String f514h;

    /* renamed from: i  reason: collision with other field name */
    public String f515i;

    /* renamed from: j  reason: collision with other field name */
    public String f516j;

    /* renamed from: k  reason: collision with other field name */
    public String f517k;

    /* renamed from: l  reason: collision with other field name */
    public String f518l;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f499a = new jk("PushMessage");
    public static final jc a = new jc("", (byte) 12, 1);
    public static final jc b = new jc("", (byte) 11, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 10, 5);
    public static final jc f = new jc("", (byte) 10, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 11, 8);
    public static final jc i = new jc("", (byte) 11, 9);
    public static final jc j = new jc("", (byte) 11, 10);
    public static final jc k = new jc("", (byte) 11, 11);
    public static final jc l = new jc("", (byte) 12, 12);
    public static final jc m = new jc("", (byte) 11, 13);
    public static final jc n = new jc("", (byte) 2, 14);
    public static final jc o = new jc("", (byte) 11, 15);
    public static final jc p = new jc("", (byte) 10, 16);
    public static final jc q = new jc("", (byte) 11, 20);
    public static final jc r = new jc("", (byte) 11, 21);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hv hvVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        int a11;
        int a12;
        int a13;
        int a14;
        int a15;
        int a16;
        int a17;
        int a18;
        int a19;
        if (!getClass().equals(hvVar.getClass())) {
            return getClass().getName().compareTo(hvVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2253a()).compareTo(Boolean.valueOf(hvVar.m2253a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2253a() && (a19 = iv.a(this.f502a, hvVar.f502a)) != 0) {
            return a19;
        }
        int compareTo2 = Boolean.valueOf(m2255b()).compareTo(Boolean.valueOf(hvVar.m2255b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2255b() && (a18 = iv.a(this.f503a, hvVar.f503a)) != 0) {
            return a18;
        }
        int compareTo3 = Boolean.valueOf(m2256c()).compareTo(Boolean.valueOf(hvVar.m2256c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2256c() && (a17 = iv.a(this.f507b, hvVar.f507b)) != 0) {
            return a17;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hvVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a16 = iv.a(this.f509c, hvVar.f509c)) != 0) {
            return a16;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hvVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a15 = iv.a(this.f500a, hvVar.f500a)) != 0) {
            return a15;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hvVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a14 = iv.a(this.f506b, hvVar.f506b)) != 0) {
            return a14;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hvVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a13 = iv.a(this.f510d, hvVar.f510d)) != 0) {
            return a13;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hvVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a12 = iv.a(this.f511e, hvVar.f511e)) != 0) {
            return a12;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hvVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a11 = iv.a(this.f512f, hvVar.f512f)) != 0) {
            return a11;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hvVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a10 = iv.a(this.f513g, hvVar.f513g)) != 0) {
            return a10;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hvVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a9 = iv.a(this.f514h, hvVar.f514h)) != 0) {
            return a9;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(hvVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a8 = iv.a(this.f501a, hvVar.f501a)) != 0) {
            return a8;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(hvVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a7 = iv.a(this.f515i, hvVar.f515i)) != 0) {
            return a7;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(hvVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a6 = iv.a(this.f505a, hvVar.f505a)) != 0) {
            return a6;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(hvVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a5 = iv.a(this.f516j, hvVar.f516j)) != 0) {
            return a5;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(hvVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a4 = iv.a(this.f508c, hvVar.f508c)) != 0) {
            return a4;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(hvVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a3 = iv.a(this.f517k, hvVar.f517k)) != 0) {
            return a3;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(hvVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (r() && (a2 = iv.a(this.f518l, hvVar.f518l)) != 0) {
            return a2;
        }
        return 0;
    }

    public long a() {
        return this.f500a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2251a() {
        return this.f503a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2252a() {
        if (this.f503a == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f507b == null) {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f509c != null) {
        } else {
            throw new jg("Required field 'payload' was not present! Struct: " + toString());
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                jfVar.f();
                m2252a();
                return;
            }
            short s = m2380a.f793a;
            if (s == 20) {
                if (b2 == 11) {
                    this.f517k = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s != 21) {
                switch (s) {
                    case 1:
                        if (b2 == 12) {
                            hy hyVar = new hy();
                            this.f502a = hyVar;
                            hyVar.a(jfVar);
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 2:
                        if (b2 == 11) {
                            this.f503a = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 3:
                        if (b2 == 11) {
                            this.f507b = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 4:
                        if (b2 == 11) {
                            this.f509c = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 5:
                        if (b2 == 10) {
                            this.f500a = jfVar.m2379a();
                            a(true);
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 6:
                        if (b2 == 10) {
                            this.f506b = jfVar.m2379a();
                            b(true);
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 7:
                        if (b2 == 11) {
                            this.f510d = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 8:
                        if (b2 == 11) {
                            this.f511e = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 9:
                        if (b2 == 11) {
                            this.f512f = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 10:
                        if (b2 == 11) {
                            this.f513g = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 11:
                        if (b2 == 11) {
                            this.f514h = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 12:
                        if (b2 == 12) {
                            hw hwVar = new hw();
                            this.f501a = hwVar;
                            hwVar.a(jfVar);
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 13:
                        if (b2 == 11) {
                            this.f515i = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 14:
                        if (b2 == 2) {
                            this.f505a = jfVar.m2389a();
                            c(true);
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 15:
                        if (b2 == 11) {
                            this.f516j = jfVar.m2385a();
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    case 16:
                        if (b2 == 10) {
                            this.f508c = jfVar.m2379a();
                            d(true);
                            break;
                        }
                        ji.a(jfVar, b2);
                        break;
                    default:
                        ji.a(jfVar, b2);
                        break;
                }
                jfVar.g();
            } else {
                if (b2 == 11) {
                    this.f518l = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            }
        }
    }

    public void a(boolean z) {
        this.f504a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2253a() {
        return this.f502a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2254a(hv hvVar) {
        if (hvVar == null) {
            return false;
        }
        boolean m2253a = m2253a();
        boolean m2253a2 = hvVar.m2253a();
        if ((m2253a || m2253a2) && (!m2253a || !m2253a2 || !this.f502a.m2271a(hvVar.f502a))) {
            return false;
        }
        boolean m2255b = m2255b();
        boolean m2255b2 = hvVar.m2255b();
        if ((m2255b || m2255b2) && (!m2255b || !m2255b2 || !this.f503a.equals(hvVar.f503a))) {
            return false;
        }
        boolean m2256c = m2256c();
        boolean m2256c2 = hvVar.m2256c();
        if ((m2256c || m2256c2) && (!m2256c || !m2256c2 || !this.f507b.equals(hvVar.f507b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hvVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f509c.equals(hvVar.f509c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = hvVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f500a != hvVar.f500a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hvVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f506b != hvVar.f506b)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hvVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f510d.equals(hvVar.f510d))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hvVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f511e.equals(hvVar.f511e))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hvVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f512f.equals(hvVar.f512f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hvVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f513g.equals(hvVar.f513g))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = hvVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f514h.equals(hvVar.f514h))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = hvVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f501a.m2263a(hvVar.f501a))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = hvVar.m();
        if ((m2 || m3) && (!m2 || !m3 || !this.f515i.equals(hvVar.f515i))) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = hvVar.n();
        if ((n2 || n3) && (!n2 || !n3 || this.f505a != hvVar.f505a)) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = hvVar.o();
        if ((o2 || o3) && (!o2 || !o3 || !this.f516j.equals(hvVar.f516j))) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = hvVar.p();
        if ((p2 || p3) && (!p2 || !p3 || this.f508c != hvVar.f508c)) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = hvVar.q();
        if ((q2 || q3) && (!q2 || !q3 || !this.f517k.equals(hvVar.f517k))) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = hvVar.r();
        if (!r2 && !r3) {
            return true;
        }
        return r2 && r3 && this.f518l.equals(hvVar.f518l);
    }

    public String b() {
        return this.f507b;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2252a();
        jfVar.a(f499a);
        if (this.f502a != null && m2253a()) {
            jfVar.a(a);
            this.f502a.b(jfVar);
            jfVar.b();
        }
        if (this.f503a != null) {
            jfVar.a(b);
            jfVar.a(this.f503a);
            jfVar.b();
        }
        if (this.f507b != null) {
            jfVar.a(c);
            jfVar.a(this.f507b);
            jfVar.b();
        }
        if (this.f509c != null) {
            jfVar.a(d);
            jfVar.a(this.f509c);
            jfVar.b();
        }
        if (e()) {
            jfVar.a(e);
            jfVar.a(this.f500a);
            jfVar.b();
        }
        if (f()) {
            jfVar.a(f);
            jfVar.a(this.f506b);
            jfVar.b();
        }
        if (this.f510d != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f510d);
            jfVar.b();
        }
        if (this.f511e != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f511e);
            jfVar.b();
        }
        if (this.f512f != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f512f);
            jfVar.b();
        }
        if (this.f513g != null && j()) {
            jfVar.a(j);
            jfVar.a(this.f513g);
            jfVar.b();
        }
        if (this.f514h != null && k()) {
            jfVar.a(k);
            jfVar.a(this.f514h);
            jfVar.b();
        }
        if (this.f501a != null && l()) {
            jfVar.a(l);
            this.f501a.b(jfVar);
            jfVar.b();
        }
        if (this.f515i != null && m()) {
            jfVar.a(m);
            jfVar.a(this.f515i);
            jfVar.b();
        }
        if (n()) {
            jfVar.a(n);
            jfVar.a(this.f505a);
            jfVar.b();
        }
        if (this.f516j != null && o()) {
            jfVar.a(o);
            jfVar.a(this.f516j);
            jfVar.b();
        }
        if (p()) {
            jfVar.a(p);
            jfVar.a(this.f508c);
            jfVar.b();
        }
        if (this.f517k != null && q()) {
            jfVar.a(q);
            jfVar.a(this.f517k);
            jfVar.b();
        }
        if (this.f518l != null && r()) {
            jfVar.a(r);
            jfVar.a(this.f518l);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f504a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2255b() {
        return this.f503a != null;
    }

    public String c() {
        return this.f509c;
    }

    public void c(boolean z) {
        this.f504a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2256c() {
        return this.f507b != null;
    }

    public void d(boolean z) {
        this.f504a.set(3, z);
    }

    public boolean d() {
        return this.f509c != null;
    }

    public boolean e() {
        return this.f504a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hv)) {
            return m2254a((hv) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f504a.get(1);
    }

    public boolean g() {
        return this.f510d != null;
    }

    public boolean h() {
        return this.f511e != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f512f != null;
    }

    public boolean j() {
        return this.f513g != null;
    }

    public boolean k() {
        return this.f514h != null;
    }

    public boolean l() {
        return this.f501a != null;
    }

    public boolean m() {
        return this.f515i != null;
    }

    public boolean n() {
        return this.f504a.get(2);
    }

    public boolean o() {
        return this.f516j != null;
    }

    public boolean p() {
        return this.f504a.get(3);
    }

    public boolean q() {
        return this.f517k != null;
    }

    public boolean r() {
        return this.f518l != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("PushMessage(");
        if (m2253a()) {
            sb.append("to:");
            hy hyVar = this.f502a;
            if (hyVar == null) {
                sb.append("null");
            } else {
                sb.append(hyVar);
            }
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            sb.append(", ");
        }
        sb.append("id:");
        String str = this.f503a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(str);
        }
        sb.append(", ");
        sb.append("appId:");
        String str2 = this.f507b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("payload:");
        String str3 = this.f509c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        if (e()) {
            sb.append(", ");
            sb.append("createAt:");
            sb.append(this.f500a);
        }
        if (f()) {
            sb.append(", ");
            sb.append("ttl:");
            sb.append(this.f506b);
        }
        if (g()) {
            sb.append(", ");
            sb.append("collapseKey:");
            String str4 = this.f510d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("packageName:");
            String str5 = this.f511e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("regId:");
            String str6 = this.f512f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("category:");
            String str7 = this.f513g;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        if (k()) {
            sb.append(", ");
            sb.append("topic:");
            String str8 = this.f514h;
            if (str8 == null) {
                sb.append("null");
            } else {
                sb.append(str8);
            }
        }
        if (l()) {
            sb.append(", ");
            sb.append("metaInfo:");
            hw hwVar = this.f501a;
            if (hwVar == null) {
                sb.append("null");
            } else {
                sb.append(hwVar);
            }
        }
        if (m()) {
            sb.append(", ");
            sb.append("aliasName:");
            String str9 = this.f515i;
            if (str9 == null) {
                sb.append("null");
            } else {
                sb.append(str9);
            }
        }
        if (n()) {
            sb.append(", ");
            sb.append("isOnline:");
            sb.append(this.f505a);
        }
        if (o()) {
            sb.append(", ");
            sb.append("userAccount:");
            String str10 = this.f516j;
            if (str10 == null) {
                sb.append("null");
            } else {
                sb.append(str10);
            }
        }
        if (p()) {
            sb.append(", ");
            sb.append("miid:");
            sb.append(this.f508c);
        }
        if (q()) {
            sb.append(", ");
            sb.append("imeiMd5:");
            String str11 = this.f517k;
            if (str11 == null) {
                sb.append("null");
            } else {
                sb.append(str11);
            }
        }
        if (r()) {
            sb.append(", ");
            sb.append("deviceId:");
            String str12 = this.f518l;
            if (str12 == null) {
                sb.append("null");
            } else {
                sb.append(str12);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
