package miuix.view;

import androidx.annotation.Keep;
import miui.util.HapticFeedbackUtil;

@Keep
/* loaded from: classes3.dex */
public class PlatformConstants {
    public static final int VERSION;
    public static double romHapticVersion = 1.0d;

    /* JADX WARN: Can't wrap try/catch for region: R(10:1|2|3|(2:5|(6:7|8|9|10|11|12))|21|8|9|10|11|12) */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x006e, code lost:
        android.util.Log.w("HapticCompat", "have no access to the definition of getCurVersion()");
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0074, code lost:
        android.util.Log.w("HapticCompat", "method getCurVersion() called using Reflection failed");
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007a, code lost:
        r1 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x007c, code lost:
        r1 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x007d, code lost:
        android.util.Log.w("HapticCompat", "MIUI Haptic Implementation not found.", r1);
     */
    static {
        /*
            java.lang.String r0 = "MIUI Haptic Implementation not found."
            java.lang.String r1 = "miui.util.HapticFeedbackUtil"
            java.lang.String r2 = "HapticCompat"
            r3 = 1
            r4 = 0
            java.lang.Class r5 = java.lang.Class.forName(r1)     // Catch: java.lang.NoSuchFieldException -> L2c java.lang.NoClassDefFoundError -> L37 java.lang.NoSuchMethodException -> L39 java.lang.ClassNotFoundException -> L3b
            java.lang.String r6 = "miui.view.MiuiHapticFeedbackConstants"
            java.lang.Class r6 = java.lang.Class.forName(r6)     // Catch: java.lang.NoSuchFieldException -> L2c java.lang.NoClassDefFoundError -> L37 java.lang.NoSuchMethodException -> L39 java.lang.ClassNotFoundException -> L3b
            java.lang.String r7 = "isSupportLinearMotorVibrate"
            java.lang.Class[] r8 = new java.lang.Class[r3]     // Catch: java.lang.NoSuchFieldException -> L2c java.lang.NoClassDefFoundError -> L37 java.lang.NoSuchMethodException -> L39 java.lang.ClassNotFoundException -> L3b
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch: java.lang.NoSuchFieldException -> L2c java.lang.NoClassDefFoundError -> L37 java.lang.NoSuchMethodException -> L39 java.lang.ClassNotFoundException -> L3b
            r8[r4] = r9     // Catch: java.lang.NoSuchFieldException -> L2c java.lang.NoClassDefFoundError -> L37 java.lang.NoSuchMethodException -> L39 java.lang.ClassNotFoundException -> L3b
            java.lang.reflect.Method r5 = r5.getMethod(r7, r8)     // Catch: java.lang.NoSuchFieldException -> L2c java.lang.NoClassDefFoundError -> L37 java.lang.NoSuchMethodException -> L39 java.lang.ClassNotFoundException -> L3b
            if (r5 == 0) goto L2a
            java.lang.String r5 = "FLAG_MIUI_HAPTIC_VERSION"
            java.lang.reflect.Field r5 = r6.getDeclaredField(r5)     // Catch: java.lang.NoSuchFieldException -> L2c java.lang.NoClassDefFoundError -> L37 java.lang.NoSuchMethodException -> L39 java.lang.ClassNotFoundException -> L3b
            if (r5 == 0) goto L2a
            r5 = 4
            goto L40
        L2a:
            r5 = r4
            goto L40
        L2c:
            r5 = move-exception
            java.lang.String r6 = "error when getting FLAG_MIUI_HAPTIC_VERSION."
            android.util.Log.w(r2, r6, r5)
            int r5 = checkVersion()
            goto L40
        L37:
            r5 = move-exception
            goto L3c
        L39:
            r5 = move-exception
            goto L3c
        L3b:
            r5 = move-exception
        L3c:
            android.util.Log.w(r2, r0, r5)
            r5 = -1
        L40:
            miuix.view.PlatformConstants.VERSION = r5
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r3[r4] = r5
            java.lang.String r5 = "Platform version: %d."
            java.lang.String r3 = java.lang.String.format(r5, r3)
            android.util.Log.i(r2, r3)
            java.lang.Class r1 = java.lang.Class.forName(r1)     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            java.lang.String r3 = "getCurVersion"
            java.lang.Class[] r5 = new java.lang.Class[r4]     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            java.lang.reflect.Method r3 = r1.getDeclaredMethod(r3, r5)     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            java.lang.Object r1 = r3.invoke(r1, r4)     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            java.lang.Double r1 = (java.lang.Double) r1     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            double r3 = r1.doubleValue()     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            miuix.view.PlatformConstants.romHapticVersion = r3     // Catch: java.lang.IllegalAccessException -> L6e java.lang.reflect.InvocationTargetException -> L74 java.lang.NoSuchMethodException -> L7a java.lang.ClassNotFoundException -> L7c
            goto L80
        L6e:
            java.lang.String r0 = "have no access to the definition of getCurVersion()"
            android.util.Log.w(r2, r0)
            goto L80
        L74:
            java.lang.String r0 = "method getCurVersion() called using Reflection failed"
            android.util.Log.w(r2, r0)
            goto L80
        L7a:
            r1 = move-exception
            goto L7d
        L7c:
            r1 = move-exception
        L7d:
            android.util.Log.w(r2, r0, r1)
        L80:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Rom haptic version: "
            r0.append(r1)
            double r3 = miuix.view.PlatformConstants.romHapticVersion
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            android.util.Log.i(r2, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.view.PlatformConstants.<clinit>():void");
    }

    public static int checkVersion() {
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(268435470)) {
            return 4;
        }
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(268435469)) {
            return 3;
        }
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(268435468)) {
            return 2;
        }
        return HapticFeedbackUtil.isSupportLinearMotorVibrate(268435465) ? 1 : 0;
    }
}
