package com.miui.gallery.ui.photodetail;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public class PhotoDetailUtils {
    public static String[] SUPPORT_TYPE = {"image/jpeg", "image/png", "image/gif"};

    public static boolean isMimeTypeSupportEditDateTime(String str) {
        for (String str2 : SUPPORT_TYPE) {
            if (TextUtils.equals(str2, str)) {
                return true;
            }
        }
        return false;
    }
}
