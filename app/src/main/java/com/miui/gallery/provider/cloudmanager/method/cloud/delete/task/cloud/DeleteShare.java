package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchItemData2;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchOperationData2;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class DeleteShare extends DeleteCloudBase {
    public String mUserId;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.DeleteCloudBase
    public String getTableName() {
        return "shareImage";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTask2
    public /* bridge */ /* synthetic */ void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, Object obj, List list) {
        executeBatch(supportSQLiteDatabase, mediaManager, bundle, (BatchOperationData2) obj, (List<IStoragePermissionStrategy.PermissionResult>) list);
    }

    public DeleteShare(Context context, ArrayList<Long> arrayList, long[] jArr, int i) {
        super(context, arrayList, jArr, i);
        this.mUserId = AccountCache.getAccount().name;
    }

    public void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchOperationData2<Long> batchOperationData2, List<IStoragePermissionStrategy.PermissionResult> list) {
        supportSQLiteDatabase.beginTransaction();
        try {
            try {
                ArrayList arrayList = new ArrayList();
                for (Map.Entry<Long, BatchItemData2> entry : batchOperationData2.keyItemDataMap.entrySet()) {
                    if (entry.getValue().result == -1) {
                        Long key = entry.getKey();
                        batchOperationData2.cursor.moveToPosition(entry.getValue().cursorIndex);
                        Cursor cursor = batchOperationData2.cursor;
                        String string = cursor.getString(cursor.getColumnIndex("localFile"));
                        if (TextUtils.isEmpty(string)) {
                            Cursor cursor2 = batchOperationData2.cursor;
                            string = cursor2.getString(cursor2.getColumnIndex("thumbnailFile"));
                        }
                        Cursor cursor3 = batchOperationData2.cursor;
                        cursor3.getLong(cursor3.getColumnIndex("localGroupId"));
                        if (batchOperationData2.cursor.isNull(4)) {
                            DefaultLogger.d("galleryAction_DeleteShare", "DELETE ITEM: no server id found, update to invalid record: %d", entry.getKey());
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("localFlag", (Integer) (-1));
                            entry.getValue().result = supportSQLiteDatabase.update("shareImage", 0, contentValues, "_id = ? ", new String[]{key.toString()});
                        } else {
                            DefaultLogger.d("galleryAction_DeleteShare", "DELETE ITEM: server id found, mark delete %d", entry.getKey());
                            ContentValues contentValues2 = new ContentValues();
                            contentValues2.put("localFlag", (Integer) 2);
                            entry.getValue().result = supportSQLiteDatabase.update("shareImage", 0, contentValues2, "_id = ? ", new String[]{key.toString()});
                        }
                        RemarkManager.remarkMediaId(RemarkInfoFactory.createDeleteRemarkInfo(ShareMediaManager.convertToMediaId(entry.getKey().longValue()), string, null));
                        DeleteRecord createDeleteRecord = Util.createDeleteRecord(this.mDeleteReason, batchOperationData2.cursor, "galleryAction_DeleteShare");
                        if (createDeleteRecord != null) {
                            arrayList.add(createDeleteRecord);
                        }
                    }
                }
                supportSQLiteDatabase.setTransactionSuccessful();
                if (BaseMiscUtil.isValid(arrayList)) {
                    DeleteRecorder.getInstance().record((DeleteRecord[]) arrayList.toArray(new DeleteRecord[0]));
                }
                supportSQLiteDatabase.endTransaction();
                ArrayList arrayList2 = new ArrayList();
                for (Map.Entry<Long, BatchItemData2> entry2 : batchOperationData2.keyItemDataMap.entrySet()) {
                    if (entry2.getValue().result > 0) {
                        long convertToMediaId = ShareMediaManager.convertToMediaId(entry2.getKey().longValue());
                        arrayList2.add(Long.valueOf(convertToMediaId));
                        markAsDirty(convertToMediaId);
                    }
                }
                if (arrayList2.size() <= 0) {
                    return;
                }
                ShareMediaManager.getInstance().delete(String.format("%s IN (%s)", j.c, TextUtils.join(",", arrayList2)), null);
            } catch (Exception unused) {
                batchOperationData2.fillResult(-110L);
                supportSQLiteDatabase.endTransaction();
            }
        } catch (Throwable th) {
            supportSQLiteDatabase.endTransaction();
            throw th;
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2
    public long verifyBatchItem(Cursor cursor) {
        if (cursor.getInt(5) == 0) {
            DefaultLogger.w("galleryAction_DeleteShare", "Album can't be deleted here, use DeleteAlbum instead");
            return -100L;
        } else if (TextUtils.isEmpty(this.mUserId)) {
            DefaultLogger.w("galleryAction_DeleteShare", "Account doesn't exist!");
            return -100L;
        } else if (TextUtils.isEmpty(cursor.getString(4)) || this.mUserId.equals(cursor.getString(51))) {
            return -1L;
        } else {
            DefaultLogger.w("galleryAction_DeleteShare", "User's deleting other's media item!");
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
        if (!TextUtils.isEmpty(string)) {
            IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(string, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission2.granted) {
                linkedList.add(checkPermission2);
            }
        }
        return linkedList;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.DeleteCloudBase
    public List<String> getFilePathsById(Context context, Collection<Long> collection) {
        return (List) SafeDBUtil.safeQuery(context, GalleryContract.ShareImage.SHARE_URI, DeleteCloudBase.PROJECTION, String.format("_id IN (%s)", TextUtils.join(", ", toShareImageIds(collection))), (String[]) null, (String) null, DeleteCloudBase.QUERY_HANDLER);
    }

    public final List<Long> toShareImageIds(Collection<Long> collection) {
        if (collection == null || collection.size() <= 0) {
            return Collections.emptyList();
        }
        return (List) collection.parallelStream().map(DeleteShare$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList());
    }
}
