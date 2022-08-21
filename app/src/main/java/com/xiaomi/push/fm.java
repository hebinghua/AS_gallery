package com.xiaomi.push;

import android.util.Log;
import com.xiaomi.push.fp;

/* loaded from: classes3.dex */
public class fm {
    public static final boolean a = Log.isLoggable("BCompressed", 3);

    public static byte[] a(fl flVar, byte[] bArr) {
        try {
            byte[] a2 = fp.a.a(bArr);
            if (a) {
                com.xiaomi.channel.commonutils.logger.b.m1860a("BCompressed", "decompress " + bArr.length + " to " + a2.length + " for " + flVar);
                if (flVar.f355a == 1) {
                    com.xiaomi.channel.commonutils.logger.b.m1860a("BCompressed", "decompress not support upStream");
                }
            }
            return a2;
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.m1860a("BCompressed", "decompress error " + e);
            return bArr;
        }
    }
}
