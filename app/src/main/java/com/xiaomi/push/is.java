package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class is implements iu<is, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f770a;

    /* renamed from: a  reason: collision with other field name */
    public hy f771a;

    /* renamed from: a  reason: collision with other field name */
    public String f772a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f773a = new BitSet(1);

    /* renamed from: b  reason: collision with other field name */
    public String f774b;

    /* renamed from: c  reason: collision with other field name */
    public String f775c;

    /* renamed from: d  reason: collision with other field name */
    public String f776d;

    /* renamed from: e  reason: collision with other field name */
    public String f777e;

    /* renamed from: f  reason: collision with other field name */
    public String f778f;

    /* renamed from: g  reason: collision with other field name */
    public String f779g;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f769a = new jk("XmPushActionUnSubscriptionResult");
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
    public int compareTo(is isVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        if (!getClass().equals(isVar.getClass())) {
            return getClass().getName().compareTo(isVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2355a()).compareTo(Boolean.valueOf(isVar.m2355a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2355a() && (a10 = iv.a(this.f772a, isVar.f772a)) != 0) {
            return a10;
        }
        int compareTo2 = Boolean.valueOf(m2357b()).compareTo(Boolean.valueOf(isVar.m2357b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2357b() && (a9 = iv.a(this.f771a, isVar.f771a)) != 0) {
            return a9;
        }
        int compareTo3 = Boolean.valueOf(m2358c()).compareTo(Boolean.valueOf(isVar.m2358c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2358c() && (a8 = iv.a(this.f774b, isVar.f774b)) != 0) {
            return a8;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(isVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a7 = iv.a(this.f775c, isVar.f775c)) != 0) {
            return a7;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(isVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a6 = iv.a(this.f770a, isVar.f770a)) != 0) {
            return a6;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(isVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a5 = iv.a(this.f776d, isVar.f776d)) != 0) {
            return a5;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(isVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a4 = iv.a(this.f777e, isVar.f777e)) != 0) {
            return a4;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(isVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a3 = iv.a(this.f778f, isVar.f778f)) != 0) {
            return a3;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(isVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a2 = iv.a(this.f779g, isVar.f779g)) != 0) {
            return a2;
        }
        return 0;
    }

    public String a() {
        return this.f774b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2354a() {
        if (this.f774b != null) {
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
                m2354a();
                return;
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f772a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f771a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f774b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f775c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 10) {
                        this.f770a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f776d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f777e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f778f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f779g = jfVar.m2385a();
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
        this.f773a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2355a() {
        return this.f772a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2356a(is isVar) {
        if (isVar == null) {
            return false;
        }
        boolean m2355a = m2355a();
        boolean m2355a2 = isVar.m2355a();
        if ((m2355a || m2355a2) && (!m2355a || !m2355a2 || !this.f772a.equals(isVar.f772a))) {
            return false;
        }
        boolean m2357b = m2357b();
        boolean m2357b2 = isVar.m2357b();
        if ((m2357b || m2357b2) && (!m2357b || !m2357b2 || !this.f771a.m2271a(isVar.f771a))) {
            return false;
        }
        boolean m2358c = m2358c();
        boolean m2358c2 = isVar.m2358c();
        if ((m2358c || m2358c2) && (!m2358c || !m2358c2 || !this.f774b.equals(isVar.f774b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = isVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f775c.equals(isVar.f775c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = isVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f770a != isVar.f770a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = isVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f776d.equals(isVar.f776d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = isVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f777e.equals(isVar.f777e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = isVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f778f.equals(isVar.f778f))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = isVar.i();
        if (!i2 && !i3) {
            return true;
        }
        return i2 && i3 && this.f779g.equals(isVar.f779g);
    }

    public String b() {
        return this.f777e;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2354a();
        jfVar.a(f769a);
        if (this.f772a != null && m2355a()) {
            jfVar.a(a);
            jfVar.a(this.f772a);
            jfVar.b();
        }
        if (this.f771a != null && m2357b()) {
            jfVar.a(b);
            this.f771a.b(jfVar);
            jfVar.b();
        }
        if (this.f774b != null) {
            jfVar.a(c);
            jfVar.a(this.f774b);
            jfVar.b();
        }
        if (this.f775c != null && d()) {
            jfVar.a(d);
            jfVar.a(this.f775c);
            jfVar.b();
        }
        if (e()) {
            jfVar.a(e);
            jfVar.a(this.f770a);
            jfVar.b();
        }
        if (this.f776d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f776d);
            jfVar.b();
        }
        if (this.f777e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f777e);
            jfVar.b();
        }
        if (this.f778f != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f778f);
            jfVar.b();
        }
        if (this.f779g != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f779g);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2357b() {
        return this.f771a != null;
    }

    public String c() {
        return this.f779g;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2358c() {
        return this.f774b != null;
    }

    public boolean d() {
        return this.f775c != null;
    }

    public boolean e() {
        return this.f773a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof is)) {
            return m2356a((is) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f776d != null;
    }

    public boolean g() {
        return this.f777e != null;
    }

    public boolean h() {
        return this.f778f != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f779g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnSubscriptionResult(");
        boolean z2 = false;
        if (m2355a()) {
            sb.append("debug:");
            String str = this.f772a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (m2357b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f771a;
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
        String str2 = this.f774b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        if (d()) {
            sb.append(", ");
            sb.append("appId:");
            String str3 = this.f775c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
        }
        if (e()) {
            sb.append(", ");
            sb.append("errorCode:");
            sb.append(this.f770a);
        }
        if (f()) {
            sb.append(", ");
            sb.append("reason:");
            String str4 = this.f776d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("topic:");
            String str5 = this.f777e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("packageName:");
            String str6 = this.f778f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("category:");
            String str7 = this.f779g;
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
