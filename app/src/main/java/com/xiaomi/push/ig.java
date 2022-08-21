package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ig implements iu<ig, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public List<hu> f619a;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f618a = new jk("XmPushActionCustomConfig");
    public static final jc a = new jc("", (byte) 15, 1);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ig igVar) {
        int a2;
        if (!getClass().equals(igVar.getClass())) {
            return getClass().getName().compareTo(igVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2303a()).compareTo(Boolean.valueOf(igVar.m2303a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2303a() && (a2 = iv.a(this.f619a, igVar.f619a)) != 0) {
            return a2;
        }
        return 0;
    }

    public List<hu> a() {
        return this.f619a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2302a() {
        if (this.f619a != null) {
            return;
        }
        throw new jg("Required field 'customConfigs' was not present! Struct: " + toString());
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b = m2380a.a;
            if (b == 0) {
                jfVar.f();
                m2302a();
                return;
            }
            if (m2380a.f793a == 1 && b == 15) {
                jd m2381a = jfVar.m2381a();
                this.f619a = new ArrayList(m2381a.f794a);
                for (int i = 0; i < m2381a.f794a; i++) {
                    hu huVar = new hu();
                    huVar.a(jfVar);
                    this.f619a.add(huVar);
                }
                jfVar.i();
            } else {
                ji.a(jfVar, b);
            }
            jfVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2303a() {
        return this.f619a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2304a(ig igVar) {
        if (igVar == null) {
            return false;
        }
        boolean m2303a = m2303a();
        boolean m2303a2 = igVar.m2303a();
        if (!m2303a && !m2303a2) {
            return true;
        }
        return m2303a && m2303a2 && this.f619a.equals(igVar.f619a);
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2302a();
        jfVar.a(f618a);
        if (this.f619a != null) {
            jfVar.a(a);
            jfVar.a(new jd((byte) 12, this.f619a.size()));
            for (hu huVar : this.f619a) {
                huVar.b(jfVar);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ig)) {
            return m2304a((ig) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionCustomConfig(");
        sb.append("customConfigs:");
        List<hu> list = this.f619a;
        if (list == null) {
            sb.append("null");
        } else {
            sb.append(list);
        }
        sb.append(")");
        return sb.toString();
    }
}
