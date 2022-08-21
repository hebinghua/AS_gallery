package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.io.File;

/* loaded from: classes2.dex */
public interface ILogWrapper {
    long addFavorite(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2);

    long deleteAndReScan(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, boolean z);

    long deleteSource(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, boolean z) throws StoragePermissionMissingException;

    long doExecute(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager);

    void logNewFileError();

    long queryAlbumAttributes(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase);

    long scanNewFile(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str);

    boolean setLastModified(IHandler iHandler);

    long updateCloudInfo(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j, long j2, long j3);

    boolean updateFile(IHandler iHandler, File file, boolean z);

    long updateLocalFlag(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, String str, long j, long j2);

    long verify(IHandler iHandler, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, long j);
}
