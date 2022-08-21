package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class in implements iu<in, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public hy f716a;

    /* renamed from: a  reason: collision with other field name */
    public String f717a;

    /* renamed from: a  reason: collision with other field name */
    public List<String> f718a;

    /* renamed from: b  reason: collision with other field name */
    public String f719b;

    /* renamed from: c  reason: collision with other field name */
    public String f720c;

    /* renamed from: d  reason: collision with other field name */
    public String f721d;

    /* renamed from: e  reason: collision with other field name */
    public String f722e;

    /* renamed from: f  reason: collision with other field name */
    public String f723f;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f715a = new jk("XmPushActionSubscription");
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
    public int compareTo(in inVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        if (!getClass().equals(inVar.getClass())) {
            return getClass().getName().compareTo(inVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2340a()).compareTo(Boolean.valueOf(inVar.m2340a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2340a() && (a9 = iv.a(this.f717a, inVar.f717a)) != 0) {
            return a9;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(inVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a8 = iv.a(this.f716a, inVar.f716a)) != 0) {
            return a8;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(inVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a7 = iv.a(this.f719b, inVar.f719b)) != 0) {
            return a7;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(inVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a6 = iv.a(this.f720c, inVar.f720c)) != 0) {
            return a6;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(inVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a5 = iv.a(this.f721d, inVar.f721d)) != 0) {
            return a5;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(inVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a4 = iv.a(this.f722e, inVar.f722e)) != 0) {
            return a4;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(inVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a3 = iv.a(this.f723f, inVar.f723f)) != 0) {
            return a3;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(inVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a2 = iv.a(this.f718a, inVar.f718a)) != 0) {
            return a2;
        }
        return 0;
    }

    public in a(String str) {
        this.f719b = str;
        return this;
    }

    public void a() {
        if (this.f719b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f720c == null) {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f721d != null) {
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
                        this.f717a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f716a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f719b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f720c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f721d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f722e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f723f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 15) {
                        jd m2381a = jfVar.m2381a();
                        this.f718a = new ArrayList(m2381a.f794a);
                        for (int i = 0; i < m2381a.f794a; i++) {
                            this.f718a.add(jfVar.m2385a());
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
    public boolean m2340a() {
        return this.f717a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2341a(in inVar) {
        if (inVar == null) {
            return false;
        }
        boolean m2340a = m2340a();
        boolean m2340a2 = inVar.m2340a();
        if ((m2340a || m2340a2) && (!m2340a || !m2340a2 || !this.f717a.equals(inVar.f717a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = inVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f716a.m2271a(inVar.f716a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = inVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f719b.equals(inVar.f719b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = inVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f720c.equals(inVar.f720c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = inVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f721d.equals(inVar.f721d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = inVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f722e.equals(inVar.f722e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = inVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f723f.equals(inVar.f723f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = inVar.h();
        if (!h2 && !h3) {
            return true;
        }
        return h2 && h3 && this.f718a.equals(inVar.f718a);
    }

    public in b(String str) {
        this.f720c = str;
        return this;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f715a);
        if (this.f717a != null && m2340a()) {
            jfVar.a(a);
            jfVar.a(this.f717a);
            jfVar.b();
        }
        if (this.f716a != null && b()) {
            jfVar.a(b);
            this.f716a.b(jfVar);
            jfVar.b();
        }
        if (this.f719b != null) {
            jfVar.a(c);
            jfVar.a(this.f719b);
            jfVar.b();
        }
        if (this.f720c != null) {
            jfVar.a(d);
            jfVar.a(this.f720c);
            jfVar.b();
        }
        if (this.f721d != null) {
            jfVar.a(e);
            jfVar.a(this.f721d);
            jfVar.b();
        }
        if (this.f722e != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f722e);
            jfVar.b();
        }
        if (this.f723f != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f723f);
            jfVar.b();
        }
        if (this.f718a != null && h()) {
            jfVar.a(h);
            jfVar.a(new jd((byte) 11, this.f718a.size()));
            for (String str : this.f718a) {
                jfVar.a(str);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean b() {
        return this.f716a != null;
    }

    public in c(String str) {
        this.f721d = str;
        return this;
    }

    public boolean c() {
        return this.f719b != null;
    }

    public in d(String str) {
        this.f722e = str;
        return this;
    }

    public boolean d() {
        return this.f720c != null;
    }

    public in e(String str) {
        this.f723f = str;
        return this;
    }

    public boolean e() {
        return this.f721d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof in)) {
            return m2341a((in) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f722e != null;
    }

    public boolean g() {
        return this.f723f != null;
    }

    public boolean h() {
        return this.f718a != null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionSubscription(");
        boolean z2 = false;
        if (m2340a()) {
            sb.append("debug:");
            String str = this.f717a;
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
            hy hyVar = this.f716a;
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
        String str2 = this.f719b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f720c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        sb.append(", ");
        sb.append("topic:");
        String str4 = this.f721d;
        if (str4 == null) {
            sb.append("null");
        } else {
            sb.append(str4);
        }
        if (f()) {
            sb.append(", ");
            sb.append("packageName:");
            String str5 = this.f722e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("category:");
            String str6 = this.f723f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("aliases:");
            List<String> list = this.f718a;
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
