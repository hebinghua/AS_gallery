package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;

/* loaded from: classes2.dex */
public interface IHandler {
    long addFavorite(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2);

    long deleteAndReScan(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager);

    long deleteSource(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException;

    long doExecute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException;

    long doVerify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager);

    long queryAlbumAttributes(SupportSQLiteDatabase supportSQLiteDatabase);

    long scanNewFile(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str);

    boolean setLastModified();

    long updateCloudInfo(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2);

    boolean updateFile();

    long updateLocalFlag(SupportSQLiteDatabase supportSQLiteDatabase);
}
