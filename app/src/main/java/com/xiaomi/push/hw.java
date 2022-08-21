package com.xiaomi.push;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class hw implements iu<hw, Object>, Serializable, Cloneable {

    /* renamed from: a  reason: collision with other field name */
    public int f520a;

    /* renamed from: a  reason: collision with other field name */
    public long f521a;

    /* renamed from: a  reason: collision with other field name */
    public String f522a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f523a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f524a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f525a;

    /* renamed from: b  reason: collision with other field name */
    public int f526b;

    /* renamed from: b  reason: collision with other field name */
    public String f527b;

    /* renamed from: b  reason: collision with other field name */
    public Map<String, String> f528b;

    /* renamed from: c  reason: collision with other field name */
    public int f529c;

    /* renamed from: c  reason: collision with other field name */
    public String f530c;

    /* renamed from: c  reason: collision with other field name */
    public Map<String, String> f531c;

    /* renamed from: d  reason: collision with other field name */
    public String f532d;

    /* renamed from: e  reason: collision with other field name */
    public String f533e;

    /* renamed from: a  reason: collision with other field name */
    public static final jk f519a = new jk("PushMetaInfo");
    public static final jc a = new jc("", (byte) 11, 1);
    public static final jc b = new jc("", (byte) 10, 2);
    public static final jc c = new jc("", (byte) 11, 3);
    public static final jc d = new jc("", (byte) 11, 4);
    public static final jc e = new jc("", (byte) 11, 5);
    public static final jc f = new jc("", (byte) 8, 6);
    public static final jc g = new jc("", (byte) 11, 7);
    public static final jc h = new jc("", (byte) 8, 8);
    public static final jc i = new jc("", (byte) 8, 9);
    public static final jc j = new jc("", (byte) 13, 10);
    public static final jc k = new jc("", (byte) 13, 11);
    public static final jc l = new jc("", (byte) 2, 12);
    public static final jc m = new jc("", (byte) 13, 13);

    public hw() {
        this.f523a = new BitSet(5);
        this.f525a = false;
    }

    public hw(hw hwVar) {
        BitSet bitSet = new BitSet(5);
        this.f523a = bitSet;
        bitSet.clear();
        this.f523a.or(hwVar.f523a);
        if (hwVar.m2262a()) {
            this.f522a = hwVar.f522a;
        }
        this.f521a = hwVar.f521a;
        if (hwVar.m2268c()) {
            this.f527b = hwVar.f527b;
        }
        if (hwVar.m2269d()) {
            this.f530c = hwVar.f530c;
        }
        if (hwVar.e()) {
            this.f532d = hwVar.f532d;
        }
        this.f520a = hwVar.f520a;
        if (hwVar.g()) {
            this.f533e = hwVar.f533e;
        }
        this.f526b = hwVar.f526b;
        this.f529c = hwVar.f529c;
        if (hwVar.j()) {
            HashMap hashMap = new HashMap();
            for (Map.Entry<String, String> entry : hwVar.f524a.entrySet()) {
                hashMap.put(entry.getKey(), entry.getValue());
            }
            this.f524a = hashMap;
        }
        if (hwVar.k()) {
            HashMap hashMap2 = new HashMap();
            for (Map.Entry<String, String> entry2 : hwVar.f528b.entrySet()) {
                hashMap2.put(entry2.getKey(), entry2.getValue());
            }
            this.f528b = hashMap2;
        }
        this.f525a = hwVar.f525a;
        if (hwVar.n()) {
            HashMap hashMap3 = new HashMap();
            for (Map.Entry<String, String> entry3 : hwVar.f531c.entrySet()) {
                hashMap3.put(entry3.getKey(), entry3.getValue());
            }
            this.f531c = hashMap3;
        }
    }

    public int a() {
        return this.f520a;
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(hw hwVar) {
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
        int a12;
        int a13;
        int a14;
        if (!getClass().equals(hwVar.getClass())) {
            return getClass().getName().compareTo(hwVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(m2262a()).compareTo(Boolean.valueOf(hwVar.m2262a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (m2262a() && (a14 = iv.a(this.f522a, hwVar.f522a)) != 0) {
            return a14;
        }
        int compareTo2 = Boolean.valueOf(m2266b()).compareTo(Boolean.valueOf(hwVar.m2266b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (m2266b() && (a13 = iv.a(this.f521a, hwVar.f521a)) != 0) {
            return a13;
        }
        int compareTo3 = Boolean.valueOf(m2268c()).compareTo(Boolean.valueOf(hwVar.m2268c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (m2268c() && (a12 = iv.a(this.f527b, hwVar.f527b)) != 0) {
            return a12;
        }
        int compareTo4 = Boolean.valueOf(m2269d()).compareTo(Boolean.valueOf(hwVar.m2269d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (m2269d() && (a11 = iv.a(this.f530c, hwVar.f530c)) != 0) {
            return a11;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hwVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a10 = iv.a(this.f532d, hwVar.f532d)) != 0) {
            return a10;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hwVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a9 = iv.a(this.f520a, hwVar.f520a)) != 0) {
            return a9;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hwVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a8 = iv.a(this.f533e, hwVar.f533e)) != 0) {
            return a8;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hwVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a7 = iv.a(this.f526b, hwVar.f526b)) != 0) {
            return a7;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hwVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a6 = iv.a(this.f529c, hwVar.f529c)) != 0) {
            return a6;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hwVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a5 = iv.a(this.f524a, hwVar.f524a)) != 0) {
            return a5;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hwVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a4 = iv.a(this.f528b, hwVar.f528b)) != 0) {
            return a4;
        }
        int compareTo12 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(hwVar.m()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (m() && (a3 = iv.a(this.f525a, hwVar.f525a)) != 0) {
            return a3;
        }
        int compareTo13 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(hwVar.n()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (n() && (a2 = iv.a(this.f531c, hwVar.f531c)) != 0) {
            return a2;
        }
        return 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m2257a() {
        return this.f521a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hw m2258a() {
        return new hw(this);
    }

    public hw a(String str) {
        this.f522a = str;
        return this;
    }

    public hw a(Map<String, String> map) {
        this.f524a = map;
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2259a() {
        return this.f522a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, String> m2260a() {
        return this.f524a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2261a() {
        if (this.f522a != null) {
            return;
        }
        throw new jg("Required field 'id' was not present! Struct: " + toString());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.xiaomi.push.iu
    public void a(jf jfVar) {
        jfVar.m2384a();
        while (true) {
            jc m2380a = jfVar.m2380a();
            byte b2 = m2380a.a;
            if (b2 == 0) {
                jfVar.f();
                if (m2266b()) {
                    m2261a();
                    return;
                }
                throw new jg("Required field 'messageTs' was not found in serialized data! Struct: " + toString());
            }
            int i2 = 0;
            switch (m2380a.f793a) {
                case 1:
                    if (b2 == 11) {
                        this.f522a = jfVar.m2385a();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 2:
                    if (b2 == 10) {
                        this.f521a = jfVar.m2379a();
                        a(true);
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 3:
                    if (b2 == 11) {
                        this.f527b = jfVar.m2385a();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 4:
                    if (b2 == 11) {
                        this.f530c = jfVar.m2385a();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 5:
                    if (b2 == 11) {
                        this.f532d = jfVar.m2385a();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 6:
                    if (b2 == 8) {
                        this.f520a = jfVar.m2378a();
                        b(true);
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 7:
                    if (b2 == 11) {
                        this.f533e = jfVar.m2385a();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 8:
                    if (b2 == 8) {
                        this.f526b = jfVar.m2378a();
                        c(true);
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 9:
                    if (b2 == 8) {
                        this.f529c = jfVar.m2378a();
                        d(true);
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 10:
                    if (b2 == 13) {
                        je m2382a = jfVar.m2382a();
                        this.f524a = new HashMap(m2382a.f795a * 2);
                        while (i2 < m2382a.f795a) {
                            this.f524a.put(jfVar.m2385a(), jfVar.m2385a());
                            i2++;
                        }
                        jfVar.h();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 11:
                    if (b2 == 13) {
                        je m2382a2 = jfVar.m2382a();
                        this.f528b = new HashMap(m2382a2.f795a * 2);
                        while (i2 < m2382a2.f795a) {
                            this.f528b.put(jfVar.m2385a(), jfVar.m2385a());
                            i2++;
                        }
                        jfVar.h();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 12:
                    if (b2 == 2) {
                        this.f525a = jfVar.m2389a();
                        e(true);
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                case 13:
                    if (b2 == 13) {
                        je m2382a3 = jfVar.m2382a();
                        this.f531c = new HashMap(m2382a3.f795a * 2);
                        while (i2 < m2382a3.f795a) {
                            this.f531c.put(jfVar.m2385a(), jfVar.m2385a());
                            i2++;
                        }
                        jfVar.h();
                        break;
                    }
                    ji.a(jfVar, b2);
                    break;
                default:
                    ji.a(jfVar, b2);
                    break;
            }
            jfVar.g();
        }
    }

    public void a(String str, String str2) {
        if (this.f524a == null) {
            this.f524a = new HashMap();
        }
        this.f524a.put(str, str2);
    }

    public void a(boolean z) {
        this.f523a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2262a() {
        return this.f522a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2263a(hw hwVar) {
        if (hwVar == null) {
            return false;
        }
        boolean m2262a = m2262a();
        boolean m2262a2 = hwVar.m2262a();
        if (((m2262a || m2262a2) && (!m2262a || !m2262a2 || !this.f522a.equals(hwVar.f522a))) || this.f521a != hwVar.f521a) {
            return false;
        }
        boolean m2268c = m2268c();
        boolean m2268c2 = hwVar.m2268c();
        if ((m2268c || m2268c2) && (!m2268c || !m2268c2 || !this.f527b.equals(hwVar.f527b))) {
            return false;
        }
        boolean m2269d = m2269d();
        boolean m2269d2 = hwVar.m2269d();
        if ((m2269d || m2269d2) && (!m2269d || !m2269d2 || !this.f530c.equals(hwVar.f530c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = hwVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f532d.equals(hwVar.f532d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hwVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f520a != hwVar.f520a)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hwVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f533e.equals(hwVar.f533e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hwVar.h();
        if ((h2 || h3) && (!h2 || !h3 || this.f526b != hwVar.f526b)) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hwVar.i();
        if ((i2 || i3) && (!i2 || !i3 || this.f529c != hwVar.f529c)) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hwVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f524a.equals(hwVar.f524a))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = hwVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f528b.equals(hwVar.f528b))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = hwVar.m();
        if ((m2 || m3) && (!m2 || !m3 || this.f525a != hwVar.f525a)) {
            return false;
        }
        boolean n = n();
        boolean n2 = hwVar.n();
        if (!n && !n2) {
            return true;
        }
        return n && n2 && this.f531c.equals(hwVar.f531c);
    }

    public int b() {
        return this.f526b;
    }

    /* renamed from: b  reason: collision with other method in class */
    public String m2264b() {
        return this.f527b;
    }

    /* renamed from: b  reason: collision with other method in class */
    public Map<String, String> m2265b() {
        return this.f528b;
    }

    @Override // com.xiaomi.push.iu
    public void b(jf jfVar) {
        m2261a();
        jfVar.a(f519a);
        if (this.f522a != null) {
            jfVar.a(a);
            jfVar.a(this.f522a);
            jfVar.b();
        }
        jfVar.a(b);
        jfVar.a(this.f521a);
        jfVar.b();
        if (this.f527b != null && m2268c()) {
            jfVar.a(c);
            jfVar.a(this.f527b);
            jfVar.b();
        }
        if (this.f530c != null && m2269d()) {
            jfVar.a(d);
            jfVar.a(this.f530c);
            jfVar.b();
        }
        if (this.f532d != null && e()) {
            jfVar.a(e);
            jfVar.a(this.f532d);
            jfVar.b();
        }
        if (f()) {
            jfVar.a(f);
            jfVar.mo2376a(this.f520a);
            jfVar.b();
        }
        if (this.f533e != null && g()) {
            jfVar.a(g);
            jfVar.a(this.f533e);
            jfVar.b();
        }
        if (h()) {
            jfVar.a(h);
            jfVar.mo2376a(this.f526b);
            jfVar.b();
        }
        if (i()) {
            jfVar.a(i);
            jfVar.mo2376a(this.f529c);
            jfVar.b();
        }
        if (this.f524a != null && j()) {
            jfVar.a(j);
            jfVar.a(new je((byte) 11, (byte) 11, this.f524a.size()));
            for (Map.Entry<String, String> entry : this.f524a.entrySet()) {
                jfVar.a(entry.getKey());
                jfVar.a(entry.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        if (this.f528b != null && k()) {
            jfVar.a(k);
            jfVar.a(new je((byte) 11, (byte) 11, this.f528b.size()));
            for (Map.Entry<String, String> entry2 : this.f528b.entrySet()) {
                jfVar.a(entry2.getKey());
                jfVar.a(entry2.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        if (m()) {
            jfVar.a(l);
            jfVar.a(this.f525a);
            jfVar.b();
        }
        if (this.f531c != null && n()) {
            jfVar.a(m);
            jfVar.a(new je((byte) 11, (byte) 11, this.f531c.size()));
            for (Map.Entry<String, String> entry3 : this.f531c.entrySet()) {
                jfVar.a(entry3.getKey());
                jfVar.a(entry3.getValue());
            }
            jfVar.d();
            jfVar.b();
        }
        jfVar.c();
        jfVar.m2388a();
    }

    public void b(String str, String str2) {
        if (this.f528b == null) {
            this.f528b = new HashMap();
        }
        this.f528b.put(str, str2);
    }

    public void b(boolean z) {
        this.f523a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2266b() {
        return this.f523a.get(0);
    }

    public int c() {
        return this.f529c;
    }

    /* renamed from: c  reason: collision with other method in class */
    public String m2267c() {
        return this.f530c;
    }

    public void c(boolean z) {
        this.f523a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2268c() {
        return this.f527b != null;
    }

    public String d() {
        return this.f532d;
    }

    public void d(boolean z) {
        this.f523a.set(3, z);
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m2269d() {
        return this.f530c != null;
    }

    public void e(boolean z) {
        this.f523a.set(4, z);
    }

    public boolean e() {
        return this.f532d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hw)) {
            return m2263a((hw) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f523a.get(1);
    }

    public boolean g() {
        return this.f533e != null;
    }

    public boolean h() {
        return this.f523a.get(2);
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f523a.get(3);
    }

    public boolean j() {
        return this.f524a != null;
    }

    public boolean k() {
        return this.f528b != null;
    }

    public boolean l() {
        return this.f525a;
    }

    public boolean m() {
        return this.f523a.get(4);
    }

    public boolean n() {
        return this.f531c != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PushMetaInfo(");
        sb.append("id:");
        String str = this.f522a;
        if (str == null) {
            sb.append("null");
        } else {
            sb.append(com.xiaomi.push.service.bd.a(str));
        }
        sb.append(", ");
        sb.append("messageTs:");
        sb.append(this.f521a);
        if (m2268c()) {
            sb.append(", ");
            sb.append("topic:");
            String str2 = this.f527b;
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append(str2);
            }
        }
        if (m2269d()) {
            sb.append(", ");
            sb.append("title:");
            String str3 = this.f530c;
            if (str3 == null) {
                sb.append("null");
            } else {
                sb.append(str3);
            }
        }
        if (e()) {
            sb.append(", ");
            sb.append("description:");
            String str4 = this.f532d;
            if (str4 == null) {
                sb.append("null");
            } else {
                sb.append(str4);
            }
        }
        if (f()) {
            sb.append(", ");
            sb.append("notifyType:");
            sb.append(this.f520a);
        }
        if (g()) {
            sb.append(", ");
            sb.append("url:");
            String str5 = this.f533e;
            if (str5 == null) {
                sb.append("null");
            } else {
                sb.append(str5);
            }
        }
        if (h()) {
            sb.append(", ");
            sb.append("passThrough:");
            sb.append(this.f526b);
        }
        if (i()) {
            sb.append(", ");
            sb.append("notifyId:");
            sb.append(this.f529c);
        }
        if (j()) {
            sb.append(", ");
            sb.append("extra:");
            Map<String, String> map = this.f524a;
            if (map == null) {
                sb.append("null");
            } else {
                sb.append(map);
            }
        }
        if (k()) {
            sb.append(", ");
            sb.append("internal:");
            Map<String, String> map2 = this.f528b;
            if (map2 == null) {
                sb.append("null");
            } else {
                sb.append(map2);
            }
        }
        if (m()) {
            sb.append(", ");
            sb.append("ignoreRegInfo:");
            sb.append(this.f525a);
        }
        if (n()) {
            sb.append(", ");
            sb.append("apsProperFields:");
            Map<String, String> map3 = this.f531c;
            if (map3 == null) {
                sb.append("null");
            } else {
                sb.append(map3);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
