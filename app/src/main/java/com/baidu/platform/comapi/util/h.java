package com.baidu.platform.comapi.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public abstract class h extends Handler {
    public h(Looper looper) {
        super(looper);
    }

    public abstract void a(Message message);

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        if (message == null) {
            return;
        }
        Message obtain = Message.obtain();
        obtain.copyFrom(message);
        a(obtain);
        obtain.recycle();
    }
}
