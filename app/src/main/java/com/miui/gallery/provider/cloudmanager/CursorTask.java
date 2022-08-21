package com.miui.gallery.provider.cloudmanager;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class CursorTask {
    public Context mContext;
    public Cursor mCursor;
    public ArrayList<Long> mDirtyBulk;

    public abstract long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) throws StoragePermissionMissingException;

    public boolean invalidValue(long j) {
        return j != -1;
    }

    public abstract Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase);

    public abstract String toString();

    public CursorTask(Context context, ArrayList<Long> arrayList) {
        this.mContext = context;
        this.mDirtyBulk = arrayList;
    }

    public long verify(SupportSQLiteDatabase supportSQLiteDatabase) throws StoragePermissionMissingException {
        Cursor cursor = this.mCursor;
        if (cursor == null) {
            DefaultLogger.d("CursorTask", "cursor for %s is null, abort", toString());
            return -101L;
        } else if (cursor.moveToFirst()) {
            return -1L;
        } else {
            DefaultLogger.d("CursorTask", "cursor for %s has nothing, abort", toString());
            return -102L;
        }
    }

    public final long run(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        DefaultLogger.d("CursorTask", "%s is running", toString());
        this.mCursor = prepare(supportSQLiteDatabase);
        try {
            long verify = verify(supportSQLiteDatabase);
            return invalidValue(verify) ? verify : execute(supportSQLiteDatabase, mediaManager, verify);
        } finally {
            DefaultLogger.d("CursorTask", "%s finish", toString());
            release();
        }
    }

    public final void release() {
        this.mContext = null;
        Cursor cursor = this.mCursor;
        if (cursor != null) {
            cursor.close();
        }
        this.mCursor = null;
    }
}
