package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.BatchCursorTask;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.handleFile.MediaFileHandleJob;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class OwnerFileHandleTask extends SubTaskSeparatorTask {
    public OwnerFileHandleTask(Context context, ArrayList<Long> arrayList, long[] jArr, String str) {
        super(context, arrayList, jArr, str);
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchCursorTask
    public Cursor queryCursor(SupportSQLiteDatabase supportSQLiteDatabase, Long[] lArr) {
        return getContext().getContentResolver().query(GalleryContract.Cloud.CLOUD_URI, Contracts.PROJECTION, String.format("%s IN (%s)", j.c, TextUtils.join(",", lArr)), null, null);
    }

    @Override // com.miui.gallery.provider.cloudmanager.handleFile.SubTaskSeparatorTask
    public int getItemTaskType(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchCursorTask.BatchOperationData<Long> batchOperationData, long j) {
        Cursor cursor;
        int i;
        BatchCursorTask.BatchItemData batchItemData = batchOperationData.keyItemDataMap.get(Long.valueOf(j));
        if (batchItemData != null && (cursor = batchOperationData.cursor) != null && (i = batchItemData.cursorIndex) >= 0) {
            cursor.moveToPosition(i);
            if (batchOperationData.cursor.getInt(2) == 0) {
                return -1;
            }
            if (batchOperationData.cursor.getInt(5) != 0) {
                int i2 = batchOperationData.cursor.getInt(2);
                return (i2 == -1 || i2 == 2) ? 12 : 11;
            }
            DefaultLogger.e("galleryAction_FileHandle_OwnerFileHandleTask", "error call:%s", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
        }
        return -1;
    }

    @Override // com.miui.gallery.provider.cloudmanager.handleFile.SubTaskSeparatorTask
    public long[] executeType(int i, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr) {
        ContentResolver contentResolver = getContext().getContentResolver();
        if (i != 11) {
            if (i == 12) {
                return batchExecuteDeleteMedia(contentResolver, batchOperationData, jArr);
            }
            throw new IllegalArgumentException("Type " + i + " is not supported!");
        }
        return batchExecuteOtherMediaOperations(contentResolver, batchOperationData, jArr);
    }

    public final long[] batchExecuteOtherMediaOperations(ContentResolver contentResolver, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr) {
        int i;
        DefaultLogger.d("galleryAction_FileHandle_OwnerFileHandleTask", "batchExecute[MediaOperation] =>");
        long[] jArr2 = new long[jArr.length];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            BatchCursorTask.BatchItemData batchItemData = batchOperationData.keyItemDataMap.get(Long.valueOf(jArr[i2]));
            if (batchItemData != null && batchItemData.result == -1 && (i = batchItemData.cursorIndex) >= 0) {
                batchOperationData.cursor.moveToPosition(i);
                jArr2[i2] = new MediaFileHandleJob.Builder().setParams(contentResolver, batchOperationData.cursor, jArr[i2], 40, this.mInvokerTag).build().handle(getContext()) ? 1L : 0L;
            }
        }
        return jArr2;
    }

    public final long[] batchExecuteDeleteMedia(ContentResolver contentResolver, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr) {
        int i;
        DefaultLogger.d("galleryAction_FileHandle_OwnerFileHandleTask", "batchExecute[MediaDelete] =>");
        long[] jArr2 = new long[jArr.length];
        for (int i2 = 0; i2 < jArr.length; i2++) {
            BatchCursorTask.BatchItemData batchItemData = batchOperationData.keyItemDataMap.get(Long.valueOf(jArr[i2]));
            if (batchItemData != null && batchItemData.result == -1 && (i = batchItemData.cursorIndex) >= 0) {
                batchOperationData.cursor.moveToPosition(i);
                jArr2[i2] = new MediaFileHandleJob.Builder().setParams(contentResolver, batchOperationData.cursor, jArr[i2], 40, this.mInvokerTag).build().handle(getContext()) ? 1L : 0L;
            }
        }
        return jArr2;
    }
}
