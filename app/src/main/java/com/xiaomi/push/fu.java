package com.xiaomi.push;

import com.xiaomi.push.fw;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/* loaded from: classes3.dex */
public class fu implements gi {
    public static boolean a = false;

    /* renamed from: a  reason: collision with other field name */
    public fw f382a;

    /* renamed from: a  reason: collision with other field name */
    public SimpleDateFormat f385a = new SimpleDateFormat("hh:mm:ss aaa");

    /* renamed from: a  reason: collision with other field name */
    public a f381a = null;
    public a b = null;

    /* renamed from: a  reason: collision with other field name */
    public fz f383a = null;

    /* renamed from: a  reason: collision with other field name */
    public final String f384a = "[Slim] ";

    /* loaded from: classes3.dex */
    public class a implements gb, gj {

        /* renamed from: a  reason: collision with other field name */
        public String f386a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f387a;

        public a(boolean z) {
            this.f387a = true;
            this.f387a = z;
            this.f386a = z ? " RCV " : " Sent ";
        }

        @Override // com.xiaomi.push.gb
        public void a(fl flVar) {
            StringBuilder sb;
            String str;
            if (fu.a) {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(fu.this.f385a.format(new Date()));
                sb.append(this.f386a);
                str = flVar.toString();
            } else {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(fu.this.f385a.format(new Date()));
                sb.append(this.f386a);
                sb.append(" Blob [");
                sb.append(flVar.m2158a());
                sb.append(",");
                sb.append(flVar.a());
                sb.append(",");
                sb.append(com.xiaomi.push.service.bd.a(flVar.e()));
                str = "]";
            }
            sb.append(str);
            com.xiaomi.channel.commonutils.logger.b.c(sb.toString());
            if (flVar == null || flVar.a() != 99999) {
                return;
            }
            String m2158a = flVar.m2158a();
            fl flVar2 = null;
            if (!this.f387a) {
                if ("BIND".equals(m2158a)) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("build binded result for loopback.");
                    dx$d dx_d = new dx$d();
                    dx_d.a(true);
                    dx_d.c("login success.");
                    dx_d.b("success");
                    dx_d.a("success");
                    fl flVar3 = new fl();
                    flVar3.a(dx_d.a(), (String) null);
                    flVar3.a((short) 2);
                    flVar3.a(99999);
                    flVar3.a("BIND", (String) null);
                    flVar3.a(flVar.e());
                    flVar3.b((String) null);
                    flVar3.c(flVar.g());
                    flVar2 = flVar3;
                } else if (!"UBND".equals(m2158a) && "SECMSG".equals(m2158a)) {
                    fl flVar4 = new fl();
                    flVar4.a(99999);
                    flVar4.a("SECMSG", (String) null);
                    flVar4.c(flVar.g());
                    flVar4.a(flVar.e());
                    flVar4.a(flVar.m2160a());
                    flVar4.b(flVar.f());
                    flVar4.a(flVar.m2163a(com.xiaomi.push.service.bg.a().a(String.valueOf(99999), flVar.g()).h), (String) null);
                    flVar2 = flVar4;
                }
            }
            if (flVar2 == null) {
                return;
            }
            for (Map.Entry<gb, fw.a> entry : fu.this.f382a.m2178a().entrySet()) {
                if (fu.this.f381a != entry.getKey()) {
                    entry.getValue().a(flVar2);
                }
            }
        }

        @Override // com.xiaomi.push.gb, com.xiaomi.push.gj
        /* renamed from: a */
        public void mo2175a(gn gnVar) {
            StringBuilder sb;
            String str;
            if (fu.a) {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(fu.this.f385a.format(new Date()));
                sb.append(this.f386a);
                sb.append(" PKT ");
                str = gnVar.m2199a();
            } else {
                sb = new StringBuilder();
                sb.append("[Slim] ");
                sb.append(fu.this.f385a.format(new Date()));
                sb.append(this.f386a);
                sb.append(" PKT [");
                sb.append(gnVar.k());
                sb.append(",");
                sb.append(gnVar.j());
                str = "]";
            }
            sb.append(str);
            com.xiaomi.channel.commonutils.logger.b.c(sb.toString());
        }

        @Override // com.xiaomi.push.gb, com.xiaomi.push.gj
        /* renamed from: a  reason: collision with other method in class */
        public boolean mo2175a(gn gnVar) {
            return true;
        }
    }

    public fu(fw fwVar) {
        this.f382a = null;
        this.f382a = fwVar;
        a();
    }

    public final void a() {
        this.f381a = new a(true);
        this.b = new a(false);
        fw fwVar = this.f382a;
        a aVar = this.f381a;
        fwVar.a(aVar, aVar);
        fw fwVar2 = this.f382a;
        a aVar2 = this.b;
        fwVar2.b(aVar2, aVar2);
        this.f383a = new fv(this);
    }
}
