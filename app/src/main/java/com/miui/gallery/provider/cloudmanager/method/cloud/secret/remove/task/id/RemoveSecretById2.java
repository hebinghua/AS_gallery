package com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id;

import android.content.Context;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.MediaConflict;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.DeleteMethod;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class RemoveSecretById2 extends BaseDataProvider {
    public final long mAlbumId;
    public com.miui.gallery.provider.cloudmanager.LogicBranch mDelegate;
    public long mMediaConflictValidation;
    public final long mMediaId;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getFileName() {
        return super.getFileName();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getLocalFile() {
        return super.getLocalFile();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ int getLocalFlag() {
        return super.getLocalFlag();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ long getLocalGroupId() {
        return super.getLocalGroupId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getMicroThumbnailFile() {
        return super.getMicroThumbnailFile();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ byte[] getSecretKey() {
        return super.getSecretKey();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getServerId() {
        return super.getServerId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ int getServerType() {
        return super.getServerType();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getSha1() {
        return super.getSha1();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getThumbnailFile() {
        return super.getThumbnailFile();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getTitle() {
        return super.getTitle();
    }

    public RemoveSecretById2(Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, long j, long j2) {
        super(context, arrayList, supportSQLiteDatabase, new String[]{String.valueOf(j)});
        this.mMediaId = j;
        this.mAlbumId = j2;
    }

    public String toString() {
        return String.format(Locale.US, "%s{%d}", "RemoveSecretById2", Long.valueOf(this.mMediaId));
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        if (SpaceFullHandler.isOwnerSpaceFull()) {
            return -106L;
        }
        long verify = MediaConflict.verify(this.mFileName, this.mAlbumId, this.mMediaId, this.mSha1, supportSQLiteDatabase);
        this.mMediaConflictValidation = verify;
        if (verify != -118) {
            return -1L;
        }
        try {
            DeleteMethod.delete(this.mContext, supportSQLiteDatabase, mediaManager, getDirtyBulk(), new long[]{this.mMediaId}, 37);
            return -103L;
        } catch (Exception unused) {
            DefaultLogger.e("RemoveSecretById2", "exit conflict image try to delete failed for %s", Long.valueOf(this.mMediaId));
            return -121L;
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DefaultLogger.d("RemoveSecretById2", "%s => doPrepare", toString());
        com.miui.gallery.provider.cloudmanager.LogicBranch matchLogicBranch = matchLogicBranch();
        this.mDelegate = matchLogicBranch;
        matchLogicBranch.doPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        DefaultLogger.d("RemoveSecretById2", "%s => postPrepare", toString());
        this.mDelegate.postPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        DefaultLogger.d("RemoveSecretById2", "%s => execute", toString());
        return this.mDelegate.execute(supportSQLiteDatabase, mediaManager);
    }

    public final com.miui.gallery.provider.cloudmanager.LogicBranch matchLogicBranch() {
        if (this.mLocalFlag == 0) {
            return new LogicBranch1(this.mContext, getDirtyBulk(), this.mMediaId, this.mAlbumId, this.mMediaConflictValidation, this);
        }
        return new LogicBranch2(this.mContext, getDirtyBulk(), this.mMediaId, this.mAlbumId, this.mMediaConflictValidation, this);
    }
}
