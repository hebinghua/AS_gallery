package com.miui.privacy;

import android.app.Activity;
import android.security.ChooseLockSettingsHelper;
import com.miui.internal.LockSettingsCompat;

/* loaded from: classes3.dex */
public class MiuiWrapper implements IPrivacyWrapper {
    public ChooseLockSettingsHelper mHelper;

    public MiuiWrapper(Activity activity) {
        this.mHelper = new ChooseLockSettingsHelper(activity, 2);
    }

    @Override // com.miui.privacy.IPrivacyWrapper
    public boolean isPrivacyPasswordEnabled() {
        return this.mHelper.isPrivacyPasswordEnabled();
    }

    @Override // com.miui.privacy.IPrivacyWrapper
    public boolean isPrivateGalleryEnabled() {
        return this.mHelper.isPrivateGalleryEnabled();
    }

    @Override // com.miui.privacy.IPrivacyWrapper
    public void setPrivateGalleryEnabled(boolean z) {
        LockSettingsCompat.setPrivateGalleryEnabled(this.mHelper, z);
    }
}
