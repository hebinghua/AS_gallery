package com.miui.gallery.storage.constants.android.cn;

import android.os.Environment;
import com.miui.gallery.storage.constants.android.IAndroidStorageConstants;

/* loaded from: classes2.dex */
public class AndroidSStorageConstants implements IAndroidStorageConstants {
    @Override // com.miui.gallery.storage.constants.android.IAndroidStorageConstants
    public String getMediaStorePackageName() {
        return "com.android.providers.media.module";
    }

    @Override // com.miui.gallery.storage.constants.android.IAndroidStorageConstants
    public String[] getStandardDirectories() {
        return new String[]{Environment.DIRECTORY_MUSIC, Environment.DIRECTORY_PODCASTS, Environment.DIRECTORY_RINGTONES, Environment.DIRECTORY_ALARMS, Environment.DIRECTORY_NOTIFICATIONS, Environment.DIRECTORY_PICTURES, Environment.DIRECTORY_MOVIES, Environment.DIRECTORY_DOWNLOADS, Environment.DIRECTORY_DCIM, Environment.DIRECTORY_DOCUMENTS, Environment.DIRECTORY_AUDIOBOOKS};
    }
}
