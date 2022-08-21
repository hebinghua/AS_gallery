package com.xiaomi.push.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.NodeType;
import com.meicam.sdk.NvsMediaFileConvertor;
import com.xiaomi.push.Cif;
import com.xiaomi.push.al;
import com.xiaomi.push.cv;
import com.xiaomi.push.dd;
import com.xiaomi.push.df;
import com.xiaomi.push.ed;
import com.xiaomi.push.eo;
import com.xiaomi.push.eu;
import com.xiaomi.push.fh;
import com.xiaomi.push.fj;
import com.xiaomi.push.fl;
import com.xiaomi.push.fs;
import com.xiaomi.push.fw;
import com.xiaomi.push.fx;
import com.xiaomi.push.fz;
import com.xiaomi.push.gb;
import com.xiaomi.push.gc;
import com.xiaomi.push.gh;
import com.xiaomi.push.gm;
import com.xiaomi.push.gn;
import com.xiaomi.push.hb;
import com.xiaomi.push.hd;
import com.xiaomi.push.hg;
import com.xiaomi.push.hj;
import com.xiaomi.push.ho;
import com.xiaomi.push.ii;
import com.xiaomi.push.ij;
import com.xiaomi.push.it;
import com.xiaomi.push.iz;
import com.xiaomi.push.service.bg;
import com.xiaomi.push.service.p;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class XMPushService extends Service implements fz {
    public static boolean b = false;

    /* renamed from: a  reason: collision with other field name */
    public ContentObserver f822a;

    /* renamed from: a  reason: collision with other field name */
    public fs f824a;

    /* renamed from: a  reason: collision with other field name */
    public fw f825a;

    /* renamed from: a  reason: collision with other field name */
    public fx f826a;

    /* renamed from: a  reason: collision with other field name */
    public a f828a;

    /* renamed from: a  reason: collision with other field name */
    public f f829a;

    /* renamed from: a  reason: collision with other field name */
    public k f830a;

    /* renamed from: a  reason: collision with other field name */
    public r f831a;

    /* renamed from: a  reason: collision with other field name */
    public t f832a;

    /* renamed from: a  reason: collision with other field name */
    public bq f834a;

    /* renamed from: a  reason: collision with other field name */
    public com.xiaomi.push.service.j f835a;

    /* renamed from: a  reason: collision with other field name */
    public String f838a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f841a = false;
    public int a = 0;

    /* renamed from: b  reason: collision with other field name */
    public int f842b = 0;

    /* renamed from: a  reason: collision with other field name */
    public long f821a = 0;

    /* renamed from: a  reason: collision with other field name */
    public Class f837a = XMJobService.class;

    /* renamed from: a  reason: collision with other field name */
    public be f833a = null;

    /* renamed from: a  reason: collision with other field name */
    public com.xiaomi.push.service.p f836a = null;

    /* renamed from: a  reason: collision with other field name */
    public Messenger f823a = null;

    /* renamed from: a  reason: collision with other field name */
    public Collection<ar> f840a = Collections.synchronizedCollection(new ArrayList());

    /* renamed from: a  reason: collision with other field name */
    public ArrayList<n> f839a = new ArrayList<>();

    /* renamed from: a  reason: collision with other field name */
    public gb f827a = new ci(this);

    /* loaded from: classes3.dex */
    public class a extends BroadcastReceiver {

        /* renamed from: a  reason: collision with other field name */
        public final Object f843a;

        public a() {
            this.f843a = new Object();
        }

        public /* synthetic */ a(XMPushService xMPushService, ci ciVar) {
            this();
        }

        public final void a() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                com.xiaomi.channel.commonutils.logger.b.d("[Alarm] Cannot perform lock.notifyAll in the UI thread!");
                return;
            }
            synchronized (this.f843a) {
                try {
                    this.f843a.notifyAll();
                } catch (Exception e) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("[Alarm] notify lock. " + e);
                }
            }
        }

        public final void a(long j) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                com.xiaomi.channel.commonutils.logger.b.d("[Alarm] Cannot perform lock.wait in the UI thread!");
                return;
            }
            synchronized (this.f843a) {
                try {
                    this.f843a.wait(j);
                } catch (InterruptedException e) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("[Alarm] interrupt from waiting state. " + e);
                }
            }
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            long currentTimeMillis = System.currentTimeMillis();
            com.xiaomi.channel.commonutils.logger.b.c("[Alarm] heartbeat alarm has been triggered.");
            if (!bk.p.equals(intent.getAction())) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("[Alarm] cancel the old ping timer");
                eu.a();
            } else if (!TextUtils.equals(context.getPackageName(), intent.getPackage())) {
            } else {
                com.xiaomi.channel.commonutils.logger.b.c("[Alarm] Ping XMChannelService on timer");
                try {
                    Intent intent2 = new Intent(context, XMPushService.class);
                    intent2.putExtra("time_stamp", System.currentTimeMillis());
                    intent2.setAction("com.xiaomi.push.timer");
                    ServiceClient.getInstance(context).startServiceSafely(intent2);
                    a(3000L);
                    com.xiaomi.channel.commonutils.logger.b.m1859a("[Alarm] heartbeat alarm finish in " + (System.currentTimeMillis() - currentTimeMillis));
                } catch (Throwable unused) {
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class b extends j {

        /* renamed from: a  reason: collision with other field name */
        public bg.b f844a;

        public b(bg.b bVar) {
            super(9);
            this.f844a = null;
            this.f844a = bVar;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "bind the client. " + this.f844a.g;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            String str;
            try {
                if (!XMPushService.this.m2428c()) {
                    com.xiaomi.channel.commonutils.logger.b.d("trying bind while the connection is not created, quit!");
                    return;
                }
                bg a = bg.a();
                bg.b bVar = this.f844a;
                bg.b a2 = a.a(bVar.g, bVar.f916b);
                if (a2 == null) {
                    str = "ignore bind because the channel " + this.f844a.g + " is removed ";
                } else if (a2.f911a == bg.c.unbind) {
                    a2.a(bg.c.binding, 0, 0, (String) null, (String) null);
                    XMPushService.this.f825a.a(a2);
                    fj.a(XMPushService.this, a2);
                    return;
                } else {
                    str = "trying duplicate bind, ingore! " + a2.f911a;
                }
                com.xiaomi.channel.commonutils.logger.b.m1859a(str);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.d("Meet error when trying to bind. " + e);
                XMPushService.this.a(10, e);
            } catch (Throwable unused) {
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class c extends j {
        public final bg.b a;

        public c(bg.b bVar) {
            super(12);
            this.a = bVar;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "bind time out. chid=" + this.a.g;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            this.a.a(bg.c.unbind, 1, 21, (String) null, (String) null);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof c)) {
                return false;
            }
            return TextUtils.equals(((c) obj).a.g, this.a.g);
        }

        public int hashCode() {
            return this.a.g.hashCode();
        }
    }

    /* loaded from: classes3.dex */
    public class d extends j {
        public fl a;

        public d(fl flVar) {
            super(8);
            this.a = null;
            this.a = flVar;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public fl mo2550a() {
            return this.a;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public String mo2550a() {
            return "receive a message.";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public void mo2550a() {
            ao aoVar = this.a.f357a;
            if (aoVar != null) {
                aoVar.c = System.currentTimeMillis();
            }
            XMPushService.this.f833a.a(this.a);
        }
    }

    /* loaded from: classes3.dex */
    public class e extends j {
        public e() {
            super(1);
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "do reconnect..";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            if (XMPushService.this.m2421a()) {
                XMPushService xMPushService = XMPushService.this;
                if (xMPushService.a(xMPushService.getApplicationContext())) {
                    XMPushService.this.f();
                    return;
                }
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("should not connect. quit the job.");
        }
    }

    /* loaded from: classes3.dex */
    public class f extends BroadcastReceiver {
        public f() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("network changed, " + com.xiaomi.push.m.a(intent));
            XMPushService.this.onStart(intent, 1);
        }
    }

    /* loaded from: classes3.dex */
    public class g extends j {

        /* renamed from: a  reason: collision with other field name */
        public Exception f846a;
        public int b;

        public g(int i, Exception exc) {
            super(2);
            this.b = i;
            this.f846a = exc;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "disconnect the connection.";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            XMPushService.this.a(this.b, this.f846a);
        }
    }

    /* loaded from: classes3.dex */
    public class h extends j {
        public h() {
            super(NvsMediaFileConvertor.CONVERTOR_ERROR_UNKNOWN);
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "Init Job";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            XMPushService.this.c();
        }
    }

    /* loaded from: classes3.dex */
    public class i extends j {
        public Intent a;

        public i(Intent intent) {
            super(15);
            this.a = null;
            this.a = intent;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public Intent mo2550a() {
            return this.a;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public String mo2550a() {
            return "Handle intent action = " + this.a.getAction();
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public void mo2550a() {
            XMPushService.this.d(this.a);
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class j extends p.b {
        public j(int i) {
            super(i);
        }

        /* renamed from: a */
        public abstract String mo2550a();

        public abstract void a();

        @Override // java.lang.Runnable
        public void run() {
            int i = this.a;
            if (i != 4 && i != 8) {
                com.xiaomi.channel.commonutils.logger.b.m1860a(com.xiaomi.channel.commonutils.logger.a.a, mo2550a());
            }
            a();
        }
    }

    /* loaded from: classes3.dex */
    public class k extends BroadcastReceiver {
        public k() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] hold short heartbeat, " + com.xiaomi.push.m.a(intent));
            if (intent == null || intent.getExtras() == null) {
                return;
            }
            XMPushService.this.onStart(intent, 1);
        }
    }

    /* loaded from: classes3.dex */
    public class l extends j {
        public l() {
            super(5);
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "ask the job queue to quit";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            XMPushService.this.f836a.m2531a();
        }
    }

    /* loaded from: classes3.dex */
    public class m extends j {
        public gn a;

        public m(gn gnVar) {
            super(8);
            this.a = null;
            this.a = gnVar;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "receive a message.";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            XMPushService.this.f833a.a(this.a);
        }
    }

    /* loaded from: classes3.dex */
    public interface n {
        /* renamed from: a */
        void mo2222a();
    }

    /* loaded from: classes3.dex */
    public class o extends j {

        /* renamed from: a  reason: collision with other field name */
        public boolean f849a;

        public o(boolean z) {
            super(4);
            this.f849a = z;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "send ping..";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            if (XMPushService.this.m2428c()) {
                try {
                    if (!this.f849a) {
                        fj.a();
                    }
                    XMPushService.this.f825a.b(this.f849a);
                } catch (gh e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                    XMPushService.this.a(10, e);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class p extends j {

        /* renamed from: a  reason: collision with other field name */
        public bg.b f850a;

        public p(bg.b bVar) {
            super(4);
            this.f850a = null;
            this.f850a = bVar;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "rebind the client. " + this.f850a.g;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            try {
                this.f850a.a(bg.c.unbind, 1, 16, (String) null, (String) null);
                fw fwVar = XMPushService.this.f825a;
                bg.b bVar = this.f850a;
                fwVar.a(bVar.g, bVar.f916b);
                XMPushService xMPushService = XMPushService.this;
                xMPushService.a(new b(this.f850a), 300L);
            } catch (gh e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
                XMPushService.this.a(10, e);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class q extends j {
        public q() {
            super(3);
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "reset the connection.";
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            XMPushService.this.a(11, (Exception) null);
            if (XMPushService.this.m2421a()) {
                XMPushService xMPushService = XMPushService.this;
                if (!xMPushService.a(xMPushService.getApplicationContext())) {
                    return;
                }
                XMPushService.this.f();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class r extends BroadcastReceiver {
        public r() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            XMPushService.this.onStart(intent, 1);
        }
    }

    /* loaded from: classes3.dex */
    public class s extends j {

        /* renamed from: a  reason: collision with other field name */
        public bg.b f851a;

        /* renamed from: a  reason: collision with other field name */
        public String f852a;
        public int b;

        /* renamed from: b  reason: collision with other field name */
        public String f853b;

        public s(bg.b bVar, int i, String str, String str2) {
            super(9);
            this.f851a = null;
            this.f851a = bVar;
            this.b = i;
            this.f852a = str;
            this.f853b = str2;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a */
        public String mo2550a() {
            return "unbind the channel. " + this.f851a.g;
        }

        @Override // com.xiaomi.push.service.XMPushService.j
        /* renamed from: a  reason: collision with other method in class */
        public void mo2550a() {
            if (this.f851a.f911a != bg.c.unbind && XMPushService.this.f825a != null) {
                try {
                    fw fwVar = XMPushService.this.f825a;
                    bg.b bVar = this.f851a;
                    fwVar.a(bVar.g, bVar.f916b);
                } catch (gh e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                    XMPushService.this.a(10, e);
                }
            }
            this.f851a.a(bg.c.unbind, this.b, 0, this.f853b, this.f852a);
        }
    }

    /* loaded from: classes3.dex */
    public class t extends BroadcastReceiver {
        public t() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!XMPushService.this.f841a) {
                XMPushService.this.f841a = true;
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] wifi changed, " + com.xiaomi.push.m.a(intent));
            XMPushService.this.onStart(intent, 1);
        }
    }

    public static void a(String str) {
        String str2;
        String str3;
        if (com.xiaomi.push.q.China.name().equals(str)) {
            cv.a("cn.app.chat.xiaomi.net", "cn.app.chat.xiaomi.net");
            cv.a("cn.app.chat.xiaomi.net", "111.13.141.211:443");
            cv.a("cn.app.chat.xiaomi.net", "39.156.81.172:443");
            cv.a("cn.app.chat.xiaomi.net", "111.202.1.250:443");
            cv.a("cn.app.chat.xiaomi.net", "123.125.102.213:443");
            str2 = "resolver.msg.xiaomi.net";
            cv.a(str2, "111.13.142.153:443");
            str3 = "111.202.1.252:443";
        } else {
            cv.a("app.chat.global.xiaomi.net", "app.chat.global.xiaomi.net");
            str2 = "resolver.msg.global.xiaomi.net";
            cv.a(str2, "161.117.97.14:443");
            str3 = "161.117.180.178:443";
        }
        cv.a(str2, str3);
    }

    public static boolean e() {
        return b;
    }

    public fw a() {
        return this.f825a;
    }

    public final gn a(gn gnVar, String str, String str2) {
        StringBuilder sb;
        String str3;
        bg a2 = bg.a();
        List<String> m2491a = a2.m2491a(str);
        if (m2491a.isEmpty()) {
            sb = new StringBuilder();
            str3 = "open channel should be called first before sending a packet, pkg=";
        } else {
            gnVar.o(str);
            str = gnVar.k();
            if (TextUtils.isEmpty(str)) {
                str = m2491a.get(0);
                gnVar.l(str);
            }
            bg.b a3 = a2.a(str, gnVar.m());
            if (!m2428c()) {
                sb = new StringBuilder();
                str3 = "drop a packet as the channel is not connected, chid=";
            } else if (a3 != null && a3.f911a == bg.c.binded) {
                if (TextUtils.equals(str2, a3.i)) {
                    return gnVar;
                }
                sb = new StringBuilder();
                sb.append("invalid session. ");
                sb.append(str2);
                com.xiaomi.channel.commonutils.logger.b.m1859a(sb.toString());
                return null;
            } else {
                sb = new StringBuilder();
                str3 = "drop a packet as the channel is not opened, chid=";
            }
        }
        sb.append(str3);
        sb.append(str);
        com.xiaomi.channel.commonutils.logger.b.m1859a(sb.toString());
        return null;
    }

    public final bg.b a(String str, Intent intent) {
        bg.b a2 = bg.a().a(str, intent.getStringExtra(bk.q));
        if (a2 == null) {
            a2 = new bg.b(this);
        }
        a2.g = intent.getStringExtra(bk.t);
        a2.f916b = intent.getStringExtra(bk.q);
        a2.c = intent.getStringExtra(bk.v);
        a2.f913a = intent.getStringExtra(bk.B);
        a2.e = intent.getStringExtra(bk.z);
        a2.f = intent.getStringExtra(bk.A);
        a2.f915a = intent.getBooleanExtra(bk.y, false);
        a2.h = intent.getStringExtra(bk.x);
        a2.i = intent.getStringExtra(bk.F);
        a2.d = intent.getStringExtra(bk.w);
        a2.f912a = this.f835a;
        a2.a((Messenger) intent.getParcelableExtra(bk.J));
        a2.f905a = getApplicationContext();
        bg.a().a(a2);
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public com.xiaomi.push.service.j m2418a() {
        return new com.xiaomi.push.service.j();
    }

    /* renamed from: a  reason: collision with other method in class */
    public final String m2419a() {
        String m2397a = com.xiaomi.push.m.m2397a("ro.miui.region");
        return TextUtils.isEmpty(m2397a) ? com.xiaomi.push.m.m2397a("ro.product.locale.region") : m2397a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2420a() {
        if (SystemClock.elapsedRealtime() - this.f821a >= gc.a() && com.xiaomi.push.bj.d(this)) {
            b(true);
        }
    }

    public void a(int i2) {
        this.f836a.a(i2);
    }

    public void a(int i2, Exception exc) {
        StringBuilder sb = new StringBuilder();
        sb.append("disconnect ");
        sb.append(hashCode());
        sb.append(", ");
        fw fwVar = this.f825a;
        sb.append(fwVar == null ? null : Integer.valueOf(fwVar.hashCode()));
        com.xiaomi.channel.commonutils.logger.b.m1859a(sb.toString());
        fw fwVar2 = this.f825a;
        if (fwVar2 != null) {
            fwVar2.b(i2, exc);
            this.f825a = null;
        }
        a(7);
        a(4);
        bg.a().a(this, i2);
    }

    public final void a(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver != null) {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (IllegalArgumentException e2) {
                com.xiaomi.channel.commonutils.logger.b.a(e2);
            }
        }
    }

    public final void a(Intent intent) {
        Bundle extras;
        if (intent == null || (extras = intent.getExtras()) == null) {
            return;
        }
        com.xiaomi.push.service.o.a(getApplicationContext()).a(extras.getString("digest"));
    }

    public final void a(Intent intent, int i2) {
        byte[] byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
        boolean booleanExtra = intent.getBooleanExtra("com.xiaomi.mipush.MESSAGE_CACHE", true);
        ii iiVar = new ii();
        try {
            it.a(iiVar, byteArrayExtra);
            com.xiaomi.push.al.a(getApplicationContext()).a((al.a) new com.xiaomi.push.service.b(iiVar, new WeakReference(this), booleanExtra), i2);
        } catch (iz unused) {
            com.xiaomi.channel.commonutils.logger.b.d("aw_ping : send help app ping  error");
        }
    }

    public void a(fl flVar) {
        fw fwVar = this.f825a;
        if (fwVar != null) {
            fwVar.b(flVar);
            return;
        }
        throw new gh("try send msg while connection is null.");
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar) {
        com.xiaomi.channel.commonutils.logger.b.c("begin to connect...");
        fh.a().a(fwVar);
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar, int i2, Exception exc) {
        fh.a().a(fwVar, i2, exc);
        if (!m2434i()) {
            a(false);
        }
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar, Exception exc) {
        fh.a().a(fwVar, exc);
        c(false);
        if (!m2434i()) {
            a(false);
        }
    }

    public void a(j jVar) {
        a(jVar, 0L);
    }

    public void a(j jVar, long j2) {
        try {
            this.f836a.a(jVar, j2);
        } catch (IllegalStateException e2) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("can't execute job err = " + e2.getMessage());
        }
    }

    public void a(n nVar) {
        synchronized (this.f839a) {
            this.f839a.add(nVar);
        }
    }

    public final void a(com.xiaomi.push.service.a aVar) {
        String str;
        String str2;
        if (aVar == null || !TextUtils.isEmpty(aVar.b()) || TextUtils.isEmpty(aVar.a())) {
            str = "no need to check country code";
        } else {
            String m2419a = com.xiaomi.stat.c.c.a.equals(getPackageName()) ? m2419a() : com.xiaomi.push.m.b();
            if (!TextUtils.isEmpty(m2419a)) {
                String name = com.xiaomi.push.m.a(m2419a).name();
                if (TextUtils.equals(name, aVar.a())) {
                    aVar.b(m2419a);
                    str2 = "update country code";
                } else {
                    str2 = "not update country code, because not equals " + name;
                }
                com.xiaomi.channel.commonutils.logger.b.m1859a(str2);
                return;
            }
            str = "check no country code";
        }
        com.xiaomi.channel.commonutils.logger.b.b(str);
    }

    public void a(bg.b bVar) {
        if (bVar != null) {
            long a2 = bVar.a();
            com.xiaomi.channel.commonutils.logger.b.m1859a("schedule rebind job in " + (a2 / 1000));
            a(new b(bVar), a2);
        }
    }

    public final void a(String str, int i2) {
        Collection<bg.b> m2490a = bg.a().m2490a(str);
        if (m2490a != null) {
            for (bg.b bVar : m2490a) {
                if (bVar != null) {
                    a(new s(bVar, i2, null, null));
                }
            }
        }
        bg.a().m2493a(str);
    }

    public void a(String str, String str2, int i2, String str3, String str4) {
        bg.b a2 = bg.a().a(str, str2);
        if (a2 != null) {
            a(new s(a2, i2, str4, str3));
        }
        bg.a().m2494a(str, str2);
    }

    public void a(String str, byte[] bArr, boolean z) {
        Collection<bg.b> m2490a = bg.a().m2490a("5");
        if (m2490a.isEmpty()) {
            if (!z) {
                return;
            }
        } else if (m2490a.iterator().next().f911a == bg.c.binded) {
            a(new cj(this, 4, str, bArr));
            return;
        } else if (!z) {
            return;
        }
        x.b(str, bArr);
    }

    public void a(boolean z) {
        this.f834a.a(z);
    }

    public void a(byte[] bArr, String str) {
        if (bArr == null) {
            x.a(this, str, bArr, 70000003, "null payload");
            com.xiaomi.channel.commonutils.logger.b.m1859a("register request without payload");
            return;
        }
        Cif cif = new Cif();
        try {
            it.a(cif, bArr);
            if (cif.f609a == hj.Registration) {
                ij ijVar = new ij();
                try {
                    it.a(ijVar, cif.m2299a());
                    a(new w(this, cif.b(), ijVar.b(), ijVar.c(), bArr));
                    eo.a(getApplicationContext()).a(cif.b(), "E100003", ijVar.a(), NodeType.E_TRAFFIC_UGC, null);
                } catch (iz e2) {
                    com.xiaomi.channel.commonutils.logger.b.d("app register error. " + e2);
                    x.a(this, str, bArr, 70000003, " data action error.");
                }
            } else {
                x.a(this, str, bArr, 70000003, " registration action required.");
                com.xiaomi.channel.commonutils.logger.b.m1859a("register request with invalid payload");
            }
        } catch (iz e3) {
            com.xiaomi.channel.commonutils.logger.b.d("app register fail. " + e3);
            x.a(this, str, bArr, 70000003, " data container error.");
        }
    }

    public void a(fl[] flVarArr) {
        fw fwVar = this.f825a;
        if (fwVar != null) {
            fwVar.a(flVarArr);
            return;
        }
        throw new gh("try send msg while connection is null.");
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2421a() {
        boolean b2 = com.xiaomi.push.bj.b(this);
        boolean z = bg.a().m2488a() > 0;
        boolean z2 = !m2427b();
        boolean m2433h = m2433h();
        boolean z3 = !m2432g();
        boolean z4 = b2 && z && z2 && m2433h && z3;
        if (!z4) {
            com.xiaomi.channel.commonutils.logger.b.e(String.format("not conn, net=%s;cnt=%s;!dis=%s;enb=%s;!spm=%s;", Boolean.valueOf(b2), Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(m2433h), Boolean.valueOf(z3)));
        }
        return z4;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2422a(int i2) {
        return this.f836a.m2533a(i2);
    }

    public final boolean a(Context context) {
        try {
            com.xiaomi.push.ar.a();
            for (int i2 = 100; i2 > 0; i2--) {
                if (com.xiaomi.push.bj.c(context)) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("network connectivity ok.");
                    return true;
                }
                try {
                    Thread.sleep(100L);
                } catch (Exception unused) {
                }
            }
            return false;
        } catch (Exception unused2) {
            return true;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public final boolean m2423a(String str, Intent intent) {
        bg.b a2 = bg.a().a(str, intent.getStringExtra(bk.q));
        boolean z = false;
        if (a2 != null && str != null) {
            String stringExtra = intent.getStringExtra(bk.F);
            String stringExtra2 = intent.getStringExtra(bk.x);
            if (!TextUtils.isEmpty(a2.i) && !TextUtils.equals(stringExtra, a2.i)) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("session changed. old session=" + a2.i + ", new session=" + stringExtra + " chid = " + str);
                z = true;
            }
            if (!stringExtra2.equals(a2.h)) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("security changed. chid = " + str + " sechash = " + com.xiaomi.push.bo.a(stringExtra2));
                return true;
            }
        }
        return z;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final int[] m2424a() {
        String[] split;
        String a2 = ba.a(getApplicationContext()).a(ho.FallDownTimeRange.a(), "");
        if (!TextUtils.isEmpty(a2) && (split = a2.split(",")) != null && split.length >= 2) {
            int[] iArr = new int[2];
            try {
                iArr[0] = Integer.valueOf(split[0]).intValue();
                iArr[1] = Integer.valueOf(split[1]).intValue();
                if (iArr[0] >= 0 && iArr[0] <= 23 && iArr[1] >= 0 && iArr[1] <= 23) {
                    if (iArr[0] != iArr[1]) {
                        return iArr;
                    }
                }
            } catch (NumberFormatException e2) {
                com.xiaomi.channel.commonutils.logger.b.d("parse falldown time range failure: " + e2);
            }
        }
        return null;
    }

    public com.xiaomi.push.service.j b() {
        return this.f835a;
    }

    /* renamed from: b  reason: collision with other method in class */
    public final String m2425b() {
        String b2;
        com.xiaomi.push.ar.a();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Object obj = new Object();
        String str = null;
        int i2 = 0;
        if (com.xiaomi.stat.c.c.a.equals(getPackageName())) {
            bn a2 = bn.a(this);
            String str2 = null;
            while (true) {
                if (!TextUtils.isEmpty(str2) && a2.a() != 0) {
                    break;
                }
                if (TextUtils.isEmpty(str2)) {
                    str2 = m2419a();
                }
                try {
                    synchronized (obj) {
                        if (i2 < 30) {
                            obj.wait(1000L);
                        } else {
                            obj.wait(30000L);
                        }
                    }
                } catch (InterruptedException unused) {
                }
                i2++;
            }
            b2 = m2419a();
        } else {
            b2 = com.xiaomi.push.m.b();
        }
        if (!TextUtils.isEmpty(b2)) {
            com.xiaomi.push.service.a.a(getApplicationContext()).b(b2);
            str = com.xiaomi.push.m.a(b2).name();
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("wait region :" + str + " cost = " + (SystemClock.elapsedRealtime() - elapsedRealtime) + " , count = " + i2);
        return str;
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2426b() {
        com.xiaomi.push.service.o.a(getApplicationContext()).d();
        Iterator it = new ArrayList(this.f839a).iterator();
        while (it.hasNext()) {
            ((n) it.next()).mo2222a();
        }
    }

    public final void b(Intent intent) {
        long j2;
        String str;
        fl flVar;
        String stringExtra = intent.getStringExtra(bk.B);
        String stringExtra2 = intent.getStringExtra(bk.F);
        Bundle bundleExtra = intent.getBundleExtra("ext_packet");
        bg a2 = bg.a();
        if (bundleExtra != null) {
            gm gmVar = (gm) a(new gm(bundleExtra), stringExtra, stringExtra2);
            if (gmVar == null) {
                return;
            }
            flVar = fl.a(gmVar, a2.a(gmVar.k(), gmVar.m()).h);
        } else {
            byte[] byteArrayExtra = intent.getByteArrayExtra("ext_raw_packet");
            if (byteArrayExtra != null) {
                try {
                    j2 = Long.parseLong(intent.getStringExtra(bk.q));
                } catch (NumberFormatException unused) {
                    j2 = 0;
                }
                String stringExtra3 = intent.getStringExtra(bk.r);
                String stringExtra4 = intent.getStringExtra(bk.s);
                String stringExtra5 = intent.getStringExtra("ext_chid");
                bg.b a3 = a2.a(stringExtra5, String.valueOf(j2));
                if (a3 != null) {
                    fl flVar2 = new fl();
                    if ("10".equals(stringExtra5)) {
                        flVar2.b(Integer.parseInt("10"));
                        flVar2.f357a.f881a = intent.getBooleanExtra("screen_on", true);
                        flVar2.f357a.f883b = intent.getBooleanExtra("wifi", true);
                        str = stringExtra3;
                        flVar2.f357a.f880a = intent.getLongExtra("rx_msg", -1L);
                        flVar2.f357a.f882b = intent.getLongExtra("enqueue", -1L);
                        flVar2.f357a.b = intent.getIntExtra("num", -1);
                        flVar2.f357a.c = intent.getLongExtra("run", -1L);
                    } else {
                        str = stringExtra3;
                    }
                    try {
                        flVar2.a(Integer.parseInt(stringExtra5));
                    } catch (NumberFormatException unused2) {
                    }
                    flVar2.a("SECMSG", (String) null);
                    flVar2.a(j2, TextUtils.isEmpty(str) ? "xiaomi.com" : str, stringExtra4);
                    flVar2.a(intent.getStringExtra("ext_pkt_id"));
                    flVar2.a(byteArrayExtra, a3.h);
                    com.xiaomi.channel.commonutils.logger.b.m1859a("send a message: chid=" + stringExtra5 + ", packetId=" + intent.getStringExtra("ext_pkt_id"));
                    flVar = flVar2;
                }
            }
            flVar = null;
        }
        if (flVar != null) {
            c(new bt(this, flVar));
        }
    }

    @Override // com.xiaomi.push.fz
    public void b(fw fwVar) {
        fh.a().b(fwVar);
        c(true);
        this.f834a.m2503a();
        if (!eu.m2141a() && !m2434i()) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("reconnection successful, reactivate alarm.");
            eu.a(true);
        }
        Iterator<bg.b> it = bg.a().m2489a().iterator();
        while (it.hasNext()) {
            a(new b(it.next()));
        }
        if (this.f841a || !com.xiaomi.push.m.m2400a(getApplicationContext())) {
            return;
        }
        com.xiaomi.push.al.a(getApplicationContext()).a(new cm(this));
    }

    public void b(j jVar) {
        this.f836a.a(jVar.a, jVar);
    }

    public final void b(boolean z) {
        this.f821a = SystemClock.elapsedRealtime();
        if (m2428c()) {
            if (com.xiaomi.push.bj.b(this)) {
                c(new o(z));
                return;
            }
            c(new g(17, null));
        }
        a(true);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m2427b() {
        try {
            Class<?> a2 = com.xiaomi.push.v.a(this, "miui.os.Build");
            Field field = a2.getField("IS_CM_CUSTOMIZATION_TEST");
            Field field2 = a2.getField("IS_CU_CUSTOMIZATION_TEST");
            Field field3 = a2.getField("IS_CT_CUSTOMIZATION_TEST");
            if (!field.getBoolean(null) && !field2.getBoolean(null)) {
                if (!field3.getBoolean(null)) {
                    return false;
                }
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public final void c() {
        String str;
        com.xiaomi.push.service.a a2 = com.xiaomi.push.service.a.a(getApplicationContext());
        String a3 = a2.a();
        com.xiaomi.channel.commonutils.logger.b.m1859a("region of cache is " + a3);
        if (TextUtils.isEmpty(a3)) {
            a3 = m2425b();
        } else {
            a(a2);
        }
        if (!TextUtils.isEmpty(a3)) {
            this.f838a = a3;
            a2.a(a3);
            if (com.xiaomi.push.q.Global.name().equals(this.f838a)) {
                str = "app.chat.global.xiaomi.net";
            } else if (com.xiaomi.push.q.Europe.name().equals(this.f838a)) {
                str = "fr.app.chat.global.xiaomi.net";
            } else if (com.xiaomi.push.q.Russia.name().equals(this.f838a)) {
                str = "ru.app.chat.global.xiaomi.net";
            } else if (com.xiaomi.push.q.India.name().equals(this.f838a)) {
                str = "idmb.app.chat.global.xiaomi.net";
            }
            fx.a(str);
        } else {
            this.f838a = com.xiaomi.push.q.China.name();
        }
        if (com.xiaomi.push.q.China.name().equals(this.f838a)) {
            fx.a("cn.app.chat.xiaomi.net");
        }
        a(this.f838a);
        if (m2433h()) {
            cs csVar = new cs(this, 11);
            a(csVar);
            u.a(new ct(this, csVar));
        }
        try {
            if (!com.xiaomi.push.v.m2553a()) {
                return;
            }
            this.f835a.a(this);
        } catch (Exception e2) {
            com.xiaomi.channel.commonutils.logger.b.a(e2);
        }
    }

    public final void c(Intent intent) {
        String stringExtra = intent.getStringExtra(bk.B);
        String stringExtra2 = intent.getStringExtra(bk.F);
        Parcelable[] parcelableArrayExtra = intent.getParcelableArrayExtra("ext_packets");
        int length = parcelableArrayExtra.length;
        gm[] gmVarArr = new gm[length];
        intent.getBooleanExtra("ext_encrypt", true);
        for (int i2 = 0; i2 < parcelableArrayExtra.length; i2++) {
            gmVarArr[i2] = new gm((Bundle) parcelableArrayExtra[i2]);
            gmVarArr[i2] = (gm) a(gmVarArr[i2], stringExtra, stringExtra2);
            if (gmVarArr[i2] == null) {
                return;
            }
        }
        bg a2 = bg.a();
        fl[] flVarArr = new fl[length];
        for (int i3 = 0; i3 < length; i3++) {
            gm gmVar = gmVarArr[i3];
            flVarArr[i3] = fl.a(gmVar, a2.a(gmVar.k(), gmVar.m()).h);
        }
        c(new com.xiaomi.push.service.c(this, flVarArr));
    }

    public final void c(j jVar) {
        this.f836a.a(jVar);
    }

    public final void c(boolean z) {
        try {
            if (!com.xiaomi.push.v.m2553a()) {
                return;
            }
            if (!z) {
                sendBroadcast(new Intent("miui.intent.action.NETWORK_BLOCKED"));
                return;
            }
            sendBroadcast(new Intent("miui.intent.action.NETWORK_CONNECTED"));
            for (ar arVar : (ar[]) this.f840a.toArray(new ar[0])) {
                arVar.mo2512a();
            }
        } catch (Exception e2) {
            com.xiaomi.channel.commonutils.logger.b.a(e2);
        }
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m2428c() {
        fw fwVar = this.f825a;
        return fwVar != null && fwVar.m2184c();
    }

    public final void d() {
        NetworkInfo networkInfo;
        try {
            networkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e2) {
            com.xiaomi.channel.commonutils.logger.b.a(e2);
            networkInfo = null;
        }
        com.xiaomi.push.service.o.a(getApplicationContext()).a(networkInfo);
        if (networkInfo != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("network changed,");
            sb.append("[type: " + networkInfo.getTypeName() + "[" + networkInfo.getSubtypeName() + "], state: " + networkInfo.getState() + com.xiaomi.stat.b.h.g + networkInfo.getDetailedState());
            com.xiaomi.channel.commonutils.logger.b.m1859a(sb.toString());
            NetworkInfo.State state = networkInfo.getState();
            if (state == NetworkInfo.State.SUSPENDED || state == NetworkInfo.State.UNKNOWN) {
                return;
            }
        } else {
            com.xiaomi.channel.commonutils.logger.b.m1859a("network changed, no active network");
        }
        if (fh.a() != null) {
            fh.a().m2150a();
        }
        hb.m2221a((Context) this);
        this.f824a.d();
        if (com.xiaomi.push.bj.b(this)) {
            if (m2428c() && m2431f()) {
                b(false);
            }
            if (!m2428c() && !m2429d()) {
                this.f836a.a(1);
                a(new e());
            }
            df.a(this).a();
        } else {
            a(new g(2, null));
        }
        m2430e();
    }

    /* JADX WARN: Removed duplicated region for block: B:155:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0450  */
    /* JADX WARN: Removed duplicated region for block: B:376:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void d(android.content.Intent r12) {
        /*
            Method dump skipped, instructions count: 2303
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.XMPushService.d(android.content.Intent):void");
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m2429d() {
        fw fwVar = this.f825a;
        return fwVar != null && fwVar.m2183b();
    }

    /* renamed from: e  reason: collision with other method in class */
    public final void m2430e() {
        if (!m2421a()) {
            eu.a();
        } else if (eu.m2141a()) {
        } else {
            eu.a(true);
        }
    }

    public final void e(Intent intent) {
        int i2;
        try {
            ed.a(getApplicationContext()).a(new bm());
            String stringExtra = intent.getStringExtra("mipush_app_package");
            byte[] byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
            if (byteArrayExtra == null) {
                return;
            }
            ii iiVar = new ii();
            it.a(iiVar, byteArrayExtra);
            String b2 = iiVar.b();
            Map<String, String> m2309a = iiVar.m2309a();
            if (m2309a == null) {
                return;
            }
            String str = m2309a.get("extra_help_aw_info");
            String str2 = m2309a.get("extra_aw_app_online_cmd");
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            try {
                i2 = Integer.parseInt(str2);
            } catch (NumberFormatException unused) {
                i2 = 0;
            }
            int i3 = i2;
            if (TextUtils.isEmpty(stringExtra) || TextUtils.isEmpty(b2) || TextUtils.isEmpty(str)) {
                return;
            }
            ed.a(getApplicationContext()).a(this, str, i3, stringExtra, b2);
        } catch (iz e2) {
            com.xiaomi.channel.commonutils.logger.b.d("aw_logic: translate fail. " + e2.getMessage());
        }
    }

    public final void f() {
        String str;
        fw fwVar = this.f825a;
        if (fwVar == null || !fwVar.m2183b()) {
            fw fwVar2 = this.f825a;
            if (fwVar2 == null || !fwVar2.m2184c()) {
                this.f826a.b(com.xiaomi.push.bj.m1969a((Context) this));
                g();
                if (this.f825a != null) {
                    return;
                }
                bg.a().a(this);
                c(false);
                return;
            }
            str = "try to connect while is connected.";
        } else {
            str = "try to connect while connecting.";
        }
        com.xiaomi.channel.commonutils.logger.b.d(str);
    }

    /* renamed from: f  reason: collision with other method in class */
    public final boolean m2431f() {
        if (SystemClock.elapsedRealtime() - this.f821a < 30000) {
            return false;
        }
        return com.xiaomi.push.bj.d(this);
    }

    public final void g() {
        try {
            this.f824a.a(this.f827a, new cl(this));
            this.f824a.e();
            this.f825a = this.f824a;
        } catch (gh e2) {
            com.xiaomi.channel.commonutils.logger.b.a("fail to create Slim connection", e2);
            this.f824a.b(3, e2);
        }
    }

    /* renamed from: g  reason: collision with other method in class */
    public final boolean m2432g() {
        return com.xiaomi.stat.c.c.a.equals(getPackageName()) && Settings.System.getInt(getContentResolver(), "power_supersave_mode_open", 0) == 1;
    }

    public final void h() {
    }

    /* renamed from: h  reason: collision with other method in class */
    public final boolean m2433h() {
        return com.xiaomi.stat.c.c.a.equals(getPackageName()) || !v.a(this).m2547b(getPackageName());
    }

    public final void i() {
        synchronized (this.f839a) {
            this.f839a.clear();
        }
    }

    /* renamed from: i  reason: collision with other method in class */
    public final boolean m2434i() {
        return getApplicationContext().getPackageName().equals(com.xiaomi.stat.c.c.a) && j() && !com.xiaomi.push.j.m2366b((Context) this) && !com.xiaomi.push.j.m2364a(getApplicationContext());
    }

    public final boolean j() {
        int intValue = Integer.valueOf(String.format("%tH", new Date())).intValue();
        int i2 = this.a;
        int i3 = this.f842b;
        if (i2 > i3) {
            if (intValue >= i2 || intValue < i3) {
                return true;
            }
        } else if (i2 < i3 && intValue >= i2 && intValue < i3) {
            return true;
        }
        return false;
    }

    public final boolean k() {
        if (TextUtils.equals(getPackageName(), com.xiaomi.stat.c.c.a)) {
            return false;
        }
        return ba.a(this).a(ho.ForegroundServiceSwitch.a(), false);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.f823a.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        String[] split;
        super.onCreate();
        com.xiaomi.channel.commonutils.logger.b.a(getApplicationContext());
        com.xiaomi.push.v.a((Context) this);
        com.xiaomi.push.service.t m2542a = u.m2542a((Context) this);
        if (m2542a != null) {
            com.xiaomi.push.ae.a(m2542a.a);
        }
        if (com.xiaomi.push.m.m2400a(getApplicationContext())) {
            HandlerThread handlerThread = new HandlerThread("hb-alarm");
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper());
            this.f828a = new a(this, null);
            registerReceiver(this.f828a, new IntentFilter(bk.p), null, handler);
            b = true;
            handler.post(new cn(this));
        }
        this.f823a = new Messenger(new co(this));
        bl.a(this);
        cp cpVar = new cp(this, null, 5222, "xiaomi.com", null);
        this.f826a = cpVar;
        cpVar.a(true);
        this.f824a = new fs(this, this.f826a);
        this.f835a = m2418a();
        eu.a(this);
        this.f824a.a(this);
        this.f833a = new be(this);
        this.f834a = new bq(this);
        new com.xiaomi.push.service.k().a();
        fh.m2151a().a(this);
        this.f836a = new com.xiaomi.push.service.p("Connection Controller Thread");
        bg a2 = bg.a();
        a2.b();
        a2.a(new cq(this));
        if (k()) {
            h();
        }
        hg.a(this).a(new com.xiaomi.push.service.r(this), "UPLOADER_PUSH_CHANNEL");
        a(new hd(this));
        a(new cg(this));
        if (com.xiaomi.push.m.m2400a((Context) this)) {
            a(new bf());
        }
        a(new h());
        this.f840a.add(bx.a(this));
        if (m2433h()) {
            this.f829a = new f();
            registerReceiver(this.f829a, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        if (com.xiaomi.push.m.m2400a(getApplicationContext())) {
            this.f832a = new t();
            registerReceiver(this.f832a, new IntentFilter("miui.net.wifi.DIGEST_INFORMATION_CHANGED"), "miui.net.wifi.permission.ACCESS_WIFI_DIGEST_INFO", null);
            k kVar = new k();
            this.f830a = kVar;
            registerReceiver(kVar, new IntentFilter("com.xiaomi.xmsf.USE_INTELLIGENT_HB"), "com.xiaomi.xmsf.permission.INTELLIGENT_HB", null);
        }
        com.xiaomi.push.service.o.a(getApplicationContext()).m2521a();
        if (com.xiaomi.stat.c.c.a.equals(getPackageName())) {
            Uri uriFor = Settings.System.getUriFor("power_supersave_mode_open");
            if (uriFor != null) {
                this.f822a = new cr(this, new Handler(Looper.getMainLooper()));
                try {
                    getContentResolver().registerContentObserver(uriFor, false, this.f822a);
                } catch (Throwable th) {
                    com.xiaomi.channel.commonutils.logger.b.d("register super-power-mode observer err:" + th.getMessage());
                }
            }
            int[] m2424a = m2424a();
            if (m2424a != null) {
                this.f831a = new r();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.SCREEN_ON");
                intentFilter.addAction("android.intent.action.SCREEN_OFF");
                registerReceiver(this.f831a, intentFilter);
                this.a = m2424a[0];
                this.f842b = m2424a[1];
                com.xiaomi.channel.commonutils.logger.b.m1859a("falldown initialized: " + this.a + "," + this.f842b);
            }
        }
        String str = "";
        if (m2542a != null) {
            try {
                if (!TextUtils.isEmpty(m2542a.f983a) && (split = m2542a.f983a.split("@")) != null && split.length > 0) {
                    str = split[0];
                }
            } catch (Exception unused) {
            }
        }
        dd.a(this);
        com.xiaomi.channel.commonutils.logger.b.e("XMPushService created. pid=" + Process.myPid() + ", uid=" + Process.myUid() + ", vc=" + com.xiaomi.push.h.a(getApplicationContext(), getPackageName()) + ", uuid=" + str);
    }

    @Override // android.app.Service
    public void onDestroy() {
        f fVar = this.f829a;
        if (fVar != null) {
            a(fVar);
            this.f829a = null;
        }
        t tVar = this.f832a;
        if (tVar != null) {
            a(tVar);
            this.f832a = null;
        }
        k kVar = this.f830a;
        if (kVar != null) {
            a(kVar);
            this.f830a = null;
        }
        r rVar = this.f831a;
        if (rVar != null) {
            a(rVar);
            this.f831a = null;
        }
        a aVar = this.f828a;
        if (aVar != null) {
            a(aVar);
            this.f828a = null;
        }
        if (com.xiaomi.stat.c.c.a.equals(getPackageName()) && this.f822a != null) {
            try {
                getContentResolver().unregisterContentObserver(this.f822a);
            } catch (Throwable th) {
                com.xiaomi.channel.commonutils.logger.b.d("unregister super-power-mode err:" + th.getMessage());
            }
        }
        this.f840a.clear();
        this.f836a.b();
        a(new ck(this, 2));
        a(new l());
        bg.a().b();
        bg.a().a(this, 15);
        bg.a().m2492a();
        this.f824a.b(this);
        bv.a().m2510a();
        eu.a();
        i();
        super.onDestroy();
        com.xiaomi.channel.commonutils.logger.b.m1859a("Service destroyed");
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i2) {
        i iVar;
        long currentTimeMillis = System.currentTimeMillis();
        if (intent == null) {
            com.xiaomi.channel.commonutils.logger.b.d("onStart() with intent NULL");
        } else {
            com.xiaomi.channel.commonutils.logger.b.m1859a(String.format("onStart() with intent.Action = %s, chid = %s, pkg = %s|%s", intent.getAction(), intent.getStringExtra(bk.t), intent.getStringExtra(bk.B), intent.getStringExtra("mipush_app_package")));
        }
        if (intent != null && intent.getAction() != null) {
            if ("com.xiaomi.push.timer".equalsIgnoreCase(intent.getAction()) || "com.xiaomi.push.check_alive".equalsIgnoreCase(intent.getAction())) {
                if (this.f836a.m2532a()) {
                    com.xiaomi.channel.commonutils.logger.b.d("ERROR, the job controller is blocked.");
                    bg.a().a(this, 14);
                    stopSelf();
                } else {
                    iVar = new i(intent);
                    a(iVar);
                }
            } else if (!"com.xiaomi.push.network_status_changed".equalsIgnoreCase(intent.getAction())) {
                if ("10".equals(intent.getStringExtra("ext_chid"))) {
                    intent.putExtra("rx_msg", System.currentTimeMillis());
                    intent.putExtra("screen_on", com.xiaomi.push.w.a(getApplicationContext()));
                    intent.putExtra("wifi", com.xiaomi.push.bj.e(getApplicationContext()));
                }
                iVar = new i(intent);
                a(iVar);
            }
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (currentTimeMillis2 > 50) {
            com.xiaomi.channel.commonutils.logger.b.c("[Prefs] spend " + currentTimeMillis2 + " ms, too more times.");
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        onStart(intent, i3);
        return 1;
    }
}
