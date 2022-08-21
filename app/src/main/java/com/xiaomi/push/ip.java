package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class ip implements iu<ip, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f736a;

    /* renamed from: a  reason: collision with other field name */
    public hy f737a;

    /* renamed from: a  reason: collision with other field name */
    public String f738a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f739a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public boolean f740a = true;

    /* renamed from: b  reason: collision with other field name */
    public String f741b;

    /* renamed from: c  reason: collision with other field name */
    public String f742c;

    /* renamed from: d  reason: collision with other field name */
    public String f743d;

    /* renamed from: e  reason: collision with other field name */
    public String f744e;

    /* renamed from: f  reason: collision with other field name */
    public String f745f;

    /* renamed from: g  reason: collision with other field name */
    public String f746g;

    /* renamed from: h  reason: collision with other field name */
    public String f747h;

    /* renamed from: i  reason: collision with other field name */
    public String f748i;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f735a = new jk("XmPushActionUnRegistration");
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
    public static final jc k = new jc("", (byte) 2, 11);
    public static final jc l = new jc("", (byte) 10, 12);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ip ipVar) {
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
        if (!getClass().equals(ipVar.getClass())) {
            return getClass().getName().compareTo(ipVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2347a()).compareTo(Boolean.valueOf(ipVar.m2347a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2347a() && (a13 = iv.a(this.f738a, ipVar.f738a)) != 0) {
            return a13;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ipVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a12 = iv.a(this.f737a, ipVar.f737a)) != 0) {
            return a12;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ipVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a11 = iv.a(this.f741b, ipVar.f741b)) != 0) {
            return a11;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ipVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a10 = iv.a(this.f742c, ipVar.f742c)) != 0) {
            return a10;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ipVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a9 = iv.a(this.f743d, ipVar.f743d)) != 0) {
            return a9;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ipVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a8 = iv.a(this.f744e, ipVar.f744e)) != 0) {
            return a8;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ipVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a7 = iv.a(this.f745f, ipVar.f745f)) != 0) {
            return a7;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ipVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a6 = iv.a(this.f746g, ipVar.f746g)) != 0) {
            return a6;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ipVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a5 = iv.a(this.f747h, ipVar.f747h)) != 0) {
            return a5;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ipVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a4 = iv.a(this.f748i, ipVar.f748i)) != 0) {
            return a4;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(ipVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a3 = iv.a(this.f740a, ipVar.f740a)) != 0) {
            return a3;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(ipVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a2 = iv.a(this.f736a, ipVar.f736a)) != 0) {
            return a2;
        }
        return 0;
    }

    public ip a(String str) {
        this.f741b = str;
        return this;
    }

    public void a() {
        if (this.f741b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f742c != null) {
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
                a();
                return;
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f738a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f737a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f741b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f742c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f743d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f744e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f745f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f746g = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f747h = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f748i = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 2) {
                        this.f740a = jfVar.m2389a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 10) {
                        this.f736a = jfVar.m2379a();
                        b(true);
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
        this.f739a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2347a() {
        return this.f738a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2348a(ip ipVar) {
        if (ipVar == null) {
            return false;
        }
        boolean m2347a = m2347a();
        boolean m2347a2 = ipVar.m2347a();
        if ((m2347a || m2347a2) && (!m2347a || !m2347a2 || !this.f738a.equals(ipVar.f738a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ipVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f737a.m2271a(ipVar.f737a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ipVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f741b.equals(ipVar.f741b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ipVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f742c.equals(ipVar.f742c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ipVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f743d.equals(ipVar.f743d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ipVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f744e.equals(ipVar.f744e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ipVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f745f.equals(ipVar.f745f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ipVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f746g.equals(ipVar.f746g))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ipVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f747h.equals(ipVar.f747h))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ipVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f748i.equals(ipVar.f748i))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = ipVar.k();
        if ((k2 || k3) && (!k2 || !k3 || this.f740a != ipVar.f740a)) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = ipVar.l();
        if (!l2 && !l3) {
            return true;
        }
        return l2 && l3 && this.f736a == ipVar.f736a;
    }

    public ip b(String str) {
        this.f742c = str;
        return this;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f735a);
        if (this.f738a != null && m2347a()) {
            jfVar.a(a);
            jfVar.a(this.f738a);
            jfVar.b();
        }
        if (this.f737a != null && b()) {
            jfVar.a(b);
            this.f737a.b(jfVar);
            jfVar.b();
        }
        if (this.f741b != null) {
            jfVar.a(c);
            jfVar.a(this.f741b);
            jfVar.b();
        }
        if (this.f742c != null) {
            jfVar.a(d);
            jfVar.a(this.f742c);
            jfVar.b();
        }
        if (this.f743d != null && e()) {
            jfVar.a(e);
            jfVar.a(this.f743d);
            jfVar.b();
        }
        if (this.f744e != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f744e);
            jfVar.b();
        }
        if (this.f745f != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f745f);
            jfVar.b();
        }
        if (this.f746g != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f746g);
            jfVar.b();
        }
        if (this.f747h != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f747h);
            jfVar.b();
        }
        if (this.f748i != null && j()) {
            jfVar.a(j);
            jfVar.a(this.f748i);
            jfVar.b();
        }
        if (k()) {
            jfVar.a(k);
            jfVar.a(this.f740a);
            jfVar.b();
        }
        if (l()) {
            jfVar.a(l);
            jfVar.a(this.f736a);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f739a.set(1, z);
    }

    public boolean b() {
        return this.f737a != null;
    }

    public ip c(String str) {
        this.f743d = str;
        return this;
    }

    public boolean c() {
        return this.f741b != null;
    }

    public ip d(String str) {
        this.f745f = str;
        return this;
    }

    public boolean d() {
        return this.f742c != null;
    }

    public ip e(String str) {
        this.f746g = str;
        return this;
    }

    public boolean e() {
        return this.f743d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ip)) {
            return m2348a((ip) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f744e != null;
    }

    public boolean g() {
        return this.f745f != null;
    }

    public boolean h() {
        return this.f746g != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f747h != null;
    }

    public boolean j() {
        return this.f748i != null;
    }

    public boolean k() {
        return this.f739a.get(0);
    }

    public boolean l() {
        return this.f739a.get(1);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnRegistration(");
        boolean z2 = false;
        if (m2347a()) {
            sb.append("debug:");
            String str = this.f738a;
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
            hy hyVar = this.f737a;
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
        String str2 = this.f741b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f742c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        if (e()) {
            sb.append(", ");
            sb.append("regId:");
            String str4 = this.f743d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (f()) {
            sb.append(", ");
            sb.append("appVersion:");
            String str5 = this.f744e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("packageName:");
            String str6 = this.f745f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("token:");
            String str7 = this.f746g;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("deviceId:");
            String str8 = this.f747h;
            if (str8 == null) {
                sb.append("null");
            } else {
                sb.append(str8);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("aliasName:");
            String str9 = this.f748i;
            if (str9 == null) {
                sb.append("null");
            } else {
                sb.append(str9);
            }
        }
        if (k()) {
            sb.append(", ");
            sb.append("needAck:");
            sb.append(this.f740a);
        }
        if (l()) {
            sb.append(", ");
            sb.append("createdTs:");
            sb.append(this.f736a);
        }
        sb.append(")");
        return sb.toString();
    }
}
