package com.miui.gallery.util;

import android.content.Context;
import com.android.internal.SystemPropertiesCompat;
import com.mi.mibridge.DeviceLevel;
import com.miui.os.FeatureHelper;
import com.miui.settings.Settings;
import java.io.File;

/* loaded from: classes2.dex */
public class BuildUtil extends BaseBuildUtil {
    public static final LazyValue<Void, Integer> MIUI_VERSION_CODE = new LazyValue<Void, Integer>() { // from class: com.miui.gallery.util.BuildUtil.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Integer mo1272onInit(Void r2) {
            return Integer.valueOf(SystemPropertiesCompat.getInt("ro.miui.ui.version.code", 0));
        }
    };
    public static Boolean sIsFoldingDevice;

    public static String getDeviceName(Context context) {
        return Settings.getDeviceName(context);
    }

    public static boolean isDefaultLockStyle() {
        return !new File("/data/system/theme//lockscreen").exists();
    }

    public static boolean isXiaoMi() {
        return FeatureHelper.isXiaoMi();
    }

    public static boolean isBlackShark() {
        return FeatureHelper.isBlackShark();
    }

    public static boolean isMiui10() {
        return MIUI_VERSION_CODE.get(null).intValue() <= 8;
    }

    public static boolean isMiui11(Context context) {
        return MIUI_VERSION_CODE.get(null).intValue() == 9 && !isMiui10();
    }

    public static boolean isMiui12() {
        return MIUI_VERSION_CODE.get(null).intValue() >= 10;
    }

    public static boolean isMiui13() {
        return MIUI_VERSION_CODE.get(null).intValue() >= 13;
    }

    public static boolean isPcMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & 8192) != 0;
    }

    public static boolean isFoldingDevice() {
        if (sIsFoldingDevice == null) {
            boolean z = false;
            if (SystemPropertiesCompat.getInt("persist.sys.muiltdisplay_type", 0) == 2) {
                z = true;
            }
            sIsFoldingDevice = Boolean.valueOf(z);
        }
        return sIsFoldingDevice.booleanValue();
    }

    public static boolean isMiuiLite() {
        return DeviceLevel.IS_MIUI_LITE_VERSION;
    }

    public static boolean isEditorProcess() {
        return "com.miui.gallery:photo_editor".equals(ProcessUtils.currentProcessName());
    }
}
