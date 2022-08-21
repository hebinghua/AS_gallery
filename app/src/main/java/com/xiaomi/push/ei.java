package com.xiaomi.push;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/* loaded from: classes3.dex */
public class ei implements eg {
    @Override // com.xiaomi.push.eg
    public void a(Context context, Intent intent, String str) {
        a(context, str);
    }

    @Override // com.xiaomi.push.eg
    public void a(Context context, ec ecVar) {
        if (ecVar != null) {
            b(context, ecVar);
        } else {
            dz.a(context, "provider", com.xiaomi.stat.c.b.h, "A receive incorrect message");
        }
    }

    public final void a(Context context, String str) {
        try {
            if (!TextUtils.isEmpty(str) && context != null) {
                String[] split = str.split(com.xiaomi.stat.b.h.g);
                if (split.length > 0 && !TextUtils.isEmpty(split[split.length - 1])) {
                    String str2 = split[split.length - 1];
                    if (TextUtils.isEmpty(str2)) {
                        dz.a(context, "provider", com.xiaomi.stat.c.b.h, "B get a incorrect message");
                        return;
                    }
                    String decode = Uri.decode(str2);
                    if (TextUtils.isEmpty(decode)) {
                        dz.a(context, "provider", com.xiaomi.stat.c.b.h, "B get a incorrect message");
                        return;
                    }
                    String b = dy.b(decode);
                    if (!TextUtils.isEmpty(b)) {
                        dz.a(context, b, com.xiaomi.stat.c.b.g, "play with provider successfully");
                        return;
                    }
                }
            }
            dz.a(context, "provider", com.xiaomi.stat.c.b.h, "B get a incorrect message");
        } catch (Exception e) {
            dz.a(context, "provider", com.xiaomi.stat.c.b.h, "B meet a exception" + e.getMessage());
        }
    }

    public final void b(Context context, ec ecVar) {
        String b = ecVar.b();
        String d = ecVar.d();
        int a = ecVar.a();
        if (context == null || TextUtils.isEmpty(b) || TextUtils.isEmpty(d)) {
            if (TextUtils.isEmpty(d)) {
                dz.a(context, "provider", com.xiaomi.stat.c.b.h, "argument error");
            } else {
                dz.a(context, d, com.xiaomi.stat.c.b.h, "argument error");
            }
        } else if (!com.xiaomi.push.service.l.b(context, b)) {
            dz.a(context, d, 1003, "B is not ready");
        } else {
            dz.a(context, d, 1002, "B is ready");
            dz.a(context, d, 1004, "A is ready");
            String a2 = dy.a(d);
            try {
                if (TextUtils.isEmpty(a2)) {
                    dz.a(context, d, com.xiaomi.stat.c.b.h, "info is empty");
                } else if (a == 1 && !ed.m2122a(context)) {
                    dz.a(context, d, com.xiaomi.stat.c.b.h, "A not in foreground");
                } else {
                    String type = context.getContentResolver().getType(dy.a(b, a2));
                    if (TextUtils.isEmpty(type) || !"success".equals(type)) {
                        dz.a(context, d, com.xiaomi.stat.c.b.h, "A is fail to help B's provider");
                        return;
                    }
                    dz.a(context, d, 1005, "A is successful");
                    dz.a(context, d, 1006, "The job is finished");
                }
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
                dz.a(context, d, com.xiaomi.stat.c.b.h, "A meet a exception when help B's provider");
            }
        }
    }
}
