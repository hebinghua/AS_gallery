package com.miui.os;

import miui.util.FeatureParser;

/* loaded from: classes3.dex */
public class FeatureHelper {
    public static boolean isPad() {
        return FeatureParser.getBoolean("is_pad", false);
    }

    public static boolean isXiaoMi() {
        return FeatureParser.getBoolean("is_xiaomi", false);
    }

    public static boolean isBlackShark() {
        return FeatureParser.getBoolean("is_blackshark", false);
    }
}
