package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.BatchCursorTask;
import com.miui.gallery.provider.cloudmanager.BatchTaskById;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.handleFile.MediaFileHandleJob;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class SharerFileHandleTask extends BatchTaskById {
    public SharerFileHandleTask(Context context, ArrayList<Long> arrayList, long[] jArr, String str) {
        super(context, arrayList, jArr, str);
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchCursorTask
    public Cursor queryCursor(SupportSQLiteDatabase supportSQLiteDatabase, Long[] lArr) {
        return getContext().getContentResolver().query(GalleryContract.ShareImage.SHARE_URI, Contracts.PROJECTION, String.format("%s IN (%s)", j.c, TextUtils.join(",", lArr)), null, null);
    }

    @Override // com.miui.gallery.provider.cloudmanager.BatchTask
    public void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, BatchCursorTask.BatchOperationData<Long> batchOperationData) {
        int i;
        ContentResolver contentResolver = getContext().getContentResolver();
        for (Long l : batchOperationData.keys) {
            long longValue = l.longValue();
            BatchCursorTask.BatchItemData batchItemData = batchOperationData.keyItemDataMap.get(Long.valueOf(longValue));
            if (batchItemData != null && batchItemData.result == -1 && (i = batchItemData.cursorIndex) >= 0) {
                batchOperationData.cursor.moveToPosition(i);
                batchItemData.result = new MediaFileHandleJob.Builder().setParams(contentResolver, batchOperationData.cursor, ShareMediaManager.convertToMediaId(longValue), 40, this.mInvokerTag).build().handle(getContext()) ? 1L : 0L;
                DefaultLogger.d("galleryAction_FileHandle_SharerFileHandleTask", "MediaFileHandleJob result for %d is %s", Long.valueOf(longValue), batchItemData);
            }
        }
    }
}
