package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import com.xiaomi.push.ho;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

/* loaded from: classes3.dex */
public class ax {
    public static Context a;

    /* renamed from: a  reason: collision with other field name */
    public static Object f892a;

    /* renamed from: a  reason: collision with other field name */
    public static WeakHashMap<Integer, ax> f893a = new WeakHashMap<>();

    /* renamed from: a  reason: collision with other field name */
    public static boolean f894a;

    /* renamed from: a  reason: collision with other field name */
    public String f895a;
    public String b;

    public ax(String str) {
        this.f895a = str;
    }

    public static int a(String str) {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                return a.getPackageManager().getPackageUid(str, 0);
            } catch (Exception unused) {
                return -1;
            }
        }
        return -1;
    }

    public static NotificationManager a() {
        return (NotificationManager) a.getSystemService("notification");
    }

    public static ax a(Context context, String str) {
        a(context);
        int hashCode = str.hashCode();
        ax axVar = f893a.get(Integer.valueOf(hashCode));
        if (axVar == null) {
            ax axVar2 = new ax(str);
            f893a.put(Integer.valueOf(hashCode), axVar2);
            return axVar2;
        }
        return axVar;
    }

    public static <T> T a(Object obj) {
        if (obj != null) {
            try {
                return (T) obj.getClass().getMethod("getList", new Class[0]).invoke(obj, new Object[0]);
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }

    public static Object a(List list) {
        return Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class).newInstance(list);
    }

    public static String a(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            String a2 = a("mipush|%s|%s", str2, "");
            return str.startsWith(a2) ? a("mipush_%s_%s", str2, str.replace(a2, "")) : str;
        }
        return str;
    }

    public static String a(String str, String str2, String str3) {
        return TextUtils.isEmpty(str) ? "" : String.format(str, str2, str3);
    }

    public static void a(Context context) {
        if (a == null) {
            a = context.getApplicationContext();
            NotificationManager a2 = a();
            Boolean bool = (Boolean) com.xiaomi.push.bk.a((Object) a2, "isSystemConditionProviderEnabled", "xmsf_fake_condition_provider_path");
            m2474a("fwk is support.init:" + bool);
            boolean booleanValue = bool != null ? bool.booleanValue() : false;
            f894a = booleanValue;
            if (!booleanValue) {
                return;
            }
            f892a = com.xiaomi.push.bk.a((Object) a2, "getService", new Object[0]);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m2474a(String str) {
        com.xiaomi.channel.commonutils.logger.b.m1859a("NMHelper:" + str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2475a() {
        if (com.xiaomi.push.m.m2399a() && ba.a(a).a(ho.NotificationBelongToAppSwitch.a(), true)) {
            return f894a;
        }
        return false;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2476a(Context context) {
        a(context);
        return m2475a();
    }

    @TargetApi(26)
    /* renamed from: a  reason: collision with other method in class */
    public NotificationChannel m2477a(String str) {
        NotificationChannel notificationChannel = null;
        try {
            if (m2475a()) {
                List<NotificationChannel> m2481a = m2481a();
                if (m2481a != null) {
                    for (NotificationChannel notificationChannel2 : m2481a) {
                        if (str.equals(notificationChannel2.getId())) {
                            notificationChannel = notificationChannel2;
                            break;
                        }
                    }
                }
            } else {
                notificationChannel = a().getNotificationChannel(str);
            }
        } catch (Exception e) {
            m2474a("getNotificationChannel error" + e);
        }
        return notificationChannel;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Context m2478a() {
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2479a() {
        return this.f895a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m2480a(String str) {
        return TextUtils.isEmpty(str) ? b() : com.xiaomi.push.m.m2400a(m2478a()) ? b(str) : str;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:10:0x0036
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:82)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:48)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:40)
        */
    @android.annotation.TargetApi(26)
    /* renamed from: a  reason: collision with other method in class */
    public java.util.List<android.app.NotificationChannel> m2481a() {
        /*
            r8 = this;
            java.lang.String r0 = r8.f895a
            r1 = 0
            boolean r2 = m2475a()     // Catch: java.lang.Exception -> L78
            if (r2 == 0) goto L3b
            int r2 = a(r0)     // Catch: java.lang.Exception -> L78
            r3 = -1
            if (r2 == r3) goto L39
            java.lang.Object r3 = com.xiaomi.push.service.ax.f892a     // Catch: java.lang.Exception -> L78
            java.lang.String r4 = "getNotificationChannelsForPackage"
            r5 = 3
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Exception -> L78
            r6 = 0
            r5[r6] = r0     // Catch: java.lang.Exception -> L78
            r6 = 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Exception -> L78
            r5[r6] = r2     // Catch: java.lang.Exception -> L78
            r2 = 2
            java.lang.Boolean r6 = java.lang.Boolean.FALSE     // Catch: java.lang.Exception -> L78
            r5[r2] = r6     // Catch: java.lang.Exception -> L78
            java.lang.Object r2 = com.xiaomi.push.bk.a(r3, r4, r5)     // Catch: java.lang.Exception -> L78
            java.lang.Object r2 = a(r2)     // Catch: java.lang.Exception -> L78
            java.util.List r2 = (java.util.List) r2     // Catch: java.lang.Exception -> L78
            java.lang.String r1 = "mipush|%s|%s"
            r7 = r2
            r2 = r1
            r1 = r7
            goto L45
        L36:
            r0 = move-exception
            r1 = r2
            goto L79
        L39:
            r2 = r1
            goto L45
        L3b:
            android.app.NotificationManager r2 = a()     // Catch: java.lang.Exception -> L78
            java.util.List r1 = r2.getNotificationChannels()     // Catch: java.lang.Exception -> L78
            java.lang.String r2 = "mipush_%s_%s"
        L45:
            boolean r3 = com.xiaomi.push.m.m2399a()     // Catch: java.lang.Exception -> L78
            if (r3 == 0) goto L8d
            if (r1 == 0) goto L8d
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Exception -> L78
            r3.<init>()     // Catch: java.lang.Exception -> L78
            java.lang.String r4 = ""
            java.lang.String r0 = a(r2, r0, r4)     // Catch: java.lang.Exception -> L78
            java.util.Iterator r2 = r1.iterator()     // Catch: java.lang.Exception -> L78
        L5c:
            boolean r4 = r2.hasNext()     // Catch: java.lang.Exception -> L78
            if (r4 == 0) goto L76
            java.lang.Object r4 = r2.next()     // Catch: java.lang.Exception -> L78
            android.app.NotificationChannel r4 = (android.app.NotificationChannel) r4     // Catch: java.lang.Exception -> L78
            java.lang.String r5 = r4.getId()     // Catch: java.lang.Exception -> L78
            boolean r5 = r5.startsWith(r0)     // Catch: java.lang.Exception -> L78
            if (r5 == 0) goto L5c
            r3.add(r4)     // Catch: java.lang.Exception -> L78
            goto L5c
        L76:
            r1 = r3
            goto L8d
        L78:
            r0 = move-exception
        L79:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "getNotificationChannels error "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            m2474a(r0)
        L8d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.ax.m2481a():java.util.List");
    }

    public void a(int i) {
        String str = this.f895a;
        try {
            if (!m2475a()) {
                a().cancel(i);
                return;
            }
            int a2 = com.xiaomi.push.j.a();
            String packageName = m2478a().getPackageName();
            if (Build.VERSION.SDK_INT >= 30) {
                com.xiaomi.push.bk.b(f892a, "cancelNotificationWithTag", str, packageName, null, Integer.valueOf(i), Integer.valueOf(a2));
            } else {
                com.xiaomi.push.bk.b(f892a, "cancelNotificationWithTag", str, null, Integer.valueOf(i), Integer.valueOf(a2));
            }
            m2474a("cancel succ:" + i);
        } catch (Exception e) {
            m2474a("cancel error" + e);
        }
    }

    public void a(int i, Notification notification) {
        String str = this.f895a;
        NotificationManager a2 = a();
        try {
            int i2 = Build.VERSION.SDK_INT;
            if (m2475a()) {
                if (i2 >= 19) {
                    notification.extras.putString("xmsf_target_package", str);
                }
                if (i2 >= 29) {
                    a2.notifyAsPackage(str, null, i, notification);
                    return;
                }
            }
            a2.notify(i, notification);
        } catch (Exception unused) {
        }
    }

    @TargetApi(26)
    public void a(NotificationChannel notificationChannel) {
        String str = this.f895a;
        try {
            if (m2475a()) {
                int a2 = a(str);
                if (a2 != -1) {
                    com.xiaomi.push.bk.b(f892a, "createNotificationChannelsForPackage", str, Integer.valueOf(a2), a(Arrays.asList(notificationChannel)));
                }
            } else {
                a().createNotificationChannel(notificationChannel);
            }
        } catch (Exception e) {
            m2474a("createNotificationChannel error" + e);
        }
    }

    public void a(NotificationChannel notificationChannel, boolean z) {
        String str = this.f895a;
        try {
            if (z) {
                int a2 = a(str);
                if (a2 != -1) {
                    com.xiaomi.push.bk.b(f892a, "updateNotificationChannelForPackage", str, Integer.valueOf(a2), notificationChannel);
                }
            } else {
                a(notificationChannel);
            }
        } catch (Exception e) {
            m2474a("updateNotificationChannel error " + e);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2482a(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.startsWith(b(""));
        }
        return false;
    }

    /* renamed from: a  reason: collision with other method in class */
    public final StatusBarNotification[] m2483a() {
        if (com.xiaomi.push.m.m2400a(m2478a())) {
            try {
                Object a2 = com.xiaomi.push.bk.a(f892a, "getActiveNotifications", m2478a().getPackageName());
                if (!(a2 instanceof StatusBarNotification[])) {
                    return null;
                }
                return (StatusBarNotification[]) a2;
            } catch (Throwable th) {
                m2474a("getAllNotifications error " + th);
                return null;
            }
        }
        return null;
    }

    public String b() {
        if (TextUtils.isEmpty(this.b)) {
            this.b = b("default");
        }
        return this.b;
    }

    public final String b(String str) {
        return a(m2475a() ? "mipush|%s|%s" : "mipush_%s_%s", this.f895a, str);
    }

    public String b(String str, String str2) {
        return m2475a() ? str : str2;
    }

    /* renamed from: b  reason: collision with other method in class */
    public List<StatusBarNotification> m2484b() {
        String str = this.f895a;
        NotificationManager a2 = a();
        ArrayList arrayList = null;
        try {
            if (m2475a()) {
                int a3 = com.xiaomi.push.j.a();
                if (a3 == -1) {
                    return null;
                }
                return (List) a(com.xiaomi.push.bk.a(f892a, "getAppActiveNotifications", str, Integer.valueOf(a3)));
            }
            StatusBarNotification[] activeNotifications = Build.VERSION.SDK_INT >= 23 ? a2.getActiveNotifications() : m2483a();
            boolean m2399a = com.xiaomi.push.m.m2399a();
            if (activeNotifications == null || activeNotifications.length <= 0) {
                return null;
            }
            ArrayList arrayList2 = new ArrayList();
            try {
                for (StatusBarNotification statusBarNotification : activeNotifications) {
                    if (!m2399a || str.equals(ay.c(statusBarNotification.getNotification()))) {
                        arrayList2.add(statusBarNotification);
                    }
                }
                return arrayList2;
            } catch (Throwable th) {
                th = th;
                arrayList = arrayList2;
                m2474a("getActiveNotifications error " + th);
                return arrayList;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public String toString() {
        return "NotificationManagerHelper{" + this.f895a + "}";
    }
}
