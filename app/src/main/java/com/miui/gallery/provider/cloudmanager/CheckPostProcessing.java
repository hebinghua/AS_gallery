package com.miui.gallery.provider.cloudmanager;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.StorageUtils;

/* loaded from: classes2.dex */
public class CheckPostProcessing extends CursorTask {
    public String mFilePath;

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) {
        return -102L;
    }

    public CheckPostProcessing(Context context, String str) {
        super(context, null);
        this.mFilePath = str;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask
    public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (!TextUtils.isEmpty(this.mFilePath)) {
            if (!MIUIStorageConstants.DIRECTORY_CAMERA_PATH.equalsIgnoreCase(StorageUtils.getRelativePath(this.mContext, BaseFileUtils.getParentFolderPath(this.mFilePath)))) {
                return null;
            }
            long mediaStoreId = MediaStoreUtils.getMediaStoreId(this.mFilePath);
            if (mediaStoreId <= 0) {
                return null;
            }
            return this.mContext.getContentResolver().query(PhotosOemApi.getQueryProcessingUri(this.mContext, mediaStoreId), null, null, null, null);
        }
        return null;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase) {
        Cursor cursor = this.mCursor;
        return (cursor == null || cursor.getCount() == 0) ? -1L : -111L;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask
    public String toString() {
        return ".CheckPostProcessing{" + this.mFilePath + "}";
    }
}
