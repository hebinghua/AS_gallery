package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ic implements iu<ic, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public List<hr> f583a;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f582a = new jk("XmPushActionCollectData");
    public static final jc a = new jc("", (byte) 15, 1);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ic icVar) {
        int a2;
        if (!getClass().equals(icVar.getClass())) {
            return getClass().getName().compareTo(icVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2281a()).compareTo(Boolean.valueOf(icVar.m2281a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2281a() && (a2 = iv.a(this.f583a, icVar.f583a)) != 0) {
            return a2;
        }
        return 0;
    }

    public ic a(List<hr> list) {
        this.f583a = list;
        return this;
    }

    public void a() {
        if (this.f583a != null) {
            return;
        }
        throw new jg("Required field 'dataCollectionItems' was not present! Struct: " + toString());
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b = m2380a.a;
            if (b == 0) {
                jfVar.f();
                a();
                return;
            }
            if (m2380a.f793a == 1 && b == 15) {
                jd m2381a = jfVar.m2381a();
                this.f583a = new ArrayList(m2381a.f794a);
                for (int i = 0; i < m2381a.f794a; i++) {
                    hr hrVar = new hr();
                    hrVar.a(jfVar);
                    this.f583a.add(hrVar);
                }
                jfVar.i();
            } else {
                ji.a(jfVar, b);
            }
            jfVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2281a() {
        return this.f583a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2282a(ic icVar) {
        if (icVar == null) {
            return false;
        }
        boolean m2281a = m2281a();
        boolean m2281a2 = icVar.m2281a();
        if (!m2281a && !m2281a2) {
            return true;
        }
        return m2281a && m2281a2 && this.f583a.equals(icVar.f583a);
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f582a);
        if (this.f583a != null) {
            jfVar.a(a);
            jfVar.a(new jd((byte) 12, this.f583a.size()));
            for (hr hrVar : this.f583a) {
                hrVar.b(jfVar);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ic)) {
            return m2282a((ic) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionCollectData(");
        sb.append("dataCollectionItems:");
        List<hr> list = this.f583a;
        if (list == null) {
            sb.append("null");
        } else {
            sb.append(list);
        }
        sb.append(")");
        return sb.toString();
    }
}
