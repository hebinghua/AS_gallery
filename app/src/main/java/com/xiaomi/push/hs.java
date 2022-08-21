package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/* loaded from: classes3.dex */
public class hs implements iu<hs, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public int f484a;

    /* renamed from: a  reason: collision with other field name */
    public hp f485a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f486a = new BitSet(1);

    /* renamed from: a  reason: collision with other field name */
    public List<hu> f487a;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f483a = new jk("NormalConfig");
    public static final jc a = new jc("", (byte) 8, 1);
    public static final jc b = new jc("", (byte) 15, 2);
    public static final jc c = new jc("", (byte) 8, 3);

    public int a() {
        return this.f484a;
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hs hsVar) {
        int a2;
        int a3;
        int a4;
        if (!getClass().equals(hsVar.getClass())) {
            return getClass().getName().compareTo(hsVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2242a()).compareTo(Boolean.valueOf(hsVar.m2242a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2242a() && (a4 = iv.a(this.f484a, hsVar.f484a)) != 0) {
            return a4;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hsVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a3 = iv.a(this.f487a, hsVar.f487a)) != 0) {
            return a3;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hsVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a2 = iv.a(this.f485a, hsVar.f485a)) != 0) {
            return a2;
        }
        return 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hp m2240a() {
        return this.f485a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2241a() {
        if (this.f487a != null) {
            return;
        }
        throw new jg("Required field 'configItems' was not present! Struct: " + toString());
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
                if (b2 == 8) {
                    this.f484a = jfVar.m2378a();
                    a(true);
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s != 2) {
                if (s == 3 && b2 == 8) {
                    this.f485a = hp.a(jfVar.m2378a());
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else {
                if (b2 == 15) {
                    jd m2381a = jfVar.m2381a();
                    this.f487a = new ArrayList(m2381a.f794a);
                    for (int i = 0; i < m2381a.f794a; i++) {
                        hu huVar = new hu();
                        huVar.a(jfVar);
                        this.f487a.add(huVar);
                    }
                    jfVar.i();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            }
        }
        jfVar.f();
        if (m2242a()) {
            m2241a();
            return;
        }
        throw new jg("Required field 'version' was not found in serialized data! Struct: " + toString());
    }

    public void a(boolean z) {
        this.f486a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2242a() {
        return this.f486a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2243a(hs hsVar) {
        if (hsVar != null && this.f484a == hsVar.f484a) {
            boolean b2 = b();
            boolean b3 = hsVar.b();
            if ((b2 || b3) && (!b2 || !b3 || !this.f487a.equals(hsVar.f487a))) {
                return false;
            }
            boolean c2 = c();
            boolean c3 = hsVar.c();
            if (!c2 && !c3) {
                return true;
            }
            return c2 && c3 && this.f485a.equals(hsVar.f485a);
        }
        return false;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2241a();
        jfVar.a(f483a);
        jfVar.a(a);
        jfVar.mo2376a(this.f484a);
        jfVar.b();
        if (this.f487a != null) {
            jfVar.a(b);
            jfVar.a(new jd((byte) 12, this.f487a.size()));
            for (hu huVar : this.f487a) {
                huVar.b(jfVar);
            }
            jfVar.e();
            jfVar.b();
        }
        if (this.f485a != null && c()) {
            jfVar.a(c);
            jfVar.mo2376a(this.f485a.a());
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean b() {
        return this.f487a != null;
    }

    public boolean c() {
        return this.f485a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hs)) {
            return m2243a((hs) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NormalConfig(");
        sb.append("version:");
        sb.append(this.f484a);
        sb.append(", ");
        sb.append("configItems:");
        List<hu> list = this.f487a;
        if (list == null) {
            sb.append("null");
        } else {
            sb.append(list);
        }
        if (c()) {
            sb.append(", ");
            sb.append("type:");
            hp hpVar = this.f485a;
            if (hpVar == null) {
                sb.append("null");
            } else {
                sb.append(hpVar);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
