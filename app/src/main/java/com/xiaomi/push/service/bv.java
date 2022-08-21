package com.xiaomi.push.service;

import android.content.SharedPreferences;
import com.xiaomi.push.ao;
import com.xiaomi.push.dw$a;
import com.xiaomi.push.dx$b;
import com.xiaomi.push.gz;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class bv {
    public static bv a = new bv();

    /* renamed from: a  reason: collision with other field name */
    public static String f929a;

    /* renamed from: a  reason: collision with other field name */
    public ao.b f930a;

    /* renamed from: a  reason: collision with other field name */
    public dw$a f931a;

    /* renamed from: a  reason: collision with other field name */
    public List<a> f932a = new ArrayList();

    /* loaded from: classes3.dex */
    public static abstract class a {
        public void a(dw$a dw_a) {
        }

        public void a(dx$b dx_b) {
        }
    }

    public static bv a() {
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized String m2505a() {
        String str;
        synchronized (bv.class) {
            if (f929a == null) {
                SharedPreferences sharedPreferences = com.xiaomi.push.v.m2551a().getSharedPreferences("XMPushServiceConfig", 0);
                String string = sharedPreferences.getString("DeviceUUID", null);
                f929a = string;
                if (string == null) {
                    String a2 = com.xiaomi.push.j.a(com.xiaomi.push.v.m2551a(), false);
                    f929a = a2;
                    if (a2 != null) {
                        sharedPreferences.edit().putString("DeviceUUID", f929a).commit();
                    }
                }
            }
            str = f929a;
        }
        return str;
    }

    /* renamed from: a  reason: collision with other method in class */
    public int m2508a() {
        b();
        dw$a dw_a = this.f931a;
        if (dw_a != null) {
            return dw_a.c();
        }
        return 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public dw$a m2509a() {
        b();
        return this.f931a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m2510a() {
        this.f932a.clear();
    }

    public void a(dx$b dx_b) {
        a[] aVarArr;
        if (dx_b.m2073d() && dx_b.d() > m2508a()) {
            c();
        }
        synchronized (this) {
            List<a> list = this.f932a;
            aVarArr = (a[]) list.toArray(new a[list.size()]);
        }
        for (a aVar : aVarArr) {
            aVar.a(dx_b);
        }
    }

    public synchronized void a(a aVar) {
        this.f932a.add(aVar);
    }

    public final void b() {
        if (this.f931a == null) {
            d();
        }
    }

    public final void c() {
        if (this.f930a != null) {
            return;
        }
        bw bwVar = new bw(this);
        this.f930a = bwVar;
        gz.a(bwVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void d() {
        /*
            r4 = this;
            r0 = 0
            android.content.Context r1 = com.xiaomi.push.v.m2551a()     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L27
            java.lang.String r2 = "XMCloudCfg"
            java.io.FileInputStream r1 = r1.openFileInput(r2)     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L27
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L27
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L23 java.lang.Exception -> L27
            com.xiaomi.push.b r0 = com.xiaomi.push.b.a(r2)     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4f
            com.xiaomi.push.dw$a r0 = com.xiaomi.push.dw$a.b(r0)     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4f
            r4.f931a = r0     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4f
            r2.close()     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4f
        L1d:
            com.xiaomi.push.ab.a(r2)
            goto L43
        L21:
            r0 = move-exception
            goto L2a
        L23:
            r1 = move-exception
            r2 = r0
            r0 = r1
            goto L50
        L27:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L2a:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4f
            r1.<init>()     // Catch: java.lang.Throwable -> L4f
            java.lang.String r3 = "load config failure: "
            r1.append(r3)     // Catch: java.lang.Throwable -> L4f
            java.lang.String r0 = r0.getMessage()     // Catch: java.lang.Throwable -> L4f
            r1.append(r0)     // Catch: java.lang.Throwable -> L4f
            java.lang.String r0 = r1.toString()     // Catch: java.lang.Throwable -> L4f
            com.xiaomi.channel.commonutils.logger.b.m1859a(r0)     // Catch: java.lang.Throwable -> L4f
            goto L1d
        L43:
            com.xiaomi.push.dw$a r0 = r4.f931a
            if (r0 != 0) goto L4e
            com.xiaomi.push.dw$a r0 = new com.xiaomi.push.dw$a
            r0.<init>()
            r4.f931a = r0
        L4e:
            return
        L4f:
            r0 = move-exception
        L50:
            com.xiaomi.push.ab.a(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.bv.d():void");
    }

    public final void e() {
        try {
            if (this.f931a == null) {
                return;
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(com.xiaomi.push.v.m2551a().openFileOutput("XMCloudCfg", 0));
            com.xiaomi.push.c a2 = com.xiaomi.push.c.a(bufferedOutputStream);
            this.f931a.a(a2);
            a2.m1990a();
            bufferedOutputStream.close();
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("save config failure: " + e.getMessage());
        }
    }
}
