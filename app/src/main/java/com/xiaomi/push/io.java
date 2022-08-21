package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class io implements iu<io, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f725a;

    /* renamed from: a  reason: collision with other field name */
    public hy f726a;

    /* renamed from: a  reason: collision with other field name */
    public String f727a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f728a = new BitSet(1);

    /* renamed from: b  reason: collision with other field name */
    public String f729b;

    /* renamed from: c  reason: collision with other field name */
    public String f730c;

    /* renamed from: d  reason: collision with other field name */
    public String f731d;

    /* renamed from: e  reason: collision with other field name */
    public String f732e;

    /* renamed from: f  reason: collision with other field name */
    public String f733f;

    /* renamed from: g  reason: collision with other field name */
    public String f734g;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f724a = new jk("XmPushActionSubscriptionResult");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 10, 6);
    public static final jc f = new jc("", (byte) 11, 7);
    public static final jc g = new jc("", (byte) 11, 8);
    public static final jc h = new jc("", (byte) 11, 9);
    public static final jc i = new jc("", (byte) 11, 10);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(io ioVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        if (!getClass().equals(ioVar.getClass())) {
            return getClass().getName().compareTo(ioVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2343a()).compareTo(Boolean.valueOf(ioVar.m2343a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2343a() && (a10 = iv.a(this.f727a, ioVar.f727a)) != 0) {
            return a10;
        }
        int compareTo2 = Boolean.valueOf(m2345b()).compareTo(Boolean.valueOf(ioVar.m2345b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2345b() && (a9 = iv.a(this.f726a, ioVar.f726a)) != 0) {
            return a9;
        }
        int compareTo3 = Boolean.valueOf(m2346c()).compareTo(Boolean.valueOf(ioVar.m2346c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2346c() && (a8 = iv.a(this.f729b, ioVar.f729b)) != 0) {
            return a8;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ioVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a7 = iv.a(this.f730c, ioVar.f730c)) != 0) {
            return a7;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ioVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a6 = iv.a(this.f725a, ioVar.f725a)) != 0) {
            return a6;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ioVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a5 = iv.a(this.f731d, ioVar.f731d)) != 0) {
            return a5;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ioVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a4 = iv.a(this.f732e, ioVar.f732e)) != 0) {
            return a4;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ioVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a3 = iv.a(this.f733f, ioVar.f733f)) != 0) {
            return a3;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ioVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a2 = iv.a(this.f734g, ioVar.f734g)) != 0) {
            return a2;
        }
        return 0;
    }

    public String a() {
        return this.f729b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2342a() {
        if (this.f729b != null) {
            return;
        }
        throw new jg("Required field 'id' was not present! Struct: " + toString());
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                jfVar.f();
                m2342a();
                return;
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f727a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f726a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f729b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f730c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 10) {
                        this.f725a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f731d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f732e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f733f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f734g = jfVar.m2385a();
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
        this.f728a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2343a() {
        return this.f727a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2344a(io ioVar) {
        if (ioVar == null) {
            return false;
        }
        boolean m2343a = m2343a();
        boolean m2343a2 = ioVar.m2343a();
        if ((m2343a || m2343a2) && (!m2343a || !m2343a2 || !this.f727a.equals(ioVar.f727a))) {
            return false;
        }
        boolean m2345b = m2345b();
        boolean m2345b2 = ioVar.m2345b();
        if ((m2345b || m2345b2) && (!m2345b || !m2345b2 || !this.f726a.m2271a(ioVar.f726a))) {
            return false;
        }
        boolean m2346c = m2346c();
        boolean m2346c2 = ioVar.m2346c();
        if ((m2346c || m2346c2) && (!m2346c || !m2346c2 || !this.f729b.equals(ioVar.f729b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ioVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f730c.equals(ioVar.f730c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ioVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f725a != ioVar.f725a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ioVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f731d.equals(ioVar.f731d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ioVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f732e.equals(ioVar.f732e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ioVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f733f.equals(ioVar.f733f))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ioVar.i();
        if (!i2 && !i3) {
            return true;
        }
        return i2 && i3 && this.f734g.equals(ioVar.f734g);
    }

    public String b() {
        return this.f732e;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2342a();
        jfVar.a(f724a);
        if (this.f727a != null && m2343a()) {
            jfVar.a(a);
            jfVar.a(this.f727a);
            jfVar.b();
        }
        if (this.f726a != null && m2345b()) {
            jfVar.a(b);
            this.f726a.b(jfVar);
            jfVar.b();
        }
        if (this.f729b != null) {
            jfVar.a(c);
            jfVar.a(this.f729b);
            jfVar.b();
        }
        if (this.f730c != null && d()) {
            jfVar.a(d);
            jfVar.a(this.f730c);
            jfVar.b();
        }
        if (e()) {
            jfVar.a(e);
            jfVar.a(this.f725a);
            jfVar.b();
        }
        if (this.f731d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f731d);
            jfVar.b();
        }
        if (this.f732e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f732e);
            jfVar.b();
        }
        if (this.f733f != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f733f);
            jfVar.b();
        }
        if (this.f734g != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f734g);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2345b() {
        return this.f726a != null;
    }

    public String c() {
        return this.f734g;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2346c() {
        return this.f729b != null;
    }

    public boolean d() {
        return this.f730c != null;
    }

    public boolean e() {
        return this.f728a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof io)) {
            return m2344a((io) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f731d != null;
    }

    public boolean g() {
        return this.f732e != null;
    }

    public boolean h() {
        return this.f733f != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f734g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionSubscriptionResult(");
        boolean z2 = false;
        if (m2343a()) {
            sb.append("debug:");
            String str = this.f727a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (m2345b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f726a;
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
        String str2 = this.f729b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        if (d()) {
            sb.append(", ");
            sb.append("appId:");
            String str3 = this.f730c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
        }
        if (e()) {
            sb.append(", ");
            sb.append("errorCode:");
            sb.append(this.f725a);
        }
        if (f()) {
            sb.append(", ");
            sb.append("reason:");
            String str4 = this.f731d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("topic:");
            String str5 = this.f732e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("packageName:");
            String str6 = this.f733f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("category:");
            String str7 = this.f734g;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
