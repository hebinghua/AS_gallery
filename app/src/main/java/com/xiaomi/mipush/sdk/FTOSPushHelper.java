package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.os.SystemClock;

/* loaded from: classes3.dex */
public class FTOSPushHelper {
    public static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    public static volatile boolean f23a = false;

    public static void a(Context context) {
        AbstractPushManager a2 = f.a(context).a(e.ASSEMBLE_PUSH_FTOS);
        if (a2 != null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH :  register fun touch os when network change!");
            a2.register();
        }
    }

    public static void doInNetworkChange(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (getNeedRegister()) {
            long j = a;
            if (j > 0 && j + 300000 > elapsedRealtime) {
                return;
            }
            a = elapsedRealtime;
            a(context);
        }
    }

    public static boolean getNeedRegister() {
        return f23a;
    }
}
