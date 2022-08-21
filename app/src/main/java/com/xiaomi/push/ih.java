package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ih implements iu<ih, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public List<hs> f621a;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f620a = new jk("XmPushActionNormalConfig");
    public static final jc a = new jc("", (byte) 15, 1);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ih ihVar) {
        int a2;
        if (!getClass().equals(ihVar.getClass())) {
            return getClass().getName().compareTo(ihVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2306a()).compareTo(Boolean.valueOf(ihVar.m2306a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2306a() && (a2 = iv.a(this.f621a, ihVar.f621a)) != 0) {
            return a2;
        }
        return 0;
    }

    public List<hs> a() {
        return this.f621a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2305a() {
        if (this.f621a != null) {
            return;
        }
        throw new jg("Required field 'normalConfigs' was not present! Struct: " + toString());
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b = m2380a.a;
            if (b == 0) {
                jfVar.f();
                m2305a();
                return;
            }
            if (m2380a.f793a == 1 && b == 15) {
                jd m2381a = jfVar.m2381a();
                this.f621a = new ArrayList(m2381a.f794a);
                for (int i = 0; i < m2381a.f794a; i++) {
                    hs hsVar = new hs();
                    hsVar.a(jfVar);
                    this.f621a.add(hsVar);
                }
                jfVar.i();
            } else {
                ji.a(jfVar, b);
            }
            jfVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2306a() {
        return this.f621a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2307a(ih ihVar) {
        if (ihVar == null) {
            return false;
        }
        boolean m2306a = m2306a();
        boolean m2306a2 = ihVar.m2306a();
        if (!m2306a && !m2306a2) {
            return true;
        }
        return m2306a && m2306a2 && this.f621a.equals(ihVar.f621a);
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2305a();
        jfVar.a(f620a);
        if (this.f621a != null) {
            jfVar.a(a);
            jfVar.a(new jd((byte) 12, this.f621a.size()));
            for (hs hsVar : this.f621a) {
                hsVar.b(jfVar);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ih)) {
            return m2307a((ih) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionNormalConfig(");
        sb.append("normalConfigs:");
        List<hs> list = this.f621a;
        if (list == null) {
            sb.append("null");
        } else {
            sb.append(list);
        }
        sb.append(")");
        return sb.toString();
    }
}
