package com.xiaomi.push;

import java.util.Objects;

/* loaded from: classes3.dex */
public final class dx$f extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f267a;

    /* renamed from: b  reason: collision with other field name */
    public boolean f268b;
    public boolean c;

    /* renamed from: a  reason: collision with other field name */
    public String f266a = "";
    public String b = "";

    /* renamed from: a  reason: collision with other field name */
    public dx$b f265a = null;
    public int a = -1;

    public static dx$f a(byte[] bArr) {
        return (dx$f) new dx$f().a(bArr);
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
    /* renamed from: a  reason: collision with other method in class */
    public dx$b mo2116a() {
        return this.f265a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$f mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 10) {
                    a(bVar.m1948a());
                } else if (m1945a == 18) {
                    b(bVar.m1948a());
                } else if (m1945a == 26) {
                    dx$b dx_b = new dx$b();
                    bVar.a(dx_b);
                    a(dx_b);
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    public dx$f a(dx$b dx_b) {
        Objects.requireNonNull(dx_b);
        this.c = true;
        this.f265a = dx_b;
        return this;
    }

    public dx$f a(String str) {
        this.f267a = true;
        this.f266a = str;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public String mo2116a() {
        return this.f266a;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m1996a(1, mo2116a());
        }
        if (mo2118b()) {
            cVar.m1996a(2, mo2118b());
        }
        if (c()) {
            cVar.m1995a(3, (e) mo2116a());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2116a() {
        return this.f267a;
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
        if (c()) {
            i += c.a(3, (e) mo2116a());
        }
        this.a = i;
        return i;
    }

    public dx$f b(String str) {
        this.f268b = true;
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
        return this.f268b;
    }

    public boolean c() {
        return this.c;
    }
}
