package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class hz implements iu<hz, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public int f545a;

    /* renamed from: a  reason: collision with other field name */
    public long f546a;

    /* renamed from: a  reason: collision with other field name */
    public hy f547a;

    /* renamed from: a  reason: collision with other field name */
    public im f548a;

    /* renamed from: a  reason: collision with other field name */
    public String f549a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f551a;

    /* renamed from: a  reason: collision with other field name */
    public short f552a;

    /* renamed from: b  reason: collision with other field name */
    public String f554b;

    /* renamed from: b  reason: collision with other field name */
    public short f555b;

    /* renamed from: c  reason: collision with other field name */
    public String f556c;

    /* renamed from: d  reason: collision with other field name */
    public String f557d;

    /* renamed from: e  reason: collision with other field name */
    public String f558e;

    /* renamed from: f  reason: collision with other field name */
    public String f559f;

    /* renamed from: g  reason: collision with other field name */
    public String f560g;

    /* renamed from: h  reason: collision with other field name */
    public String f561h;

    /* renamed from: i  reason: collision with other field name */
    public String f562i;

    /* renamed from: j  reason: collision with other field name */
    public String f563j;

    /* renamed from: k  reason: collision with other field name */
    public String f564k;

    /* renamed from: l  reason: collision with other field name */
    public String f565l;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f544a = new jk("XmPushActionAckMessage");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 10, 5);
    public static final jc f = new jc("", (byte) 11, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 12, 8);
    public static final jc i = new jc("", (byte) 11, 9);
    public static final jc j = new jc("", (byte) 11, 10);
    public static final jc k = new jc("", (byte) 2, 11);
    public static final jc l = new jc("", (byte) 11, 12);
    public static final jc m = new jc("", (byte) 11, 13);
    public static final jc n = new jc("", (byte) 11, 14);
    public static final jc o = new jc("", (byte) 6, 15);
    public static final jc p = new jc("", (byte) 6, 16);
    public static final jc q = new jc("", (byte) 11, 20);
    public static final jc r = new jc("", (byte) 11, 21);
    public static final jc s = new jc("", (byte) 8, 22);
    public static final jc t = new jc("", (byte) 13, 23);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f550a = new BitSet(5);

    /* renamed from: a  reason: collision with other field name */
    public boolean f553a = false;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hz hzVar) {
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
        if (!getClass().equals(hzVar.getClass())) {
            return getClass().getName().compareTo(hzVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2272a()).compareTo(Boolean.valueOf(hzVar.m2272a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2272a() && (a21 = iv.a(this.f549a, hzVar.f549a)) != 0) {
            return a21;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hzVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a20 = iv.a(this.f547a, hzVar.f547a)) != 0) {
            return a20;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hzVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a19 = iv.a(this.f554b, hzVar.f554b)) != 0) {
            return a19;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hzVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a18 = iv.a(this.f556c, hzVar.f556c)) != 0) {
            return a18;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hzVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a17 = iv.a(this.f546a, hzVar.f546a)) != 0) {
            return a17;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hzVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a16 = iv.a(this.f557d, hzVar.f557d)) != 0) {
            return a16;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hzVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a15 = iv.a(this.f558e, hzVar.f558e)) != 0) {
            return a15;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hzVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a14 = iv.a(this.f548a, hzVar.f548a)) != 0) {
            return a14;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hzVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a13 = iv.a(this.f559f, hzVar.f559f)) != 0) {
            return a13;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hzVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a12 = iv.a(this.f560g, hzVar.f560g)) != 0) {
            return a12;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hzVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a11 = iv.a(this.f553a, hzVar.f553a)) != 0) {
            return a11;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(hzVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a10 = iv.a(this.f561h, hzVar.f561h)) != 0) {
            return a10;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(hzVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a9 = iv.a(this.f562i, hzVar.f562i)) != 0) {
            return a9;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(hzVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a8 = iv.a(this.f563j, hzVar.f563j)) != 0) {
            return a8;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(hzVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a7 = iv.a(this.f552a, hzVar.f552a)) != 0) {
            return a7;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(hzVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a6 = iv.a(this.f555b, hzVar.f555b)) != 0) {
            return a6;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(hzVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a5 = iv.a(this.f564k, hzVar.f564k)) != 0) {
            return a5;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(hzVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (r() && (a4 = iv.a(this.f565l, hzVar.f565l)) != 0) {
            return a4;
        }
        int compareTo19 = Boolean.valueOf(s()).compareTo(Boolean.valueOf(hzVar.s()));
        if (compareTo19 != 0) {
            return compareTo19;
        }
        if (s() && (a3 = iv.a(this.f545a, hzVar.f545a)) != 0) {
            return a3;
        }
        int compareTo20 = Boolean.valueOf(t()).compareTo(Boolean.valueOf(hzVar.t()));
        if (compareTo20 != 0) {
            return compareTo20;
        }
        if (t() && (a2 = iv.a(this.f551a, hzVar.f551a)) != 0) {
            return a2;
        }
        return 0;
    }

    public hz a(long j2) {
        this.f546a = j2;
        a(true);
        return this;
    }

    public hz a(String str) {
        this.f554b = str;
        return this;
    }

    public hz a(short s2) {
        this.f552a = s2;
        c(true);
        return this;
    }

    public void a() {
        if (this.f554b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f556c != null) {
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
                    a();
                    return;
                }
                throw new jg("Required field 'messageTs' was not found in serialized data! Struct: " + toString());
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f549a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f547a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f554b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f556c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 10) {
                        this.f546a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f557d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f558e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 12) {
                        im imVar = new im();
                        this.f548a = imVar;
                        imVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f559f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f560g = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 2) {
                        this.f553a = jfVar.m2389a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 11) {
                        this.f561h = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 13:
                    if (b2 == 11) {
                        this.f562i = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 14:
                    if (b2 == 11) {
                        this.f563j = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 15:
                    if (b2 == 6) {
                        this.f552a = jfVar.m2387a();
                        c(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 16:
                    if (b2 == 6) {
                        this.f555b = jfVar.m2387a();
                        d(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 20:
                    if (b2 == 11) {
                        this.f564k = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 21:
                    if (b2 == 11) {
                        this.f565l = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 22:
                    if (b2 == 8) {
                        this.f545a = jfVar.m2378a();
                        e(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 23:
                    if (b2 == 13) {
                        je m2382a = jfVar.m2382a();
                        this.f551a = new HashMap(m2382a.f795a * 2);
                        for (int i2 = 0; i2 < m2382a.f795a; i2++) {
                            this.f551a.put(jfVar.m2385a(), jfVar.m2385a());
                        }
                        jfVar.h();
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
        this.f550a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2272a() {
        return this.f549a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2273a(hz hzVar) {
        if (hzVar == null) {
            return false;
        }
        boolean m2272a = m2272a();
        boolean m2272a2 = hzVar.m2272a();
        if ((m2272a || m2272a2) && (!m2272a || !m2272a2 || !this.f549a.equals(hzVar.f549a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hzVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f547a.m2271a(hzVar.f547a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hzVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f554b.equals(hzVar.f554b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hzVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f556c.equals(hzVar.f556c))) || this.f546a != hzVar.f546a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hzVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f557d.equals(hzVar.f557d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hzVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f558e.equals(hzVar.f558e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hzVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f548a.m2334a(hzVar.f548a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hzVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f559f.equals(hzVar.f559f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hzVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f560g.equals(hzVar.f560g))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = hzVar.k();
        if ((k2 || k3) && (!k2 || !k3 || this.f553a != hzVar.f553a)) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = hzVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f561h.equals(hzVar.f561h))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = hzVar.m();
        if ((m2 || m3) && (!m2 || !m3 || !this.f562i.equals(hzVar.f562i))) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = hzVar.n();
        if ((n2 || n3) && (!n2 || !n3 || !this.f563j.equals(hzVar.f563j))) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = hzVar.o();
        if ((o2 || o3) && (!o2 || !o3 || this.f552a != hzVar.f552a)) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = hzVar.p();
        if ((p2 || p3) && (!p2 || !p3 || this.f555b != hzVar.f555b)) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = hzVar.q();
        if ((q2 || q3) && (!q2 || !q3 || !this.f564k.equals(hzVar.f564k))) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = hzVar.r();
        if ((r2 || r3) && (!r2 || !r3 || !this.f565l.equals(hzVar.f565l))) {
            return false;
        }
        boolean s2 = s();
        boolean s3 = hzVar.s();
        if ((s2 || s3) && (!s2 || !s3 || this.f545a != hzVar.f545a)) {
            return false;
        }
        boolean t2 = t();
        boolean t3 = hzVar.t();
        if (!t2 && !t3) {
            return true;
        }
        return t2 && t3 && this.f551a.equals(hzVar.f551a);
    }

    public hz b(String str) {
        this.f556c = str;
        return this;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f544a);
        if (this.f549a != null && m2272a()) {
            jfVar.a(a);
            jfVar.a(this.f549a);
            jfVar.b();
        }
        if (this.f547a != null && b()) {
            jfVar.a(b);
            this.f547a.b(jfVar);
            jfVar.b();
        }
        if (this.f554b != null) {
            jfVar.a(c);
            jfVar.a(this.f554b);
            jfVar.b();
        }
        if (this.f556c != null) {
            jfVar.a(d);
            jfVar.a(this.f556c);
            jfVar.b();
        }
        jfVar.a(e);
        jfVar.a(this.f546a);
        jfVar.b();
        if (this.f557d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f557d);
            jfVar.b();
        }
        if (this.f558e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f558e);
            jfVar.b();
        }
        if (this.f548a != null && h()) {
            jfVar.a(h);
            this.f548a.b(jfVar);
            jfVar.b();
        }
        if (this.f559f != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f559f);
            jfVar.b();
        }
        if (this.f560g != null && j()) {
            jfVar.a(j);
            jfVar.a(this.f560g);
            jfVar.b();
        }
        if (k()) {
            jfVar.a(k);
            jfVar.a(this.f553a);
            jfVar.b();
        }
        if (this.f561h != null && l()) {
            jfVar.a(l);
            jfVar.a(this.f561h);
            jfVar.b();
        }
        if (this.f562i != null && m()) {
            jfVar.a(m);
            jfVar.a(this.f562i);
            jfVar.b();
        }
        if (this.f563j != null && n()) {
            jfVar.a(n);
            jfVar.a(this.f563j);
            jfVar.b();
        }
        if (o()) {
            jfVar.a(o);
            jfVar.a(this.f552a);
            jfVar.b();
        }
        if (p()) {
            jfVar.a(p);
            jfVar.a(this.f555b);
            jfVar.b();
        }
        if (this.f564k != null && q()) {
            jfVar.a(q);
            jfVar.a(this.f564k);
            jfVar.b();
        }
        if (this.f565l != null && r()) {
            jfVar.a(r);
            jfVar.a(this.f565l);
            jfVar.b();
        }
        if (s()) {
            jfVar.a(s);
            jfVar.mo2376a(this.f545a);
            jfVar.b();
        }
        if (this.f551a != null && t()) {
            jfVar.a(t);
            jfVar.a(new je((byte) 11, (byte) 11, this.f551a.size()));
            for (Map.Entry<String, String> entry : this.f551a.entrySet()) {
                jfVar.a(entry.getKey());
                jfVar.a(entry.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f550a.set(1, z);
    }

    public boolean b() {
        return this.f547a != null;
    }

    public hz c(String str) {
        this.f557d = str;
        return this;
    }

    public void c(boolean z) {
        this.f550a.set(2, z);
    }

    public boolean c() {
        return this.f554b != null;
    }

    public hz d(String str) {
        this.f558e = str;
        return this;
    }

    public void d(boolean z) {
        this.f550a.set(3, z);
    }

    public boolean d() {
        return this.f556c != null;
    }

    public void e(boolean z) {
        this.f550a.set(4, z);
    }

    public boolean e() {
        return this.f550a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hz)) {
            return m2273a((hz) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f557d != null;
    }

    public boolean g() {
        return this.f558e != null;
    }

    public boolean h() {
        return this.f548a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f559f != null;
    }

    public boolean j() {
        return this.f560g != null;
    }

    public boolean k() {
        return this.f550a.get(1);
    }

    public boolean l() {
        return this.f561h != null;
    }

    public boolean m() {
        return this.f562i != null;
    }

    public boolean n() {
        return this.f563j != null;
    }

    public boolean o() {
        return this.f550a.get(2);
    }

    public boolean p() {
        return this.f550a.get(3);
    }

    public boolean q() {
        return this.f564k != null;
    }

    public boolean r() {
        return this.f565l != null;
    }

    public boolean s() {
        return this.f550a.get(4);
    }

    public boolean t() {
        return this.f551a != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionAckMessage(");
        boolean z2 = false;
        if (m2272a()) {
            sb.append("debug:");
            String str = this.f549a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f547a;
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
        String str2 = this.f554b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f556c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        sb.append(", ");
        sb.append("messageTs:");
        sb.append(this.f546a);
        if (f()) {
            sb.append(", ");
            sb.append("topic:");
            String str4 = this.f557d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("aliasName:");
            String str5 = this.f558e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("request:");
            im imVar = this.f548a;
            if (imVar == null) {
                sb.append("null");
            } else {
                sb.append(imVar);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("packageName:");
            String str6 = this.f559f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("category:");
            String str7 = this.f560g;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        if (k()) {
            sb.append(", ");
            sb.append("isOnline:");
            sb.append(this.f553a);
        }
        if (l()) {
            sb.append(", ");
            sb.append("regId:");
            String str8 = this.f561h;
            if (str8 == null) {
                sb.append("null");
            } else {
                sb.append(str8);
            }
        }
        if (m()) {
            sb.append(", ");
            sb.append("callbackUrl:");
            String str9 = this.f562i;
            if (str9 == null) {
                sb.append("null");
            } else {
                sb.append(str9);
            }
        }
        if (n()) {
            sb.append(", ");
            sb.append("userAccount:");
            String str10 = this.f563j;
            if (str10 == null) {
                sb.append("null");
            } else {
                sb.append(str10);
            }
        }
        if (o()) {
            sb.append(", ");
            sb.append("deviceStatus:");
            sb.append((int) this.f552a);
        }
        if (p()) {
            sb.append(", ");
            sb.append("geoMsgStatus:");
            sb.append((int) this.f555b);
        }
        if (q()) {
            sb.append(", ");
            sb.append("imeiMd5:");
            String str11 = this.f564k;
            if (str11 == null) {
                sb.append("null");
            } else {
                sb.append(str11);
            }
        }
        if (r()) {
            sb.append(", ");
            sb.append("deviceId:");
            String str12 = this.f565l;
            if (str12 == null) {
                sb.append("null");
            } else {
                sb.append(str12);
            }
        }
        if (s()) {
            sb.append(", ");
            sb.append("passThrough:");
            sb.append(this.f545a);
        }
        if (t()) {
            sb.append(", ");
            sb.append("extra:");
            Map<String, String> map = this.f551a;
            if (map == null) {
                sb.append("null");
            } else {
                sb.append(map);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
