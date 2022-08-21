package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class hu implements iu<hu, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public int f491a;

    /* renamed from: a  reason: collision with other field name */
    public long f492a;

    /* renamed from: a  reason: collision with other field name */
    public String f493a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f494a = new BitSet(6);

    /* renamed from: a  reason: collision with other field name */
    public boolean f495a;

    /* renamed from: b  reason: collision with other field name */
    public int f496b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f497b;

    /* renamed from: c  reason: collision with other field name */
    public int f498c;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f490a = new jk("OnlineConfigItem");
    public static final jc a = new jc("", (byte) 8, 1);
    public static final jc b = new jc("", (byte) 8, 2);
    public static final jc c = new jc("", (byte) 2, 3);
    public static final jc d = new jc("", (byte) 8, 4);
    public static final jc e = new jc("", (byte) 10, 5);
    public static final jc f = new jc("", (byte) 11, 6);
    public static final jc g = new jc("", (byte) 2, 7);

    public int a() {
        return this.f491a;
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hu huVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        if (!getClass().equals(huVar.getClass())) {
            return getClass().getName().compareTo(huVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2247a()).compareTo(Boolean.valueOf(huVar.m2247a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2247a() && (a8 = iv.a(this.f491a, huVar.f491a)) != 0) {
            return a8;
        }
        int compareTo2 = Boolean.valueOf(m2249b()).compareTo(Boolean.valueOf(huVar.m2249b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2249b() && (a7 = iv.a(this.f496b, huVar.f496b)) != 0) {
            return a7;
        }
        int compareTo3 = Boolean.valueOf(m2250c()).compareTo(Boolean.valueOf(huVar.m2250c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2250c() && (a6 = iv.a(this.f495a, huVar.f495a)) != 0) {
            return a6;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(huVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a5 = iv.a(this.f498c, huVar.f498c)) != 0) {
            return a5;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(huVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a4 = iv.a(this.f492a, huVar.f492a)) != 0) {
            return a4;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(huVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a3 = iv.a(this.f493a, huVar.f493a)) != 0) {
            return a3;
        }
        int compareTo7 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(huVar.h()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (h() && (a2 = iv.a(this.f497b, huVar.f497b)) != 0) {
            return a2;
        }
        return 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m2244a() {
        return this.f492a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2245a() {
        return this.f493a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2246a() {
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                jfVar.f();
                m2246a();
                return;
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 8) {
                        this.f491a = jfVar.m2378a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 8) {
                        this.f496b = jfVar.m2378a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 2) {
                        this.f495a = jfVar.m2389a();
                        c(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 8) {
                        this.f498c = jfVar.m2378a();
                        d(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 10) {
                        this.f492a = jfVar.m2379a();
                        e(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f493a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 2) {
                        this.f497b = jfVar.m2389a();
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
        this.f494a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2247a() {
        return this.f494a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2248a(hu huVar) {
        if (huVar == null) {
            return false;
        }
        boolean m2247a = m2247a();
        boolean m2247a2 = huVar.m2247a();
        if ((m2247a || m2247a2) && (!m2247a || !m2247a2 || this.f491a != huVar.f491a)) {
            return false;
        }
        boolean m2249b = m2249b();
        boolean m2249b2 = huVar.m2249b();
        if ((m2249b || m2249b2) && (!m2249b || !m2249b2 || this.f496b != huVar.f496b)) {
            return false;
        }
        boolean m2250c = m2250c();
        boolean m2250c2 = huVar.m2250c();
        if ((m2250c || m2250c2) && (!m2250c || !m2250c2 || this.f495a != huVar.f495a)) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = huVar.d();
        if ((d2 || d3) && (!d2 || !d3 || this.f498c != huVar.f498c)) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = huVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f492a != huVar.f492a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = huVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f493a.equals(huVar.f493a))) {
            return false;
        }
        boolean h = h();
        boolean h2 = huVar.h();
        if (!h && !h2) {
            return true;
        }
        return h && h2 && this.f497b == huVar.f497b;
    }

    public int b() {
        return this.f496b;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2246a();
        jfVar.a(f490a);
        if (m2247a()) {
            jfVar.a(a);
            jfVar.mo2376a(this.f491a);
            jfVar.b();
        }
        if (m2249b()) {
            jfVar.a(b);
            jfVar.mo2376a(this.f496b);
            jfVar.b();
        }
        if (m2250c()) {
            jfVar.a(c);
            jfVar.a(this.f495a);
            jfVar.b();
        }
        if (d()) {
            jfVar.a(d);
            jfVar.mo2376a(this.f498c);
            jfVar.b();
        }
        if (e()) {
            jfVar.a(e);
            jfVar.a(this.f492a);
            jfVar.b();
        }
        if (this.f493a != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f493a);
            jfVar.b();
        }
        if (h()) {
            jfVar.a(g);
            jfVar.a(this.f497b);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f494a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2249b() {
        return this.f494a.get(1);
    }

    public int c() {
        return this.f498c;
    }

    public void c(boolean z) {
        this.f494a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2250c() {
        return this.f494a.get(2);
    }

    public void d(boolean z) {
        this.f494a.set(3, z);
    }

    public boolean d() {
        return this.f494a.get(3);
    }

    public void e(boolean z) {
        this.f494a.set(4, z);
    }

    public boolean e() {
        return this.f494a.get(4);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hu)) {
            return m2248a((hu) obj);
        }
        return false;
    }

    public void f(boolean z) {
        this.f494a.set(5, z);
    }

    public boolean f() {
        return this.f493a != null;
    }

    public boolean g() {
        return this.f497b;
    }

    public boolean h() {
        return this.f494a.get(5);
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("OnlineConfigItem(");
        boolean z2 = false;
        if (m2247a()) {
            sb.append("key:");
            sb.append(this.f491a);
            z = false;
        } else {
            z = true;
        }
        if (m2249b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("type:");
            sb.append(this.f496b);
            z = false;
        }
        if (m2250c()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("clear:");
            sb.append(this.f495a);
            z = false;
        }
        if (d()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("intValue:");
            sb.append(this.f498c);
            z = false;
        }
        if (e()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("longValue:");
            sb.append(this.f492a);
            z = false;
        }
        if (f()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("stringValue:");
            String str = this.f493a;
            if (str == null) {
                str = "null";
            }
            sb.append(str);
        } else {
            z2 = z;
        }
        if (h()) {
            if (!z2) {
                sb.append(", ");
            }
            sb.append("boolValue:");
            sb.append(this.f497b);
        }
        sb.append(")");
        return sb.toString();
    }
}
