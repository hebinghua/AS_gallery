package com.miui.gallery.permission.cn.legacy;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.permission.core.OnPermissionIntroduced;
import com.miui.gallery.preference.BaseGalleryPreferences;

/* loaded from: classes2.dex */
public class CTAPermissionIntroduction {
    public void introduce(FragmentActivity fragmentActivity, final String str, final OnPermissionIntroduced onPermissionIntroduced) {
        String str2 = "cta_privacy_permission_dialog_" + str;
        DialogFragment dialogFragment = (DialogFragment) fragmentActivity.getSupportFragmentManager().findFragmentByTag(str2);
        if (dialogFragment == null) {
            dialogFragment = CtaPermissionIntroduceDialog.newInstance(str, new OnPermissionIntroduced() { // from class: com.miui.gallery.permission.cn.legacy.CTAPermissionIntroduction.1
                @Override // com.miui.gallery.permission.core.OnPermissionIntroduced
                public void onPermissionIntroduced(boolean z) {
                    BaseGalleryPreferences.PermissionIntroduction.setCtaPrivacyPermissionsAllowed(str, z);
                    onPermissionIntroduced.onPermissionIntroduced(z);
                }
            });
        }
        if (!dialogFragment.isAdded()) {
            dialogFragment.show(fragmentActivity.getSupportFragmentManager(), str2);
        }
    }
}
