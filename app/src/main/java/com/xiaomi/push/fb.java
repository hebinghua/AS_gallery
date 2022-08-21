package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class fb implements iu<fb, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public String f334a;

    /* renamed from: a  reason: collision with other field name */
    public List<fa> f335a;

    /* renamed from: b  reason: collision with other field name */
    public String f336b;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f333a = new jk("StatsEvents");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 11, 2);
    public static final jc c = new jc("", (byte) 15, 3);

    public fb() {
    }

    public fb(String str, List<fa> list) {
        this();
        this.f334a = str;
        this.f335a = list;
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(fb fbVar) {
        int a2;
        int a3;
        int a4;
        if (!getClass().equals(fbVar.getClass())) {
            return getClass().getName().compareTo(fbVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2146a()).compareTo(Boolean.valueOf(fbVar.m2146a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2146a() && (a4 = iv.a(this.f334a, fbVar.f334a)) != 0) {
            return a4;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(fbVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a3 = iv.a(this.f336b, fbVar.f336b)) != 0) {
            return a3;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(fbVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a2 = iv.a(this.f335a, fbVar.f335a)) != 0) {
            return a2;
        }
        return 0;
    }

    public fb a(String str) {
        this.f336b = str;
        return this;
    }

    public void a() {
        if (this.f334a == null) {
            throw new jg("Required field 'uuid' was not present! Struct: " + toString());
        } else if (this.f335a != null) {
        } else {
            throw new jg("Required field 'events' was not present! Struct: " + toString());
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
                a();
                return;
            }
            short s = m2380a.f793a;
            if (s == 1) {
                if (b2 == 11) {
                    this.f334a = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s != 2) {
                if (s == 3 && b2 == 15) {
                    jd m2381a = jfVar.m2381a();
                    this.f335a = new ArrayList(m2381a.f794a);
                    for (int i = 0; i < m2381a.f794a; i++) {
                        fa faVar = new fa();
                        faVar.a(jfVar);
                        this.f335a.add(faVar);
                    }
                    jfVar.i();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else {
                if (b2 == 11) {
                    this.f336b = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2146a() {
        return this.f334a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2147a(fb fbVar) {
        if (fbVar == null) {
            return false;
        }
        boolean m2146a = m2146a();
        boolean m2146a2 = fbVar.m2146a();
        if ((m2146a || m2146a2) && (!m2146a || !m2146a2 || !this.f334a.equals(fbVar.f334a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = fbVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f336b.equals(fbVar.f336b))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = fbVar.c();
        if (!c2 && !c3) {
            return true;
        }
        return c2 && c3 && this.f335a.equals(fbVar.f335a);
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f333a);
        if (this.f334a != null) {
            jfVar.a(a);
            jfVar.a(this.f334a);
            jfVar.b();
        }
        if (this.f336b != null && b()) {
            jfVar.a(b);
            jfVar.a(this.f336b);
            jfVar.b();
        }
        if (this.f335a != null) {
            jfVar.a(c);
            jfVar.a(new jd((byte) 12, this.f335a.size()));
            for (fa faVar : this.f335a) {
                faVar.b(jfVar);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean b() {
        return this.f336b != null;
    }

    public boolean c() {
        return this.f335a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof fb)) {
            return m2147a((fb) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StatsEvents(");
        sb.append("uuid:");
        String str = this.f334a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(str);
        }
        if (b()) {
            sb.append(", ");
            sb.append("operator:");
            String str2 = this.f336b;
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append(str2);
            }
        }
        sb.append(", ");
        sb.append("events:");
        List<fa> list = this.f335a;
        if (list == null) {
            sb.append("null");
        } else {
            sb.append(list);
        }
        sb.append(")");
        return sb.toString();
    }
}
