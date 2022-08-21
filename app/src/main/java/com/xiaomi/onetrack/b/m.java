package com.xiaomi.onetrack.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
public class m extends BroadcastReceiver {
    public final /* synthetic */ l a;

    public m(l lVar) {
        this.a = lVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        a.a(new n(this));
    }
}
