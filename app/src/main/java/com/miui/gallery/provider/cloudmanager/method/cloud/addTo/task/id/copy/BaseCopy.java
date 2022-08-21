package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseCopyAndMoveByMediaId;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.CopyLogicBranch;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseCopy extends BaseCopyAndMoveByMediaId {
    public final CopyLogicBranch mDelegate;
    public long mFromAlbum;

    public abstract void buildValues(boolean z);

    public BaseCopy(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, j, j2, cursor);
        this.mDelegate = new CopyLogicBranch(context, arrayList, this, this.mMediaId, this.mAlbumId, this.mCursor);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        return this.mDelegate.verify(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public boolean checkValidation(long j) {
        return this.mDelegate.checkValidation(j);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        this.mFromAlbum = this.mCursor.getLong(3);
        int i = this.mLocalFlag;
        buildValues((i == 0 || i == 5 || i == 6 || i == 9) ? false : true);
        modifierNameCheck(supportSQLiteDatabase, isToShareAlbum());
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseCopyAndMoveByMediaId, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        this.mDelegate.setFromFilePath(getFromFilePath());
        this.mDelegate.setToFilePath(getToFilePath());
        this.mDelegate.setFinalFileName(getFinalFileName());
        this.mDelegate.postPrepare(supportSQLiteDatabase, mediaManager);
        super.postPrepare(supportSQLiteDatabase, mediaManager);
    }
}
