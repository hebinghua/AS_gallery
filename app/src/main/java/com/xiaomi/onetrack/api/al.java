package com.xiaomi.onetrack.api;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.miui.analytics.ITrack;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.util.p;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes3.dex */
public class al {
    public ITrack d;
    public AtomicBoolean e;
    public AtomicBoolean f;
    public final Object g;
    public Context h;
    public c j;
    public ServiceConnection k;
    public CopyOnWriteArrayList<b> l;

    /* loaded from: classes3.dex */
    public interface b {
        void a();
    }

    public static /* synthetic */ ITrack a(al alVar, ITrack iTrack) {
        alVar.d = iTrack;
        return iTrack;
    }

    public static /* synthetic */ Object a(al alVar) {
        return alVar.g;
    }

    public static /* synthetic */ AtomicBoolean b(al alVar) {
        return alVar.e;
    }

    public static /* synthetic */ AtomicBoolean c(al alVar) {
        return alVar.f;
    }

    public static /* synthetic */ void d(al alVar) {
        alVar.e();
    }

    public static /* synthetic */ ITrack e(al alVar) {
        return alVar.d;
    }

    public static /* synthetic */ ServiceConnection f(al alVar) {
        return alVar.k;
    }

    public static /* synthetic */ Context g(al alVar) {
        return alVar.h;
    }

    public static al a() {
        return a.a;
    }

    /* loaded from: classes3.dex */
    public static class a {
        public static al a = new al();
    }

    public al() {
        this.e = new AtomicBoolean(false);
        this.f = new AtomicBoolean(false);
        this.g = new Object();
        this.k = new ServiceConnection() { // from class: com.xiaomi.onetrack.api.ServiceConnectionManager$1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                int i;
                try {
                    synchronized (al.a(al.this)) {
                        i = 0;
                        al.b(al.this).set(false);
                        al.c(al.this).set(true);
                        al.a(al.this, ITrack.Stub.asInterface(iBinder));
                    }
                    al.d(al.this);
                    StringBuilder sb = new StringBuilder();
                    sb.append("onServiceConnected  mConnecting ");
                    sb.append(al.b(al.this));
                    sb.append(" mIOneTrackService ");
                    if (al.e(al.this) != null) {
                        i = 1;
                    }
                    sb.append(i);
                    sb.append(" pid:");
                    sb.append(Process.myPid());
                    sb.append(" tid:");
                    sb.append(Process.myTid());
                    p.a("ServiceConnectManager", sb.toString());
                } catch (Throwable th) {
                    p.a("ServiceConnectManager", "onServiceConnected throwable:" + th.getMessage());
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                int i;
                try {
                    synchronized (al.a(al.this)) {
                        al.a(al.this, null);
                        i = 0;
                        al.b(al.this).set(false);
                        al.c(al.this).set(false);
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("onServiceDisconnected:  mConnecting ");
                    sb.append(al.b(al.this));
                    sb.append(" mIOneTrackService ");
                    if (al.e(al.this) != null) {
                        i = 1;
                    }
                    sb.append(i);
                    p.a("ServiceConnectManager", sb.toString());
                } catch (Throwable th) {
                    p.a("ServiceConnectManager", "onServiceDisconnected throwable:" + th.getMessage());
                }
            }

            @Override // android.content.ServiceConnection
            public void onBindingDied(ComponentName componentName) {
                try {
                    synchronized (al.a(al.this)) {
                        al.a(al.this, null);
                        al.b(al.this).set(false);
                        al.c(al.this).set(false);
                    }
                    try {
                        al.g(al.this).unbindService(al.f(al.this));
                    } catch (Exception e) {
                        p.a("ServiceConnectManager", "onBindingDied: " + e.toString());
                    }
                } catch (Throwable th) {
                    p.a("ServiceConnectManager", "onBindingDied throwable:" + th.getMessage());
                }
            }

            @Override // android.content.ServiceConnection
            public void onNullBinding(ComponentName componentName) {
                try {
                    synchronized (al.a(al.this)) {
                        al.a(al.this, null);
                        al.b(al.this).set(false);
                        al.c(al.this).set(false);
                    }
                } catch (Throwable th) {
                    p.a("ServiceConnectManager", "onNullBinding throwable:" + th.getMessage());
                }
            }
        };
        this.l = new CopyOnWriteArrayList<>();
        this.h = com.xiaomi.onetrack.e.a.b();
        this.j = new c(Looper.getMainLooper());
        b();
    }

    public boolean a(String str, String str2, Configuration configuration) {
        boolean z;
        synchronized (this.g) {
            b();
            ITrack iTrack = this.d;
            z = false;
            if (iTrack != null) {
                try {
                    iTrack.trackEvent(configuration.getAppId(), com.xiaomi.onetrack.e.a.e(), str, str2);
                    z = true;
                } catch (RemoteException e) {
                    d();
                    this.e.set(false);
                    this.f.set(false);
                    this.d = null;
                    p.a("ServiceConnectManager", "track: " + e.toString());
                } catch (NullPointerException unused) {
                }
            }
        }
        return z;
    }

    public void b(String str, String str2, Configuration configuration) {
        try {
            synchronized (this.g) {
                this.d.trackEvent(configuration.getAppId(), com.xiaomi.onetrack.e.a.e(), str, str2);
            }
        } catch (Exception e) {
            p.b("ServiceConnectManager", "trackCacheData error:" + e.toString());
        }
    }

    public final void b() {
        if (!this.e.get() && (!this.f.get() || this.d == null)) {
            c();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ensureService mConnecting: ");
        sb.append(this.e.get());
        sb.append(" mIsBindSuccess:");
        sb.append(this.f.get());
        sb.append(" mAnalytics: ");
        sb.append(this.d == null ? 0 : 1);
        p.a("ServiceConnectManager", sb.toString());
    }

    public final void c() {
        try {
            Intent intent = new Intent();
            intent.setClassName("com.miui.analytics", "com.miui.analytics.onetrack.OneTrackService");
            boolean bindService = this.h.bindService(intent, this.k, 1);
            if (bindService) {
                this.e.set(true);
            } else {
                this.e.set(false);
                this.h.unbindService(this.k);
            }
            p.a("ServiceConnectManager", "bindService:  mConnecting: " + this.e + " bindResult:" + bindService);
        } catch (Exception e) {
            try {
                this.e.set(false);
                this.h.unbindService(this.k);
            } catch (Exception e2) {
                Log.d("ServiceConnectManager", "bindService e1: " + e2.toString());
            }
            p.b("ServiceConnectManager", "bindService e: " + e.toString());
        }
    }

    public final void d() {
        try {
            if (!this.f.get()) {
                return;
            }
            this.h.unbindService(this.k);
            this.f.set(false);
            p.a("ServiceConnectManager", "unBindService  mIsBindSuccess:" + this.f.get());
        } catch (Exception e) {
            p.a("ServiceConnectManager", "unBindService: " + e.toString());
        }
    }

    public void a(b bVar) {
        if (!this.l.contains(bVar)) {
            this.l.add(bVar);
        }
    }

    public final void e() {
        Iterator<b> it = this.l.iterator();
        while (it.hasNext()) {
            it.next().a();
        }
    }

    public void a(int i) {
        if (i == 2) {
            this.j.sendEmptyMessageDelayed(1, 5000L);
        } else if (!this.j.hasMessages(1)) {
        } else {
            this.j.removeMessages(1);
        }
    }

    /* loaded from: classes3.dex */
    public class c extends Handler {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public c(Looper looper) {
            super(looper);
            al.this = r1;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                al.this.d();
            }
        }
    }
}
