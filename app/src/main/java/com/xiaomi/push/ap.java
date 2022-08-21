package com.xiaomi.push;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.xiaomi.push.ao;

/* loaded from: classes3.dex */
public class ap extends Handler {
    public final /* synthetic */ ao a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ap(ao aoVar, Looper looper) {
        super(looper);
        this.a = aoVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        ao.b bVar = (ao.b) message.obj;
        int i = message.what;
        if (i == 0) {
            bVar.a();
        } else if (i == 1) {
            bVar.mo2040c();
        }
        super.handleMessage(message);
    }
}
