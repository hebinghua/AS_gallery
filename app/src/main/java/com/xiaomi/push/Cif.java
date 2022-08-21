package com.xiaomi.push;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.BitSet;

/* renamed from: com.xiaomi.push.if  reason: invalid class name */
/* loaded from: classes3.dex */
public class Cif implements iu<Cif, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public hj f609a;

    /* renamed from: a  reason: collision with other field name */
    public hw f610a;

    /* renamed from: a  reason: collision with other field name */
    public hy f611a;

    /* renamed from: a  reason: collision with other field name */
    public String f612a;

    /* renamed from: a  reason: collision with other field name */
    public ByteBuffer f613a;

    /* renamed from: b  reason: collision with other field name */
    public String f616b;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f608a = new jk("XmPushActionContainer");
    public static final jc a = new jc("", (byte) 8, 1);
    public static final jc b = new jc("", (byte) 2, 2);
    public static final jc c = new jc("", (byte) 2, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 11, 6);
    public static final jc g = new jc("", (byte) 12, 7);
    public static final jc h = new jc("", (byte) 12, 8);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f614a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public boolean f615a = true;

    /* renamed from: b  reason: collision with other field name */
    public boolean f617b = true;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(Cif cif) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        if (!getClass().equals(cif.getClass())) {
            return getClass().getName().compareTo(cif.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2297a()).compareTo(Boolean.valueOf(cif.m2297a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2297a() && (a9 = iv.a(this.f609a, cif.f609a)) != 0) {
            return a9;
        }
        int compareTo2 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(cif.c()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (c() && (a8 = iv.a(this.f615a, cif.f615a)) != 0) {
            return a8;
        }
        int compareTo3 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(cif.d()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (d() && (a7 = iv.a(this.f617b, cif.f617b)) != 0) {
            return a7;
        }
        int compareTo4 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(cif.e()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (e() && (a6 = iv.a(this.f613a, cif.f613a)) != 0) {
            return a6;
        }
        int compareTo5 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(cif.f()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (f() && (a5 = iv.a(this.f612a, cif.f612a)) != 0) {
            return a5;
        }
        int compareTo6 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(cif.g()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (g() && (a4 = iv.a(this.f616b, cif.f616b)) != 0) {
            return a4;
        }
        int compareTo7 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(cif.h()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (h() && (a3 = iv.a(this.f611a, cif.f611a)) != 0) {
            return a3;
        }
        int compareTo8 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(cif.i()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (i() && (a2 = iv.a(this.f610a, cif.f610a)) != 0) {
            return a2;
        }
        return 0;
    }

    public hj a() {
        return this.f609a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hw m2293a() {
        return this.f610a;
    }

    public Cif a(hj hjVar) {
        this.f609a = hjVar;
        return this;
    }

    public Cif a(hw hwVar) {
        this.f610a = hwVar;
        return this;
    }

    public Cif a(hy hyVar) {
        this.f611a = hyVar;
        return this;
    }

    public Cif a(String str) {
        this.f612a = str;
        return this;
    }

    public Cif a(ByteBuffer byteBuffer) {
        this.f613a = byteBuffer;
        return this;
    }

    public Cif a(boolean z) {
        this.f615a = z;
        m2296a(true);
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2294a() {
        return this.f612a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2295a() {
        if (this.f609a == null) {
            throw new jg("Required field 'action' was not present! Struct: " + toString());
        } else if (this.f613a == null) {
            throw new jg("Required field 'pushAction' was not present! Struct: " + toString());
        } else if (this.f611a != null) {
        } else {
            throw new jg("Required field 'target' was not present! Struct: " + toString());
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
                if (!c()) {
                    throw new jg("Required field 'encryptAction' was not found in serialized data! Struct: " + toString());
                } else if (d()) {
                    m2295a();
                    return;
                } else {
                    throw new jg("Required field 'isRequest' was not found in serialized data! Struct: " + toString());
                }
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 8) {
                        this.f609a = hj.a(jfVar.m2378a());
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 2) {
                        this.f615a = jfVar.m2389a();
                        m2296a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 2) {
                        this.f617b = jfVar.m2389a();
                        m2300b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f613a = jfVar.m2386a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f612a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 11) {
                        this.f616b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f611a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 12) {
                        hw hwVar = new hw();
                        this.f610a = hwVar;
                        hwVar.a(jfVar);
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
    public void m2296a(boolean z) {
        this.f614a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2297a() {
        return this.f609a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2298a(Cif cif) {
        if (cif == null) {
            return false;
        }
        boolean m2297a = m2297a();
        boolean m2297a2 = cif.m2297a();
        if (((m2297a || m2297a2) && (!m2297a || !m2297a2 || !this.f609a.equals(cif.f609a))) || this.f615a != cif.f615a || this.f617b != cif.f617b) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = cif.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f613a.equals(cif.f613a))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = cif.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f612a.equals(cif.f612a))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = cif.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f616b.equals(cif.f616b))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = cif.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f611a.m2271a(cif.f611a))) {
            return false;
        }
        boolean i = i();
        boolean i2 = cif.i();
        if (!i && !i2) {
            return true;
        }
        return i && i2 && this.f610a.m2263a(cif.f610a);
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m2299a() {
        a(iv.a(this.f613a));
        return this.f613a.array();
    }

    public Cif b(String str) {
        this.f616b = str;
        return this;
    }

    public Cif b(boolean z) {
        this.f617b = z;
        m2300b(true);
        return this;
    }

    public String b() {
        return this.f616b;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2295a();
        jfVar.a(f608a);
        if (this.f609a != null) {
            jfVar.a(a);
            jfVar.mo2376a(this.f609a.a());
            jfVar.b();
        }
        jfVar.a(b);
        jfVar.a(this.f615a);
        jfVar.b();
        jfVar.a(c);
        jfVar.a(this.f617b);
        jfVar.b();
        if (this.f613a != null) {
            jfVar.a(d);
            jfVar.a(this.f613a);
            jfVar.b();
        }
        if (this.f612a != null && f()) {
            jfVar.a(e);
            jfVar.a(this.f612a);
            jfVar.b();
        }
        if (this.f616b != null && g()) {
            jfVar.a(f);
            jfVar.a(this.f616b);
            jfVar.b();
        }
        if (this.f611a != null) {
            jfVar.a(g);
            this.f611a.b(jfVar);
            jfVar.b();
        }
        if (this.f610a != null && i()) {
            jfVar.a(h);
            this.f610a.b(jfVar);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2300b(boolean z) {
        this.f614a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2301b() {
        return this.f615a;
    }

    public boolean c() {
        return this.f614a.get(0);
    }

    public boolean d() {
        return this.f614a.get(1);
    }

    public boolean e() {
        return this.f613a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Cif)) {
            return m2298a((Cif) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f612a != null;
    }

    public boolean g() {
        return this.f616b != null;
    }

    public boolean h() {
        return this.f611a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f610a != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionContainer(");
        sb.append("action:");
        hj hjVar = this.f609a;
        if (hjVar == null) {
            sb.append("null");
        } else {
            sb.append(hjVar);
        }
        sb.append(", ");
        sb.append("encryptAction:");
        sb.append(this.f615a);
        sb.append(", ");
        sb.append("isRequest:");
        sb.append(this.f617b);
        if (f()) {
            sb.append(", ");
            sb.append("appid:");
            String str = this.f612a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("packageName:");
            String str2 = this.f616b;
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append(str2);
            }
        }
        sb.append(", ");
        sb.append("target:");
        hy hyVar = this.f611a;
        if (hyVar == null) {
            sb.append("null");
        } else {
            sb.append(hyVar);
        }
        if (i()) {
            sb.append(", ");
            sb.append("metaInfo:");
            hw hwVar = this.f610a;
            if (hwVar == null) {
                sb.append("null");
            } else {
                sb.append(hwVar);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
