package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class ij implements iu<ij, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public int f640a;

    /* renamed from: a  reason: collision with other field name */
    public long f641a;

    /* renamed from: a  reason: collision with other field name */
    public hx f642a;

    /* renamed from: a  reason: collision with other field name */
    public hy f643a;

    /* renamed from: a  reason: collision with other field name */
    public String f644a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f646a;

    /* renamed from: b  reason: collision with other field name */
    public int f648b;

    /* renamed from: b  reason: collision with other field name */
    public long f649b;

    /* renamed from: b  reason: collision with other field name */
    public String f650b;

    /* renamed from: c  reason: collision with other field name */
    public int f652c;

    /* renamed from: c  reason: collision with other field name */
    public String f653c;

    /* renamed from: d  reason: collision with other field name */
    public String f655d;

    /* renamed from: e  reason: collision with other field name */
    public String f656e;

    /* renamed from: f  reason: collision with other field name */
    public String f657f;

    /* renamed from: g  reason: collision with other field name */
    public String f658g;

    /* renamed from: h  reason: collision with other field name */
    public String f659h;

    /* renamed from: i  reason: collision with other field name */
    public String f660i;

    /* renamed from: j  reason: collision with other field name */
    public String f661j;

    /* renamed from: k  reason: collision with other field name */
    public String f662k;

    /* renamed from: l  reason: collision with other field name */
    public String f663l;

    /* renamed from: m  reason: collision with other field name */
    public String f664m;

    /* renamed from: n  reason: collision with other field name */
    public String f665n;

    /* renamed from: o  reason: collision with other field name */
    public String f666o;

    /* renamed from: p  reason: collision with other field name */
    public String f667p;

    /* renamed from: q  reason: collision with other field name */
    public String f668q;

    /* renamed from: r  reason: collision with other field name */
    public String f669r;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f639a = new jk("XmPushActionRegistration");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 11, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 11, 8);
    public static final jc i = new jc("", (byte) 11, 9);
    public static final jc j = new jc("", (byte) 11, 10);
    public static final jc k = new jc("", (byte) 11, 11);
    public static final jc l = new jc("", (byte) 11, 12);
    public static final jc m = new jc("", (byte) 8, 13);
    public static final jc n = new jc("", (byte) 8, 14);
    public static final jc o = new jc("", (byte) 11, 15);
    public static final jc p = new jc("", (byte) 11, 16);
    public static final jc q = new jc("", (byte) 11, 17);
    public static final jc r = new jc("", (byte) 11, 18);
    public static final jc s = new jc("", (byte) 8, 19);
    public static final jc t = new jc("", (byte) 8, 20);
    public static final jc u = new jc("", (byte) 2, 21);
    public static final jc v = new jc("", (byte) 10, 22);
    public static final jc w = new jc("", (byte) 10, 23);
    public static final jc x = new jc("", (byte) 11, 24);
    public static final jc y = new jc("", (byte) 11, 25);
    public static final jc z = new jc("", (byte) 2, 26);
    public static final jc A = new jc("", (byte) 13, 100);
    public static final jc B = new jc("", (byte) 2, 101);
    public static final jc C = new jc("", (byte) 11, 102);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f645a = new BitSet(8);

    /* renamed from: a  reason: collision with other field name */
    public boolean f647a = true;

    /* renamed from: c  reason: collision with other field name */
    public boolean f654c = false;

    /* renamed from: b  reason: collision with other field name */
    public boolean f651b = false;

    public boolean A() {
        return this.f646a != null;
    }

    public boolean B() {
        return this.f645a.get(7);
    }

    public boolean C() {
        return this.f669r != null;
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ij ijVar) {
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
        int a20;
        int a21;
        int a22;
        int a23;
        int a24;
        int a25;
        int a26;
        int a27;
        int a28;
        int a29;
        int a30;
        if (!getClass().equals(ijVar.getClass())) {
            return getClass().getName().compareTo(ijVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2318a()).compareTo(Boolean.valueOf(ijVar.m2318a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2318a() && (a30 = iv.a(this.f644a, ijVar.f644a)) != 0) {
            return a30;
        }
        int compareTo2 = Boolean.valueOf(m2320b()).compareTo(Boolean.valueOf(ijVar.m2320b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2320b() && (a29 = iv.a(this.f643a, ijVar.f643a)) != 0) {
            return a29;
        }
        int compareTo3 = Boolean.valueOf(m2321c()).compareTo(Boolean.valueOf(ijVar.m2321c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2321c() && (a28 = iv.a(this.f650b, ijVar.f650b)) != 0) {
            return a28;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ijVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a27 = iv.a(this.f653c, ijVar.f653c)) != 0) {
            return a27;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ijVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a26 = iv.a(this.f655d, ijVar.f655d)) != 0) {
            return a26;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ijVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a25 = iv.a(this.f656e, ijVar.f656e)) != 0) {
            return a25;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ijVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a24 = iv.a(this.f657f, ijVar.f657f)) != 0) {
            return a24;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ijVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a23 = iv.a(this.f658g, ijVar.f658g)) != 0) {
            return a23;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ijVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a22 = iv.a(this.f659h, ijVar.f659h)) != 0) {
            return a22;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ijVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a21 = iv.a(this.f660i, ijVar.f660i)) != 0) {
            return a21;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(ijVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a20 = iv.a(this.f661j, ijVar.f661j)) != 0) {
            return a20;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(ijVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a19 = iv.a(this.f662k, ijVar.f662k)) != 0) {
            return a19;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(ijVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a18 = iv.a(this.f640a, ijVar.f640a)) != 0) {
            return a18;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(ijVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a17 = iv.a(this.f648b, ijVar.f648b)) != 0) {
            return a17;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(ijVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a16 = iv.a(this.f663l, ijVar.f663l)) != 0) {
            return a16;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(ijVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a15 = iv.a(this.f664m, ijVar.f664m)) != 0) {
            return a15;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(ijVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a14 = iv.a(this.f665n, ijVar.f665n)) != 0) {
            return a14;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(ijVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (r() && (a13 = iv.a(this.f666o, ijVar.f666o)) != 0) {
            return a13;
        }
        int compareTo19 = Boolean.valueOf(s()).compareTo(Boolean.valueOf(ijVar.s()));
        if (compareTo19 != 0) {
            return compareTo19;
        }
        if (s() && (a12 = iv.a(this.f652c, ijVar.f652c)) != 0) {
            return a12;
        }
        int compareTo20 = Boolean.valueOf(t()).compareTo(Boolean.valueOf(ijVar.t()));
        if (compareTo20 != 0) {
            return compareTo20;
        }
        if (t() && (a11 = iv.a(this.f642a, ijVar.f642a)) != 0) {
            return a11;
        }
        int compareTo21 = Boolean.valueOf(u()).compareTo(Boolean.valueOf(ijVar.u()));
        if (compareTo21 != 0) {
            return compareTo21;
        }
        if (u() && (a10 = iv.a(this.f647a, ijVar.f647a)) != 0) {
            return a10;
        }
        int compareTo22 = Boolean.valueOf(v()).compareTo(Boolean.valueOf(ijVar.v()));
        if (compareTo22 != 0) {
            return compareTo22;
        }
        if (v() && (a9 = iv.a(this.f641a, ijVar.f641a)) != 0) {
            return a9;
        }
        int compareTo23 = Boolean.valueOf(w()).compareTo(Boolean.valueOf(ijVar.w()));
        if (compareTo23 != 0) {
            return compareTo23;
        }
        if (w() && (a8 = iv.a(this.f649b, ijVar.f649b)) != 0) {
            return a8;
        }
        int compareTo24 = Boolean.valueOf(x()).compareTo(Boolean.valueOf(ijVar.x()));
        if (compareTo24 != 0) {
            return compareTo24;
        }
        if (x() && (a7 = iv.a(this.f667p, ijVar.f667p)) != 0) {
            return a7;
        }
        int compareTo25 = Boolean.valueOf(y()).compareTo(Boolean.valueOf(ijVar.y()));
        if (compareTo25 != 0) {
            return compareTo25;
        }
        if (y() && (a6 = iv.a(this.f668q, ijVar.f668q)) != 0) {
            return a6;
        }
        int compareTo26 = Boolean.valueOf(z()).compareTo(Boolean.valueOf(ijVar.z()));
        if (compareTo26 != 0) {
            return compareTo26;
        }
        if (z() && (a5 = iv.a(this.f651b, ijVar.f651b)) != 0) {
            return a5;
        }
        int compareTo27 = Boolean.valueOf(A()).compareTo(Boolean.valueOf(ijVar.A()));
        if (compareTo27 != 0) {
            return compareTo27;
        }
        if (A() && (a4 = iv.a(this.f646a, ijVar.f646a)) != 0) {
            return a4;
        }
        int compareTo28 = Boolean.valueOf(B()).compareTo(Boolean.valueOf(ijVar.B()));
        if (compareTo28 != 0) {
            return compareTo28;
        }
        if (B() && (a3 = iv.a(this.f654c, ijVar.f654c)) != 0) {
            return a3;
        }
        int compareTo29 = Boolean.valueOf(C()).compareTo(Boolean.valueOf(ijVar.C()));
        if (compareTo29 != 0) {
            return compareTo29;
        }
        if (C() && (a2 = iv.a(this.f669r, ijVar.f669r)) != 0) {
            return a2;
        }
        return 0;
    }

    public ij a(int i2) {
        this.f640a = i2;
        a(true);
        return this;
    }

    public ij a(hx hxVar) {
        this.f642a = hxVar;
        return this;
    }

    public ij a(String str) {
        this.f650b = str;
        return this;
    }

    public String a() {
        return this.f650b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2317a() {
        if (this.f650b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f653c == null) {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f657f != null) {
        } else {
            throw new jg("Required field 'token' was not present! Struct: " + toString());
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
                m2317a();
                return;
            }
            short s2 = m2380a.f793a;
            switch (s2) {
                case 1:
                    if (b2 == 11) {
                        this.f644a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f643a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f650b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f653c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f655d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f656e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f657f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f658g = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f659h = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f660i = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 11) {
                        this.f661j = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 11) {
                        this.f662k = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 13:
                    if (b2 == 8) {
                        this.f640a = jfVar.m2378a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 14:
                    if (b2 == 8) {
                        this.f648b = jfVar.m2378a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 15:
                    if (b2 == 11) {
                        this.f663l = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 16:
                    if (b2 == 11) {
                        this.f664m = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 17:
                    if (b2 == 11) {
                        this.f665n = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 18:
                    if (b2 == 11) {
                        this.f666o = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 19:
                    if (b2 == 8) {
                        this.f652c = jfVar.m2378a();
                        c(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 20:
                    if (b2 == 8) {
                        this.f642a = hx.a(jfVar.m2378a());
                        continue;
                        jfVar.g();
                    }
                    break;
                case 21:
                    if (b2 == 2) {
                        this.f647a = jfVar.m2389a();
                        d(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 22:
                    if (b2 == 10) {
                        this.f641a = jfVar.m2379a();
                        e(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 23:
                    if (b2 == 10) {
                        this.f649b = jfVar.m2379a();
                        f(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 24:
                    if (b2 == 11) {
                        this.f667p = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 25:
                    if (b2 == 11) {
                        this.f668q = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 26:
                    if (b2 == 2) {
                        this.f651b = jfVar.m2389a();
                        g(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                default:
                    switch (s2) {
                        case 100:
                            if (b2 == 13) {
                                je m2382a = jfVar.m2382a();
                                this.f646a = new HashMap(m2382a.f795a * 2);
                                for (int i2 = 0; i2 < m2382a.f795a; i2++) {
                                    this.f646a.put(jfVar.m2385a(), jfVar.m2385a());
                                }
                                jfVar.h();
                                break;
                            }
                            break;
                        case 101:
                            if (b2 == 2) {
                                this.f654c = jfVar.m2389a();
                                h(true);
                                break;
                            }
                            break;
                        case 102:
                            if (b2 == 11) {
                                this.f669r = jfVar.m2385a();
                                continue;
                            }
                            break;
                    }
                    jfVar.g();
                    break;
            }
            ji.a(jfVar, b2);
            jfVar.g();
        }
    }

    public void a(boolean z2) {
        this.f645a.set(0, z2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2318a() {
        return this.f644a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2319a(ij ijVar) {
        if (ijVar == null) {
            return false;
        }
        boolean m2318a = m2318a();
        boolean m2318a2 = ijVar.m2318a();
        if ((m2318a || m2318a2) && (!m2318a || !m2318a2 || !this.f644a.equals(ijVar.f644a))) {
            return false;
        }
        boolean m2320b = m2320b();
        boolean m2320b2 = ijVar.m2320b();
        if ((m2320b || m2320b2) && (!m2320b || !m2320b2 || !this.f643a.m2271a(ijVar.f643a))) {
            return false;
        }
        boolean m2321c = m2321c();
        boolean m2321c2 = ijVar.m2321c();
        if ((m2321c || m2321c2) && (!m2321c || !m2321c2 || !this.f650b.equals(ijVar.f650b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ijVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f653c.equals(ijVar.f653c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ijVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f655d.equals(ijVar.f655d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ijVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f656e.equals(ijVar.f656e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ijVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f657f.equals(ijVar.f657f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ijVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f658g.equals(ijVar.f658g))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ijVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f659h.equals(ijVar.f659h))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ijVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f660i.equals(ijVar.f660i))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = ijVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f661j.equals(ijVar.f661j))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = ijVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f662k.equals(ijVar.f662k))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = ijVar.m();
        if ((m2 || m3) && (!m2 || !m3 || this.f640a != ijVar.f640a)) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = ijVar.n();
        if ((n2 || n3) && (!n2 || !n3 || this.f648b != ijVar.f648b)) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = ijVar.o();
        if ((o2 || o3) && (!o2 || !o3 || !this.f663l.equals(ijVar.f663l))) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = ijVar.p();
        if ((p2 || p3) && (!p2 || !p3 || !this.f664m.equals(ijVar.f664m))) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = ijVar.q();
        if ((q2 || q3) && (!q2 || !q3 || !this.f665n.equals(ijVar.f665n))) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = ijVar.r();
        if ((r2 || r3) && (!r2 || !r3 || !this.f666o.equals(ijVar.f666o))) {
            return false;
        }
        boolean s2 = s();
        boolean s3 = ijVar.s();
        if ((s2 || s3) && (!s2 || !s3 || this.f652c != ijVar.f652c)) {
            return false;
        }
        boolean t2 = t();
        boolean t3 = ijVar.t();
        if ((t2 || t3) && (!t2 || !t3 || !this.f642a.equals(ijVar.f642a))) {
            return false;
        }
        boolean u2 = u();
        boolean u3 = ijVar.u();
        if ((u2 || u3) && (!u2 || !u3 || this.f647a != ijVar.f647a)) {
            return false;
        }
        boolean v2 = v();
        boolean v3 = ijVar.v();
        if ((v2 || v3) && (!v2 || !v3 || this.f641a != ijVar.f641a)) {
            return false;
        }
        boolean w2 = w();
        boolean w3 = ijVar.w();
        if ((w2 || w3) && (!w2 || !w3 || this.f649b != ijVar.f649b)) {
            return false;
        }
        boolean x2 = x();
        boolean x3 = ijVar.x();
        if ((x2 || x3) && (!x2 || !x3 || !this.f667p.equals(ijVar.f667p))) {
            return false;
        }
        boolean y2 = y();
        boolean y3 = ijVar.y();
        if ((y2 || y3) && (!y2 || !y3 || !this.f668q.equals(ijVar.f668q))) {
            return false;
        }
        boolean z2 = z();
        boolean z3 = ijVar.z();
        if ((z2 || z3) && (!z2 || !z3 || this.f651b != ijVar.f651b)) {
            return false;
        }
        boolean A2 = A();
        boolean A3 = ijVar.A();
        if ((A2 || A3) && (!A2 || !A3 || !this.f646a.equals(ijVar.f646a))) {
            return false;
        }
        boolean B2 = B();
        boolean B3 = ijVar.B();
        if ((B2 || B3) && (!B2 || !B3 || this.f654c != ijVar.f654c)) {
            return false;
        }
        boolean C2 = C();
        boolean C3 = ijVar.C();
        if (!C2 && !C3) {
            return true;
        }
        return C2 && C3 && this.f669r.equals(ijVar.f669r);
    }

    public ij b(int i2) {
        this.f648b = i2;
        b(true);
        return this;
    }

    public ij b(String str) {
        this.f653c = str;
        return this;
    }

    public String b() {
        return this.f653c;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2317a();
        jfVar.a(f639a);
        if (this.f644a != null && m2318a()) {
            jfVar.a(a);
            jfVar.a(this.f644a);
            jfVar.b();
        }
        if (this.f643a != null && m2320b()) {
            jfVar.a(b);
            this.f643a.b(jfVar);
            jfVar.b();
        }
        if (this.f650b != null) {
            jfVar.a(c);
            jfVar.a(this.f650b);
            jfVar.b();
        }
        if (this.f653c != null) {
            jfVar.a(d);
            jfVar.a(this.f653c);
            jfVar.b();
        }
        if (this.f655d != null && e()) {
            jfVar.a(e);
            jfVar.a(this.f655d);
            jfVar.b();
        }
        if (this.f656e != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f656e);
            jfVar.b();
        }
        if (this.f657f != null) {
            jfVar.a(g);
            jfVar.a(this.f657f);
            jfVar.b();
        }
        if (this.f658g != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f658g);
            jfVar.b();
        }
        if (this.f659h != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f659h);
            jfVar.b();
        }
        if (this.f660i != null && j()) {
            jfVar.a(j);
            jfVar.a(this.f660i);
            jfVar.b();
        }
        if (this.f661j != null && k()) {
            jfVar.a(k);
            jfVar.a(this.f661j);
            jfVar.b();
        }
        if (this.f662k != null && l()) {
            jfVar.a(l);
            jfVar.a(this.f662k);
            jfVar.b();
        }
        if (m()) {
            jfVar.a(m);
            jfVar.mo2376a(this.f640a);
            jfVar.b();
        }
        if (n()) {
            jfVar.a(n);
            jfVar.mo2376a(this.f648b);
            jfVar.b();
        }
        if (this.f663l != null && o()) {
            jfVar.a(o);
            jfVar.a(this.f663l);
            jfVar.b();
        }
        if (this.f664m != null && p()) {
            jfVar.a(p);
            jfVar.a(this.f664m);
            jfVar.b();
        }
        if (this.f665n != null && q()) {
            jfVar.a(q);
            jfVar.a(this.f665n);
            jfVar.b();
        }
        if (this.f666o != null && r()) {
            jfVar.a(r);
            jfVar.a(this.f666o);
            jfVar.b();
        }
        if (s()) {
            jfVar.a(s);
            jfVar.mo2376a(this.f652c);
            jfVar.b();
        }
        if (this.f642a != null && t()) {
            jfVar.a(t);
            jfVar.mo2376a(this.f642a.a());
            jfVar.b();
        }
        if (u()) {
            jfVar.a(u);
            jfVar.a(this.f647a);
            jfVar.b();
        }
        if (v()) {
            jfVar.a(v);
            jfVar.a(this.f641a);
            jfVar.b();
        }
        if (w()) {
            jfVar.a(w);
            jfVar.a(this.f649b);
            jfVar.b();
        }
        if (this.f667p != null && x()) {
            jfVar.a(x);
            jfVar.a(this.f667p);
            jfVar.b();
        }
        if (this.f668q != null && y()) {
            jfVar.a(y);
            jfVar.a(this.f668q);
            jfVar.b();
        }
        if (z()) {
            jfVar.a(z);
            jfVar.a(this.f651b);
            jfVar.b();
        }
        if (this.f646a != null && A()) {
            jfVar.a(A);
            jfVar.a(new je((byte) 11, (byte) 11, this.f646a.size()));
            for (Map.Entry<String, String> entry : this.f646a.entrySet()) {
                jfVar.a(entry.getKey());
                jfVar.a(entry.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        if (B()) {
            jfVar.a(B);
            jfVar.a(this.f654c);
            jfVar.b();
        }
        if (this.f669r != null && C()) {
            jfVar.a(C);
            jfVar.a(this.f669r);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z2) {
        this.f645a.set(1, z2);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2320b() {
        return this.f643a != null;
    }

    public ij c(int i2) {
        this.f652c = i2;
        c(true);
        return this;
    }

    public ij c(String str) {
        this.f655d = str;
        return this;
    }

    public String c() {
        return this.f657f;
    }

    public void c(boolean z2) {
        this.f645a.set(2, z2);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2321c() {
        return this.f650b != null;
    }

    public ij d(String str) {
        this.f656e = str;
        return this;
    }

    public void d(boolean z2) {
        this.f645a.set(3, z2);
    }

    public boolean d() {
        return this.f653c != null;
    }

    public ij e(String str) {
        this.f657f = str;
        return this;
    }

    public void e(boolean z2) {
        this.f645a.set(4, z2);
    }

    public boolean e() {
        return this.f655d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ij)) {
            return m2319a((ij) obj);
        }
        return false;
    }

    public ij f(String str) {
        this.f658g = str;
        return this;
    }

    public void f(boolean z2) {
        this.f645a.set(5, z2);
    }

    public boolean f() {
        return this.f656e != null;
    }

    public ij g(String str) {
        this.f659h = str;
        return this;
    }

    public void g(boolean z2) {
        this.f645a.set(6, z2);
    }

    public boolean g() {
        return this.f657f != null;
    }

    public ij h(String str) {
        this.f662k = str;
        return this;
    }

    public void h(boolean z2) {
        this.f645a.set(7, z2);
    }

    public boolean h() {
        return this.f658g != null;
    }

    public int hashCode() {
        return 0;
    }

    public ij i(String str) {
        this.f666o = str;
        return this;
    }

    public boolean i() {
        return this.f659h != null;
    }

    public boolean j() {
        return this.f660i != null;
    }

    public boolean k() {
        return this.f661j != null;
    }

    public boolean l() {
        return this.f662k != null;
    }

    public boolean m() {
        return this.f645a.get(0);
    }

    public boolean n() {
        return this.f645a.get(1);
    }

    public boolean o() {
        return this.f663l != null;
    }

    public boolean p() {
        return this.f664m != null;
    }

    public boolean q() {
        return this.f665n != null;
    }

    public boolean r() {
        return this.f666o != null;
    }

    public boolean s() {
        return this.f645a.get(2);
    }

    public boolean t() {
        return this.f642a != null;
    }

    public String toString() {
        boolean z2;
        StringBuilder sb = new StringBuilder("XmPushActionRegistration(");
        boolean z3 = false;
        if (m2318a()) {
            sb.append("debug:");
            String str = this.f644a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z2 = false;
        } else {
            z2 = true;
        }
        if (m2320b()) {
            if (!z2) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f643a;
            if (hyVar == null) {
                sb.append("null");
            } else {
                sb.append(hyVar);
            }
        } else {
            z3 = z2;
        }
        if (!z3) {
            sb.append(", ");
        }
        sb.append("id:");
        String str2 = this.f650b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(com.xiaomi.push.service.bd.a(str2));
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f653c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        if (e()) {
            sb.append(", ");
            sb.append("appVersion:");
            String str4 = this.f655d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (f()) {
            sb.append(", ");
            sb.append("packageName:");
            String str5 = this.f656e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        sb.append(", ");
        sb.append("token:");
        String str6 = this.f657f;
        if (str6 == null) {
            sb.append("null");
        } else {
            sb.append(str6);
        }
        if (h()) {
            sb.append(", ");
            sb.append("deviceId:");
            String str7 = this.f658g;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("aliasName:");
            String str8 = this.f659h;
            if (str8 == null) {
                sb.append("null");
            } else {
                sb.append(str8);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("sdkVersion:");
            String str9 = this.f660i;
            if (str9 == null) {
                sb.append("null");
            } else {
                sb.append(str9);
            }
        }
        if (k()) {
            sb.append(", ");
            sb.append("regId:");
            String str10 = this.f661j;
            if (str10 == null) {
                sb.append("null");
            } else {
                sb.append(str10);
            }
        }
        if (l()) {
            sb.append(", ");
            sb.append("pushSdkVersionName:");
            String str11 = this.f662k;
            if (str11 == null) {
                sb.append("null");
            } else {
                sb.append(str11);
            }
        }
        if (m()) {
            sb.append(", ");
            sb.append("pushSdkVersionCode:");
            sb.append(this.f640a);
        }
        if (n()) {
            sb.append(", ");
            sb.append("appVersionCode:");
            sb.append(this.f648b);
        }
        if (o()) {
            sb.append(", ");
            sb.append("androidId:");
            String str12 = this.f663l;
            if (str12 == null) {
                sb.append("null");
            } else {
                sb.append(str12);
            }
        }
        if (p()) {
            sb.append(", ");
            sb.append("imei:");
            String str13 = this.f664m;
            if (str13 == null) {
                sb.append("null");
            } else {
                sb.append(str13);
            }
        }
        if (q()) {
            sb.append(", ");
            sb.append("serial:");
            String str14 = this.f665n;
            if (str14 == null) {
                sb.append("null");
            } else {
                sb.append(str14);
            }
        }
        if (r()) {
            sb.append(", ");
            sb.append("imeiMd5:");
            String str15 = this.f666o;
            if (str15 == null) {
                sb.append("null");
            } else {
                sb.append(str15);
            }
        }
        if (s()) {
            sb.append(", ");
            sb.append("spaceId:");
            sb.append(this.f652c);
        }
        if (t()) {
            sb.append(", ");
            sb.append("reason:");
            hx hxVar = this.f642a;
            if (hxVar == null) {
                sb.append("null");
            } else {
                sb.append(hxVar);
            }
        }
        if (u()) {
            sb.append(", ");
            sb.append("validateToken:");
            sb.append(this.f647a);
        }
        if (v()) {
            sb.append(", ");
            sb.append("miid:");
            sb.append(this.f641a);
        }
        if (w()) {
            sb.append(", ");
            sb.append("createdTs:");
            sb.append(this.f649b);
        }
        if (x()) {
            sb.append(", ");
            sb.append("subImei:");
            String str16 = this.f667p;
            if (str16 == null) {
                sb.append("null");
            } else {
                sb.append(str16);
            }
        }
        if (y()) {
            sb.append(", ");
            sb.append("subImeiMd5:");
            String str17 = this.f668q;
            if (str17 == null) {
                sb.append("null");
            } else {
                sb.append(str17);
            }
        }
        if (z()) {
            sb.append(", ");
            sb.append("isHybridFrame:");
            sb.append(this.f651b);
        }
        if (A()) {
            sb.append(", ");
            sb.append("connectionAttrs:");
            Map<String, String> map = this.f646a;
            if (map == null) {
                sb.append("null");
            } else {
                sb.append(map);
            }
        }
        if (B()) {
            sb.append(", ");
            sb.append("cleanOldRegInfo:");
            sb.append(this.f654c);
        }
        if (C()) {
            sb.append(", ");
            sb.append("oldRegId:");
            String str18 = this.f669r;
            if (str18 == null) {
                sb.append("null");
            } else {
                sb.append(str18);
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean u() {
        return this.f645a.get(3);
    }

    public boolean v() {
        return this.f645a.get(4);
    }

    public boolean w() {
        return this.f645a.get(5);
    }

    public boolean x() {
        return this.f667p != null;
    }

    public boolean y() {
        return this.f668q != null;
    }

    public boolean z() {
        return this.f645a.get(6);
    }
}
