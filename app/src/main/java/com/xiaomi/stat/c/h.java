package com.xiaomi.stat.c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
class h extends BroadcastReceiver {
    public final /* synthetic */ g a;

    public h(g gVar) {
        this.a = gVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        this.a.sendEmptyMessage(3);
    }
}
