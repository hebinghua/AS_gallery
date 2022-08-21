package com.miui.gallery.assistant.library;

import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.pendingtask.base.PendingTask;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Deprecated
/* loaded from: classes.dex */
public class DeleteLibraryTask extends PendingTask {
    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public int getNetworkType() {
        return 0;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return false;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireDeviceIdle() {
        return true;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public byte[] wrapData(Object obj) throws Exception {
        return new byte[0];
    }

    public DeleteLibraryTask(int i) {
        super(i);
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    /* renamed from: parseData */
    public Object mo1252parseData(byte[] bArr) throws Exception {
        return new Object();
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean process(Object obj) throws Exception {
        DocumentFile documentFile;
        File[] listFiles;
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("DeleteLibraryTask", "process");
        Long[] lArr = LibraryConstantsHelper.sAllLibraries;
        HashSet hashSet = new HashSet();
        if (lArr != null && lArr.length > 0 && LibraryManager.getInstance().isInitialized()) {
            for (Long l : lArr) {
                Library librarySync = LibraryManager.getInstance().getLibrarySync(l.longValue());
                if (librarySync == null || !BaseMiscUtil.isValid(librarySync.getLibraryItems())) {
                    return false;
                }
                for (LibraryItem libraryItem : librarySync.getLibraryItems()) {
                    hashSet.add(libraryItem.getSha1());
                }
            }
            Set<File> allDirs = LibraryConstantsHelper.getAllDirs();
            if (allDirs != null && allDirs.size() > 0) {
                for (File file : allDirs) {
                    if (file.exists() && file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                        for (File file2 : listFiles) {
                            String sha1 = FileUtils.getSha1(file2.getAbsolutePath());
                            if (!TextUtils.isEmpty(sha1) && !hashSet.contains(sha1) && !isDownloadTempFile(file2)) {
                                DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(file2.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                                DefaultLogger.i("DeleteLibraryTask", "Overdue Library item file (%s) delete success:%b", file2.getName(), Boolean.valueOf(documentFile2 != null && documentFile2.delete()));
                            }
                        }
                    }
                }
            }
            Set<File> allDirsOfParentDir = LibraryConstantsHelper.getAllDirsOfParentDir();
            if (allDirsOfParentDir.size() <= 0) {
                return false;
            }
            for (File file3 : allDirsOfParentDir) {
                if (!allDirs.contains(file3) && (documentFile = StorageSolutionProvider.get().getDocumentFile(file3.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, appendInvokerTag)) != null) {
                    documentFile.delete();
                }
            }
        }
        return false;
    }

    public final boolean isDownloadTempFile(File file) {
        return file != null && file.getName().endsWith(".download");
    }
}
