package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class hy implements iu<hy, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public String f538a;

    /* renamed from: d  reason: collision with other field name */
    public String f543d;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f536a = new jk("Target");
    public static final jc a = new jc("", (byte) 10, 1);
    public static final jc b = new jc("", (byte) 11, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 2, 5);
    public static final jc f = new jc("", (byte) 11, 7);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f539a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public long f537a = 5;

    /* renamed from: b  reason: collision with other field name */
    public String f541b = "xiaomi.com";

    /* renamed from: c  reason: collision with other field name */
    public String f542c = "";

    /* renamed from: a  reason: collision with other field name */
    public boolean f540a = false;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hy hyVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        if (!getClass().equals(hyVar.getClass())) {
            return getClass().getName().compareTo(hyVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2270a()).compareTo(Boolean.valueOf(hyVar.m2270a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2270a() && (a7 = iv.a(this.f537a, hyVar.f537a)) != 0) {
            return a7;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hyVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a6 = iv.a(this.f538a, hyVar.f538a)) != 0) {
            return a6;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hyVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a5 = iv.a(this.f541b, hyVar.f541b)) != 0) {
            return a5;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hyVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a4 = iv.a(this.f542c, hyVar.f542c)) != 0) {
            return a4;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hyVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a3 = iv.a(this.f540a, hyVar.f540a)) != 0) {
            return a3;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hyVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a2 = iv.a(this.f543d, hyVar.f543d)) != 0) {
            return a2;
        }
        return 0;
    }

    public void a() {
        if (this.f538a != null) {
            return;
        }
        throw new jg("Required field 'userId' was not present! Struct: " + toString());
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                break;
            }
            short s = m2380a.f793a;
            if (s == 1) {
                if (b2 == 10) {
                    this.f537a = jfVar.m2379a();
                    a(true);
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s == 2) {
                if (b2 == 11) {
                    this.f538a = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s == 3) {
                if (b2 == 11) {
                    this.f541b = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s == 4) {
                if (b2 == 11) {
                    this.f542c = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s != 5) {
                if (s == 7 && b2 == 11) {
                    this.f543d = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else {
                if (b2 == 2) {
                    this.f540a = jfVar.m2389a();
                    b(true);
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            }
        }
        jfVar.f();
        if (m2270a()) {
            a();
            return;
        }
        throw new jg("Required field 'channelId' was not found in serialized data! Struct: " + toString());
    }

    public void a(boolean z) {
        this.f539a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2270a() {
        return this.f539a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2271a(hy hyVar) {
        if (hyVar != null && this.f537a == hyVar.f537a) {
            boolean b2 = b();
            boolean b3 = hyVar.b();
            if ((b2 || b3) && (!b2 || !b3 || !this.f538a.equals(hyVar.f538a))) {
                return false;
            }
            boolean c2 = c();
            boolean c3 = hyVar.c();
            if ((c2 || c3) && (!c2 || !c3 || !this.f541b.equals(hyVar.f541b))) {
                return false;
            }
            boolean d2 = d();
            boolean d3 = hyVar.d();
            if ((d2 || d3) && (!d2 || !d3 || !this.f542c.equals(hyVar.f542c))) {
                return false;
            }
            boolean e2 = e();
            boolean e3 = hyVar.e();
            if ((e2 || e3) && (!e2 || !e3 || this.f540a != hyVar.f540a)) {
                return false;
            }
            boolean f2 = f();
            boolean f3 = hyVar.f();
            if (!f2 && !f3) {
                return true;
            }
            return f2 && f3 && this.f543d.equals(hyVar.f543d);
        }
        return false;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f536a);
        jfVar.a(a);
        jfVar.a(this.f537a);
        jfVar.b();
        if (this.f538a != null) {
            jfVar.a(b);
            jfVar.a(this.f538a);
            jfVar.b();
        }
        if (this.f541b != null && c()) {
            jfVar.a(c);
            jfVar.a(this.f541b);
            jfVar.b();
        }
        if (this.f542c != null && d()) {
            jfVar.a(d);
            jfVar.a(this.f542c);
            jfVar.b();
        }
        if (e()) {
            jfVar.a(e);
            jfVar.a(this.f540a);
            jfVar.b();
        }
        if (this.f543d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f543d);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f539a.set(1, z);
    }

    public boolean b() {
        return this.f538a != null;
    }

    public boolean c() {
        return this.f541b != null;
    }

    public boolean d() {
        return this.f542c != null;
    }

    public boolean e() {
        return this.f539a.get(1);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hy)) {
            return m2271a((hy) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f543d != null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Target(");
        sb.append("channelId:");
        sb.append(this.f537a);
        sb.append(", ");
        sb.append("userId:");
        String str = this.f538a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(str);
        }
        if (c()) {
            sb.append(", ");
            sb.append("server:");
            String str2 = this.f541b;
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append(str2);
            }
        }
        if (d()) {
            sb.append(", ");
            sb.append("resource:");
            String str3 = this.f542c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
        }
        if (e()) {
            sb.append(", ");
            sb.append("isPreview:");
            sb.append(this.f540a);
        }
        if (f()) {
            sb.append(", ");
            sb.append("token:");
            String str4 = this.f543d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
