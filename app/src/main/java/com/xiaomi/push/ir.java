package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ir implements iu<ir, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public hy f761a;

    /* renamed from: a  reason: collision with other field name */
    public String f762a;

    /* renamed from: a  reason: collision with other field name */
    public List<String> f763a;

    /* renamed from: b  reason: collision with other field name */
    public String f764b;

    /* renamed from: c  reason: collision with other field name */
    public String f765c;

    /* renamed from: d  reason: collision with other field name */
    public String f766d;

    /* renamed from: e  reason: collision with other field name */
    public String f767e;

    /* renamed from: f  reason: collision with other field name */
    public String f768f;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f760a = new jk("XmPushActionUnSubscription");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 11, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 15, 8);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ir irVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        if (!getClass().equals(irVar.getClass())) {
            return getClass().getName().compareTo(irVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2352a()).compareTo(Boolean.valueOf(irVar.m2352a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2352a() && (a9 = iv.a(this.f762a, irVar.f762a)) != 0) {
            return a9;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(irVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a8 = iv.a(this.f761a, irVar.f761a)) != 0) {
            return a8;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(irVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a7 = iv.a(this.f764b, irVar.f764b)) != 0) {
            return a7;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(irVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a6 = iv.a(this.f765c, irVar.f765c)) != 0) {
            return a6;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(irVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a5 = iv.a(this.f766d, irVar.f766d)) != 0) {
            return a5;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(irVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a4 = iv.a(this.f767e, irVar.f767e)) != 0) {
            return a4;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(irVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a3 = iv.a(this.f768f, irVar.f768f)) != 0) {
            return a3;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(irVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a2 = iv.a(this.f763a, irVar.f763a)) != 0) {
            return a2;
        }
        return 0;
    }

    public ir a(String str) {
        this.f764b = str;
        return this;
    }

    public void a() {
        if (this.f764b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f765c == null) {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f766d != null) {
        } else {
            throw new jg("Required field 'topic' was not present! Struct: " + toString());
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
                        this.f762a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f761a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f764b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f765c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f766d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f767e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f768f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 15) {
                        jd m2381a = jfVar.m2381a();
                        this.f763a = new ArrayList(m2381a.f794a);
                        for (int i = 0; i < m2381a.f794a; i++) {
                            this.f763a.add(jfVar.m2385a());
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

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2352a() {
        return this.f762a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2353a(ir irVar) {
        if (irVar == null) {
            return false;
        }
        boolean m2352a = m2352a();
        boolean m2352a2 = irVar.m2352a();
        if ((m2352a || m2352a2) && (!m2352a || !m2352a2 || !this.f762a.equals(irVar.f762a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = irVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f761a.m2271a(irVar.f761a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = irVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f764b.equals(irVar.f764b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = irVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f765c.equals(irVar.f765c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = irVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f766d.equals(irVar.f766d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = irVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f767e.equals(irVar.f767e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = irVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f768f.equals(irVar.f768f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = irVar.h();
        if (!h2 && !h3) {
            return true;
        }
        return h2 && h3 && this.f763a.equals(irVar.f763a);
    }

    public ir b(String str) {
        this.f765c = str;
        return this;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f760a);
        if (this.f762a != null && m2352a()) {
            jfVar.a(a);
            jfVar.a(this.f762a);
            jfVar.b();
        }
        if (this.f761a != null && b()) {
            jfVar.a(b);
            this.f761a.b(jfVar);
            jfVar.b();
        }
        if (this.f764b != null) {
            jfVar.a(c);
            jfVar.a(this.f764b);
            jfVar.b();
        }
        if (this.f765c != null) {
            jfVar.a(d);
            jfVar.a(this.f765c);
            jfVar.b();
        }
        if (this.f766d != null) {
            jfVar.a(e);
            jfVar.a(this.f766d);
            jfVar.b();
        }
        if (this.f767e != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f767e);
            jfVar.b();
        }
        if (this.f768f != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f768f);
            jfVar.b();
        }
        if (this.f763a != null && h()) {
            jfVar.a(h);
            jfVar.a(new jd((byte) 11, this.f763a.size()));
            for (String str : this.f763a) {
                jfVar.a(str);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean b() {
        return this.f761a != null;
    }

    public ir c(String str) {
        this.f766d = str;
        return this;
    }

    public boolean c() {
        return this.f764b != null;
    }

    public ir d(String str) {
        this.f767e = str;
        return this;
    }

    public boolean d() {
        return this.f765c != null;
    }

    public ir e(String str) {
        this.f768f = str;
        return this;
    }

    public boolean e() {
        return this.f766d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ir)) {
            return m2353a((ir) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f767e != null;
    }

    public boolean g() {
        return this.f768f != null;
    }

    public boolean h() {
        return this.f763a != null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnSubscription(");
        boolean z2 = false;
        if (m2352a()) {
            sb.append("debug:");
            String str = this.f762a;
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
            hy hyVar = this.f761a;
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
        String str2 = this.f764b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f765c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        sb.append(", ");
        sb.append("topic:");
        String str4 = this.f766d;
        if (str4 == null) {
            sb.append("null");
        } else {
            sb.append(str4);
        }
        if (f()) {
            sb.append(", ");
            sb.append("packageName:");
            String str5 = this.f767e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("category:");
            String str6 = this.f768f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("aliases:");
            List<String> list = this.f763a;
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
