package com.miui.gallery.vlog.home;

import android.text.TextUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.tools.FileHelper;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class VlogStorage {
    public static final String IMAGE_SAVED_SUFFIX = "VLOG";
    public static final String SAVE_FILE_FORMAT = "%sMP4_%s.mp4";
    public static final String TEMP_FILE_NAME = "TEMPVLOG.mp4";

    public static String getOutputMediaFilePath() {
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE);
        if (StorageSolutionProvider.get().getDocumentFile(pathInPriorStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("VlogStorage", "getOutputMediaFilePath")) == null) {
            DefaultLogger.e("VlogStorage", "getOutputMediaFile failed");
            return null;
        }
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String str = SAVE_FILE_FORMAT;
        return String.format(str, pathInPriorStorage + File.separator, format + IMAGE_SAVED_SUFFIX);
    }

    public static String getOutputMediaFilePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return getOutputMediaFilePath();
        }
        return FileHelper.generateOutputFilePath(str);
    }

    public static String getTempReverseFilePath() {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/vlogReverseTemp");
        if (StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("VlogStorage", "getTempReverseFilePath")) == null) {
            DefaultLogger.e("VlogStorage", "getTempReverseFilePath failed");
            return null;
        }
        return pathInPrimaryStorage + File.separator;
    }

    public static String getTempFilePath() {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/vlogTemp");
        if (StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("VlogStorage", "getTempFilePath")) == null) {
            DefaultLogger.e("VlogStorage", "getTempFilePath failed");
            return null;
        }
        return pathInPrimaryStorage + File.separator + TEMP_FILE_NAME;
    }
}
