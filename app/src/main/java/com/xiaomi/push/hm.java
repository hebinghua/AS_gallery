package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class hm implements iu<hm, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public List<hn> f458a;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f457a = new jk("ClientUploadData");
    public static final jc a = new jc("", (byte) 15, 1);

    public int a() {
        List<hn> list = this.f458a;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hm hmVar) {
        int a2;
        if (!getClass().equals(hmVar.getClass())) {
            return getClass().getName().compareTo(hmVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2225a()).compareTo(Boolean.valueOf(hmVar.m2225a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2225a() && (a2 = iv.a(this.f458a, hmVar.f458a)) != 0) {
            return a2;
        }
        return 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2224a() {
        if (this.f458a != null) {
            return;
        }
        throw new jg("Required field 'uploadDataItems' was not present! Struct: " + toString());
    }

    public void a(hn hnVar) {
        if (this.f458a == null) {
            this.f458a = new ArrayList();
        }
        this.f458a.add(hnVar);
    }

    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b = m2380a.a;
            if (b == 0) {
                jfVar.f();
                m2224a();
                return;
            }
            if (m2380a.f793a == 1 && b == 15) {
                jd m2381a = jfVar.m2381a();
                this.f458a = new ArrayList(m2381a.f794a);
                for (int i = 0; i < m2381a.f794a; i++) {
                    hn hnVar = new hn();
                    hnVar.a(jfVar);
                    this.f458a.add(hnVar);
                }
                jfVar.i();
            } else {
                ji.a(jfVar, b);
            }
            jfVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2225a() {
        return this.f458a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2226a(hm hmVar) {
        if (hmVar == null) {
            return false;
        }
        boolean m2225a = m2225a();
        boolean m2225a2 = hmVar.m2225a();
        if (!m2225a && !m2225a2) {
            return true;
        }
        return m2225a && m2225a2 && this.f458a.equals(hmVar.f458a);
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2224a();
        jfVar.a(f457a);
        if (this.f458a != null) {
            jfVar.a(a);
            jfVar.a(new jd((byte) 12, this.f458a.size()));
            for (hn hnVar : this.f458a) {
                hnVar.b(jfVar);
            }
            jfVar.e();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hm)) {
            return m2226a((hm) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ClientUploadData(");
        sb.append("uploadDataItems:");
        List<hn> list = this.f458a;
        if (list == null) {
            sb.append("null");
        } else {
            sb.append(list);
        }
        sb.append(")");
        return sb.toString();
    }
}
