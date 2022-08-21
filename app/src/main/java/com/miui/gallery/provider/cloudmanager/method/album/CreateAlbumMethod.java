package com.miui.gallery.provider.cloudmanager.method.album;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CursorTask;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CreateAlbumMethod implements IAlbumMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_CreateAlbum";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        bundle2.putAll(createAlbum(context, supportSQLiteDatabase, mediaManager, arrayList, str));
    }

    public static Bundle createAlbum(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, String str) throws StoragePermissionMissingException {
        long j;
        Bundle bundle = new Bundle(3);
        if (TextUtils.isEmpty(str)) {
            j = -100;
        } else {
            CreateAlbum createAlbum = new CreateAlbum(context, arrayList, str, bundle);
            long run = createAlbum.run(supportSQLiteDatabase, mediaManager);
            if (run == -103 || run == -105) {
                long conflictAlbumId = createAlbum.getConflictAlbumId(supportSQLiteDatabase);
                if (conflictAlbumId >= 0) {
                    bundle.putLong("conflict_album_id", conflictAlbumId);
                }
            }
            j = run;
        }
        bundle.putLong("id", j);
        return bundle;
    }

    /* loaded from: classes2.dex */
    public static class CreateAlbum extends CursorTask {
        public String mAlbumPath;
        public String mName;
        public Bundle mResultBundle;

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public String toString() {
            return "CreateAlbum";
        }

        public CreateAlbum(Context context, ArrayList<Long> arrayList, String str, Bundle bundle) {
            super(context, arrayList);
            this.mName = str;
            this.mAlbumPath = Util.genRelativePath(str, false);
            this.mResultBundle = bundle;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(AlbumConstants.DB_REAL_PROJECTION).selection("name=? COLLATE NOCASE  AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{this.mName}).create());
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) throws StoragePermissionMissingException {
            long verify = super.verify(supportSQLiteDatabase);
            if (verify == -1) {
                DefaultLogger.d("galleryAction_Method_CreateAlbum", "album with name found, exist %d", Integer.valueOf(this.mCursor.getCount()));
                this.mResultBundle.putParcelable("album_source", Album.fromCursor(this.mCursor));
                return -103L;
            }
            if (verify == -102) {
                verify = verifyAlbumPath(this.mContext, this.mAlbumPath);
                if (verify > 0) {
                    this.mAlbumPath = regenerateAlbumPath(this.mContext, this.mAlbumPath);
                    return -1L;
                }
            }
            return verify;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) {
            long currentTimeMillis = System.currentTimeMillis();
            ContentValues insertAlbumContentValue = AlbumDataHelper.getInsertAlbumContentValue(this.mContext, this.mName, currentTimeMillis, currentTimeMillis, this.mAlbumPath, 8L, AlbumSortHelper.calculateSortPositionByNormalAlbum(currentTimeMillis));
            long insert = supportSQLiteDatabase.insert("album", 0, insertAlbumContentValue);
            DefaultLogger.d("galleryAction_Method_CreateAlbum", "album creation finish with a id(%s) and albumPath(%s)", Long.valueOf(insert), this.mAlbumPath);
            if (insert > 0) {
                insertAlbumContentValue.put(j.c, Long.valueOf(insert));
                AlbumCacheManager.getInstance().insert(insert, insertAlbumContentValue);
                SyncUtil.requestSync(this.mContext);
                this.mResultBundle.putParcelable("album_source", Album.fromContentValues(insertAlbumContentValue));
                return insert;
            }
            return -101L;
        }

        public static String regenerateAlbumPath(Context context, String str) {
            String str2;
            int i = 0;
            while (true) {
                String valueOf = String.valueOf(System.currentTimeMillis());
                str2 = str + "_" + valueOf.substring(valueOf.length() - 2);
                if (verifyAlbumPath(context, str2) == -1) {
                    break;
                }
                int i2 = i + 1;
                if (i >= 3) {
                    i = i2;
                    break;
                }
                i = i2;
            }
            if (i >= 3) {
                return str + "_" + System.currentTimeMillis();
            }
            return str2;
        }

        public static long verifyAlbumPath(Context context, String str) {
            if (TextUtils.isEmpty(str)) {
                return -100L;
            }
            DBAlbum albumByFilePath = AlbumDataHelper.getAlbumByFilePath(context, str, "(localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))");
            if (albumByFilePath != null && !TextUtils.isEmpty(albumByFilePath.getId())) {
                return Long.parseLong(albumByFilePath.getId());
            }
            return -1L;
        }

        public long getConflictAlbumId(SupportSQLiteDatabase supportSQLiteDatabase) {
            Cursor prepare = prepare(supportSQLiteDatabase);
            if (prepare != null) {
                try {
                    if (prepare.moveToFirst()) {
                        long j = prepare.getLong(0);
                        prepare.close();
                        return j;
                    }
                } catch (Throwable th) {
                    if (prepare != null) {
                        try {
                            prepare.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            long verifyAlbumPath = verifyAlbumPath(this.mContext, this.mAlbumPath);
            if (verifyAlbumPath >= 0) {
                if (prepare != null) {
                    prepare.close();
                }
                return verifyAlbumPath;
            }
            if (prepare != null) {
                prepare.close();
            }
            return -102L;
        }
    }
}
