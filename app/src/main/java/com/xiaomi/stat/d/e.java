package com.xiaomi.stat.d;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Process;
import android.os.RemoteException;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.ak;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/* loaded from: classes3.dex */
public class e {
    private static String A = null;
    private static String B = null;
    private static String C = null;
    private static String D = null;
    private static String E = null;
    private static Boolean F = null;
    private static String G = null;
    private static String H = null;
    private static String I = null;
    private static boolean J = false;
    private static final String a = "DeviceUtil";
    private static final int b = 15;
    private static final int c = 14;
    private static final String d = "";
    private static final long e = 7776000000L;
    private static final String f = "mistat";
    private static final String g = "device_id";
    private static final String h = "anonymous_id";
    private static Method i;
    private static Method j;
    private static Method k;
    private static Object l;
    private static Method m;
    private static String n;
    private static String o;
    private static String p;
    private static String q;
    private static String r;
    private static String s;
    private static String t;
    private static String u;
    private static String v;
    private static String w;
    private static String x;
    private static String y;
    private static String z;

    static {
        try {
            i = Class.forName("android.os.SystemProperties").getMethod(CallMethod.METHOD_GET, String.class);
        } catch (Exception unused) {
        }
        try {
            Class<?> cls = Class.forName("miui.telephony.TelephonyManagerEx");
            l = cls.getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            j = cls.getMethod("getImeiList", new Class[0]);
            k = cls.getMethod("getMeidList", new Class[0]);
        } catch (Exception unused2) {
        }
        try {
            if (Build.VERSION.SDK_INT < 21) {
                return;
            }
            m = Class.forName("android.telephony.TelephonyManager").getMethod("getImei", Integer.TYPE);
        } catch (Exception unused3) {
        }
    }

    public static void a() {
        boolean z2 = r.b() - com.xiaomi.stat.b.v() > e;
        if (TextUtils.isEmpty(com.xiaomi.stat.b.w()) || z2) {
            com.xiaomi.stat.b.i(UUID.randomUUID().toString());
        }
    }

    private static String e() {
        String w2 = com.xiaomi.stat.b.w();
        if (TextUtils.isEmpty(w2)) {
            String uuid = UUID.randomUUID().toString();
            com.xiaomi.stat.b.i(uuid);
            return uuid;
        }
        return w2;
    }

    public static String a(Context context) {
        if (com.xiaomi.stat.b.e()) {
            return "";
        }
        if (!TextUtils.isEmpty(n)) {
            return n;
        }
        String a2 = p.a(context);
        if (!TextUtils.isEmpty(a2)) {
            n = a2;
            return a2;
        }
        y(context);
        if (TextUtils.isEmpty(n)) {
            return "";
        }
        p.a(context, n);
        return n;
    }

    public static String b(Context context) {
        if (!TextUtils.isEmpty(v)) {
            return v;
        }
        String a2 = a(context);
        if (TextUtils.isEmpty(a2)) {
            return "";
        }
        String c2 = g.c(a2);
        v = c2;
        return c2;
    }

    public static String c(Context context) {
        if (!TextUtils.isEmpty(A)) {
            return A;
        }
        String a2 = a(context);
        if (TextUtils.isEmpty(a2)) {
            return "";
        }
        String d2 = g.d(a2);
        A = d2;
        return d2;
    }

    public static String d(Context context) {
        if (com.xiaomi.stat.b.e()) {
            return "";
        }
        if (!TextUtils.isEmpty(o)) {
            return o;
        }
        String b2 = p.b(context);
        if (!TextUtils.isEmpty(b2)) {
            o = b2;
            return b2;
        }
        y(context);
        if (TextUtils.isEmpty(o)) {
            return "";
        }
        p.b(context, o);
        return o;
    }

    public static String e(Context context) {
        if (!TextUtils.isEmpty(w)) {
            return w;
        }
        String d2 = d(context);
        if (TextUtils.isEmpty(d2)) {
            return "";
        }
        String c2 = g.c(d2);
        w = c2;
        return c2;
    }

    public static String f(Context context) {
        if (!TextUtils.isEmpty(B)) {
            return B;
        }
        String d2 = d(context);
        if (TextUtils.isEmpty(d2)) {
            return "";
        }
        String d3 = g.d(d2);
        B = d3;
        return d3;
    }

