package com.xiaomi.push;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/* loaded from: classes3.dex */
public class eb implements eg {
    public final void a(Activity activity, Intent intent) {
        String stringExtra = intent.getStringExtra("awake_info");
        if (!TextUtils.isEmpty(stringExtra)) {
            String b = dy.b(stringExtra);
            if (!TextUtils.isEmpty(b)) {
                dz.a(activity.getApplicationContext(), b, com.xiaomi.stat.c.b.g, "play with activity successfully");
                return;
            }
        }
        dz.a(activity.getApplicationContext(), "activity", com.xiaomi.stat.c.b.h, "B get incorrect message");
    }

    @Override // com.xiaomi.push.eg
    public void a(Context context, Intent intent, String str) {
        if (context == null || !(context instanceof Activity) || intent == null) {
            dz.a(context, "activity", com.xiaomi.stat.c.b.h, "B receive incorrect message");
        } else {
            a((Activity) context, intent);
        }
    }

    @Override // com.xiaomi.push.eg
    public void a(Context context, ec ecVar) {
        if (ecVar != null) {
            b(context, ecVar);
        } else {
            dz.a(context, "activity", com.xiaomi.stat.c.b.h, "A receive incorrect message");
        }
    }

    public final void b(Context context, ec ecVar) {
        String m2121a = ecVar.m2121a();
        String b = ecVar.b();
        String d = ecVar.d();
        int a = ecVar.a();
        if (context == null || TextUtils.isEmpty(m2121a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(d)) {
            if (TextUtils.isEmpty(d)) {
                dz.a(context, "activity", com.xiaomi.stat.c.b.h, "argument error");
            } else {
                dz.a(context, d, com.xiaomi.stat.c.b.h, "argument error");
            }
        } else if (!com.xiaomi.push.service.l.b(context, m2121a, b)) {
            dz.a(context, d, 1003, "B is not ready");
        } else {
            dz.a(context, d, 1002, "B is ready");
            dz.a(context, d, 1004, "A is ready");
            Intent intent = new Intent(b);
            intent.setPackage(m2121a);
            intent.putExtra("awake_info", dy.a(d));
            intent.addFlags(276824064);
            intent.setAction(b);
            if (a == 1) {
                try {
                    if (!ed.m2122a(context)) {
                        dz.a(context, d, com.xiaomi.stat.c.b.h, "A not in foreground");
                        return;
                    }
                } catch (Exception e) {
                    com.xiaomi.channel.commonutils.logger.b.a(e);
                    dz.a(context, d, com.xiaomi.stat.c.b.h, "A meet a exception when help B's activity");
                    return;
                }
            }
            context.startActivity(intent);
            dz.a(context, d, 1005, "A is successful");
            dz.a(context, d, 1006, "The job is finished");
        }
    }
}
