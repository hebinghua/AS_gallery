package com.xiaomi.push.service;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.xiaomi.push.gy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ServiceClient {

    /* renamed from: a  reason: collision with other field name */
    public static ServiceClient f812a;

    /* renamed from: a  reason: collision with other field name */
    public Context f813a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f816a;

    /* renamed from: b  reason: collision with other field name */
    public Messenger f817b;
    public static String b = gy.a(5) + "-";
    public static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    public Messenger f814a = null;

    /* renamed from: a  reason: collision with other field name */
    public List<Message> f815a = new ArrayList();

    /* renamed from: b  reason: collision with other field name */
    public boolean f818b = false;

    public ServiceClient(Context context) {
        this.f816a = false;
        this.f813a = context.getApplicationContext();
        if (a()) {
            com.xiaomi.channel.commonutils.logger.b.c("use miui push service");
            this.f816a = true;
        }
    }

    public static ServiceClient getInstance(Context context) {
        if (f812a == null) {
            f812a = new ServiceClient(context);
        }
        return f812a;
    }

    public final Message a(Intent intent) {
        Message obtain = Message.obtain();
        obtain.what = 17;
        obtain.obj = intent;
        return obtain;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final synchronized void m2410a(Intent intent) {
        if (this.f818b) {
            Message a2 = a(intent);
            if (this.f815a.size() >= 50) {
                this.f815a.remove(0);
            }
            this.f815a.add(a2);
            return;
        }
        if (this.f817b == null) {
            this.f813a.bindService(intent, new bu(this), 1);
            this.f818b = true;
            this.f815a.clear();
            this.f815a.add(a(intent));
        } else {
            try {
                this.f817b.send(a(intent));
            } catch (RemoteException unused) {
                this.f817b = null;
                this.f818b = false;
            }
        }
    }

    public final boolean a() {
        if (com.xiaomi.push.ae.e) {
            return false;
        }
        try {
            PackageInfo packageInfo = this.f813a.getPackageManager().getPackageInfo(com.xiaomi.stat.c.c.a, 4);
            if (packageInfo == null) {
                return false;
            }
            return packageInfo.versionCode >= 104;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean startServiceSafely(Intent intent) {
        try {
            if (com.xiaomi.push.m.m2399a() || Build.VERSION.SDK_INT < 26) {
                this.f813a.startService(intent);
                return true;
            }
            m2410a(intent);
            return true;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            return false;
        }
    }
}