    public static String g(Context context) {
        if (com.xiaomi.stat.b.e()) {
            return "";
        }
        if (!TextUtils.isEmpty(p)) {
            return p;
        }
        String c2 = p.c(context);
        if (!TextUtils.isEmpty(c2)) {
            p = c2;
            return c2;
        }
        String s2 = s(context);
        if (TextUtils.isEmpty(s2)) {
            return "";
        }
        p = s2;
        p.c(context, s2);
        return p;
    }

    public static String h(Context context) {
        if (!TextUtils.isEmpty(x)) {
            return x;
        }
        String g2 = g(context);
        if (TextUtils.isEmpty(g2)) {
            return "";
        }
        String c2 = g.c(g2);
        x = c2;
        return c2;
    }

    public static String i(Context context) {
        if (!TextUtils.isEmpty(C)) {
            return C;
        }
        String g2 = g(context);
        if (TextUtils.isEmpty(g2)) {
            return "";
        }
        String d2 = g.d(g2);
        C = d2;
        return d2;
    }

    public static String j(Context context) {
        String B2;
        if (com.xiaomi.stat.b.e()) {
            return "";
        }
        if (!TextUtils.isEmpty(q)) {
            return q;
        }
        String d2 = p.d(context);
        if (!TextUtils.isEmpty(d2)) {
            q = d2;
            return d2;
        }
        if (w(context)) {
            B2 = b.b(context);
        } else {
            B2 = B(context);
        }
        if (TextUtils.isEmpty(B2)) {
            return "";
        }
        q = B2;
        p.d(context, B2);
        return q;
    }

    public static String k(Context context) {
        if (!TextUtils.isEmpty(y)) {
            return y;
        }
        String j2 = j(context);
        if (TextUtils.isEmpty(j2)) {
            return "";
        }
        String c2 = g.c(j2);
        y = c2;
        return c2;
    }

    public static String l(Context context) {
        if (!TextUtils.isEmpty(D)) {
            return D;
        }
        String j2 = j(context);
        if (TextUtils.isEmpty(j2)) {
            return "";
        }
        String d2 = g.d(j2);
        D = d2;
        return d2;
    }

    public static String m(Context context) {
        if (com.xiaomi.stat.b.e()) {
            return "";
        }
        if (!TextUtils.isEmpty(r)) {
            return r;
        }
        String e2 = p.e(context);
        if (!TextUtils.isEmpty(e2)) {
            r = e2;
            return e2;
        }
        String t2 = t(context);
        if (TextUtils.isEmpty(t2)) {
            return "";
        }
        r = t2;
        p.e(context, t2);
        return r;
    }

    public static String n(Context context) {
        if (!TextUtils.isEmpty(z)) {
            return z;
        }
        String m2 = m(context);
        if (TextUtils.isEmpty(m2)) {
            return "";
        }
        String c2 = g.c(m2);
        z = c2;
        return c2;
    }

    public static String o(Context context) {
        if (!TextUtils.isEmpty(E)) {
            return E;
        }
        String m2 = m(context);
        if (TextUtils.isEmpty(m2)) {
            return "";
        }
        String d2 = g.d(m2);
        E = d2;
        return d2;
    }

    public static String p(Context context) {
        if (!TextUtils.isEmpty(s)) {
            return s;
        }
        String string = Settings.System.getString(context.getContentResolver(), "android_id");
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        s = string;
        return string;
    }

    public static String q(Context context) {
        if (!TextUtils.isEmpty(t)) {
            return t;
        }
        try {
            String type = context.getContentResolver().getType(Uri.parse("content://com.miui.analytics.server.AnalyticsProvider/aaid"));
            if (!TextUtils.isEmpty(type)) {
                t = type;
                return type;
            }
            Object invoke = Class.forName("android.provider.MiuiSettings$Ad").getDeclaredMethod("getAaid", ContentResolver.class).invoke(null, context.getContentResolver());
            if (!(invoke instanceof String) || TextUtils.isEmpty((String) invoke)) {
                return "";
            }
            String str = (String) invoke;
            t = str;
            return str;
        } catch (Exception e2) {
            k.b(a, "getAaid failed ex: " + e2.getMessage());
            return "";
        }
    }

    public static String r(Context context) {
        if (!TextUtils.isEmpty(u)) {
            return u;
        }
        String a2 = a.a(context);
        if (TextUtils.isEmpty(a2)) {
            return "";
        }
        u = a2;
        return a2;
    }

