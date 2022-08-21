package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class dx$g extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f270a;

    /* renamed from: b  reason: collision with other field name */
    public boolean f271b;

    /* renamed from: c  reason: collision with other field name */
    public boolean f272c;

    /* renamed from: a  reason: collision with other field name */
    public String f269a = "";
    public String b = "";
    public String c = "";
    public int a = -1;

    public static dx$g a(byte[] bArr) {
        return (dx$g) new dx$g().a(bArr);
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
    public dx$g mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 10) {
                    a(bVar.m1948a());
                } else if (m1945a == 18) {
                    b(bVar.m1948a());
                } else if (m1945a == 26) {
                    c(bVar.m1948a());
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public dx$g a(String str) {
        this.f270a = true;
        this.f269a = str;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public String mo2116a() {
        return this.f269a;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m1996a(1, mo2116a());
        }
        if (mo2118b()) {
            cVar.m1996a(2, mo2118b());
        }
        if (m2104c()) {
            cVar.m1996a(3, c());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public boolean mo2116a() {
        return this.f270a;
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
        if (m2104c()) {
            i += c.a(3, c());
        }
        this.a = i;
        return i;
    }

    public dx$g b(String str) {
        this.f271b = true;
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
        return this.f271b;
    }

    public dx$g c(String str) {
        this.f272c = true;
        this.c = str;
        return this;
    }

    public String c() {
        return this.c;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2104c() {
        return this.f272c;
    }
}
