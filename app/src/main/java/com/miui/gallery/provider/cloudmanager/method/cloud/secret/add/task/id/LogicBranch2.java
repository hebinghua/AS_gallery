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
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class LogicBranch2 extends LogicBranch {
    public final ContentValues mContentValues;

    public LogicBranch2(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, int i) {
        super(context, arrayList, iDataProvider, j, i);
        this.mContentValues = new ContentValues();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        String str;
        super.doPrepare(supportSQLiteDatabase, mediaManager);
        SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("cloud");
        String[] strArr = Contracts.PUBLIC_COPYABLE_PROJECTION;
        Cursor query = supportSQLiteDatabase.query(builder.columns(strArr).selection("_id=" + this.mMediaId, null).create());
        boolean z = false;
        if (query != null) {
            try {
                if (query.getCount() == 1) {
                    query.moveToFirst();
                    this.mContentValues.putAll(Util.copyOf(strArr, query));
                    query.close();
                    this.mContentValues.put("localFlag", (Integer) 8);
                    this.mContentValues.put("localGroupId", (Long) (-1000L));
                    if (this.mCheckConflict == -105) {
                        str = DownloadPathHelper.addPostfixToFileName(this.mFileName, String.valueOf(System.currentTimeMillis()));
                    } else {
                        str = this.mFileName;
                    }
                    this.mContentValues.put("fileName", str);
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mLocalFile, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("galleryAction_addSecret2", "doPrepare"));
                    if (documentFile == null || !documentFile.exists()) {
                        throw new IllegalArgumentException(String.format("[%s] %s", "galleryAction_addSecret2", "local file do not exists."));
                    }
                    String pathInPriorStorage = StorageUtils.getPathInPriorStorage("MIUI/Gallery/cloud/secretAlbum");
                    if (this.mServerType == 2) {
                        z = true;
                    }
                    String encodeFileName = CloudUtils.SecretAlbumUtils.encodeFileName(str, z);
                    this.mContentValues.put("localFile", pathInPriorStorage + File.separator + encodeFileName);
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
        throw new IllegalArgumentException(String.format("[%s] %s", "galleryAction_addSecret2", "copy error."));
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
        boolean moveFile = StorageSolutionProvider.get().moveFile(this.mLocalFile, this.mContentValues.getAsString("localFile"), FileHandleRecordHelper.appendInvokerTag("galleryAction_addSecret2", "executeFile"));
        DefaultLogger.d("galleryAction_addSecret2", "executeFile: %b", Boolean.valueOf(moveFile));
        return moveFile;
    }

    public final long executeDB(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        long insert = supportSQLiteDatabase.insert("cloud", 0, this.mContentValues);
        DefaultLogger.d("galleryAction_addSecret2", "executeDB insert => %s ; id = [%d]", Util.desensitization(this.mContentValues), Long.valueOf(insert));
        if (insert > 0) {
            mediaManager.insert(insert, this.mContentValues);
            if (supportSQLiteDatabase.delete("cloud", "_id=?", new String[]{String.valueOf(this.mMediaId)}) > 0) {
                mediaManager.delete("_id=?", new String[]{String.valueOf(this.mMediaId)});
            }
            return insert;
        }
        return 0L;
    }
}