    private static List<String> y(Context context) {
        List<String> list;
        if (a(context, "android.permission.READ_PHONE_STATE")) {
            list = f();
            if (list == null || list.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 21) {
                    list = z(context);
                } else {
                    list = A(context);
                }
            }
        } else {
            list = null;
        }
        if (list != null && !list.isEmpty()) {
            Collections.sort(list);
            n = list.get(0);
            if (list.size() >= 2) {
                o = list.get(1);
            }
        }
        return list;
    }

    private static List<String> f() {
        if (j == null || h()) {
            return null;
        }
        try {
            List<String> list = (List) j.invoke(l, new Object[0]);
            if (list == null || list.size() <= 0) {
                return null;
            }
            if (a(list)) {
                return null;
            }
            return list;
        } catch (Exception e2) {
            k.b(a, "getImeiListFromMiui failed ex: " + e2.getMessage());
            return null;
        }
    }

    private static List<String> z(Context context) {
        if (m != null) {
            try {
                ArrayList arrayList = new ArrayList();
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                String str = (String) m.invoke(telephonyManager, 0);
                if (c(str)) {
                    arrayList.add(str);
                }
                if (g()) {
                    String str2 = (String) m.invoke(telephonyManager, 1);
                    if (c(str2)) {
                        arrayList.add(str2);
                    }
                }
                return arrayList;
            } catch (Exception e2) {
                k.b(a, "getImeiListAboveLollipop failed ex: " + e2.getMessage());
                return null;
            }
        }
        return null;
    }

    private static List<String> A(Context context) {
        try {
            ArrayList arrayList = new ArrayList();
            Class<?> cls = Class.forName("android.telephony.TelephonyManager");
            if (!g()) {
                String deviceId = ((TelephonyManager) cls.getMethod("getDefault", new Class[0]).invoke(null, new Object[0])).getDeviceId();
                if (c(deviceId)) {
                    arrayList.add(deviceId);
                }
                return arrayList;
            }
            Class<?> cls2 = Integer.TYPE;
            String deviceId2 = ((TelephonyManager) cls.getMethod("getDefault", cls2).invoke(null, 0)).getDeviceId();
            String deviceId3 = ((TelephonyManager) cls.getMethod("getDefault", cls2).invoke(null, 1)).getDeviceId();
            if (c(deviceId2)) {
                arrayList.add(deviceId2);
            }
            if (c(deviceId3)) {
                arrayList.add(deviceId3);
            }
            return arrayList;
        } catch (Exception e2) {
            k.b(a, "getImeiListBelowLollipop failed ex: " + e2.getMessage());
            return null;
        }
    }

    public static String s(Context context) {
        if (a(context, "android.permission.READ_PHONE_STATE")) {
            Method method = k;
            if (method != null) {
                try {
                    List list = (List) method.invoke(l, new Object[0]);
                    if (list != null && list.size() > 0 && !b(list)) {
                        Collections.sort(list);
                        return (String) list.get(0);
                    }
                } catch (Exception e2) {
                    k.b(a, "queryMeid failed ex: " + e2.getMessage());
                }
            }
            try {
                Class<?> cls = Class.forName("android.telephony.TelephonyManager");
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                String str = null;
                if (Build.VERSION.SDK_INT >= 26) {
                    Method method2 = cls.getMethod("getMeid", new Class[0]);
                    if (method2 != null) {
                        str = (String) method2.invoke(telephonyManager, new Object[0]);
                    }
                } else {
                    Method method3 = cls.getMethod("getDeviceId", new Class[0]);
                    if (method3 != null) {
                        str = (String) method3.invoke(telephonyManager, new Object[0]);
                    }
                }
                return d(str) ? str : "";
            } catch (Exception e3) {
                k.b(a, "queryMeid->getMeid failed ex: " + e3.getMessage());
                return "";
            }
        }
        return "";
    }

    private static boolean g() {
        if ("dsds".equals(b("persist.radio.multisim.config"))) {
            return true;
        }
        String str = Build.DEVICE;
        return "lcsh92_wet_jb9".equals(str) || "lcsh92_wet_tdd".equals(str) || "HM2013022".equals(str) || "HM2013023".equals(str) || "armani".equals(str) || "HM2014011".equals(str) || "HM2014012".equals(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(String str) {
        try {
            Method method = i;
            if (method != null) {
                return String.valueOf(method.invoke(null, str));
            }
        } catch (Exception e2) {
            k.b(a, "getProp failed ex: " + e2.getMessage());
        }
        return null;
    }

    private static boolean h() {
        if (Build.VERSION.SDK_INT >= 21) {
            return false;
        }
        String str = Build.DEVICE;
        String b2 = b("persist.radio.modem");
        if ("HM2014812".equals(str) || "HM2014821".equals(str)) {
            return true;
        }
        return ("gucci".equals(str) && "ct".equals(b("persist.sys.modem"))) || "CDMA".equals(b2) || "HM1AC".equals(b2) || "LTE-X5-ALL".equals(b2) || "LTE-CT".equals(b2) || "MI 3C".equals(Build.MODEL);
    }

    private static boolean a(List<String> list) {
        for (String str : list) {
            if (!c(str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean b(List<String> list) {
        for (String str : list) {
            if (!d(str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean c(String str) {
        return str != null && str.length() == 15 && !str.matches("^0*$");
    }

    private static boolean d(String str) {
        return str != null && str.length() == 14 && !str.matches("^0*$");
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0041 A[Catch: Exception -> 0x0091, TryCatch #1 {Exception -> 0x0091, blocks: (B:15:0x0037, B:16:0x003b, B:18:0x0041, B:20:0x004d, B:23:0x0051, B:26:0x005c, B:27:0x0072, B:29:0x0078, B:30:0x0080, B:32:0x008c), top: B:39:0x0037 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String B(android.content.Context r11) {
        /*
            java.lang.String r0 = "android.permission.ACCESS_WIFI_STATE"
            boolean r0 = a(r11, r0)
            r1 = 0
            if (r0 == 0) goto Laa
            int r0 = android.os.Build.VERSION.SDK_INT
            r2 = 23
            java.lang.String r3 = "DeviceUtil"
            if (r0 >= r2) goto L28
            java.lang.String r0 = "wifi"
            java.lang.Object r11 = r11.getSystemService(r0)     // Catch: java.lang.Exception -> L22
            android.net.wifi.WifiManager r11 = (android.net.wifi.WifiManager) r11     // Catch: java.lang.Exception -> L22
            android.net.wifi.WifiInfo r11 = r11.getConnectionInfo()     // Catch: java.lang.Exception -> L22
            java.lang.String r11 = r11.getMacAddress()     // Catch: java.lang.Exception -> L22
            goto L29
        L22:
            r11 = move-exception
            java.lang.String r0 = "getMAC exception: "
            com.xiaomi.stat.d.k.d(r3, r0, r11)
        L28:
            r11 = r1
        L29:
            boolean r0 = android.text.TextUtils.isEmpty(r11)
            if (r0 != 0) goto L37
            java.lang.String r0 = "02:00:00:00:00:00"
            boolean r11 = r0.equals(r11)
            if (r11 == 0) goto Laa
        L37:
            java.util.Enumeration r11 = java.net.NetworkInterface.getNetworkInterfaces()     // Catch: java.lang.Exception -> L91
        L3b:
            boolean r0 = r11.hasMoreElements()     // Catch: java.lang.Exception -> L91
            if (r0 == 0) goto Laa
            java.lang.Object r0 = r11.nextElement()     // Catch: java.lang.Exception -> L91
            java.net.NetworkInterface r0 = (java.net.NetworkInterface) r0     // Catch: java.lang.Exception -> L91
            byte[] r2 = r0.getHardwareAddress()     // Catch: java.lang.Exception -> L91
            if (r2 == 0) goto L3b
            int r4 = r2.length     // Catch: java.lang.Exception -> L91
            if (r4 != 0) goto L51
            goto L3b
        L51:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L91
            r4.<init>()     // Catch: java.lang.Exception -> L91
            int r5 = r2.length     // Catch: java.lang.Exception -> L91
            r6 = 0
            r7 = r6
        L59:
            r8 = 1
            if (r7 >= r5) goto L72
            r9 = r2[r7]     // Catch: java.lang.Exception -> L91
            java.lang.String r10 = "%02x:"
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch: java.lang.Exception -> L91
            java.lang.Byte r9 = java.lang.Byte.valueOf(r9)     // Catch: java.lang.Exception -> L91
            r8[r6] = r9     // Catch: java.lang.Exception -> L91
            java.lang.String r8 = java.lang.String.format(r10, r8)     // Catch: java.lang.Exception -> L91
            r4.append(r8)     // Catch: java.lang.Exception -> L91
            int r7 = r7 + 1
            goto L59
        L72:
            int r2 = r4.length()     // Catch: java.lang.Exception -> L91
            if (r2 <= 0) goto L80
            int r2 = r4.length()     // Catch: java.lang.Exception -> L91
            int r2 = r2 - r8
            r4.deleteCharAt(r2)     // Catch: java.lang.Exception -> L91
        L80:
            java.lang.String r2 = "wlan0"
            java.lang.String r0 = r0.getName()     // Catch: java.lang.Exception -> L91
            boolean r0 = r2.equals(r0)     // Catch: java.lang.Exception -> L91
            if (r0 == 0) goto L3b
            java.lang.String r11 = r4.toString()     // Catch: java.lang.Exception -> L91
            return r11
        L91:
            r11 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "queryMac failed ex: "
            r0.append(r2)
            java.lang.String r11 = r11.getMessage()
            r0.append(r11)
            java.lang.String r11 = r0.toString()
            com.xiaomi.stat.d.k.b(r3, r11)
        Laa:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.stat.d.e.B(android.content.Context):java.lang.String");
    }

    public static String t(Context context) {
        String str = null;
        if (Build.VERSION.SDK_INT >= 26) {
            if (a(context, "android.permission.READ_PHONE_STATE")) {
                try {
                    Method method = Class.forName("android.os.Build").getMethod("getSerial", new Class[0]);
                    if (method != null) {
                        str = (String) method.invoke(null, new Object[0]);
                    }
                } catch (Exception e2) {
                    k.b(a, "querySerial failed ex: " + e2.getMessage());
                }
            }
        } else {
            str = Build.SERIAL;
        }
        if (TextUtils.isEmpty(str) || "unknown".equals(str)) {
            return "";
        }
        r = str;
        return str;
    }

    private static boolean a(Context context, String str) {
        return context.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    /* loaded from: classes3.dex */
    public static class a {
        private static final String a = "GAIDClient";

        private a() {
        }

        public static String a(Context context) {
            if (!b(context)) {
                k.b(a, "Google play service is not available");
                return "";
            }
            ServiceConnectionC0114a serviceConnectionC0114a = new ServiceConnectionC0114a();
            try {
                try {
                    Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                    intent.setPackage("com.google.android.gms");
                    if (context.bindService(intent, serviceConnectionC0114a, 1)) {
                        return new b(serviceConnectionC0114a.a()).a();
                    }
                } catch (Exception e) {
                    k.b(a, "Query Google ADID failed ", e);
                }
                return "";
            } finally {
                context.unbindService(serviceConnectionC0114a);
            }
        }

        private static boolean b(Context context) {
            try {
                context.getPackageManager().getPackageInfo("com.android.vending", 16384);
                return true;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }

        /* renamed from: com.xiaomi.stat.d.e$a$a  reason: collision with other inner class name */
        /* loaded from: classes3.dex */
        public static final class ServiceConnectionC0114a implements ServiceConnection {
            private static final int a = 30000;
            private boolean b;
            private IBinder c;

            private ServiceConnectionC0114a() {
                this.b = false;
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                synchronized (this) {
                    this.c = iBinder;
                    notifyAll();
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                this.b = true;
                this.c = null;
            }

            public IBinder a() throws InterruptedException {
                IBinder iBinder = this.c;
                if (iBinder != null) {
                    return iBinder;
                }
                if (iBinder == null && !this.b) {
                    synchronized (this) {
                        wait(30000L);
                        if (this.c == null) {
                            throw new InterruptedException("Not connect or connect timeout to google play service");
                        }
                    }
                }
                return this.c;
            }
        }

        /* loaded from: classes3.dex */
        public static final class b implements IInterface {
            private IBinder a;

            public b(IBinder iBinder) {
                this.a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.a;
            }

            public String a() throws RemoteException {
                if (this.a == null) {
                    return "";
                }
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                    this.a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean a(boolean z) throws RemoteException {
                boolean z2 = false;
                if (this.a == null) {
                    return false;
                }
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                    obtain.writeInt(z ? 1 : 0);
                    this.a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z2 = true;
                    }
                    return z2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }

    public static String b() {
        return Build.MODEL;
    }

    public static String c() {
        return Build.MANUFACTURER;
    }

    public static String u(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealSize(point);
        } else {
            defaultDisplay.getSize(point);
        }
        return String.format("%d*%d", Integer.valueOf(point.y), Integer.valueOf(point.x));
    }

    public static String v(Context context) {
        if (!TextUtils.isEmpty(G)) {
            return G;
        }
        if (i()) {
            G = "Pad";
            return "Pad";
        } else if (C(context)) {
            G = "Pad";
            return "Pad";
        } else if (!D(context)) {
            return "Phone";
        } else {
            G = "Tv";
            return "Tv";
        }
    }

    private static boolean i() {
        try {
            return ((Boolean) Class.forName("miui.os.Build").getField("IS_TABLET").get(null)).booleanValue();
        } catch (Exception unused) {
            try {
                return ((Boolean) Class.forName("miui.util.FeatureParser").getMethod("getBoolean", String.class, Boolean.TYPE).invoke(null, "is_pad", Boolean.FALSE)).booleanValue();
            } catch (Exception unused2) {
                return false;
            }
        }
    }

    private static boolean C(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static boolean w(Context context) {
        if (F == null) {
            F = Boolean.valueOf(b.a(context));
        }
        return F.booleanValue();
    }

    private static boolean D(Context context) {
        return b.a(context) || (context.getResources().getConfiguration().uiMode & 15) == 4;
    }

    public static String d() {
        if (!TextUtils.isEmpty(H)) {
            return H;
        }
        boolean e2 = com.xiaomi.stat.b.e();
        String s2 = com.xiaomi.stat.b.s();
        if (!TextUtils.isEmpty(s2)) {
            if (!e2) {
                H = s2;
                return s2;
            }
            long b2 = r.b();
            if (b2 - com.xiaomi.stat.b.v() <= e) {
                H = s2;
                com.xiaomi.stat.b.b(b2);
                return H;
            }
        }
        if (e2 && !p.k(ak.a())) {
            Context a2 = ak.a();
            p.b(a2, true);
            String string = a2.getSharedPreferences(f, 0).getString(h, null);
            k.c(a, "last version instance id: " + string);
            H = string;
        }
        if (TextUtils.isEmpty(H)) {
            H = e();
        }
        com.xiaomi.stat.b.g(H);
        if (e2) {
            com.xiaomi.stat.b.b(r.b());
        }
        return H;
    }

    public static String x(Context context) {
        if (!J) {
            J = true;
            if (!p.i(context)) {
                p.a(context, true);
                p.f(context, context.getSharedPreferences(f, 0).getString(g, null));
            }
            I = p.j(context);
        }
        return I;
    }

    /* loaded from: classes3.dex */
    public static class b {
        private static final String a = "box";
        private static final String b = "tvbox";
        private static final String c = "projector";
        private static final String d = "tv";
        private static final String e = "mi_device_mac";
        private static Signature[] f;
        private static final Signature g = new Signature("3082033b30820223a003020102020900a07a328482f70d2a300d06092a864886f70d01010505003035310b30090603550406130255533113301106035504080c0a43616c69666f726e69613111300f06035504070c084d6f756e7461696e301e170d3133303430313033303831325a170d3430303831373033303831325a3035310b30090603550406130255533113301106035504080c0a43616c69666f726e69613111300f06035504070c084d6f756e7461696e30820120300d06092a864886f70d01010105000382010d00308201080282010100ac678c9234a0226edbeb75a43e8e18f632d8c8a094c087fffbbb0b5e4429d845e36bffbe2d7098e320855258aa777368c18c538f968063d5d61663dc946ab03acbb31d00a27d452e12e6d42865e27d6d0ad2d8b12cf3b3096a7ec66a21db2a6a697857fd4d29fb4cdf294b3371d7601f2e3f190c0164efa543897026c719b334808e4f612fe3a3da589115fc30f9ca89862feefdf31a9164ecb295dcf7767e673be2192dda64f88189fd6e6ebd62e572c7997c2385a0ea9292ec799dee8f87596fc73aa123fb6f577d09ac0c123179c3bdbc978c2fe6194eb9fa4ab3658bfe8b44040bb13fe7809409e622189379fbc63966ab36521793547b01673ecb5f15cf020103a350304e301d0603551d0e0416041447203684e562385ada79108c4c94c5055037592f301f0603551d2304183016801447203684e562385ada79108c4c94c5055037592f300c0603551d13040530030101ff300d06092a864886f70d010105050003820101008d530fe05c6fe694c7559ddb5dd2c556528dd3cad4f7580f439f9a90a4681d37ce246b9a6681bdd5a5437f0b8bba903e39bac309fc0e9ee5553681612e723e9ec4f6abab6b643b33013f09246a9b5db7703b96f838fb27b00612f5fcd431bea32f68350ae51a4a1d012c520c401db7cccc15a7b19c4310b0c3bfc625ce5744744d0b9eeb02b0a4e7d51ed59849ce580b9f7c3062c84b9a0b13cc211e1c916c289820266a610801e3316c915649804571b147beadbf88d3b517ee04121d40630853f2f2a506bb788620de9648faeacff568e5033a666316bc2046526674ed3de25ceefdc4ad3628f1a230fd41bf9ca9f6a078173850dba555768fe1c191483ad9");

        private b() {
        }

        public static boolean a(Context context) {
            if (f == null) {
                f = new Signature[]{c(context)};
            }
            Signature[] signatureArr = f;
            return signatureArr[0] != null && signatureArr[0].equals(g);
        }

        private static Signature a(PackageInfo packageInfo) {
            Signature[] signatureArr;
            if (packageInfo == null || (signatureArr = packageInfo.signatures) == null || signatureArr.length <= 0) {
                return null;
            }
            return signatureArr[0];
        }

        private static Signature c(Context context) {
            try {
                return a(context.getPackageManager().getPackageInfo("android", 64));
            } catch (Exception unused) {
                return null;
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x004e, code lost:
            if ("casablanca".equals(r1) != false) goto L12;
         */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0059 A[Catch: Exception -> 0x009a, TryCatch #0 {Exception -> 0x009a, blocks: (B:8:0x001a, B:10:0x002f, B:12:0x0037, B:20:0x0051, B:22:0x0059, B:33:0x008e, B:35:0x0094, B:23:0x0060, B:25:0x0068, B:29:0x0078, B:27:0x0070, B:31:0x0081, B:32:0x0088, B:15:0x0040, B:17:0x0048), top: B:42:0x001a }] */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0060 A[Catch: Exception -> 0x009a, TryCatch #0 {Exception -> 0x009a, blocks: (B:8:0x001a, B:10:0x002f, B:12:0x0037, B:20:0x0051, B:22:0x0059, B:33:0x008e, B:35:0x0094, B:23:0x0060, B:25:0x0068, B:29:0x0078, B:27:0x0070, B:31:0x0081, B:32:0x0088, B:15:0x0040, B:17:0x0048), top: B:42:0x001a }] */
        /* JADX WARN: Removed duplicated region for block: B:35:0x0094 A[Catch: Exception -> 0x009a, TRY_LEAVE, TryCatch #0 {Exception -> 0x009a, blocks: (B:8:0x001a, B:10:0x002f, B:12:0x0037, B:20:0x0051, B:22:0x0059, B:33:0x008e, B:35:0x0094, B:23:0x0060, B:25:0x0068, B:29:0x0078, B:27:0x0070, B:31:0x0081, B:32:0x0088, B:15:0x0040, B:17:0x0048), top: B:42:0x001a }] */
        /* JADX WARN: Removed duplicated region for block: B:37:0x0099 A[RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static java.lang.String b(android.content.Context r5) {
            /*
                java.lang.String r0 = ""
                int r1 = android.os.Build.VERSION.SDK_INT
                r2 = 17
                if (r1 < r2) goto L19
                android.content.ContentResolver r5 = r5.getContentResolver()     // Catch: java.lang.Exception -> L19
                java.lang.String r1 = "mi_device_mac"
                java.lang.String r5 = android.provider.Settings.Global.getString(r5, r1)     // Catch: java.lang.Exception -> L19
                boolean r1 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Exception -> L19
                if (r1 != 0) goto L19
                return r5
            L19:
                r5 = 0
                java.lang.String r1 = android.os.Build.PRODUCT     // Catch: java.lang.Exception -> L9a
                java.lang.String r2 = "ro.product.model"
                java.lang.String r2 = com.xiaomi.stat.d.e.a(r2)     // Catch: java.lang.Exception -> L9a
                java.lang.String r3 = a()     // Catch: java.lang.Exception -> L9a
                java.lang.String r4 = "tv"
                boolean r3 = android.text.TextUtils.equals(r4, r3)     // Catch: java.lang.Exception -> L9a
                r4 = 1
                if (r3 == 0) goto L40
                java.lang.String r3 = "batman"
                boolean r3 = r3.equals(r1)     // Catch: java.lang.Exception -> L9a
                if (r3 != 0) goto L40
                java.lang.String r3 = "conan"
                boolean r3 = r3.equals(r1)     // Catch: java.lang.Exception -> L9a
                if (r3 != 0) goto L40
                goto L50
            L40:
                java.lang.String r3 = "augustrush"
                boolean r3 = r3.equals(r1)     // Catch: java.lang.Exception -> L9a
                if (r3 != 0) goto L50
                java.lang.String r3 = "casablanca"
                boolean r3 = r3.equals(r1)     // Catch: java.lang.Exception -> L9a
                if (r3 == 0) goto L51
            L50:
                r5 = r4
            L51:
                java.lang.String r3 = "me2"
                boolean r3 = android.text.TextUtils.equals(r3, r1)     // Catch: java.lang.Exception -> L9a
                if (r3 == 0) goto L60
                java.lang.String r5 = "persist.service.bdroid.bdaddr"
                java.lang.String r5 = com.xiaomi.stat.d.e.a(r5)     // Catch: java.lang.Exception -> L9a
                goto L8e
            L60:
                java.lang.String r3 = "transformers"
                boolean r3 = android.text.TextUtils.equals(r3, r1)     // Catch: java.lang.Exception -> L9a
                if (r3 == 0) goto L70
                java.lang.String r3 = "MiBOX4C"
                boolean r2 = android.text.TextUtils.equals(r3, r2)     // Catch: java.lang.Exception -> L9a
                if (r2 != 0) goto L78
            L70:
                java.lang.String r2 = "dolphin-zorro"
                boolean r1 = android.text.TextUtils.equals(r2, r1)     // Catch: java.lang.Exception -> L9a
                if (r1 == 0) goto L7f
            L78:
                java.lang.String r5 = "/sys/class/net/wlan0/address"
                java.lang.String r5 = a(r5)     // Catch: java.lang.Exception -> L9a
                goto L8e
            L7f:
                if (r5 == 0) goto L88
                java.lang.String r5 = "/sys/class/net/eth0/address"
                java.lang.String r5 = a(r5)     // Catch: java.lang.Exception -> L9a
                goto L8e
            L88:
                java.lang.String r5 = "ro.boot.btmac"
                java.lang.String r5 = a(r5)     // Catch: java.lang.Exception -> L9a
            L8e:
                boolean r1 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Exception -> L9a
                if (r1 != 0) goto L99
                java.lang.String r5 = r5.trim()     // Catch: java.lang.Exception -> L9a
                return r5
            L99:
                return r0
            L9a:
                r5 = move-exception
                java.lang.String r1 = "DeviceUtil"
                java.lang.String r2 = "getMiTvMac exception"
                com.xiaomi.stat.d.k.b(r1, r2, r5)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.stat.d.e.b.b(android.content.Context):java.lang.String");
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static String a(String str) {
            BufferedReader bufferedReader;
            String str2 = "";
            BufferedReader bufferedReader2 = null;
            BufferedReader bufferedReader3 = null;
            BufferedReader bufferedReader4 = null;
            try {
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(str)), 512);
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Exception e2) {
                e = e2;
            }
            try {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(readLine);
                    str2 = sb.toString();
                    bufferedReader3 = sb;
                }
                j.a((Reader) bufferedReader);
                bufferedReader2 = bufferedReader3;
            } catch (Exception e3) {
                e = e3;
                bufferedReader4 = bufferedReader;
                k.d(e.a, "catEntry exception", e);
                j.a((Reader) bufferedReader4);
                bufferedReader2 = bufferedReader4;
                return str2;
            } catch (Throwable th2) {
                th = th2;
                bufferedReader2 = bufferedReader;
                j.a((Reader) bufferedReader2);
                throw th;
            }
            return str2;
        }

        private static String a() {
            try {
                Class<?> cls = Class.forName("mitv.common.ConfigurationManager");
                int parseInt = Integer.parseInt(String.valueOf(cls.getMethod("getProductCategory", new Class[0]).invoke(cls.getMethod("getInstance", new Class[0]).invoke(cls, new Object[0]), new Object[0])));
                Class<?> cls2 = Class.forName("mitv.tv.TvContext");
                return parseInt == Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MITV"))) ? "tv" : parseInt == Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MIBOX"))) ? a : parseInt == Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MITVBOX"))) ? b : parseInt == Integer.parseInt(String.valueOf(a(cls2, "PRODUCT_CATEGORY_MIPROJECTOR"))) ? c : "";
            } catch (Exception e2) {
                k.b(e.a, "getMiTvProductCategory exception", e2);
                return "";
            }
        }

        private static <T> T a(Class<?> cls, String str) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                declaredField.setAccessible(true);
                return (T) declaredField.get(null);
            } catch (Exception e2) {
                k.d(e.a, "getStaticVariableValue exception", e2);
                return null;
            }
        }
    }
}
