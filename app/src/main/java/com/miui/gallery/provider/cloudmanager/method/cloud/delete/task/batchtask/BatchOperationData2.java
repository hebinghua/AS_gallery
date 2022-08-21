package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask;

import android.database.Cursor;
import android.util.ArrayMap;
import com.miui.gallery.util.BaseMiscUtil;

/* loaded from: classes2.dex */
public class BatchOperationData2<T> {
    public Cursor cursor;
    public ArrayMap<T, BatchItemData2> keyItemDataMap;
    public T[] keys;

    public BatchOperationData2(T[] tArr) {
        this.keys = tArr;
        this.keyItemDataMap = new ArrayMap<>(this.keys.length);
    }

    public void fillResult(long j, boolean z) {
        for (BatchItemData2 batchItemData2 : this.keyItemDataMap.values()) {
            if (z || batchItemData2.result == -1) {
                batchItemData2.result = j;
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
            android.util.ArrayMap<T, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2> r0 = r6.keyItemDataMap
            r1 = 1
            if (r0 == 0) goto L32
            int r0 = r0.size()
            if (r0 > 0) goto Lc
            goto L32
        Lc:
            android.util.ArrayMap<T, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2> r0 = r6.keyItemDataMap
            java.util.Collection r0 = r0.values()
            java.util.Iterator r0 = r0.iterator()
        L16:
            boolean r2 = r0.hasNext()
            if (r2 == 0) goto L32
            java.lang.Object r2 = r0.next()
            com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2 r2 = (com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2) r2
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
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchOperationData2.isAllInvalid():boolean");
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
            BatchItemData2 batchItemData2 = this.keyItemDataMap.get(tArr[i]);
            jArr[i] = batchItemData2 == null ? -102L : batchItemData2.result;
            i++;
        }
    }

    public void release() {
        BaseMiscUtil.closeSilently(this.cursor);
        this.cursor = null;
    }
}
