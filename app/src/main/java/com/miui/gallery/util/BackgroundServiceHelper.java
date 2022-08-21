package com.miui.gallery.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class BackgroundServiceHelper {
    public static void startForegroundServiceIfNeed(Context context, Intent intent) {
        String action;
        if (!MiscUtil.isAppProcessInForeground() && Build.VERSION.SDK_INT >= 28) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager == null) {
                return;
            }
            if (activityManager.isBackgroundRestricted()) {
                if (intent.getComponent() != null) {
                    action = intent.getComponent().getClassName();
                } else {
                    action = intent.getAction();
                }
                DefaultLogger.w("BackgroundServiceHelper", "cannot start foreground service [%s] due to bg restriction.", action);
                return;
            }
        }
        ContextCompat.startForegroundService(context, intent);
    }
}
