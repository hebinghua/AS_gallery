package com.xiaomi.onetrack.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.e.a;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/* loaded from: classes3.dex */
public class q {
    public static Method E;
    public static boolean F;
    public static String G;
    public static int I;
    public static final Set<String> J;
    public static Class k;
    public static Method l;
    public static Boolean m;
    public static final DecimalFormat y = new DecimalFormat("#0");
    public static final DecimalFormat z = new DecimalFormat("#0.#");
    public static volatile long A = 0;
    public static boolean H = false;

    static {
        try {
            E = Class.forName("android.os.SystemProperties").getMethod(CallMethod.METHOD_GET, String.class);
        } catch (Throwable th) {
            p.b("OsUtil", "sGetProp init failed ex: " + th.getMessage());
        }
        try {
            k = Class.forName("miui.os.Build");
        } catch (Throwable th2) {
            p.b("OsUtil", "sMiuiBuild init failed ex: " + th2.getMessage());
        }
        try {
            Method declaredMethod = Class.forName("android.provider.MiuiSettings$Secure").getDeclaredMethod("isUserExperienceProgramEnable", ContentResolver.class);
            l = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (Throwable th3) {
            p.b("OsUtil", "sMiuiUEPMethod init failed ex: " + th3.getMessage());
        }
        J = new HashSet(Arrays.asList("AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GR", "HR", "HU", "IE", "IT", "LT", "LU", "LV", "MT", "NL", "PL", "PT", "RO", "SE", "SI", "SK"));
    }

    public static String f(String str) {
        try {
            Method method = E;
            if (method != null) {
                return String.valueOf(method.invoke(null, str));
            }
        } catch (Exception e) {
            p.b("OsUtil", "getProp failed ex: " + e.getMessage());
        }
        return null;
    }

    public static boolean a() {
        Boolean bool = m;
        if (bool != null) {
            return bool.booleanValue();
        }
        if (!TextUtils.isEmpty(f("ro.miui.ui.version.code"))) {
            m = Boolean.TRUE;
        } else {
            m = Boolean.FALSE;
        }
        return m.booleanValue();
    }

    public static String b() {
        return a(TimeZone.getDefault().getRawOffset());
    }

    public static String a(int i) {
        try {
            int i2 = i / 60000;
            char c = '+';
            if (i2 < 0) {
                c = CoreConstants.DASH_CHAR;
                i2 = -i2;
            }
            StringBuilder sb = new StringBuilder(9);
            sb.append("GMT");
            sb.append(c);
            a(sb, i2 / 60);
            sb.append(CoreConstants.COLON_CHAR);
            a(sb, i2 % 60);
            return sb.toString();
        } catch (Exception unused) {
            return "";
        }
    }

    public static void a(StringBuilder sb, int i) {
        String num = Integer.toString(i);
        for (int i2 = 0; i2 < 2 - num.length(); i2++) {
            sb.append('0');
        }
        sb.append(num);
    }

    public static String d() {
        Class cls = k;
        if (cls != null) {
            try {
                return ((Boolean) cls.getField("IS_ALPHA_BUILD").get(null)).booleanValue() ? "A" : ((Boolean) k.getField("IS_DEVELOPMENT_VERSION").get(null)).booleanValue() ? "D" : ((Boolean) k.getField("IS_STABLE_VERSION").get(null)).booleanValue() ? "S" : "";
            } catch (Exception e) {
                Log.e("OsUtil", "getRomBuildCode failed: " + e.toString());
                return "";
            }
        }
        return "";
    }

    public static boolean b(Context context) {
        Method method = l;
        if (method == null) {
            return true;
        }
        try {
            return ((Boolean) method.invoke(null, context.getContentResolver())).booleanValue();
        } catch (Exception e) {
            Log.e("OsUtil", "isUserExperiencePlanEnabled failed: " + e.toString());
            return true;
        }
    }

    public static boolean a(String str) {
        if (OneTrack.isDisable() || OneTrack.isUseSystemNetTrafficOnly()) {
            p.c(str, "should not access network or location, cta");
            return true;
        } else if (!w()) {
            p.c(str, "should not access network or location, not provisioned");
            return true;
        } else if (com.xiaomi.onetrack.b.h.b()) {
            return false;
        } else {
            p.c(str, "should not access network or location, cta");
            return true;
        }
    }

    public static String h() {
        return Build.VERSION.INCREMENTAL;
    }

    public static String A() {
        try {
            String a = ab.a("ro.miui.region", "");
            if (TextUtils.isEmpty(a)) {
                a = ab.a("ro.product.locale.region", "");
            }
            if (TextUtils.isEmpty(a) && Build.VERSION.SDK_INT >= 24) {
                Object invoke = Class.forName("android.os.LocaleList").getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
                Object invoke2 = invoke.getClass().getMethod(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, new Class[0]).invoke(invoke, new Object[0]);
                if ((invoke2 instanceof Integer) && ((Integer) invoke2).intValue() > 0) {
                    Object invoke3 = invoke.getClass().getMethod(CallMethod.METHOD_GET, Integer.TYPE).invoke(invoke, 0);
                    Object invoke4 = invoke3.getClass().getMethod("getCountry", new Class[0]).invoke(invoke3, new Object[0]);
                    if (invoke4 instanceof String) {
                        a = (String) invoke4;
                    }
                }
            }
            if (TextUtils.isEmpty(a)) {
                a = Locale.getDefault().getCountry();
            }
            if (!TextUtils.isEmpty(a)) {
                return a.trim();
            }
        } catch (Throwable th) {
            p.b("OsUtil", "getRegion Exception: " + th.getMessage());
        }
        return "";
    }

    public static String i() {
        return Build.VERSION.RELEASE;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int r() {
        /*
            java.lang.String r0 = "OsUtil"
            r1 = 0
            r2 = 0
            int r3 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L49
            r4 = 17
            if (r3 < r4) goto L6a
            java.lang.String r3 = "android.os.UserHandle"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.Throwable -> L49
            java.lang.String r4 = "getUserId"
            r5 = 1
            java.lang.Class[] r6 = new java.lang.Class[r5]     // Catch: java.lang.Throwable -> L49
            java.lang.Class r7 = java.lang.Integer.TYPE     // Catch: java.lang.Throwable -> L49
            r6[r2] = r7     // Catch: java.lang.Throwable -> L49
            java.lang.reflect.Method r3 = r3.getDeclaredMethod(r4, r6)     // Catch: java.lang.Throwable -> L49
            r3.setAccessible(r5)     // Catch: java.lang.Throwable -> L49
            int r4 = android.os.Process.myUid()     // Catch: java.lang.Throwable -> L49
            java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L49
            java.lang.Integer r7 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L49
            r6[r2] = r7     // Catch: java.lang.Throwable -> L49
            java.lang.Object r3 = r3.invoke(r1, r6)     // Catch: java.lang.Throwable -> L49
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch: java.lang.Throwable -> L49
            java.lang.String r1 = "getUserId, uid:%d, userId:%d"
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch: java.lang.Throwable -> L47
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L47
            r6[r2] = r4     // Catch: java.lang.Throwable -> L47
            r6[r5] = r3     // Catch: java.lang.Throwable -> L47
            java.lang.String r1 = java.lang.String.format(r1, r6)     // Catch: java.lang.Throwable -> L47
            com.xiaomi.onetrack.util.p.a(r0, r1)     // Catch: java.lang.Throwable -> L47
            goto L69
        L47:
            r1 = move-exception
            goto L4d
        L49:
            r3 = move-exception
            r8 = r3
            r3 = r1
            r1 = r8
        L4d:
            java.lang.String r0 = com.xiaomi.onetrack.util.p.a(r0)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "getUserId exception: "
            r4.append(r5)
            java.lang.String r1 = r1.getMessage()
            r4.append(r1)
            java.lang.String r1 = r4.toString()
            android.util.Log.e(r0, r1)
        L69:
            r1 = r3
        L6a:
            if (r1 != 0) goto L70
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
        L70:
            int r0 = r1.intValue()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.onetrack.util.q.r():int");
    }

    @TargetApi(17)
    public static boolean w() {
        try {
            Context b = a.b();
            if (Build.VERSION.SDK_INT < 17) {
                return true;
            }
            boolean z2 = false;
            if (Settings.Global.getInt(b.getContentResolver(), "device_provisioned", 0) != 0) {
                z2 = true;
            }
            if (!z2) {
                p.c("OsUtil", "Provisioned: " + z2);
            }
            return z2;
        } catch (Exception e) {
            p.b("OsUtil", "isDeviceProvisioned exception", e);
            return true;
        }
    }

    public static boolean C() {
        Class cls = k;
        if (cls != null) {
            try {
                return ((Boolean) cls.getField("IS_INTERNATIONAL_BUILD").get(null)).booleanValue();
            } catch (Exception unused) {
            }
        }
        String A2 = A();
        if (!TextUtils.isEmpty(A2)) {
            return !TextUtils.equals("CN", A2.toUpperCase());
        }
        return false;
    }

    public static boolean x() {
        if (a() && !H) {
            return C();
        }
        return F;
    }

    public static String y() {
        if (!a() || H) {
            return !TextUtils.isEmpty(G) ? G : A();
        }
        return A();
    }

    public static void a(boolean z2, String str, OneTrack.Mode mode) {
        int i;
        if (mode == OneTrack.Mode.APP) {
            i = 3;
        } else if (mode == OneTrack.Mode.PLUGIN) {
            i = 2;
        } else {
            i = mode == OneTrack.Mode.SDK ? 1 : 0;
        }
        if (I <= i) {
            F = z2;
            G = str;
            I = i;
        }
    }

    public static void a(boolean z2) {
        H = z2;
    }

    public static String z() {
        return A();
    }
}
