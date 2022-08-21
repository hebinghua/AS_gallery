package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.local;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class DeleteFile extends DeleteCursorDataProviderBase {
    public BaseLogicBranch mDelegate;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.local.DeleteCursorDataProviderBase
    public /* bridge */ /* synthetic */ Cursor getCursor() {
        return super.getCursor();
    }

    public DeleteFile(Context context, ArrayList<Long> arrayList, long j, int i, SupportSQLiteDatabase supportSQLiteDatabase) {
        super(context, arrayList, supportSQLiteDatabase, j, i);
        this.mDelegate = new BaseLogicBranch(context, arrayList, this);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        return this.mDelegate.verify(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public boolean checkValidation(long j) {
        return this.mDelegate.checkValidation(j);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        this.mDelegate.doPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        this.mDelegate.postPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        return this.mDelegate.execute(supportSQLiteDatabase, mediaManager);
    }
}
