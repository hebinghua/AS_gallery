package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import com.xiaomi.push.hj;
import com.xiaomi.push.hn;
import com.xiaomi.push.hw;
import com.xiaomi.push.ii;
import com.xiaomi.push.service.bz;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class MiTinyDataClient {

    /* loaded from: classes3.dex */
    public static class a {
        public static volatile a a;

        /* renamed from: a  reason: collision with other field name */
        public Context f26a;

        /* renamed from: a  reason: collision with other field name */
        public Boolean f28a;

        /* renamed from: a  reason: collision with other field name */
        public String f29a;

        /* renamed from: a  reason: collision with other field name */
        public C0108a f27a = new C0108a();

        /* renamed from: a  reason: collision with other field name */
        public final ArrayList<hn> f30a = new ArrayList<>();

        /* renamed from: com.xiaomi.mipush.sdk.MiTinyDataClient$a$a  reason: collision with other inner class name */
        /* loaded from: classes3.dex */
        public class C0108a {

            /* renamed from: a  reason: collision with other field name */
            public ScheduledFuture<?> f33a;

            /* renamed from: a  reason: collision with other field name */
            public ScheduledThreadPoolExecutor f34a = new ScheduledThreadPoolExecutor(1);

            /* renamed from: a  reason: collision with other field name */
            public final ArrayList<hn> f32a = new ArrayList<>();

            /* renamed from: a  reason: collision with other field name */
            public final Runnable f31a = new ab(this);

            public C0108a() {
            }

            public final void a() {
                if (this.f33a == null) {
                    this.f33a = this.f34a.scheduleAtFixedRate(this.f31a, 1000L, 1000L, TimeUnit.MILLISECONDS);
                }
            }

            public void a(hn hnVar) {
                this.f34a.execute(new aa(this, hnVar));
            }

            public final void b() {
                hn remove = this.f32a.remove(0);
                for (ii iiVar : bz.a(Arrays.asList(remove), a.this.f26a.getPackageName(), b.m1906a(a.this.f26a).m1907a(), 30720)) {
                    com.xiaomi.channel.commonutils.logger.b.c("MiTinyDataClient Send item by PushServiceClient.sendMessage(XmActionNotification)." + remove.d());
                    ao.a(a.this.f26a).a((ao) iiVar, hj.Notification, true, (hw) null);
                }
            }
        }

        public static a a() {
            if (a == null) {
                synchronized (a.class) {
                    if (a == null) {
                        a = new a();
                    }
                }
            }
            return a;
        }

        public void a(Context context) {
            if (context == null) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("context is null, MiTinyDataClientImp.init() failed.");
                return;
            }
            this.f26a = context;
            this.f28a = Boolean.valueOf(m1881a(context));
            b("com.xiaomi.xmpushsdk.tinydataPending.init");
        }

        public final void a(hn hnVar) {
            synchronized (this.f30a) {
                if (!this.f30a.contains(hnVar)) {
                    this.f30a.add(hnVar);
                    if (this.f30a.size() > 100) {
                        this.f30a.remove(0);
                    }
                }
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m1880a() {
            return this.f26a != null;
        }

        /* renamed from: a  reason: collision with other method in class */
        public final boolean m1881a(Context context) {
            if (!ao.a(context).m1897a()) {
                return true;
            }
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(com.xiaomi.stat.c.c.a, 4);
                if (packageInfo == null) {
                    return false;
                }
                return packageInfo.versionCode >= 108;
            } catch (Exception unused) {
                return false;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:47:0x00a3, code lost:
            r0 = "MiTinyDataClient Pending " + r6.b() + " reason is com.xiaomi.xmpushsdk.tinydataPending.channel";
         */
        /* renamed from: a  reason: collision with other method in class */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public synchronized boolean m1882a(com.xiaomi.push.hn r6) {
            /*
                Method dump skipped, instructions count: 274
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.MiTinyDataClient.a.m1882a(com.xiaomi.push.hn):boolean");
        }

        public void b(String str) {
            com.xiaomi.channel.commonutils.logger.b.c("MiTinyDataClient.processPendingList(" + str + ")");
            ArrayList arrayList = new ArrayList();
            synchronized (this.f30a) {
                arrayList.addAll(this.f30a);
                this.f30a.clear();
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                m1882a((hn) it.next());
            }
        }

        public final boolean b(Context context) {
            return b.m1906a(context).m1907a() == null && !m1881a(this.f26a);
        }

        public final boolean b(hn hnVar) {
            if (bz.a(hnVar, false)) {
                return false;
            }
            if (!this.f28a.booleanValue()) {
                this.f27a.a(hnVar);
                return true;
            }
            com.xiaomi.channel.commonutils.logger.b.c("MiTinyDataClient Send item by PushServiceClient.sendTinyData(ClientUploadDataItem)." + hnVar.d());
            ao.a(this.f26a).a(hnVar);
            return true;
        }
    }

    public static boolean upload(Context context, hn hnVar) {
        com.xiaomi.channel.commonutils.logger.b.c("MiTinyDataClient.upload " + hnVar.d());
        if (!a.a().m1880a()) {
            a.a().a(context);
        }
        return a.a().m1882a(hnVar);
    }

    public static boolean upload(String str, String str2, long j, String str3) {
        hn hnVar = new hn();
        hnVar.d(str);
        hnVar.c(str2);
        hnVar.a(j);
        hnVar.b(str3);
        return a.a().m1882a(hnVar);
    }
}
