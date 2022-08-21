package com.xiaomi.stat;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.SystemClock;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: classes3.dex */
public class e {
    private boolean a;
    private String b;
    private String c;
    private Context d;
    private Executor e;
    private long f;
    private Map<String, Long> g;
    private al h;
    private int i;
    private int j;
    private int k;
    private long l;

    public static /* synthetic */ int g(e eVar) {
        int i = eVar.i;
        eVar.i = i + 1;
        return i;
    }

    public static /* synthetic */ int h(e eVar) {
        int i = eVar.j;
        eVar.j = i + 1;
        return i;
    }

    public static /* synthetic */ int j(e eVar) {
        int i = eVar.k;
        eVar.k = i + 1;
        return i;
    }

    public static /* synthetic */ int m(e eVar) {
        int i = eVar.i;
        eVar.i = i - 1;
        return i;
    }

    public e(Context context, String str, String str2, boolean z) {
        this(context, str, str2, z, (String) null);
    }

    public e(Context context, String str, String str2, boolean z, String str3) {
        this.i = 0;
        this.j = 0;
        this.k = 0;
        this.a = true;
        a(context, str, str2, z, str3);
    }

    public e(Context context, String str, String str2, String str3, boolean z) {
        this.i = 0;
        this.j = 0;
        this.k = 0;
        this.a = false;
        this.b = str3;
        a(context, str, str2, z, (String) null);
    }

