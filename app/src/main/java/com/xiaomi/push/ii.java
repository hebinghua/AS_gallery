package com.xiaomi.push;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class ii implements iu<ii, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f623a;

    /* renamed from: a  reason: collision with other field name */
    public hy f624a;

    /* renamed from: a  reason: collision with other field name */
    public String f625a;

    /* renamed from: a  reason: collision with other field name */
    public ByteBuffer f626a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f627a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f628a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f629a;

    /* renamed from: b  reason: collision with other field name */
    public String f630b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f631b;

    /* renamed from: c  reason: collision with other field name */
    public String f632c;

    /* renamed from: d  reason: collision with other field name */
    public String f633d;

    /* renamed from: e  reason: collision with other field name */
    public String f634e;

    /* renamed from: f  reason: collision with other field name */
    public String f635f;

    /* renamed from: g  reason: collision with other field name */
    public String f636g;

    /* renamed from: h  reason: collision with other field name */
    public String f637h;

    /* renamed from: i  reason: collision with other field name */
    public String f638i;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f622a = new jk("XmPushActionNotification");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 2, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 13, 8);
    public static final jc i = new jc("", (byte) 11, 9);
    public static final jc j = new jc("", (byte) 11, 10);
    public static final jc k = new jc("", (byte) 11, 12);
    public static final jc l = new jc("", (byte) 11, 13);
    public static final jc m = new jc("", (byte) 11, 14);
    public static final jc n = new jc("", (byte) 10, 15);
    public static final jc o = new jc("", (byte) 2, 20);

    public ii() {
        this.f627a = new BitSet(3);
        this.f629a = true;
        this.f631b = false;
    }

    public ii(String str, boolean z) {
        this();
        this.f630b = str;
        this.f629a = z;
        m2311a(true);
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ii iiVar) {
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
        int a14;
        int a15;
        int a16;
        if (!getClass().equals(iiVar.getClass())) {
            return getClass().getName().compareTo(iiVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2312a()).compareTo(Boolean.valueOf(iiVar.m2312a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2312a() && (a16 = iv.a(this.f625a, iiVar.f625a)) != 0) {
            return a16;
        }
        int compareTo2 = Boolean.valueOf(m2315b()).compareTo(Boolean.valueOf(iiVar.m2315b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2315b() && (a15 = iv.a(this.f624a, iiVar.f624a)) != 0) {
            return a15;
        }
        int compareTo3 = Boolean.valueOf(m2316c()).compareTo(Boolean.valueOf(iiVar.m2316c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2316c() && (a14 = iv.a(this.f630b, iiVar.f630b)) != 0) {
            return a14;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(iiVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a13 = iv.a(this.f632c, iiVar.f632c)) != 0) {
            return a13;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(iiVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a12 = iv.a(this.f633d, iiVar.f633d)) != 0) {
            return a12;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(iiVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a11 = iv.a(this.f629a, iiVar.f629a)) != 0) {
            return a11;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(iiVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a10 = iv.a(this.f634e, iiVar.f634e)) != 0) {
            return a10;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(iiVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a9 = iv.a(this.f628a, iiVar.f628a)) != 0) {
            return a9;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(iiVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a8 = iv.a(this.f635f, iiVar.f635f)) != 0) {
            return a8;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(iiVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a7 = iv.a(this.f636g, iiVar.f636g)) != 0) {
            return a7;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(iiVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a6 = iv.a(this.f637h, iiVar.f637h)) != 0) {
            return a6;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(iiVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a5 = iv.a(this.f638i, iiVar.f638i)) != 0) {
            return a5;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(iiVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a4 = iv.a(this.f626a, iiVar.f626a)) != 0) {
            return a4;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(iiVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a3 = iv.a(this.f623a, iiVar.f623a)) != 0) {
            return a3;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(iiVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a2 = iv.a(this.f631b, iiVar.f631b)) != 0) {
            return a2;
        }
        return 0;
    }

    public hy a() {
        return this.f624a;
    }

    public ii a(String str) {
        this.f630b = str;
        return this;
    }

    public ii a(ByteBuffer byteBuffer) {
        this.f626a = byteBuffer;
        return this;
    }

    public ii a(Map<String, String> map) {
        this.f628a = map;
        return this;
    }

    public ii a(boolean z) {
        this.f629a = z;
        m2311a(true);
        return this;
    }

    public ii a(byte[] bArr) {
        a(ByteBuffer.wrap(bArr));
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2308a() {
        return this.f630b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, String> m2309a() {
        return this.f628a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2310a() {
        if (this.f630b != null) {
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
                if (f()) {
                    m2310a();
                    return;
                }
                throw new jg("Required field 'requireAck' was not found in serialized data! Struct: " + toString());
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f625a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f624a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f630b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f632c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f633d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 2) {
                        this.f629a = jfVar.m2389a();
                        m2311a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f634e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 13) {
                        je m2382a = jfVar.m2382a();
                        this.f628a = new HashMap(m2382a.f795a * 2);
                        for (int i2 = 0; i2 < m2382a.f795a; i2++) {
                            this.f628a.put(jfVar.m2385a(), jfVar.m2385a());
                        }
                        jfVar.h();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f635f = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 11) {
                        this.f636g = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 11) {
                        this.f637h = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 13:
                    if (b2 == 11) {
                        this.f638i = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 14:
                    if (b2 == 11) {
                        this.f626a = jfVar.m2386a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 15:
                    if (b2 == 10) {
                        this.f623a = jfVar.m2379a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 20:
                    if (b2 == 2) {
                        this.f631b = jfVar.m2389a();
                        c(true);
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
        if (this.f628a == null) {
            this.f628a = new HashMap();
        }
        this.f628a.put(str, str2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2311a(boolean z) {
        this.f627a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2312a() {
        return this.f625a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2313a(ii iiVar) {
        if (iiVar == null) {
            return false;
        }
        boolean m2312a = m2312a();
        boolean m2312a2 = iiVar.m2312a();
        if ((m2312a || m2312a2) && (!m2312a || !m2312a2 || !this.f625a.equals(iiVar.f625a))) {
            return false;
        }
        boolean m2315b = m2315b();
        boolean m2315b2 = iiVar.m2315b();
        if ((m2315b || m2315b2) && (!m2315b || !m2315b2 || !this.f624a.m2271a(iiVar.f624a))) {
            return false;
        }
        boolean m2316c = m2316c();
        boolean m2316c2 = iiVar.m2316c();
        if ((m2316c || m2316c2) && (!m2316c || !m2316c2 || !this.f630b.equals(iiVar.f630b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = iiVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f632c.equals(iiVar.f632c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = iiVar.e();
        if (((e2 || e3) && (!e2 || !e3 || !this.f633d.equals(iiVar.f633d))) || this.f629a != iiVar.f629a) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = iiVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f634e.equals(iiVar.f634e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = iiVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f628a.equals(iiVar.f628a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = iiVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f635f.equals(iiVar.f635f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = iiVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f636g.equals(iiVar.f636g))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = iiVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f637h.equals(iiVar.f637h))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = iiVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f638i.equals(iiVar.f638i))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = iiVar.m();
        if ((m2 || m3) && (!m2 || !m3 || !this.f626a.equals(iiVar.f626a))) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = iiVar.n();
        if ((n2 || n3) && (!n2 || !n3 || this.f623a != iiVar.f623a)) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = iiVar.o();
        if (!o2 && !o3) {
            return true;
        }
        return o2 && o3 && this.f631b == iiVar.f631b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m2314a() {
        a(iv.a(this.f626a));
        return this.f626a.array();
    }

    public ii b(String str) {
        this.f632c = str;
        return this;
    }

    public String b() {
        return this.f632c;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2310a();
        jfVar.a(f622a);
        if (this.f625a != null && m2312a()) {
            jfVar.a(a);
            jfVar.a(this.f625a);
            jfVar.b();
        }
        if (this.f624a != null && m2315b()) {
            jfVar.a(b);
            this.f624a.b(jfVar);
            jfVar.b();
        }
        if (this.f630b != null) {
            jfVar.a(c);
            jfVar.a(this.f630b);
            jfVar.b();
        }
        if (this.f632c != null && d()) {
            jfVar.a(d);
            jfVar.a(this.f632c);
            jfVar.b();
        }
        if (this.f633d != null && e()) {
            jfVar.a(e);
            jfVar.a(this.f633d);
            jfVar.b();
        }
        jfVar.a(f);
        jfVar.a(this.f629a);
        jfVar.b();
        if (this.f634e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f634e);
            jfVar.b();
        }
        if (this.f628a != null && h()) {
            jfVar.a(h);
            jfVar.a(new je((byte) 11, (byte) 11, this.f628a.size()));
            for (Map.Entry<String, String> entry : this.f628a.entrySet()) {
                jfVar.a(entry.getKey());
                jfVar.a(entry.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        if (this.f635f != null && i()) {
            jfVar.a(i);
            jfVar.a(this.f635f);
            jfVar.b();
        }
        if (this.f636g != null && j()) {
            jfVar.a(j);
            jfVar.a(this.f636g);
            jfVar.b();
        }
        if (this.f637h != null && k()) {
            jfVar.a(k);
            jfVar.a(this.f637h);
            jfVar.b();
        }
        if (this.f638i != null && l()) {
            jfVar.a(l);
            jfVar.a(this.f638i);
            jfVar.b();
        }
        if (this.f626a != null && m()) {
            jfVar.a(m);
            jfVar.a(this.f626a);
            jfVar.b();
        }
        if (n()) {
            jfVar.a(n);
            jfVar.a(this.f623a);
            jfVar.b();
        }
        if (o()) {
            jfVar.a(o);
            jfVar.a(this.f631b);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f627a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2315b() {
        return this.f624a != null;
    }

    public ii c(String str) {
        this.f633d = str;
        return this;
    }

    public String c() {
        return this.f635f;
    }

    public void c(boolean z) {
        this.f627a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2316c() {
        return this.f630b != null;
    }

    public ii d(String str) {
        this.f635f = str;
        return this;
    }

    public boolean d() {
        return this.f632c != null;
    }

    public boolean e() {
        return this.f633d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ii)) {
            return m2313a((ii) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f627a.get(0);
    }

    public boolean g() {
        return this.f634e != null;
    }

    public boolean h() {
        return this.f628a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f635f != null;
    }

    public boolean j() {
        return this.f636g != null;
    }

    public boolean k() {
        return this.f637h != null;
    }

    public boolean l() {
        return this.f638i != null;
    }

    public boolean m() {
        return this.f626a != null;
    }

    public boolean n() {
        return this.f627a.get(1);
    }

    public boolean o() {
        return this.f627a.get(2);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionNotification(");
        boolean z2 = false;
        if (m2312a()) {
            sb.append("debug:");
            String str = this.f625a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (m2315b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f624a;
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
        String str2 = this.f630b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        if (d()) {
            sb.append(", ");
            sb.append("appId:");
            String str3 = this.f632c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
        }
        if (e()) {
            sb.append(", ");
            sb.append("type:");
            String str4 = this.f633d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        sb.append(", ");
        sb.append("requireAck:");
        sb.append(this.f629a);
        if (g()) {
            sb.append(", ");
            sb.append("payload:");
            String str5 = this.f634e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("extra:");
            Map<String, String> map = this.f628a;
            if (map == null) {
                sb.append("null");
            } else {
                sb.append(map);
            }
        }
        if (i()) {
            sb.append(", ");
            sb.append("packageName:");
            String str6 = this.f635f;
            if (str6 == null) {
                sb.append("null");
            } else {
                sb.append(str6);
            }
        }
        if (j()) {
            sb.append(", ");
            sb.append("category:");
            String str7 = this.f636g;
            if (str7 == null) {
                sb.append("null");
            } else {
                sb.append(str7);
            }
        }
        if (k()) {
            sb.append(", ");
            sb.append("regId:");
            String str8 = this.f637h;
            if (str8 == null) {
                sb.append("null");
            } else {
                sb.append(str8);
            }
        }
        if (l()) {
            sb.append(", ");
            sb.append("aliasName:");
            String str9 = this.f638i;
            if (str9 == null) {
                sb.append("null");
            } else {
                sb.append(str9);
            }
        }
        if (m()) {
            sb.append(", ");
            sb.append("binaryExtra:");
            ByteBuffer byteBuffer = this.f626a;
            if (byteBuffer == null) {
                sb.append("null");
            } else {
                iv.a(byteBuffer, sb);
            }
        }
        if (n()) {
            sb.append(", ");
            sb.append("createdTs:");
            sb.append(this.f623a);
        }
        if (o()) {
            sb.append(", ");
            sb.append("alreadyLogClickInXmq:");
            sb.append(this.f631b);
        }
        sb.append(")");
        return sb.toString();
    }
}
