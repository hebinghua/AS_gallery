package com.miui.gallery.permission.cn.legacy;

import android.text.TextUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;

@Deprecated
/* loaded from: classes2.dex */
public class CtaPermissions {
    public static final String[] CTA_PRIVACY_PERMISSIONS = {"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.CAMERA", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.SEND_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_SMS", "android.permission.RECORD_AUDIO", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG"};

    public static boolean isPrivacyAllowed(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        for (String str2 : CTA_PRIVACY_PERMISSIONS) {
            if (TextUtils.equals(str, str2)) {
                return BaseGalleryPreferences.PermissionIntroduction.isCtaPrivacyPermissionsAllowed(str);
            }
        }
        return true;
    }
}
