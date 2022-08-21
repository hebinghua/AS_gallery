package com.xiaomi.push;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/* loaded from: classes3.dex */
public class ek implements eg {
    public final void a(Service service, Intent intent) {
        if ("com.xiaomi.mipush.sdk.WAKEUP".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("waker_pkgname");
            String stringExtra2 = intent.getStringExtra("awake_info");
            if (TextUtils.isEmpty(stringExtra)) {
                dz.a(service.getApplicationContext(), "service", com.xiaomi.stat.c.b.g, "old version message");
            } else if (TextUtils.isEmpty(stringExtra2)) {
                dz.a(service.getApplicationContext(), stringExtra, com.xiaomi.stat.c.b.g, "play with service ");
            } else {
                String b = dy.b(stringExtra2);
                boolean isEmpty = TextUtils.isEmpty(b);
                Context applicationContext = service.getApplicationContext();
                if (!isEmpty) {
                    dz.a(applicationContext, b, com.xiaomi.stat.c.b.g, "old version message ");
                } else {
                    dz.a(applicationContext, "service", com.xiaomi.stat.c.b.h, "B get a incorrect message");
                }
            }
        }
    }

    @Override // com.xiaomi.push.eg
    public void a(Context context, Intent intent, String str) {
        if (context == null || !(context instanceof Service)) {
            return;
        }
        a((Service) context, intent);
    }

    @Override // com.xiaomi.push.eg
    public void a(Context context, ec ecVar) {
        if (ecVar != null) {
            a(context, ecVar.m2121a(), ecVar.c(), ecVar.d());
        }
    }

    public final void a(Context context, String str, String str2, String str3) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (TextUtils.isEmpty(str3)) {
                dz.a(context, "service", com.xiaomi.stat.c.b.h, "argument error");
            } else {
                dz.a(context, str3, com.xiaomi.stat.c.b.h, "argument error");
            }
        } else if (!com.xiaomi.push.service.l.a(context, str)) {
            dz.a(context, str3, 1003, "B is not ready");
        } else {
            dz.a(context, str3, 1002, "B is ready");
            dz.a(context, str3, 1004, "A is ready");
            try {
                Intent intent = new Intent();
                intent.setClassName(str, str2);
                intent.setAction("com.xiaomi.mipush.sdk.WAKEUP");
                intent.putExtra("waker_pkgname", context.getPackageName());
                intent.putExtra("awake_info", dy.a(str3));
                if (context.startService(intent) == null) {
                    dz.a(context, str3, com.xiaomi.stat.c.b.h, "A is fail to help B's service");
                    return;
                }
                dz.a(context, str3, 1005, "A is successful");
                dz.a(context, str3, 1006, "The job is finished");
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
                dz.a(context, str3, com.xiaomi.stat.c.b.h, "A meet a exception when help B's service");
            }
        }
    }
}
