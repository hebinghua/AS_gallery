package com.miui.settings;

import android.content.Context;
import android.os.Build;
import android.provider.SystemSettings;
import android.text.TextUtils;
import com.android.internal.SystemPropertiesCompat;
import com.miui.core.SdkHelper;
import com.nexstreaming.nexeditorsdk.BuildConfig;
import java.util.Locale;

/* loaded from: classes3.dex */
public class Settings {
    public static String getDeviceName(Context context) {
        if (SdkHelper.IS_MIUI) {
            String deviceName = SystemSettings.System.getDeviceName(context);
            return TextUtils.isEmpty(deviceName) ? BuildConfig.KM_PROJECT : deviceName;
        }
        return String.format(Locale.US, "%s %s", Build.MANUFACTURER, Build.MODEL);
    }

    public static String getRegion() {
        return SystemPropertiesCompat.get("ro.miui.region", "CN");
    }

    public static boolean checkRegion(String str) {
        return getRegion().equalsIgnoreCase(str);
    }
}
