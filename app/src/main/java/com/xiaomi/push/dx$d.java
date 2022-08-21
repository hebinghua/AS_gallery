package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class dx$d extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f251a;

    /* renamed from: c  reason: collision with other field name */
    public boolean f253c;
    public boolean d;
    public boolean e;

    /* renamed from: b  reason: collision with other field name */
    public boolean f252b = false;

    /* renamed from: a  reason: collision with other field name */
    public String f250a = "";
    public String b = "";
    public String c = "";
    public int a = -1;

    public static dx$d a(byte[] bArr) {
        return (dx$d) new dx$d().a(bArr);
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public int mo2116a() {
        if (this.a < 0) {
            mo2118b();
        }
        return this.a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$d mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 8) {
                    a(bVar.m1951a());
                } else if (m1945a == 18) {
                    a(bVar.m1948a());
                } else if (m1945a == 26) {
                    b(bVar.m1948a());
                } else if (m1945a == 34) {
                    c(bVar.m1948a());
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public dx$d a(String str) {
        this.f253c = true;
        this.f250a = str;
        return this;
    }

    public dx$d a(boolean z) {
        this.f251a = true;
        this.f252b = z;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public String mo2116a() {
        return this.f250a;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2118b()) {
            cVar.m1997a(1, mo2116a());
        }
        if (m2085c()) {
            cVar.m1996a(2, mo2116a());
        }
        if (d()) {
            cVar.m1996a(3, mo2118b());
        }
        if (e()) {
            cVar.m1996a(4, c());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public boolean mo2116a() {
        return this.f252b;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        if (mo2118b()) {
            i = 0 + c.a(1, mo2116a());
        }
        if (m2085c()) {
            i += c.a(2, mo2116a());
        }
        if (d()) {
            i += c.a(3, mo2118b());
        }
        if (e()) {
            i += c.a(4, c());
        }
        this.a = i;
        return i;
    }

    public dx$d b(String str) {
        this.d = true;
        this.b = str;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public String mo2118b() {
        return this.b;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public boolean mo2118b() {
        return this.f251a;
    }

    public dx$d c(String str) {
        this.e = true;
        this.c = str;
        return this;
    }

    public String c() {
        return this.c;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2085c() {
        return this.f253c;
    }

    public boolean d() {
        return this.d;
    }

    public boolean e() {
        return this.e;
    }
}
