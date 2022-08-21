package com.miui.gallery.provider.cloudmanager.method.cloud.rename.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.scanner.provider.GalleryMediaScannerProviderContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class RenameById2 extends BaseDataProvider {
    public final ContentValues mContentValues;
    public String mDstFilePath;
    public long mId;
    public final String mNewName;
    public String mScrFilePath;
    public String mScrFilePathColumn;
    public DocumentFile mSrcFile;

    public RenameById2(Context context, ArrayList<Long> arrayList, long j, String str, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        super(context, arrayList, supportSQLiteDatabase, new String[]{String.valueOf(j)});
        this.mId = j;
        this.mNewName = str;
        this.mContentValues = new ContentValues();
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        String str = this.mLocalFile;
        this.mScrFilePath = str;
        this.mScrFilePathColumn = "localFile";
        if (TextUtils.isEmpty(str)) {
            this.mScrFilePath = this.mThumbnailFile;
            this.mScrFilePathColumn = "thumbnailFile";
        }
        if (TextUtils.isEmpty(this.mScrFilePath)) {
            return -113L;
        }
        checkQueryPermission(this.mScrFilePath);
        this.mSrcFile = StorageSolutionProvider.get().getDocumentFile(this.mScrFilePath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("galleryAction_Method_RenameMethod", "setLastModified"));
        return super.verify(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DocumentFile documentFile = this.mSrcFile;
        if (documentFile != null && documentFile.exists()) {
            this.mDstFilePath = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(this.mScrFilePath), this.mNewName);
            if (new File(this.mDstFilePath).exists()) {
                this.mDstFilePath = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(this.mScrFilePath), String.format("%s_%s.%s", BaseFileUtils.getFileTitle(this.mNewName), Long.valueOf(System.currentTimeMillis()), BaseFileUtils.getExtension(this.mNewName)));
            }
        } else {
            DefaultLogger.d("galleryAction_Method_RenameMethod", "do prepare src null or not exist - %s", this.mSrcFile);
        }
        this.mContentValues.put(this.mScrFilePathColumn, this.mDstFilePath);
        this.mContentValues.put("title", BaseFileUtils.getFileTitle(this.mNewName));
        if (TextUtils.isEmpty(this.mLocalFile) && !BaseFileUtils.getExtension(this.mFileName).equals(BaseFileUtils.getExtension(this.mNewName))) {
            this.mContentValues.put("fileName", String.format("%s.%s", BaseFileUtils.getFileTitle(this.mNewName), BaseFileUtils.getExtension(this.mFileName)));
        } else {
            this.mContentValues.put("fileName", this.mNewName);
        }
        if (this.mGroupId != 2 || !TextUtils.isEmpty(this.mSourcePkg)) {
            return;
        }
        this.mContentValues.put("source_pkg", PackageUtils.gePackageNameForScreenshot(this.mFileName));
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList(checkRenamePermission(this.mScrFilePath, this.mDstFilePath));
        if (!BaseMiscUtil.isValid(linkedList)) {
            return;
        }
        throw new StoragePermissionMissingException(linkedList);
    }

    public final void checkQueryPermission(String str) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList();
        IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.QUERY);
        if (checkPermission.granted) {
            return;
        }
        linkedList.add(checkPermission);
        throw new StoragePermissionMissingException(linkedList);
    }

    public final List<IStoragePermissionStrategy.PermissionResult> checkRenamePermission(String str, String str2) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.UPDATE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
            IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission2.granted) {
                linkedList.add(checkPermission2);
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            IStoragePermissionStrategy.PermissionResult checkPermission3 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.QUERY);
            if (!checkPermission3.granted) {
                linkedList.add(checkPermission3);
            }
            IStoragePermissionStrategy.PermissionResult checkPermission4 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.INSERT);
            if (!checkPermission4.granted) {
                linkedList.add(checkPermission4);
            }
        }
        return linkedList;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        supportSQLiteDatabase.beginTransactionNonExclusive();
        try {
            try {
                RemarkManager.remarkMediaId(RemarkInfoFactory.createRenameRemarkInfo(this.mId, this.mScrFilePath, this.mDstFilePath));
                long executeDB = executeDB();
                if (executeDB > 0 && !executeFile()) {
                    throw new RuntimeException("executeFile failed!");
                }
                supportSQLiteDatabase.setTransactionSuccessful();
                if (supportSQLiteDatabase.inTransaction()) {
                    supportSQLiteDatabase.endTransaction();
                }
                RemarkManager.doneRemarkMediaIds(this.mId);
                return executeDB;
            } catch (Exception e) {
                DefaultLogger.e("galleryAction_Method_RenameMethod", e.getMessage());
                if (supportSQLiteDatabase.inTransaction()) {
                    supportSQLiteDatabase.endTransaction();
                }
                RemarkManager.doneRemarkMediaIds(this.mId);
                return -113L;
            }
        } catch (Throwable th) {
            if (supportSQLiteDatabase.inTransaction()) {
                supportSQLiteDatabase.endTransaction();
            }
            RemarkManager.doneRemarkMediaIds(this.mId);
            throw th;
        }
    }

    public final boolean executeFile() {
        Bundle bundle = new Bundle();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_Method_RenameMethod", "executeFile");
        bundle.putString("param_path", this.mScrFilePath);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri uri = GalleryMediaScannerProviderContract.AUTHORITY_URI;
        contentResolver.call(uri, "save_request_media_store_scan_record", (String) null, bundle);
        bundle.putString("param_path", this.mDstFilePath);
        this.mContext.getContentResolver().call(uri, "save_request_media_store_scan_record", (String) null, bundle);
        boolean moveFile = StorageSolutionProvider.get().moveFile(this.mScrFilePath, this.mDstFilePath, appendInvokerTag);
        DefaultLogger.d("galleryAction_Method_RenameMethod", "executeFile => [%b] from [%s] to [%s]", Boolean.valueOf(moveFile), this.mScrFilePath, this.mDstFilePath);
        return moveFile;
    }

    public final long executeDB() {
        DefaultLogger.d("galleryAction_Method_RenameMethod", "executeDB => [%s]", this.mContentValues.toString());
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(7);
        GalleryUtils.safeExec(String.format("update %s set %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s=%s", "cloud", "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, j.c, Long.valueOf(this.mId)));
        return SafeDBUtil.safeUpdate(this.mContext, GalleryContract.Cloud.CLOUD_URI, this.mContentValues, "_id=?", new String[]{String.valueOf(this.mId)});
    }

    public String toString() {
        return String.format(Locale.US, "Rename{id: %d}", Long.valueOf(this.mId));
    }
}