    private void a(Context context, String str, String str2, boolean z, String str3) {
        this.d = context.getApplicationContext();
        ak.a(context.getApplicationContext(), str, str2);
        if (!this.a) {
            str = this.b;
        }
        this.c = str;
        this.e = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        if (this.a) {
            e();
        }
        com.xiaomi.stat.d.r.a();
        this.e.execute(new f(this, str3, z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long d() {
        return com.xiaomi.stat.d.r.b();
    }

    private void e() {
        ((Application) this.d).registerActivityLifecycleCallbacks(new r(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, long j, long j2) {
        this.e.execute(new u(this, str, j, j2));
    }

    public void a(boolean z) {
        if (!this.a) {
            return;
        }
        this.e.execute(new v(this, z));
    }

    public void b(boolean z) {
        if (!this.a) {
            return;
        }
        this.e.execute(new w(this, z));
    }

    public void a(String str) {
        if (this.g == null) {
            this.g = new HashMap();
        }
        this.g.put(str, Long.valueOf(SystemClock.elapsedRealtime()));
    }

    public void b(String str) {
        a(str, (MiStatParams) null);
    }

    public void a(String str, MiStatParams miStatParams) {
        Long l;
        Map<String, Long> map = this.g;
        if (map == null || (l = map.get(str)) == null) {
            return;
        }
        this.g.remove(str);
        if (!com.xiaomi.stat.d.n.a(str)) {
            return;
        }
        if (miStatParams != null && !c(miStatParams)) {
            return;
        }
        this.e.execute(new x(this, str, d(), SystemClock.elapsedRealtime() - l.longValue(), miStatParams));
    }

    public void c(String str) {
        a(str, (String) null, (MiStatParams) null);
    }

    public void a(String str, String str2) {
        a(str, str2, (MiStatParams) null);
    }

    public void b(String str, MiStatParams miStatParams) {
        a(str, (String) null, miStatParams);
    }

    public void a(String str, String str2, MiStatParams miStatParams) {
        a(str, str2, miStatParams, false);
    }

    private void a(String str, String str2, MiStatParams miStatParams, boolean z) {
        if (!com.xiaomi.stat.d.n.a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (str2 != null && !com.xiaomi.stat.d.n.a(str2)) {
            com.xiaomi.stat.d.n.e(str2);
        } else if (miStatParams != null && !c(miStatParams)) {
        } else {
            this.e.execute(new y(this, z, str, str2, miStatParams));
        }
    }

    public void d(String str) {
        b(str, null, null);
    }

    public void b(String str, String str2) {
        b(str, str2, null);
    }

    public void c(String str, MiStatParams miStatParams) {
        a(str, (String) null, miStatParams);
    }

    public void b(String str, String str2, MiStatParams miStatParams) {
        a(str, str2, miStatParams, true);
    }

    public void c(boolean z) {
        if (!this.a) {
            return;
        }
        b.d(z);
        al alVar = this.h;
        if (alVar != null) {
            alVar.a(z);
        } else if (!z) {
        } else {
            al alVar2 = new al(this);
            this.h = alVar2;
            alVar2.a();
        }
    }

    public void a(Throwable th) {
        a(th, (String) null);
    }

    public void a(Throwable th, String str) {
        a(th, str, true);
    }

    public void a(Throwable th, String str, boolean z) {
        if (th == null) {
            return;
        }
        this.e.execute(new z(this, th, str, z));
    }

    public void c(String str, String str2) {
        if (!com.xiaomi.stat.d.n.a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (!com.xiaomi.stat.d.n.b(str2)) {
            com.xiaomi.stat.d.n.f(str2);
        } else {
            MiStatParams miStatParams = new MiStatParams();
            miStatParams.putString(str, str2);
            a(miStatParams);
        }
    }

    public void a(MiStatParams miStatParams) {
        a(miStatParams, false);
    }

    public void d(String str, String str2) {
        if (!com.xiaomi.stat.d.n.a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (!com.xiaomi.stat.d.n.b(str2)) {
            com.xiaomi.stat.d.n.f(str2);
        } else {
            MiStatParams miStatParams = new MiStatParams();
            miStatParams.putString(str, str2);
            b(miStatParams);
        }
    }

    public void b(MiStatParams miStatParams) {
        a(miStatParams, true);
    }

    private void a(MiStatParams miStatParams, boolean z) {
        if (miStatParams == null || miStatParams.isEmpty()) {
            com.xiaomi.stat.d.k.e("set user profile failed: empty property !");
        } else if (!c(miStatParams)) {
        } else {
            this.e.execute(new aa(this, z, miStatParams));
        }
    }

    public void e(String str) {
        if (!this.a) {
            return;
        }
        this.e.execute(new h(this, str));
    }

    public void a(int i) {
        if (!this.a) {
            return;
        }
        b.a(i);
    }

    public int a() {
        return b.i();
    }

    public void b(int i) {
        if (!this.a) {
            return;
        }
        b.b(i);
    }

    public int b() {
        return b.j();
    }

    public void d(boolean z) {
        this.e.execute(new i(this, z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        if (!this.a) {
            return;
        }
        int p = b.p();
        int a = com.xiaomi.stat.d.c.a();
        if (p == -1) {
            b.e(a);
        } else if (p >= a) {
        } else {
            this.e.execute(new j(this, a, p));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g(boolean z) {
        if (b.d(this.c)) {
            return b.e(this.c) != 2;
        } else if (b.e() && !z) {
            return true;
        } else {
            return com.xiaomi.stat.d.m.b(this.d);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g() {
        return !b.d(this.c) || b.e(this.c) != 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        this.e.execute(new k(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(long j, long j2) {
        if (j == -1) {
            return true;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(j);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(j2);
        return (calendar.get(1) == calendar2.get(1) && calendar.get(6) == calendar2.get(6)) ? false : true;
    }

    public void a(boolean z, String str) {
        if (!this.a) {
            return;
        }
        this.e.execute(new l(this, z, str));
    }

    public void a(HttpEvent httpEvent) {
        if (httpEvent == null) {
            return;
        }
        this.e.execute(new m(this, httpEvent));
    }

    public void a(NetAvailableEvent netAvailableEvent) {
        if (netAvailableEvent == null) {
            return;
        }
        this.e.execute(new n(this, netAvailableEvent));
    }

    public String c() {
        FutureTask futureTask = new FutureTask(new o(this));
        com.xiaomi.stat.b.e.a().execute(futureTask);
        try {
            return (String) futureTask.get(5L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException unused) {
            return null;
        }
    }

    public boolean e(boolean z) {
        return a(z, false);
    }

    public boolean a(boolean z, boolean z2) {
        if (i()) {
            b.f(z);
            b.g(z2);
            return true;
        }
        return false;
    }

    private boolean i() {
        boolean z;
        PackageInfo packageInfo;
        PackageInfo packageInfo2;
        Signature[] signatureArr;
        Signature[] signatureArr2;
        boolean z2 = (this.d.getApplicationInfo().flags & 1) == 1;
        PackageManager packageManager = this.d.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(this.d.getPackageName(), 64);
            packageInfo2 = packageManager.getPackageInfo("android", 64);
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (packageInfo != null && (signatureArr = packageInfo.signatures) != null && signatureArr.length > 0 && packageInfo2 != null && (signatureArr2 = packageInfo2.signatures) != null && signatureArr2.length > 0) {
            z = signatureArr2[0].equals(signatureArr[0]);
            return z2 || z;
        }
        z = false;
        if (z2) {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i, int i2, long j, long j2) {
        this.e.execute(new p(this, i, i2, j, j2));
    }

    public void f(boolean z) {
        com.xiaomi.stat.d.k.a(z);
    }

    private boolean c(MiStatParams miStatParams) {
        return miStatParams.getClass().equals(MiStatParams.class) && miStatParams.getParamsNumber() <= 30;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(com.xiaomi.stat.a.l lVar) {
        com.xiaomi.stat.a.c.a().a(lVar);
        com.xiaomi.stat.c.i.a().d();
    }

    public void e(String str, String str2) {
        if (!com.xiaomi.stat.d.n.a(str)) {
            com.xiaomi.stat.d.n.e(str);
        } else if (!com.xiaomi.stat.d.n.d(str2)) {
            com.xiaomi.stat.d.k.e("invalid plain text value for event: " + str);
        } else {
            this.e.execute(new q(this, str, str2));
        }
    }
}
