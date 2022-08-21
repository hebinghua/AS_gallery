package com.miui.privacy;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;

/* loaded from: classes3.dex */
public class MiuiHelper implements IPrivacyHelper {
    @Override // com.miui.privacy.IPrivacyHelper
    public void startSetPrivacyPasswordActivity(Fragment fragment, int i) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.PrivacyPasswordChooseLockPattern");
        intent.setAction("android.app.action.PRIVACY_PASSWORD_SET_NEW_PASSWORD");
        intent.putExtra("android.intent.extra.shortcut.NAME", fragment.getActivity().getPackageName());
        fragment.startActivityForResult(intent, i);
    }

    @Override // com.miui.privacy.IPrivacyHelper
    public void startAuthenticatePasswordActivity(Fragment fragment, int i) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.PrivacyPasswordConfirmLockPattern");
        intent.setAction("android.app.action.PRIVACY_PASSWORD_CONFIRM_PASSWORD");
        intent.putExtra("android.intent.extra.shortcut.NAME", fragment.getActivity().getPackageName());
        fragment.startActivityForResult(intent, i);
    }

    @Override // com.miui.privacy.IPrivacyHelper
    public void confirmPrivateGalleryPassword(Fragment fragment, int i) {
        fragment.startActivityForResult(new Intent("android.app.action.CONFIRM_GALLERY_PASSWORD"), i);
    }

    @Override // com.miui.privacy.IPrivacyHelper
    public void startAuthenticatePasswordActivity(Activity activity, int i) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.PrivacyPasswordConfirmLockPattern");
        intent.setAction("android.app.action.PRIVACY_PASSWORD_CONFIRM_PASSWORD");
        intent.putExtra("android.intent.extra.shortcut.NAME", activity.getPackageName());
        activity.startActivityForResult(intent, i);
    }
}
