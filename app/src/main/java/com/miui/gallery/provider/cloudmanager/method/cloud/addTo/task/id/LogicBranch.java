package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.scanner.provider.GalleryMediaScannerProviderContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class LogicBranch extends BaseLogicBranch {
    public long mAlbumId;
    public Cursor mCursor;
    public String mFinalFileName;
    public long mFromAlbum;
    public String mFromFilePath;
    public long mMediaId;
    public String mToFilePath;

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        return -1L;
    }

    public LogicBranch(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, long j2, Cursor cursor) {
        super(context, arrayList, iDataProvider);
        this.mMediaId = j;
        this.mAlbumId = j2;
        this.mCursor = cursor;
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList(checkPermissions(this.mFromFilePath, this.mToFilePath));
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        addScanIgnorePath();
        this.mFromAlbum = this.mCursor.getLong(3);
    }

    public final void addScanIgnorePath() {
        if (this.mContext == null) {
            return;
        }
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(this.mFromFilePath)) {
            bundle.putString("param_path", this.mFromFilePath);
            this.mContext.getContentResolver().call(GalleryMediaScannerProviderContract.AUTHORITY_URI, "save_request_media_store_scan_record", (String) null, bundle);
        }
        if (TextUtils.isEmpty(this.mToFilePath)) {
            return;
        }
        bundle.putString("param_path", this.mToFilePath);
        this.mContext.getContentResolver().call(GalleryMediaScannerProviderContract.AUTHORITY_URI, "save_request_media_store_scan_record", (String) null, bundle);
    }

    public final List<IStoragePermissionStrategy.PermissionResult> checkPermissions(String str, String str2) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                DefaultLogger.e("galleryAction_Method_AddToAlbum", "delete not granted: %s", str);
                linkedList.add(checkPermission);
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.INSERT);
            if (!checkPermission2.granted) {
                DefaultLogger.e("galleryAction_Method_AddToAlbum", "insert not granted: %s", str2);
                linkedList.add(checkPermission2);
            }
            IStoragePermissionStrategy.PermissionResult checkPermission3 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.UPDATE);
            if (!checkPermission3.granted) {
                DefaultLogger.e("galleryAction_Method_AddToAlbum", "update not granted: %s", str2);
                linkedList.add(checkPermission3);
            }
        }
        return linkedList;
    }

    public void setFromFilePath(String str) {
        this.mFromFilePath = str;
    }

    public void setToFilePath(String str) {
        this.mToFilePath = str;
    }

    public void setFinalFileName(String str) {
        this.mFinalFileName = str;
    }
}
