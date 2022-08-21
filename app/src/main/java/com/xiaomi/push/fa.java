package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class fa implements iu<fa, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public byte f322a;

    /* renamed from: a  reason: collision with other field name */
    public int f323a;

    /* renamed from: a  reason: collision with other field name */
    public String f324a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f325a = new BitSet(6);

    /* renamed from: b  reason: collision with other field name */
    public int f326b;

    /* renamed from: b  reason: collision with other field name */
    public String f327b;

    /* renamed from: c  reason: collision with other field name */
    public int f328c;

    /* renamed from: c  reason: collision with other field name */
    public String f329c;

    /* renamed from: d  reason: collision with other field name */
    public int f330d;

    /* renamed from: d  reason: collision with other field name */
    public String f331d;

    /* renamed from: e  reason: collision with other field name */
    public int f332e;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f321a = new jk("StatsEvent");
    public static final jc a = new jc("", (byte) 3, 1);
    public static final jc b = new jc("", (byte) 8, 2);
    public static final jc c = new jc("", (byte) 8, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 8, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 11, 8);
    public static final jc i = new jc("", (byte) 8, 9);
    public static final jc j = new jc("", (byte) 8, 10);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(fa faVar) {
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
        if (!getClass().equals(faVar.getClass())) {
            return getClass().getName().compareTo(faVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2144a()).compareTo(Boolean.valueOf(faVar.m2144a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2144a() && (a11 = iv.a(this.f322a, faVar.f322a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(faVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a10 = iv.a(this.f323a, faVar.f323a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(faVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a9 = iv.a(this.f326b, faVar.f326b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(faVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = iv.a(this.f324a, faVar.f324a)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(faVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = iv.a(this.f327b, faVar.f327b)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(faVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = iv.a(this.f328c, faVar.f328c)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(faVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = iv.a(this.f329c, faVar.f329c)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(faVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = iv.a(this.f331d, faVar.f331d)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(faVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = iv.a(this.f330d, faVar.f330d)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(faVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a2 = iv.a(this.f332e, faVar.f332e)) != 0) {
            return a2;
        }
        return 0;
    }

    public fa a(byte b2) {
        this.f322a = b2;
        a(true);
        return this;
    }

    public fa a(int i2) {
        this.f323a = i2;
        b(true);
        return this;
    }

    public fa a(String str) {
        this.f324a = str;
        return this;
    }

    public void a() {
        if (this.f324a != null) {
            return;
        }
        throw new jg("Required field 'connpt' was not present! Struct: " + toString());
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                jfVar.f();
                if (!m2144a()) {
                    throw new jg("Required field 'chid' was not found in serialized data! Struct: " + toString());
                } else if (!b()) {
                    throw new jg("Required field 'type' was not found in serialized data! Struct: " + toString());
                } else if (c()) {
                    a();
                    return;
                } else {
                    throw new jg("Required field 'value' was not found in serialized data! Struct: " + toString());
                }
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 3) {
                        this.f322a = jfVar.mo2391a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 8) {
                        this.f323a = jfVar.m2378a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 8) {
                        this.f326b = jfVar.m2378a();
                        c(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f324a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f327b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 8) {
                        this.f328c = jfVar.m2378a();
                        d(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f329c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f331d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 8) {
                        this.f330d = jfVar.m2378a();
                        e(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 8) {
                        this.f332e = jfVar.m2378a();
                        f(true);
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
        this.f325a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2144a() {
        return this.f325a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2145a(fa faVar) {
        if (faVar != null && this.f322a == faVar.f322a && this.f323a == faVar.f323a && this.f326b == faVar.f326b) {
            boolean d2 = d();
            boolean d3 = faVar.d();
            if ((d2 || d3) && (!d2 || !d3 || !this.f324a.equals(faVar.f324a))) {
                return false;
            }
            boolean e2 = e();
            boolean e3 = faVar.e();
            if ((e2 || e3) && (!e2 || !e3 || !this.f327b.equals(faVar.f327b))) {
                return false;
            }
            boolean f2 = f();
            boolean f3 = faVar.f();
            if ((f2 || f3) && (!f2 || !f3 || this.f328c != faVar.f328c)) {
                return false;
            }
            boolean g2 = g();
            boolean g3 = faVar.g();
            if ((g2 || g3) && (!g2 || !g3 || !this.f329c.equals(faVar.f329c))) {
                return false;
            }
            boolean h2 = h();
            boolean h3 = faVar.h();
            if ((h2 || h3) && (!h2 || !h3 || !this.f331d.equals(faVar.f331d))) {
                return false;
            }
            boolean i2 = i();
            boolean i3 = faVar.i();
            if ((i2 || i3) && (!i2 || !i3 || this.f330d != faVar.f330d)) {
                return false;
            }
            boolean j2 = j();
            boolean j3 = faVar.j();
            if (!j2 && !j3) {
                return true;
            }
            return j2 && j3 && this.f332e == faVar.f332e;
        }
        return false;
    }

    public fa b(int i2) {
        this.f326b = i2;
        c(true);
        return this;
    }

    public fa b(String str) {
        this.f327b = str;
        return this;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f321a);
        jfVar.a(a);
        jfVar.a(this.f322a);
        jfVar.b();
        jfVar.a(b);
        jfVar.mo2376a(this.f323a);
        jfVar.b();
        jfVar.a(c);
        jfVar.mo2376a(this.f326b);
        jfVar.b();
        if (this.f324a != null) {
            jfVar.a(d);
            jfVar.a(this.f324a);
            jfVar.b();
        }
        if (this.f327b != null && e()) {
            jfVar.a(e);
            jfVar.a(this.f327b);
            jfVar.b();
        }
        if (f()) {
            jfVar.a(f);
            jfVar.mo2376a(this.f328c);
            jfVar.b();
        }
        if (this.f329c != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f329c);
            jfVar.b();
        }
        if (this.f331d != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f331d);
            jfVar.b();
        }
        if (i()) {
            jfVar.a(i);
            jfVar.mo2376a(this.f330d);
            jfVar.b();
        }
        if (j()) {
            jfVar.a(j);
            jfVar.mo2376a(this.f332e);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f325a.set(1, z);
    }

    public boolean b() {
        return this.f325a.get(1);
    }

    public fa c(int i2) {
        this.f328c = i2;
        d(true);
        return this;
    }

    public fa c(String str) {
        this.f329c = str;
        return this;
    }

    public void c(boolean z) {
        this.f325a.set(2, z);
    }

    public boolean c() {
        return this.f325a.get(2);
    }

    public fa d(int i2) {
        this.f330d = i2;
        e(true);
        return this;
    }

    public fa d(String str) {
        this.f331d = str;
        return this;
    }

    public void d(boolean z) {
        this.f325a.set(3, z);
    }

    public boolean d() {
        return this.f324a != null;
    }

    public void e(boolean z) {
        this.f325a.set(4, z);
    }

    public boolean e() {
        return this.f327b != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof fa)) {
            return m2145a((fa) obj);
        }
        return false;
    }

    public void f(boolean z) {
        this.f325a.set(5, z);
    }

    public boolean f() {
        return this.f325a.get(3);
    }

    public boolean g() {
        return this.f329c != null;
    }

    public boolean h() {
        return this.f331d != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f325a.get(4);
    }

    public boolean j() {
        return this.f325a.get(5);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StatsEvent(");
        sb.append("chid:");
        sb.append((int) this.f322a);
        sb.append(", ");
        sb.append("type:");
        sb.append(this.f323a);
        sb.append(", ");
        sb.append("value:");
        sb.append(this.f326b);
        sb.append(", ");
        sb.append("connpt:");
        String str = this.f324a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(str);
        }
        if (e()) {
            sb.append(", ");
            sb.append("host:");
            String str2 = this.f327b;
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append(str2);
            }
        }
        if (f()) {
            sb.append(", ");
            sb.append("subvalue:");
            sb.append(this.f328c);
        }
        if (g()) {
            sb.append(", ");
            sb.append("annotation:");
            String str3 = this.f329c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("user:");
            String str4 = this.f331d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("time:");
            sb.append(this.f330d);
        }
        if (j()) {
            sb.append(", ");
            sb.append("clientIp:");
            sb.append(this.f332e);
        }
        sb.append(")");
        return sb.toString();
    }
}
