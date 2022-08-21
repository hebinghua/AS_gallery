package com.xiaomi.stat.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xiaomi.stat.d.k;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class b extends BroadcastReceiver {
    public final /* synthetic */ a a;

    public b(a aVar) {
        this.a = aVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int i;
        BroadcastReceiver broadcastReceiver;
        try {
            i = this.a.u;
            if (i != 1) {
                broadcastReceiver = this.a.x;
                context.unregisterReceiver(broadcastReceiver);
                return;
            }
            e.a().execute(new c(this));
        } catch (Exception e) {
            k.d("ConfigManager", "mNetReceiver exception", e);
        }
    }
}
