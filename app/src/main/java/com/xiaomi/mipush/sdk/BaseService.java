package com.xiaomi.mipush.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public abstract class BaseService extends Service {
    public a a;

    /* loaded from: classes3.dex */
    public static class a extends Handler {
        public WeakReference<BaseService> a;

        public a(WeakReference<BaseService> weakReference) {
            this.a = weakReference;
        }

        public void a() {
            if (hasMessages(1001)) {
                removeMessages(1001);
            }
            sendEmptyMessageDelayed(1001, 1000L);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            WeakReference<BaseService> weakReference;
            BaseService baseService;
            if (message.what != 1001 || (weakReference = this.a) == null || (baseService = weakReference.get()) == null) {
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.c("TimeoutHandler " + baseService.toString() + " kill self");
            if (!baseService.mo1885a()) {
                baseService.stopSelf();
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.c("TimeoutHandler has job");
            sendEmptyMessageDelayed(1001, 1000L);
        }
    }

    /* renamed from: a */
    public abstract boolean mo1885a();

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
        if (this.a == null) {
            this.a = new a(new WeakReference(this));
        }
        this.a.a();
    }
}
