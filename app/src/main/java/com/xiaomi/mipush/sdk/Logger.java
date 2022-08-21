package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.push.di;
import com.xiaomi.push.dj;

/* loaded from: classes3.dex */
public class Logger {
    public static boolean sDisablePushLog = false;
    public static LoggerInterface sUserLogger;

    public static LoggerInterface getUserLogger() {
        return sUserLogger;
    }

    public static boolean hasWritePermission(Context context) {
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr != null) {
                for (String str : strArr) {
                    if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(str)) {
                        return true;
                    }
                }
            }
        } catch (Exception unused) {
        }
        return false;
    }

    public static void setLogger(Context context, LoggerInterface loggerInterface) {
        sUserLogger = loggerInterface;
        setPushLog(context);
    }

    public static void setPushLog(Context context) {
        boolean z = true;
        boolean z2 = false;
        boolean z3 = sUserLogger != null;
        if (sDisablePushLog) {
            z = false;
        } else {
            if (!hasWritePermission(context)) {
                z = false;
            }
            z2 = z3;
        }
        dj djVar = null;
        LoggerInterface loggerInterface = z2 ? sUserLogger : null;
        if (z) {
            djVar = dj.a(context);
        }
        com.xiaomi.channel.commonutils.logger.b.a(new di(loggerInterface, djVar));
    }
}
