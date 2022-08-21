package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.BatchCursorTask;
import com.miui.gallery.provider.cloudmanager.BatchTaskById;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class SubTaskSeparatorTask extends BatchTaskById {
    public abstract long[] executeType(int i, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr);

    public abstract int getItemTaskType(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchCursorTask.BatchOperationData<Long> batchOperationData, long j);

    public SubTaskSeparatorTask(Context context, ArrayList<Long> arrayList, long[] jArr, String str) {
        super(context, arrayList, jArr, str);
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchTask
    public void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchCursorTask.BatchOperationData<Long> batchOperationData) {
        OperationData operationData = (OperationData) batchOperationData;
        for (int i = 0; i < operationData.typeIdsArrays.size(); i++) {
            Pair<Integer, ArrayList<Long>> pair = operationData.typeIdsArrays.get(i);
            Object obj = pair.second;
            if (obj != null && ((ArrayList) obj).size() > 0) {
                DefaultLogger.d("SubTaskSeparatorTask", "[%s] Start execute type %d for ids [%s]", toString(), pair.first, TextUtils.join(",", (Iterable) pair.second));
                long[] executeType = executeType(((Integer) pair.first).intValue(), supportSQLiteDatabase, mediaManager, batchOperationData, Numbers.toArray((List) pair.second));
                DefaultLogger.d("SubTaskSeparatorTask", "[%s] Result [%s]", toString(), StringUtils.join(",", executeType));
                applyResult(batchOperationData, executeType, (ArrayList) pair.second);
            }
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchTaskById, com.miui.gallery.provider.cloudmanager.BatchCursorTask
    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchCursorTask.BatchOperationData<Long> batchOperationData) {
        int itemTaskType;
        super.verifyBatch(supportSQLiteDatabase, mediaManager, bundle, batchOperationData);
        OperationData operationData = (OperationData) batchOperationData;
        for (Long l : (Long[]) operationData.keys) {
            long longValue = l.longValue();
            BatchCursorTask.BatchItemData batchItemData = batchOperationData.keyItemDataMap.get(Long.valueOf(longValue));
            if (batchItemData != null && (itemTaskType = getItemTaskType(supportSQLiteDatabase, mediaManager, bundle, batchOperationData, longValue)) != -1) {
                operationData.putItemToType(longValue, itemTaskType);
                batchItemData.result = -1L;
            }
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchCursorTask
    public BatchCursorTask.BatchOperationData<Long> genBatchOperationData(Long[] lArr) {
        return new OperationData(lArr);
    }

    public final void applyResult(BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr, ArrayList<Long> arrayList) {
        for (int i = 0; i < jArr.length; i++) {
            BatchCursorTask.BatchItemData batchItemData = batchOperationData.keyItemDataMap.get(arrayList.get(i));
            if (batchItemData != null) {
                batchItemData.result = jArr[i];
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class OperationData extends BatchCursorTask.BatchOperationData<Long> {
        public ArrayList<Pair<Integer, ArrayList<Long>>> typeIdsArrays;

        public OperationData(Long[] lArr) {
            super(lArr);
            this.typeIdsArrays = new ArrayList<>();
        }

        public void putItemToType(long j, int i) {
            ArrayList<Pair<Integer, ArrayList<Long>>> arrayList;
            Pair<Integer, ArrayList<Long>> pair;
            String str;
            if (this.typeIdsArrays.size() <= 0) {
                pair = null;
            } else {
                pair = this.typeIdsArrays.get(arrayList.size() - 1);
            }
            if (pair == null || ((Integer) pair.first).intValue() != i) {
                Integer valueOf = Integer.valueOf(i);
                if (pair == null) {
                    str = "null";
                } else {
                    str = pair.first + ", count:" + ((ArrayList) pair.second).size();
                }
                DefaultLogger.d("SubTaskSeparatorTask", "New type group of [%d] is created! Last group is [%s]", valueOf, str);
                pair = new Pair<>(Integer.valueOf(i), new ArrayList());
                this.typeIdsArrays.add(pair);
            }
            ((ArrayList) pair.second).add(Long.valueOf(j));
        }
    }
}
