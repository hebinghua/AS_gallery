package com.xiaomi.push;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: classes3.dex */
public final class dw$a extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f224a;

    /* renamed from: b  reason: collision with other field name */
    public boolean f225b;
    public boolean d;
    public boolean e;
    public int a = 0;

    /* renamed from: c  reason: collision with other field name */
    public boolean f226c = false;
    public int b = 0;
    public boolean f = false;

    /* renamed from: a  reason: collision with other field name */
    public List<String> f223a = Collections.emptyList();
    public int c = -1;

    public static dw$a a(byte[] bArr) {
        return (dw$a) new dw$a().a(bArr);
    }

    public static dw$a b(b bVar) {
        return new dw$a().mo2115a(bVar);
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public int mo2116a() {
        if (this.c < 0) {
            mo2118b();
        }
        return this.c;
    }

    public dw$a a(int i) {
        this.f224a = true;
        this.a = i;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dw$a mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 8) {
                    a(bVar.c());
                } else if (m1945a == 16) {
                    a(bVar.m1951a());
                } else if (m1945a == 24) {
                    b(bVar.b());
                } else if (m1945a == 32) {
                    b(bVar.m1951a());
                } else if (m1945a == 42) {
                    a(bVar.m1948a());
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public dw$a a(String str) {
        Objects.requireNonNull(str);
        if (this.f223a.isEmpty()) {
            this.f223a = new ArrayList();
        }
        this.f223a.add(str);
        return this;
    }

    public dw$a a(boolean z) {
        this.f225b = true;
        this.f226c = z;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public List<String> mo2116a() {
        return this.f223a;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m2005b(1, c());
        }
        if (m2054c()) {
            cVar.m1997a(2, mo2118b());
        }
        if (m2055d()) {
            cVar.m1992a(3, d());
        }
        if (f()) {
            cVar.m1997a(4, m2056e());
        }
        for (String str : mo2116a()) {
            cVar.m1996a(5, str);
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public boolean mo2116a() {
        return this.f224a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        int b = mo2116a() ? c.b(1, c()) + 0 : 0;
        if (m2054c()) {
            b += c.a(2, mo2118b());
        }
        if (m2055d()) {
            b += c.a(3, d());
        }
        if (f()) {
            b += c.a(4, m2056e());
        }
        for (String str : mo2116a()) {
            i += c.a(str);
        }
        int size = b + i + (mo2116a().size() * 1);
        this.c = size;
        return size;
    }

    public dw$a b(int i) {
        this.d = true;
        this.b = i;
        return this;
    }

    public dw$a b(boolean z) {
        this.e = true;
        this.f = z;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public boolean mo2118b() {
        return this.f226c;
    }

    public int c() {
        return this.a;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2054c() {
        return this.f225b;
    }

    public int d() {
        return this.b;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m2055d() {
        return this.d;
    }

    public int e() {
        return this.f223a.size();
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m2056e() {
        return this.f;
    }

    public boolean f() {
        return this.e;
    }
}
