package com.miui.gallery.util;

import android.os.Build;
import com.miui.os.Rom;

/* loaded from: classes2.dex */
public class BaseFeatureUtil {
    public static final String[] sDisableFastBlurList = {"tucana"};
    public static final String[] sDisableFastDisplayRawList = {"phoenix", "phoenixin", "curtana", "sunny", "mojito", "rainbow"};

    public static boolean isDisableFastBlur() {
        for (String str : sDisableFastBlurList) {
            if (str.equals(Build.DEVICE)) {
                return !Rom.IS_STABLE;
            }
        }
        return false;
    }

    public static boolean isDisableFastDisplayRaw() {
        for (String str : sDisableFastDisplayRawList) {
            if (str.equals(Build.DEVICE)) {
                return true;
            }
        }
        return false;
    }
}
