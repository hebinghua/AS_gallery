package com.miui.gallery.provider.cloudmanager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.ArrayMap;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes2.dex */
public abstract class BatchCursorTask<K> extends BatchTask<K, BatchOperationData<K>> {
    public Context mContext;
    public ArrayList<Long> mDirtyBulk;

    /* loaded from: classes2.dex */
    public static class BatchItemData {
        public int cursorIndex = -1;
        public long result = -1;
    }

    public int getBatchCount() {
        return 100;
    }

    public abstract Cursor queryCursor(SupportSQLiteDatabase supportSQLiteDatabase, K[] kArr);

    public BatchCursorTask(Context context, ArrayList<Long> arrayList) {
        this.mContext = context;
        this.mDirtyBulk = arrayList;
    }

    public long[] run(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        long[] jArr = new long[getTotalCount()];
        Arrays.fill(jArr, -101L);
        DefaultLogger.d("CloudManager.BatchCursorTask", "%s is running", toString());
        try {
            Bundle bundle = new Bundle();
            int i = 0;
            while (i < getTotalCount()) {
                int min = Math.min(getTotalCount() - i, getBatchCount());
                int i2 = i + min;
                bundle = getBatchBundle(i, min, bundle);
                long[] runBatch = runBatch(supportSQLiteDatabase, mediaManager, bundle);
                if (runBatch.length != min) {
                    DefaultLogger.e("CloudManager.BatchCursorTask", "%s, Invalid batch result, expecting %d results, but actually is %d", toString(), Integer.valueOf(min), Integer.valueOf(runBatch.length));
                }
                for (int i3 = 0; i3 < runBatch.length && i3 < min; i3++) {
                    jArr[i + i3] = runBatch[i3];
                }
                releaseBatchBundle(bundle);
                i = i2;
            }
            return jArr;
        } finally {
            DefaultLogger.d("CloudManager.BatchCursorTask", "%s finish", toString());
            release();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0036, code lost:
        if (r1 == null) goto L8;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public long[] runBatch(androidx.sqlite.db.SupportSQLiteDatabase r5, com.miui.gallery.provider.cache.MediaManager r6, android.os.Bundle r7) {
        /*
            r4 = this;
            java.lang.String r0 = r4.toString()
            java.lang.String r1 = r4.describeBundle(r7)
            java.lang.String r2 = "CloudManager.BatchCursorTask"
            java.lang.String r3 = "%s run batch for bundle %s"
            com.miui.gallery.util.logger.DefaultLogger.d(r2, r3, r0, r1)
            java.lang.Object[] r0 = r4.mo1228getBatchExecuteKeys(r7)
            int r0 = r0.length
            long[] r0 = new long[r0]
            r1 = 0
            com.miui.gallery.provider.cloudmanager.BatchCursorTask$BatchOperationData r1 = r4.prepareBatch(r5, r6, r7)     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            r4.verifyBatch(r5, r6, r7, r1)     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            boolean r3 = r1.isAllInvalid()     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            if (r3 != 0) goto L27
            r4.executeBatch(r5, r6, r7, r1)     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
        L27:
            r1.copyResultsTo(r0)     // Catch: java.lang.Throwable -> L2b java.lang.Exception -> L2d
            goto L38
        L2b:
            r5 = move-exception
            goto L49
        L2d:
            r5 = move-exception
            com.miui.gallery.util.logger.DefaultLogger.w(r2, r5)     // Catch: java.lang.Throwable -> L2b
            r5 = -101(0xffffffffffffff9b, double:NaN)
            java.util.Arrays.fill(r0, r5)     // Catch: java.lang.Throwable -> L2b
            if (r1 == 0) goto L3b
        L38:
            r1.release()
        L3b:
            java.lang.String r5 = r4.toString()
            java.lang.String r6 = r4.describeBundle(r7)
            java.lang.String r7 = "%s done run batch for bundle %s"
            com.miui.gallery.util.logger.DefaultLogger.d(r2, r7, r5, r6)
            return r0
        L49:
            if (r1 == 0) goto L4e
            r1.release()
        L4e:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.BatchCursorTask.runBatch(androidx.sqlite.db.SupportSQLiteDatabase, com.miui.gallery.provider.cache.MediaManager, android.os.Bundle):long[]");
    }

    public BatchOperationData<K> prepareBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle) {
        K[] mo1228getBatchExecuteKeys = mo1228getBatchExecuteKeys(bundle);
        BatchOperationData<K> genBatchOperationData = genBatchOperationData(mo1228getBatchExecuteKeys);
        for (K k : mo1228getBatchExecuteKeys) {
            genBatchOperationData.keyItemDataMap.put(k, genBatchItemData());
        }
        Cursor cursor = null;
        try {
            cursor = queryCursor(supportSQLiteDatabase, mo1228getBatchExecuteKeys);
        } catch (Exception e) {
            DefaultLogger.w("CloudManager.BatchCursorTask", e);
        }
        genBatchOperationData.cursor = cursor;
        return genBatchOperationData;
    }

    public BatchOperationData<K> genBatchOperationData(K[] kArr) {
        return new BatchOperationData<>(kArr);
    }

    public BatchItemData genBatchItemData() {
        return new BatchItemData();
    }

    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData<K> batchOperationData) {
        Cursor cursor = batchOperationData.cursor;
        if (cursor == null) {
            DefaultLogger.d("CloudManager.BatchCursorTask", "cursor for %s is null, abort", toString());
            batchOperationData.fillResult(-101L);
        } else if (cursor.getCount() <= 0) {
            DefaultLogger.d("CloudManager.BatchCursorTask", "cursor for %s has nothing, abort", toString());
            batchOperationData.fillResult(-102L);
        } else {
            batchOperationData.fillResult(-1L);
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

    /* loaded from: classes2.dex */
    public static class BatchOperationData<T> {
        public Cursor cursor;
        public ArrayMap<T, BatchItemData> keyItemDataMap;
        public T[] keys;

        public BatchOperationData(T[] tArr) {
            this.keys = tArr;
            this.keyItemDataMap = new ArrayMap<>(this.keys.length);
        }

        public void fillResult(long j, boolean z) {
            for (BatchItemData batchItemData : this.keyItemDataMap.values()) {
                if (z || batchItemData.result == -1) {
                    batchItemData.result = j;
                }
            }
        }

        public void fillResult(long j) {
            fillResult(j, false);
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x001c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean isAllInvalid() {
            /*
                r6 = this;
                android.util.ArrayMap<T, com.miui.gallery.provider.cloudmanager.BatchCursorTask$BatchItemData> r0 = r6.keyItemDataMap
                r1 = 1
                if (r0 == 0) goto L32
                int r0 = r0.size()
                if (r0 > 0) goto Lc
                goto L32
            Lc:
                android.util.ArrayMap<T, com.miui.gallery.provider.cloudmanager.BatchCursorTask$BatchItemData> r0 = r6.keyItemDataMap
                java.util.Collection r0 = r0.values()
                java.util.Iterator r0 = r0.iterator()
            L16:
                boolean r2 = r0.hasNext()
                if (r2 == 0) goto L32
                java.lang.Object r2 = r0.next()
                com.miui.gallery.provider.cloudmanager.BatchCursorTask$BatchItemData r2 = (com.miui.gallery.provider.cloudmanager.BatchCursorTask.BatchItemData) r2
                long r2 = r2.result
                r4 = -1
                int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r4 == 0) goto L30
                r4 = 0
                int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r2 < 0) goto L16
            L30:
                r0 = 0
                return r0
            L32:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.BatchCursorTask.BatchOperationData.isAllInvalid():boolean");
        }

        public void copyResultsTo(long[] jArr) {
            if (this.keyItemDataMap.size() != jArr.length) {
                throw new IllegalArgumentException("Expect results length to be " + this.keyItemDataMap.size() + " instead of " + jArr.length);
            }
            int i = 0;
            while (true) {
                T[] tArr = this.keys;
                if (i >= tArr.length) {
                    return;
                }
                BatchItemData batchItemData = this.keyItemDataMap.get(tArr[i]);
                jArr[i] = batchItemData == null ? -102L : batchItemData.result;
                i++;
            }
        }

        public void release() {
            BaseMiscUtil.closeSilently(this.cursor);
            this.cursor = null;
        }
    }
}
