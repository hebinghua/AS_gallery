package com.miui.gallery.storage.constants.android.global;

import android.os.Build;
import com.miui.gallery.storage.constants.android.IAndroidStorageConstants;
import com.miui.gallery.storage.constants.android.IAndroidStorageConstantsProvider;

/* loaded from: classes2.dex */
public class InternationalAndroidStorageConstantsProvider implements IAndroidStorageConstantsProvider {
    @Override // com.miui.gallery.storage.constants.android.IAndroidStorageConstantsProvider
    public IAndroidStorageConstants getAndroidStorageConstants() {
        switch (Build.VERSION.SDK_INT) {
            case 26:
                return new AndroidOStorageConstants();
            case 27:
                return new AndroidOMR1StorageConstants();
            case 28:
                return new AndroidPStorageConstants();
            case 29:
                return new AndroidQStorageConstants();
            case 30:
                return new AndroidRStorageConstants();
            default:
                return new AndroidSStorageConstants();
        }
    }
}
