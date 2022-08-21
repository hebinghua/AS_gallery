package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.os.SystemClock;

/* loaded from: classes3.dex */
public class COSPushHelper {
    public static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    public static volatile boolean f22a = false;

    public static void doInNetworkChange(Context context) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (getNeedRegister()) {
            long j = a;
            if (j > 0 && j + 300000 > elapsedRealtime) {
                return;
            }
            a = elapsedRealtime;
            registerCOSAssemblePush(context);
        }
    }

    public static boolean getNeedRegister() {
        return f22a;
    }

    public static void registerCOSAssemblePush(Context context) {
        AbstractPushManager a2 = f.a(context).a(e.ASSEMBLE_PUSH_COS);
        if (a2 != null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH :  register cos when network change!");
            a2.register();
        }
    }
}
