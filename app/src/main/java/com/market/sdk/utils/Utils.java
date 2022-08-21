package com.market.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.market.sdk.Singleton;
import com.xiaomi.mirror.synergy.CallMethod;
import java.io.File;

/* loaded from: classes.dex */
public class Utils {
    public static boolean DEBUG = new File("/sdcard/com.xiaomi.market.sdk/sdk_debug").exists();
    public static Singleton<String> marketPkgName = new Singleton<String>() { // from class: com.market.sdk.utils.Utils.1
        @Override // com.market.sdk.Singleton
        /* renamed from: create  reason: avoid collision after fix types in other method */
        public String mo443create() {
            return (!Client.isMiui() || Client.isInternationalMiui()) ? "" : "com.xiaomi.market";
        }
    };
    public static volatile Singleton<Boolean> isMiMarketExists = new Singleton<Boolean>() { // from class: com.market.sdk.utils.Utils.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.market.sdk.Singleton
        /* renamed from: create */
        public Boolean mo443create() {
            String str = (String) Utils.marketPkgName.get();
            if (TextUtils.isEmpty(str)) {
                return Boolean.FALSE;
            }
            try {
                int applicationEnabledSetting = AppGlobal.getContext().getPackageManager().getApplicationEnabledSetting(str);
                boolean z = true;
                if (applicationEnabledSetting != 0 && applicationEnabledSetting != 1) {
                    z = false;
                }
                return Boolean.valueOf(z);
            } catch (Exception unused) {
                return Boolean.FALSE;
            }
        }
    };

    public static String getMarketPackageName() {
        return marketPkgName.get();
    }

    public static boolean isMiuiMarketExisted(Context context) {
        return isMiMarketExists.get().booleanValue();
    }

    public static boolean isMiuiPad() {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return ((String) cls.getDeclaredMethod(CallMethod.METHOD_GET, String.class).invoke(cls, "ro.build.characteristics")).contains("tablet");
        } catch (Exception e) {
            android.util.Log.e("MarketSdkUtils", e.getMessage(), e);
            return false;
        }
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo activeNetworkInfo;
        return (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null || activeNetworkInfo.getType() != 1) ? false : true;
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
