package com.miui.gallery.util;

import android.util.Base64;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.util.FileSize;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class ThumbnailUtil {
    public static LockEngine sLockEngine = new LockEngine(30000);
    public static AtomicBoolean sCleanUpIsRunning = new AtomicBoolean(false);

    public static void cleanup() {
        if (sCleanUpIsRunning.get()) {
            DefaultLogger.d("ThumbnailUtil", "another clean up job is running.");
            return;
        }
        try {
            sCleanUpIsRunning.set(true);
            doCleanup();
        } finally {
            sCleanUpIsRunning.set(false);
        }
    }

    public static File getLocalThumbnailFolder() {
        File file = new File(StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/localThumbnailFile"));
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ThumbnailUtil", "getLocalThumbnailFolder");
        if (!file.exists()) {
            StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
        }
        return file;
    }

    public static File getOriginalFile(String str) {
        String str2 = new String(Base64.decode(BaseFileUtils.getFileTitle(str), 8));
        int lastIndexOf = str2.lastIndexOf("_L");
        if (lastIndexOf > 0) {
            int lastIndexOf2 = str2.lastIndexOf("_V");
            if (lastIndexOf2 <= 0) {
                lastIndexOf2 = lastIndexOf;
            }
            String substring = str2.substring(0, lastIndexOf2);
            long parseLong = Long.parseLong(str2.substring(lastIndexOf + 2));
            File file = new File(substring);
            if (file.exists() && file.length() == parseLong) {
                return file;
            }
            return null;
        }
        return null;
    }

    public static boolean isThumbnailValid(DocumentFile documentFile) {
        return System.currentTimeMillis() - documentFile.lastModified() < 2592000000L;
    }

    public static void doCleanup() {
        List<File> listThumbnailsByLastModified = listThumbnailsByLastModified(getLocalThumbnailFolder());
        if (!BaseMiscUtil.isValid(listThumbnailsByLastModified)) {
            return;
        }
        long j = 0;
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ThumbnailUtil", "doCleanup");
        for (File file : listThumbnailsByLastModified) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile != null) {
                if (j > FileSize.GB_COEFFICIENT || getOriginalFile(file.getName()) == null || !isThumbnailValid(documentFile)) {
                    DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile2 != null) {
                        documentFile2.delete();
                    }
                } else {
                    j += file.length();
                }
            }
        }
        DefaultLogger.d("ThumbnailUtil", "clean up local thumbnails.");
    }

    public static List<File> listThumbnailsByLastModified(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles == null) {
            return Collections.emptyList();
        }
        List<File> asList = Arrays.asList(listFiles);
        Collections.sort(asList, new Comparator<File>() { // from class: com.miui.gallery.util.ThumbnailUtil.1
            @Override // java.util.Comparator
            public int compare(File file2, File file3) {
                return -Long.compare(file2.lastModified(), file3.lastModified());
            }
        });
        return asList;
    }

    /* loaded from: classes2.dex */
    public static class LockEngine {
        public long mKeepAliveTime;
        public long mNextPollTime = 0;
        public final Map<String, Object> mLocks = new WeakHashMap();

        public LockEngine(long j) {
            this.mKeepAliveTime = -1L;
            this.mKeepAliveTime = j;
        }
    }
}
