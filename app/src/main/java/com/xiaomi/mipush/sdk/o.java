package com.xiaomi.mipush.sdk;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.xiaomi.push.al;
import com.xiaomi.push.ed;
import com.xiaomi.push.ef;
import com.xiaomi.push.ho;
import com.xiaomi.push.ht;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import com.xiaomi.push.iu;
import com.xiaomi.push.service.ba;
import com.xiaomi.push.service.bd;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class o {
    public static void a(Context context, Intent intent, Uri uri) {
        ed a;
        ef efVar;
        if (context == null) {
            return;
        }
        ao.a(context).m1895a();
        if (ed.a(context.getApplicationContext()).m2123a() == null) {
            ed.a(context.getApplicationContext()).a(b.m1906a(context.getApplicationContext()).m1907a(), context.getPackageName(), ba.a(context.getApplicationContext()).a(ho.AwakeInfoUploadWaySwitch.a(), 0), new c());
            ba.a(context).a(new q(102, "awake online config", context));
        }
        if ((context instanceof Activity) && intent != null) {
            a = ed.a(context.getApplicationContext());
            efVar = ef.ACTIVITY;
        } else if (!(context instanceof Service) || intent == null) {
            if (uri == null || TextUtils.isEmpty(uri.toString())) {
                return;
            }
            ed.a(context.getApplicationContext()).a(ef.PROVIDER, context, (Intent) null, uri.toString());
            return;
        } else if ("com.xiaomi.mipush.sdk.WAKEUP".equals(intent.getAction())) {
            a = ed.a(context.getApplicationContext());
            efVar = ef.SERVICE_COMPONENT;
        } else {
            a = ed.a(context.getApplicationContext());
            efVar = ef.SERVICE_ACTION;
        }
        a.a(efVar, context, intent, (String) null);
    }

    public static void a(Context context, ii iiVar) {
        boolean z = false;
        boolean a = ba.a(context).a(ho.AwakeAppPingSwitch.a(), false);
        int a2 = ba.a(context).a(ho.AwakeAppPingFrequency.a(), 0);
        if (a2 >= 0 && a2 < 30) {
            com.xiaomi.channel.commonutils.logger.b.c("aw_ping: frquency need > 30s.");
            a2 = 30;
        }
        if (a2 >= 0) {
            z = a;
        }
        if (!com.xiaomi.push.m.m2399a()) {
            a(context, iiVar, z, a2);
        } else if (!z) {
        } else {
            com.xiaomi.push.al.a(context.getApplicationContext()).a((al.a) new p(iiVar, context), a2);
        }
    }

    public static final <T extends iu<T, ?>> void a(Context context, T t, boolean z, int i) {
        byte[] a = it.a(t);
        if (a == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("send message fail, because msgBytes is null.");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("action_help_ping");
        intent.putExtra("extra_help_ping_switch", z);
        intent.putExtra("extra_help_ping_frequency", i);
        intent.putExtra("mipush_payload", a);
        intent.putExtra("com.xiaomi.mipush.MESSAGE_CACHE", true);
        ao.a(context).m1896a(intent);
    }

    public static void a(Context context, String str) {
        com.xiaomi.channel.commonutils.logger.b.m1859a("aw_ping : send aw_ping cmd and content to push service from 3rd app");
        HashMap hashMap = new HashMap();
        hashMap.put("awake_info", str);
        hashMap.put("event_type", String.valueOf(9999));
        hashMap.put("description", "ping message");
        ii iiVar = new ii();
        iiVar.b(b.m1906a(context).m1907a());
        iiVar.d(context.getPackageName());
        iiVar.c(ht.AwakeAppResponse.f489a);
        iiVar.a(bd.a());
        iiVar.f628a = hashMap;
        a(context, iiVar);
    }

    public static void a(Context context, String str, int i, String str2) {
        ii iiVar = new ii();
        iiVar.b(str);
        iiVar.a(new HashMap());
        iiVar.m2309a().put("extra_aw_app_online_cmd", String.valueOf(i));
        iiVar.m2309a().put("extra_help_aw_info", str2);
        iiVar.a(bd.a());
        byte[] a = it.a(iiVar);
        if (a == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("send message fail, because msgBytes is null.");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("action_aw_app_logic");
        intent.putExtra("mipush_payload", a);
        ao.a(context).m1896a(intent);
    }
}
