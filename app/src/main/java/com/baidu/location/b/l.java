package com.baidu.location.b;

import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.baidu.location.Jni;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public abstract class l {
    public static String c;
    public com.baidu.location.c.h a = null;
    public com.baidu.location.c.a b = null;
    private boolean e = true;
    private boolean f = true;
    private boolean g = false;
    private long h = 0;
    public final Handler d = new a();
    private String i = null;
    private String j = null;
    private boolean k = false;

    /* loaded from: classes.dex */
    public class a extends Handler {
        public a() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (!com.baidu.location.f.isServing) {
                return;
            }
            int i = message.what;
            if (i == 21) {
                l.this.a(message);
            } else if (i != 62 && i != 63) {
            } else {
                l.this.a();
            }
        }
    }

    /* loaded from: classes.dex */
    public class b extends com.baidu.location.e.e {
        public String a = null;
        public String b = null;
        public long c = 0;
        public long d = 0;

        public b() {
            this.j = new HashMap();
        }

        @Override // com.baidu.location.e.e
        public void a() {
            this.g = com.baidu.location.e.j.d();
            if ((com.baidu.location.e.j.h || com.baidu.location.e.j.j) && l.this.i != null && l.this.j != null) {
                this.b += String.format(Locale.CHINA, "&ki=%s&sn=%s", l.this.i, l.this.j);
            }
            if (m.a().b()) {
                this.b += "&enc=2";
            }
            String encodeTp4 = Jni.encodeTp4(this.b);
            this.b = null;
            if (this.a == null) {
                this.a = w.b();
            }
            this.j.put("bloc", encodeTp4);
            String str = this.a;
            if (str != null) {
                this.j.put(MapBundleKey.OfflineMapKey.OFFLINE_UPDATE, str);
            }
            this.j.put("trtm", String.format(Locale.CHINA, "%d", Long.valueOf(System.currentTimeMillis())));
        }

        public void a(String str, long j) {
            this.b = str;
            this.d = System.currentTimeMillis();
            this.c = j;
            ExecutorService b = u.a().b();
            if (com.baidu.location.e.j.b()) {
                a(b, false, null);
            } else if (b != null) {
                a(b, com.baidu.location.e.j.f);
            } else {
                b(com.baidu.location.e.j.f);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:4:0x0008, code lost:
            r10 = r9.i;
         */
        /* JADX WARN: Removed duplicated region for block: B:44:0x00e2  */
        /* JADX WARN: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
        @Override // com.baidu.location.e.e
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void a(boolean r10) {
            /*
                r9 = this;
                java.lang.String r0 = "enc"
                java.lang.String r1 = "HttpStatus error"
                r2 = 63
                if (r10 == 0) goto Ld1
                java.lang.String r10 = r9.i
                if (r10 == 0) goto Ld1
                com.baidu.location.b.l.c = r10     // Catch: java.lang.Exception -> Ld1
                boolean r3 = r10.contains(r0)     // Catch: java.lang.Exception -> Ld1
                if (r3 == 0) goto L3a
                com.baidu.location.b.m r3 = com.baidu.location.b.m.a()     // Catch: java.lang.Exception -> Ld1
                boolean r3 = r3.b()     // Catch: java.lang.Exception -> Ld1
                if (r3 == 0) goto L3a
                org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> L36
                r3.<init>(r10)     // Catch: java.lang.Exception -> L36
                boolean r4 = r3.has(r0)     // Catch: java.lang.Exception -> L36
                if (r4 == 0) goto L3a
                java.lang.String r0 = r3.getString(r0)     // Catch: java.lang.Exception -> L36
                com.baidu.location.b.m r3 = com.baidu.location.b.m.a()     // Catch: java.lang.Exception -> L36
                java.lang.String r10 = r3.b(r0)     // Catch: java.lang.Exception -> L36
                goto L3a
            L36:
                r0 = move-exception
                r0.printStackTrace()     // Catch: java.lang.Exception -> Ld1
            L3a:
                com.baidu.location.BDLocation r0 = new com.baidu.location.BDLocation     // Catch: java.lang.Exception -> L6f
                r0.<init>(r10)     // Catch: java.lang.Exception -> L6f
                int r3 = r0.getLocType()     // Catch: java.lang.Exception -> L6f
                r4 = 161(0xa1, float:2.26E-43)
                if (r3 != r4) goto L4e
                com.baidu.location.b.k r3 = com.baidu.location.b.k.a()     // Catch: java.lang.Exception -> L6f
                r3.a(r10)     // Catch: java.lang.Exception -> L6f
            L4e:
                com.baidu.location.c.b r10 = com.baidu.location.c.b.a()     // Catch: java.lang.Exception -> L6f
                int r10 = r10.h()     // Catch: java.lang.Exception -> L6f
                r0.setOperators(r10)     // Catch: java.lang.Exception -> L6f
                com.baidu.location.b.r r10 = com.baidu.location.b.r.a()     // Catch: java.lang.Exception -> L6f
                boolean r10 = r10.d()     // Catch: java.lang.Exception -> L6f
                if (r10 == 0) goto L7c
                com.baidu.location.b.r r10 = com.baidu.location.b.r.a()     // Catch: java.lang.Exception -> L6f
                float r10 = r10.e()     // Catch: java.lang.Exception -> L6f
                r0.setDirection(r10)     // Catch: java.lang.Exception -> L6f
                goto L7c
            L6f:
                r10 = move-exception
                r10.printStackTrace()     // Catch: java.lang.Exception -> Ld1
                com.baidu.location.BDLocation r0 = new com.baidu.location.BDLocation     // Catch: java.lang.Exception -> Ld1
                r0.<init>()     // Catch: java.lang.Exception -> Ld1
                r10 = 0
                r0.setLocType(r10)     // Catch: java.lang.Exception -> Ld1
            L7c:
                r10 = 0
                r9.a = r10     // Catch: java.lang.Exception -> Ld1
                int r10 = r0.getLocType()     // Catch: java.lang.Exception -> Ld1
                if (r10 != 0) goto La5
                double r3 = r0.getLatitude()     // Catch: java.lang.Exception -> Ld1
                r5 = 1
                int r10 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r10 != 0) goto La5
                double r3 = r0.getLongitude()     // Catch: java.lang.Exception -> Ld1
                int r10 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r10 != 0) goto La5
                com.baidu.location.b.l r10 = com.baidu.location.b.l.this     // Catch: java.lang.Exception -> Ld1
                android.os.Handler r10 = r10.d     // Catch: java.lang.Exception -> Ld1
                android.os.Message r10 = r10.obtainMessage(r2)     // Catch: java.lang.Exception -> Ld1
                r10.obj = r1     // Catch: java.lang.Exception -> Ld1
            La1:
                r10.sendToTarget()     // Catch: java.lang.Exception -> Ld1
                goto Lde
            La5:
                long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Exception -> Ld1
                long r5 = r9.d     // Catch: java.lang.Exception -> Ld1
                long r3 = r3 - r5
                r5 = 1000(0x3e8, double:4.94E-321)
                long r3 = r3 / r5
                r5 = 0
                int r10 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r10 >= 0) goto Lb6
                r3 = r5
            Lb6:
                long r7 = r9.c     // Catch: java.lang.Exception -> Ld1
                int r10 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
                if (r10 >= 0) goto Lbe
                r9.c = r5     // Catch: java.lang.Exception -> Ld1
            Lbe:
                long r5 = r9.c     // Catch: java.lang.Exception -> Ld1
                long r5 = r5 + r3
                r0.setDelayTime(r5)     // Catch: java.lang.Exception -> Ld1
                com.baidu.location.b.l r10 = com.baidu.location.b.l.this     // Catch: java.lang.Exception -> Ld1
                android.os.Handler r10 = r10.d     // Catch: java.lang.Exception -> Ld1
                r3 = 21
                android.os.Message r10 = r10.obtainMessage(r3)     // Catch: java.lang.Exception -> Ld1
                r10.obj = r0     // Catch: java.lang.Exception -> Ld1
                goto La1
            Ld1:
                com.baidu.location.b.l r10 = com.baidu.location.b.l.this
                android.os.Handler r10 = r10.d
                android.os.Message r10 = r10.obtainMessage(r2)
                r10.obj = r1
                r10.sendToTarget()
            Lde:
                java.util.Map<java.lang.String, java.lang.Object> r10 = r9.j
                if (r10 == 0) goto Le5
                r10.clear()
            Le5:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.b.l.b.a(boolean):void");
        }
    }

    public String a(String str) {
        com.baidu.location.c.h hVar;
        String l;
        if (this.i == null) {
            this.i = com.baidu.location.a.a.b(com.baidu.location.f.getServiceContext());
        }
        if (this.j == null) {
            this.j = com.baidu.location.a.a.c(com.baidu.location.f.getServiceContext());
        }
        com.baidu.location.c.a aVar = this.b;
        if (aVar == null || !aVar.a()) {
            this.b = com.baidu.location.c.b.a().f();
        }
        com.baidu.location.c.h hVar2 = this.a;
        if (hVar2 == null || !hVar2.j()) {
            this.a = com.baidu.location.c.i.a().o();
        }
        Location g = com.baidu.location.c.f.a().j() ? com.baidu.location.c.f.a().g() : null;
        com.baidu.location.c.a aVar2 = this.b;
        if ((aVar2 == null || aVar2.d() || this.b.c()) && (((hVar = this.a) == null || hVar.a() == 0) && g == null)) {
            return null;
        }
        String b2 = b();
        if (k.a().d() == -2) {
            b2 = b2 + "&imo=1";
        }
        int b3 = com.baidu.location.e.j.b(com.baidu.location.f.getServiceContext());
        if (b3 >= 0) {
            b2 = b2 + "&lmd=" + b3;
            if (Build.VERSION.SDK_INT >= 28 && !this.k) {
                this.k = true;
                try {
                    if (com.baidu.location.f.getServiceContext().getPackageManager().hasSystemFeature("android.hardware.wifi.rtt")) {
                        b2 = b2 + "&rtt=1";
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        com.baidu.location.c.h hVar3 = this.a;
        if ((hVar3 == null || hVar3.a() == 0) && (l = com.baidu.location.c.i.a().l()) != null) {
            b2 = l + b2;
        }
        String str2 = b2;
        if (!this.f) {
            return com.baidu.location.e.j.a(this.b, this.a, g, str2, 0);
        }
        this.f = false;
        return com.baidu.location.e.j.a(this.b, this.a, g, str2, 0, true);
    }

    public abstract void a();

    public abstract void a(Message message);

    public String b() {
        String c2 = com.baidu.location.b.b.a().c();
        String format = com.baidu.location.c.i.a().i() ? "&cn=32" : String.format(Locale.CHINA, "&cn=%d", Integer.valueOf(com.baidu.location.c.b.a().e()));
        if (Build.VERSION.SDK_INT >= 18 && System.currentTimeMillis() - this.h > 60000) {
            this.h = System.currentTimeMillis();
            String c3 = com.baidu.location.e.j.c();
            if (!TextUtils.isEmpty(c3)) {
                format = format + "&qcip6c=" + c3;
            }
        }
        if (this.e) {
            this.e = false;
        } else if (!this.g) {
            String e = w.e();
            if (e != null) {
                format = format + e;
            }
            this.g = true;
        }
        return format + c2;
    }
}
