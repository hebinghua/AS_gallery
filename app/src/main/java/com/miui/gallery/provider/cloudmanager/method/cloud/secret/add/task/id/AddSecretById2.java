package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.Config$SecretAlbumConfig;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CheckPostProcessing;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class AddSecretById2 extends BaseDataProvider {
    public com.miui.gallery.provider.cloudmanager.LogicBranch mDelegate;
    public final long mMediaId;

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getFileName() {
        return super.getFileName();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getLocalFile() {
        return super.getLocalFile();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ int getLocalFlag() {
        return super.getLocalFlag();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ long getLocalGroupId() {
        return super.getLocalGroupId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getMicroThumbnailFile() {
        return super.getMicroThumbnailFile();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getServerId() {
        return super.getServerId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getServerStatus() {
        return super.getServerStatus();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ int getServerType() {
        return super.getServerType();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getSha1() {
        return super.getSha1();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getThumbnailFile() {
        return super.getThumbnailFile();
    }

    public AddSecretById2(Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, long j) {
        super(context, arrayList, supportSQLiteDatabase, new String[]{String.valueOf(j)});
        this.mMediaId = j;
    }

    public String toString() {
        return String.format(Locale.US, "%s[%d]", "galleryAction_AddSecretById2", Long.valueOf(this.mMediaId));
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        DefaultLogger.d("galleryAction_AddSecretById2", "%s => verify", toString());
        if (SpaceFullHandler.isOwnerSpaceFull()) {
            return -106L;
        }
        int i = this.mServerType;
        if (i != 1 && (i != 2 || !Config$SecretAlbumConfig.isVideoSupported())) {
            return -107L;
        }
        if (this.mServerId == null) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mLocalFile, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("galleryAction_AddSecretById2", "verify"));
            if (documentFile == null || !documentFile.exists()) {
                return -102L;
            }
            int canUpload = CloudUtils.canUpload(this.mLocalFile);
            if (canUpload == 4) {
                return -108L;
            }
            if (canUpload != 0) {
                return -107L;
            }
        }
        if (new CheckPostProcessing(this.mContext, this.mLocalFile).run(supportSQLiteDatabase, null) == -111) {
            return -111L;
        }
        return "recovery".equalsIgnoreCase(this.mServerStatus) ? -115L : -1L;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DefaultLogger.d("galleryAction_AddSecretById2", "%s => doPrepare", toString());
        com.miui.gallery.provider.cloudmanager.LogicBranch matchLogicBranch = matchLogicBranch(supportSQLiteDatabase);
        this.mDelegate = matchLogicBranch;
        matchLogicBranch.doPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        DefaultLogger.d("galleryAction_AddSecretById2", "%s => postPrepare", toString());
        this.mDelegate.postPrepare(supportSQLiteDatabase, mediaManager);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        DefaultLogger.d("galleryAction_AddSecretById2", "%s => execute", toString());
        return this.mDelegate.execute(supportSQLiteDatabase, mediaManager);
    }

    public final com.miui.gallery.provider.cloudmanager.LogicBranch matchLogicBranch(SupportSQLiteDatabase supportSQLiteDatabase) {
        int checkConflict = checkConflict(supportSQLiteDatabase);
        if (checkConflict == -117 || checkConflict == -118) {
            return new LogicBranch1(this.mContext, getDirtyBulk(), this, this.mMediaId, checkConflict);
        }
        int i = this.mLocalFlag;
        if (i == 0) {
            return new LogicBranch4(this.mContext, getDirtyBulk(), this, this.mMediaId, checkConflict);
        }
        if (i == 7 || i == 8) {
            return new LogicBranch2(this.mContext, getDirtyBulk(), this, this.mMediaId, checkConflict);
        }
        return new LogicBranch3(this.mContext, getDirtyBulk(), this, this.mMediaId, checkConflict);
    }

    public final int checkConflict(SupportSQLiteDatabase supportSQLiteDatabase) {
        String encodeFileName = CloudUtils.SecretAlbumUtils.encodeFileName(this.mFileName, this.mServerType == 2);
        if (TextUtils.isEmpty(this.mSha1)) {
            return 0;
        }
        Cursor cursor = null;
        try {
            cursor = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(new String[]{"sha1", "fileName", "localFlag", "localFile"}).selection("(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (sha1 = ? OR fileName = ? OR fileName = ?) AND localGroupId=? AND (serverId IS NOT NULL OR localFile IS NOT NULL)", new String[]{this.mSha1, this.mFileName, encodeFileName, String.valueOf(-1000L)}).create());
            if (cursor != null && cursor.getCount() > 0) {
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_AddSecretById2", "checkConflict");
                boolean z = false;
                while (cursor.moveToNext()) {
                    String string = cursor.getString(0);
                    String string2 = cursor.getString(1);
                    int i = cursor.getInt(2);
                    String string3 = cursor.getString(3);
                    if (TextUtils.equals(this.mSha1, string)) {
                        if (i != 7 && i != 8) {
                            return -117;
                        }
                        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(string3, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                        if (documentFile != null && documentFile.exists()) {
                            return -117;
                        }
                    }
                    if (!z && (this.mFileName.equalsIgnoreCase(string2) || encodeFileName.equalsIgnoreCase(string2))) {
                        z = true;
                    }
                }
                if (!z) {
                    return 0;
                }
                return -105;
            }
            return 0;
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }
}
