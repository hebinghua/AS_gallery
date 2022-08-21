package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Contracts;
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
public class LogicBranch4 extends LogicBranch {
    public final ContentValues mInsertContentValues;
    public final ContentValues mUpdateContentValues;

    public LogicBranch4(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, int i) {
        super(context, arrayList, iDataProvider, j, i);
        this.mUpdateContentValues = new ContentValues();
        this.mInsertContentValues = new ContentValues();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        super.doPrepare(supportSQLiteDatabase, mediaManager);
        this.mUpdateContentValues.put("localFlag", (Integer) 11);
        this.mInsertContentValues.put("localFlag", (Integer) 5);
        SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("cloud");
        String[] strArr = Contracts.PUBLIC_COPYABLE_PROJECTION;
        Cursor query = supportSQLiteDatabase.query(builder.columns(strArr).selection("_id=" + this.mMediaId, null).create());
        if (query != null) {
            try {
                boolean z = true;
                if (query.getCount() == 1) {
                    query.moveToFirst();
                    this.mInsertContentValues.putAll(Util.copyOf(strArr, query));
                    query.close();
                    this.mInsertContentValues.put("microthumbfile", this.mMicroThumbnailFile);
                    this.mInsertContentValues.put("fromLocalGroupId", Long.valueOf(this.mLocalGroupId));
                    this.mInsertContentValues.put("localGroupId", (Long) (-1000L));
                    this.mInsertContentValues.put("localImageId", Long.valueOf(this.mMediaId));
                    String addPostfixToFileName = this.mCheckConflict == -105 ? DownloadPathHelper.addPostfixToFileName(this.mFileName, String.valueOf(System.currentTimeMillis())) : this.mFileName;
                    this.mInsertContentValues.put("fileName", addPostfixToFileName);
                    String pathInPriorStorage = StorageUtils.getPathInPriorStorage("MIUI/Gallery/cloud/secretAlbum");
                    String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_addSecret4", "doPrepare");
                    StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                    String str = this.mLocalFile;
                    IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
                    DocumentFile documentFile = storageStrategyManager.getDocumentFile(str, permission, appendInvokerTag);
                    if (documentFile != null && documentFile.exists()) {
                        if (this.mServerType != 2) {
                            z = false;
                        }
                        String encodeFileName = CloudUtils.SecretAlbumUtils.encodeFileName(addPostfixToFileName, z);
                        this.mInsertContentValues.put("localFile", pathInPriorStorage + File.separator + encodeFileName);
                    }
                    DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(this.mThumbnailFile, permission, appendInvokerTag);
                    if (documentFile2 == null || !documentFile2.exists()) {
                        return;
                    }
                    String encodeFileName2 = CloudUtils.SecretAlbumUtils.encodeFileName(addPostfixToFileName, false);
                    this.mInsertContentValues.put("thumbnailFile", pathInPriorStorage + File.separator + encodeFileName2);
                    return;
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        throw new IllegalArgumentException("copy error");
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
            String asString = this.mInsertContentValues.getAsString("localFile");
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
            String asString2 = this.mInsertContentValues.getAsString("thumbnailFile");
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
        DefaultLogger.e("galleryAction_addSecret4", "executeFile");
        String asString = this.mInsertContentValues.getAsString("localFile");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_addSecret4", "executeFile");
        if (!StorageSolutionProvider.get().moveFile(this.mLocalFile, asString, appendInvokerTag)) {
            DefaultLogger.e("galleryAction_addSecret4", "executeFile => moveFile: false");
            String asString2 = this.mInsertContentValues.getAsString("thumbnailFile");
            if (!TextUtils.isEmpty(this.mThumbnailFile) && !StorageSolutionProvider.get().moveFile(this.mThumbnailFile, asString2, appendInvokerTag)) {
                DefaultLogger.e("galleryAction_addSecret4", "executeFile => moveThumbnailFile: false");
                return false;
            }
            this.mInsertContentValues.putNull("localFile");
            FileHandleManager.deleteFile(this.mLocalFile, 1003, "galleryAction_addSecret4");
            return true;
        }
        this.mInsertContentValues.putNull("thumbnailFile");
        FileHandleManager.deleteFile(this.mThumbnailFile, 1003, "galleryAction_addSecret4");
        return true;
    }

    public final long executeDB(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        long insert = supportSQLiteDatabase.insert("cloud", 0, this.mInsertContentValues);
        DefaultLogger.d("galleryAction_addSecret4", "executeDB insert => %s ; id = [%d]", Util.desensitization(this.mInsertContentValues), Long.valueOf(insert));
        if (insert > 0) {
            mediaManager.insert(insert, this.mInsertContentValues);
            int update = supportSQLiteDatabase.update("cloud", 0, this.mUpdateContentValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
            DefaultLogger.d("galleryAction_addSecret4", "executeDB update => %s ; count = [%d]", Util.desensitization(this.mUpdateContentValues), Integer.valueOf(update));
            if (update > 0) {
                mediaManager.delete("_id=?", new String[]{String.valueOf(this.mMediaId)});
            }
            return insert;
        }
        return 0L;
    }
}
