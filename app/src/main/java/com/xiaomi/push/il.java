package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;

/* loaded from: classes3.dex */
public class il implements iu<il, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f693a;

    /* renamed from: a  reason: collision with other field name */
    public hy f694a;

    /* renamed from: a  reason: collision with other field name */
    public String f695a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f696a = new BitSet(1);

    /* renamed from: b  reason: collision with other field name */
    public String f697b;

    /* renamed from: c  reason: collision with other field name */
    public String f698c;

    /* renamed from: d  reason: collision with other field name */
    public String f699d;

    /* renamed from: e  reason: collision with other field name */
    public String f700e;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f692a = new jk("XmPushActionSendFeedbackResult");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 12, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 10, 6);
    public static final jc f = new jc("", (byte) 11, 7);
    public static final jc g = new jc("", (byte) 11, 8);

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(il ilVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        if (!getClass().equals(ilVar.getClass())) {
            return getClass().getName().compareTo(ilVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2329a()).compareTo(Boolean.valueOf(ilVar.m2329a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2329a() && (a8 = iv.a(this.f695a, ilVar.f695a)) != 0) {
            return a8;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ilVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a7 = iv.a(this.f694a, ilVar.f694a)) != 0) {
            return a7;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ilVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a6 = iv.a(this.f697b, ilVar.f697b)) != 0) {
            return a6;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ilVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a5 = iv.a(this.f698c, ilVar.f698c)) != 0) {
            return a5;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ilVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a4 = iv.a(this.f693a, ilVar.f693a)) != 0) {
            return a4;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ilVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a3 = iv.a(this.f699d, ilVar.f699d)) != 0) {
            return a3;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ilVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a2 = iv.a(this.f700e, ilVar.f700e)) != 0) {
            return a2;
        }
        return 0;
    }

    public void a() {
        if (this.f697b == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f698c != null) {
        } else {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
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
                if (e()) {
                    a();
                    return;
                }
                throw new jg("Required field 'errorCode' was not found in serialized data! Struct: " + toString());
            }
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f695a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f694a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f697b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f698c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 10) {
                        this.f693a = jfVar.m2379a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f699d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 8:
                    if (b2 == 11) {
                        this.f700e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
            }
            ji.a(jfVar, b2);
            jfVar.g();
        }
    }

    public void a(boolean z) {
        this.f696a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2329a() {
        return this.f695a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2330a(il ilVar) {
        if (ilVar == null) {
            return false;
        }
        boolean m2329a = m2329a();
        boolean m2329a2 = ilVar.m2329a();
        if ((m2329a || m2329a2) && (!m2329a || !m2329a2 || !this.f695a.equals(ilVar.f695a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ilVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f694a.m2271a(ilVar.f694a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ilVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f697b.equals(ilVar.f697b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ilVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f698c.equals(ilVar.f698c))) || this.f693a != ilVar.f693a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ilVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f699d.equals(ilVar.f699d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ilVar.g();
        if (!g2 && !g3) {
            return true;
        }
        return g2 && g3 && this.f700e.equals(ilVar.f700e);
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        a();
        jfVar.a(f692a);
        if (this.f695a != null && m2329a()) {
            jfVar.a(a);
            jfVar.a(this.f695a);
            jfVar.b();
        }
        if (this.f694a != null && b()) {
            jfVar.a(b);
            this.f694a.b(jfVar);
            jfVar.b();
        }
        if (this.f697b != null) {
            jfVar.a(c);
            jfVar.a(this.f697b);
            jfVar.b();
        }
        if (this.f698c != null) {
            jfVar.a(d);
            jfVar.a(this.f698c);
            jfVar.b();
        }
        jfVar.a(e);
        jfVar.a(this.f693a);
        jfVar.b();
        if (this.f699d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f699d);
            jfVar.b();
        }
        if (this.f700e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f700e);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public boolean b() {
        return this.f694a != null;
    }

    public boolean c() {
        return this.f697b != null;
    }

    public boolean d() {
        return this.f698c != null;
    }

    public boolean e() {
        return this.f696a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof il)) {
            return m2330a((il) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f699d != null;
    }

    public boolean g() {
        return this.f700e != null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionSendFeedbackResult(");
        boolean z2 = false;
        if (m2329a()) {
            sb.append("debug:");
            String str = this.f695a;
            if (str == null) {
                sb.append("null");
            } else {
                sb.append(str);
            }
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(", ");
            }
            sb.append("target:");
            hy hyVar = this.f694a;
            if (hyVar == null) {
                sb.append("null");
            } else {
                sb.append(hyVar);
            }
        } else {
            z2 = z;
        }
        if (!z2) {
            sb.append(", ");
        }
        sb.append("id:");
        String str2 = this.f697b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("appId:");
        String str3 = this.f698c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        sb.append(", ");
        sb.append("errorCode:");
        sb.append(this.f693a);
        if (f()) {
            sb.append(", ");
            sb.append("reason:");
            String str4 = this.f699d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("category:");
            String str5 = this.f700e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
