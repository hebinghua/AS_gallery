package com.miui.gallery.provider.cloudmanager.method.album;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CursorTask;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class DoChangeAlbumSortPositionMethod implements IAlbumMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_ChangeAlbumSortPositionMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long[] longArray = bundle.getLongArray("param_album_id");
        String[] stringArray = bundle.getStringArray("param_sort_position");
        if (longArray == null || stringArray == null || longArray.length == 0 || stringArray.length == 0) {
            return;
        }
        int length = longArray.length;
        long[] jArr = new long[longArray.length];
        int i = 0;
        long j = 0;
        for (int i2 = 0; i2 < length; i2++) {
            long j2 = longArray[i2];
            j += changeAlbumSortPosition(context, supportSQLiteDatabase, mediaManager, arrayList, j2, stringArray[i2]);
            DefaultLogger.d("galleryAction_Method_ChangeAlbumSortPositionMethod", "changeAlbumSortPosition is success,id:[%s]", Long.valueOf(j2));
            if (j > 0) {
                jArr[i] = j2;
                i++;
            }
        }
        if (j <= 0) {
            return;
        }
        bundle2.putLongArray("ids", jArr);
    }

    public static long changeAlbumSortPosition(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long j, String str) throws StoragePermissionMissingException {
        try {
            return new DoChangeAlbumSortPosition(context, arrayList, j, str).run(supportSQLiteDatabase, mediaManager);
        } catch (Exception e) {
            DefaultLogger.w("galleryAction_Method_ChangeAlbumSortPositionMethod", e);
            if (e.getCause() instanceof StoragePermissionMissingException) {
                throw ((StoragePermissionMissingException) e.getCause());
            }
            return -100;
        }
    }

    /* loaded from: classes2.dex */
    public static class DoChangeAlbumSortPosition extends CursorTask {
        public static final String[] QUERY_ALBUM_ITEM_PROJECTION = {j.c};
        public static final String[] QUERY_SHARE_ALBUM_ITEM_PROJECTION = {j.c};
        public boolean isShareAlbum;
        public boolean isVirtualAlbum;
        public long mId;
        public String newValue;

        public DoChangeAlbumSortPosition(Context context, ArrayList<Long> arrayList, long j, String str) {
            super(context, arrayList);
            this.mId = j;
            this.newValue = str;
            this.isShareAlbum = ShareAlbumHelper.isOtherShareAlbumId(j);
            this.isVirtualAlbum = Album.isVirtualAlbum(this.mId);
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            if (this.isVirtualAlbum) {
                return null;
            }
            if (this.isShareAlbum) {
                return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("shareAlbum").columns(QUERY_SHARE_ALBUM_ITEM_PROJECTION).selection("_id=?  AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", new String[]{String.valueOf(ShareAlbumHelper.getOriginalAlbumId(this.mId))}).create());
            }
            return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(QUERY_ALBUM_ITEM_PROJECTION).selection("_id=?  AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{String.valueOf(this.mId)}).create());
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) throws StoragePermissionMissingException {
            if (this.isVirtualAlbum) {
                return -1L;
            }
            return super.verify(supportSQLiteDatabase);
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) {
            String[] strArr;
            String[] strArr2;
            int i = 1;
            ContentValues contentValues = new ContentValues(1);
            contentValues.put("sortInfo", this.newValue);
            if (this.isShareAlbum) {
                strArr = new String[]{String.valueOf(ShareAlbumHelper.getOriginalAlbumId(this.mId))};
                strArr2 = new String[]{String.valueOf(this.mId)};
            } else {
                strArr = new String[]{String.valueOf(this.mId)};
                strArr2 = null;
            }
            String[] strArr3 = strArr2;
            if (this.isVirtualAlbum) {
                GalleryPreferences.Album.setVirtualAlbumSortPosition(this.mId, this.newValue);
            } else {
                i = supportSQLiteDatabase.update(this.isShareAlbum ? "shareAlbum" : "album", 0, contentValues, "_id = ?", strArr);
            }
            AlbumCacheManager albumCacheManager = AlbumCacheManager.getInstance();
            if (strArr3 != null) {
                strArr = strArr3;
            }
            albumCacheManager.update("_id = ?", strArr, contentValues);
            return i;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public String toString() {
            return String.format(Locale.US, "ChangeAlbumSortPosition{id: %d},{sort: %s}", Long.valueOf(this.mId), this.newValue);
        }
    }
}
