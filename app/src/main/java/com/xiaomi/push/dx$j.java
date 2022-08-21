package com.xiaomi.push;

import java.util.Objects;

/* loaded from: classes3.dex */
public final class dx$j extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f280a;
    public boolean b;

    /* renamed from: a  reason: collision with other field name */
    public a f278a = a.a;

    /* renamed from: a  reason: collision with other field name */
    public dx$b f279a = null;
    public int a = -1;

    public static dx$j a(byte[] bArr) {
        return (dx$j) new dx$j().a(bArr);
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
        return this.f278a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$b mo2116a() {
        return this.f279a;
    }

    public dx$j a(a aVar) {
        this.f280a = true;
        this.f278a = aVar;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$j mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            if (m1945a != 0) {
                if (m1945a == 10) {
                    a(bVar.m1947a());
                } else if (m1945a == 18) {
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

    public dx$j a(dx$b dx_b) {
        Objects.requireNonNull(dx_b);
        this.b = true;
        this.f279a = dx_b;
        return this;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m1994a(1, mo2116a());
        }
        if (mo2118b()) {
            cVar.m1995a(2, (e) mo2116a());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2116a() {
        return this.f280a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        if (mo2116a()) {
            i = 0 + c.a(1, mo2116a());
        }
        if (mo2118b()) {
            i += c.a(2, (e) mo2116a());
        }
        this.a = i;
        return i;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public boolean mo2118b() {
        return this.b;
    }
}
