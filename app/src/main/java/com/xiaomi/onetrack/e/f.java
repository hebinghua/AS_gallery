package com.xiaomi.onetrack.e;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import com.xiaomi.onetrack.util.i;

/* loaded from: classes3.dex */
public class f {
    public static f b;
    public static BroadcastReceiver c = new h();

    public static void a(Context context) {
        if (b == null) {
            b = new f(context);
        }
    }

    public f(Context context) {
        i.a(new g(this, context.getApplicationContext()));
    }

    public static void c(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(c, intentFilter);
    }
}
