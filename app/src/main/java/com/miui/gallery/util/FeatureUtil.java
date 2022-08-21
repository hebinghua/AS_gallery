package com.miui.gallery.util;

import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import miui.os.Build;

/* loaded from: classes2.dex */
public class FeatureUtil extends BaseFeatureUtil {
    public static boolean isSupportPublicShareAlbum() {
        return false;
    }

    public static boolean isSupportBackupOnlyWifi() {
        String str = Build.DEVICE;
        if ("elish".equalsIgnoreCase(str) || "nabu".equalsIgnoreCase(str)) {
            return false;
        }
        return CloudControlStrategyHelper.getSyncStrategy().isSupportBackupOnlyWifi();
    }

    public static boolean isReplaceAssistantPageRecommend() {
        return FilterSkyEntranceUtils.showSingleFilterSky();
    }
}
