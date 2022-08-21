package com.miui.keyguard;

import android.content.ContentResolver;
import android.provider.Settings;
import com.miui.core.SdkHelper;

/* loaded from: classes3.dex */
public class LockScreenHelper {
    public static String getLockWallpaperProvider(ContentResolver contentResolver) {
        if (SdkHelper.IS_MIUI) {
            return Settings.System.getString(contentResolver, "lock_wallpaper_provider_authority");
        }
        return null;
    }

    public static void setLockWallpaperProvider(ContentResolver contentResolver, String str) {
        if (SdkHelper.IS_MIUI) {
            Settings.System.putString(contentResolver, "lock_wallpaper_provider_authority", str);
        }
    }
}
