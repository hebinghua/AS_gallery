package com.miui.gallery.provider.cloudmanager.method.cloud.delete;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class CursorTaskWithException {
    public Context mContext;
    public Cursor mCursor;
    public ArrayList<Long> mDirtyBulk;

    public abstract long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) throws Exception;

    public boolean invalidValue(long j) {
        return j != -1;
    }

    public abstract Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase);

    public abstract String toString();

    public abstract long verify(SupportSQLiteDatabase supportSQLiteDatabase);

    public CursorTaskWithException(Context context, ArrayList<Long> arrayList) {
        this.mContext = context;
        this.mDirtyBulk = arrayList;
    }

    public final long run(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        DefaultLogger.d("CursorTaskWithException", "%s is running", toString());
        this.mCursor = prepare(supportSQLiteDatabase);
        try {
            long verify = verify(supportSQLiteDatabase);
            return invalidValue(verify) ? verify : execute(supportSQLiteDatabase, mediaManager, verify);
        } finally {
            DefaultLogger.d("CursorTaskWithException", "%s finish", toString());
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

    public ArrayList<Long> getDirtyBulk() {
        return this.mDirtyBulk;
    }
}
