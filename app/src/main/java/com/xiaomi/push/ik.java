package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/* loaded from: classes3.dex */
public class ik implements iu<ik, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public int f671a;

    /* renamed from: a  reason: collision with other field name */
    public long f672a;

    /* renamed from: a  reason: collision with other field name */
    public hy f673a;

    /* renamed from: a  reason: collision with other field name */
    public String f674a;

    /* renamed from: a  reason: collision with other field name */
    public List<String> f676a;

    /* renamed from: b  reason: collision with other field name */
    public int f678b;

    /* renamed from: b  reason: collision with other field name */
    public long f679b;

    /* renamed from: b  reason: collision with other field name */
    public String f680b;

    /* renamed from: c  reason: collision with other field name */
    public long f681c;

    /* renamed from: c  reason: collision with other field name */
    public String f682c;

    /* renamed from: d  reason: collision with other field name */
    public String f683d;

    /* renamed from: e  reason: collision with other field name */
    public String f684e;

    /* renamed from: f  reason: collision with other field name */
    public String f685f;

    /* renamed from: g  reason: collision with other field name */
    public String f686g;

    /* renamed from: h  reason: collision with other field name */
    public String f687h;

    /* renamed from: i  reason: collision with other field name */
    public String f688i;

    /* renamed from: j  reason: collision with other field name */
    public String f689j;

    /* renamed from: k  reason: collision with other field name */
    public String f690k;

    /* renamed from: l  reason: collision with other field name */
    public String f691l;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f670a = new jk("XmPushActionRegistrationResult");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 10, 6);
    public static final jc f = new jc("", (byte) 11, 7);
    public static final jc g = new jc("", (byte) 11, 8);
    public static final jc h = new jc("", (byte) 11, 9);
    public static final jc i = new jc("", (byte) 11, 10);
    public static final jc j = new jc("", (byte) 10, 11);
    public static final jc k = new jc("", (byte) 11, 12);
    public static final jc l = new jc("", (byte) 11, 13);
    public static final jc m = new jc("", (byte) 10, 14);
    public static final jc n = new jc("", (byte) 11, 15);
    public static final jc o = new jc("", (byte) 8, 16);
    public static final jc p = new jc("", (byte) 11, 17);
    public static final jc q = new jc("", (byte) 8, 18);
    public static final jc r = new jc("", (byte) 11, 19);
    public static final jc s = new jc("", (byte) 2, 20);
    public static final jc t = new jc("", (byte) 15, 21);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f675a = new BitSet(6);

    /* renamed from: a  reason: collision with other field name */
    public boolean f677a = false;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ik ikVar) {
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
        if (!getClass().equals(ikVar.getClass())) {
            return getClass().getName().compareTo(ikVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2325a()).compareTo(Boolean.valueOf(ikVar.m2325a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2325a() && (a21 = iv.a(this.f674a, ikVar.f674a)) != 0) {
            return a21;
        }
        int compareTo2 = Boolean.valueOf(m2327b()).compareTo(Boolean.valueOf(ikVar.m2327b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2327b() && (a20 = iv.a(this.f673a, ikVar.f673a)) != 0) {
            return a20;
        }
        int compareTo3 = Boolean.valueOf(m2328c()).compareTo(Boolean.valueOf(ikVar.m2328c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2328c() && (a19 = iv.a(this.f680b, ikVar.f680b)) != 0) {
            return a19;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ikVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a18 = iv.a(this.f682c, ikVar.f682c)) != 0) {
            return a18;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ikVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a17 = iv.a(this.f672a, ikVar.f672a)) != 0) {
            return a17;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ikVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a16 = iv.a(this.f683d, ikVar.f683d)) != 0) {
            return a16;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ikVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a15 = iv.a(this.f684e, ikVar.f684e)) != 0) {
            return a15;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ikVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a14 = iv.a(this.f685f, ikVar.f685f)) != 0) {
            return a14;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ikVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a13 = iv.a(this.f686g, ikVar.f686g)) != 0) {
            return a13;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ikVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a12 = iv.a(this.f679b, ikVar.f679b)) != 0) {
            return a12;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(ikVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a11 = iv.a(this.f687h, ikVar.f687h)) != 0) {
            return a11;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(ikVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a10 = iv.a(this.f688i, ikVar.f688i)) != 0) {
            return a10;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(ikVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a9 = iv.a(this.f681c, ikVar.f681c)) != 0) {
            return a9;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(ikVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a8 = iv.a(this.f689j, ikVar.f689j)) != 0) {
            return a8;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(ikVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a7 = iv.a(this.f671a, ikVar.f671a)) != 0) {
            return a7;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(ikVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a6 = iv.a(this.f690k, ikVar.f690k)) != 0) {
            return a6;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(ikVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a5 = iv.a(this.f678b, ikVar.f678b)) != 0) {
            return a5;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(ikVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (r() && (a4 = iv.a(this.f691l, ikVar.f691l)) != 0) {
            return a4;
        }
        int compareTo19 = Boolean.valueOf(s()).compareTo(Boolean.valueOf(ikVar.s()));
        if (compareTo19 != 0) {
            return compareTo19;
        }
        if (s() && (a3 = iv.a(this.f677a, ikVar.f677a)) != 0) {
            return a3;
        }
        int compareTo20 = Boolean.valueOf(t()).compareTo(Boolean.valueOf(ikVar.t()));
        if (compareTo20 != 0) {
            return compareTo20;
        }
        if (t() && (a2 = iv.a(this.f676a, ikVar.f676a)) != 0) {
            return a2;
        }
        return 0;
    }

    public long a() {
        return this.f672a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2322a() {
        return this.f680b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public List<String> m2323a() {
        return this.f676a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2324a() {
        if (this.f680b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f682c != null) {
        } else {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
        }
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                jfVar.f();
                if (e()) {
                    m2324a();
                    return;
                }
                throw new jg("Required field 'errorCode' was not found in serialized data! Struct: " + toString());
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f674a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f673a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f680b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f682c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 10) {
                        this.f672a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f683d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f684e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f685f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f686g = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 10) {
                        this.f679b = jfVar.m2379a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 11) {
                        this.f687h = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 13:
                    if (b2 == 11) {
                        this.f688i = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 14:
                    if (b2 == 10) {
                        this.f681c = jfVar.m2379a();
                        c(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 15:
                    if (b2 == 11) {
                        this.f689j = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 16:
                    if (b2 == 8) {
                        this.f671a = jfVar.m2378a();
                        d(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 17:
                    if (b2 == 11) {
                        this.f690k = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 18:
                    if (b2 == 8) {
                        this.f678b = jfVar.m2378a();
                        e(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 19:
                    if (b2 == 11) {
                        this.f691l = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 20:
                    if (b2 == 2) {
                        this.f677a = jfVar.m2389a();
                        f(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 21:
                    if (b2 == 15) {
                        jd m2381a = jfVar.m2381a();
                        this.f676a = new ArrayList(m2381a.f794a);
                        for (int i2 = 0; i2 < m2381a.f794a; i2++) {
                            this.f676a.add(jfVar.m2385a());
                        }
                        jfVar.i();
                        continue;
                        jfVar.g();
                    }
                    break;
            }
            ji.a(jfVar, b2);
            jfVar.g();
        }
    }

    public void a(boolean z) {
        this.f675a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2325a() {
        return this.f674a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2326a(ik ikVar) {
        if (ikVar == null) {
            return false;
        }
        boolean m2325a = m2325a();
        boolean m2325a2 = ikVar.m2325a();
        if ((m2325a || m2325a2) && (!m2325a || !m2325a2 || !this.f674a.equals(ikVar.f674a))) {
            return false;
        }
        boolean m2327b = m2327b();
        boolean m2327b2 = ikVar.m2327b();
        if ((m2327b || m2327b2) && (!m2327b || !m2327b2 || !this.f673a.m2271a(ikVar.f673a))) {
            return false;
        }
        boolean m2328c = m2328c();
        boolean m2328c2 = ikVar.m2328c();
        if ((m2328c || m2328c2) && (!m2328c || !m2328c2 || !this.f680b.equals(ikVar.f680b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ikVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f682c.equals(ikVar.f682c))) || this.f672a != ikVar.f672a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ikVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f683d.equals(ikVar.f683d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ikVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f684e.equals(ikVar.f684e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ikVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f685f.equals(ikVar.f685f))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ikVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f686g.equals(ikVar.f686g))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ikVar.j();
        if ((j2 || j3) && (!j2 || !j3 || this.f679b != ikVar.f679b)) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = ikVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f687h.equals(ikVar.f687h))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = ikVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f688i.equals(ikVar.f688i))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = ikVar.m();
        if ((m2 || m3) && (!m2 || !m3 || this.f681c != ikVar.f681c)) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = ikVar.n();
        if ((n2 || n3) && (!n2 || !n3 || !this.f689j.equals(ikVar.f689j))) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = ikVar.o();
        if ((o2 || o3) && (!o2 || !o3 || this.f671a != ikVar.f671a)) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = ikVar.p();
        if ((p2 || p3) && (!p2 || !p3 || !this.f690k.equals(ikVar.f690k))) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = ikVar.q();
        if ((q2 || q3) && (!q2 || !q3 || this.f678b != ikVar.f678b)) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = ikVar.r();
        if ((r2 || r3) && (!r2 || !r3 || !this.f691l.equals(ikVar.f691l))) {
            return false;
        }
        boolean s2 = s();
        boolean s3 = ikVar.s();
        if ((s2 || s3) && (!s2 || !s3 || this.f677a != ikVar.f677a)) {
            return false;
        }
        boolean t2 = t();
        boolean t3 = ikVar.t();
        if (!t2 && !t3) {
            return true;
        }
        return t2 && t3 && this.f676a.equals(ikVar.f676a);
    }

    public String b() {
        return this.f685f;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2324a();
        jfVar.a(f670a);
        if (this.f674a != null && m2325a()) {
            jfVar.a(a);
            jfVar.a(this.f674a);
            jfVar.b();
        }
        if (this.f673a != null && m2327b()) {
            jfVar.a(b);
            this.f673a.b(jfVar);
            jfVar.b();
        }
        if (this.f680b != null) {
            jfVar.a(c);
            jfVar.a(this.f680b);
            jfVar.b();
        }
        if (this.f682c != null) {
            jfVar.a(d);
            jfVar.a(this.f682c);
            jfVar.b();
        }
        jfVar.a(e);
        jfVar.a(this.f672a);
        jfVar.b();
        if (this.f683d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f683d);
            jfVar.b();
        }
        if (this.f684e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f684e);
            jfVar.b();
        }
        if (this.f685f != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f685f);
            jfVar.b();
        }
        if (this.f686g != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f686g);
            jfVar.b();
        }
        if (j()) {
            jfVar.a(j);
            jfVar.a(this.f679b);
            jfVar.b();
        }
        if (this.f687h != null && k()) {
            jfVar.a(k);
            jfVar.a(this.f687h);
            jfVar.b();
        }
        if (this.f688i != null && l()) {
            jfVar.a(l);
            jfVar.a(this.f688i);
            jfVar.b();
        }
        if (m()) {
            jfVar.a(m);
            jfVar.a(this.f681c);
            jfVar.b();
        }
        if (this.f689j != null && n()) {
            jfVar.a(n);
            jfVar.a(this.f689j);
            jfVar.b();
        }
        if (o()) {
            jfVar.a(o);
            jfVar.mo2376a(this.f671a);
            jfVar.b();
        }
        if (this.f690k != null && p()) {
            jfVar.a(p);
            jfVar.a(this.f690k);
            jfVar.b();
        }
        if (q()) {
            jfVar.a(q);
            jfVar.mo2376a(this.f678b);
            jfVar.b();
        }
        if (this.f691l != null && r()) {
            jfVar.a(r);
            jfVar.a(this.f691l);
            jfVar.b();
        }
        if (s()) {
            jfVar.a(s);
            jfVar.a(this.f677a);
            jfVar.b();
        }
        if (this.f676a != null && t()) {
            jfVar.a(t);
            jfVar.a(new jd((byte) 11, this.f676a.size()));
            for (String str : this.f676a) {
                jfVar.a(str);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f675a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2327b() {
        return this.f673a != null;
    }

    public String c() {
        return this.f686g;
    }

    public void c(boolean z) {
        this.f675a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2328c() {
        return this.f680b != null;
    }

    public void d(boolean z) {
        this.f675a.set(3, z);
    }

    public boolean d() {
        return this.f682c != null;
    }

    public void e(boolean z) {
        this.f675a.set(4, z);
    }

    public boolean e() {
        return this.f675a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ik)) {
            return m2326a((ik) obj);
        }
        return false;
    }

    public void f(boolean z) {
        this.f675a.set(5, z);
    }

    public boolean f() {
        return this.f683d != null;
    }

    public boolean g() {
        return this.f684e != null;
    }

    public boolean h() {
        return this.f685f != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f686g != null;
    }

    public boolean j() {
        return this.f675a.get(1);
    }

    public boolean k() {
        return this.f687h != null;
    }

    public boolean l() {
        return this.f688i != null;
    }

    public boolean m() {
        return this.f675a.get(2);
    }

    public boolean n() {
        return this.f689j != null;
    }

    public boolean o() {
        return this.f675a.get(3);
    }

    public boolean p() {
        return this.f690k != null;
    }

    public boolean q() {
        return this.f675a.get(4);
    }

    public boolean r() {
        return this.f691l != null;
    }

    public boolean s() {
        return this.f675a.get(5);
    }

    public boolean t() {
        return this.f676a != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionRegistrationResult(");
        boolean z2 = false;
        if (m2325a()) {
            sb.append("debug:");
            String str = this.f674a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (m2327b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f673a;
            if (hyVar == null) {
                sb.append("null");
            } else {
                sb.append(hyVar);
            }
        } else {
            z2 = z;
        }
        if (!z2) {
            sb.append(", ");
        }
        sb.append("id:");
        String str2 = this.f680b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(com.xiaomi.push.service.bd.a(str2));
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f682c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        sb.append(", ");
        sb.append("errorCode:");
        sb.append(this.f672a);
        if (f()) {
            sb.append(", ");
            sb.append("reason:");
            String str4 = this.f683d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("regId:");
            String str5 = this.f684e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("packageName:");
            String str6 = this.f686g;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("registeredAt:");
            sb.append(this.f679b);
        }
        if (k()) {
            sb.append(", ");
            sb.append("aliasName:");
            String str7 = this.f687h;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        if (l()) {
            sb.append(", ");
            sb.append("clientId:");
            String str8 = this.f688i;
            if (str8 == null) {
                sb.append("null");
            } else {
                sb.append(str8);
            }
        }
        if (m()) {
            sb.append(", ");
            sb.append("costTime:");
            sb.append(this.f681c);
        }
        if (n()) {
            sb.append(", ");
            sb.append("appVersion:");
            String str9 = this.f689j;
            if (str9 == null) {
                sb.append("null");
            } else {
                sb.append(str9);
            }
        }
        if (o()) {
            sb.append(", ");
            sb.append("pushSdkVersionCode:");
            sb.append(this.f671a);
        }
        if (p()) {
            sb.append(", ");
            sb.append("hybridPushEndpoint:");
            String str10 = this.f690k;
            if (str10 == null) {
                sb.append("null");
            } else {
                sb.append(str10);
            }
        }
        if (q()) {
            sb.append(", ");
            sb.append("appVersionCode:");
            sb.append(this.f678b);
        }
        if (r()) {
            sb.append(", ");
            sb.append("region:");
            String str11 = this.f691l;
            if (str11 == null) {
                sb.append("null");
            } else {
                sb.append(str11);
            }
        }
        if (s()) {
            sb.append(", ");
            sb.append("isHybridFrame:");
            sb.append(this.f677a);
        }
        if (t()) {
            sb.append(", ");
            sb.append("autoMarkPkgs:");
            List<String> list = this.f676a;
            if (list == null) {
                sb.append("null");
            } else {
                sb.append(list);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
