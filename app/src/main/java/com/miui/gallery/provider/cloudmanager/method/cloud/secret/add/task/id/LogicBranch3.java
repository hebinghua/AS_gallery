package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class LogicBranch3 extends LogicBranch {
    public final ContentValues mContentValues;

    public LogicBranch3(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, int i) {
        super(context, arrayList, iDataProvider, j, i);
        this.mContentValues = new ContentValues();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        String str;
        super.doPrepare(supportSQLiteDatabase, mediaManager);
        this.mContentValues.put("localGroupId", (Long) (-1000L));
        if (this.mCheckConflict == -105) {
            str = DownloadPathHelper.addPostfixToFileName(this.mFileName, String.valueOf(System.currentTimeMillis()));
        } else {
            str = this.mFileName;
        }
        this.mContentValues.put("fileName", str);
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage("MIUI/Gallery/cloud/secretAlbum");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_addSecret3", "doPrepare");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String str2 = this.mLocalFile;
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(str2, permission, appendInvokerTag);
        if (documentFile != null && documentFile.exists()) {
            String encodeFileName = CloudUtils.SecretAlbumUtils.encodeFileName(str, this.mServerType == 2);
            ContentValues contentValues = this.mContentValues;
            contentValues.put("localFile", pathInPriorStorage + File.separator + encodeFileName);
        }
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(this.mThumbnailFile, permission, appendInvokerTag);
        if (documentFile2 == null || !documentFile2.exists()) {
            return;
        }
        String encodeFileName2 = CloudUtils.SecretAlbumUtils.encodeFileName(str, false);
        ContentValues contentValues2 = this.mContentValues;
        contentValues2.put("thumbnailFile", pathInPriorStorage + File.separator + encodeFileName2);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList();
        try {
            super.postPrepare(supportSQLiteDatabase, mediaManager);
        } catch (StoragePermissionMissingException e) {
            linkedList.addAll(e.getPermissionResultList());
        }
        if (!TextUtils.isEmpty(this.mLocalFile)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(this.mLocalFile, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
            String asString = this.mContentValues.getAsString("localFile");
            if (!TextUtils.isEmpty(asString)) {
                IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(asString, IStoragePermissionStrategy.Permission.INSERT);
                if (!checkPermission2.granted) {
                    linkedList.add(checkPermission2);
                }
                IStoragePermissionStrategy.PermissionResult checkPermission3 = StorageSolutionProvider.get().checkPermission(asString, IStoragePermissionStrategy.Permission.UPDATE);
                if (!checkPermission3.granted) {
                    linkedList.add(checkPermission3);
                }
            }
        }
        if (!TextUtils.isEmpty(this.mThumbnailFile)) {
            IStoragePermissionStrategy.PermissionResult checkPermission4 = StorageSolutionProvider.get().checkPermission(this.mThumbnailFile, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission4.granted) {
                linkedList.add(checkPermission4);
            }
            String asString2 = this.mContentValues.getAsString("thumbnailFile");
            if (!TextUtils.isEmpty(asString2)) {
                IStoragePermissionStrategy.PermissionResult checkPermission5 = StorageSolutionProvider.get().checkPermission(asString2, IStoragePermissionStrategy.Permission.INSERT);
                if (!checkPermission5.granted) {
                    linkedList.add(checkPermission5);
                }
                IStoragePermissionStrategy.PermissionResult checkPermission6 = StorageSolutionProvider.get().checkPermission(asString2, IStoragePermissionStrategy.Permission.UPDATE);
                if (!checkPermission6.granted) {
                    linkedList.add(checkPermission6);
                }
            }
        }
        if (!BaseMiscUtil.isValid(linkedList)) {
            return;
        }
        throw new StoragePermissionMissingException(linkedList);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        try {
            return executeFile() ? executeDB(supportSQLiteDatabase, mediaManager) : 0L;
        } finally {
            super.execute(supportSQLiteDatabase, mediaManager);
        }
    }

    public final boolean executeFile() {
        DefaultLogger.e("galleryAction_addSecret3", "executeFile");
        String asString = this.mContentValues.getAsString("localFile");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_addSecret3", "executeFile");
        if (!StorageSolutionProvider.get().moveFile(this.mLocalFile, asString, appendInvokerTag)) {
            DefaultLogger.e("galleryAction_addSecret3", "executeFile => moveFile: false");
            if (!StorageSolutionProvider.get().moveFile(this.mThumbnailFile, this.mContentValues.getAsString("thumbnailFile"), appendInvokerTag)) {
                DefaultLogger.e("galleryAction_addSecret3", "executeFile => moveThumbnailFile: false");
                return false;
            }
            this.mContentValues.putNull("localFile");
            FileHandleManager.deleteFile(this.mLocalFile, 1003, "galleryAction_addSecret3");
            return true;
        }
        this.mContentValues.putNull("thumbnailFile");
        FileHandleManager.deleteFile(this.mThumbnailFile, 1003, "galleryAction_addSecret3");
        return true;
    }

    public final long executeDB(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        int update = supportSQLiteDatabase.update("cloud", 0, this.mContentValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
        DefaultLogger.d("galleryAction_addSecret3", "executeDB update => %s ; count = [%d]", Util.desensitization(this.mContentValues), Integer.valueOf(update));
        if (update > 0) {
            if (mediaManager != null) {
                mediaManager.update("_id=?", new String[]{String.valueOf(this.mMediaId)}, this.mContentValues);
            }
            return this.mMediaId;
        }
        return 0L;
    }
}
