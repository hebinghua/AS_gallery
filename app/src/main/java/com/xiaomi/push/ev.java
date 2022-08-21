package com.xiaomi.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.xiaomi.push.eu;

/* loaded from: classes3.dex */
public class ev implements eu.a {

    /* renamed from: a  reason: collision with other field name */
    public Context f316a;

    /* renamed from: a  reason: collision with other field name */
    public PendingIntent f315a = null;
    public volatile long a = 0;

    public ev(Context context) {
        this.f316a = null;
        this.f316a = context;
    }

    @Override // com.xiaomi.push.eu.a
    /* renamed from: a */
    public void mo2143a() {
        if (this.f315a != null) {
            try {
                ((AlarmManager) this.f316a.getSystemService("alarm")).cancel(this.f315a);
            } catch (Exception unused) {
            } catch (Throwable th) {
                this.f315a = null;
                com.xiaomi.channel.commonutils.logger.b.c("[Alarm] unregister timer");
                this.a = 0L;
                throw th;
            }
            this.f315a = null;
            com.xiaomi.channel.commonutils.logger.b.c("[Alarm] unregister timer");
            this.a = 0L;
        }
        this.a = 0L;
    }

    public final void a(AlarmManager alarmManager, long j, PendingIntent pendingIntent) {
        try {
            AlarmManager.class.getMethod("setExact", Integer.TYPE, Long.TYPE, PendingIntent.class).invoke(alarmManager, 2, Long.valueOf(j), pendingIntent);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.d("[Alarm] invoke setExact method meet error. " + e);
        }
    }

    public void a(Intent intent, long j) {
        AlarmManager alarmManager = (AlarmManager) this.f316a.getSystemService("alarm");
        int i = Build.VERSION.SDK_INT;
        this.f315a = i >= 31 ? PendingIntent.getBroadcast(this.f316a, 0, intent, nexEngine.ExportHEVCHighTierLevel62) : PendingIntent.getBroadcast(this.f316a, 0, intent, 0);
        if (i >= 23) {
            bk.a((Object) alarmManager, "setExactAndAllowWhileIdle", 2, Long.valueOf(j), this.f315a);
        } else {
            a(alarmManager, j, this.f315a);
        }
        com.xiaomi.channel.commonutils.logger.b.c("[Alarm] register timer " + j);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0036, code lost:
        if (r8.a < r4) goto L17;
     */
    @Override // com.xiaomi.push.eu.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(boolean r9) {
        /*
            r8 = this;
            android.content.Context r0 = r8.f316a
            com.xiaomi.push.service.o r0 = com.xiaomi.push.service.o.a(r0)
            long r0 = r0.m2520a()
            r2 = 0
            if (r9 != 0) goto L15
            long r4 = r8.a
            int r4 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r4 != 0) goto L15
            return
        L15:
            if (r9 == 0) goto L1a
            r8.mo2143a()
        L1a:
            long r4 = android.os.SystemClock.elapsedRealtime()
            if (r9 != 0) goto L39
            long r6 = r8.a
            int r9 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r9 != 0) goto L27
            goto L39
        L27:
            long r2 = r8.a
            int r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r9 > 0) goto L3f
            long r2 = r8.a
            long r2 = r2 + r0
            r8.a = r2
            long r2 = r8.a
            int r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r9 >= 0) goto L3f
            goto L3c
        L39:
            long r2 = r4 % r0
            long r0 = r0 - r2
        L3c:
            long r4 = r4 + r0
            r8.a = r4
        L3f:
            android.content.Intent r9 = new android.content.Intent
            java.lang.String r0 = com.xiaomi.push.service.bk.p
            r9.<init>(r0)
            android.content.Context r0 = r8.f316a
            java.lang.String r0 = r0.getPackageName()
            r9.setPackage(r0)
            long r0 = r8.a
            r8.a(r9, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ev.a(boolean):void");
    }

    @Override // com.xiaomi.push.eu.a
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2143a() {
        return this.a != 0;
    }
}
