package com.market.sdk.utils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PkgUtils {
    public static boolean isPackageEnabled(String str) {
        try {
            PackageManager packageManager = AppGlobal.getPackageManager();
            int applicationEnabledSetting = packageManager.getApplicationEnabledSetting(str);
            Log.d("PkgUtils", "state: " + applicationEnabledSetting);
            if (applicationEnabledSetting == 0) {
                return packageManager.getApplicationInfo(str, 0).enabled;
            }
            return applicationEnabledSetting == 1;
        } catch (IllegalArgumentException unused) {
            return false;
        } catch (Exception e) {
            Log.e("PkgUtils", e.toString(), e);
            return false;
        }
    }

    public static String queryDefaultPackageForIntent(Intent intent) {
        for (ResolveInfo resolveInfo : queryIntentActivities(intent, 0)) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo.enabled && activityInfo.exported) {
                return activityInfo.packageName;
            }
        }
        return null;
    }

    public static List<ResolveInfo> queryIntentActivities(Intent intent, int i) {
        List<ResolveInfo> list;
        try {
            list = AppGlobal.getPackageManager().queryIntentActivities(intent, i);
        } catch (Exception e) {
            Log.e("PkgUtils", e.getMessage(), e);
            list = null;
        }
        return list != null ? list : new ArrayList();
    }
}
