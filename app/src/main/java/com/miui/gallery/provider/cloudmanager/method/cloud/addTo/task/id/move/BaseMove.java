package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseCopyAndMoveByMediaId;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.MoveLogicBranch;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseMove extends BaseCopyAndMoveByMediaId {
    public final MoveLogicBranch mDelegate;
    public String mFinalFileName;
    public long mFromAlbum;

    public abstract void buildValues();

    public BaseMove(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, j, j2, cursor);
        this.mDelegate = new MoveLogicBranch(context, arrayList, this, this.mMediaId, this.mAlbumId, this.mCursor);
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
        buildValues();
        modifierNameCheck(supportSQLiteDatabase, false);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseCopyAndMoveByMediaId
    public boolean isToShareAlbum() {
        return ShareAlbumHelper.isOtherShareAlbumId(this.mAlbumId);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseCopyAndMoveByMediaId, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        this.mDelegate.setFromFilePath(getFromFilePath());
        this.mDelegate.setToFilePath(getToFilePath());
        String finalFileName = getFinalFileName();
        this.mFinalFileName = finalFileName;
        this.mDelegate.setFinalFileName(finalFileName);
        this.mDelegate.postPrepare(supportSQLiteDatabase, mediaManager);
        super.postPrepare(supportSQLiteDatabase, mediaManager);
    }
}
