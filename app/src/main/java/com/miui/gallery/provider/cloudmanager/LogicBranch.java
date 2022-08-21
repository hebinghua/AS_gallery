package com.miui.gallery.provider.cloudmanager;

import android.content.Context;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class LogicBranch extends CursorTask2 {
    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public abstract void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager);

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public abstract long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException;

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public abstract void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception;

    public LogicBranch(Context context, ArrayList<Long> arrayList) {
        super(context, arrayList);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        return super.verify(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public boolean checkValidation(long j) {
        return super.checkValidation(j);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public final ArrayList<Long> getDirtyBulk() {
        return super.getDirtyBulk();
    }
}
