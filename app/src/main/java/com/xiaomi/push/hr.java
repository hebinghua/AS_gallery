package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class hr implements iu<hr, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f479a;

    /* renamed from: a  reason: collision with other field name */
    public hl f480a;

    /* renamed from: a  reason: collision with other field name */
    public String f481a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f482a = new BitSet(1);

    /* renamed from: a  reason: collision with other field name */
    public static final jk f478a = new jk("DataCollectionItem");
    public static final jc a = new jc("", (byte) 10, 1);
    public static final jc b = new jc("", (byte) 8, 2);
    public static final jc c = new jc("", (byte) 11, 3);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hr hrVar) {
        int a2;
        int a3;
        int a4;
        if (!getClass().equals(hrVar.getClass())) {
            return getClass().getName().compareTo(hrVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2238a()).compareTo(Boolean.valueOf(hrVar.m2238a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2238a() && (a4 = iv.a(this.f479a, hrVar.f479a)) != 0) {
            return a4;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hrVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a3 = iv.a(this.f480a, hrVar.f480a)) != 0) {
            return a3;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hrVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a2 = iv.a(this.f481a, hrVar.f481a)) != 0) {
            return a2;
        }
        return 0;
    }

    public hr a(long j) {
        this.f479a = j;
        a(true);
        return this;
    }

    public hr a(hl hlVar) {
        this.f480a = hlVar;
        return this;
    }

    public hr a(String str) {
        this.f481a = str;
        return this;
    }

    public String a() {
        return this.f481a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2237a() {
        if (this.f480a == null) {
            throw new jg("Required field 'collectionType' was not present! Struct: " + toString());
        } else if (this.f481a != null) {
        } else {
            throw new jg("Required field 'content' was not present! Struct: " + toString());
        }
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
                    this.f479a = jfVar.m2379a();
                    a(true);
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else if (s != 2) {
                if (s == 3 && b2 == 11) {
                    this.f481a = jfVar.m2385a();
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            } else {
                if (b2 == 8) {
                    this.f480a = hl.a(jfVar.m2378a());
                    jfVar.g();
                }
                ji.a(jfVar, b2);
                jfVar.g();
            }
        }
        jfVar.f();
        if (m2238a()) {
            m2237a();
            return;
        }
        throw new jg("Required field 'collectedAt' was not found in serialized data! Struct: " + toString());
    }

    public void a(boolean z) {
        this.f482a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2238a() {
        return this.f482a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2239a(hr hrVar) {
        if (hrVar != null && this.f479a == hrVar.f479a) {
            boolean b2 = b();
            boolean b3 = hrVar.b();
            if ((b2 || b3) && (!b2 || !b3 || !this.f480a.equals(hrVar.f480a))) {
                return false;
            }
            boolean c2 = c();
            boolean c3 = hrVar.c();
            if (!c2 && !c3) {
                return true;
            }
            return c2 && c3 && this.f481a.equals(hrVar.f481a);
        }
        return false;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2237a();
        jfVar.a(f478a);
        jfVar.a(a);
        jfVar.a(this.f479a);
        jfVar.b();
        if (this.f480a != null) {
            jfVar.a(b);
            jfVar.mo2376a(this.f480a.a());
            jfVar.b();
        }
        if (this.f481a != null) {
            jfVar.a(c);
            jfVar.a(this.f481a);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean b() {
        return this.f480a != null;
    }

    public boolean c() {
        return this.f481a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hr)) {
            return m2239a((hr) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DataCollectionItem(");
        sb.append("collectedAt:");
        sb.append(this.f479a);
        sb.append(", ");
        sb.append("collectionType:");
        hl hlVar = this.f480a;
        if (hlVar == null) {
            sb.append("null");
        } else {
            sb.append(hlVar);
        }
        sb.append(", ");
        sb.append("content:");
        String str = this.f481a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(str);
        }
        sb.append(")");
        return sb.toString();
    }
}
