package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BatchTaskById2 extends BatchCursorTask2<Long> {
    public long[] mIds;

    public abstract List<IStoragePermissionStrategy.PermissionResult> checkBatchItemPermission(Cursor cursor);

    public long verifyBatchItem(Cursor cursor) {
        return -1L;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public /* bridge */ /* synthetic */ ArrayList getDirtyBulk() {
        return super.getDirtyBulk();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public /* bridge */ /* synthetic */ long[] run(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        return super.run(supportSQLiteDatabase, mediaManager);
    }

    public BatchTaskById2(Context context, ArrayList<Long> arrayList, long[] jArr) {
        super(context, arrayList);
        Numbers.ensurePositive(jArr);
        this.mIds = jArr;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTask2
    public int getTotalCount() {
        return this.mIds.length;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTask2
    public Bundle getBatchBundle(int i, int i2, Bundle bundle) {
        bundle.putLongArray("ids", Arrays.copyOfRange(this.mIds, i, i2 + i));
        return bundle;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTask2
    /* renamed from: getBatchExecuteKeys */
    public Long[] mo1234getBatchExecuteKeys(Bundle bundle) {
        long[] longArray = bundle.getLongArray("ids");
        Long[] lArr = new Long[longArray.length];
        for (int i = 0; i < longArray.length; i++) {
            lArr[i] = Long.valueOf(longArray[i]);
        }
        return lArr;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2) {
        super.verifyBatch(supportSQLiteDatabase, mediaManager, bundle, batchOperationData2);
        if (!batchOperationData2.isAllInvalid()) {
            batchOperationData2.fillResult(-102L);
            bindBatchCursorIndexes(batchOperationData2);
        }
    }

    public void bindBatchCursorIndexes(BatchOperationData2<Long> batchOperationData2) {
        Cursor cursor = batchOperationData2.cursor;
        if (cursor == null || cursor.getCount() <= 0) {
            return;
        }
        Cursor cursor2 = batchOperationData2.cursor;
        for (int i = 0; i < cursor2.getCount(); i++) {
            cursor2.moveToPosition(i);
            BatchItemData2 batchItemData2 = batchOperationData2.keyItemDataMap.get(Long.valueOf(cursor2.getLong(0)));
            if (batchItemData2 != null) {
                batchItemData2.cursorIndex = i;
                List<IStoragePermissionStrategy.PermissionResult> checkBatchItemPermission = checkBatchItemPermission(cursor2);
                batchItemData2.permissionResult = checkBatchItemPermission;
                if (checkBatchItemPermission != null && checkBatchItemPermission.size() > 0) {
                    batchItemData2.result = -121L;
                } else {
                    batchItemData2.result = verifyBatchItem(cursor2);
                }
            }
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTask2
    public String describeBundle(Bundle bundle) {
        if (bundle == null || !bundle.containsKey("ids")) {
            return null;
        }
        return String.format("%s:[%s]", "ids", StringUtils.join(",", bundle.getLongArray("ids")));
    }
}
