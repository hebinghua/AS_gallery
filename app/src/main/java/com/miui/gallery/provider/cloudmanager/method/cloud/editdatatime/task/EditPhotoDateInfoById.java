package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.AddRemoveFavoriteMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.local.DeleteFile;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryExifUtils;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class EditPhotoDateInfoById extends BaseDataProvider implements IHandler {
    public boolean isExifSupportModification;
    public boolean isFavorites;
    public Long mAlbumAttributes;
    public File mNewFile;
    public String mPackageName;
    public long mPhotoNewTime;
    public Bundle mResult;
    public long mSourcePhotoId;
    public String mTargetPath;
    public final ILogWrapper mWrapper;

    public EditPhotoDateInfoById(Context context, ArrayList<Long> arrayList, long j, long j2, boolean z, Cursor cursor, Bundle bundle) {
        super(context, arrayList, cursor);
        this.mPhotoNewTime = j2;
        this.isFavorites = z;
        this.mSourcePhotoId = j;
        this.mResult = bundle;
        this.mWrapper = new LogWrapper();
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        return this.mWrapper.verify(this, supportSQLiteDatabase, mediaManager, this.mLocalFile, this.mLocalGroupId);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long doVerify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        long j = this.mLocalGroupId;
        if (j == 0 || j == -1) {
            return -128L;
        }
        this.mAlbumAttributes = Long.valueOf(this.mWrapper.queryAlbumAttributes(this, supportSQLiteDatabase));
        return -1L;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long queryAlbumAttributes(SupportSQLiteDatabase supportSQLiteDatabase) {
        Cursor cursor = null;
        try {
            cursor = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(new String[]{"attributes"}).selection("_id=?", new String[]{String.valueOf(this.mLocalGroupId)}).create());
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(0);
            }
            BaseMiscUtil.closeSilently(cursor);
            return -1L;
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        boolean isExifSupportModification = BaseFileMimeUtil.isExifSupportModification(this.mLocalFile);
        this.isExifSupportModification = isExifSupportModification;
        if (isExifSupportModification) {
            File file = new File(this.mLocalFile);
            String name = file.getName();
            this.mPackageName = name;
            if (name.startsWith("Screenshot")) {
                this.mPackageName = PackageUtils.gePackageNameForScreenshot(this.mPackageName);
            } else {
                this.mPackageName = null;
            }
            this.mTargetPath = GalleryExifUtils.generateUniqueFileDateTimeName(file, this.mPhotoNewTime, this.mPackageName);
            return;
        }
        this.mTargetPath = GalleryExifUtils.generateUniqueFileDateTimeName(new File(this.mLocalFile), this.mPhotoNewTime);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList();
        IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(this.mLocalFile, IStoragePermissionStrategy.Permission.QUERY);
        if (!checkPermission.granted) {
            linkedList.add(checkPermission);
        }
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String str = this.mLocalFile;
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
        IStoragePermissionStrategy.PermissionResult checkPermission2 = storageStrategyManager.checkPermission(str, permission);
        if (!checkPermission2.granted) {
            linkedList.add(checkPermission2);
        }
        if (!TextUtils.isEmpty(this.mTargetPath)) {
            IStoragePermissionStrategy.PermissionResult checkPermission3 = StorageSolutionProvider.get().checkPermission(this.mTargetPath, IStoragePermissionStrategy.Permission.INSERT);
            if (!checkPermission3.granted) {
                linkedList.add(checkPermission3);
            }
            IStoragePermissionStrategy.PermissionResult checkPermission4 = StorageSolutionProvider.get().checkPermission(this.mTargetPath, permission);
            if (!checkPermission4.granted) {
                linkedList.add(checkPermission4);
            }
        }
        if (!BaseMiscUtil.isValid(linkedList)) {
            return;
        }
        throw new StoragePermissionMissingException(linkedList);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        return this.mWrapper.doExecute(this, supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long doExecute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        supportSQLiteDatabase.beginTransactionNonExclusive();
        try {
            RemarkManager.remarkMediaId(RemarkInfoFactory.createEditDateTimeRemarkInfo(this.mSourcePhotoId, this.mLocalFile, this.mTargetPath));
            boolean updateFile = this.mWrapper.updateFile(this, this.mNewFile, this.isExifSupportModification);
            File file = this.mNewFile;
            if (file != null && isFileExist(file)) {
                boolean z = this.isExifSupportModification;
                if (!z && !updateFile) {
                    deleteFile(this.mNewFile);
                    supportSQLiteDatabase.setTransactionSuccessful();
                    RemarkManager.doneRemarkMediaIds(this.mSourcePhotoId);
                    return -4L;
                } else if ((!z || !updateFile) && !this.mWrapper.setLastModified(this)) {
                    deleteFile(this.mNewFile);
                    supportSQLiteDatabase.setTransactionSuccessful();
                    RemarkManager.doneRemarkMediaIds(this.mSourcePhotoId);
                    return -4L;
                } else if (this.mWrapper.updateLocalFlag(this, supportSQLiteDatabase, this.mLocalFile, this.mServerId, this.mSourcePhotoId) <= 0) {
                    deleteFile(this.mNewFile);
                    supportSQLiteDatabase.setTransactionSuccessful();
                    RemarkManager.doneRemarkMediaIds(this.mSourcePhotoId);
                    return -16L;
                } else {
                    long deleteSource = this.mWrapper.deleteSource(this, supportSQLiteDatabase, mediaManager, this.mLocalFile, true);
                    if (deleteSource <= 0) {
                        deleteFile(this.mNewFile);
                        supportSQLiteDatabase.setTransactionSuccessful();
                        RemarkManager.doneRemarkMediaIds(this.mSourcePhotoId);
                        return -16L;
                    }
                    RemarkManager.doneRemarkMediaIds(this.mSourcePhotoId);
                    String absolutePath = this.mNewFile.getAbsolutePath();
                    long scanNewFile = this.mWrapper.scanNewFile(this, supportSQLiteDatabase, mediaManager, absolutePath);
                    if (scanNewFile <= 0) {
                        deleteFile(this.mNewFile);
                        supportSQLiteDatabase.setTransactionSuccessful();
                        return -16L;
                    }
                    long updateCloudInfo = this.mWrapper.updateCloudInfo(this, supportSQLiteDatabase, mediaManager, scanNewFile, deleteSource, this.mServerId);
                    long j = -32;
                    long addFavorite = this.mWrapper.addFavorite(this, supportSQLiteDatabase, mediaManager, scanNewFile, updateCloudInfo <= 0 ? -32L : updateCloudInfo);
                    if (addFavorite > 0) {
                        j = addFavorite;
                    }
                    this.mResult.putString("photo_path", absolutePath);
                    supportSQLiteDatabase.setTransactionSuccessful();
                    return j;
                }
            }
            this.mWrapper.logNewFileError();
            supportSQLiteDatabase.setTransactionSuccessful();
            RemarkManager.doneRemarkMediaIds(this.mSourcePhotoId);
            return -4L;
        } finally {
            supportSQLiteDatabase.endTransaction();
        }
    }

    public final boolean isFileExist(File file) {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("galleryAction_EditPhotoDateInfoById", "isFileExist"));
        return documentFile != null && documentFile.exists();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public boolean updateFile() {
        if (this.isExifSupportModification) {
            Pair<Boolean, File> imageFileDataTime = GalleryExifUtils.setImageFileDataTime(this.mContext, this.mLocalFile, this.mPhotoNewTime, true, this.mPackageName, this.mTargetPath);
            if (imageFileDataTime == null) {
                return false;
            }
            this.mNewFile = (File) imageFileDataTime.second;
            return ((Boolean) imageFileDataTime.first).booleanValue();
        }
        this.mNewFile = new File(this.mTargetPath);
        return StorageSolutionProvider.get().copyFile(this.mLocalFile, this.mNewFile.getAbsolutePath(), FileHandleRecordHelper.appendInvokerTag("galleryAction_EditPhotoDateInfoById", "updateFile"));
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public boolean setLastModified() {
        return StorageSolutionProvider.get().setLastModified(StorageSolutionProvider.get().getDocumentFile(this.mNewFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("galleryAction_EditPhotoDateInfoById", "setLastModified")), this.mPhotoNewTime);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long addFavorite(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2) {
        return !this.isFavorites ? j2 : AddRemoveFavoriteMethod.addRemoveFavoritesById(GalleryApp.sGetAndroidContext(), supportSQLiteDatabase, mediaManager, getDirtyBulk(), j, 1);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long updateCloudInfo(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2) {
        ContentValues contentValues = new ContentValues();
        String idSelection = EditDateTimeFactory.getIdSelection(j);
        if (!TextUtils.isEmpty(this.mLocation)) {
            contentValues.put("location", this.mLocation);
        } else {
            LocationManager.getInstance().loadLocation(j);
        }
        if (!TextUtils.isEmpty(this.mSourcePkg)) {
            contentValues.put("source_pkg", this.mSourcePkg);
        }
        if (this.mServerId != 0) {
            contentValues.put("localImageId", Long.valueOf(this.mSourcePhotoId));
        }
        if (contentValues.isEmpty()) {
            return j2;
        }
        long update = supportSQLiteDatabase.update("cloud", 0, contentValues, idSelection, null);
        mediaManager.update("_id = ?", new String[]{String.valueOf(j)}, contentValues);
        return update;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long updateLocalFlag(SupportSQLiteDatabase supportSQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) 15);
        return supportSQLiteDatabase.update("cloud", 0, contentValues, "_id=?", new String[]{String.valueOf(this.mSourcePhotoId)});
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long deleteSource(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        try {
            return new DeleteFile(this.mContext, new ArrayList(), this.mSourcePhotoId, 58, supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager);
        } catch (Exception unused) {
            DefaultLogger.e("EditPhotoDateInfoById", "deleteSource failed");
            return -121L;
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long scanNewFile(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str) {
        ScanResult scanNewFile = scanNewFile(this.mLocalGroupId, this.mNewFile, this.mAlbumAttributes.intValue());
        return scanNewFile.getReasonCode() == ScanContracts$ScanResultReason.ALREADY_EXISTS ? this.mWrapper.deleteAndReScan(this, supportSQLiteDatabase, mediaManager, str, true) : scanNewFile.getMediaId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.IHandler
    public long deleteAndReScan(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        String str = "localFile = '" + this.mNewFile + "'";
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(new String[]{j.c}).selection(str, null).create());
        if (query != null && query.moveToFirst()) {
            long j = query.getLong(0);
            supportSQLiteDatabase.delete("cloud", str, null);
            mediaManager.delete("_id=?", new String[]{String.valueOf(j)});
        }
        if (query != null) {
            query.close();
        }
        return scanNewFile(this.mLocalGroupId, this.mNewFile, this.mAlbumAttributes.intValue()).getMediaId();
    }

    public final ScanResult scanNewFile(long j, File file, int i) {
        return SaveToCloudUtil.saveToCloudDB(this.mContext, new SaveParams.Builder().setAlbumId(j).setSaveFile(file).setBulkNotify(false).setSpecifiedTakenTime(this.mPhotoNewTime).setAlbumAttributes(i).setLocalFlag(7).setCredible(true).build());
    }

    public final void deleteFile(File file) {
        if (file == null) {
            return;
        }
        FileHandleManager.deleteFile(file.getAbsolutePath(), 1002, "galleryAction_EditDateTime");
    }

    public String toString() {
        return String.format(Locale.US, "updatePhotoDateTime by id: [%s]", Long.valueOf(this.mSourcePhotoId));
    }
}
