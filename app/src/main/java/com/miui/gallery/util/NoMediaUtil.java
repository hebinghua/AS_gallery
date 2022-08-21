package com.miui.gallery.util;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.io.File;

/* loaded from: classes2.dex */
public class NoMediaUtil {
    public static boolean isManualHideAlbum(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return NoMediaRecorder.getInstance().match(str);
    }

    public static boolean tryAddNoMediaForAlbum(String str) {
        boolean addRecord = addRecord(str);
        DefaultLogger.d("NoMediaUtil", "add .nomedia for album %s %b", str, Boolean.valueOf(addRecord));
        return addRecord;
    }

    public static boolean tryRemoveNoMediaForAlbum(String str) {
        boolean removeRecord = removeRecord(str);
        DefaultLogger.d("NoMediaUtil", "remove .nomedia for album %s %b", str, Boolean.valueOf(removeRecord));
        return removeRecord;
    }

    public static boolean addRecord(String str) {
        String[] absolutePath;
        if (str == null || NoMediaRecorder.getInstance().match(str) || (absolutePath = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), StorageUtils.ensureCommonRelativePath(str))) == null || absolutePath.length == 0) {
            return false;
        }
        for (String str2 : absolutePath) {
            if (new File(str2).isDirectory()) {
                if (!addNoMediaFileForFolder(str2)) {
                    return false;
                }
                com.miui.gallery.storage.utils.Utils.notifySystemScanFolder(GalleryApp.sGetAndroidContext(), str2);
            }
        }
        NoMediaRecorder.getInstance().add(str);
        return true;
    }

    public static boolean removeRecord(String str) {
        String[] absolutePath;
        if (str == null || !NoMediaRecorder.getInstance().match(str) || (absolutePath = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), StorageUtils.ensureCommonRelativePath(str))) == null || absolutePath.length == 0) {
            return false;
        }
        boolean z = true;
        for (String str2 : absolutePath) {
            if (new File(str2).exists()) {
                if (!removeNoMediaForFolder(str2)) {
                    z = false;
                } else {
                    com.miui.gallery.storage.utils.Utils.notifySystemScanFolder(GalleryApp.sGetAndroidContext(), str2);
                }
            }
        }
        if (z) {
            NoMediaRecorder.getInstance().remove(str);
        }
        return z;
    }

    public static boolean addNoMediaFileForFolder(String str) {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("NoMediaUtil", "addNoMediaFileForFolder");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, appendInvokerTag);
        if (documentFile == null || !documentFile.exists() || !documentFile.isDirectory()) {
            return false;
        }
        String str2 = str + "/.nomedia";
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        return (documentFile2 != null && documentFile2.exists()) || StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag) != null;
    }

    public static boolean removeNoMediaForFolder(String str) {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("NoMediaUtil", "removeNoMediaForFolder");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, appendInvokerTag);
        if (documentFile == null || !documentFile.exists() || !documentFile.isDirectory()) {
            return false;
        }
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        DocumentFile documentFile2 = storageStrategyManager.getDocumentFile(str + h.g + ".nomedia", IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
        if (documentFile2 != null && documentFile2.exists()) {
            if (!documentFile2.delete()) {
                return false;
            }
            if (Build.VERSION.SDK_INT <= 28) {
                try {
                    GalleryApp.sGetAndroidContext().getContentResolver().call(Uri.parse("content://media"), "unhide", str, (Bundle) null);
                } catch (UnsupportedOperationException unused) {
                }
            }
        }
        return true;
    }
}
