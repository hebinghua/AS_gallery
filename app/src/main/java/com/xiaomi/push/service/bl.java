package com.xiaomi.push.service;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import com.xiaomi.push.cv;
import com.xiaomi.push.dw$a;
import com.xiaomi.push.dx$b;
import com.xiaomi.push.ez;
import com.xiaomi.push.fh;
import com.xiaomi.push.fj;
import com.xiaomi.push.fw;
import com.xiaomi.push.gy;
import com.xiaomi.push.service.bv;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class bl extends bv.a implements cv.a {
    public long a;

    /* renamed from: a  reason: collision with other field name */
    public XMPushService f922a;

    /* loaded from: classes3.dex */
    public static class a implements cv.b {
        @Override // com.xiaomi.push.cv.b
        public String a(String str) {
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            buildUpon.appendQueryParameter("sdkver", String.valueOf(48));
            buildUpon.appendQueryParameter("osver", String.valueOf(Build.VERSION.SDK_INT));
            buildUpon.appendQueryParameter(com.xiaomi.stat.d.l, gy.a(Build.MODEL + ":" + Build.VERSION.INCREMENTAL));
            buildUpon.appendQueryParameter("mi", String.valueOf(com.xiaomi.push.v.a()));
            String builder = buildUpon.toString();
            com.xiaomi.channel.commonutils.logger.b.c("fetch bucket from : " + builder);
            URL url = new URL(builder);
            int port = url.getPort() == -1 ? 80 : url.getPort();
            try {
                long currentTimeMillis = System.currentTimeMillis();
                String a = com.xiaomi.push.bj.a(com.xiaomi.push.v.m2551a(), url);
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                fj.a(url.getHost() + ":" + port, (int) currentTimeMillis2, null);
                return a;
            } catch (IOException e) {
                fj.a(url.getHost() + ":" + port, -1, e);
                throw e;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class b extends cv {
        public b(Context context, com.xiaomi.push.cu cuVar, cv.b bVar, String str) {
            super(context, cuVar, bVar, str);
        }

        @Override // com.xiaomi.push.cv
        public String a(ArrayList<String> arrayList, String str, String str2, boolean z) {
            try {
                if (fh.m2151a().m2156a()) {
                    str2 = bv.m2505a();
                }
                return super.a(arrayList, str, str2, z);
            } catch (IOException e) {
                fj.a(0, ez.GSLB_ERR.a(), 1, null, com.xiaomi.push.bj.c(cv.a) ? 1 : 0);
                throw e;
            }
        }
    }

    public bl(XMPushService xMPushService) {
        this.f922a = xMPushService;
    }

    public static void a(XMPushService xMPushService) {
        bl blVar = new bl(xMPushService);
        bv.a().a(blVar);
        synchronized (cv.class) {
            cv.a(blVar);
            cv.a(xMPushService, null, new a(), "0", "push", "2.2");
        }
    }

    @Override // com.xiaomi.push.cv.a
    public cv a(Context context, com.xiaomi.push.cu cuVar, cv.b bVar, String str) {
        return new b(context, cuVar, bVar, str);
    }

    @Override // com.xiaomi.push.service.bv.a
    public void a(dw$a dw_a) {
    }

    @Override // com.xiaomi.push.service.bv.a
    public void a(dx$b dx_b) {
        com.xiaomi.push.cr b2;
        if (!dx_b.mo2118b() || !dx_b.mo2116a() || System.currentTimeMillis() - this.a <= 3600000) {
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("fetch bucket :" + dx_b.mo2116a());
        this.a = System.currentTimeMillis();
        cv a2 = cv.a();
        a2.m2028a();
        a2.m2031b();
        fw a3 = this.f922a.a();
        if (a3 == null || (b2 = a2.b(a3.m2176a().c())) == null) {
            return;
        }
        ArrayList<String> m2016a = b2.m2016a();
        boolean z = true;
        Iterator<String> it = m2016a.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().equals(a3.m2177a())) {
                    z = false;
                    break;
                }
            } else {
                break;
            }
        }
        if (!z || m2016a.isEmpty()) {
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("bucket changed, force reconnect");
        this.f922a.a(0, (Exception) null);
        this.f922a.a(false);
    }
}
