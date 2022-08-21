package com.miui.privacy;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import com.miui.core.SdkHelper;

/* loaded from: classes3.dex */
public class LockSettingsHelper {
    public static final IPrivacyHelper IMPL;
    public final IPrivacyWrapper mImpl;

    static {
        IMPL = SdkHelper.IS_MIUI ? new MiuiHelper() : new XmsHelper();
    }

    public static void startSetPrivacyPasswordActivity(Fragment fragment, int i) {
        IMPL.startSetPrivacyPasswordActivity(fragment, i);
    }

    public static void startAuthenticatePasswordActivity(Fragment fragment, int i) {
        IMPL.startAuthenticatePasswordActivity(fragment, i);
    }

    public static void startAuthenticatePasswordActivity(Activity activity, int i) {
        IMPL.startAuthenticatePasswordActivity(activity, i);
    }

    public static void confirmPrivateGalleryPassword(Fragment fragment, int i) {
        IMPL.confirmPrivateGalleryPassword(fragment, i);
    }

    public LockSettingsHelper(Activity activity) {
        this.mImpl = SdkHelper.IS_MIUI ? new MiuiWrapper(activity) : new XmsWrapper(activity);
    }

    public boolean isPrivacyPasswordEnabled() {
        return this.mImpl.isPrivacyPasswordEnabled();
    }

    public boolean isPrivateGalleryEnabled() {
        return this.mImpl.isPrivateGalleryEnabled();
    }

    public void setPrivateGalleryEnabled(boolean z) {
        this.mImpl.setPrivateGalleryEnabled(z);
    }
}
