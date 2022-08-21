package com.xiaomi.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/* loaded from: classes3.dex */
public class ej implements eg {
    public final void a(Service service, Intent intent) {
        String stringExtra = intent.getStringExtra("awake_info");
        if (!TextUtils.isEmpty(stringExtra)) {
            String b = dy.b(stringExtra);
            if (!TextUtils.isEmpty(b)) {
                dz.a(service.getApplicationContext(), b, com.xiaomi.stat.c.b.g, "play with service successfully");
                return;
            }
        }
        dz.a(service.getApplicationContext(), "service", com.xiaomi.stat.c.b.h, "B get a incorrect message");
    }

    @Override // com.xiaomi.push.eg
    public void a(Context context, Intent intent, String str) {
        if (context == null || !(context instanceof Service)) {
            dz.a(context, "service", com.xiaomi.stat.c.b.h, "A receive incorrect message");
        } else {
            a((Service) context, intent);
        }
    }

    @Override // com.xiaomi.push.eg
    public void a(Context context, ec ecVar) {
        if (ecVar != null) {
            b(context, ecVar);
        } else {
            dz.a(context, "service", com.xiaomi.stat.c.b.h, "A receive incorrect message");
        }
    }

    public final void b(Context context, ec ecVar) {
        String m2121a = ecVar.m2121a();
        String b = ecVar.b();
        String d = ecVar.d();
        int a = ecVar.a();
        if (context == null || TextUtils.isEmpty(m2121a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(d)) {
            if (TextUtils.isEmpty(d)) {
                dz.a(context, "service", com.xiaomi.stat.c.b.h, "argument error");
            } else {
                dz.a(context, d, com.xiaomi.stat.c.b.h, "argument error");
            }
        } else if (!com.xiaomi.push.service.l.a(context, m2121a, b)) {
            dz.a(context, d, 1003, "B is not ready");
        } else {
            dz.a(context, d, 1002, "B is ready");
            dz.a(context, d, 1004, "A is ready");
            try {
                Intent intent = new Intent();
                intent.setAction(b);
                intent.setPackage(m2121a);
                intent.putExtra("awake_info", dy.a(d));
                if (a == 1 && !ed.m2122a(context)) {
                    dz.a(context, d, com.xiaomi.stat.c.b.h, "A not in foreground");
                } else if (context.startService(intent) == null) {
                    dz.a(context, d, com.xiaomi.stat.c.b.h, "A is fail to help B's service");
                } else {
                    dz.a(context, d, 1005, "A is successful");
                    dz.a(context, d, 1006, "The job is finished");
                }
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
                dz.a(context, d, com.xiaomi.stat.c.b.h, "A meet a exception when help B's service");
            }
        }
    }
}
