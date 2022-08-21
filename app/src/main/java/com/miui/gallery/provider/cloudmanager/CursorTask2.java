package com.miui.gallery.provider.cloudmanager;

import android.content.Context;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class CursorTask2 {
    public final Context mContext;
    public final ArrayList<Long> mDirtyBulk;
    public long mValidation;

    public boolean checkValidation(long j) {
        return j == -1;
    }

    public abstract void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager);

    public abstract long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception;

    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
    }

    public void release() {
    }

    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        return -1L;
    }

    public CursorTask2(Context context, ArrayList<Long> arrayList) {
        this.mContext = context;
        this.mDirtyBulk = arrayList;
    }

    public final Long prepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        Long l;
        DefaultLogger.d("CursorTask", "%s is preparing", toString());
        try {
            long verify = verify(supportSQLiteDatabase, mediaManager);
            this.mValidation = verify;
            if (!checkValidation(verify)) {
                l = Long.valueOf(this.mValidation);
            } else {
                doPrepare(supportSQLiteDatabase, mediaManager);
                postPrepare(supportSQLiteDatabase, mediaManager);
                l = null;
            }
            return l;
        } finally {
            DefaultLogger.d("CursorTask", "%s is preparing done", toString());
        }
    }

    public final long run(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        long execute;
        DefaultLogger.d("CursorTask", "%s is running", toString());
        try {
            Long prepare = prepare(supportSQLiteDatabase, mediaManager);
            if (prepare != null) {
                execute = prepare.longValue();
            } else {
                execute = execute(supportSQLiteDatabase, mediaManager);
            }
            return execute;
        } finally {
            release();
            DefaultLogger.d("CursorTask", "%s finish", toString());
        }
    }

    public ArrayList<Long> getDirtyBulk() {
        return this.mDirtyBulk;
    }

    public final void markAsDirty(long j) {
        this.mDirtyBulk.add(Long.valueOf(j));
    }
}
