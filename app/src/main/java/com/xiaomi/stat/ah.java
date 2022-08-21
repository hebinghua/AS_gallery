package com.xiaomi.stat;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/* loaded from: classes3.dex */
public class ah {
    public static final int a = 1;
    private static final int b = 10000;
    private static final int c = 3;
    private Handler d;
    private Runnable e;
    private HandlerThread f;
    private int g = 3;
    private int h = 10000;
    private int i = 0;
    private boolean j = false;

    public ah(Runnable runnable) {
        this.e = runnable;
    }

    private void d() {
        a aVar = new a();
        HandlerThread handlerThread = new HandlerThread("".concat("_").concat(String.valueOf(com.xiaomi.stat.d.r.b())));
        this.f = handlerThread;
        handlerThread.start();
        Handler handler = new Handler(this.f.getLooper(), aVar);
        this.d = handler;
        aVar.a(handler);
    }

    public void a() {
        Handler handler = this.d;
        if (handler == null || !handler.hasMessages(1)) {
            d();
            Message obtainMessage = this.d.obtainMessage(1);
            obtainMessage.obj = 0;
            this.j = true;
            this.d.sendMessageDelayed(obtainMessage, this.i);
        }
    }

    public void b() {
        this.d.removeMessages(1);
        this.d.getLooper().quit();
        this.j = false;
    }

    public void a(int i) {
        this.i = i;
    }

    public void b(int i) {
        this.g = i;
    }

    public void c(int i) {
        this.h = i;
    }

    public boolean c() {
        return this.j;
    }

    /* loaded from: classes3.dex */
    public class a implements Handler.Callback {
        private Handler b;

        private a() {
            this.b = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(Handler handler) {
            this.b = handler;
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                int intValue = ((Integer) message.obj).intValue();
                if (intValue < ah.this.g) {
                    ah.this.e.run();
                    if (ah.this.j) {
                        Message obtainMessage = this.b.obtainMessage(1);
                        obtainMessage.obj = Integer.valueOf(intValue + 1);
                        this.b.sendMessageDelayed(obtainMessage, ah.this.h);
                    }
                } else {
                    ah.this.b();
                }
            }
            return true;
        }
    }
}
