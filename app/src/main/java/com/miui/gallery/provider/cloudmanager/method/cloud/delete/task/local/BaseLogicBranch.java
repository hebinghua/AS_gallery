package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.StringBuilderPrinter;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.LogicBranch;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseLogicBranch extends LogicBranch {
    public Cursor mCursor;
    public int mDeleteReason;
    public long mId;

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
    }

    public BaseLogicBranch(Context context, ArrayList<Long> arrayList, DeleteCursorDataProviderBase deleteCursorDataProviderBase) {
        super(context, arrayList);
        this.mCursor = deleteCursorDataProviderBase.getCursor();
        this.mId = deleteCursorDataProviderBase.mId;
        this.mDeleteReason = deleteCursorDataProviderBase.mDeleteReason;
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        long verify = super.verify(supportSQLiteDatabase, mediaManager);
        if (verify != -1) {
            return verify;
        }
        Cursor cursor = this.mCursor;
        if (cursor == null) {
            DefaultLogger.e("galleryAction_DeleteFile", "cursor for %s is null, abort", toString());
            return -101L;
        } else if (!cursor.moveToFirst()) {
            DefaultLogger.e("galleryAction_DeleteFile", "cursor for %s has nothing, abort", toString());
            return -102L;
        } else if (Album.isSystemAlbum(String.valueOf(this.mCursor.getLong(4)))) {
            DefaultLogger.w("galleryAction_DeleteFile", "system album can't be deleted");
            return -102L;
        } else if (this.mCursor.isNull(5) || this.mCursor.getInt(5) != 0) {
            return -1L;
        } else {
            DefaultLogger.e("galleryAction_DeleteFile", "error call:%s", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
            return -102L;
        }
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList(checkDeletePermission(getThumbFilePath(), getLocalFilePath()));
        if (!BaseMiscUtil.isValid(linkedList)) {
            return;
        }
        throw new StoragePermissionMissingException(linkedList);
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        int delete;
        DefaultLogger.d("galleryAction_DeleteFile", "DELETING MEDIA FILE");
        TimingTracing.beginTracing("deleteFile", "delete local 1 file");
        FileHandleManager.deleteFileToTrash(GalleryApp.sGetAndroidContext(), this.mCursor, this.mId, this.mDeleteReason, false, "galleryAction_DeleteFile");
        if (!this.mCursor.isNull(2) && this.mCursor.getInt(2) == 15) {
            delete = mediaManager.delete("_id=?", new String[]{String.valueOf(this.mId)});
            DefaultLogger.d("galleryAction_DeleteFile", "delete cache : cloud id [%d] count [%d]", Long.valueOf(this.mId), Integer.valueOf(delete));
        } else if (this.mCursor.isNull(4) || this.mCursor.isNull(23) || this.mCursor.getString(23).equals("temp")) {
            mediaManager.delete("_id=?", new String[]{String.valueOf(this.mId)});
            delete = supportSQLiteDatabase.delete("cloud", "_id = ? ", new String[]{String.valueOf(this.mId)});
            DefaultLogger.d("galleryAction_DeleteFile", "delete db : cloud id [%d] in album [%d] count [%d]", Long.valueOf(this.mId), Integer.valueOf(delete));
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("thumbnailFile", "");
            contentValues.put("localFile", "");
            mediaManager.update("_id=?", new String[]{String.valueOf(this.mId)}, contentValues);
            delete = supportSQLiteDatabase.update("cloud", 0, contentValues, "_id = ? ", new String[]{String.valueOf(this.mId)});
            DefaultLogger.d("galleryAction_DeleteFile", "update db : cloud localFile & thumbnailFile to empty id [%d] count [%d]", Long.valueOf(this.mId), Integer.valueOf(delete));
        }
        StringBuilder sb = new StringBuilder();
        long stopTracing = TimingTracing.stopTracing(new StringBuilderPrinter(sb));
        if (delete > 0) {
            long j = delete;
            if (stopTracing > 300 * j) {
                DefaultLogger.w(toString(), "delete slowly: %s", sb);
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", String.valueOf(stopTracing / j));
                hashMap.put(CallMethod.ARG_EXTRA_STRING, sb.toString());
                SamplingStatHelper.recordCountEvent("delete_performance", toString(), hashMap);
            }
        }
        return delete;
    }

    public final String getThumbFilePath() {
        Cursor cursor = this.mCursor;
        return cursor.getString(cursor.getColumnIndexOrThrow("thumbnailFile"));
    }

    public final String getLocalFilePath() {
        Cursor cursor = this.mCursor;
        return cursor.getString(cursor.getColumnIndexOrThrow("localFile"));
    }

    public final List<IStoragePermissionStrategy.PermissionResult> checkDeletePermission(String str, String str2) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission2.granted) {
                linkedList.add(checkPermission2);
            }
        }
        return linkedList;
    }
}
