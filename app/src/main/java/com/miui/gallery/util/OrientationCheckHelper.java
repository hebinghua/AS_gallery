package com.miui.gallery.util;

import android.os.Build;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class OrientationCheckHelper {
    public static final String[] sWhiteList = {"enuma", "elish", "nabu", "cetus"};
    public static final LazyValue<Void, Boolean> SUPPORT_VALUE = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.OrientationCheckHelper.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(OrientationCheckHelper.isSupport());
        }
    };

    public static boolean isSupport() {
        String str = Build.DEVICE;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String str2 : sWhiteList) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSupportOrientationChange() {
        return SUPPORT_VALUE.get(null).booleanValue();
    }
}
