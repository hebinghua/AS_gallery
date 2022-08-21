package com.miui.gallery.provider.cloudmanager.method.album;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CursorTask;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class RenameAlbumMethod implements IAlbumMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_RenameAlbumMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        Bundle renameAlbum = renameAlbum(context, supportSQLiteDatabase, mediaManager, arrayList, bundle.getLong("album_id"), str);
        bundle2.putAll(renameAlbum);
        if (renameAlbum.getLong("id") > 0) {
            bundle2.putBoolean("should_request_sync", true);
        }
    }

    public static Bundle renameAlbum(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long j, String str) throws StoragePermissionMissingException {
        Bundle bundle = new Bundle(2);
        long j2 = -100;
        if (!TextUtils.isEmpty(str)) {
            try {
                Numbers.ensurePositive(j);
                RenameAlbum renameAlbum = new RenameAlbum(context, arrayList, j, str);
                long run = renameAlbum.run(supportSQLiteDatabase, mediaManager);
                if (run == -103 || run == -105) {
                    long conflictAlbumId = renameAlbum.getConflictAlbumId(supportSQLiteDatabase);
                    if (conflictAlbumId >= 0) {
                        bundle.putLong("conflict_album_id", conflictAlbumId);
                    }
                }
                j2 = run;
            } catch (Exception e) {
                DefaultLogger.w("galleryAction_Method_RenameAlbumMethod", e);
                if (e.getCause() instanceof StoragePermissionMissingException) {
                    throw ((StoragePermissionMissingException) e.getCause());
                }
            }
        }
        bundle.putLong("id", j2);
        return bundle;
    }

    /* loaded from: classes2.dex */
    public static class RenameAlbum extends CursorTask {
        public long mAlbumId;
        public String mNewName;

        public RenameAlbum(Context context, ArrayList<Long> arrayList, long j, String str) {
            super(context, arrayList);
            this.mAlbumId = j;
            this.mNewName = str;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(AlbumConstants.DB_REAL_PROJECTION).selection("_id=?  AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{String.valueOf(this.mAlbumId)}).create());
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) throws StoragePermissionMissingException {
            long verify = super.verify(supportSQLiteDatabase);
            if (verify != -1) {
                return verify;
            }
            try {
                try {
                    Cursor conflictAlbumCursor = getConflictAlbumCursor(supportSQLiteDatabase);
                    if (conflictAlbumCursor == null) {
                        DefaultLogger.d("galleryAction_Method_RenameAlbumMethod", "cursor is null, verify failed.");
                        if (conflictAlbumCursor != null) {
                            BaseMiscUtil.closeSilently(conflictAlbumCursor);
                        }
                        return -101L;
                    } else if (!conflictAlbumCursor.moveToFirst()) {
                        BaseMiscUtil.closeSilently(conflictAlbumCursor);
                        return -1L;
                    } else {
                        DefaultLogger.d("galleryAction_Method_RenameAlbumMethod", "Album name already exists.");
                        BaseMiscUtil.closeSilently(conflictAlbumCursor);
                        return -103L;
                    }
                } catch (Exception e) {
                    DefaultLogger.e("galleryAction_Method_RenameAlbumMethod", e);
                    if (0 != 0) {
                        BaseMiscUtil.closeSilently(null);
                    }
                    return -101L;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    BaseMiscUtil.closeSilently(null);
                }
                throw th;
            }
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", this.mNewName);
            if (this.mCursor.getInt(7) == 0) {
                contentValues.put("localFlag", (Integer) 10);
            }
            String[] strArr = {String.valueOf(this.mAlbumId)};
            AlbumCacheManager.getInstance().update("_id=?", strArr, contentValues);
            int update = supportSQLiteDatabase.update("album", 0, contentValues, "_id=?", strArr);
            DefaultLogger.d("galleryAction_Method_RenameAlbumMethod", "Album(id: %d) rename finished.", Long.valueOf(this.mAlbumId));
            if (update > 0) {
                return this.mAlbumId;
            }
            return -101L;
        }

        public final Cursor getConflictAlbumCursor(SupportSQLiteDatabase supportSQLiteDatabase) {
            return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(AlbumConstants.DB_REAL_PROJECTION).selection("name=? COLLATE NOCASE  AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{this.mNewName}).create());
        }

        public long getConflictAlbumId(SupportSQLiteDatabase supportSQLiteDatabase) {
            Cursor conflictAlbumCursor = getConflictAlbumCursor(supportSQLiteDatabase);
            if (conflictAlbumCursor != null) {
                try {
                    if (conflictAlbumCursor.moveToFirst()) {
                        long j = conflictAlbumCursor.getLong(0);
                        conflictAlbumCursor.close();
                        return j;
                    }
                } catch (Throwable th) {
                    try {
                        conflictAlbumCursor.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
            if (conflictAlbumCursor != null) {
                conflictAlbumCursor.close();
            }
            return -102L;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public String toString() {
            return String.format(Locale.US, "RenameAlbum{id: %d}", Long.valueOf(this.mAlbumId));
        }
    }
}
