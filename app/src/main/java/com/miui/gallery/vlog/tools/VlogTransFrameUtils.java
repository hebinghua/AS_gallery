package com.miui.gallery.vlog.tools;

import android.os.Build;

/* loaded from: classes2.dex */
public class VlogTransFrameUtils {
    public static boolean sLoaded;
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"atom", "apricot", "bomb", "banana", "cannon", "cannong", "merlinnfc", "merlin", "merlinin", "lancelot", "galahad", "shiva", "dandelion", "angelica", "cattail", "angelican", "lime", "citrus", "lemon", "pomelo", "curtana", "durandal", "excalibur", "joyeuse", "gram"};
        sWhiteList = strArr;
        sLoaded = false;
        for (String str : strArr) {
            if (Build.DEVICE.equals(str)) {
                sLoaded = true;
                return;
            }
        }
    }

    public static boolean isNeedLowEndFrame() {
        return sLoaded;
    }
}
