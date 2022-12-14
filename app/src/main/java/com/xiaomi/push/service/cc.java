package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import com.xiaomi.push.al;
import com.xiaomi.push.eq;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class cc {
    public static int a(Map<String, String> map) {
        return Math.max(0, com.xiaomi.push.w.a(map.get("notification_top_period"), 0));
    }

    @TargetApi(19)
    public static Notification a(Notification notification, int i, String str, ax axVar) {
        if (notification != null) {
            if (!str.equals(notification.extras.getString("message_id"))) {
                notification = null;
            }
            return notification;
        }
        List<StatusBarNotification> m2484b = axVar.m2484b();
        if (m2484b == null) {
            return null;
        }
        for (StatusBarNotification statusBarNotification : m2484b) {
            Notification notification2 = statusBarNotification.getNotification();
            String string = notification2.extras.getString("message_id");
            if (i == statusBarNotification.getId() && str.equals(string)) {
                return notification2;
            }
        }
        return null;
    }

    public static al.a a(Context context, String str, int i, String str2, Notification notification) {
        return new cd(i, str2, context, str, notification);
    }

    @TargetApi(19)
    /* renamed from: a  reason: collision with other method in class */
    public static void m2514a(Context context, String str, int i, String str2, Notification notification) {
        if (!com.xiaomi.push.m.m2400a(context) || notification == null || !notification.extras.getBoolean("mipush_n_top_flag", false)) {
            return;
        }
        c(context, str, i, str2, notification);
    }

    public static void a(Context context, Map<String, String> map, eq eqVar, long j) {
        if (map == null || eqVar == null || !com.xiaomi.push.m.m2400a(context) || !m2515a(map)) {
            return;
        }
        int a = a(map);
        int b = b(map);
        if (a <= 0 || b > a) {
            com.xiaomi.channel.commonutils.logger.b.d("set top notification failed - period:" + a + " frequency:" + b);
            return;
        }
        eqVar.setPriority(2);
        Bundle bundle = new Bundle();
        bundle.putLong("mipush_org_when", j);
        bundle.putBoolean("mipush_n_top_flag", true);
        if (b > 0) {
            bundle.putInt("mipush_n_top_fre", b);
        }
        bundle.putInt("mipush_n_top_prd", a);
        eqVar.addExtras(bundle);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2515a(Map<String, String> map) {
        String str = map.get("notification_top_repeat");
        if (!TextUtils.isEmpty(str)) {
            boolean parseBoolean = Boolean.parseBoolean(str);
            com.xiaomi.channel.commonutils.logger.b.c("top notification' repeat is " + parseBoolean);
            return parseBoolean;
        }
        return false;
    }

    public static int b(Map<String, String> map) {
        return Math.max(0, com.xiaomi.push.w.a(map.get("notification_top_frequency"), 0));
    }

    public static String b(int i, String str) {
        return "n_top_update_" + i + "_" + str;
    }

    @TargetApi(19)
    public static void c(Context context, String str, int i, String str2, Notification notification) {
        ax a;
        Notification a2;
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || Build.VERSION.SDK_INT < 26 || (a2 = a(notification, i, str2, (a = ax.a(context, str)))) == null) {
            return;
        }
        boolean z = notification != null;
        if (a2.getGroupAlertBehavior() != 1) {
            com.xiaomi.push.bk.a((Object) a2, "mGroupAlertBehavior", (Object) 1);
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = a2.extras.getLong("mipush_org_when", 0L);
        int i2 = a2.extras.getInt("mipush_n_top_fre", 0);
        int i3 = a2.extras.getInt("mipush_n_top_prd", 0);
        if (i3 <= 0 || i3 < i2) {
            return;
        }
        long j2 = (i3 * 1000) + j;
        int min = (j >= currentTimeMillis || currentTimeMillis >= j2) ? 0 : i2 > 0 ? (int) Math.min((j2 - currentTimeMillis) / 1000, i2) : i3;
        if (!z) {
            if (min > 0) {
                a2.when = currentTimeMillis;
                com.xiaomi.channel.commonutils.logger.b.m1859a("update top notification: " + str2);
                a.a(i, a2);
            } else {
                Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(context, a2);
                recoverBuilder.setPriority(0);
                recoverBuilder.setWhen(currentTimeMillis);
                Bundle extras = recoverBuilder.getExtras();
                if (extras != null) {
                    extras.remove("mipush_n_top_flag");
                    extras.remove("mipush_org_when");
                    extras.remove("mipush_n_top_fre");
                    extras.remove("mipush_n_top_prd");
                    recoverBuilder.setExtras(extras);
                }
                com.xiaomi.channel.commonutils.logger.b.m1859a("update top notification to common: " + str2);
                a.a(i, recoverBuilder.build());
            }
        }
        if (min <= 0) {
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("schedule top notification next update delay: " + min);
        com.xiaomi.push.al.a(context).m1936a(b(i, str2));
        com.xiaomi.push.al.a(context).b(a(context, str, i, str2, (Notification) null), min);
    }
}
