package com.xiaomi.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/* loaded from: classes3.dex */
public class id implements iu<id, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public long f585a;

    /* renamed from: a  reason: collision with other field name */
    public hy f586a;

    /* renamed from: a  reason: collision with other field name */
    public String f587a;

    /* renamed from: a  reason: collision with other field name */
    public List<String> f589a;

    /* renamed from: b  reason: collision with other field name */
    public String f591b;

    /* renamed from: c  reason: collision with other field name */
    public String f593c;

    /* renamed from: d  reason: collision with other field name */
    public String f594d;

    /* renamed from: e  reason: collision with other field name */
    public String f595e;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f584a = new jk("XmPushActionCommand");
    public static final jc a = new jc("", (byte) 12, 2);
    public static final jc b = new jc("", (byte) 11, 3);
    public static final jc c = new jc("", (byte) 11, 4);
    public static final jc d = new jc("", (byte) 11, 5);
    public static final jc e = new jc("", (byte) 15, 6);
    public static final jc f = new jc("", (byte) 11, 7);
    public static final jc g = new jc("", (byte) 11, 9);
    public static final jc h = new jc("", (byte) 2, 10);
    public static final jc i = new jc("", (byte) 2, 11);
    public static final jc j = new jc("", (byte) 10, 12);

    /* renamed from: a  reason: collision with other field name */
    private BitSet f588a = new BitSet(3);

    /* renamed from: a  reason: collision with other field name */
    public boolean f590a = false;

    /* renamed from: b  reason: collision with other field name */
    public boolean f592b = true;

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(id idVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        int a11;
        if (!getClass().equals(idVar.getClass())) {
            return getClass().getName().compareTo(idVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2285a()).compareTo(Boolean.valueOf(idVar.m2285a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2285a() && (a11 = iv.a(this.f586a, idVar.f586a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(idVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a10 = iv.a(this.f587a, idVar.f587a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(idVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a9 = iv.a(this.f591b, idVar.f591b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(idVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = iv.a(this.f593c, idVar.f593c)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(idVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = iv.a(this.f589a, idVar.f589a)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(idVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = iv.a(this.f594d, idVar.f594d)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(idVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = iv.a(this.f595e, idVar.f595e)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(idVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = iv.a(this.f590a, idVar.f590a)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(idVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = iv.a(this.f592b, idVar.f592b)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(idVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a2 = iv.a(this.f585a, idVar.f585a)) != 0) {
            return a2;
        }
        return 0;
    }

    public id a(String str) {
        this.f587a = str;
        return this;
    }

    public String a() {
        return this.f593c;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2283a() {
        if (this.f587a == null) {
            throw new jg("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f591b == null) {
            throw new jg("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f593c != null) {
        } else {
            throw new jg("Required field 'cmdName' was not present! Struct: " + toString());
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
                m2283a();
                return;
            }
            switch (m2380a.f793a) {
                case 2:
                    if (b2 == 12) {
                        hy hyVar = new hy();
                        this.f586a = hyVar;
                        hyVar.a(jfVar);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f587a = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f591b = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f593c = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 6:
                    if (b2 == 15) {
                        jd m2381a = jfVar.m2381a();
                        this.f589a = new ArrayList(m2381a.f794a);
                        for (int i2 = 0; i2 < m2381a.f794a; i2++) {
                            this.f589a.add(jfVar.m2385a());
                        }
                        jfVar.i();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f594d = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 9:
                    if (b2 == 11) {
                        this.f595e = jfVar.m2385a();
                        continue;
                        jfVar.g();
                    }
                    break;
                case 10:
                    if (b2 == 2) {
                        this.f590a = jfVar.m2389a();
                        a(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 11:
                    if (b2 == 2) {
                        this.f592b = jfVar.m2389a();
                        b(true);
                        continue;
                        jfVar.g();
                    }
                    break;
                case 12:
                    if (b2 == 10) {
                        this.f585a = jfVar.m2379a();
                        c(true);
                        continue;
                        jfVar.g();
                    }
                    break;
            }
            ji.a(jfVar, b2);
            jfVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2284a(String str) {
        if (this.f589a == null) {
            this.f589a = new ArrayList();
        }
        this.f589a.add(str);
    }

    public void a(boolean z) {
        this.f588a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2285a() {
        return this.f586a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2286a(id idVar) {
        if (idVar == null) {
            return false;
        }
        boolean m2285a = m2285a();
        boolean m2285a2 = idVar.m2285a();
        if ((m2285a || m2285a2) && (!m2285a || !m2285a2 || !this.f586a.m2271a(idVar.f586a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = idVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f587a.equals(idVar.f587a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = idVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f591b.equals(idVar.f591b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = idVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f593c.equals(idVar.f593c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = idVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f589a.equals(idVar.f589a))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = idVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f594d.equals(idVar.f594d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = idVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f595e.equals(idVar.f595e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = idVar.h();
        if ((h2 || h3) && (!h2 || !h3 || this.f590a != idVar.f590a)) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = idVar.i();
        if ((i2 || i3) && (!i2 || !i3 || this.f592b != idVar.f592b)) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = idVar.j();
        if (!j2 && !j3) {
            return true;
        }
        return j2 && j3 && this.f585a == idVar.f585a;
    }

    public id b(String str) {
        this.f591b = str;
        return this;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2283a();
        jfVar.a(f584a);
        if (this.f586a != null && m2285a()) {
            jfVar.a(a);
            this.f586a.b(jfVar);
            jfVar.b();
        }
        if (this.f587a != null) {
            jfVar.a(b);
            jfVar.a(this.f587a);
            jfVar.b();
        }
        if (this.f591b != null) {
            jfVar.a(c);
            jfVar.a(this.f591b);
            jfVar.b();
        }
        if (this.f593c != null) {
            jfVar.a(d);
            jfVar.a(this.f593c);
            jfVar.b();
        }
        if (this.f589a != null && e()) {
            jfVar.a(e);
            jfVar.a(new jd((byte) 11, this.f589a.size()));
            for (String str : this.f589a) {
                jfVar.a(str);
            }
            jfVar.e();
            jfVar.b();
        }
        if (this.f594d != null && f()) {
            jfVar.a(f);
            jfVar.a(this.f594d);
            jfVar.b();
        }
        if (this.f595e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f595e);
            jfVar.b();
        }
        if (h()) {
            jfVar.a(h);
            jfVar.a(this.f590a);
            jfVar.b();
        }
        if (i()) {
            jfVar.a(i);
            jfVar.a(this.f592b);
            jfVar.b();
        }
        if (j()) {
            jfVar.a(j);
            jfVar.a(this.f585a);
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(boolean z) {
        this.f588a.set(1, z);
    }

    public boolean b() {
        return this.f587a != null;
    }

    public id c(String str) {
        this.f593c = str;
        return this;
    }

    public void c(boolean z) {
        this.f588a.set(2, z);
    }

    public boolean c() {
        return this.f591b != null;
    }

    public id d(String str) {
        this.f594d = str;
        return this;
    }

    public boolean d() {
        return this.f593c != null;
    }

    public id e(String str) {
        this.f595e = str;
        return this;
    }

    public boolean e() {
        return this.f589a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof id)) {
            return m2286a((id) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f594d != null;
    }

    public boolean g() {
        return this.f595e != null;
    }

    public boolean h() {
        return this.f588a.get(0);
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f588a.get(1);
    }

    public boolean j() {
        return this.f588a.get(2);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionCommand(");
        if (m2285a()) {
            sb.append("target:");
            hy hyVar = this.f586a;
            if (hyVar == null) {
                sb.append("null");
            } else {
                sb.append(hyVar);
            }
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            sb.append(", ");
        }
        sb.append("id:");
        String str = this.f587a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(str);
        }
        sb.append(", ");
        sb.append("appId:");
        String str2 = this.f591b;
        if (str2 == null) {
            sb.append("null");
        } else {
            sb.append(str2);
        }
        sb.append(", ");
        sb.append("cmdName:");
        String str3 = this.f593c;
        if (str3 == null) {
            sb.append("null");
        } else {
            sb.append(str3);
        }
        if (e()) {
            sb.append(", ");
            sb.append("cmdArgs:");
            List<String> list = this.f589a;
            if (list == null) {
                sb.append("null");
            } else {
                sb.append(list);
            }
        }
        if (f()) {
            sb.append(", ");
            sb.append("packageName:");
            String str4 = this.f594d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (g()) {
            sb.append(", ");
            sb.append("category:");
            String str5 = this.f595e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("updateCache:");
            sb.append(this.f590a);
        }
        if (i()) {
            sb.append(", ");
            sb.append("response2Client:");
            sb.append(this.f592b);
        }
        if (j()) {
            sb.append(", ");
            sb.append("createdTs:");
            sb.append(this.f585a);
        }
        sb.append(")");
        return sb.toString();
    }
}
