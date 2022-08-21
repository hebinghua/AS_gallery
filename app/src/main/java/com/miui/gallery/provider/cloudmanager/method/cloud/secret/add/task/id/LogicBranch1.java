package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import android.content.Context;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class LogicBranch1 extends LogicBranch {
    public LogicBranch1(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, int i) {
        super(context, arrayList, iDataProvider, j, i);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        querySha1ConflictItems(supportSQLiteDatabase, this.mSha1ConflictItems, true);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        super.execute(supportSQLiteDatabase, mediaManager);
        return this.mMediaId;
    }
}
