package com.miui.gallery.provider.cloudmanager;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.BatchCursorTask;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes2.dex */
public abstract class BatchTaskById extends BatchCursorTask<Long> {
    public long[] mIds;
    public String mInvokerTag;

    public long verifyBatchItem(Cursor cursor) {
        return -1L;
    }

    public BatchTaskById(Context context, ArrayList<Long> arrayList, long[] jArr, String str) {
        super(context, arrayList);
        Numbers.ensurePositive(jArr);
        this.mIds = jArr;
        this.mInvokerTag = str;
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchTask
    public int getTotalCount() {
        return this.mIds.length;
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchTask
    public Bundle getBatchBundle(int i, int i2, Bundle bundle) {
        bundle.putLongArray("ids", Arrays.copyOfRange(this.mIds, i, i2 + i));
        bundle.putString("invokerTag", this.mInvokerTag);
        return bundle;
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchTask
    /* renamed from: getBatchExecuteKeys */
    public Long[] mo1228getBatchExecuteKeys(Bundle bundle) {
        long[] longArray = bundle.getLongArray("ids");
        Long[] lArr = new Long[longArray.length];
        for (int i = 0; i < longArray.length; i++) {
            lArr[i] = Long.valueOf(longArray[i]);
        }
        return lArr;
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchCursorTask
    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchCursorTask.BatchOperationData<Long> batchOperationData) {
        super.verifyBatch(supportSQLiteDatabase, mediaManager, bundle, batchOperationData);
        if (!batchOperationData.isAllInvalid()) {
            batchOperationData.fillResult(-102L);
            bindBatchCursorIndexes(batchOperationData);
        }
    }

    public void bindBatchCursorIndexes(BatchCursorTask.BatchOperationData<Long> batchOperationData) {
        Cursor cursor = batchOperationData.cursor;
        if (cursor == null || cursor.getCount() <= 0) {
            return;
        }
        Cursor cursor2 = batchOperationData.cursor;
        for (int i = 0; i < cursor2.getCount(); i++) {
            cursor2.moveToPosition(i);
            BatchCursorTask.BatchItemData batchItemData = batchOperationData.keyItemDataMap.get(Long.valueOf(cursor2.getLong(0)));
            if (batchItemData != null) {
                batchItemData.cursorIndex = i;
                batchItemData.result = verifyBatchItem(cursor2);
            }
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchTask
    public String describeBundle(Bundle bundle) {
        if (bundle == null || !bundle.containsKey("ids")) {
            return null;
        }
        return String.format("%s:[%s]", "ids", StringUtils.join(",", bundle.getLongArray("ids")));
    }
}
