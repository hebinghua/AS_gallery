package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class hn implements iu<hn, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f460a;

    /* renamed from: a  reason: collision with other field name */
    public String f461a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f462a = new BitSet(3);

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f463a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f464a;

    /* renamed from: b  reason: collision with other field name */
    public long f465b;

    /* renamed from: b  reason: collision with other field name */
    public String f466b;

    /* renamed from: c  reason: collision with other field name */
    public String f467c;

    /* renamed from: d  reason: collision with other field name */
    public String f468d;

    /* renamed from: e  reason: collision with other field name */
    public String f469e;

    /* renamed from: f  reason: collision with other field name */
    public String f470f;

    /* renamed from: g  reason: collision with other field name */
    public String f471g;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f459a = new jk("ClientUploadDataItem");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 11, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 10, 4);
    public static final jc e = new jc("", (byte) 10, 5);
    public static final jc f = new jc("", (byte) 2, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 11, 8);
    public static final jc i = new jc("", (byte) 11, 9);
    public static final jc j = new jc("", (byte) 13, 10);
    public static final jc k = new jc("", (byte) 11, 11);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hn hnVar) {
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
        if (!getClass().equals(hnVar.getClass())) {
            return getClass().getName().compareTo(hnVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2231a()).compareTo(Boolean.valueOf(hnVar.m2231a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2231a() && (a12 = iv.a(this.f461a, hnVar.f461a)) != 0) {
            return a12;
        }
        int compareTo2 = Boolean.valueOf(m2233b()).compareTo(Boolean.valueOf(hnVar.m2233b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2233b() && (a11 = iv.a(this.f466b, hnVar.f466b)) != 0) {
            return a11;
        }
        int compareTo3 = Boolean.valueOf(m2234c()).compareTo(Boolean.valueOf(hnVar.m2234c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2234c() && (a10 = iv.a(this.f467c, hnVar.f467c)) != 0) {
            return a10;
        }
        int compareTo4 = Boolean.valueOf(m2235d()).compareTo(Boolean.valueOf(hnVar.m2235d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (m2235d() && (a9 = iv.a(this.f460a, hnVar.f460a)) != 0) {
            return a9;
        }
        int compareTo5 = Boolean.valueOf(m2236e()).compareTo(Boolean.valueOf(hnVar.m2236e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (m2236e() && (a8 = iv.a(this.f465b, hnVar.f465b)) != 0) {
            return a8;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hnVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a7 = iv.a(this.f464a, hnVar.f464a)) != 0) {
            return a7;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hnVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a6 = iv.a(this.f468d, hnVar.f468d)) != 0) {
            return a6;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hnVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a5 = iv.a(this.f469e, hnVar.f469e)) != 0) {
            return a5;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hnVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a4 = iv.a(this.f470f, hnVar.f470f)) != 0) {
            return a4;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hnVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a3 = iv.a(this.f463a, hnVar.f463a)) != 0) {
            return a3;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hnVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a2 = iv.a(this.f471g, hnVar.f471g)) != 0) {
            return a2;
        }
        return 0;
    }

    public long a() {
        return this.f465b;
    }

    public hn a(long j2) {
        this.f460a = j2;
        m2230a(true);
        return this;
    }

    public hn a(String str) {
        this.f461a = str;
        return this;
    }

    public hn a(Map<String, String> map) {
        this.f463a = map;
        return this;
    }

    public hn a(boolean z) {
        this.f464a = z;
        c(true);
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2227a() {
        return this.f461a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, String> m2228a() {
        return this.f463a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2229a() {
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                jfVar.f();
                m2229a();
                return;
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f461a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 11) {
                        this.f466b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f467c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 10) {
                        this.f460a = jfVar.m2379a();
                        m2230a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 10) {
                        this.f465b = jfVar.m2379a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 2) {
                        this.f464a = jfVar.m2389a();
                        c(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f468d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f469e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f470f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 13) {
                        je m2382a = jfVar.m2382a();
                        this.f463a = new HashMap(m2382a.f795a * 2);
                        for (int i2 = 0; i2 < m2382a.f795a; i2++) {
                            this.f463a.put(jfVar.m2385a(), jfVar.m2385a());
                        }
                        jfVar.h();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 11) {
                        this.f471g = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
            }
            ji.a(jfVar, b2);
            jfVar.g();
        }
    }

    public void a(String str, String str2) {
        if (this.f463a == null) {
            this.f463a = new HashMap();
        }
        this.f463a.put(str, str2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2230a(boolean z) {
        this.f462a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2231a() {
        return this.f461a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2232a(hn hnVar) {
        if (hnVar == null) {
            return false;
        }
        boolean m2231a = m2231a();
        boolean m2231a2 = hnVar.m2231a();
        if ((m2231a || m2231a2) && (!m2231a || !m2231a2 || !this.f461a.equals(hnVar.f461a))) {
            return false;
        }
        boolean m2233b = m2233b();
        boolean m2233b2 = hnVar.m2233b();
        if ((m2233b || m2233b2) && (!m2233b || !m2233b2 || !this.f466b.equals(hnVar.f466b))) {
            return false;
        }
        boolean m2234c = m2234c();
        boolean m2234c2 = hnVar.m2234c();
        if ((m2234c || m2234c2) && (!m2234c || !m2234c2 || !this.f467c.equals(hnVar.f467c))) {
            return false;
        }
        boolean m2235d = m2235d();
        boolean m2235d2 = hnVar.m2235d();
        if ((m2235d || m2235d2) && (!m2235d || !m2235d2 || this.f460a != hnVar.f460a)) {
            return false;
        }
        boolean m2236e = m2236e();
        boolean m2236e2 = hnVar.m2236e();
        if ((m2236e || m2236e2) && (!m2236e || !m2236e2 || this.f465b != hnVar.f465b)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hnVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f464a != hnVar.f464a)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hnVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f468d.equals(hnVar.f468d))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hnVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f469e.equals(hnVar.f469e))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hnVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f470f.equals(hnVar.f470f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hnVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f463a.equals(hnVar.f463a))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = hnVar.k();
        if (!k2 && !k3) {
            return true;
        }
        return k2 && k3 && this.f471g.equals(hnVar.f471g);
    }

    public hn b(long j2) {
        this.f465b = j2;
        b(true);
        return this;
    }

    public hn b(String str) {
        this.f466b = str;
        return this;
    }

    public String b() {
        return this.f467c;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2229a();
        jfVar.a(f459a);
        if (this.f461a != null && m2231a()) {
            jfVar.a(a);
            jfVar.a(this.f461a);
            jfVar.b();
        }
        if (this.f466b != null && m2233b()) {
            jfVar.a(b);
            jfVar.a(this.f466b);
            jfVar.b();
        }
        if (this.f467c != null && m2234c()) {
            jfVar.a(c);
            jfVar.a(this.f467c);
            jfVar.b();
        }
        if (m2235d()) {
            jfVar.a(d);
            jfVar.a(this.f460a);
            jfVar.b();
        }
        if (m2236e()) {
            jfVar.a(e);
            jfVar.a(this.f465b);
            jfVar.b();
        }
        if (f()) {
            jfVar.a(f);
            jfVar.a(this.f464a);
            jfVar.b();
        }
        if (this.f468d != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f468d);
            jfVar.b();
        }
        if (this.f469e != null && h()) {
            jfVar.a(h);
            jfVar.a(this.f469e);
            jfVar.b();
        }
        if (this.f470f != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f470f);
            jfVar.b();
        }
        if (this.f463a != null && j()) {
            jfVar.a(j);
            jfVar.a(new je((byte) 11, (byte) 11, this.f463a.size()));
            for (Map.Entry<String, String> entry : this.f463a.entrySet()) {
                jfVar.a(entry.getKey());
                jfVar.a(entry.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        if (this.f471g != null && k()) {
            jfVar.a(k);
            jfVar.a(this.f471g);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f462a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2233b() {
        return this.f466b != null;
    }

    public hn c(String str) {
        this.f467c = str;
        return this;
    }

    public String c() {
        return this.f469e;
    }

    public void c(boolean z) {
        this.f462a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2234c() {
        return this.f467c != null;
    }

    public hn d(String str) {
        this.f468d = str;
        return this;
    }

    public String d() {
        return this.f470f;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m2235d() {
        return this.f462a.get(0);
    }

    public hn e(String str) {
        this.f469e = str;
        return this;
    }

    public String e() {
        return this.f471g;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m2236e() {
        return this.f462a.get(1);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hn)) {
            return m2232a((hn) obj);
        }
        return false;
    }

    public hn f(String str) {
        this.f470f = str;
        return this;
    }

    public boolean f() {
        return this.f462a.get(2);
    }

    public hn g(String str) {
        this.f471g = str;
        return this;
    }

    public boolean g() {
        return this.f468d != null;
    }

    public boolean h() {
        return this.f469e != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f470f != null;
    }

    public boolean j() {
        return this.f463a != null;
    }

    public boolean k() {
        return this.f471g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("ClientUploadDataItem(");
        boolean z2 = false;
        if (m2231a()) {
            sb.append("channel:");
            String str = this.f461a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (m2233b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("data:");
            String str2 = this.f466b;
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append(str2);
            }
            z = false;
        }
        if (m2234c()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("name:");
            String str3 = this.f467c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
            z = false;
        }
        if (m2235d()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("counter:");
            sb.append(this.f460a);
            z = false;
        }
        if (m2236e()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("timestamp:");
            sb.append(this.f465b);
            z = false;
        }
        if (f()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("fromSdk:");
            sb.append(this.f464a);
            z = false;
        }
        if (g()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("category:");
            String str4 = this.f468d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
            z = false;
        }
        if (h()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("sourcePackage:");
            String str5 = this.f469e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
            z = false;
        }
        if (i()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("id:");
            String str6 = this.f470f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
            z = false;
        }
        if (j()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("extra:");
            Map<String, String> map = this.f463a;
            if (map == null) {
                sb.append("null");
            } else {
                sb.append(map);
            }
        } else {
            z2 = z;
        }
        if (k()) {
            if (!z2) {
                sb.append(", ");
            }
            sb.append("pkgName:");
            String str7 = this.f471g;
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
