package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.RemoteViews;
import com.xiaomi.push.Cif;
import com.xiaomi.push.eo;
import com.xiaomi.push.ep;
import com.xiaomi.push.eq;
import com.xiaomi.push.er;
import com.xiaomi.push.h;
import com.xiaomi.push.hj;
import com.xiaomi.push.hw;
import com.xiaomi.push.service.aw;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.jcodec.containers.mp4.boxes.Box;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class al {
    public static long a;

    /* renamed from: a  reason: collision with other field name */
    public static final LinkedList<Pair<Integer, Cif>> f870a = new LinkedList<>();

    /* renamed from: a  reason: collision with other field name */
    public static ExecutorService f871a = Executors.newCachedThreadPool();

    /* loaded from: classes3.dex */
    public static class a implements Callable<Bitmap> {
        public Context a;

        /* renamed from: a  reason: collision with other field name */
        public String f872a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f873a;

        public a(String str, Context context, boolean z) {
            this.a = context;
            this.f872a = str;
            this.f873a = z;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a */
        public Bitmap call() {
            Bitmap bitmap = null;
            if (TextUtils.isEmpty(this.f872a)) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("Failed get online picture/icon resource cause picUrl is empty");
                return null;
            }
            if (this.f872a.startsWith("http")) {
                aw.b a = aw.a(this.a, this.f872a, this.f873a);
                if (a != null) {
                    return a.f891a;
                }
            } else {
                bitmap = aw.a(this.a, this.f872a);
                if (bitmap != null) {
                    return bitmap;
                }
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("Failed get online picture/icon resource");
            return bitmap;
        }
    }

    /* loaded from: classes3.dex */
    public static class b {
        public long a = 0;

        /* renamed from: a  reason: collision with other field name */
        public Notification f874a;
    }

    /* loaded from: classes3.dex */
    public static class c {

        /* renamed from: a  reason: collision with other field name */
        public String f875a;
        public long a = 0;

        /* renamed from: a  reason: collision with other field name */
        public boolean f876a = false;
    }

    public static int a(Context context, String str) {
        return context.getSharedPreferences("pref_notify_type", 0).getInt(str, Integer.MAX_VALUE);
    }

    public static int a(Context context, String str, String str2) {
        if (str.equals(context.getPackageName())) {
            return context.getResources().getIdentifier(str2, "drawable", str);
        }
        return 0;
    }

    public static int a(Context context, String str, Map<String, String> map, int i) {
        ComponentName a2;
        Intent b2 = b(context, str, map, i);
        if (b2 == null || (a2 = l.a(context, b2)) == null) {
            return 0;
        }
        return a2.hashCode();
    }

    public static int a(Map<String, String> map) {
        String str = map == null ? null : map.get("timeout");
        if (!TextUtils.isEmpty(str)) {
            try {
                return Integer.parseInt(str);
            } catch (Exception unused) {
                return 0;
            }
        }
        return 0;
    }

    public static Notification a(Notification notification) {
        Object a2 = com.xiaomi.push.bk.a(notification, "extraNotification");
        if (a2 != null) {
            com.xiaomi.push.bk.a(a2, "setCustomizedIcon", Boolean.TRUE);
        }
        return notification;
    }

    public static PendingIntent a(Context context, Cif cif, String str, byte[] bArr, int i) {
        return a(context, cif, str, bArr, i, 0, a(context, cif, str));
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00a0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.app.PendingIntent a(android.content.Context r16, com.xiaomi.push.Cif r17, java.lang.String r18, byte[] r19, int r20, int r21, boolean r22) {
        /*
            Method dump skipped, instructions count: 350
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.al.a(android.content.Context, com.xiaomi.push.if, java.lang.String, byte[], int, int, boolean):android.app.PendingIntent");
    }

    public static PendingIntent a(Context context, String str, Cif cif, byte[] bArr, int i, int i2) {
        Map<String, String> m2260a = cif.m2293a().m2260a();
        if (m2260a == null) {
            return null;
        }
        boolean a2 = a(context, cif, str);
        if (a2) {
            return a(context, cif, str, bArr, i, i2, a2);
        }
        Intent m2458a = m2458a(context, str, m2260a, i2);
        if (m2458a == null) {
            return null;
        }
        return PendingIntent.getActivity(context, 0, m2458a, Build.VERSION.SDK_INT >= 31 ? 167772160 : Box.MAX_BOX_SIZE);
    }

    public static ComponentName a(String str) {
        return new ComponentName(str, "com.xiaomi.mipush.sdk.NotificationClickedActivity");
    }

    /* renamed from: a  reason: collision with other method in class */
    public static Intent m2458a(Context context, String str, Map<String, String> map, int i) {
        if (m2468b(map)) {
            return a(context, str, map, String.format("cust_btn_%s_ne", Integer.valueOf(i)), String.format("cust_btn_%s_iu", Integer.valueOf(i)), String.format("cust_btn_%s_ic", Integer.valueOf(i)), String.format("cust_btn_%s_wu", Integer.valueOf(i)));
        }
        if (i == 1) {
            return a(context, str, map, "notification_style_button_left_notify_effect", "notification_style_button_left_intent_uri", "notification_style_button_left_intent_class", "notification_style_button_left_web_uri");
        }
        if (i == 2) {
            return a(context, str, map, "notification_style_button_mid_notify_effect", "notification_style_button_mid_intent_uri", "notification_style_button_mid_intent_class", "notification_style_button_mid_web_uri");
        }
        if (i == 3) {
            return a(context, str, map, "notification_style_button_right_notify_effect", "notification_style_button_right_intent_uri", "notification_style_button_right_intent_class", "notification_style_button_right_web_uri");
        }
        if (i == 4) {
            return a(context, str, map, "notification_colorful_button_notify_effect", "notification_colorful_button_intent_uri", "notification_colorful_button_intent_class", "notification_colorful_button_web_uri");
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x0113  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.content.Intent a(android.content.Context r3, java.lang.String r4, java.util.Map<java.lang.String, java.lang.String> r5, java.lang.String r6, java.lang.String r7, java.lang.String r8, java.lang.String r9) {
        /*
            Method dump skipped, instructions count: 364
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.al.a(android.content.Context, java.lang.String, java.util.Map, java.lang.String, java.lang.String, java.lang.String, java.lang.String):android.content.Intent");
    }

    public static Bitmap a(Context context, int i) {
        return a(context.getResources().getDrawable(i));
    }

    public static Bitmap a(Context context, String str, boolean z) {
        Future submit = f871a.submit(new a(str, context, z));
        try {
            try {
                Bitmap bitmap = (Bitmap) submit.get(180L, TimeUnit.SECONDS);
                return bitmap == null ? bitmap : bitmap;
            } finally {
                submit.cancel(true);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            submit.cancel(true);
            return null;
        }
    }

    public static Bitmap a(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i = 1;
        if (intrinsicWidth <= 0) {
            intrinsicWidth = 1;
        }
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicHeight > 0) {
            i = intrinsicHeight;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public static RemoteViews a(Context context, Cif cif, byte[] bArr) {
        hw m2293a = cif.m2293a();
        String a2 = a(cif);
        if (m2293a != null && m2293a.m2260a() != null) {
            Map<String, String> m2260a = m2293a.m2260a();
            String str = m2260a.get("layout_name");
            String str2 = m2260a.get("layout_value");
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                try {
                    Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(a2);
                    int identifier = resourcesForApplication.getIdentifier(str, "layout", a2);
                    if (identifier == 0) {
                        return null;
                    }
                    RemoteViews remoteViews = new RemoteViews(a2, identifier);
                    try {
                        JSONObject jSONObject = new JSONObject(str2);
                        if (jSONObject.has("text")) {
                            JSONObject jSONObject2 = jSONObject.getJSONObject("text");
                            Iterator<String> keys = jSONObject2.keys();
                            while (keys.hasNext()) {
                                String next = keys.next();
                                String string = jSONObject2.getString(next);
                                int identifier2 = resourcesForApplication.getIdentifier(next, "id", a2);
                                if (identifier2 > 0) {
                                    remoteViews.setTextViewText(identifier2, string);
                                }
                            }
                        }
                        if (jSONObject.has("image")) {
                            JSONObject jSONObject3 = jSONObject.getJSONObject("image");
                            Iterator<String> keys2 = jSONObject3.keys();
                            while (keys2.hasNext()) {
                                String next2 = keys2.next();
                                String string2 = jSONObject3.getString(next2);
                                int identifier3 = resourcesForApplication.getIdentifier(next2, "id", a2);
                                int identifier4 = resourcesForApplication.getIdentifier(string2, "drawable", a2);
                                if (identifier3 > 0) {
                                    remoteViews.setImageViewResource(identifier3, identifier4);
                                }
                            }
                        }
                        if (jSONObject.has(com.xiaomi.stat.b.j)) {
                            JSONObject jSONObject4 = jSONObject.getJSONObject(com.xiaomi.stat.b.j);
                            Iterator<String> keys3 = jSONObject4.keys();
                            while (keys3.hasNext()) {
                                String next3 = keys3.next();
                                String string3 = jSONObject4.getString(next3);
                                if (string3.length() == 0) {
                                    string3 = "yy-MM-dd hh:mm";
                                }
                                int identifier5 = resourcesForApplication.getIdentifier(next3, "id", a2);
                                if (identifier5 > 0) {
                                    remoteViews.setTextViewText(identifier5, new SimpleDateFormat(string3).format(new Date(System.currentTimeMillis())));
                                }
                            }
                        }
                        return remoteViews;
                    } catch (JSONException e) {
                        com.xiaomi.channel.commonutils.logger.b.a(e);
                        return null;
                    }
                } catch (PackageManager.NameNotFoundException e2) {
                    com.xiaomi.channel.commonutils.logger.b.a(e2);
                }
            }
        }
        return null;
    }

    @TargetApi(16)
    public static eq a(Context context, Cif cif, byte[] bArr, String str, int i) {
        PendingIntent a2;
        String a3 = a(cif);
        Map<String, String> m2260a = cif.m2293a().m2260a();
        String str2 = m2260a.get("notification_style_type");
        com.xiaomi.push.m.m2400a(context);
        if ("2".equals(str2)) {
            eq eqVar = new eq(context);
            Bitmap a4 = TextUtils.isEmpty(m2260a.get("notification_bigPic_uri")) ? null : a(context, m2260a.get("notification_bigPic_uri"), false);
            if (a4 == null) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("can not get big picture.");
                return eqVar;
            }
            Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(eqVar);
            bigPictureStyle.bigPicture(a4);
            bigPictureStyle.setSummaryText(str);
            bigPictureStyle.bigLargeIcon((Bitmap) null);
            eqVar.setStyle(bigPictureStyle);
            return eqVar;
        } else if ("1".equals(str2)) {
            eq eqVar2 = new eq(context);
            eqVar2.setStyle(new Notification.BigTextStyle().bigText(str));
            return eqVar2;
        } else if ("4".equals(str2) && com.xiaomi.push.m.m2399a()) {
            ep epVar = new ep(context, a3);
            if (!TextUtils.isEmpty(m2260a.get("notification_banner_image_uri"))) {
                epVar.mo2129a(a(context, m2260a.get("notification_banner_image_uri"), false));
            }
            if (!TextUtils.isEmpty(m2260a.get("notification_banner_icon_uri"))) {
                epVar.b(a(context, m2260a.get("notification_banner_icon_uri"), false));
            }
            epVar.a(m2260a);
            return epVar;
        } else if (!"3".equals(str2) || !com.xiaomi.push.m.m2399a()) {
            return new eq(context);
        } else {
            er erVar = new er(context, i, a3);
            if (!TextUtils.isEmpty(m2260a.get("notification_colorful_button_text")) && (a2 = a(context, a3, cif, bArr, i, 4)) != null) {
                erVar.a(m2260a.get("notification_colorful_button_text"), a2).a(m2260a.get("notification_colorful_button_bg_color"));
            }
            if (!TextUtils.isEmpty(m2260a.get("notification_colorful_bg_color"))) {
                erVar.b(m2260a.get("notification_colorful_bg_color"));
            } else if (!TextUtils.isEmpty(m2260a.get("notification_colorful_bg_image_uri"))) {
                erVar.mo2129a(a(context, m2260a.get("notification_colorful_bg_image_uri"), false));
            }
            erVar.a(m2260a);
            return erVar;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:102:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02d9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:152:0x03c2  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x03db  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x03f6  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x042a  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01b2  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x01b4  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01c3  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x025c  */
    /* JADX WARN: Type inference failed for: r2v42 */
    /* JADX WARN: Type inference failed for: r2v50 */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.xiaomi.push.service.al.b a(android.content.Context r27, com.xiaomi.push.Cif r28, byte[] r29, android.widget.RemoteViews r30, android.app.PendingIntent r31, int r32) {
        /*
            Method dump skipped, instructions count: 1204
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.al.a(android.content.Context, com.xiaomi.push.if, byte[], android.widget.RemoteViews, android.app.PendingIntent, int):com.xiaomi.push.service.al$b");
    }

    /* renamed from: a  reason: collision with other method in class */
    public static c m2459a(Context context, Cif cif, byte[] bArr) {
        int i;
        Map<String, String> map;
        String str;
        c cVar = new c();
        h.a a2 = com.xiaomi.push.h.a(context, a(cif), true);
        hw m2293a = cif.m2293a();
        String str2 = null;
        if (m2293a != null) {
            i = m2293a.c();
            map = m2293a.m2260a();
        } else {
            i = 0;
            map = null;
        }
        int b2 = com.xiaomi.push.w.b(a(cif), i);
        if (!com.xiaomi.push.m.m2400a(context) || a2 != h.a.NOT_ALLOWED) {
            com.xiaomi.push.m.m2400a(context);
            RemoteViews a3 = a(context, cif, bArr);
            PendingIntent a4 = a(context, cif, cif.b(), bArr, b2);
            if (a4 != null) {
                b a5 = a(context, cif, bArr, a3, a4, b2);
                cVar.a = a5.a;
                cVar.f875a = a(cif);
                Notification notification = a5.f874a;
                if (com.xiaomi.push.m.m2399a()) {
                    if (!TextUtils.isEmpty(m2293a.m2259a())) {
                        notification.extras.putString("message_id", m2293a.m2259a());
                    }
                    notification.extras.putString("local_paid", cif.m2294a());
                    ay.a(map, notification.extras, "msg_busi_type");
                    ay.a(map, notification.extras, "disable_notification_flags");
                    String str3 = m2293a.m2265b() == null ? null : m2293a.m2265b().get("score_info");
                    if (!TextUtils.isEmpty(str3)) {
                        notification.extras.putString("score_info", str3);
                    }
                    notification.extras.putString("pushUid", a(m2293a.f524a, "n_stats_expose"));
                    int i2 = -1;
                    if (c(cif)) {
                        i2 = 1000;
                    } else if (m2463a(cif)) {
                        i2 = 3000;
                    }
                    notification.extras.putString("eventMessageType", String.valueOf(i2));
                    notification.extras.putString("target_package", a(cif));
                }
                if (m2293a.m2260a() != null) {
                    str2 = m2293a.m2260a().get("message_count");
                }
                if (com.xiaomi.push.m.m2399a() && str2 != null) {
                    try {
                        ay.a(notification, Integer.parseInt(str2));
                    } catch (NumberFormatException e) {
                        eo.a(context.getApplicationContext()).b(cif.b(), b(cif), m2293a.m2259a(), "8");
                        com.xiaomi.channel.commonutils.logger.b.d("fail to set message count. " + e);
                    }
                }
                String a6 = a(cif);
                if (!com.xiaomi.push.m.m2404c() && com.xiaomi.push.m.m2400a(context)) {
                    ay.m2485a(notification, a6);
                }
                ax a7 = ax.a(context, a6);
                com.xiaomi.push.m.m2400a(context);
                com.xiaomi.push.m.m2400a(context);
                a7.a(b2, notification);
                cVar.f876a = true;
                com.xiaomi.channel.commonutils.logger.b.m1859a("notification: " + m2293a.m2259a() + " is notifyied");
                if (com.xiaomi.push.m.m2399a() && com.xiaomi.push.m.m2400a(context)) {
                    au.a().a(context, b2, notification);
                    cc.m2514a(context, a6, b2, m2293a.m2259a(), notification);
                }
                if (m2463a(cif)) {
                    eo.a(context.getApplicationContext()).a(cif.b(), b(cif), m2293a.m2259a(), 3002, null);
                }
                if (c(cif)) {
                    eo.a(context.getApplicationContext()).a(cif.b(), b(cif), m2293a.m2259a(), 1002, null);
                }
                if (Build.VERSION.SDK_INT < 26) {
                    String m2259a = m2293a.m2259a();
                    com.xiaomi.push.al a8 = com.xiaomi.push.al.a(context);
                    int a9 = a(m2293a.m2260a());
                    if (a9 > 0 && !TextUtils.isEmpty(m2259a)) {
                        String str4 = "n_timeout_" + m2259a;
                        a8.m1936a(str4);
                        a8.b(new am(str4, a7, b2), a9);
                    }
                }
                Pair<Integer, Cif> pair = new Pair<>(Integer.valueOf(b2), cif);
                LinkedList<Pair<Integer, Cif>> linkedList = f870a;
                synchronized (linkedList) {
                    linkedList.add(pair);
                    if (linkedList.size() > 100) {
                        linkedList.remove();
                    }
                }
                return cVar;
            }
            if (m2293a != null) {
                eo.a(context.getApplicationContext()).a(cif.b(), b(cif), m2293a.m2259a(), "11");
            }
            str = "The click PendingIntent is null. ";
        } else {
            if (m2293a != null) {
                eo.a(context.getApplicationContext()).a(cif.b(), b(cif), m2293a.m2259a(), "10:" + a(cif));
            }
            str = "Do not notify because user block " + a(cif) + "‘s notification";
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a(str);
        return cVar;
    }

    public static String a(Context context, String str, Map<String, String> map) {
        return (map == null || TextUtils.isEmpty(map.get("channel_name"))) ? com.xiaomi.push.h.m2216b(context, str) : map.get("channel_name");
    }

    public static String a(Cif cif) {
        hw m2293a;
        if (com.xiaomi.stat.c.c.a.equals(cif.f616b) && (m2293a = cif.m2293a()) != null && m2293a.m2260a() != null) {
            String str = m2293a.m2260a().get("miui_package_name");
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return cif.f616b;
    }

    public static String a(Map<String, String> map, int i) {
        String format = i == 0 ? "notify_effect" : m2468b(map) ? String.format("cust_btn_%s_ne", Integer.valueOf(i)) : i == 1 ? "notification_style_button_left_notify_effect" : i == 2 ? "notification_style_button_mid_notify_effect" : i == 3 ? "notification_style_button_right_notify_effect" : i == 4 ? "notification_colorful_button_notify_effect" : null;
        if (map == null || format == null) {
            return null;
        }
        return map.get(format);
    }

    public static String a(Map<String, String> map, String str) {
        if (map != null) {
            return map.get(str);
        }
        return null;
    }

    public static void a(Context context, Intent intent, Cif cif, hw hwVar, String str, int i) {
        if (cif == null || hwVar == null || TextUtils.isEmpty(str)) {
            return;
        }
        String a2 = a(hwVar.m2260a(), i);
        if (TextUtils.isEmpty(a2)) {
            return;
        }
        if (!bk.a.equals(a2) && !bk.b.equals(a2) && !bk.c.equals(a2)) {
            return;
        }
        intent.putExtra("messageId", str);
        intent.putExtra("local_paid", cif.f612a);
        if (!TextUtils.isEmpty(cif.f616b)) {
            intent.putExtra("target_package", cif.f616b);
        }
        intent.putExtra("job_key", a(hwVar.m2260a(), "jobkey"));
        intent.putExtra(i + "_target_component", a(context, cif.f616b, hwVar.m2260a(), i));
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m2460a(Context context, String str) {
        a(context, str, -1);
    }

    public static void a(Context context, String str, int i) {
        a(context, str, i, -1);
    }

    public static void a(Context context, String str, int i, int i2) {
        int hashCode;
        if (context == null || TextUtils.isEmpty(str) || i < -1) {
            return;
        }
        ax a2 = ax.a(context, str);
        List<StatusBarNotification> m2484b = a2.m2484b();
        if (com.xiaomi.push.w.a(m2484b)) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        boolean z = false;
        if (i == -1) {
            z = true;
            hashCode = 0;
        } else {
            hashCode = ((str.hashCode() / 10) * 10) + i;
        }
        Iterator<StatusBarNotification> it = m2484b.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            StatusBarNotification next = it.next();
            if (!TextUtils.isEmpty(String.valueOf(next.getId()))) {
                int id = next.getId();
                if (z) {
                    linkedList.add(next);
                    a2.a(id);
                } else if (hashCode == id) {
                    d.a(context, next, i2);
                    linkedList.add(next);
                    a2.a(id);
                    break;
                }
            }
        }
        a(context, linkedList);
    }

    public static void a(Context context, String str, String str2, String str3) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return;
        }
        ax a2 = ax.a(context, str);
        List<StatusBarNotification> m2484b = a2.m2484b();
        if (com.xiaomi.push.w.a(m2484b)) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        for (StatusBarNotification statusBarNotification : m2484b) {
            Notification notification = statusBarNotification.getNotification();
            if (notification != null && !TextUtils.isEmpty(String.valueOf(statusBarNotification.getId()))) {
                int id = statusBarNotification.getId();
                String a3 = ay.a(notification);
                String b2 = ay.b(notification);
                if (!TextUtils.isEmpty(a3) && !TextUtils.isEmpty(b2) && a(a3, str2) && a(b2, str3)) {
                    linkedList.add(statusBarNotification);
                    a2.a(id);
                }
            }
        }
        a(context, linkedList);
    }

    public static void a(Context context, LinkedList<? extends Object> linkedList) {
        if (linkedList == null || linkedList.size() <= 0) {
            return;
        }
        bz.a(context, "category_clear_notification", "clear_notification", linkedList.size(), "");
    }

    public static void a(Intent intent) {
        try {
            Method declaredMethod = intent.getClass().getDeclaredMethod("addMiuiFlags", Integer.TYPE);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(intent, 2);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.b("insert flags error " + e);
        }
    }

    @TargetApi(16)
    public static void a(eq eqVar, Context context, String str, Cif cif, byte[] bArr, int i) {
        PendingIntent a2;
        PendingIntent a3;
        PendingIntent a4;
        PendingIntent a5;
        Map<String, String> m2260a = cif.m2293a().m2260a();
        if (TextUtils.equals("3", m2260a.get("notification_style_type")) || TextUtils.equals("4", m2260a.get("notification_style_type"))) {
            return;
        }
        if (m2468b(m2260a)) {
            for (int i2 = 1; i2 <= 3; i2++) {
                String str2 = m2260a.get(String.format("cust_btn_%s_n", Integer.valueOf(i2)));
                if (!TextUtils.isEmpty(str2) && (a5 = a(context, str, cif, bArr, i, i2)) != null) {
                    eqVar.addAction(0, str2, a5);
                }
            }
            return;
        }
        if (!TextUtils.isEmpty(m2260a.get("notification_style_button_left_name")) && (a4 = a(context, str, cif, bArr, i, 1)) != null) {
            eqVar.addAction(0, m2260a.get("notification_style_button_left_name"), a4);
        }
        if (!TextUtils.isEmpty(m2260a.get("notification_style_button_mid_name")) && (a3 = a(context, str, cif, bArr, i, 2)) != null) {
            eqVar.addAction(0, m2260a.get("notification_style_button_mid_name"), a3);
        }
        if (TextUtils.isEmpty(m2260a.get("notification_style_button_right_name")) || (a2 = a(context, str, cif, bArr, i, 3)) == null) {
            return;
        }
        eqVar.addAction(0, m2260a.get("notification_style_button_right_name"), a2);
    }

    public static boolean a(Context context, Cif cif, String str) {
        if (cif != null && cif.m2293a() != null && cif.m2293a().m2260a() != null && !TextUtils.isEmpty(str)) {
            return Boolean.parseBoolean(cif.m2293a().m2260a().get("use_clicked_activity")) && l.a(context, a(str));
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("should clicked activity params are null.");
        return false;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2461a(Context context, String str) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.importance == 100 && Arrays.asList(runningAppProcessInfo.pkgList).contains(str)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2462a(Context context, String str, boolean z) {
        return com.xiaomi.push.m.m2399a() && !z && m2461a(context, str);
    }

    public static boolean a(hw hwVar) {
        if (hwVar != null) {
            String m2259a = hwVar.m2259a();
            return !TextUtils.isEmpty(m2259a) && m2259a.length() == 22 && "satuigmo".indexOf(m2259a.charAt(0)) >= 0;
        }
        return false;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2463a(Cif cif) {
        hw m2293a = cif.m2293a();
        return a(m2293a) && m2293a.l();
    }

    public static boolean a(String str, String str2) {
        return TextUtils.isEmpty(str) || str2.contains(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2464a(Map<String, String> map) {
        if (map == null || !map.containsKey("notify_foreground")) {
            return true;
        }
        return "1".equals(map.get("notify_foreground"));
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x004e, code lost:
        if (android.text.TextUtils.isEmpty(r3) == false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0070, code lost:
        if (android.text.TextUtils.isEmpty(r3) == false) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0072, code lost:
        r1 = r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String[] a(android.content.Context r3, com.xiaomi.push.hw r4) {
        /*
            java.lang.String r0 = r4.m2267c()
            java.lang.String r1 = r4.d()
            java.util.Map r4 = r4.m2260a()
            if (r4 == 0) goto L73
            android.content.res.Resources r2 = r3.getResources()
            android.util.DisplayMetrics r2 = r2.getDisplayMetrics()
            int r2 = r2.widthPixels
            android.content.res.Resources r3 = r3.getResources()
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()
            float r3 = r3.density
            float r2 = (float) r2
            float r2 = r2 / r3
            r3 = 1056964608(0x3f000000, float:0.5)
            float r2 = r2 + r3
            java.lang.Float r3 = java.lang.Float.valueOf(r2)
            int r3 = r3.intValue()
            r2 = 320(0x140, float:4.48E-43)
            if (r3 > r2) goto L51
            java.lang.String r3 = "title_short"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L42
            r0 = r3
        L42:
            java.lang.String r3 = "description_short"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 != 0) goto L73
            goto L72
        L51:
            r2 = 360(0x168, float:5.04E-43)
            if (r3 <= r2) goto L73
            java.lang.String r3 = "title_long"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r2 = android.text.TextUtils.isEmpty(r3)
            if (r2 != 0) goto L64
            r0 = r3
        L64:
            java.lang.String r3 = "description_long"
            java.lang.Object r3 = r4.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 != 0) goto L73
        L72:
            r1 = r3
        L73:
            r3 = 2
            java.lang.String[] r3 = new java.lang.String[r3]
            r4 = 0
            r3[r4] = r0
            r4 = 1
            r3[r4] = r1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.al.a(android.content.Context, com.xiaomi.push.hw):java.lang.String[]");
    }

    public static int b(Context context, String str) {
        int a2 = a(context, str, "mipush_notification");
        int a3 = a(context, str, "mipush_small_notification");
        if (a2 <= 0) {
            a2 = a3 > 0 ? a3 : context.getApplicationInfo().icon;
        }
        return a2 == 0 ? context.getApplicationInfo().logo : a2;
    }

    public static int b(Map<String, String> map) {
        if (map != null) {
            String str = map.get("channel_importance");
            if (TextUtils.isEmpty(str)) {
                return 3;
            }
            try {
                com.xiaomi.channel.commonutils.logger.b.c("importance=" + str);
                return Integer.parseInt(str);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.d("parsing channel importance error: " + e);
                return 3;
            }
        }
        return 3;
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x014e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.content.Intent b(android.content.Context r5, java.lang.String r6, java.util.Map<java.lang.String, java.lang.String> r7, int r8) {
        /*
            Method dump skipped, instructions count: 426
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.al.b(android.content.Context, java.lang.String, java.util.Map, int):android.content.Intent");
    }

    public static String b(Cif cif) {
        return m2463a(cif) ? "E100002" : c(cif) ? "E100000" : m2467b(cif) ? "E100001" : d(cif) ? "E100003" : "";
    }

    /* renamed from: b  reason: collision with other method in class */
    public static void m2465b(Context context, String str) {
        com.xiaomi.push.m.m2400a(context);
    }

    public static void b(Context context, String str, int i) {
        context.getSharedPreferences("pref_notify_type", 0).edit().putInt(str, i).commit();
    }

    public static void b(Intent intent) {
        if (intent == null) {
            return;
        }
        int flags = intent.getFlags() & (-2) & (-3) & (-65);
        if (Build.VERSION.SDK_INT >= 21) {
            flags &= -129;
        }
        intent.setFlags(flags);
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m2466b(Context context, String str) {
        return context.getSharedPreferences("pref_notify_type", 0).contains(str);
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m2467b(Cif cif) {
        hw m2293a = cif.m2293a();
        return a(m2293a) && m2293a.f526b == 1 && !m2463a(cif);
    }

    /* renamed from: b  reason: collision with other method in class */
    public static boolean m2468b(Map<String, String> map) {
        if (map == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("meta extra is null");
            return false;
        }
        return "6".equals(map.get("notification_style_type"));
    }

    public static int c(Map<String, String> map) {
        if (map != null) {
            String str = map.get("notification_priority");
            if (TextUtils.isEmpty(str)) {
                return 0;
            }
            try {
                com.xiaomi.channel.commonutils.logger.b.c("priority=" + str);
                return Integer.parseInt(str);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.d("parsing notification priority error: " + e);
                return 0;
            }
        }
        return 0;
    }

    public static void c(Context context, String str) {
        context.getSharedPreferences("pref_notify_type", 0).edit().remove(str).commit();
    }

    public static boolean c(Cif cif) {
        hw m2293a = cif.m2293a();
        return a(m2293a) && m2293a.f526b == 0 && !m2463a(cif);
    }

    public static boolean d(Cif cif) {
        return cif.a() == hj.Registration;
    }

    public static boolean e(Cif cif) {
        return m2463a(cif) || c(cif) || m2467b(cif);
    }
}
