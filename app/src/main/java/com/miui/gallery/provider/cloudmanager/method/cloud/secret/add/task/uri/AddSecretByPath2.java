package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.uri;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CheckPostProcessing;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class AddSecretByPath2 extends CursorTask2 {
    public final ContentValues mContentValues;
    public final String mSrcPath;

    public AddSecretByPath2(Context context, ArrayList<Long> arrayList, String str) {
        super(context, arrayList);
        this.mContentValues = new ContentValues();
        this.mSrcPath = str;
    }

    public String toString() {
        return String.format("%s[%s]", "galleryAction_AddSecretByPath2", this.mSrcPath);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        DefaultLogger.d("galleryAction_AddSecretByPath2", "verify", toString());
        if (SpaceFullHandler.isOwnerSpaceFull()) {
            return -106L;
        }
        int canUpload = CloudUtils.canUpload(this.mSrcPath);
        if (canUpload == 4) {
            return -108L;
        }
        if (canUpload != 0) {
            return -107L;
        }
        if (new CheckPostProcessing(this.mContext, this.mSrcPath).run(supportSQLiteDatabase, null) == -111) {
            return -111L;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mSrcPath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("galleryAction_AddSecretByPath2", "verify"));
        return (documentFile == null || !documentFile.exists()) ? -100L : -1L;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DefaultLogger.d("galleryAction_AddSecretByPath2", "doPrepare", toString());
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage("MIUI/Gallery/cloud/secretAlbum");
        String fileName = BaseFileUtils.getFileName(this.mSrcPath);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_AddSecretByPath2", "doPrepare");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        StringBuilder sb = new StringBuilder();
        sb.append(pathInPriorStorage);
        String str = File.separator;
        sb.append(str);
        sb.append(fileName);
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(sb.toString(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        String str2 = this.mSrcPath;
        if (documentFile != null && documentFile.exists()) {
            str2 = BaseFileUtils.getParentFolderPath(this.mSrcPath) + str + DownloadPathHelper.addPostfixToFileName(fileName, String.valueOf(System.currentTimeMillis()));
        }
        this.mContentValues.put("rename_path", str2);
        this.mContentValues.put("move_path", BaseFileUtils.concat(pathInPriorStorage, BaseFileUtils.getFileName(str2)));
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        DefaultLogger.d("galleryAction_AddSecretByPath2", "postPrepare", toString());
        LinkedList linkedList = new LinkedList();
        String asString = this.mContentValues.getAsString("rename_path");
        if (!TextUtils.equals(asString, this.mSrcPath)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(this.mSrcPath, IStoragePermissionStrategy.Permission.UPDATE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
        }
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        if (TextUtils.isEmpty(asString)) {
            asString = this.mSrcPath;
        }
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
        IStoragePermissionStrategy.PermissionResult checkPermission2 = storageStrategyManager.checkPermission(asString, permission);
        if (!checkPermission2.granted) {
            linkedList.add(checkPermission2);
        }
        String asString2 = this.mContentValues.getAsString("move_path");
        IStoragePermissionStrategy.PermissionResult checkPermission3 = StorageSolutionProvider.get().checkPermission(asString2, IStoragePermissionStrategy.Permission.INSERT);
        if (!checkPermission3.granted) {
            linkedList.add(checkPermission3);
        }
        IStoragePermissionStrategy.PermissionResult checkPermission4 = StorageSolutionProvider.get().checkPermission(asString2, permission);
        if (!checkPermission4.granted) {
            linkedList.add(checkPermission4);
        }
        IStoragePermissionStrategy.PermissionResult checkPermission5 = StorageSolutionProvider.get().checkPermission(asString2, IStoragePermissionStrategy.Permission.UPDATE);
        if (!checkPermission5.granted) {
            linkedList.add(checkPermission5);
        }
        if (!BaseMiscUtil.isValid(linkedList)) {
            return;
        }
        throw new StoragePermissionMissingException(linkedList);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DefaultLogger.d("galleryAction_AddSecretByPath2", "execute", toString());
        String asString = this.mContentValues.getAsString("rename_path");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("galleryAction_AddSecretByPath2", "execute");
        if (TextUtils.isEmpty(asString) || !StorageSolutionProvider.get().moveFile(this.mSrcPath, asString, appendInvokerTag)) {
            return -101L;
        }
        if (TextUtils.isEmpty(asString)) {
            asString = this.mSrcPath;
        }
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage("MIUI/Gallery/cloud/secretAlbum");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(asString, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        if (documentFile == null || !documentFile.exists()) {
            return -102L;
        }
        String concat = BaseFileUtils.concat(pathInPriorStorage, BaseFileUtils.getFileName(asString));
        if (!StorageSolutionProvider.get().moveFile(asString, concat, appendInvokerTag)) {
            return -101L;
        }
        long lastModified = documentFile.lastModified();
        if (lastModified != 0) {
            StorageSolutionProvider.get().setLastModified(StorageSolutionProvider.get().getDocumentFile(concat, IStoragePermissionStrategy.Permission.UPDATE, appendInvokerTag), lastModified);
        }
        ScanResult saveToCloudDB = SaveToCloudUtil.saveToCloudDB(this.mContext, new SaveParams.Builder().setSaveFile(new File(this.mContentValues.getAsString("move_path"))).setAlbumId(-1000L).setLocalFlag(8).setCredible(true).build());
        long mediaId = saveToCloudDB.getMediaId();
        if (mediaId > 0) {
            return mediaId;
        }
        if (AnonymousClass1.$SwitchMap$com$miui$gallery$scanner$core$ScanContracts$ScanResultReason[saveToCloudDB.getReasonCode().ordinal()] != 1) {
            return -101L;
        }
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(this.mSrcPath, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
        if (documentFile2 == null || !documentFile2.delete()) {
            return -103L;
        }
        DeleteRecorder.getInstance().record(new DeleteRecord(35, this.mSrcPath, "galleryAction_AddSecretByPath2"));
        return -103L;
    }

    /* renamed from: com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.uri.AddSecretByPath2$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$scanner$core$ScanContracts$ScanResultReason;

        static {
            int[] iArr = new int[ScanContracts$ScanResultReason.values().length];
            $SwitchMap$com$miui$gallery$scanner$core$ScanContracts$ScanResultReason = iArr;
            try {
                iArr[ScanContracts$ScanResultReason.ALREADY_EXISTS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }
}
