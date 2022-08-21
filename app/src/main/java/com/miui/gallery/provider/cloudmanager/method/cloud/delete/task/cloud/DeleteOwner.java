package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.StringBuilderPrinter;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchOperationData2;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class DeleteOwner extends DeleteCloudBase {
    public final String TRACE_TAG;
    public boolean mNotDeleteOriginFlag;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.DeleteCloudBase
    public String getTableName() {
        return "cloud";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTask2
    public /* bridge */ /* synthetic */ void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, Object obj, List list) {
        executeBatch(supportSQLiteDatabase, mediaManager, bundle, (BatchOperationData2) obj, (List<IStoragePermissionStrategy.PermissionResult>) list);
    }

    public DeleteOwner(Context context, ArrayList<Long> arrayList, long[] jArr, int i) {
        super(context, arrayList, jArr, i);
        this.TRACE_TAG = String.format("%s{%s}", "galleryAction_DeleteOwner", Long.valueOf(Thread.currentThread().getId()));
    }

    public DeleteOwner(Context context, ArrayList<Long> arrayList, long[] jArr, boolean z, int i) {
        super(context, arrayList, jArr, i);
        this.mNotDeleteOriginFlag = z;
        this.TRACE_TAG = String.format("%s{%s}", "galleryAction_DeleteOwner", Long.valueOf(Thread.currentThread().getId()));
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

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2, com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public void verifyBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2) {
        try {
            super.verifyBatch(supportSQLiteDatabase, mediaManager, bundle, batchOperationData2);
        } finally {
            TimingTracing.addSplit("verifyBatch");
        }
    }

    public void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2, List<IStoragePermissionStrategy.PermissionResult> list) {
        TimingTracing.beginTracing(String.format("DeleteOwner.executeBatch{%s}", Long.valueOf(Thread.currentThread().getId())), String.format("count{%s}", Integer.valueOf(batchOperationData2.keyItemDataMap.size())));
        supportSQLiteDatabase.beginTransaction();
        TimingTracing.addSplit("beginTransaction");
        try {
            try {
                ArrayList arrayList = new ArrayList();
                final ArrayList arrayList2 = new ArrayList();
                for (Map.Entry<Long, BatchItemData2> entry : batchOperationData2.keyItemDataMap.entrySet()) {
                    if (entry.getValue().result == -1 && entry.getValue().cursorIndex >= 0) {
                        batchOperationData2.cursor.moveToPosition(entry.getValue().cursorIndex);
                        Cursor cursor = batchOperationData2.cursor;
                        String string = cursor.getString(cursor.getColumnIndex("localFile"));
                        if (TextUtils.isEmpty(string)) {
                            Cursor cursor2 = batchOperationData2.cursor;
                            string = cursor2.getString(cursor2.getColumnIndex("thumbnailFile"));
                        }
                        Cursor cursor3 = batchOperationData2.cursor;
                        cursor3.getLong(cursor3.getColumnIndex("localGroupId"));
                        ContentValues contentValues = new ContentValues();
                        if (this.mNotDeleteOriginFlag) {
                            contentValues.put("localFile", "");
                            DefaultLogger.d("galleryAction_DeleteOwner", "DELETE ITEM: not delete origin file: %d", entry.getKey());
                        }
                        if (batchOperationData2.cursor.isNull(4)) {
                            DefaultLogger.d("galleryAction_DeleteOwner", "DELETE ITEM: no server id found, update to invalid record: %d", entry.getKey());
                            contentValues.put("localFlag", (Integer) (-1));
                        } else {
                            DefaultLogger.d("galleryAction_DeleteOwner", "DELETE ITEM: server id found, mark delete %d", entry.getKey());
                            contentValues.put("localFlag", (Integer) 2);
                        }
                        arrayList2.add(entry.getKey());
                        entry.getValue().result = supportSQLiteDatabase.update("cloud", 0, contentValues, "_id = ? ", new String[]{entry.getKey().toString()});
                        TimingTracing.addSplit("update");
                        RemarkManager.remarkMediaId(RemarkInfoFactory.createDeleteRemarkInfo(entry.getKey().longValue(), string, null));
                        DeleteRecord createDeleteRecord = Util.createDeleteRecord(this.mDeleteReason, batchOperationData2.cursor, "galleryAction_DeleteOwner");
                        if (createDeleteRecord != null) {
                            arrayList.add(createDeleteRecord);
                        }
                    }
                    if (entry.getValue().result == -121 && entry.getValue().permissionResult != null && entry.getValue().permissionResult.size() > 0) {
                        list.addAll(entry.getValue().permissionResult);
                    }
                }
                supportSQLiteDatabase.setTransactionSuccessful();
                TimingTracing.addSplit("setTransactionSuccessful");
                if (BaseMiscUtil.isValid(arrayList)) {
                    DeleteRecorder.getInstance().record((DeleteRecord[]) arrayList.toArray(new DeleteRecord[0]));
                    TimingTracing.addSplit("deleteRecords");
                }
                if (BaseMiscUtil.isValid(arrayList2)) {
                    ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.DeleteOwner.1
                        @Override // java.lang.Runnable
                        public void run() {
                            MediaFeatureManager.getInstance().onImageBatchDelete(arrayList2);
                        }
                    });
                }
                supportSQLiteDatabase.endTransaction();
                TimingTracing.addSplit("endTransaction");
                ArrayList arrayList3 = new ArrayList();
                for (Map.Entry<Long, BatchItemData2> entry2 : batchOperationData2.keyItemDataMap.entrySet()) {
                    if (entry2.getValue().result > 0) {
                        arrayList3.add(entry2.getKey());
                    }
                }
                if (arrayList3.size() > 0) {
                    mediaManager.delete(String.format("%s IN (%s)", j.c, TextUtils.join(",", arrayList3)), null);
                    markAsDirty(arrayList3);
                }
                TimingTracing.addSplit("managerDelete");
                StringBuilder sb = new StringBuilder();
                long stopTracing = TimingTracing.stopTracing(new StringBuilderPrinter(sb));
                if (batchOperationData2.keyItemDataMap.size() <= 0 || stopTracing <= batchOperationData2.keyItemDataMap.size() * 100) {
                    return;
                }
                DefaultLogger.w("galleryAction_DeleteOwner", "delete slowly: %s", sb.toString());
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", String.valueOf(stopTracing / batchOperationData2.keyItemDataMap.size()));
                hashMap.put(CallMethod.ARG_EXTRA_STRING, sb.toString());
                SamplingStatHelper.recordCountEvent("delete_performance", "galleryAction_DeleteOwner", hashMap);
            } catch (Exception unused) {
                batchOperationData2.fillResult(-110L);
                supportSQLiteDatabase.endTransaction();
                TimingTracing.addSplit("endTransaction");
            }
        } catch (Throwable th) {
            supportSQLiteDatabase.endTransaction();
            TimingTracing.addSplit("endTransaction");
            throw th;
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2
    public long verifyBatchItem(Cursor cursor) {
        if (cursor.getInt(5) == 0) {
            DefaultLogger.w("galleryAction_DeleteOwner", "Album can't be deleted here, use DeleteAlbum instead");
            return -100L;
        } else if (!ShareMediaManager.isOtherShareMediaId(cursor.getLong(0))) {
            return -1L;
        } else {
            DefaultLogger.w("galleryAction_DeleteOwner", "Share medias can't be deleted here, use DeleteSharer instead");
            return -100L;
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2
    public List<IStoragePermissionStrategy.PermissionResult> checkBatchItemPermission(Cursor cursor) {
        LinkedList linkedList = new LinkedList();
        String string = cursor.getString(7);
        String string2 = cursor.getString(8);
        if (!TextUtils.isEmpty(string2)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(string2, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
        }
        if (!this.mNotDeleteOriginFlag && !TextUtils.isEmpty(string)) {
            IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(string, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission2.granted) {
                linkedList.add(checkPermission2);
            }
        }
        return linkedList;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.DeleteCloudBase
    public List<String> getFilePathsById(Context context, Collection<Long> collection) {
        return (List) SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, DeleteCloudBase.PROJECTION, String.format("_id IN (%s)", TextUtils.join(", ", collection)), (String[]) null, (String) null, DeleteCloudBase.QUERY_HANDLER);
    }
}
