package com.baidu.location.d;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;
import ch.qos.logback.core.joran.action.Action;
import com.baidu.location.LLSInterface;
import com.baidu.location.b.k;
import com.baidu.location.b.o;
import com.baidu.location.b.t;
import com.baidu.location.b.u;
import com.baidu.location.b.w;
import com.baidu.location.b.x;
import com.baidu.location.c.i;
import com.baidu.location.e.j;
import com.baidu.location.f;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class a extends Service implements LLSInterface {
    public static HandlerC0010a a;
    public static long c;
    private static long g;
    public Messenger b = null;
    private Looper d = null;
    private HandlerThread e = null;
    private boolean f = true;
    private int h = 0;
    private boolean i = true;

    /* renamed from: com.baidu.location.d.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class HandlerC0010a extends Handler {
        private final WeakReference<a> a;

        public HandlerC0010a(Looper looper, a aVar) {
            super(looper);
            this.a = new WeakReference<>(aVar);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            a aVar = this.a.get();
            if (aVar == null) {
                return;
            }
            if (f.isServing) {
                int i = message.what;
                if (i == 11) {
                    aVar.a(message);
                } else if (i == 12) {
                    aVar.b(message);
                } else if (i == 15) {
                    aVar.c(message);
                } else if (i == 22) {
                    o.c().b(message);
                } else if (i == 41) {
                    o.c().i();
                } else if (i == 401) {
                    try {
                        message.getData();
                    } catch (Exception unused) {
                    }
                } else if (i == 406) {
                    k.a().e();
                } else if (i == 705) {
                    com.baidu.location.b.b.a().a(message.getData().getBoolean("foreground"));
                }
            }
            if (message.what == 1) {
                aVar.b();
            }
            if (message.what == 0) {
                aVar.a();
            }
            super.handleMessage(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        com.baidu.location.a.a.a().a(f.getServiceContext());
        com.baidu.location.e.b.a();
        try {
            x.a().e();
        } catch (Exception unused) {
        }
        k.a().b();
        com.baidu.location.c.f.a().b();
        com.baidu.location.c.b.a().b();
        o.c().d();
        i.a().c();
        this.h = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Message message) {
        com.baidu.location.b.b.a().a(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        com.baidu.location.c.f.a().e();
        i.a().d();
        x.a().f();
        com.baidu.location.c.b.a().c();
        o.c().e();
        k.a().c();
        if (this.i) {
            w.d();
        }
        com.baidu.location.b.b.a().b();
        try {
            u.a().d();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.h = 4;
        Log.d("baidu_location_service", "baidu location service has stoped ...");
        if (!this.f) {
            Process.killProcess(Process.myPid());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Message message) {
        com.baidu.location.b.b.a().b(message);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(Message message) {
        com.baidu.location.b.b.a().c(message);
    }

    @Override // com.baidu.location.LLSInterface
    public double getVersion() {
        return 9.15999984741211d;
    }

    @Override // android.app.Service, com.baidu.location.LLSInterface
    public IBinder onBind(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            com.baidu.location.e.b.h = extras.getString(Action.KEY_ATTRIBUTE);
            com.baidu.location.e.b.g = extras.getString("sign");
            this.f = extras.getBoolean("kill_process");
            extras.getBoolean("cache_exception");
        }
        return this.b.getBinder();
    }

    @Override // com.baidu.location.LLSInterface
    public void onCreate(Context context) {
        try {
            j.ax = context.getPackageName();
        } catch (Exception unused) {
        }
        g = System.currentTimeMillis();
        HandlerThread a2 = t.a();
        this.e = a2;
        if (a2 != null) {
            this.d = a2.getLooper();
        }
        a = this.d == null ? new HandlerC0010a(Looper.getMainLooper(), this) : new HandlerC0010a(this.d, this);
        c = System.currentTimeMillis();
        this.b = new Messenger(a);
        a.sendEmptyMessage(0);
        this.h = 1;
        Log.d("baidu_location_service", "baidu location service start1 ...20201104_1..." + Process.myPid());
    }

    @Override // android.app.Service, com.baidu.location.LLSInterface
    public void onDestroy() {
        try {
            a.sendEmptyMessage(1);
        } catch (Exception unused) {
            Log.d("baidu_location_service", "baidu location service stop exception...");
            this.i = false;
            b();
            Process.killProcess(Process.myPid());
        }
        this.h = 3;
        new Handler(Looper.getMainLooper()).postDelayed(new b(this, new WeakReference(this)), 1000L);
        Log.d("baidu_location_service", "baidu location service stop ...");
    }

    @Override // android.app.Service, com.baidu.location.LLSInterface
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    @Override // android.app.Service, com.baidu.location.LLSInterface
    public void onTaskRemoved(Intent intent) {
        Log.d("baidu_location_service", "baidu location service remove task...");
    }

    @Override // com.baidu.location.LLSInterface
    public boolean onUnBind(Intent intent) {
        return false;
    }
}
