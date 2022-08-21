package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class DeleteAlbum extends DeleteAlbumBaseDataProvider {
    public final BaseLogicBranch mDelegate;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ Cursor getAlbumCursor() {
        return super.getAlbumCursor();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ long getAlbumId() {
        return super.getAlbumId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ String getCheckPath() {
        return super.getCheckPath();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ Cursor getCursor() {
        return super.getCursor();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ int getDeleteReason() {
        return super.getDeleteReason();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ boolean isLocalAlbum() {
        return super.isLocalAlbum();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ boolean isOnlyDeleteLocal() {
        return super.isOnlyDeleteLocal();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album.DeleteAlbumBaseDataProvider
    public /* bridge */ /* synthetic */ boolean isSystemAlbum() {
        return super.isSystemAlbum();
    }

    public DeleteAlbum(Context context, SupportSQLiteDatabase supportSQLiteDatabase, ArrayList<Long> arrayList, long j, int i, boolean z) {
        super(context, arrayList, supportSQLiteDatabase, j, i, z);
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
