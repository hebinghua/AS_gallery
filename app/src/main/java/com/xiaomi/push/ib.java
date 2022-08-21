package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class ib implements iu<ib, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public int f579a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f580a = new BitSet(2);

    /* renamed from: b  reason: collision with other field name */
    public int f581b;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f578a = new jk("XmPushActionCheckClientInfo");
    public static final jc a = new jc("", (byte) 8, 1);
    public static final jc b = new jc("", (byte) 8, 2);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(ib ibVar) {
        int a2;
        int a3;
        if (!getClass().equals(ibVar.getClass())) {
            return getClass().getName().compareTo(ibVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2279a()).compareTo(Boolean.valueOf(ibVar.m2279a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2279a() && (a3 = iv.a(this.f579a, ibVar.f579a)) != 0) {
            return a3;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ibVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a2 = iv.a(this.f581b, ibVar.f581b)) != 0) {
            return a2;
        }
        return 0;
    }

    public ib a(int i) {
        this.f579a = i;
        a(true);
        return this;
    }

    public void a() {
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
            if (s != 1) {
                if (s == 2 && b2 == 8) {
                    this.f581b = jfVar.m2378a();
                    b(true);
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else {
                if (b2 == 8) {
                    this.f579a = jfVar.m2378a();
                    a(true);
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            }
        }
        jfVar.f();
        if (!m2279a()) {
            throw new jg("Required field 'miscConfigVersion' was not found in serialized data! Struct: " + toString());
        } else if (b()) {
            a();
        } else {
            throw new jg("Required field 'pluginConfigVersion' was not found in serialized data! Struct: " + toString());
        }
    }

    public void a(boolean z) {
        this.f580a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2279a() {
        return this.f580a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2280a(ib ibVar) {
        return ibVar != null && this.f579a == ibVar.f579a && this.f581b == ibVar.f581b;
    }

    public ib b(int i) {
        this.f581b = i;
        b(true);
        return this;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f578a);
        jfVar.a(a);
        jfVar.mo2376a(this.f579a);
        jfVar.b();
        jfVar.a(b);
        jfVar.mo2376a(this.f581b);
        jfVar.b();
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f580a.set(1, z);
    }

    public boolean b() {
        return this.f580a.get(1);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ib)) {
            return m2280a((ib) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "XmPushActionCheckClientInfo(miscConfigVersion:" + this.f579a + ", pluginConfigVersion:" + this.f581b + ")";
    }
}
