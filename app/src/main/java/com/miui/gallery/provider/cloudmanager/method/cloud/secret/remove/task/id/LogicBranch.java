package com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class LogicBranch extends BaseLogicBranch {
    public final long mAlbumId;
    public final ContentValues mContentValues;
    public final long mMediaConflictValidation;
    public final long mMediaId;

    public LogicBranch(Context context, ArrayList<Long> arrayList, long j, long j2, long j3, IDataProvider iDataProvider) {
        super(context, arrayList, iDataProvider);
        this.mContentValues = new ContentValues();
        this.mMediaId = j;
        this.mAlbumId = j2;
        this.mMediaConflictValidation = j3;
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList();
        linkedList.addAll(checkDecryptPermission(this.mMicroThumbnailFile, this.mContentValues.getAsString("microthumbfile")));
        linkedList.addAll(checkDecryptPermission(this.mThumbnailFile, this.mContentValues.getAsString("thumbnailFile")));
        linkedList.addAll(checkDecryptPermission(this.mLocalFile, this.mContentValues.getAsString("localFile")));
        String str = (String) SafeDBUtil.safeQuery(supportSQLiteDatabase, "album", new String[]{"localPath"}, "_id=" + this.mAlbumId, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<String>() { // from class: com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.LogicBranch.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1808handle(Cursor cursor) {
                if (cursor == null || cursor.getCount() != 1) {
                    return null;
                }
                cursor.moveToFirst();
                return StorageUtils.getPathInPriorStorage(cursor.getString(0));
            }
        });
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException(String.format("album id [%d] is illegal", Long.valueOf(this.mAlbumId)));
        }
        linkedList.addAll(checkMovePermission(this.mContentValues.getAsString("thumbnailFile"), str));
        linkedList.addAll(checkMovePermission(this.mContentValues.getAsString("localFile"), str));
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        executeFile();
        return -101L;
    }

    public final void executeFile() {
        executeFile("microthumbfile", false, this.mMicroThumbnailFile, this.mContentValues.getAsString("microthumbfile"), this.mSecretKey, false);
        executeFile("thumbnailFile", executeFile("localFile", false, this.mLocalFile, this.mContentValues.getAsString("localFile"), this.mSecretKey, this.mServerType == 2), this.mThumbnailFile, this.mContentValues.getAsString("thumbnailFile"), this.mSecretKey, false);
    }

    public final boolean executeFile(String str, boolean z, String str2, String str3, byte[] bArr, boolean z2) {
        if (TextUtils.equals(str2, str3)) {
            return false;
        }
        if (z || TextUtils.isEmpty(str3)) {
            this.mContentValues.putNull(str);
            FileHandleManager.deleteFile(str2, 1004, "galleryAction_removeSecretBase");
            return false;
        } else if (bArr == null || !CloudUtils.SecretAlbumUtils.isEncryptedFile(str2, z2)) {
            DefaultLogger.d("galleryAction_removeSecretBase", "executeFile only move file because  ", bArr == null ? "seckey null" : "sec file not encyrpted extension");
            return StorageSolutionProvider.get().moveFile(str2, str3, FileHandleRecordHelper.appendInvokerTag("galleryAction_removeSecretBase", "executeFile"));
        } else {
            return str3.equals(CloudUtils.SecretAlbumUtils.decryptFile(str2, str3, z2, bArr, true));
        }
    }

    public void addFilePath(ContentValues contentValues, boolean z) {
        contentValues.put("localFile", this.mLocalFile);
        contentValues.put("thumbnailFile", this.mThumbnailFile);
        contentValues.put("microthumbfile", this.mMicroThumbnailFile);
        String addPostfixToFileName = z ? DownloadPathHelper.addPostfixToFileName(this.mFileName, String.valueOf(System.currentTimeMillis())) : this.mFileName;
        contentValues.put("fileName", addPostfixToFileName);
        if (this.mSecretKey != null) {
            decryptFilePath(addPostfixToFileName, contentValues);
        } else {
            decodeFileNames(addPostfixToFileName, contentValues);
        }
    }

    public void decryptFilePath(String str, ContentValues contentValues) {
        if (this.mSecretKey == null) {
            return;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_removeSecretBase", "decryptFilePath");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String str2 = this.mMicroThumbnailFile;
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(str2, permission, appendInvokerTag);
        if (documentFile != null && documentFile.exists()) {
            contentValues.put("microthumbfile", decryptFilePath(this.mMicroThumbnailFile, CloudUtils.getThumbnailNameBySha1(this.mSha1)));
        }
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(this.mLocalFile, permission, appendInvokerTag);
        if (documentFile2 != null && documentFile2.exists()) {
            contentValues.put("localFile", decryptFilePath(this.mLocalFile, str));
        }
        DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(this.mThumbnailFile, permission, appendInvokerTag);
        if (documentFile3 == null || !documentFile3.exists()) {
            return;
        }
        contentValues.put("thumbnailFile", decryptFilePath(this.mThumbnailFile, CloudUtils.getThumbnailNameByTitle(this.mTitle)));
    }

    public void decodeFileNames(String str, ContentValues contentValues) {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_removeSecretBase", "decodeFileNames");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String str2 = this.mLocalFile;
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(str2, permission, appendInvokerTag);
        if (documentFile != null && documentFile.exists()) {
            contentValues.put("localFile", decodeFileName(this.mLocalFile, str));
        }
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(this.mThumbnailFile, permission, appendInvokerTag);
        if (documentFile2 == null || !documentFile2.exists()) {
            return;
        }
        String decodeFileName = decodeFileName(this.mThumbnailFile, CloudUtils.getThumbnailNameByTitle(this.mTitle));
        if (decodeFileName != null) {
            contentValues.put("thumbnailFile", decodeFileName);
            return;
        }
        contentValues.put("localFile", this.mThumbnailFile);
        contentValues.put("thumbnailFile", this.mThumbnailFile);
    }

    public static String decodeFileName(String str, String str2) {
        int lastIndexOf;
        if (!TextUtils.isEmpty(str) && (lastIndexOf = str.lastIndexOf(h.g)) != -1) {
            String str3 = str.substring(0, lastIndexOf + 1) + str2;
            return new File(str3).exists() ? DownloadPathHelper.addPostfixToFileName(str3, String.valueOf(System.currentTimeMillis())) : str3;
        }
        return null;
    }

    public static List<IStoragePermissionStrategy.PermissionResult> checkDecryptPermission(String str, String str2) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.INSERT);
            if (!checkPermission2.granted) {
                linkedList.add(checkPermission2);
            }
        }
        return linkedList;
    }

    public static List<IStoragePermissionStrategy.PermissionResult> checkMovePermission(String str, String str2) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
            String fileName = BaseFileUtils.getFileName(str);
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            IStoragePermissionStrategy.PermissionResult checkPermission2 = storageStrategyManager.checkPermission(str2 + File.separator + fileName, IStoragePermissionStrategy.Permission.INSERT);
            if (!checkPermission2.granted) {
                linkedList.add(checkPermission2);
            }
        }
        return linkedList;
    }

    public static String decryptFilePath(String str, String str2) {
        int lastIndexOf;
        if (!TextUtils.isEmpty(str) && (lastIndexOf = str.lastIndexOf(h.g)) != -1) {
            return str.substring(0, lastIndexOf + 1) + str2;
        }
        return null;
    }
}
