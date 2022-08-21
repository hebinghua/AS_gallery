package com.miui.mediaeditor.utils;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes3.dex */
public class FilePermissionUtils {
    public static boolean checkFileDeletePermission(FragmentActivity fragmentActivity, String str) {
        IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
        DefaultLogger.d("FilePermissionUtils_", "checkFileDeletePermission: path = %s, result.granted = %b, result.applicable = %b", str, Boolean.valueOf(checkPermission.granted), Boolean.valueOf(checkPermission.applicable));
        if (!checkPermission.granted) {
            if (checkPermission.applicable) {
                StorageSolutionProvider.get().requestPermission(fragmentActivity, checkPermission.path, checkPermission.type);
            }
            return false;
        }
        return true;
    }

    public static boolean checkFileCreatePermission(FragmentActivity fragmentActivity, String str) {
        IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.INSERT);
        DefaultLogger.d("FilePermissionUtils_", "checkFileCreatePermission: path = %s, result.granted = %b, result.applicable = %b", str, Boolean.valueOf(checkPermission.granted), Boolean.valueOf(checkPermission.applicable));
        if (!checkPermission.granted) {
            if (checkPermission.applicable) {
                StorageSolutionProvider.get().requestPermission(fragmentActivity, checkPermission.path, checkPermission.type);
            }
            return false;
        }
        return true;
    }

    public static boolean checkFileCreatePermissions(FragmentActivity fragmentActivity, String... strArr) {
        for (String str : strArr) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.INSERT);
            DefaultLogger.d("FilePermissionUtils_", "checkFilePermission: path = %s, result.granted = %b, result.applicable = %b", str, Boolean.valueOf(checkPermission.granted), Boolean.valueOf(checkPermission.applicable));
            if (!checkPermission.granted) {
                if (checkPermission.applicable) {
                    StorageSolutionProvider.get().requestPermission(fragmentActivity, checkPermission.path, checkPermission.type);
                }
                return false;
            }
        }
        return true;
    }
}
