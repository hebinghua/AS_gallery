package com.miui.gallery.permission;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.permission.cn.legacy.CTAPermissionIntroduction;
import com.miui.gallery.permission.cn.legacy.CtaPermissions;
import com.miui.gallery.permission.core.OnPermissionIntroduced;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes2.dex */
public class PermissionIntroductionUtils {
    public static void showPermissionIntroduction(FragmentActivity fragmentActivity, String str, OnPermissionIntroduced onPermissionIntroduced) {
        if (!BaseBuildUtil.isInternational()) {
            if (CtaPermissions.isPrivacyAllowed(str)) {
                onPermissionIntroduced.onPermissionIntroduced(true);
                return;
            } else if (!BaseGalleryPreferences.PermissionIntroduction.containCtaPrivacyPermission(str) && PermissionUtils.CUSTOM_REQUEST_PERMISSION.get(fragmentActivity).booleanValue()) {
                BaseGalleryPreferences.PermissionIntroduction.setCtaPrivacyPermissionsAllowed(str, true);
                onPermissionIntroduced.onPermissionIntroduced(true);
                return;
            } else {
                new CTAPermissionIntroduction().introduce(fragmentActivity, str, onPermissionIntroduced);
                return;
            }
        }
        onPermissionIntroduced.onPermissionIntroduced(true);
    }

    public static boolean isAlreadyGetExternalStoragePermission(Context context) {
        if (!BaseBuildUtil.isInternational()) {
            return CtaPermissions.isPrivacyAllowed("android.permission.WRITE_EXTERNAL_STORAGE") && PermissionUtils.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        } else if (!AgreementsUtils.isKoreaRegion()) {
            return PermissionUtils.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        } else {
            return BaseGalleryPreferences.PermissionIntroduction.isRuntimePermissionsIntroduced() && PermissionUtils.checkPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }
}
