package com.xiaomi.push;

import android.text.TextUtils;
import com.xiaomi.push.service.bg;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class fk {
    /* JADX WARN: Type inference failed for: r0v0, types: [com.xiaomi.push.e, com.xiaomi.push.dx$c] */
    public static void a(bg.b bVar, String str, fw fwVar) {
        String a;
        ?? r0 = new e() { // from class: com.xiaomi.push.dx$c

            /* renamed from: a  reason: collision with other field name */
            public boolean f244a;

            /* renamed from: b  reason: collision with other field name */
            public boolean f245b;

            /* renamed from: c  reason: collision with other field name */
            public boolean f246c;

            /* renamed from: d  reason: collision with other field name */
            public boolean f247d;

            /* renamed from: e  reason: collision with other field name */
            public boolean f248e;

            /* renamed from: f  reason: collision with other field name */
            public boolean f249f;

            /* renamed from: a  reason: collision with other field name */
            public String f243a = "";
            public String b = "";
            public String c = "";
            public String d = "";
            public String e = "";
            public String f = "";
            public int a = -1;

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
            public dx$c mo2115a(b bVar2) {
                while (true) {
                    int m1945a = bVar2.m1945a();
                    if (m1945a != 0) {
                        if (m1945a == 10) {
                            a(bVar2.m1948a());
                        } else if (m1945a == 18) {
                            b(bVar2.m1948a());
                        } else if (m1945a == 26) {
                            c(bVar2.m1948a());
                        } else if (m1945a == 34) {
                            d(bVar2.m1948a());
                        } else if (m1945a == 42) {
                            e(bVar2.m1948a());
                        } else if (m1945a == 50) {
                            f(bVar2.m1948a());
                        } else if (!a(bVar2, m1945a)) {
                            return this;
                        }
                    } else {
                        return this;
                    }
                }
            }

            public dx$c a(String str2) {
                this.f244a = true;
                this.f243a = str2;
                return this;
            }

            @Override // com.xiaomi.push.e
            /* renamed from: a  reason: collision with other method in class */
            public String mo2116a() {
                return this.f243a;
            }

            @Override // com.xiaomi.push.e
            public void a(c cVar) {
                if (mo2116a()) {
                    cVar.m1996a(1, mo2116a());
                }
                if (mo2118b()) {
                    cVar.m1996a(2, mo2118b());
                }
                if (m2078c()) {
                    cVar.m1996a(3, c());
                }
                if (m2079d()) {
                    cVar.m1996a(4, d());
                }
                if (m2080e()) {
                    cVar.m1996a(5, e());
                }
                if (m2081f()) {
                    cVar.m1996a(6, f());
                }
            }

            @Override // com.xiaomi.push.e
            /* renamed from: a */
            public boolean mo2116a() {
                return this.f244a;
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
                if (m2078c()) {
                    i += c.a(3, c());
                }
                if (m2079d()) {
                    i += c.a(4, d());
                }
                if (m2080e()) {
                    i += c.a(5, e());
                }
                if (m2081f()) {
                    i += c.a(6, f());
                }
                this.a = i;
                return i;
            }

            public dx$c b(String str2) {
                this.f245b = true;
                this.b = str2;
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
                return this.f245b;
            }

            public dx$c c(String str2) {
                this.f246c = true;
                this.c = str2;
                return this;
            }

            public String c() {
                return this.c;
            }

            /* renamed from: c  reason: collision with other method in class */
            public boolean m2078c() {
                return this.f246c;
            }

            public dx$c d(String str2) {
                this.f247d = true;
                this.d = str2;
                return this;
            }

            public String d() {
                return this.d;
            }

            /* renamed from: d  reason: collision with other method in class */
            public boolean m2079d() {
                return this.f247d;
            }

            public dx$c e(String str2) {
                this.f248e = true;
                this.e = str2;
                return this;
            }

            public String e() {
                return this.e;
            }

            /* renamed from: e  reason: collision with other method in class */
            public boolean m2080e() {
                return this.f248e;
            }

            public dx$c f(String str2) {
                this.f249f = true;
                this.f = str2;
                return this;
            }

            public String f() {
                return this.f;
            }

            /* renamed from: f  reason: collision with other method in class */
            public boolean m2081f() {
                return this.f249f;
            }
        };
        if (!TextUtils.isEmpty(bVar.c)) {
            r0.a(bVar.c);
        }
        if (!TextUtils.isEmpty(bVar.e)) {
            r0.d(bVar.e);
        }
        if (!TextUtils.isEmpty(bVar.f)) {
            r0.e(bVar.f);
        }
        r0.b(bVar.f915a ? "1" : "0");
        if (!TextUtils.isEmpty(bVar.d)) {
            r0.c(bVar.d);
        } else {
            r0.c("XIAOMI-SASL");
        }
        fl flVar = new fl();
        flVar.c(bVar.f916b);
        flVar.a(Integer.parseInt(bVar.g));
        flVar.b(bVar.f913a);
        flVar.a("BIND", (String) null);
        flVar.a(flVar.e());
        com.xiaomi.channel.commonutils.logger.b.m1859a("[Slim]: bind id=" + flVar.e());
        HashMap hashMap = new HashMap();
        hashMap.put("challenge", str);
        hashMap.put("token", bVar.c);
        hashMap.put("chid", bVar.g);
        hashMap.put("from", bVar.f916b);
        hashMap.put("id", flVar.e());
        hashMap.put("to", "xiaomi.com");
        if (bVar.f915a) {
            hashMap.put("kick", "1");
        } else {
            hashMap.put("kick", "0");
        }
        if (!TextUtils.isEmpty(bVar.e)) {
            hashMap.put("client_attrs", bVar.e);
        } else {
            hashMap.put("client_attrs", "");
        }
        if (!TextUtils.isEmpty(bVar.f)) {
            hashMap.put("cloud_attrs", bVar.f);
        } else {
            hashMap.put("cloud_attrs", "");
        }
        if (bVar.d.equals("XIAOMI-PASS") || bVar.d.equals("XMPUSH-PASS")) {
            a = bn.a(bVar.d, null, hashMap, bVar.h);
        } else {
            bVar.d.equals("XIAOMI-SASL");
            a = null;
        }
        r0.f(a);
        flVar.a(r0.a(), (String) null);
        fwVar.b(flVar);
    }

    public static void a(String str, String str2, fw fwVar) {
        fl flVar = new fl();
        flVar.c(str2);
        flVar.a(Integer.parseInt(str));
        flVar.a("UBND", (String) null);
        fwVar.b(flVar);
    }
}
