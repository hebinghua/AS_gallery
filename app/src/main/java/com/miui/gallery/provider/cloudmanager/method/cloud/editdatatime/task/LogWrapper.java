package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task;

import android.os.Build;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

/* loaded from: classes2.dex */
public class LogWrapper implements ILogWrapper {
    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long verify(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, long j) {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] ------------------------->start");
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 1. query source record is success ,mLocalFile: [%s], mLocalGroupId: [%d]", str, Long.valueOf(j));
        return iHandler.doVerify(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long queryAlbumAttributes(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase) {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 2. query attributes");
        long queryAlbumAttributes = iHandler.queryAlbumAttributes(supportSQLiteDatabase);
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 2. query attributes success");
        return queryAlbumAttributes;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long doExecute(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        try {
            long doExecute = iHandler.doExecute(supportSQLiteDatabase, mediaManager);
            DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] ------------------------->end");
            return doExecute;
        } catch (Exception e) {
            DefaultLogger.e("galleryAction_EditDateTime", "[Edit FileInfo] Failed edit ,Error Message Is %s", e.getMessage());
            return -16L;
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public boolean updateFile(IHandler iHandler, File file, boolean z) {
        boolean updateFile = iHandler.updateFile();
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 3. edit Photo exifSupportModification is [%b], edit result is [%b]", Boolean.valueOf(z), Boolean.valueOf(updateFile));
        return updateFile;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public void logNewFileError() {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 3. edit newFile is null or newFile not exists !");
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public boolean setLastModified(IHandler iHandler) {
        boolean lastModified = iHandler.setLastModified();
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 3. edit lastModifyTime result [%s] , Android Rom version is [%d]", Boolean.valueOf(lastModified), Integer.valueOf(Build.VERSION.SDK_INT));
        return lastModified;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long updateLocalFlag(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, String str, long j, long j2) {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 4. prepare edit source local_flag to LOCAL_FLAG_NOT_USE, now mLocalFile: [%s] , source ServerId [%s]", str, Long.valueOf(j));
        long updateLocalFlag = iHandler.updateLocalFlag(supportSQLiteDatabase);
        if (updateLocalFlag <= 0) {
            DefaultLogger.e("galleryAction_EditDateTime", "[Edit FileInfo]4. Failed edit Source Local_Flag , SourceCloudId Is [%s]", Long.valueOf(j2));
        } else {
            DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 4. Edit Source LocalFlag Is Success,SourceCloudId Is [%s] , ServerId is [%s]", Long.valueOf(j2), Long.valueOf(j));
            logStepEndTime();
        }
        return updateLocalFlag;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long deleteSource(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, boolean z) throws StoragePermissionMissingException {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 5. prepare delete source File ,file is [%s]", str);
        long deleteSource = iHandler.deleteSource(supportSQLiteDatabase, mediaManager);
        if (deleteSource > 0 && z) {
            DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 5. delete source File is Success");
            logStepEndTime();
        }
        return deleteSource;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long scanNewFile(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str) {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 6. prepare scanner newFile [%s]", str);
        long scanNewFile = iHandler.scanNewFile(supportSQLiteDatabase, mediaManager, str);
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 6. Scanner newFile IsSuccess %s ", str);
        logStepEndTime();
        return scanNewFile;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long deleteAndReScan(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, boolean z) {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 7. scanner sdcard File [%s] is exist,now delete record", str);
        long deleteAndReScan = iHandler.deleteAndReScan(supportSQLiteDatabase, mediaManager);
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 7.again scanner sdcard File [%s],result is [%b]", str, Boolean.valueOf(!z));
        return deleteAndReScan;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long updateCloudInfo(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2, long j3) {
        long updateCloudInfo = iHandler.updateCloudInfo(supportSQLiteDatabase, mediaManager, j, j2);
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 8. Edit newFile LocalImageId ,sourceFile ServerId  Is %s", Long.valueOf(j3));
        return updateCloudInfo;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.ILogWrapper
    public long addFavorite(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2) {
        long addFavorite = iHandler.addFavorite(supportSQLiteDatabase, mediaManager, j, j2);
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] 9. addRemoveFavoritesByPath is Success,update or delete count %d", Long.valueOf(j2));
        return addFavorite;
    }

    public final void logStepEndTime() {
        DefaultLogger.d("galleryAction_EditDateTime", "[Edit FileInfo] step endTime %d", Long.valueOf(System.currentTimeMillis()));
    }
}
