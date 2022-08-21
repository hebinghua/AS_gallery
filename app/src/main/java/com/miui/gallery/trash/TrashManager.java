package com.miui.gallery.trash;

import android.content.ContentValues;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.dao.base.EntityTransaction;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Encode;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import miuix.core.util.FileUtils;

/* loaded from: classes2.dex */
public class TrashManager {
    public static final String[] TRASH_ITEM_ALBUM_PROJECTION_INFO = {"localPath", "name", "attributes"};

    public TrashManager() {
    }

    /* loaded from: classes2.dex */
    public static final class TrashManagerHolder {
        public static final TrashManager S_INSTANCE = new TrashManager();
    }

    public static TrashManager getInstance() {
        return TrashManagerHolder.S_INSTANCE;
    }

    public synchronized void addTrashBinItem(TrashBinItem trashBinItem) {
        if (trashBinItem == null) {
            return;
        }
        if (!canContinue(trashBinItem)) {
            return;
        }
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        EntityTransaction transaction = galleryEntityManager.getTransaction();
        transaction.begin();
        if (getItemByServerId(trashBinItem.getCloudServerId()) == null) {
            if (galleryEntityManager.insert(trashBinItem) > 1) {
                DefaultLogger.d("galleryAction_TrashManager", "addTrashBinItem success!");
            }
        }
        transaction.commit();
        transaction.end();
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, null);
    }

    public final boolean canContinue(TrashBinItem trashBinItem) {
        return trashBinItem.getAlbumAttributes() == 0 || !CloudUtils.isRubbishAlbum(trashBinItem.getAlbumAttributes());
    }

    public TrashBinItem getItemByServerId(String str) {
        if (!TextUtils.isEmpty(str)) {
            List query = GalleryEntityManager.getInstance().query(TrashBinItem.class, String.format("cloudServerId = %s", str), null, null, null);
            if (!BaseMiscUtil.isValid(query)) {
                return null;
            }
            return (TrashBinItem) query.get(0);
        }
        return null;
    }

    public synchronized void updateTrashBinItem(ContentValues contentValues, String str, String[] strArr) {
        if (contentValues == null) {
            return;
        }
        GalleryEntityManager.getInstance().update(TrashBinItem.class, contentValues, str, strArr);
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, null);
    }

    public synchronized void updateTrashBinItem(TrashBinItem trashBinItem) {
        if (trashBinItem == null) {
            return;
        }
        GalleryEntityManager.getInstance().update(trashBinItem);
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, null);
    }

    public synchronized void removeTrashBinItem(TrashBinItem trashBinItem) {
        GalleryEntityManager.getInstance().delete(trashBinItem);
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, null);
    }

    public synchronized void removeTrashBinItems(List<Long> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        GalleryEntityManager.getInstance().delete(TrashBinItem.class, "_id IN (" + TextUtils.join(",", list) + ")", null);
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, null);
    }

    public synchronized void onAccountDelete(int i) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i == 0 || i == 1) {
            deleteAllSyncedItems();
        } else {
            if (i == 2) {
                deleteAllSyncedItems();
            }
            TrashUtils.clearSyncTag();
            GalleryPreferences.Trash.setUserInfo(null);
            GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, null);
        }
        TrashUtils.clearSyncTag();
        GalleryPreferences.Trash.setUserInfo(null);
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.TrashBin.TRASH_BIN_URI, null);
    }

    public void handleFilesAndDb(String str) {
        List<TrashBinItem> query = GalleryEntityManager.getInstance().query(TrashBinItem.class, str, null);
        if (BaseMiscUtil.isValid(query)) {
            long currentTimeMillis = System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.putNull("cloudServerId");
            contentValues.put("status", (Integer) 2);
            updateTrashBinItem(contentValues, str, null);
            for (TrashBinItem trashBinItem : query) {
                trashBinItem.setCloudServerId(null);
            }
            TrashUtils.doPurge(GalleryApp.sGetAndroidContext(), query);
            DefaultLogger.d("galleryAction_TrashManager", "handle %d trash item, cost %d", Integer.valueOf(query.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        }
    }

    public final void deleteAllSyncedItems() {
        handleFilesAndDb("cloudServerId IS NOT NULL");
    }

    /* loaded from: classes2.dex */
    public static class SimpleResult {
        public boolean isOriginFileDeleted;
        public String mTrashPath;

        public String getTrashPath() {
            return this.mTrashPath;
        }

        public final void setTrashPath(String str) {
            this.mTrashPath = str;
        }

        public boolean isOriginFileDeleted() {
            return this.isOriginFileDeleted;
        }

        public final void setOriginFileDeleted(boolean z) {
            this.isOriginFileDeleted = z;
        }
    }

    public static SimpleResult moveFileToTrash(String str, int i, String str2) throws StoragePermissionMissingException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DefaultLogger.d("galleryAction_TrashManager", "start move file to trash");
        SimpleResult simpleResult = new SimpleResult();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_TrashManager", "moveFileToTrash");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
        IStoragePermissionStrategy.PermissionResult checkPermission = storageStrategyManager.checkPermission(str, permission);
        if (!checkPermission.granted) {
            throw new StoragePermissionMissingException(Collections.singletonList(checkPermission));
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, permission, appendInvokerTag);
        String trashBinPath = getTrashBinPath();
        String fileName = BaseFileUtils.getFileName(str);
        String concat = BaseFileUtils.concat(trashBinPath, fileName);
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(concat, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        if (documentFile2 != null && documentFile2.exists() && documentFile2.isFile()) {
            if (documentFile == null || !documentFile.exists()) {
                simpleResult.setTrashPath(concat);
                return simpleResult;
            }
            concat = BaseFileUtils.concat(trashBinPath, DownloadPathHelper.addPostfixToFileName(fileName, String.valueOf(System.currentTimeMillis())));
            DefaultLogger.d("galleryAction_TrashManager", "same filename");
        }
        DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(concat, IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
        if (documentFile == null || !documentFile.exists()) {
            if (documentFile3 != null && documentFile3.exists()) {
                simpleResult.setTrashPath(concat);
                return simpleResult;
            }
            DefaultLogger.d("galleryAction_TrashManager", "null origin");
            return null;
        } else if (!moveFileToFolder(documentFile3, documentFile, -1L, new RecordParams(i, str2, str))) {
            return null;
        } else {
            simpleResult.setTrashPath(concat);
            simpleResult.setOriginFileDeleted(true);
            return simpleResult;
        }
    }

    public static String getTrashBinPath() {
        String filePathUnder = StorageUtils.getFilePathUnder(StorageUtils.getPrimaryStoragePath(), "/Android/data/com.miui.gallery/files/trashBin");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_TrashManager", "getTrashBinPath");
        if (!new File(filePathUnder).exists()) {
            StorageSolutionProvider.get().getDocumentFile(filePathUnder, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
        }
        return filePathUnder;
    }

    public static String getTargetFileName(String str, String str2, boolean z) {
        if (z) {
            return BaseFileUtils.getFileName(str);
        }
        String fileName = BaseFileUtils.getFileName(str);
        if (!TextUtils.isEmpty(fileName) && fileName.startsWith("{-trash-}")) {
            fileName = fileName.substring(9);
            try {
                fileName = Encode.decodeBase64(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(BaseFileUtils.getExtension(fileName))) {
            return str2;
        }
        return FileUtils.getName(str2) + "." + BaseFileUtils.getExtension(fileName);
    }

    public static String moveFileFromTrash(String str, String str2, String str3, long j) throws StoragePermissionMissingException {
        DocumentFile ensureSrcDst = ensureSrcDst(str, str2, str3);
        String concat = BaseFileUtils.concat(str, str2);
        if (ensureSrcDst == null) {
            return null;
        }
        DefaultLogger.d("galleryAction_TrashManager", "start move file from trash");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_TrashManager", "moveFileFromTrash");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT;
        IStoragePermissionStrategy.PermissionResult checkPermission = storageStrategyManager.checkPermission(concat, permission);
        if (!checkPermission.granted) {
            throw new StoragePermissionMissingException(Collections.singletonList(checkPermission));
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(concat, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        if (documentFile != null && documentFile.exists() && documentFile.isFile()) {
            DefaultLogger.d("galleryAction_TrashManager", "same filename");
            concat = BaseFileUtils.concat(str, DownloadPathHelper.addPostfixToFileName(str2, String.valueOf(System.currentTimeMillis())));
        }
        if (!moveFileToFolder(StorageSolutionProvider.get().getDocumentFile(concat, permission, appendInvokerTag), ensureSrcDst, j)) {
            return null;
        }
        return concat;
    }

    public static DocumentFile ensureSrcDst(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str2)) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str3, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("galleryAction_TrashManager", "ensureSrcDst"));
            if (documentFile != null && documentFile.exists()) {
                return documentFile;
            }
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class RecordParams {
        public final int deleteReason;
        public final String invokerTag;
        public final String originPath;

        public RecordParams(int i, String str, String str2) {
            this.deleteReason = i;
            this.invokerTag = str;
            this.originPath = str2;
        }
    }

    public static boolean moveFileToFolder(DocumentFile documentFile, DocumentFile documentFile2, long j) {
        return moveFileToFolder(documentFile, documentFile2, j, null);
    }

    public static boolean moveFileToFolder(DocumentFile documentFile, DocumentFile documentFile2, long j, RecordParams recordParams) {
        OutputStream outputStream;
        OutputStream outputStream2;
        OutputStream outputStream3 = null;
        try {
            InputStream openInputStream = StorageSolutionProvider.get().openInputStream(documentFile2);
            try {
                outputStream3 = StorageSolutionProvider.get().openOutputStream(documentFile);
                if (GalleryUtils.copyFile(openInputStream, outputStream3)) {
                    DefaultLogger.d("galleryAction_TrashManager", "move File [%s] toTrashFolder [%s] success.", documentFile2.getUri().toString(), documentFile.getUri().toString());
                }
                BaseMiscUtil.closeSilently(openInputStream);
                BaseMiscUtil.closeSilently(outputStream3);
                if (j > 0) {
                    StorageSolutionProvider.get().setLastModified(documentFile, j);
                }
                if (!documentFile2.delete() || recordParams == null) {
                    return true;
                }
                DefaultLogger.d("galleryAction_TrashManager", "delete originFile [%s] success.", recordParams.originPath);
                DeleteRecorder.getInstance().record(new DeleteRecord(recordParams.deleteReason, recordParams.originPath, recordParams.invokerTag));
                return true;
            } catch (Exception unused) {
                outputStream2 = outputStream3;
                outputStream3 = openInputStream;
                try {
                    DefaultLogger.e("galleryAction_TrashManager", "moveFileToFolder fails");
                    BaseMiscUtil.closeSilently(outputStream3);
                    BaseMiscUtil.closeSilently(outputStream2);
                    return false;
                } catch (Throwable th) {
                    outputStream = outputStream2;
                    th = th;
                    BaseMiscUtil.closeSilently(outputStream3);
                    BaseMiscUtil.closeSilently(outputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                outputStream = outputStream3;
                outputStream3 = openInputStream;
                BaseMiscUtil.closeSilently(outputStream3);
                BaseMiscUtil.closeSilently(outputStream);
                throw th;
            }
        } catch (Exception unused2) {
            outputStream2 = null;
        } catch (Throwable th3) {
            th = th3;
            outputStream = null;
        }
    }

    public static boolean isTrashFileByPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String fileName = BaseFileUtils.getFileName(str);
        return !TextUtils.isEmpty(fileName) && fileName.startsWith("{-trash-}");
    }

    public static String probeFileName(String str) {
        String substring = BaseFileUtils.getFileName(str).substring(9);
        try {
            return Encode.decodeBase64(substring);
        } catch (Exception e) {
            e.printStackTrace();
            return substring;
        }
    }

    public static List<IStoragePermissionStrategy.PermissionResult> checkRecoveryPermission(String str, String str2, String str3) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
        }
        if (StorageSolutionProvider.get().getDocumentFile(str3, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("galleryAction_TrashManager", "checkRecoveryPermission")).exists()) {
            if (!TextUtils.isEmpty(str2)) {
                IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.QUERY);
                if (!checkPermission2.granted) {
                    linkedList.add(checkPermission2);
                }
                IStoragePermissionStrategy.PermissionResult checkPermission3 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.INSERT);
                if (!checkPermission3.granted) {
                    linkedList.add(checkPermission3);
                }
            }
        } else {
            IStoragePermissionStrategy.PermissionResult checkPermission4 = StorageSolutionProvider.get().checkPermission(str3, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY);
            if (!checkPermission4.granted) {
                linkedList.add(checkPermission4);
            }
        }
        return linkedList;
    }
}
