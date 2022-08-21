package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class dx$h extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f274a;

    /* renamed from: b  reason: collision with other field name */
    public boolean f275b;
    public int a = 0;

    /* renamed from: a  reason: collision with other field name */
    public String f273a = "";
    public int b = -1;

    public static dx$h a(byte[] bArr) {
        return (dx$h) new dx$h().a(bArr);
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public int mo2116a() {
        if (this.b < 0) {
            mo2118b();
        }
        return this.b;
    }

    public dx$h a(int i) {
        this.f274a = true;
        this.a = i;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$h mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 8) {
                    a(bVar.b());
                } else if (m1945a == 18) {
                    a(bVar.m1948a());
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public dx$h a(String str) {
        this.f275b = true;
        this.f273a = str;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public String mo2116a() {
        return this.f273a;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m1992a(1, c());
        }
        if (mo2118b()) {
            cVar.m1996a(2, mo2116a());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public boolean mo2116a() {
        return this.f274a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        if (mo2116a()) {
            i = 0 + c.a(1, c());
        }
        if (mo2118b()) {
            i += c.a(2, mo2116a());
        }
        this.b = i;
        return i;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public boolean mo2118b() {
        return this.f275b;
    }

    public int c() {
        return this.a;
    }
}
