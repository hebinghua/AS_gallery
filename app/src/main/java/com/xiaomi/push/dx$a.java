package com.xiaomi.push;

/* loaded from: classes3.dex */
public final class dx$a extends e {

    /* renamed from: a  reason: collision with other field name */
    public boolean f229a;

    /* renamed from: b  reason: collision with other field name */
    public boolean f231b;

    /* renamed from: c  reason: collision with other field name */
    public boolean f233c;

    /* renamed from: d  reason: collision with other field name */
    public boolean f235d;

    /* renamed from: e  reason: collision with other field name */
    public boolean f237e;

    /* renamed from: f  reason: collision with other field name */
    public boolean f238f;
    public boolean g;
    public boolean h;
    public boolean i;
    public boolean j;
    public boolean k;
    public int a = 0;

    /* renamed from: a  reason: collision with other field name */
    public long f227a = 0;

    /* renamed from: a  reason: collision with other field name */
    public String f228a = "";

    /* renamed from: b  reason: collision with other field name */
    public String f230b = "";

    /* renamed from: c  reason: collision with other field name */
    public String f232c = "";

    /* renamed from: d  reason: collision with other field name */
    public String f234d = "";

    /* renamed from: e  reason: collision with other field name */
    public String f236e = "";
    public int b = 1;
    public int c = 0;
    public int d = 0;
    public String f = "";
    public int e = -1;

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public int mo2116a() {
        if (this.e < 0) {
            mo2118b();
        }
        return this.e;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public long mo2116a() {
        return this.f227a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$a mo2116a() {
        this.f238f = false;
        this.f234d = "";
        return this;
    }

    public dx$a a(int i) {
        this.f229a = true;
        this.a = i;
        return this;
    }

    public dx$a a(long j) {
        this.f231b = true;
        this.f227a = j;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public dx$a mo2115a(b bVar) {
        while (true) {
            int m1945a = bVar.m1945a();
            switch (m1945a) {
                case 0:
                    return this;
                case 8:
                    a(bVar.b());
                    break;
                case 16:
                    a(bVar.m1954b());
                    break;
                case 26:
                    a(bVar.m1948a());
                    break;
                case 34:
                    b(bVar.m1948a());
                    break;
                case 42:
                    c(bVar.m1948a());
                    break;
                case 50:
                    d(bVar.m1948a());
                    break;
                case 58:
                    e(bVar.m1948a());
                    break;
                case 64:
                    b(bVar.b());
                    break;
                case 72:
                    c(bVar.b());
                    break;
                case 80:
                    d(bVar.b());
                    break;
                case 90:
                    f(bVar.m1948a());
                    break;
                default:
                    if (a(bVar, m1945a)) {
                        break;
                    } else {
                        return this;
                    }
            }
        }
    }

    public dx$a a(String str) {
        this.f233c = true;
        this.f228a = str;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a  reason: collision with other method in class */
    public String mo2116a() {
        return this.f228a;
    }

    @Override // com.xiaomi.push.e
    public void a(c cVar) {
        if (mo2116a()) {
            cVar.m1992a(1, c());
        }
        if (mo2118b()) {
            cVar.m2006b(2, mo2116a());
        }
        if (m2062c()) {
            cVar.m1996a(3, mo2116a());
        }
        if (m2064d()) {
            cVar.m1996a(4, mo2118b());
        }
        if (m2066e()) {
            cVar.m1996a(5, m2061c());
        }
        if (m2068f()) {
            cVar.m1996a(6, m2063d());
        }
        if (g()) {
            cVar.m1996a(7, m2065e());
        }
        if (h()) {
            cVar.m1992a(8, d());
        }
        if (i()) {
            cVar.m1992a(9, e());
        }
        if (j()) {
            cVar.m1992a(10, f());
        }
        if (k()) {
            cVar.m1996a(11, m2067f());
        }
    }

    @Override // com.xiaomi.push.e
    /* renamed from: a */
    public boolean mo2116a() {
        return this.f229a;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public int mo2118b() {
        int i = 0;
        if (mo2116a()) {
            i = 0 + c.a(1, c());
        }
        if (mo2118b()) {
            i += c.b(2, mo2116a());
        }
        if (m2062c()) {
            i += c.a(3, mo2116a());
        }
        if (m2064d()) {
            i += c.a(4, mo2118b());
        }
        if (m2066e()) {
            i += c.a(5, m2061c());
        }
        if (m2068f()) {
            i += c.a(6, m2063d());
        }
        if (g()) {
            i += c.a(7, m2065e());
        }
        if (h()) {
            i += c.a(8, d());
        }
        if (i()) {
            i += c.a(9, e());
        }
        if (j()) {
            i += c.a(10, f());
        }
        if (k()) {
            i += c.a(11, m2067f());
        }
        this.e = i;
        return i;
    }

    public dx$a b(int i) {
        this.h = true;
        this.b = i;
        return this;
    }

    public dx$a b(String str) {
        this.f235d = true;
        this.f230b = str;
        return this;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b  reason: collision with other method in class */
    public String mo2118b() {
        return this.f230b;
    }

    @Override // com.xiaomi.push.e
    /* renamed from: b */
    public boolean mo2118b() {
        return this.f231b;
    }

    public int c() {
        return this.a;
    }

    public dx$a c(int i) {
        this.i = true;
        this.c = i;
        return this;
    }

    public dx$a c(String str) {
        this.f237e = true;
        this.f232c = str;
        return this;
    }

    /* renamed from: c  reason: collision with other method in class */
    public String m2061c() {
        return this.f232c;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2062c() {
        return this.f233c;
    }

    public int d() {
        return this.b;
    }

    public dx$a d(int i) {
        this.j = true;
        this.d = i;
        return this;
    }

    public dx$a d(String str) {
        this.f238f = true;
        this.f234d = str;
        return this;
    }

    /* renamed from: d  reason: collision with other method in class */
    public String m2063d() {
        return this.f234d;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m2064d() {
        return this.f235d;
    }

    public int e() {
        return this.c;
    }

    public dx$a e(String str) {
        this.g = true;
        this.f236e = str;
        return this;
    }

    /* renamed from: e  reason: collision with other method in class */
    public String m2065e() {
        return this.f236e;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m2066e() {
        return this.f237e;
    }

    public int f() {
        return this.d;
    }

    public dx$a f(String str) {
        this.k = true;
        this.f = str;
        return this;
    }

    /* renamed from: f  reason: collision with other method in class */
    public String m2067f() {
        return this.f;
    }

    /* renamed from: f  reason: collision with other method in class */
    public boolean m2068f() {
        return this.f238f;
    }

    public boolean g() {
        return this.g;
    }

    public boolean h() {
        return this.h;
    }

    public boolean i() {
        return this.i;
    }

    public boolean j() {
        return this.j;
    }

    public boolean k() {
        return this.k;
    }
}
