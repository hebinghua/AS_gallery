package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud;

import android.content.Context;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchOperationData2;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.OwnerShareSeparatorTask2;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.logger.TimingTracing;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class Delete extends OwnerShareSeparatorTask2 {
    public final String TRACE_TAG;
    public int mDeleteReason;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTask2
    public /* bridge */ /* synthetic */ void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, Object obj, List list) {
        executeBatch(supportSQLiteDatabase, mediaManager, bundle, (BatchOperationData2) obj, (List<IStoragePermissionStrategy.PermissionResult>) list);
    }

    public Delete(Context context, ArrayList<Long> arrayList, long[] jArr, int i) {
        super(context, arrayList, jArr);
        this.mDeleteReason = i;
        this.TRACE_TAG = String.format("%s{%s}", "galleryAction_Delete", Long.valueOf(Thread.currentThread().getId()));
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public long[] run(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        TimingTracing.beginTracing(this.TRACE_TAG, String.format(Locale.US, "count{%s}, reason{%s}", Integer.valueOf(getTotalCount()), Integer.valueOf(this.mDeleteReason)));
        try {
            return super.run(supportSQLiteDatabase, mediaManager);
        } finally {
            TimingTracing.stopTracing(null);
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public BatchOperationData2<Long> prepareBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle) {
        try {
            return super.prepareBatch(supportSQLiteDatabase, mediaManager, bundle);
        } finally {
            TimingTracing.addSplit("prepareBatch");
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.SubTaskSeparatorTask2, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2) {
        try {
            super.verifyBatch(supportSQLiteDatabase, mediaManager, bundle, batchOperationData2);
        } finally {
            TimingTracing.addSplit("verifyBatch");
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.SubTaskSeparatorTask2
    public void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2, List<IStoragePermissionStrategy.PermissionResult> list) {
        try {
            super.executeBatch(supportSQLiteDatabase, mediaManager, bundle, batchOperationData2, list);
        } finally {
            TimingTracing.addSplit("executeBatch");
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.OwnerShareSeparatorTask2
    public long[] executeOwner(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchOperationData2<Long> batchOperationData2, long[] jArr) throws StoragePermissionMissingException {
        return new DeleteOwner(getContext(), getDirtyBulk(), jArr, this.mDeleteReason).run(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.OwnerShareSeparatorTask2
    public long[] executeSharer(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchOperationData2<Long> batchOperationData2, long[] jArr) throws StoragePermissionMissingException {
        return new DeleteShare(getContext(), getDirtyBulk(), jArr, this.mDeleteReason).run(supportSQLiteDatabase, mediaManager);
    }
}
