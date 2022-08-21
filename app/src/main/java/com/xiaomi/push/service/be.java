package com.xiaomi.push.service;

import android.text.TextUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.push.cv;
import com.xiaomi.push.df;
import com.xiaomi.push.dx$b;
import com.xiaomi.push.dx$d;
import com.xiaomi.push.dx$g;
import com.xiaomi.push.dx$h;
import com.xiaomi.push.dx$i;
import com.xiaomi.push.dx$j;
import com.xiaomi.push.dx$k;
import com.xiaomi.push.ez;
import com.xiaomi.push.fj;
import com.xiaomi.push.fl;
import com.xiaomi.push.fx;
import com.xiaomi.push.gk;
import com.xiaomi.push.gl;
import com.xiaomi.push.gm;
import com.xiaomi.push.gn;
import com.xiaomi.push.hb;
import com.xiaomi.push.service.bg;
import java.util.Date;

/* loaded from: classes3.dex */
public class be {
    public XMPushService a;

    public be(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public void a(fl flVar) {
        if (5 != flVar.a()) {
            c(flVar);
        }
        try {
            b(flVar);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("handle Blob chid = " + flVar.a() + " cmd = " + flVar.m2158a() + " packetid = " + flVar.e() + " failure ", e);
        }
    }

    public final void a(gk gkVar) {
        String c = gkVar.c();
        if (!TextUtils.isEmpty(c)) {
            String[] split = c.split(";");
            com.xiaomi.push.cr a = cv.a().a(fx.a(), false);
            if (a == null || split.length <= 0) {
                return;
            }
            a.a(split);
            this.a.a(20, (Exception) null);
            this.a.a(true);
        }
    }

    public void a(gn gnVar) {
        if (!"5".equals(gnVar.k())) {
            b(gnVar);
        }
        String k = gnVar.k();
        if (TextUtils.isEmpty(k)) {
            k = "1";
            gnVar.l(k);
        }
        if (k.equals("0")) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Received wrong packet with chid = 0 : " + gnVar.m2199a());
        }
        if (gnVar instanceof gl) {
            gk a = gnVar.a("kick");
            if (a != null) {
                String l = gnVar.l();
                String a2 = a.a(nexExportFormat.TAG_FORMAT_TYPE);
                String a3 = a.a("reason");
                com.xiaomi.channel.commonutils.logger.b.m1859a("kicked by server, chid=" + k + " res=" + bg.b.a(l) + " type=" + a2 + " reason=" + a3);
                if (!"wait".equals(a2)) {
                    this.a.a(k, l, 3, a3, a2);
                    bg.a().m2494a(k, l);
                    return;
                }
                bg.b a4 = bg.a().a(k, l);
                if (a4 == null) {
                    return;
                }
                this.a.a(a4);
                a4.a(bg.c.unbind, 3, 0, a3, a2);
                return;
            }
        } else if (gnVar instanceof gm) {
            gm gmVar = (gm) gnVar;
            if ("redir".equals(gmVar.b())) {
                gk a5 = gmVar.a("hosts");
                if (a5 == null) {
                    return;
                }
                a(a5);
                return;
            }
        }
        this.a.b().a(this.a, k, gnVar);
    }

