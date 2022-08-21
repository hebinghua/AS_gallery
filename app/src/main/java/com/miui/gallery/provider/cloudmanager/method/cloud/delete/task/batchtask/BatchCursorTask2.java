package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public abstract class BatchCursorTask2<K> extends BatchTask2<K, BatchOperationData2<K>> {
    public Context mContext;
    public ArrayList<Long> mDirtyBulk;

    public void doMarkAsDirty(long j) {
    }

    public void doMarkAsDirty(Collection<Long> collection) {
    }

    public int getBatchCount() {
        return 100;
    }

    public abstract Cursor queryCursor(SupportSQLiteDatabase supportSQLiteDatabase, K[] kArr);

    public BatchCursorTask2(Context context, ArrayList<Long> arrayList) {
        this.mContext = context;
        this.mDirtyBulk = arrayList;
    }

    public long[] run(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        int i;
        long[] jArr = new long[getTotalCount()];
        LinkedList linkedList = new LinkedList();
        Arrays.fill(jArr, -101L);
        DefaultLogger.d("BatchCursorTask", "%s is running", toString());
        try {
            Bundle bundle = new Bundle();
            int i2 = 0;
            while (i2 < getTotalCount()) {
                int min = Math.min(getTotalCount() - i2, getBatchCount());
                int i3 = i2 + min;
                bundle = getBatchBundle(i2, min, bundle);
                LinkedList linkedList2 = new LinkedList();
                long[] runBatch = runBatch(supportSQLiteDatabase, mediaManager, bundle, linkedList2);
                if (runBatch.length != min) {
                    i = i3;
                    DefaultLogger.e("BatchCursorTask", "%s, Invalid batch result, expecting %d results, but actually is %d", toString(), Integer.valueOf(min), Integer.valueOf(runBatch.length));
                } else {
                    i = i3;
                }
                for (int i4 = 0; i4 < runBatch.length && i4 < min; i4++) {
                    jArr[i2 + i4] = runBatch[i4];
                }
                linkedList.addAll(linkedList2);
                releaseBatchBundle(bundle);
                i2 = i;
            }
            if (!BaseMiscUtil.isValid(linkedList)) {
                return jArr;
            }
            throw new StoragePermissionMissingException(linkedList);
        } finally {
            DefaultLogger.d("BatchCursorTask", "%s finish", toString());
            release();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0085, code lost:
        if (r1 == null) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long[] runBatch(androidx.sqlite.db.SupportSQLiteDatabase r10, com.miui.gallery.provider.cache.MediaManager r11, android.os.Bundle r12, java.util.List<com.miui.gallery.storage.strategies.IStoragePermissionStrategy.PermissionResult> r13) {
        /*
            r9 = this;
            java.lang.String r0 = r9.toString()
            java.lang.String r1 = r9.describeBundle(r12)
            java.lang.String r2 = "BatchCursorTask"
            java.lang.String r3 = "%s run batch for bundle %s"
            com.miui.gallery.util.logger.DefaultLogger.d(r2, r3, r0, r1)
            java.lang.Object[] r0 = r9.mo1234getBatchExecuteKeys(r12)
            int r0 = r0.length
            long[] r0 = new long[r0]
            r1 = 0
            com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchOperationData2 r1 = r9.prepareBatch(r10, r11, r12)     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            r9.verifyBatch(r10, r11, r12, r1)     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            boolean r3 = r1.isAllInvalid()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            if (r3 != 0) goto L2e
            r3 = r9
            r4 = r10
            r5 = r11
            r6 = r12
            r7 = r1
            r8 = r13
            r3.executeBatch(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            goto L76
        L2e:
            android.util.ArrayMap<T, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2> r10 = r1.keyItemDataMap     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            java.util.Set r10 = r10.entrySet()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            java.util.Iterator r10 = r10.iterator()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
        L38:
            boolean r11 = r10.hasNext()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            if (r11 == 0) goto L76
            java.lang.Object r11 = r10.next()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            java.util.Map$Entry r11 = (java.util.Map.Entry) r11     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            java.lang.Object r3 = r11.getValue()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2 r3 = (com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2) r3     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            long r3 = r3.result     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            r5 = -121(0xffffffffffffff87, double:NaN)
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 != 0) goto L38
            java.lang.Object r3 = r11.getValue()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2 r3 = (com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2) r3     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            java.util.List<com.miui.gallery.storage.strategies.IStoragePermissionStrategy$PermissionResult> r3 = r3.permissionResult     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            if (r3 == 0) goto L38
            java.lang.Object r3 = r11.getValue()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2 r3 = (com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2) r3     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            java.util.List<com.miui.gallery.storage.strategies.IStoragePermissionStrategy$PermissionResult> r3 = r3.permissionResult     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            int r3 = r3.size()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            if (r3 <= 0) goto L38
            java.lang.Object r11 = r11.getValue()     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2 r11 = (com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2) r11     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            java.util.List<com.miui.gallery.storage.strategies.IStoragePermissionStrategy$PermissionResult> r11 = r11.permissionResult     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            r13.addAll(r11)     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            goto L38
        L76:
            r1.copyResultsTo(r0)     // Catch: java.lang.Throwable -> L7a java.lang.Exception -> L7c
            goto L87
        L7a:
            r10 = move-exception
            goto L98
        L7c:
            r10 = move-exception
            com.miui.gallery.util.logger.DefaultLogger.w(r2, r10)     // Catch: java.lang.Throwable -> L7a
            r10 = -101(0xffffffffffffff9b, double:NaN)
            java.util.Arrays.fill(r0, r10)     // Catch: java.lang.Throwable -> L7a
            if (r1 == 0) goto L8a
        L87:
            r1.release()
        L8a:
            java.lang.String r10 = r9.toString()
            java.lang.String r11 = r9.describeBundle(r12)
            java.lang.String r12 = "%s done run batch for bundle %s"
            com.miui.gallery.util.logger.DefaultLogger.d(r2, r12, r10, r11)
            return r0
        L98:
            if (r1 == 0) goto L9d
            r1.release()
        L9d:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2.runBatch(androidx.sqlite.db.SupportSQLiteDatabase, com.miui.gallery.provider.cache.MediaManager, android.os.Bundle, java.util.List):long[]");
    }

    public BatchOperationData2<K> prepareBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle) {
        K[] mo1234getBatchExecuteKeys = mo1234getBatchExecuteKeys(bundle);
        BatchOperationData2<K> genBatchOperationData = genBatchOperationData(mo1234getBatchExecuteKeys);
        for (K k : mo1234getBatchExecuteKeys) {
            genBatchOperationData.keyItemDataMap.put(k, genBatchItemData());
        }
        Cursor cursor = null;
        try {
            cursor = queryCursor(supportSQLiteDatabase, mo1234getBatchExecuteKeys);
        } catch (Exception e) {
            DefaultLogger.w("BatchCursorTask", e);
        }
        genBatchOperationData.cursor = cursor;
        return genBatchOperationData;
    }

    public BatchOperationData2<K> genBatchOperationData(K[] kArr) {
        return new BatchOperationData2<>(kArr);
    }

    public BatchItemData2 genBatchItemData() {
        return new BatchItemData2();
    }

    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<K> batchOperationData2) {
        Cursor cursor = batchOperationData2.cursor;
        if (cursor == null) {
            DefaultLogger.d("BatchCursorTask", "cursor for %s is null, abort", toString());
            batchOperationData2.fillResult(-101L);
        } else if (cursor.getCount() <= 0) {
            DefaultLogger.d("BatchCursorTask", "cursor for %s has nothing, abort", toString());
            batchOperationData2.fillResult(-102L);
        } else {
            batchOperationData2.fillResult(-1L);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public void release() {
        this.mContext = null;
    }

    public void releaseBatchBundle(Bundle bundle) {
        if (bundle != null) {
            bundle.clear();
        }
    }

    public ArrayList<Long> getDirtyBulk() {
        return this.mDirtyBulk;
    }

    public final void markAsDirty(long j) {
        this.mDirtyBulk.add(Long.valueOf(j));
        doMarkAsDirty(j);
    }

    public final void markAsDirty(Collection<Long> collection) {
        this.mDirtyBulk.addAll(collection);
        doMarkAsDirty(collection);
    }
}
