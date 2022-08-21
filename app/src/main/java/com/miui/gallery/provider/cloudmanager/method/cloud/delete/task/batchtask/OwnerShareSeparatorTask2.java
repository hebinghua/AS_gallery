package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class OwnerShareSeparatorTask2 extends SubTaskSeparatorTask2 {
    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2
    public List<IStoragePermissionStrategy.PermissionResult> checkBatchItemPermission(Cursor cursor) {
        return null;
    }

    public abstract long[] executeOwner(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchOperationData2<Long> batchOperationData2, long[] jArr) throws Exception;

    public abstract long[] executeSharer(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchOperationData2<Long> batchOperationData2, long[] jArr) throws Exception;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public Cursor queryCursor(SupportSQLiteDatabase supportSQLiteDatabase, Long[] lArr) {
        return null;
    }

    public OwnerShareSeparatorTask2(Context context, ArrayList<Long> arrayList, long[] jArr) {
        super(context, arrayList, jArr);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.SubTaskSeparatorTask2
    public int getItemTaskType(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2, long j) {
        return ShareMediaManager.isOtherShareMediaId(j) ? 1 : 0;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.SubTaskSeparatorTask2
    public long[] executeType(int i, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchOperationData2<Long> batchOperationData2, long[] jArr) throws Exception {
        if (i != 0) {
            if (i == 1) {
                return executeSharer(supportSQLiteDatabase, mediaManager, batchOperationData2, toShareImageIds(jArr));
            }
            throw new IllegalArgumentException("Type " + i + " is not supported!");
        }
        return executeOwner(supportSQLiteDatabase, mediaManager, batchOperationData2, jArr);
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
