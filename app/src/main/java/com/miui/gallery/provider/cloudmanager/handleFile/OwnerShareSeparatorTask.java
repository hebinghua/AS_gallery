package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.BatchCursorTask;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class OwnerShareSeparatorTask extends SubTaskSeparatorTask {
    public abstract long[] executeOwner(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr);

    public abstract long[] executeSharer(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr);

    @Override // com.miui.gallery.provider.cloudmanager.BatchCursorTask
    public Cursor queryCursor(SupportSQLiteDatabase supportSQLiteDatabase, Long[] lArr) {
        return null;
    }

    public OwnerShareSeparatorTask(Context context, ArrayList<Long> arrayList, long[] jArr, String str) {
        super(context, arrayList, jArr, str);
    }

    @Override // com.miui.gallery.provider.cloudmanager.handleFile.SubTaskSeparatorTask
    public int getItemTaskType(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchCursorTask.BatchOperationData<Long> batchOperationData, long j) {
        return ShareMediaManager.isOtherShareMediaId(j) ? 1 : 0;
    }

    @Override // com.miui.gallery.provider.cloudmanager.handleFile.SubTaskSeparatorTask
    public long[] executeType(int i, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr) {
        if (i != 0) {
            if (i == 1) {
                return executeSharer(supportSQLiteDatabase, mediaManager, batchOperationData, toShareImageIds(jArr));
            }
            throw new IllegalArgumentException("Type " + i + " is not supported!");
        }
        return executeOwner(supportSQLiteDatabase, mediaManager, batchOperationData, jArr);
    }

    public static long[] toShareImageIds(long[] jArr) {
        if (jArr == null || jArr.length <= 0) {
            return jArr;
        }
        long[] jArr2 = new long[jArr.length];
        for (int i = 0; i < jArr.length; i++) {
            jArr2[i] = ShareMediaManager.getOriginalMediaId(jArr[i]);
        }
        return jArr2;
    }
}
