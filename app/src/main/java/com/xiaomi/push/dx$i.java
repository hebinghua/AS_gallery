package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class dx$i extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f277a;

    /* renamed from: a  reason: collision with other field name */
    public a f276a = a.a;
    public int a = -1;

    public static dx$i a(byte[] bArr) {
        return (dx$i) new dx$i().a(bArr);
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
    public a mo2116a() {
        return this.f276a;
    }

    public dx$i a(a aVar) {
        this.f277a = true;
        this.f276a = aVar;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$i mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 10) {
                    a(bVar.m1947a());
                } else if (!a(bVar, m1945a)) {
                    return this;
                }
            } else {
                return this;
            }
        }
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m1994a(1, mo2116a());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public boolean mo2116a() {
        return this.f277a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        if (mo2116a()) {
            i = 0 + c.a(1, mo2116a());
        }
        this.a = i;
        return i;
    }
}
