package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.bm;
import com.xiaomi.push.hj;
import com.xiaomi.push.hy;
import com.xiaomi.push.hz;
import com.xiaomi.push.ia;
import com.xiaomi.push.ie;
import com.xiaomi.push.ii;
import com.xiaomi.push.ik;
import com.xiaomi.push.il;
import com.xiaomi.push.im;
import com.xiaomi.push.io;
import com.xiaomi.push.iq;
import com.xiaomi.push.is;
import com.xiaomi.push.it;
import com.xiaomi.push.iu;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class ai {
    public static <T extends iu<T, ?>> Cif a(Context context, T t, hj hjVar) {
        return a(context, t, hjVar, !hjVar.equals(hj.Registration), context.getPackageName(), b.m1906a(context).m1907a());
    }

    public static <T extends iu<T, ?>> Cif a(Context context, T t, hj hjVar, boolean z, String str, String str2) {
        return a(context, t, hjVar, z, str, str2, true);
    }

    public static <T extends iu<T, ?>> Cif a(Context context, T t, hj hjVar, boolean z, String str, String str2, boolean z2) {
        String str3;
        byte[] a = it.a(t);
        if (a != null) {
            Cif cif = new Cif();
            if (z) {
                String d = b.m1906a(context).d();
                if (TextUtils.isEmpty(d)) {
                    str3 = "regSecret is empty, return null";
                } else {
                    try {
                        a = com.xiaomi.push.i.b(bm.m1977a(d), a);
                    } catch (Exception unused) {
                        com.xiaomi.channel.commonutils.logger.b.d("encryption error. ");
                    }
                }
            }
            hy hyVar = new hy();
            hyVar.f537a = 5L;
            hyVar.f538a = "fakeid";
            cif.a(hyVar);
            cif.a(ByteBuffer.wrap(a));
            cif.a(hjVar);
            cif.b(z2);
            cif.b(str);
            cif.a(z);
            cif.a(str2);
            return cif;
        }
        str3 = "invoke convertThriftObjectToBytes method, return null.";
        com.xiaomi.channel.commonutils.logger.b.m1859a(str3);
        return null;
    }

    public static iu a(Context context, Cif cif) {
        byte[] m2299a;
        if (cif.m2301b()) {
            byte[] a = i.a(context, cif, e.ASSEMBLE_PUSH_FCM);
            if (a == null) {
                a = bm.m1977a(b.m1906a(context).d());
            }
            try {
                m2299a = com.xiaomi.push.i.a(a, cif.m2299a());
            } catch (Exception e) {
                throw new u("the aes decrypt failed.", e);
            }
        } else {
            m2299a = cif.m2299a();
        }
        iu a2 = a(cif.a(), cif.f617b);
        if (a2 != null) {
            it.a(a2, m2299a);
        }
        return a2;
    }

    public static iu a(hj hjVar, boolean z) {
        switch (aj.a[hjVar.ordinal()]) {
            case 1:
                return new ik();
            case 2:
                return new iq();
            case 3:
                return new io();
            case 4:
                return new is();
            case 5:
                return new im();
            case 6:
                return new hz();
            case 7:
                return new ie();
            case 8:
                return new il();
            case 9:
                if (z) {
                    return new ii();
                }
                ia iaVar = new ia();
                iaVar.a(true);
                return iaVar;
            case 10:
                return new ie();
            default:
                return null;
        }
    }

    public static <T extends iu<T, ?>> Cif b(Context context, T t, hj hjVar, boolean z, String str, String str2) {
        return a(context, t, hjVar, z, str, str2, false);
    }
}
