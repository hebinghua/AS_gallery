package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class ia implements iu<ia, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public hy f568a;

    /* renamed from: a  reason: collision with other field name */
    public String f569a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f571a;

    /* renamed from: b  reason: collision with other field name */
    public String f572b;

    /* renamed from: c  reason: collision with other field name */
    public String f573c;

    /* renamed from: d  reason: collision with other field name */
    public String f574d;

    /* renamed from: e  reason: collision with other field name */
    public String f575e;

    /* renamed from: f  reason: collision with other field name */
    public String f576f;

    /* renamed from: g  reason: collision with other field name */
    public String f577g;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f566a = new jk("XmPushActionAckNotification");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 10, 7);
    public static final jc g = new jc("", (byte) 11, 8);
    public static final jc h = new jc("", (byte) 13, 9);
    public static final jc i = new jc("", (byte) 11, 10);
    public static final jc j = new jc("", (byte) 11, 11);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f570a = new BitSet(1);

    /* renamed from: a  reason: collision with other field name */
    public long f567a = 0;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ia iaVar) {
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
        if (!getClass().equals(iaVar.getClass())) {
            return getClass().getName().compareTo(iaVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2276a()).compareTo(Boolean.valueOf(iaVar.m2276a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2276a() && (a11 = iv.a(this.f569a, iaVar.f569a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(m2278b()).compareTo(Boolean.valueOf(iaVar.m2278b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2278b() && (a10 = iv.a(this.f568a, iaVar.f568a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(iaVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a9 = iv.a(this.f572b, iaVar.f572b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(iaVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = iv.a(this.f573c, iaVar.f573c)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(iaVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = iv.a(this.f574d, iaVar.f574d)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(iaVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = iv.a(this.f567a, iaVar.f567a)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(iaVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = iv.a(this.f575e, iaVar.f575e)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(iaVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = iv.a(this.f571a, iaVar.f571a)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(iaVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = iv.a(this.f576f, iaVar.f576f)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(iaVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a2 = iv.a(this.f577g, iaVar.f577g)) != 0) {
            return a2;
        }
        return 0;
    }

    public ia a(long j2) {
        this.f567a = j2;
        a(true);
        return this;
    }

    public ia a(hy hyVar) {
        this.f568a = hyVar;
        return this;
    }

    public ia a(String str) {
        this.f572b = str;
        return this;
    }

    public String a() {
        return this.f572b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, String> m2274a() {
        return this.f571a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2275a() {
        if (this.f572b != null) {
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
                m2275a();
                return;
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f569a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f568a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f572b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f573c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f574d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 10) {
                        this.f567a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f575e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 13) {
                        je m2382a = jfVar.m2382a();
                        this.f571a = new HashMap(m2382a.f795a * 2);
                        for (int i2 = 0; i2 < m2382a.f795a; i2++) {
                            this.f571a.put(jfVar.m2385a(), jfVar.m2385a());
                        }
                        jfVar.h();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f576f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 11) {
                        this.f577g = jfVar.m2385a();
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
        this.f570a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2276a() {
        return this.f569a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2277a(ia iaVar) {
        if (iaVar == null) {
            return false;
        }
        boolean m2276a = m2276a();
        boolean m2276a2 = iaVar.m2276a();
        if ((m2276a || m2276a2) && (!m2276a || !m2276a2 || !this.f569a.equals(iaVar.f569a))) {
            return false;
        }
        boolean m2278b = m2278b();
        boolean m2278b2 = iaVar.m2278b();
        if ((m2278b || m2278b2) && (!m2278b || !m2278b2 || !this.f568a.m2271a(iaVar.f568a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = iaVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f572b.equals(iaVar.f572b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = iaVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f573c.equals(iaVar.f573c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = iaVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f574d.equals(iaVar.f574d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = iaVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f567a != iaVar.f567a)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = iaVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f575e.equals(iaVar.f575e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = iaVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f571a.equals(iaVar.f571a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = iaVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f576f.equals(iaVar.f576f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = iaVar.j();
        if (!j2 && !j3) {
            return true;
        }
        return j2 && j3 && this.f577g.equals(iaVar.f577g);
    }

    public ia b(String str) {
        this.f573c = str;
        return this;
    }

    public String b() {
        return this.f574d;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2275a();
        jfVar.a(f566a);
        if (this.f569a != null && m2276a()) {
            jfVar.a(a);
            jfVar.a(this.f569a);
            jfVar.b();
        }
        if (this.f568a != null && m2278b()) {
            jfVar.a(b);
            this.f568a.b(jfVar);
            jfVar.b();
        }
        if (this.f572b != null) {
            jfVar.a(c);
            jfVar.a(this.f572b);
            jfVar.b();
        }
        if (this.f573c != null && d()) {
            jfVar.a(d);
            jfVar.a(this.f573c);
            jfVar.b();
        }
        if (this.f574d != null && e()) {
            jfVar.a(e);
            jfVar.a(this.f574d);
            jfVar.b();
        }
        if (f()) {
            jfVar.a(f);
            jfVar.a(this.f567a);
            jfVar.b();
        }
        if (this.f575e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f575e);
            jfVar.b();
        }
        if (this.f571a != null && h()) {
            jfVar.a(h);
            jfVar.a(new je((byte) 11, (byte) 11, this.f571a.size()));
            for (Map.Entry<String, String> entry : this.f571a.entrySet()) {
                jfVar.a(entry.getKey());
                jfVar.a(entry.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        if (this.f576f != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f576f);
            jfVar.b();
        }
        if (this.f577g != null && j()) {
            jfVar.a(j);
            jfVar.a(this.f577g);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2278b() {
        return this.f568a != null;
    }

    public ia c(String str) {
        this.f574d = str;
        return this;
    }

    public boolean c() {
        return this.f572b != null;
    }

    public ia d(String str) {
        this.f575e = str;
        return this;
    }

    public boolean d() {
        return this.f573c != null;
    }

    public ia e(String str) {
        this.f576f = str;
        return this;
    }

    public boolean e() {
        return this.f574d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ia)) {
            return m2277a((ia) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f570a.get(0);
    }

    public boolean g() {
        return this.f575e != null;
    }

    public boolean h() {
        return this.f571a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f576f != null;
    }

    public boolean j() {
        return this.f577g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionAckNotification(");
        boolean z2 = false;
        if (m2276a()) {
            sb.append("debug:");
            String str = this.f569a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (m2278b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f568a;
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
        String str2 = this.f572b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        if (d()) {
            sb.append(", ");
            sb.append("appId:");
            String str3 = this.f573c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
        }
        if (e()) {
            sb.append(", ");
            sb.append("type:");
            String str4 = this.f574d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (f()) {
            sb.append(", ");
            sb.append("errorCode:");
            sb.append(this.f567a);
        }
        if (g()) {
            sb.append(", ");
            sb.append("reason:");
            String str5 = this.f575e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("extra:");
            Map<String, String> map = this.f571a;
            if (map == null) {
                sb.append("null");
            } else {
                sb.append(map);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("packageName:");
            String str6 = this.f576f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("category:");
            String str7 = this.f577g;
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
