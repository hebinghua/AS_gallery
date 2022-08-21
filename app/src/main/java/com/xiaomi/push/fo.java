package com.xiaomi.push;

import android.os.Build;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.zip.Adler32;

/* loaded from: classes3.dex */
public class fo {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public fs f368a;

    /* renamed from: a  reason: collision with other field name */
    public OutputStream f369a;

    /* renamed from: a  reason: collision with other field name */
    public byte[] f372a;
    public int b;

    /* renamed from: a  reason: collision with other field name */
    public ByteBuffer f370a = ByteBuffer.allocate(2048);

    /* renamed from: b  reason: collision with other field name */
    public ByteBuffer f373b = ByteBuffer.allocate(4);

    /* renamed from: a  reason: collision with other field name */
    public Adler32 f371a = new Adler32();

    public fo(OutputStream outputStream, fs fsVar) {
        this.f369a = new BufferedOutputStream(outputStream);
        this.f368a = fsVar;
        TimeZone timeZone = TimeZone.getDefault();
        this.a = timeZone.getRawOffset() / 3600000;
        this.b = timeZone.useDaylightTime() ? 1 : 0;
    }

    public int a(fl flVar) {
        int c = flVar.c();
        if (c > 32768) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Blob size=" + c + " should be less than 32768 Drop blob chid=" + flVar.a() + " id=" + flVar.e());
            return 0;
        }
        this.f370a.clear();
        int i = c + 8 + 4;
        if (i > this.f370a.capacity() || this.f370a.capacity() > 4096) {
            this.f370a = ByteBuffer.allocate(i);
        }
        this.f370a.putShort((short) -15618);
        this.f370a.putShort((short) 5);
        this.f370a.putInt(c);
        int position = this.f370a.position();
        this.f370a = flVar.mo2159a(this.f370a);
        if (!"CONN".equals(flVar.m2158a())) {
            if (this.f372a == null) {
                this.f372a = this.f368a.mo2190a();
            }
            com.xiaomi.push.service.bp.a(this.f372a, this.f370a.array(), true, position, c);
        }
        this.f371a.reset();
        this.f371a.update(this.f370a.array(), 0, this.f370a.position());
        this.f373b.putInt(0, (int) this.f371a.getValue());
        this.f369a.write(this.f370a.array(), 0, this.f370a.position());
        this.f369a.write(this.f373b.array(), 0, 4);
        this.f369a.flush();
        int position2 = this.f370a.position() + 4;
        com.xiaomi.channel.commonutils.logger.b.c("[Slim] Wrote {cmd=" + flVar.m2158a() + ";chid=" + flVar.a() + ";len=" + position2 + "}");
        return position2;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.xiaomi.push.e, com.xiaomi.push.dx$e] */
    public void a() {
        ?? r0 = new e() { // from class: com.xiaomi.push.dx$e

            /* renamed from: a  reason: collision with other field name */
            public boolean f256a;

            /* renamed from: b  reason: collision with other field name */
            public boolean f258b;

            /* renamed from: c  reason: collision with other field name */
            public boolean f260c;

            /* renamed from: d  reason: collision with other field name */
            public boolean f262d;

            /* renamed from: e  reason: collision with other field name */
            public boolean f263e;

            /* renamed from: f  reason: collision with other field name */
            public boolean f264f;
            public boolean g;
            public boolean h;
            public boolean i;
            public boolean j;
            public int a = 0;

            /* renamed from: a  reason: collision with other field name */
            public String f255a = "";

            /* renamed from: b  reason: collision with other field name */
            public String f257b = "";

            /* renamed from: c  reason: collision with other field name */
            public String f259c = "";
            public int b = 0;

            /* renamed from: d  reason: collision with other field name */
            public String f261d = "";
            public String e = "";
            public String f = "";

            /* renamed from: a  reason: collision with other field name */
            public dx$b f254a = null;
            public int c = 0;
            public int d = -1;

            @Override // com.xiaomi.push.e
            /* renamed from: a */
            public int mo2116a() {
                if (this.d < 0) {
                    mo2118b();
                }
                return this.d;
            }

            @Override // com.xiaomi.push.e
            /* renamed from: a  reason: collision with other method in class */
            public dx$b mo2116a() {
                return this.f254a;
            }

            public dx$e a(int i) {
                this.f256a = true;
                this.a = i;
                return this;
            }

            @Override // com.xiaomi.push.e
            /* renamed from: a */
            public dx$e mo2115a(b bVar) {
                while (true) {
                    int m1945a = bVar.m1945a();
                    switch (m1945a) {
                        case 0:
                            return this;
                        case 8:
                            a(bVar.c());
                            break;
                        case 18:
                            a(bVar.m1948a());
                            break;
                        case 26:
                            b(bVar.m1948a());
                            break;
                        case 34:
                            c(bVar.m1948a());
                            break;
                        case 40:
                            b(bVar.b());
                            break;
                        case 50:
                            d(bVar.m1948a());
                            break;
                        case 58:
                            e(bVar.m1948a());
                            break;
                        case 66:
                            f(bVar.m1948a());
                            break;
                        case 74:
                            dx$b dx_b = new dx$b();
                            bVar.a(dx_b);
                            a(dx_b);
                            break;
                        case 80:
                            c(bVar.b());
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

            public dx$e a(dx$b dx_b) {
                Objects.requireNonNull(dx_b);
                this.i = true;
                this.f254a = dx_b;
                return this;
            }

            public dx$e a(String str) {
                this.f258b = true;
                this.f255a = str;
                return this;
            }

            @Override // com.xiaomi.push.e
            /* renamed from: a */
            public String mo2116a() {
                return this.f255a;
            }

            @Override // com.xiaomi.push.e
            public void a(c cVar) {
                if (mo2116a()) {
                    cVar.m2005b(1, c());
                }
                if (mo2118b()) {
                    cVar.m1996a(2, mo2116a());
                }
                if (m2091c()) {
                    cVar.m1996a(3, mo2118b());
                }
                if (m2093d()) {
                    cVar.m1996a(4, m2090c());
                }
                if (m2095e()) {
                    cVar.m1992a(5, d());
                }
                if (m2096f()) {
                    cVar.m1996a(6, m2092d());
                }
                if (g()) {
                    cVar.m1996a(7, m2094e());
                }
                if (h()) {
                    cVar.m1996a(8, f());
                }
                if (i()) {
                    cVar.m1995a(9, (e) mo2116a());
                }
                if (j()) {
                    cVar.m1992a(10, e());
                }
            }

            @Override // com.xiaomi.push.e
            /* renamed from: a  reason: collision with other method in class */
            public boolean mo2116a() {
                return this.f256a;
            }

            @Override // com.xiaomi.push.e
            /* renamed from: b */
            public int mo2118b() {
                int i = 0;
                if (mo2116a()) {
                    i = 0 + c.b(1, c());
                }
                if (mo2118b()) {
                    i += c.a(2, mo2116a());
                }
                if (m2091c()) {
                    i += c.a(3, mo2118b());
                }
                if (m2093d()) {
                    i += c.a(4, m2090c());
                }
                if (m2095e()) {
                    i += c.a(5, d());
                }
                if (m2096f()) {
                    i += c.a(6, m2092d());
                }
                if (g()) {
                    i += c.a(7, m2094e());
                }
                if (h()) {
                    i += c.a(8, f());
                }
                if (i()) {
                    i += c.a(9, (e) mo2116a());
                }
                if (j()) {
                    i += c.a(10, e());
                }
                this.d = i;
                return i;
            }

            public dx$e b(int i) {
                this.f263e = true;
                this.b = i;
                return this;
            }

            public dx$e b(String str) {
                this.f260c = true;
                this.f257b = str;
                return this;
            }

            @Override // com.xiaomi.push.e
            /* renamed from: b  reason: collision with other method in class */
            public String mo2118b() {
                return this.f257b;
            }

            @Override // com.xiaomi.push.e
            /* renamed from: b */
            public boolean mo2118b() {
                return this.f258b;
            }

            public int c() {
                return this.a;
            }

            public dx$e c(int i) {
                this.j = true;
                this.c = i;
                return this;
            }

            public dx$e c(String str) {
                this.f262d = true;
                this.f259c = str;
                return this;
            }

            /* renamed from: c  reason: collision with other method in class */
            public String m2090c() {
                return this.f259c;
            }

            /* renamed from: c  reason: collision with other method in class */
            public boolean m2091c() {
                return this.f260c;
            }

            public int d() {
                return this.b;
            }

            public dx$e d(String str) {
                this.f264f = true;
                this.f261d = str;
                return this;
            }

            /* renamed from: d  reason: collision with other method in class */
            public String m2092d() {
                return this.f261d;
            }

            /* renamed from: d  reason: collision with other method in class */
            public boolean m2093d() {
                return this.f262d;
            }

            public int e() {
                return this.c;
            }

            public dx$e e(String str) {
                this.g = true;
                this.e = str;
                return this;
            }

            /* renamed from: e  reason: collision with other method in class */
            public String m2094e() {
                return this.e;
            }

            /* renamed from: e  reason: collision with other method in class */
            public boolean m2095e() {
                return this.f263e;
            }

            public dx$e f(String str) {
                this.h = true;
                this.f = str;
                return this;
            }

            public String f() {
                return this.f;
            }

            /* renamed from: f  reason: collision with other method in class */
            public boolean m2096f() {
                return this.f264f;
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
        };
        r0.a(106);
        String str = Build.MODEL;
        r0.a(str);
        r0.b(v.m2552a());
        r0.c(com.xiaomi.push.service.bv.m2505a());
        r0.b(48);
        r0.d(this.f368a.m2181b());
        r0.e(this.f368a.mo2190a());
        r0.f(Locale.getDefault().toString());
        int i = Build.VERSION.SDK_INT;
        r0.c(i);
        byte[] m2187a = this.f368a.m2176a().m2187a();
        if (m2187a != null) {
            r0.a(dx$b.a(m2187a));
        }
        fl flVar = new fl();
        flVar.a(0);
        flVar.a("CONN", (String) null);
        flVar.a(0L, "xiaomi.com", null);
        flVar.a(r0.a(), (String) null);
        a(flVar);
        com.xiaomi.channel.commonutils.logger.b.m1859a("[slim] open conn: andver=" + i + " sdk=48 tz=" + this.a + ":" + this.b + " Model=" + str + " os=" + Build.VERSION.INCREMENTAL);
    }

    public void b() {
        fl flVar = new fl();
        flVar.a("CLOSE", (String) null);
        a(flVar);
        this.f369a.close();
    }
}
