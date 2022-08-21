package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.service.XMJobService;

/* loaded from: classes3.dex */
public final class eu {

    /* renamed from: a  reason: collision with other field name */
    public static a f313a;

    /* renamed from: a  reason: collision with other field name */
    public static final String f314a = XMJobService.class.getCanonicalName();
    public static int a = 0;

    /* loaded from: classes3.dex */
    public interface a {
        /* renamed from: a */
        void mo2143a();

        void a(boolean z);

        /* renamed from: a  reason: collision with other method in class */
        boolean m2142a();
    }

    public static synchronized void a() {
        synchronized (eu.class) {
            if (f313a == null) {
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("[Alarm] stop alarm.");
            f313a.mo2143a();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x005a, code lost:
        if (r7.equals(com.xiaomi.push.v.a(r9, r6.name).getSuperclass().getCanonicalName()) != false) goto L19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void a(android.content.Context r9) {
        /*
            java.lang.String r0 = "android.permission.BIND_JOB_SERVICE"
            android.content.Context r9 = r9.getApplicationContext()
            java.lang.String r1 = r9.getPackageName()
            java.lang.String r2 = "com.xiaomi.xmsf"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L1b
            com.xiaomi.push.ev r0 = new com.xiaomi.push.ev
            r0.<init>(r9)
        L17:
            com.xiaomi.push.eu.f313a = r0
            goto Lcf
        L1b:
            android.content.pm.PackageManager r1 = r9.getPackageManager()
            r2 = 0
            java.lang.String r3 = r9.getPackageName()     // Catch: java.lang.Exception -> L7c
            r4 = 4
            android.content.pm.PackageInfo r1 = r1.getPackageInfo(r3, r4)     // Catch: java.lang.Exception -> L7c
            android.content.pm.ServiceInfo[] r1 = r1.services     // Catch: java.lang.Exception -> L7c
            r3 = 1
            if (r1 == 0) goto L95
            int r4 = r1.length     // Catch: java.lang.Exception -> L7c
            r5 = r2
        L30:
            if (r2 >= r4) goto L7a
            r6 = r1[r2]     // Catch: java.lang.Exception -> L77
            java.lang.String r7 = r6.permission     // Catch: java.lang.Exception -> L77
            boolean r7 = r0.equals(r7)     // Catch: java.lang.Exception -> L77
            if (r7 == 0) goto L60
            java.lang.String r7 = com.xiaomi.push.eu.f314a     // Catch: java.lang.Exception -> L77
            java.lang.String r8 = r6.name     // Catch: java.lang.Exception -> L77
            boolean r8 = r7.equals(r8)     // Catch: java.lang.Exception -> L77
            if (r8 == 0) goto L48
        L46:
            r5 = r3
            goto L5d
        L48:
            java.lang.String r8 = r6.name     // Catch: java.lang.Exception -> L5d
            java.lang.Class r8 = com.xiaomi.push.v.a(r9, r8)     // Catch: java.lang.Exception -> L5d
            java.lang.Class r8 = r8.getSuperclass()     // Catch: java.lang.Exception -> L5d
            java.lang.String r8 = r8.getCanonicalName()     // Catch: java.lang.Exception -> L5d
            boolean r7 = r7.equals(r8)     // Catch: java.lang.Exception -> L5d
            if (r7 == 0) goto L5d
            goto L46
        L5d:
            if (r5 != r3) goto L60
            goto L7a
        L60:
            java.lang.String r7 = com.xiaomi.push.eu.f314a     // Catch: java.lang.Exception -> L77
            java.lang.String r8 = r6.name     // Catch: java.lang.Exception -> L77
            boolean r7 = r7.equals(r8)     // Catch: java.lang.Exception -> L77
            if (r7 == 0) goto L74
            java.lang.String r6 = r6.permission     // Catch: java.lang.Exception -> L77
            boolean r6 = r0.equals(r6)     // Catch: java.lang.Exception -> L77
            if (r6 == 0) goto L74
            r2 = r3
            goto L95
        L74:
            int r2 = r2 + 1
            goto L30
        L77:
            r1 = move-exception
            r2 = r5
            goto L7d
        L7a:
            r2 = r5
            goto L95
        L7c:
            r1 = move-exception
        L7d:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "check service err : "
            r3.append(r4)
            java.lang.String r1 = r1.getMessage()
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            com.xiaomi.channel.commonutils.logger.b.m1859a(r1)
        L95:
            if (r2 != 0) goto Lc4
            boolean r1 = com.xiaomi.push.v.m2554a(r9)
            if (r1 != 0) goto L9e
            goto Lc4
        L9e:
            java.lang.RuntimeException r9 = new java.lang.RuntimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Should export service: "
            r1.append(r2)
            java.lang.String r2 = com.xiaomi.push.eu.f314a
            r1.append(r2)
            java.lang.String r2 = " with permission "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = " in AndroidManifest.xml file"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r9.<init>(r0)
            throw r9
        Lc4:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 21
            com.xiaomi.push.ev r0 = new com.xiaomi.push.ev
            r0.<init>(r9)
            goto L17
        Lcf:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.eu.a(android.content.Context):void");
    }

    public static synchronized void a(Context context, int i) {
        synchronized (eu.class) {
            int i2 = a;
            if (!com.xiaomi.stat.c.c.a.equals(context.getPackageName())) {
                if (i == 2) {
                    a = 2;
                } else {
                    a = 0;
                }
            }
            int i3 = a;
            if (i2 != i3 && i3 == 2) {
                a();
                f313a = new ex(context);
            }
        }
    }

    public static synchronized void a(boolean z) {
        synchronized (eu.class) {
            if (f313a == null) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("timer is not initialized");
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("[Alarm] register alarm. (" + z + ")");
            f313a.a(z);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized boolean m2141a() {
        synchronized (eu.class) {
            a aVar = f313a;
            if (aVar == null) {
                return false;
            }
            return aVar.m2142a();
        }
    }
}
