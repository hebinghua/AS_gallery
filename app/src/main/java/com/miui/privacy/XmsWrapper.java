package com.miui.privacy;

import android.content.Context;
import android.provider.Settings;

/* loaded from: classes3.dex */
public class XmsWrapper implements IPrivacyWrapper {
    public Context mContext;

    @Override // com.miui.privacy.IPrivacyWrapper
    public boolean isPrivateGalleryEnabled() {
        return false;
    }

    @Override // com.miui.privacy.IPrivacyWrapper
    public void setPrivateGalleryEnabled(boolean z) {
    }

    public XmsWrapper(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.privacy.IPrivacyWrapper
    public boolean isPrivacyPasswordEnabled() {
        return 1 == Settings.Secure.getInt(this.mContext.getContentResolver(), "privacy_password_is_open", 0);
    }
}
