package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class dx$b extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f239a;

    /* renamed from: c  reason: collision with other field name */
    public boolean f241c;

    /* renamed from: d  reason: collision with other field name */
    public boolean f242d;
    public boolean e;

    /* renamed from: b  reason: collision with other field name */
    public boolean f240b = false;
    public int a = 0;
    public int b = 0;
    public int c = 0;
    public int d = -1;

    public static dx$b a(byte[] bArr) {
        return (dx$b) new dx$b().a(bArr);
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public int mo2116a() {
        if (this.d < 0) {
            mo2118b();
        }
        return this.d;
    }

    public dx$b a(int i) {
        this.f241c = true;
        this.a = i;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$b mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 8) {
                    a(bVar.m1951a());
                } else if (m1945a == 24) {
                    a(bVar.b());
                } else if (m1945a == 32) {
                    b(bVar.b());
                } else if (m1945a == 40) {
                    c(bVar.b());
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public dx$b a(boolean z) {
        this.f239a = true;
        this.f240b = z;
        return this;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2118b()) {
            cVar.m1997a(1, mo2116a());
        }
        if (m2072c()) {
            cVar.m1992a(3, c());
        }
        if (m2073d()) {
            cVar.m1992a(4, d());
        }
        if (m2074e()) {
            cVar.m1992a(5, e());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2116a() {
        return this.f240b;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        if (mo2118b()) {
            i = 0 + c.a(1, mo2116a());
        }
        if (m2072c()) {
            i += c.a(3, c());
        }
        if (m2073d()) {
            i += c.a(4, d());
        }
        if (m2074e()) {
            i += c.a(5, e());
        }
        this.d = i;
        return i;
    }

    public dx$b b(int i) {
        this.f242d = true;
        this.b = i;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public boolean mo2118b() {
        return this.f239a;
    }

    public int c() {
        return this.a;
    }

    public dx$b c(int i) {
        this.e = true;
        this.c = i;
        return this;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2072c() {
        return this.f241c;
    }

    public int d() {
        return this.b;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m2073d() {
        return this.f242d;
    }

    public int e() {
        return this.c;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m2074e() {
        return this.e;
    }
}
