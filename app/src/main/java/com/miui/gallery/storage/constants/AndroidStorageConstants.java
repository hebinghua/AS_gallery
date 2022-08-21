package com.miui.gallery.storage.constants;

import com.miui.gallery.storage.constants.android.AndroidStorageConstantsProvider;
import com.miui.gallery.storage.constants.android.IAndroidStorageConstants;

/* loaded from: classes2.dex */
public class AndroidStorageConstants {
    public static final IAndroidStorageConstants ANDROID_STORAGE_CONSTANTS;
    public static final String PACKAGE_NAME_MEDIA_STORE;
    public static final String[] STANDARD_DIRECTORIES;

    static {
        IAndroidStorageConstants androidStorageConstants = new AndroidStorageConstantsProvider().getAndroidStorageConstants();
        ANDROID_STORAGE_CONSTANTS = androidStorageConstants;
        PACKAGE_NAME_MEDIA_STORE = androidStorageConstants.getMediaStorePackageName();
        STANDARD_DIRECTORIES = androidStorageConstants.getStandardDirectories();
    }
}
