package com.milink.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes.dex */
public class ApiUtils {
    public static int getVersionCode(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            Log.e("ML::ApiUtils", "getVersionCode error, package is null");
            return -1;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            if (packageInfo != null) {
                return packageInfo.versionCode;
            }
        } catch (Exception e) {
            Log.e("ML::ApiUtils", "catch getVersionInfo error: " + e.getMessage());
        }
        return -1;
    }
}
