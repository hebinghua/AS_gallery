package com.miui.gallery.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import com.android.internal.SystemPropertiesCompat;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.FeatureHelper;
import com.miui.os.Rom;
import com.miui.settings.Settings;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import miuix.animation.utils.DeviceUtils;

/* loaded from: classes2.dex */
public class BaseBuildUtil {
    public static int BIG_HORIZONTAL_WINDOW_STANDARD;
    public static int BIG_SCREEN_DEVICE_STANDARD;
    public static final Set<String> FOLD_ABLE_DEVICE;
    public static final boolean IS_DEBUG_BUILD;
    public static final LazyValue<Void, Boolean> IS_LOW_RAM_DEVICE;
    public static final LazyValue<Void, String> ROM_BUILD_REGION;
    public static Boolean sIsNewDevice;

    static {
        String str = Build.TYPE;
        IS_DEBUG_BUILD = str.equals("eng") || str.equals("userdebug");
        BIG_SCREEN_DEVICE_STANDARD = 711;
        BIG_HORIZONTAL_WINDOW_STANDARD = 637;
        ROM_BUILD_REGION = new LazyValue<Void, String>() { // from class: com.miui.gallery.util.BaseBuildUtil.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public String mo1272onInit(Void r2) {
                return SystemPropertiesCompat.get("ro.miui.build.region", "cn");
            }
        };
        IS_LOW_RAM_DEVICE = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.BaseBuildUtil.2
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public Boolean mo1272onInit(Void r7) {
                ActivityManager activityManager = (ActivityManager) StaticContext.sGetAndroidContext().getSystemService("activity");
                if (activityManager.isLowRamDevice()) {
                    DefaultLogger.d("BaseBuildUtil", "ActivityManager#isLowRamDevice");
                    return Boolean.TRUE;
                }
                ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                activityManager.getMemoryInfo(memoryInfo);
                long j = memoryInfo.totalMem;
                boolean z = j <= 4294967296L;
                if (z) {
                    DefaultLogger.d("BaseBuildUtil", "LOW_MEMORY_THRESHOLD: %s", Long.valueOf(j));
                }
                return Boolean.valueOf(z);
            }
        };
        FOLD_ABLE_DEVICE = new HashSet(Arrays.asList("cetus", "zizhan"));
    }

    public static boolean isInternational() {
        return Rom.IS_INTERNATIONAL;
    }

    public static String getRegion() {
        return Settings.getRegion();
    }

    public static boolean isLowRamDevice() {
        return IS_LOW_RAM_DEVICE.get(null).booleanValue();
    }

    public static String getRomBuildRegion() {
        return ROM_BUILD_REGION.get(null);
    }

    public static boolean isRomBuildRegionTW() {
        return "tw".equals(getRomBuildRegion());
    }

    public static boolean isBlackShark() {
        return FeatureHelper.isBlackShark();
    }

    public static boolean isLargeScreenDevice() {
        return getScreenWidthInDip() >= BIG_SCREEN_DEVICE_STANDARD;
    }

    public static boolean isLargeScreen(Context context) {
        return getCurScreenWidthInDip() >= 679 && getCurScreenHeightInDip() >= 600;
    }

    public static boolean isLargeScreenIndependentOrientation() {
        return getCurScreenWidthInDip() >= 629 && getCurScreenHeightInDip() >= 629;
    }

    public static boolean isLargeHorizontalWindow() {
        return ScreenUtils.getScreenHorizontalInDp() >= BIG_HORIZONTAL_WINDOW_STANDARD;
    }

    public static boolean isLargerHorizontalWidthDevice() {
        return getCurScreenWidthInDip() >= 920 && getCurScreenHeightInDip() <= 305;
    }

    public static int getScreenWidthInDip() {
        return BaseConfig$ScreenConfig.getScreenWidthInDip();
    }

    public static int getCurScreenWidthInDip() {
        return ScreenUtils.getScreenHorizontalInDp();
    }

    public static int getCurScreenHeightInDip() {
        return ScreenUtils.getScreenVerticalInDp();
    }

    public static boolean isFoldableDevice() {
        return FOLD_ABLE_DEVICE.contains(Build.DEVICE);
    }

    public static boolean isNewDevice() {
        if (sIsNewDevice == null) {
            sIsNewDevice = Boolean.valueOf(!DeviceUtils.isStockDevice());
        }
        return sIsNewDevice.booleanValue();
    }

    public static boolean isPad() {
        return FeatureHelper.isPad();
    }

    public static boolean isAboveAndroidS() {
        return Build.VERSION.SDK_INT >= 31;
    }
}
