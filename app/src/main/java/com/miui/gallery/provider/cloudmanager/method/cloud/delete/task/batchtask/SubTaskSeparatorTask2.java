package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class SubTaskSeparatorTask2 extends BatchTaskById2 {
    public abstract long[] executeType(int i, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchOperationData2<Long> batchOperationData2, long[] jArr) throws Exception;

    public abstract int getItemTaskType(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2, long j);

    public SubTaskSeparatorTask2(Context context, ArrayList<Long> arrayList, long[] jArr) {
        super(context, arrayList, jArr);
    }

    public void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2, List<IStoragePermissionStrategy.PermissionResult> list) {
        OperationData operationData = (OperationData) batchOperationData2;
        for (int i = 0; i < operationData.typeIdsArrays.size(); i++) {
            Pair<Integer, ArrayList<Long>> pair = operationData.typeIdsArrays.get(i);
            Object obj = pair.second;
            if (obj != null && ((ArrayList) obj).size() > 0) {
                DefaultLogger.d("SubTaskSeparatorTask", "[%s] Start execute type %d for ids [%s]", toString(), pair.first, TextUtils.join(",", (Iterable) pair.second));
                try {
                    long[] executeType = executeType(((Integer) pair.first).intValue(), supportSQLiteDatabase, mediaManager, batchOperationData2, Numbers.toArray((List) pair.second));
                    DefaultLogger.d("SubTaskSeparatorTask", "[%s] Result [%s]", toString(), StringUtils.join(",", executeType));
                    applyResult(batchOperationData2, executeType, (ArrayList) pair.second);
                } catch (StoragePermissionMissingException e) {
                    list.addAll(e.getPermissionResultList());
                    long[] jArr = new long[((ArrayList) pair.second).size()];
                    Arrays.fill(jArr, -121L);
                    applyResult(batchOperationData2, jArr, (ArrayList) pair.second);
                } catch (Exception e2) {
                    DefaultLogger.e("SubTaskSeparatorTask", "executeBatch error %s", e2);
                }
            }
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2) {
        int itemTaskType;
        super.verifyBatch(supportSQLiteDatabase, mediaManager, bundle, batchOperationData2);
        OperationData operationData = (OperationData) batchOperationData2;
        for (Long l : (Long[]) operationData.keys) {
            long longValue = l.longValue();
            BatchItemData2 batchItemData2 = batchOperationData2.keyItemDataMap.get(Long.valueOf(longValue));
            if (batchItemData2 != null && (itemTaskType = getItemTaskType(supportSQLiteDatabase, mediaManager, bundle, batchOperationData2, longValue)) != -1) {
                operationData.putItemToType(longValue, itemTaskType);
                batchItemData2.result = -1L;
            }
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public BatchOperationData2<Long> genBatchOperationData(Long[] lArr) {
        return new OperationData(lArr);
    }

    public final void applyResult(BatchOperationData2<Long> batchOperationData2, long[] jArr, ArrayList<Long> arrayList) {
        for (int i = 0; i < jArr.length; i++) {
            BatchItemData2 batchItemData2 = batchOperationData2.keyItemDataMap.get(arrayList.get(i));
            if (batchItemData2 != null) {
                batchItemData2.result = jArr[i];
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class OperationData extends BatchOperationData2<Long> {
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
