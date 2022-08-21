package com.miui.gallery.ui.photoPage.menufilter.extra;

import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ExtraParams {
    public boolean isPhotoRenameAble;
    public boolean isStartWhenLockedAndSecret;
    public long mOperationMask;

    public boolean isPhotoRenameAble() {
        DefaultLogger.d("PhotoPageFragment_MenuManager_Filter", "isPhotoRenameAble => [%b]", Boolean.valueOf(this.isPhotoRenameAble));
        return this.isPhotoRenameAble;
    }

    public void setPhotoRenameAble(boolean z) {
        this.isPhotoRenameAble = z;
    }

    public void setOperationMask(long j) {
        this.mOperationMask = j;
    }

    public long getOperationMask() {
        DefaultLogger.d("PhotoPageFragment_MenuManager_Filter", "getOperationMask => [%s]", Long.valueOf(this.mOperationMask));
        return this.mOperationMask;
    }

    public void setStartWhenLockedAndSecret(boolean z) {
        this.isStartWhenLockedAndSecret = z;
    }

    public boolean isStartWhenLockedAndSecret() {
        DefaultLogger.d("PhotoPageFragment_MenuManager_Filter", "isStartWhenLockedAndSecret => [%b]", Boolean.valueOf(this.isStartWhenLockedAndSecret));
        return this.isStartWhenLockedAndSecret;
    }
}
