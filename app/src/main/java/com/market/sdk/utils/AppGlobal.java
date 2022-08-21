package com.market.sdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import com.market.sdk.utils.PrefUtils;

/* loaded from: classes.dex */
public class AppGlobal {
    public static Context sContext;

    public static Context getContext() {
        Context context = sContext;
        if (context != null) {
            return context;
        }
        throw new IllegalArgumentException("sContext is null,should call setContext first!!");
    }

    public static void setContext(Context context) {
        sContext = context.getApplicationContext();
        init();
    }

    public static void init() {
        if (PrefUtils.getLong("sdkBeginTime", new PrefUtils.PrefFile[0]) == 0) {
            PrefUtils.setLong("sdkBeginTime", System.currentTimeMillis(), new PrefUtils.PrefFile[0]);
        }
    }

    public static PackageManager getPackageManager() {
        return sContext.getPackageManager();
    }
}
