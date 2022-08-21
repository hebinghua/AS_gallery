package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class iq implements iu<iq, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f750a;

    /* renamed from: a  reason: collision with other field name */
    public hy f751a;

    /* renamed from: a  reason: collision with other field name */
    public String f752a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f753a = new BitSet(3);

    /* renamed from: b  reason: collision with other field name */
    public long f754b;

    /* renamed from: b  reason: collision with other field name */
    public String f755b;

    /* renamed from: c  reason: collision with other field name */
    public long f756c;

    /* renamed from: c  reason: collision with other field name */
    public String f757c;

    /* renamed from: d  reason: collision with other field name */
    public String f758d;

    /* renamed from: e  reason: collision with other field name */
    public String f759e;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f749a = new jk("XmPushActionUnRegistrationResult");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 10, 6);
    public static final jc f = new jc("", (byte) 11, 7);
    public static final jc g = new jc("", (byte) 11, 8);
    public static final jc h = new jc("", (byte) 10, 9);
    public static final jc i = new jc("", (byte) 10, 10);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(iq iqVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        if (!getClass().equals(iqVar.getClass())) {
            return getClass().getName().compareTo(iqVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2350a()).compareTo(Boolean.valueOf(iqVar.m2350a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2350a() && (a10 = iv.a(this.f752a, iqVar.f752a)) != 0) {
            return a10;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(iqVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a9 = iv.a(this.f751a, iqVar.f751a)) != 0) {
            return a9;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(iqVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a8 = iv.a(this.f755b, iqVar.f755b)) != 0) {
            return a8;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(iqVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a7 = iv.a(this.f757c, iqVar.f757c)) != 0) {
            return a7;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(iqVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a6 = iv.a(this.f750a, iqVar.f750a)) != 0) {
            return a6;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(iqVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a5 = iv.a(this.f758d, iqVar.f758d)) != 0) {
            return a5;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(iqVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a4 = iv.a(this.f759e, iqVar.f759e)) != 0) {
            return a4;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(iqVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a3 = iv.a(this.f754b, iqVar.f754b)) != 0) {
            return a3;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(iqVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a2 = iv.a(this.f756c, iqVar.f756c)) != 0) {
            return a2;
        }
        return 0;
    }

    public String a() {
        return this.f759e;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2349a() {
        if (this.f755b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f757c != null) {
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
                    m2349a();
                    return;
                }
                throw new jg("Required field 'errorCode' was not found in serialized data! Struct: " + toString());
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f752a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f751a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f755b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f757c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 10) {
                        this.f750a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f758d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f759e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 10) {
                        this.f754b = jfVar.m2379a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 10) {
                        this.f756c = jfVar.m2379a();
                        c(true);
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
        this.f753a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2350a() {
        return this.f752a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2351a(iq iqVar) {
        if (iqVar == null) {
            return false;
        }
        boolean m2350a = m2350a();
        boolean m2350a2 = iqVar.m2350a();
        if ((m2350a || m2350a2) && (!m2350a || !m2350a2 || !this.f752a.equals(iqVar.f752a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = iqVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f751a.m2271a(iqVar.f751a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = iqVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f755b.equals(iqVar.f755b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = iqVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f757c.equals(iqVar.f757c))) || this.f750a != iqVar.f750a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = iqVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f758d.equals(iqVar.f758d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = iqVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f759e.equals(iqVar.f759e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = iqVar.h();
        if ((h2 || h3) && (!h2 || !h3 || this.f754b != iqVar.f754b)) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = iqVar.i();
        if (!i2 && !i3) {
            return true;
        }
        return i2 && i3 && this.f756c == iqVar.f756c;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2349a();
        jfVar.a(f749a);
        if (this.f752a != null && m2350a()) {
            jfVar.a(a);
            jfVar.a(this.f752a);
            jfVar.b();
        }
        if (this.f751a != null && b()) {
            jfVar.a(b);
            this.f751a.b(jfVar);
            jfVar.b();
        }
        if (this.f755b != null) {
            jfVar.a(c);
            jfVar.a(this.f755b);
            jfVar.b();
        }
        if (this.f757c != null) {
            jfVar.a(d);
            jfVar.a(this.f757c);
            jfVar.b();
        }
        jfVar.a(e);
        jfVar.a(this.f750a);
        jfVar.b();
        if (this.f758d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f758d);
            jfVar.b();
        }
        if (this.f759e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f759e);
            jfVar.b();
        }
        if (h()) {
            jfVar.a(h);
            jfVar.a(this.f754b);
            jfVar.b();
        }
        if (i()) {
            jfVar.a(i);
            jfVar.a(this.f756c);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f753a.set(1, z);
    }

    public boolean b() {
        return this.f751a != null;
    }

    public void c(boolean z) {
        this.f753a.set(2, z);
    }

    public boolean c() {
        return this.f755b != null;
    }

    public boolean d() {
        return this.f757c != null;
    }

    public boolean e() {
        return this.f753a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof iq)) {
            return m2351a((iq) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f758d != null;
    }

    public boolean g() {
        return this.f759e != null;
    }

    public boolean h() {
        return this.f753a.get(1);
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f753a.get(2);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnRegistrationResult(");
        boolean z2 = false;
        if (m2350a()) {
            sb.append("debug:");
            String str = this.f752a;
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
            hy hyVar = this.f751a;
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
        String str2 = this.f755b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f757c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        sb.append(", ");
        sb.append("errorCode:");
        sb.append(this.f750a);
        if (f()) {
            sb.append(", ");
            sb.append("reason:");
            String str4 = this.f758d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("packageName:");
            String str5 = this.f759e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("unRegisteredAt:");
            sb.append(this.f754b);
        }
        if (i()) {
            sb.append(", ");
            sb.append("costTime:");
            sb.append(this.f756c);
        }
        sb.append(")");
        return sb.toString();
    }
}
