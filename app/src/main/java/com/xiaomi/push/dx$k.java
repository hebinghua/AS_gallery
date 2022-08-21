package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class dx$k extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f283a;

    /* renamed from: b  reason: collision with other field name */
    public boolean f286b;
    public boolean c;
    public boolean d;
    public boolean e;
    public boolean g;

    /* renamed from: a  reason: collision with other field name */
    public String f282a = "";

    /* renamed from: b  reason: collision with other field name */
    public String f285b = "";

    /* renamed from: a  reason: collision with other field name */
    public long f281a = 0;

    /* renamed from: b  reason: collision with other field name */
    public long f284b = 0;
    public boolean f = false;
    public int a = 0;
    public int b = -1;

    public static dx$k a(byte[] bArr) {
        return (dx$k) new dx$k().a(bArr);
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public int mo2116a() {
        if (this.b < 0) {
            mo2118b();
        }
        return this.b;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public long mo2116a() {
        return this.f281a;
    }

    public dx$k a(int i) {
        this.g = true;
        this.a = i;
        return this;
    }

    public dx$k a(long j) {
        this.c = true;
        this.f281a = j;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$k mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 10) {
                    a(bVar.m1948a());
                } else if (m1945a == 18) {
                    b(bVar.m1948a());
                } else if (m1945a == 24) {
                    a(bVar.m1946a());
                } else if (m1945a == 32) {
                    b(bVar.m1946a());
                } else if (m1945a == 40) {
                    a(bVar.m1951a());
                } else if (m1945a == 48) {
                    a(bVar.b());
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public dx$k a(String str) {
        this.f283a = true;
        this.f282a = str;
        return this;
    }

    public dx$k a(boolean z) {
        this.e = true;
        this.f = z;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public String mo2116a() {
        return this.f282a;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m1996a(1, mo2116a());
        }
        if (mo2118b()) {
            cVar.m1996a(2, mo2118b());
        }
        if (m2119c()) {
            cVar.m1993a(3, mo2116a());
        }
        if (d()) {
            cVar.m1993a(4, mo2118b());
        }
        if (f()) {
            cVar.m1997a(5, e());
        }
        if (g()) {
            cVar.m1992a(6, c());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2116a() {
        return this.f283a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        if (mo2116a()) {
            i = 0 + c.a(1, mo2116a());
        }
        if (mo2118b()) {
            i += c.a(2, mo2118b());
        }
        if (m2119c()) {
            i += c.a(3, mo2116a());
        }
        if (d()) {
            i += c.a(4, mo2118b());
        }
        if (f()) {
            i += c.a(5, e());
        }
        if (g()) {
            i += c.a(6, c());
        }
        this.b = i;
        return i;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public long mo2118b() {
        return this.f284b;
    }

    public dx$k b(long j) {
        this.d = true;
        this.f284b = j;
        return this;
    }

    public dx$k b(String str) {
        this.f286b = true;
        this.f285b = str;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public String mo2118b() {
        return this.f285b;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public boolean mo2118b() {
        return this.f286b;
    }

    public int c() {
        return this.a;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2119c() {
        return this.c;
    }

    public boolean d() {
        return this.d;
    }

    public boolean e() {
        return this.f;
    }

    public boolean f() {
        return this.e;
    }

    public boolean g() {
        return this.g;
    }
}