    public void b(fl flVar) {
        StringBuilder sb;
        String mo2116a;
        String str;
        bg.c cVar;
        int i;
        int i2;
        String m2158a = flVar.m2158a();
        if (flVar.a() != 0) {
            String num = Integer.toString(flVar.a());
            if (!"SECMSG".equals(flVar.m2158a())) {
                if (!"BIND".equals(m2158a)) {
                    if (!"KICK".equals(m2158a)) {
                        return;
                    }
                    dx$g a = dx$g.a(flVar.m2162a());
                    String g = flVar.g();
                    String mo2116a2 = a.mo2116a();
                    String mo2118b = a.mo2118b();
                    com.xiaomi.channel.commonutils.logger.b.m1859a("kicked by server, chid=" + num + " res= " + bg.b.a(g) + " type=" + mo2116a2 + " reason=" + mo2118b);
                    if (!"wait".equals(mo2116a2)) {
                        this.a.a(num, g, 3, mo2118b, mo2116a2);
                        bg.a().m2494a(num, g);
                        return;
                    }
                    bg.b a2 = bg.a().a(num, g);
                    if (a2 == null) {
                        return;
                    }
                    this.a.a(a2);
                    a2.a(bg.c.unbind, 3, 0, mo2118b, mo2116a2);
                    return;
                }
                dx$d a3 = dx$d.a(flVar.m2162a());
                String g2 = flVar.g();
                bg.b a4 = bg.a().a(num, g2);
                if (a4 == null) {
                    return;
                }
                if (a3.mo2116a()) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("SMACK: channel bind succeeded, chid=" + flVar.a());
                    a4.a(bg.c.binded, 1, 0, (String) null, (String) null);
                    return;
                }
                String mo2116a3 = a3.mo2116a();
                if ("auth".equals(mo2116a3)) {
                    if ("invalid-sig".equals(a3.mo2118b())) {
                        com.xiaomi.channel.commonutils.logger.b.m1859a("SMACK: bind error invalid-sig token = " + a4.c + " sec = " + a4.h);
                        fj.a(0, ez.BIND_INVALID_SIG.a(), 1, null, 0);
                    }
                    cVar = bg.c.unbind;
                    i = 1;
                    i2 = 5;
                } else if (!"cancel".equals(mo2116a3)) {
                    if ("wait".equals(mo2116a3)) {
                        this.a.a(a4);
                        a4.a(bg.c.unbind, 1, 7, a3.mo2118b(), mo2116a3);
                    }
                    str = "SMACK: channel bind failed, chid=" + num + " reason=" + a3.mo2118b();
                    com.xiaomi.channel.commonutils.logger.b.m1859a(str);
                } else {
                    cVar = bg.c.unbind;
                    i = 1;
                    i2 = 7;
                }
                a4.a(cVar, i, i2, a3.mo2118b(), mo2116a3);
                bg.a().m2494a(num, g2);
                str = "SMACK: channel bind failed, chid=" + num + " reason=" + a3.mo2118b();
                com.xiaomi.channel.commonutils.logger.b.m1859a(str);
            } else if (!flVar.m2161a()) {
                this.a.b().a(this.a, num, flVar);
                return;
            } else {
                sb = new StringBuilder();
                sb.append("Recv SECMSG errCode = ");
                sb.append(flVar.b());
                sb.append(" errStr = ");
                mo2116a = flVar.m2165c();
            }
        } else if ("PING".equals(m2158a)) {
            byte[] m2162a = flVar.m2162a();
            if (m2162a != null && m2162a.length > 0) {
                dx$j a5 = dx$j.a(m2162a);
                if (a5.mo2118b()) {
                    bv.a().a(a5.mo2116a());
                }
            }
            if (!com.xiaomi.stat.c.c.a.equals(this.a.getPackageName())) {
                this.a.m2420a();
            }
            if ("1".equals(flVar.e())) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("received a server ping");
            } else {
                fj.b();
            }
            this.a.m2426b();
            return;
        } else if ("SYNC".equals(m2158a)) {
            if ("CONF".equals(flVar.m2164b())) {
                bv.a().a(dx$b.a(flVar.m2162a()));
                return;
            } else if (TextUtils.equals("U", flVar.m2164b())) {
                dx$k a6 = dx$k.a(flVar.m2162a());
                df.a(this.a).a(a6.mo2116a(), a6.mo2118b(), new Date(a6.mo2116a()), new Date(a6.mo2118b()), a6.c() * 1024, a6.e());
                fl flVar2 = new fl();
                flVar2.a(0);
                flVar2.a(flVar.m2158a(), "UCA");
                flVar2.a(flVar.e());
                XMPushService xMPushService = this.a;
                xMPushService.a(new bt(xMPushService, flVar2));
                return;
            } else if (!TextUtils.equals("P", flVar.m2164b())) {
                return;
            } else {
                dx$i a7 = dx$i.a(flVar.m2162a());
                fl flVar3 = new fl();
                flVar3.a(0);
                flVar3.a(flVar.m2158a(), "PCA");
                flVar3.a(flVar.e());
                dx$i dx_i = new dx$i();
                if (a7.mo2116a()) {
                    dx_i.a(a7.mo2116a());
                }
                flVar3.a(dx_i.a(), (String) null);
                XMPushService xMPushService2 = this.a;
                xMPushService2.a(new bt(xMPushService2, flVar3));
                sb = new StringBuilder();
                sb.append("ACK msgP: id = ");
                mo2116a = flVar.e();
            }
        } else if (!"NOTIFY".equals(flVar.m2158a())) {
            return;
        } else {
            dx$h a8 = dx$h.a(flVar.m2162a());
            sb = new StringBuilder();
            sb.append("notify by server err = ");
            sb.append(a8.c());
            sb.append(" desc = ");
            mo2116a = a8.mo2116a();
        }
        sb.append(mo2116a);
        str = sb.toString();
        com.xiaomi.channel.commonutils.logger.b.m1859a(str);
    }

    public final void b(gn gnVar) {
        bg.b a;
        String l = gnVar.l();
        String k = gnVar.k();
        if (TextUtils.isEmpty(l) || TextUtils.isEmpty(k) || (a = bg.a().a(k, l)) == null) {
            return;
        }
        hb.a(this.a, a.f913a, hb.a(gnVar.m2199a()), true, true, System.currentTimeMillis());
    }

    public final void c(fl flVar) {
        bg.b a;
        String g = flVar.g();
        String num = Integer.toString(flVar.a());
        if (TextUtils.isEmpty(g) || TextUtils.isEmpty(num) || (a = bg.a().a(num, g)) == null) {
            return;
        }
        hb.a(this.a, a.f913a, flVar.c(), true, true, System.currentTimeMillis());
    }
}
