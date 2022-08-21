package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/* loaded from: classes3.dex */
public class ie implements iu<ie, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f597a;

    /* renamed from: a  reason: collision with other field name */
    public hy f598a;

    /* renamed from: a  reason: collision with other field name */
    public String f599a;

    /* renamed from: a  reason: collision with other field name */
    public List<String> f601a;

    /* renamed from: b  reason: collision with other field name */
    public String f603b;

    /* renamed from: c  reason: collision with other field name */
    public String f604c;

    /* renamed from: d  reason: collision with other field name */
    public String f605d;

    /* renamed from: e  reason: collision with other field name */
    public String f606e;

    /* renamed from: f  reason: collision with other field name */
    public String f607f;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f596a = new jk("XmPushActionCommandResult");
    public static final jc a = new jc("", (byte) 12, 2);
    public static final jc b = new jc("", (byte) 11, 3);
    public static final jc c = new jc("", (byte) 11, 4);
    public static final jc d = new jc("", (byte) 11, 5);
    public static final jc e = new jc("", (byte) 10, 7);
    public static final jc f = new jc("", (byte) 11, 8);
    public static final jc g = new jc("", (byte) 11, 9);
    public static final jc h = new jc("", (byte) 15, 10);
    public static final jc i = new jc("", (byte) 11, 12);
    public static final jc j = new jc("", (byte) 2, 13);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f600a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public boolean f602a = true;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ie ieVar) {
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
        if (!getClass().equals(ieVar.getClass())) {
            return getClass().getName().compareTo(ieVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2289a()).compareTo(Boolean.valueOf(ieVar.m2289a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2289a() && (a11 = iv.a(this.f598a, ieVar.f598a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(m2291b()).compareTo(Boolean.valueOf(ieVar.m2291b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2291b() && (a10 = iv.a(this.f599a, ieVar.f599a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(m2292c()).compareTo(Boolean.valueOf(ieVar.m2292c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2292c() && (a9 = iv.a(this.f603b, ieVar.f603b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ieVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = iv.a(this.f604c, ieVar.f604c)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ieVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = iv.a(this.f597a, ieVar.f597a)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ieVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = iv.a(this.f605d, ieVar.f605d)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ieVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = iv.a(this.f606e, ieVar.f606e)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ieVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = iv.a(this.f601a, ieVar.f601a)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ieVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = iv.a(this.f607f, ieVar.f607f)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ieVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a2 = iv.a(this.f602a, ieVar.f602a)) != 0) {
            return a2;
        }
        return 0;
    }

    public String a() {
        return this.f599a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public List<String> m2287a() {
        return this.f601a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2288a() {
        if (this.f599a == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f603b == null) {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f604c != null) {
        } else {
            throw new jg("Required field 'cmdName' was not present! Struct: " + toString());
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
                    m2288a();
                    return;
                }
                throw new jg("Required field 'errorCode' was not found in serialized data! Struct: " + toString());
            }
            switch (m2380a.f793a) {
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f598a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f599a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f603b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f604c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 10) {
                        this.f597a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f605d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f606e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 15) {
                        jd m2381a = jfVar.m2381a();
                        this.f601a = new ArrayList(m2381a.f794a);
                        for (int i2 = 0; i2 < m2381a.f794a; i2++) {
                            this.f601a.add(jfVar.m2385a());
                        }
                        jfVar.i();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 11) {
                        this.f607f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 13:
                    if (b2 == 2) {
                        this.f602a = jfVar.m2389a();
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
        this.f600a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2289a() {
        return this.f598a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2290a(ie ieVar) {
        if (ieVar == null) {
            return false;
        }
        boolean m2289a = m2289a();
        boolean m2289a2 = ieVar.m2289a();
        if ((m2289a || m2289a2) && (!m2289a || !m2289a2 || !this.f598a.m2271a(ieVar.f598a))) {
            return false;
        }
        boolean m2291b = m2291b();
        boolean m2291b2 = ieVar.m2291b();
        if ((m2291b || m2291b2) && (!m2291b || !m2291b2 || !this.f599a.equals(ieVar.f599a))) {
            return false;
        }
        boolean m2292c = m2292c();
        boolean m2292c2 = ieVar.m2292c();
        if ((m2292c || m2292c2) && (!m2292c || !m2292c2 || !this.f603b.equals(ieVar.f603b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ieVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f604c.equals(ieVar.f604c))) || this.f597a != ieVar.f597a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ieVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f605d.equals(ieVar.f605d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ieVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f606e.equals(ieVar.f606e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ieVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f601a.equals(ieVar.f601a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ieVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f607f.equals(ieVar.f607f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ieVar.j();
        if (!j2 && !j3) {
            return true;
        }
        return j2 && j3 && this.f602a == ieVar.f602a;
    }

    public String b() {
        return this.f604c;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2288a();
        jfVar.a(f596a);
        if (this.f598a != null && m2289a()) {
            jfVar.a(a);
            this.f598a.b(jfVar);
            jfVar.b();
        }
        if (this.f599a != null) {
            jfVar.a(b);
            jfVar.a(this.f599a);
            jfVar.b();
        }
        if (this.f603b != null) {
            jfVar.a(c);
            jfVar.a(this.f603b);
            jfVar.b();
        }
        if (this.f604c != null) {
            jfVar.a(d);
            jfVar.a(this.f604c);
            jfVar.b();
        }
        jfVar.a(e);
        jfVar.a(this.f597a);
        jfVar.b();
        if (this.f605d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f605d);
            jfVar.b();
        }
        if (this.f606e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f606e);
            jfVar.b();
        }
        if (this.f601a != null && h()) {
            jfVar.a(h);
            jfVar.a(new jd((byte) 11, this.f601a.size()));
            for (String str : this.f601a) {
                jfVar.a(str);
            }
            jfVar.e();
            jfVar.b();
        }
        if (this.f607f != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f607f);
            jfVar.b();
        }
        if (j()) {
            jfVar.a(j);
            jfVar.a(this.f602a);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f600a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2291b() {
        return this.f599a != null;
    }

    public String c() {
        return this.f607f;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2292c() {
        return this.f603b != null;
    }

    public boolean d() {
        return this.f604c != null;
    }

    public boolean e() {
        return this.f600a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ie)) {
            return m2290a((ie) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f605d != null;
    }

    public boolean g() {
        return this.f606e != null;
    }

    public boolean h() {
        return this.f601a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f607f != null;
    }

    public boolean j() {
        return this.f600a.get(1);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionCommandResult(");
        if (m2289a()) {
            sb.append("target:");
            hy hyVar = this.f598a;
            if (hyVar == null) {
                sb.append("null");
            } else {
                sb.append(hyVar);
            }
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            sb.append(", ");
        }
        sb.append("id:");
        String str = this.f599a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(str);
        }
        sb.append(", ");
        sb.append("appId:");
        String str2 = this.f603b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("cmdName:");
        String str3 = this.f604c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        sb.append(", ");
        sb.append("errorCode:");
        sb.append(this.f597a);
        if (f()) {
            sb.append(", ");
            sb.append("reason:");
            String str4 = this.f605d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("packageName:");
            String str5 = this.f606e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("cmdArgs:");
            List<String> list = this.f601a;
            if (list == null) {
                sb.append("null");
            } else {
                sb.append(list);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("category:");
            String str6 = this.f607f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("response2Client:");
            sb.append(this.f602a);
        }
        sb.append(")");
        return sb.toString();
    }
}
