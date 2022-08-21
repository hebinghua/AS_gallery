package com.miui.gallery.provider.cloudmanager.handleFile;

import android.content.Context;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.BatchCursorTask;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class FileHandleTask extends OwnerShareSeparatorTask {
    public FileHandleTask(Context context, long[] jArr, String str) {
        super(context, null, jArr, str);
    }

    @Override // com.miui.gallery.provider.cloudmanager.handleFile.OwnerShareSeparatorTask
    public long[] executeOwner(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr) {
        DefaultLogger.d("galleryAction_FileHandleTask", "executeOwner =>");
        return new OwnerFileHandleTask(getContext(), getDirtyBulk(), jArr, this.mInvokerTag).run(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.handleFile.OwnerShareSeparatorTask
    public long[] executeSharer(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, BatchCursorTask.BatchOperationData<Long> batchOperationData, long[] jArr) {
        DefaultLogger.d("galleryAction_FileHandleTask", "executeSharer =>");
        return new SharerFileHandleTask(getContext(), getDirtyBulk(), jArr, this.mInvokerTag).run(supportSQLiteDatabase, mediaManager);
    }
}
