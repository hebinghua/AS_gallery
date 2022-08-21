package com.miui.gallery.storage.constants.android;

import com.miui.gallery.storage.constants.android.cn.CNAndroidStorageConstantsProvider;
import com.miui.gallery.storage.constants.android.global.InternationalAndroidStorageConstantsProvider;
import miuix.os.Build;

/* loaded from: classes2.dex */
public class AndroidStorageConstantsProvider implements IAndroidStorageConstantsProvider {
    public static final IAndroidStorageConstantsProvider INNER_PROVIDER;

    static {
        IAndroidStorageConstantsProvider cNAndroidStorageConstantsProvider;
        if (Build.IS_INTERNATIONAL_BUILD) {
            cNAndroidStorageConstantsProvider = new InternationalAndroidStorageConstantsProvider();
        } else {
            cNAndroidStorageConstantsProvider = new CNAndroidStorageConstantsProvider();
        }
        INNER_PROVIDER = cNAndroidStorageConstantsProvider;
    }

    @Override // com.miui.gallery.storage.constants.android.IAndroidStorageConstantsProvider
    public IAndroidStorageConstants getAndroidStorageConstants() {
        return INNER_PROVIDER.getAndroidStorageConstants();
    }
}
