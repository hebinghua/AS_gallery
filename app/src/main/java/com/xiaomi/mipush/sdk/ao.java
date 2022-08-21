package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.bj;
import com.xiaomi.push.bo;
import com.xiaomi.push.db;
import com.xiaomi.push.eo;
import com.xiaomi.push.hj;
import com.xiaomi.push.hk;
import com.xiaomi.push.hn;
import com.xiaomi.push.ho;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.ii;
import com.xiaomi.push.ij;
import com.xiaomi.push.ip;
import com.xiaomi.push.it;
import com.xiaomi.push.iu;
import com.xiaomi.push.service.ba;
import com.xiaomi.push.service.bd;
import com.xiaomi.push.service.bk;
import com.xiaomi.push.service.bn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class ao {
    public static ao a = null;

    /* renamed from: a  reason: collision with other field name */
    public static final ArrayList<a> f45a = new ArrayList<>();
    public static boolean b = false;

    /* renamed from: a  reason: collision with other field name */
    public long f46a;

    /* renamed from: a  reason: collision with other field name */
    public Context f47a;

    /* renamed from: a  reason: collision with other field name */
    public Handler f49a;

    /* renamed from: a  reason: collision with other field name */
    public Messenger f50a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f54a;

    /* renamed from: a  reason: collision with other field name */
    public List<Message> f53a = new ArrayList();
    public boolean c = false;

    /* renamed from: a  reason: collision with other field name */
    public Intent f48a = null;

    /* renamed from: a  reason: collision with other field name */
    public Integer f51a = null;

    /* renamed from: a  reason: collision with other field name */
    public String f52a = null;

    /* loaded from: classes3.dex */
    public static class a<T extends iu<T, ?>> {
        public hj a;

        /* renamed from: a  reason: collision with other field name */
        public T f55a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f56a;
    }

    public ao(Context context) {
        this.f54a = false;
        this.f49a = null;
        this.f47a = context.getApplicationContext();
        this.f54a = m1902c();
        b = m1904d();
        this.f49a = new ap(this, Looper.getMainLooper());
        if (com.xiaomi.push.m.m2400a(context)) {
            com.xiaomi.push.service.i.a(new aq(this));
        }
        Intent b2 = b();
        if (b2 != null) {
            b(b2);
        }
    }

    public static synchronized ao a(Context context) {
        ao aoVar;
        synchronized (ao.class) {
            if (a == null) {
                a = new ao(context);
            }
            aoVar = a;
        }
        return aoVar;
    }

    public final synchronized int a() {
        return this.f47a.getSharedPreferences("mipush_extra", 0).getInt("service_boot_mode", -1);
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m1892a() {
        return this.f46a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final Intent m1893a() {
        return (!m1897a() || com.xiaomi.stat.c.c.a.equals(this.f47a.getPackageName())) ? e() : d();
    }

    public final Message a(Intent intent) {
        Message obtain = Message.obtain();
        obtain.what = 17;
        obtain.obj = intent;
        return obtain;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final String m1894a() {
        try {
            return this.f47a.getPackageManager().getPackageInfo(com.xiaomi.stat.c.c.a, 4).versionCode >= 106 ? "com.xiaomi.push.service.XMPushService" : "com.xiaomi.xmsf.push.service.XMPushService";
        } catch (Exception unused) {
            return "com.xiaomi.xmsf.push.service.XMPushService";
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1895a() {
        b(m1893a());
    }

    public void a(int i) {
        a(i, 0);
    }

    public void a(int i, int i2) {
        Intent m1893a = m1893a();
        m1893a.setAction("com.xiaomi.mipush.CLEAR_NOTIFICATION");
        m1893a.putExtra(bk.B, this.f47a.getPackageName());
        m1893a.putExtra(bk.C, i);
        m1893a.putExtra(bk.D, i2);
        c(m1893a);
    }

    public void a(int i, String str) {
        Intent m1893a = m1893a();
        m1893a.setAction("com.xiaomi.mipush.thirdparty");
        m1893a.putExtra("com.xiaomi.mipush.thirdparty_LEVEL", i);
        m1893a.putExtra("com.xiaomi.mipush.thirdparty_DESC", str);
        b(m1893a);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1896a(Intent intent) {
        intent.fillIn(m1893a(), 24);
        c(intent);
    }

    public final void a(hn hnVar) {
        Intent m1893a = m1893a();
        byte[] a2 = it.a(hnVar);
        if (a2 == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("send TinyData failed, because tinyDataBytes is null.");
            return;
        }
        m1893a.setAction("com.xiaomi.mipush.SEND_TINYDATA");
        m1893a.putExtra("mipush_payload", a2);
        b(m1893a);
    }

    public final void a(ij ijVar, boolean z) {
        eo.a(this.f47a.getApplicationContext()).a(this.f47a.getPackageName(), "E100003", ijVar.a(), 6001, null);
        this.f48a = null;
        b.m1906a(this.f47a).f61a = ijVar.a();
        Intent m1893a = m1893a();
        byte[] a2 = it.a(ai.a(this.f47a, ijVar, hj.Registration));
        if (a2 == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("register fail, because msgBytes is null.");
            return;
        }
        m1893a.setAction("com.xiaomi.mipush.REGISTER_APP");
        m1893a.putExtra("mipush_app_id", b.m1906a(this.f47a).m1907a());
        m1893a.putExtra("mipush_payload", a2);
        m1893a.putExtra("mipush_session", this.f52a);
        m1893a.putExtra("mipush_env_chanage", z);
        m1893a.putExtra("mipush_env_type", b.m1906a(this.f47a).a());
        if (!bj.b(this.f47a) || !m1900b()) {
            this.f48a = m1893a;
            return;
        }
        g();
        c(m1893a);
    }

    public final void a(ip ipVar) {
        byte[] a2 = it.a(ai.a(this.f47a, ipVar, hj.UnRegistration));
        if (a2 == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("unregister fail, because msgBytes is null.");
            return;
        }
        Intent m1893a = m1893a();
        m1893a.setAction("com.xiaomi.mipush.UNREGISTER_APP");
        m1893a.putExtra("mipush_app_id", b.m1906a(this.f47a).m1907a());
        m1893a.putExtra("mipush_payload", a2);
        c(m1893a);
    }

    public final <T extends iu<T, ?>> void a(T t, hj hjVar, hw hwVar) {
        a((ao) t, hjVar, !hjVar.equals(hj.Registration), hwVar);
    }

    public <T extends iu<T, ?>> void a(T t, hj hjVar, boolean z) {
        a aVar = new a();
        aVar.f55a = t;
        aVar.a = hjVar;
        aVar.f56a = z;
        ArrayList<a> arrayList = f45a;
        synchronized (arrayList) {
            arrayList.add(aVar);
            if (arrayList.size() > 10) {
                arrayList.remove(0);
            }
        }
    }

    public final <T extends iu<T, ?>> void a(T t, hj hjVar, boolean z, hw hwVar) {
        a(t, hjVar, z, true, hwVar, true);
    }

    public final <T extends iu<T, ?>> void a(T t, hj hjVar, boolean z, hw hwVar, boolean z2) {
        a(t, hjVar, z, true, hwVar, z2);
    }

    public final <T extends iu<T, ?>> void a(T t, hj hjVar, boolean z, boolean z2, hw hwVar, boolean z3) {
        a(t, hjVar, z, z2, hwVar, z3, this.f47a.getPackageName(), b.m1906a(this.f47a).m1907a());
    }

    public final <T extends iu<T, ?>> void a(T t, hj hjVar, boolean z, boolean z2, hw hwVar, boolean z3, String str, String str2) {
        a(t, hjVar, z, z2, hwVar, z3, str, str2, true);
    }

    public final <T extends iu<T, ?>> void a(T t, hj hjVar, boolean z, boolean z2, hw hwVar, boolean z3, String str, String str2, boolean z4) {
        a(t, hjVar, z, z2, hwVar, z3, str, str2, z4, true);
    }

    public final <T extends iu<T, ?>> void a(T t, hj hjVar, boolean z, boolean z2, hw hwVar, boolean z3, String str, String str2, boolean z4, boolean z5) {
        if (z5 && !b.m1906a(this.f47a).m1913c()) {
            if (z2) {
                a((ao) t, hjVar, z);
                return;
            } else {
                com.xiaomi.channel.commonutils.logger.b.m1859a("drop the message before initialization.");
                return;
            }
        }
        Cif a2 = z4 ? ai.a(this.f47a, t, hjVar, z, str, str2) : ai.b(this.f47a, t, hjVar, z, str, str2);
        if (hwVar != null) {
            a2.a(hwVar);
        }
        byte[] a3 = it.a(a2);
        if (a3 == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("send message fail, because msgBytes is null.");
            return;
        }
        db.a(this.f47a.getPackageName(), this.f47a, t, hjVar, a3.length);
        Intent m1893a = m1893a();
        m1893a.setAction("com.xiaomi.mipush.SEND_MESSAGE");
        m1893a.putExtra("mipush_payload", a3);
        m1893a.putExtra("com.xiaomi.mipush.MESSAGE_CACHE", z3);
        c(m1893a);
    }

    public final void a(String str, au auVar, e eVar) {
        af.a(this.f47a).a(auVar, "syncing");
        a(str, auVar, false, i.m1922a(this.f47a, eVar));
    }

    public final void a(String str, au auVar, boolean z, HashMap<String, String> hashMap) {
        ii iiVar;
        String str2;
        if (!b.m1906a(this.f47a).m1911b() || !bj.b(this.f47a)) {
            return;
        }
        ii iiVar2 = new ii();
        iiVar2.a(true);
        Intent m1893a = m1893a();
        if (TextUtils.isEmpty(str)) {
            str = bd.a();
            iiVar2.a(str);
            iiVar = z ? new ii(str, true) : null;
            synchronized (af.class) {
                af.a(this.f47a).m1886a(str);
            }
        } else {
            iiVar2.a(str);
            iiVar = z ? new ii(str, true) : null;
        }
        switch (at.a[auVar.ordinal()]) {
            case 1:
                ht htVar = ht.DisablePushMessage;
                iiVar2.c(htVar.f489a);
                iiVar.c(htVar.f489a);
                if (hashMap != null) {
                    iiVar2.a(hashMap);
                    iiVar.a(hashMap);
                }
                str2 = "com.xiaomi.mipush.DISABLE_PUSH_MESSAGE";
                m1893a.setAction(str2);
                break;
            case 2:
                ht htVar2 = ht.EnablePushMessage;
                iiVar2.c(htVar2.f489a);
                iiVar.c(htVar2.f489a);
                if (hashMap != null) {
                    iiVar2.a(hashMap);
                    iiVar.a(hashMap);
                }
                str2 = "com.xiaomi.mipush.ENABLE_PUSH_MESSAGE";
                m1893a.setAction(str2);
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                iiVar2.c(ht.ThirdPartyRegUpdate.f489a);
                if (hashMap != null) {
                    iiVar2.a(hashMap);
                    break;
                }
                break;
        }
        com.xiaomi.channel.commonutils.logger.b.e("type:" + auVar + ", " + str);
        iiVar2.b(b.m1906a(this.f47a).m1907a());
        iiVar2.d(this.f47a.getPackageName());
        hj hjVar = hj.Notification;
        a((ao) iiVar2, hjVar, false, (hw) null);
        if (z) {
            iiVar.b(b.m1906a(this.f47a).m1907a());
            iiVar.d(this.f47a.getPackageName());
            Context context = this.f47a;
            byte[] a2 = it.a(ai.a(context, iiVar, hjVar, false, context.getPackageName(), b.m1906a(this.f47a).m1907a()));
            if (a2 != null) {
                db.a(this.f47a.getPackageName(), this.f47a, iiVar, hjVar, a2.length);
                m1893a.putExtra("mipush_payload", a2);
                m1893a.putExtra("com.xiaomi.mipush.MESSAGE_CACHE", true);
                m1893a.putExtra("mipush_app_id", b.m1906a(this.f47a).m1907a());
                m1893a.putExtra("mipush_app_token", b.m1906a(this.f47a).b());
                c(m1893a);
            }
        }
        Message obtain = Message.obtain();
        obtain.what = 19;
        int ordinal = auVar.ordinal();
        obtain.obj = str;
        obtain.arg1 = ordinal;
        this.f49a.sendMessageDelayed(obtain, 5000L);
    }

    public void a(String str, String str2) {
        Intent m1893a = m1893a();
        m1893a.setAction("com.xiaomi.mipush.CLEAR_NOTIFICATION");
        m1893a.putExtra(bk.B, this.f47a.getPackageName());
        m1893a.putExtra(bk.H, str);
        m1893a.putExtra(bk.I, str2);
        c(m1893a);
    }

    public final void a(boolean z) {
        a(z, (String) null);
    }

    public final void a(boolean z, String str) {
        au auVar;
        af a2;
        au auVar2;
        if (z) {
            af a3 = af.a(this.f47a);
            auVar = au.DISABLE_PUSH;
            a3.a(auVar, "syncing");
            a2 = af.a(this.f47a);
            auVar2 = au.ENABLE_PUSH;
        } else {
            af a4 = af.a(this.f47a);
            auVar = au.ENABLE_PUSH;
            a4.a(auVar, "syncing");
            a2 = af.a(this.f47a);
            auVar2 = au.DISABLE_PUSH;
        }
        a2.a(auVar2, "");
        a(str, auVar, true, (HashMap<String, String>) null);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m1897a() {
        return this.f54a && 1 == b.m1906a(this.f47a).a();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m1898a(int i) {
        if (!b.m1906a(this.f47a).m1911b()) {
            return false;
        }
        c(i);
        ii iiVar = new ii();
        iiVar.a(bd.a());
        iiVar.b(b.m1906a(this.f47a).m1907a());
        iiVar.d(this.f47a.getPackageName());
        iiVar.c(ht.ClientABTest.f489a);
        HashMap hashMap = new HashMap();
        iiVar.f628a = hashMap;
        hashMap.put("boot_mode", i + "");
        a(this.f47a).a((ao) iiVar, hj.Notification, false, (hw) null);
        return true;
    }

    public final Intent b() {
        if (!com.xiaomi.stat.c.c.a.equals(this.f47a.getPackageName())) {
            return c();
        }
        com.xiaomi.channel.commonutils.logger.b.c("pushChannel xmsf create own channel");
        return e();
    }

    /* renamed from: b  reason: collision with other method in class */
    public final void m1899b() {
        Intent m1893a = m1893a();
        m1893a.setAction("com.xiaomi.mipush.DISABLE_PUSH");
        c(m1893a);
    }

    public final void b(Intent intent) {
        try {
            if (com.xiaomi.push.m.m2399a() || Build.VERSION.SDK_INT < 26) {
                this.f47a.startService(intent);
            } else {
                d(intent);
            }
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m1900b() {
        if (!m1897a() || !m1905e()) {
            return true;
        }
        if (this.f51a == null) {
            Integer valueOf = Integer.valueOf(bn.a(this.f47a).a());
            this.f51a = valueOf;
            if (valueOf.intValue() == 0) {
                this.f47a.getContentResolver().registerContentObserver(bn.a(this.f47a).m2500a(), false, new ar(this, new Handler(Looper.getMainLooper())));
            }
        }
        return this.f51a.intValue() != 0;
    }

    public final Intent c() {
        if (m1897a()) {
            com.xiaomi.channel.commonutils.logger.b.c("pushChannel app start miui china channel");
            return d();
        }
        com.xiaomi.channel.commonutils.logger.b.c("pushChannel app start  own channel");
        return e();
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m1901c() {
        if (this.f48a != null) {
            g();
            c(this.f48a);
            this.f48a = null;
        }
    }

    public final synchronized void c(int i) {
        this.f47a.getSharedPreferences("mipush_extra", 0).edit().putInt("service_boot_mode", i).commit();
    }

    public final void c(Intent intent) {
        ba a2 = ba.a(this.f47a);
        int a3 = ho.ServiceBootMode.a();
        hk hkVar = hk.START;
        int a4 = a2.a(a3, hkVar.a());
        int a5 = a();
        hk hkVar2 = hk.BIND;
        boolean z = a4 == hkVar2.a() && b;
        int a6 = z ? hkVar2.a() : hkVar.a();
        if (a6 != a5) {
            m1898a(a6);
        }
        if (z) {
            d(intent);
        } else {
            b(intent);
        }
    }

    /* renamed from: c  reason: collision with other method in class */
    public final boolean m1902c() {
        try {
            PackageInfo packageInfo = this.f47a.getPackageManager().getPackageInfo(com.xiaomi.stat.c.c.a, 4);
            if (packageInfo == null) {
                return false;
            }
            return packageInfo.versionCode >= 105;
        } catch (Throwable unused) {
            return false;
        }
    }

    public final Intent d() {
        Intent intent = new Intent();
        String packageName = this.f47a.getPackageName();
        intent.setPackage(com.xiaomi.stat.c.c.a);
        intent.setClassName(com.xiaomi.stat.c.c.a, m1894a());
        intent.putExtra("mipush_app_package", packageName);
        h();
        return intent;
    }

    /* renamed from: d  reason: collision with other method in class */
    public void m1903d() {
        ArrayList<a> arrayList = f45a;
        synchronized (arrayList) {
            boolean z = Thread.currentThread() == Looper.getMainLooper().getThread();
            Iterator<a> it = arrayList.iterator();
            while (it.hasNext()) {
                a next = it.next();
                a(next.f55a, next.a, next.f56a, false, null, true);
                if (!z) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException unused) {
                    }
                }
            }
            f45a.clear();
        }
    }

    public final synchronized void d(Intent intent) {
        if (this.c) {
            Message a2 = a(intent);
            if (this.f53a.size() >= 50) {
                this.f53a.remove(0);
            }
            this.f53a.add(a2);
            return;
        }
        if (this.f50a == null) {
            this.f47a.bindService(intent, new as(this), 1);
            this.c = true;
            this.f53a.clear();
            this.f53a.add(a(intent));
        } else {
            try {
                this.f50a.send(a(intent));
            } catch (RemoteException unused) {
                this.f50a = null;
                this.c = false;
            }
        }
    }

    /* renamed from: d  reason: collision with other method in class */
    public final boolean m1904d() {
        if (m1897a()) {
            try {
                return this.f47a.getPackageManager().getPackageInfo(com.xiaomi.stat.c.c.a, 4).versionCode >= 108;
            } catch (Exception unused) {
            }
        }
        return true;
    }

    public final Intent e() {
        Intent intent = new Intent();
        String packageName = this.f47a.getPackageName();
        i();
        intent.setComponent(new ComponentName(this.f47a, "com.xiaomi.push.service.XMPushService"));
        intent.putExtra("mipush_app_package", packageName);
        return intent;
    }

    /* renamed from: e  reason: collision with other method in class */
    public final boolean m1905e() {
        String packageName = this.f47a.getPackageName();
        return packageName.contains("miui") || packageName.contains("xiaomi") || (this.f47a.getApplicationInfo().flags & 1) != 0;
    }

    public void f() {
        Intent m1893a = m1893a();
        m1893a.setAction("com.xiaomi.mipush.SET_NOTIFICATION_TYPE");
        m1893a.putExtra(bk.B, this.f47a.getPackageName());
        m1893a.putExtra(bk.G, bo.b(this.f47a.getPackageName()));
        c(m1893a);
    }

    public final void g() {
        this.f46a = SystemClock.elapsedRealtime();
    }

    public final void h() {
        try {
            PackageManager packageManager = this.f47a.getPackageManager();
            ComponentName componentName = new ComponentName(this.f47a, "com.xiaomi.push.service.XMPushService");
            if (packageManager.getComponentEnabledSetting(componentName) == 2) {
                return;
            }
            packageManager.setComponentEnabledSetting(componentName, 2, 1);
        } catch (Throwable unused) {
        }
    }

    public final void i() {
        try {
            PackageManager packageManager = this.f47a.getPackageManager();
            ComponentName componentName = new ComponentName(this.f47a, "com.xiaomi.push.service.XMPushService");
            if (packageManager.getComponentEnabledSetting(componentName) == 1) {
                return;
            }
            packageManager.setComponentEnabledSetting(componentName, 1, 1);
        } catch (Throwable unused) {
        }
    }
}
