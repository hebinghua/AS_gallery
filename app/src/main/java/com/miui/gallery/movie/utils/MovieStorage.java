package com.miui.gallery.movie.utils;

import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class MovieStorage {
    public static final String IMAGE_SAVED_SUFFIX = "_PHOTOMOVIE";
    public static final String SAVE_FILE_FORMAT = "%sMP4_%s.mp4";
    public static final String TEMP_FILE_NAME = "TEMP_PHOTOMOVIE.mp4";

    public static String getOutputMediaFilePath() {
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE);
        if (pathInPriorStorage == null) {
            return null;
        }
        if (StorageSolutionProvider.get().getDocumentFile(pathInPriorStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("MovieStorage", "getOutputMediaFilePath")) == null) {
            DefaultLogger.e("MovieStorage", "getOutputMediaFile failed");
            return null;
        }
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String str = SAVE_FILE_FORMAT;
        return String.format(str, pathInPriorStorage + File.separator, format + IMAGE_SAVED_SUFFIX);
    }

    public static String getTempFilePath() {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/movieTemp");
        if (StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("MovieStorage", "getTempFilePath")) == null) {
            DefaultLogger.e("MovieStorage", "getTempFilePath failed");
            return null;
        }
        return pathInPrimaryStorage + File.separator + TEMP_FILE_NAME;
    }
}
