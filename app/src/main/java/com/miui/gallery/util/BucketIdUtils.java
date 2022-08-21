package com.miui.gallery.util;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class BucketIdUtils {
    public static final String[] LOAD_ALL_BUCKED_PATHS = {MIUIStorageConstants.DIRECTORY_CAMERA_PATH, MIUIStorageConstants.DIRECTORY_CAMERA_RAW_PATH};

    public static String[] genAllBucketIds(Context context, String str, String str2) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str2)) {
            linkedList.add(str2);
        }
        if (!TextUtils.isEmpty(str) && needLoadAllBucket(str)) {
            List<String> bucketId = getBucketId(context, str);
            if (bucketId == null) {
                DefaultLogger.e("BucketIdUtils", "target bucket is null");
                return null;
            }
            linkedList.addAll(bucketId);
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            return (String[]) linkedList.toArray(new String[0]);
        }
        return null;
    }

    public static List<String> getBucketId(Context context, String str) {
        String[] pathsInExternalStorage = StorageUtils.getPathsInExternalStorage(context, str);
        if (pathsInExternalStorage == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(pathsInExternalStorage.length);
        for (String str2 : pathsInExternalStorage) {
            arrayList.add(String.valueOf(BaseFileUtils.getBucketID(str2)));
        }
        return arrayList;
    }

    public static boolean needLoadAllBucket(String str) {
        for (String str2 : LOAD_ALL_BUCKED_PATHS) {
            if (miuix.core.util.FileUtils.normalizeDirectoryName(str2).equalsIgnoreCase(miuix.core.util.FileUtils.normalizeDirectoryName(str))) {
                return true;
            }
        }
        return false;
    }
}
