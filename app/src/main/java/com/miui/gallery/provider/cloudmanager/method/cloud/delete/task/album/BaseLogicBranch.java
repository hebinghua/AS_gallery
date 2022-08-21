package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.album;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.LogicBranch;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.local.DeleteFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseLogicBranch extends LogicBranch {
    public final boolean isLocalAlbum;
    public final boolean isOnlyDeleteLocal;
    public final boolean isSystemAlbum;
    public Cursor mAlbumCursor;
    public final long mAlbumId;
    public String mCheckPath;
    public Cursor mCursor;
    public final int mDeleteReason;

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
    }

    public BaseLogicBranch(Context context, ArrayList<Long> arrayList, DeleteAlbumBaseDataProvider deleteAlbumBaseDataProvider) {
        super(context, arrayList);
        this.mCursor = deleteAlbumBaseDataProvider.getCursor();
        this.mAlbumCursor = deleteAlbumBaseDataProvider.getAlbumCursor();
        this.mCheckPath = deleteAlbumBaseDataProvider.getCheckPath();
        this.isSystemAlbum = deleteAlbumBaseDataProvider.isSystemAlbum();
        this.mAlbumId = deleteAlbumBaseDataProvider.getAlbumId();
        this.isOnlyDeleteLocal = deleteAlbumBaseDataProvider.isOnlyDeleteLocal();
        this.mDeleteReason = deleteAlbumBaseDataProvider.getDeleteReason();
        this.isLocalAlbum = deleteAlbumBaseDataProvider.isLocalAlbum();
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        long deleteMediaInAlbum = deleteMediaInAlbum(supportSQLiteDatabase, mediaManager);
        long deleteAlbumItem = deleteAlbumItem(supportSQLiteDatabase, mediaManager);
        return deleteAlbumItem < 0 ? deleteAlbumItem : deleteMediaInAlbum;
    }

    public final int deleteAlbumItem(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        int update;
        DefaultLogger.d("galleryAction_deleteAlbum", "deleteAlbumItem => album [%d] isOnlyDeleteLocal [%b] isLocalAlbum [%b]", Long.valueOf(this.mAlbumId), Boolean.valueOf(this.isOnlyDeleteLocal), Boolean.valueOf(this.isLocalAlbum));
        int i = 0;
        if (this.isOnlyDeleteLocal) {
            if (this.isLocalAlbum) {
                String[] strArr = {String.valueOf(this.mAlbumId)};
                AlbumCacheManager.getInstance().delete("_id=?", new String[]{String.valueOf(this.mAlbumId)});
                i = supportSQLiteDatabase.delete("album", "_id=?", strArr);
                DefaultLogger.d("galleryAction_deleteAlbum", "delete (local album) [%d] deleteAlbumCount [%d]", Long.valueOf(this.mAlbumId), Integer.valueOf(i));
            }
        } else {
            if (this.isLocalAlbum) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("localFlag", (Integer) (-1));
                update = supportSQLiteDatabase.update("album", 0, contentValues, "_id=?", new String[]{String.valueOf(this.mAlbumId)});
                DefaultLogger.d("galleryAction_deleteAlbum", "update (local album) [%d] localFlag to [INVALID] updateAlbumCount [%d]", Long.valueOf(this.mAlbumId), Integer.valueOf(update));
            } else {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("localFlag", (Integer) 2);
                update = supportSQLiteDatabase.update("album", 0, contentValues2, "_id=?", new String[]{String.valueOf(this.mAlbumId)});
                DefaultLogger.d("galleryAction_deleteAlbum", "update (cloud album) [%d] localFlag to [DELETE] updateAlbumCount [%d]", Long.valueOf(this.mAlbumId), Integer.valueOf(update));
            }
            AlbumCacheManager.getInstance().delete("_id=?", new String[]{String.valueOf(this.mAlbumId)});
            i = update;
        }
        if (i > 0 && !this.isOnlyDeleteLocal && !this.isLocalAlbum) {
            SyncUtil.requestSync(this.mContext);
        }
        return i;
    }

    public final int deleteMediaInAlbum(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        DefaultLogger.d("galleryAction_deleteAlbum", "deleteMediaInAlbum => album [%d] isOnlyDeleteLocal [%b]", Long.valueOf(this.mAlbumId), Boolean.valueOf(this.isOnlyDeleteLocal));
        Cursor cursor = this.mCursor;
        if (cursor == null || cursor.isClosed() || this.mCursor.getCount() <= 0) {
            return 0;
        }
        ArrayList arrayList = new ArrayList();
        this.mCursor.moveToFirst();
        int i = 0;
        while (!this.mCursor.isAfterLast()) {
            if (this.isOnlyDeleteLocal) {
                try {
                } catch (Exception e) {
                    e = e;
                }
                try {
                    i = (int) (i + new DeleteFile(this.mContext, new ArrayList(), this.mCursor.getLong(0), this.mDeleteReason, supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager));
                } catch (Exception e2) {
                    e = e2;
                    DefaultLogger.e("galleryAction_deleteAlbum", "DeleteAlbum deleteMediaInAlbum error %s", e);
                    this.mCursor.moveToNext();
                }
            } else {
                markAsDirty(this.mCursor.getLong(0));
                DeleteRecord createDeleteRecord = Util.createDeleteRecord(this.mDeleteReason, this.mCursor, "galleryAction_deleteAlbum");
                if (createDeleteRecord != null) {
                    arrayList.add(createDeleteRecord);
                }
            }
            this.mCursor.moveToNext();
        }
        if (this.isOnlyDeleteLocal) {
            return i;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) (-1));
        int update = i + supportSQLiteDatabase.update("cloud", 0, contentValues, "localGroupId=? AND serverId IS NULL", new String[]{String.valueOf(this.mAlbumId)});
        DefaultLogger.d("galleryAction_deleteAlbum", "update db : local media localFlag to [INVALID] in album [%d] count [%d]", Long.valueOf(this.mAlbumId), Integer.valueOf(update));
        contentValues.put("localFlag", (Integer) 2);
        int update2 = update + supportSQLiteDatabase.update("cloud", 0, contentValues, "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId = ? ", new String[]{String.valueOf(this.mAlbumId)});
        DefaultLogger.d("galleryAction_deleteAlbum", "update db : cloud media localFlag to [DELETE] in album [%d] count [%d]", Long.valueOf(this.mAlbumId), Integer.valueOf(update2));
        mediaManager.delete("localGroupId = ? ", new String[]{String.valueOf(this.mAlbumId)});
        if (BaseMiscUtil.isValid(arrayList)) {
            DeleteRecorder.getInstance().record((DeleteRecord[]) arrayList.toArray(new DeleteRecord[0]));
        }
        return update2;
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList(checkDeletePermission(this.mCheckPath));
        if (!BaseMiscUtil.isValid(linkedList)) {
            return;
        }
        throw new StoragePermissionMissingException(linkedList);
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        long verify = super.verify(supportSQLiteDatabase, mediaManager);
        if (verify != -1) {
            return verify;
        }
        Cursor cursor = this.mAlbumCursor;
        if (cursor == null) {
            DefaultLogger.d("galleryAction_deleteAlbum", "mCursor for %s is null, abort", toString());
            return -101L;
        } else if (!cursor.moveToFirst()) {
            DefaultLogger.d("galleryAction_deleteAlbum", "mCursor for %s has nothing, abort", toString());
            return -102L;
        } else if (!this.isSystemAlbum) {
            return -1L;
        } else {
            DefaultLogger.w("galleryAction_deleteAlbum", "system album can't be deleted");
            return -100L;
        }
    }

    public final List<IStoragePermissionStrategy.PermissionResult> checkDeletePermission(String str) {
        LinkedList linkedList = new LinkedList();
        if (!TextUtils.isEmpty(str)) {
            IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
            if (!checkPermission.granted) {
                linkedList.add(checkPermission);
            }
        }
        return linkedList;
    }
}
