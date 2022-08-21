package com.xiaomi.onetrack.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes3.dex */
public class x extends BroadcastReceiver {
    public final /* synthetic */ g a;

    public x(g gVar) {
        this.a = gVar;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        try {
            com.xiaomi.onetrack.b.a.a(new y(this, intent));
        } catch (Throwable unused) {
        }
    }
}
