package com.miui.gallery.permission.core;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Process;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.internal.util.ReflectUtil;

/* loaded from: classes2.dex */
public class AppOpUtils {
    public static boolean isShortCutEnable(Context context) {
        if (context == null) {
            return false;
        }
        int callCheckOpNoThrow = callCheckOpNoThrow(context, 10017);
        DefaultLogger.d("AppOpUtils", "[Permission] short cut mode %d %s", Integer.valueOf(callCheckOpNoThrow), modeToStr(callCheckOpNoThrow));
        return callCheckOpNoThrow != 1;
    }

    public static boolean isWriteClipBoard(Context context) {
        if (context == null) {
            return false;
        }
        int callCheckOpNoThrow = callCheckOpNoThrow(context, "android:write_clipboard");
        DefaultLogger.d("AppOpUtils", "[Permission] write clipboard mode %d %s", Integer.valueOf(callCheckOpNoThrow), modeToStr(callCheckOpNoThrow));
        return callCheckOpNoThrow != 1;
    }

    public static boolean isStorageAllow(Context context) {
        if (context == null) {
            return false;
        }
        int callCheckOpNoThrow = callCheckOpNoThrow(context, "android:write_external_storage");
        DefaultLogger.d("AppOpUtils", "[Permission] write storage mode %d %s", Integer.valueOf(callCheckOpNoThrow), modeToStr(callCheckOpNoThrow));
        return callCheckOpNoThrow == 0;
    }

    public static int callCheckOpNoThrow(Context context, String str) {
        return ((AppOpsManager) context.getSystemService("appops")).checkOpNoThrow(str, Process.myUid(), context.getPackageName());
    }

    public static int callCheckOpNoThrow(Context context, int i) {
        Class cls = Integer.TYPE;
        Object callObjectMethod = ReflectUtil.callObjectMethod((AppOpsManager) context.getSystemService("appops"), "checkOpNoThrow", new Class[]{cls, cls, String.class}, Integer.valueOf(i), Integer.valueOf(Process.myUid()), context.getPackageName());
        if (callObjectMethod != null) {
            return ((Integer) callObjectMethod).intValue();
        }
        return 0;
    }

    public static String modeToStr(int i) {
        return (String) ReflectUtil.callStaticObjectMethod(AppOpsManager.class, String.class, "modeToName", new Class[]{Integer.TYPE}, Integer.valueOf(i));
    }
}
