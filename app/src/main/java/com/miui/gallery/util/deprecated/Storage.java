package com.miui.gallery.util.deprecated;

import android.os.Environment;
import com.android.internal.storage.StorageInfo;
import com.android.internal.storage.StorageManager;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StorageUtils;

/* loaded from: classes2.dex */
public class Storage {
    public static String getPrimaryStorageRoot() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static String getExternalSDCardRoot() {
        for (StorageInfo storageInfo : StorageManager.getInstance().getStorageInfos(StaticContext.sGetAndroidContext())) {
            if (storageInfo.isMounted() && storageInfo.isSd()) {
                return storageInfo.getPath();
            }
        }
        return null;
    }

    @Deprecated
    public static String[] getAllSdCardAvatarFilePath() {
        return StorageUtils.getPathsInExternalStorage(StaticContext.sGetAndroidContext(), "/MIUI/Gallery/cloud/.avatar");
    }

    @Deprecated
    public static String[] getCloudThumbnailFilePath() {
        return StorageUtils.getPathsInExternalStorage(StaticContext.sGetAndroidContext(), "/MIUI/Gallery/cloud/.thumbnailFile");
    }

    public static String[] getCloudWaitUploadFilePath() {
        return StorageUtils.getPathsInExternalStorage(StaticContext.sGetAndroidContext(), "/MIUI/Gallery/cloud/.waitUpload");
    }

    @Deprecated
    public static String getMainSDCardCloudThumbnailFilePath() {
        return StorageUtils.getPathInPriorStorage("/MIUI/Gallery/cloud/.thumbnailFile");
    }

    public static boolean startsWithFilePath(String[] strArr, String str) {
        String lowerCase = str.toLowerCase();
        for (String str2 : strArr) {
            if (lowerCase.startsWith(str2.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
