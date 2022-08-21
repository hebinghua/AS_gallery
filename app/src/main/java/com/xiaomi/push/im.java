package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class im implements iu<im, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public hv f702a;

    /* renamed from: a  reason: collision with other field name */
    public hy f703a;

    /* renamed from: a  reason: collision with other field name */
    public String f704a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f706a;

    /* renamed from: b  reason: collision with other field name */
    public String f708b;

    /* renamed from: c  reason: collision with other field name */
    public String f709c;

    /* renamed from: d  reason: collision with other field name */
    public String f710d;

    /* renamed from: e  reason: collision with other field name */
    public String f711e;

    /* renamed from: f  reason: collision with other field name */
    public String f712f;

    /* renamed from: g  reason: collision with other field name */
    public String f713g;

    /* renamed from: h  reason: collision with other field name */
    public String f714h;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f701a = new jk("XmPushActionSendMessage");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 11, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 12, 8);
    public static final jc i = new jc("", (byte) 2, 9);
    public static final jc j = new jc("", (byte) 13, 10);
    public static final jc k = new jc("", (byte) 11, 11);
    public static final jc l = new jc("", (byte) 11, 12);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f705a = new BitSet(1);

    /* renamed from: a  reason: collision with other field name */
    public boolean f707a = true;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(im imVar) {
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
        if (!getClass().equals(imVar.getClass())) {
            return getClass().getName().compareTo(imVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2333a()).compareTo(Boolean.valueOf(imVar.m2333a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2333a() && (a13 = iv.a(this.f704a, imVar.f704a)) != 0) {
            return a13;
        }
        int compareTo2 = Boolean.valueOf(m2335b()).compareTo(Boolean.valueOf(imVar.m2335b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2335b() && (a12 = iv.a(this.f703a, imVar.f703a)) != 0) {
            return a12;
        }
        int compareTo3 = Boolean.valueOf(m2336c()).compareTo(Boolean.valueOf(imVar.m2336c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2336c() && (a11 = iv.a(this.f708b, imVar.f708b)) != 0) {
            return a11;
        }
        int compareTo4 = Boolean.valueOf(m2337d()).compareTo(Boolean.valueOf(imVar.m2337d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (m2337d() && (a10 = iv.a(this.f709c, imVar.f709c)) != 0) {
            return a10;
        }
        int compareTo5 = Boolean.valueOf(m2338e()).compareTo(Boolean.valueOf(imVar.m2338e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (m2338e() && (a9 = iv.a(this.f710d, imVar.f710d)) != 0) {
            return a9;
        }
        int compareTo6 = Boolean.valueOf(m2339f()).compareTo(Boolean.valueOf(imVar.m2339f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (m2339f() && (a8 = iv.a(this.f711e, imVar.f711e)) != 0) {
            return a8;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(imVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a7 = iv.a(this.f712f, imVar.f712f)) != 0) {
            return a7;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(imVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a6 = iv.a(this.f702a, imVar.f702a)) != 0) {
            return a6;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(imVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a5 = iv.a(this.f707a, imVar.f707a)) != 0) {
            return a5;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(imVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a4 = iv.a(this.f706a, imVar.f706a)) != 0) {
            return a4;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(imVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a3 = iv.a(this.f713g, imVar.f713g)) != 0) {
            return a3;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(imVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a2 = iv.a(this.f714h, imVar.f714h)) != 0) {
            return a2;
        }
        return 0;
    }

    public hv a() {
        return this.f702a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2331a() {
        return this.f708b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2332a() {
        if (this.f708b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f709c != null) {
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
                m2332a();
                return;
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f704a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f703a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f708b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f709c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f710d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f711e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f712f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 12) {
                        hv hvVar = new hv();
                        this.f702a = hvVar;
                        hvVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 2) {
                        this.f707a = jfVar.m2389a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 13) {
                        je m2382a = jfVar.m2382a();
                        this.f706a = new HashMap(m2382a.f795a * 2);
                        for (int i2 = 0; i2 < m2382a.f795a; i2++) {
                            this.f706a.put(jfVar.m2385a(), jfVar.m2385a());
                        }
                        jfVar.h();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 11) {
                        this.f713g = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 11) {
                        this.f714h = jfVar.m2385a();
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
        this.f705a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2333a() {
        return this.f704a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2334a(im imVar) {
        if (imVar == null) {
            return false;
        }
        boolean m2333a = m2333a();
        boolean m2333a2 = imVar.m2333a();
        if ((m2333a || m2333a2) && (!m2333a || !m2333a2 || !this.f704a.equals(imVar.f704a))) {
            return false;
        }
        boolean m2335b = m2335b();
        boolean m2335b2 = imVar.m2335b();
        if ((m2335b || m2335b2) && (!m2335b || !m2335b2 || !this.f703a.m2271a(imVar.f703a))) {
            return false;
        }
        boolean m2336c = m2336c();
        boolean m2336c2 = imVar.m2336c();
        if ((m2336c || m2336c2) && (!m2336c || !m2336c2 || !this.f708b.equals(imVar.f708b))) {
            return false;
        }
        boolean m2337d = m2337d();
        boolean m2337d2 = imVar.m2337d();
        if ((m2337d || m2337d2) && (!m2337d || !m2337d2 || !this.f709c.equals(imVar.f709c))) {
            return false;
        }
        boolean m2338e = m2338e();
        boolean m2338e2 = imVar.m2338e();
        if ((m2338e || m2338e2) && (!m2338e || !m2338e2 || !this.f710d.equals(imVar.f710d))) {
            return false;
        }
        boolean m2339f = m2339f();
        boolean m2339f2 = imVar.m2339f();
        if ((m2339f || m2339f2) && (!m2339f || !m2339f2 || !this.f711e.equals(imVar.f711e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = imVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f712f.equals(imVar.f712f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = imVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f702a.m2254a(imVar.f702a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = imVar.i();
        if ((i2 || i3) && (!i2 || !i3 || this.f707a != imVar.f707a)) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = imVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f706a.equals(imVar.f706a))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = imVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f713g.equals(imVar.f713g))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = imVar.l();
        if (!l2 && !l3) {
            return true;
        }
        return l2 && l3 && this.f714h.equals(imVar.f714h);
    }

    public String b() {
        return this.f709c;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2332a();
        jfVar.a(f701a);
        if (this.f704a != null && m2333a()) {
            jfVar.a(a);
            jfVar.a(this.f704a);
            jfVar.b();
        }
        if (this.f703a != null && m2335b()) {
            jfVar.a(b);
            this.f703a.b(jfVar);
            jfVar.b();
        }
        if (this.f708b != null) {
            jfVar.a(c);
            jfVar.a(this.f708b);
            jfVar.b();
        }
        if (this.f709c != null) {
            jfVar.a(d);
            jfVar.a(this.f709c);
            jfVar.b();
        }
        if (this.f710d != null && m2338e()) {
            jfVar.a(e);
            jfVar.a(this.f710d);
            jfVar.b();
        }
        if (this.f711e != null && m2339f()) {
            jfVar.a(f);
            jfVar.a(this.f711e);
            jfVar.b();
        }
        if (this.f712f != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f712f);
            jfVar.b();
        }
        if (this.f702a != null && h()) {
            jfVar.a(h);
            this.f702a.b(jfVar);
            jfVar.b();
        }
        if (i()) {
            jfVar.a(i);
            jfVar.a(this.f707a);
            jfVar.b();
        }
        if (this.f706a != null && j()) {
            jfVar.a(j);
            jfVar.a(new je((byte) 11, (byte) 11, this.f706a.size()));
            for (Map.Entry<String, String> entry : this.f706a.entrySet()) {
                jfVar.a(entry.getKey());
                jfVar.a(entry.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        if (this.f713g != null && k()) {
            jfVar.a(k);
            jfVar.a(this.f713g);
            jfVar.b();
        }
        if (this.f714h != null && l()) {
            jfVar.a(l);
            jfVar.a(this.f714h);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2335b() {
        return this.f703a != null;
    }

    public String c() {
        return this.f711e;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2336c() {
        return this.f708b != null;
    }

    public String d() {
        return this.f712f;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m2337d() {
        return this.f709c != null;
    }

    public String e() {
        return this.f713g;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m2338e() {
        return this.f710d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof im)) {
            return m2334a((im) obj);
        }
        return false;
    }

    public String f() {
        return this.f714h;
    }

    /* renamed from: f  reason: collision with other method in class */
    public boolean m2339f() {
        return this.f711e != null;
    }

    public boolean g() {
        return this.f712f != null;
    }

    public boolean h() {
        return this.f702a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f705a.get(0);
    }

    public boolean j() {
        return this.f706a != null;
    }

    public boolean k() {
        return this.f713g != null;
    }

    public boolean l() {
        return this.f714h != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionSendMessage(");
        boolean z2 = false;
        if (m2333a()) {
            sb.append("debug:");
            String str = this.f704a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (m2335b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f703a;
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
        String str2 = this.f708b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f709c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        if (m2338e()) {
            sb.append(", ");
            sb.append("packageName:");
            String str4 = this.f710d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (m2339f()) {
            sb.append(", ");
            sb.append("topic:");
            String str5 = this.f711e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("aliasName:");
            String str6 = this.f712f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("message:");
            hv hvVar = this.f702a;
            if (hvVar == null) {
                sb.append("null");
            } else {
                sb.append(hvVar);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("needAck:");
            sb.append(this.f707a);
        }
        if (j()) {
            sb.append(", ");
            sb.append("params:");
            Map<String, String> map = this.f706a;
            if (map == null) {
                sb.append("null");
            } else {
                sb.append(map);
            }
        }
        if (k()) {
            sb.append(", ");
            sb.append("category:");
            String str7 = this.f713g;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        if (l()) {
            sb.append(", ");
            sb.append("userAccount:");
            String str8 = this.f714h;
            if (str8 == null) {
                sb.append("null");
            } else {
                sb.append(str8);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
